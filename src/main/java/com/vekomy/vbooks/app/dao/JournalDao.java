/**
 * com.vekomy.vbooks.app.dao.JournalDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 16, 2013
 */
package com.vekomy.vbooks.app.dao;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.app.dao.base.BaseDao;
import com.vekomy.vbooks.app.request.Journal;
import com.vekomy.vbooks.app.response.Response;
import com.vekomy.vbooks.app.utils.ApplicationConstants;
import com.vekomy.vbooks.app.utils.CRStatus;
import com.vekomy.vbooks.app.utils.DateUtils;
import com.vekomy.vbooks.app.utils.ENotificationTypes;
import com.vekomy.vbooks.hibernate.model.VbInvoiceNoPeriod;
import com.vekomy.vbooks.hibernate.model.VbJournal;
import com.vekomy.vbooks.hibernate.model.VbJournalTypes;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;

/**
 * @author Sudhakar
 * 
 */
public class JournalDao extends BaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(JournalDao.class);
	
	/**
	 * This method is responsible to persist {@link VbJournal} into database.
	 * 
	 * @param request - {@link JournalRequest}
	 * @return response - {@link Response} 
	 * 
	 */
	public Response saveJournal(Journal request) {
		Session session = null;
		Transaction txn = null;
		Response response = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date date = new Date();
			String userName = request.getCreatedBy();
			VbOrganization organization = getOrganization(session, request.getOrganizationId());
			VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
			
			// Populating VbJournal instance.
			String businessName = request.getBusinessName();
			String journalType = request.getJournalType();
			String invoiceNo = new StringBuffer(generateInvoiceNo(organization, journalType))
				.append("#").append(request.getInvoiceNo()).toString();
			VbJournal vbJournalinstance = new VbJournal();
			vbJournalinstance.setAmount(request.getAmount());
			vbJournalinstance.setBusinessName(businessName);
			vbJournalinstance.setInvoiceName(request.getInvoiceName());
			vbJournalinstance.setCreatedBy(userName);
			vbJournalinstance.setCreatedOn(date);
			vbJournalinstance.setModifiedOn(date);
			vbJournalinstance.setDescription(request.getDescription());
			vbJournalinstance.setFlag(new Integer(1));
			vbJournalinstance.setInvoiceNo(invoiceNo);
			vbJournalinstance.setStatus(CRStatus.PENDING.name());
			vbJournalinstance.setJournalType(journalType);
			vbJournalinstance.setVbOrganization(organization);
			vbJournalinstance.setVbSalesBook(salesBook);		
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbJournal: {}", vbJournalinstance);
			}
			session.save(vbJournalinstance);
			
			// Populating VbSystemNotifications instance.
			saveSystemNotification(session, userName, userName, organization, 
					ENotificationTypes.JOURNAL.name(), CRStatus.PENDING.name(), invoiceNo, businessName);
			txn.commit();
			
			// Preparing Response.
			response = new Response();
			response.setMessage("success");
			response.setStatusCode(new Integer(200));
			return response;
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			
			// Preparing Response.
			response = new Response();
			response.setMessage("fail");
			response.setStatusCode(new Integer(500));
			return response;
		} finally {
			if(session != null) {
				session.close();
			}
		}
		
	}
	
	/**
	 * This method is responsible to generate invoiceNo based on {@link VbOrganization} and journal type.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param journalType - {@link String}
	 * @return generatedInvoiceNo - {@link String}
	 * 
	 */
	public String generateInvoiceNo(VbOrganization organization, String journalType) {
		Session session = this.getSession();
		String seperator = ApplicationConstants.SEPERATOR;
		String formattedDate = DateUtils.format2(new Date());
		StringBuffer generatedInvoiceNo = new StringBuffer();
		String invoiceNumberPattern = (String) session.createCriteria(VbJournalTypes.class)
				.setProjection(Projections.property("invoiceNo"))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("journalType", journalType))
				.uniqueResult();
		if(invoiceNumberPattern != null) {
			Query query = session.createQuery("SELECT vb.invoiceNo FROM VbJournal vb WHERE vb.createdOn " +
					"IN(SELECT MAX(vbj.createdOn) FROM VbJournal vbj WHERE vbj.vbOrganization = :vbOrganization AND vbj.invoiceNo LIKE :invoiceNo)")
					.setParameter("vbOrganization", organization)
					.setParameter("invoiceNo", "%"+invoiceNumberPattern+"%");
			String latestInvoiceNo = getSingleResultOrNull(query);
			
			VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session.createCriteria(VbInvoiceNoPeriod.class)
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.eq("txnType", ApplicationConstants.TXN_TYPE_JOURNAL))
					.uniqueResult();
			
			if(latestInvoiceNo == null) {
				if(invoiceNoPeriod != null) {
					generatedInvoiceNo.append(organization.getOrganizationCode()).append(seperator).append(invoiceNumberPattern).append(seperator).append(formattedDate).append(seperator).append(invoiceNoPeriod.getStartValue());
				} else {
					generatedInvoiceNo.append(organization.getOrganizationCode()).append(seperator).append(invoiceNumberPattern).append(seperator).append(formattedDate).append(seperator).append("1");
				}
			} else {
				String[] journalInvoiceNo = latestInvoiceNo.split("#");
				String[] latestInvoiceNoArray = journalInvoiceNo[0].split(seperator);
				latestInvoiceNo = latestInvoiceNoArray[3];
				Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
				String invoiceNumber = resetInvoiceNo(session, invoiceNoPeriod, organization, invoiceNo);
				generatedInvoiceNo.append(organization.getOrganizationCode()).append(seperator).append(invoiceNumberPattern).append(seperator).append(formattedDate).append(seperator).append(invoiceNumber);
			}
			session.close();
		} 
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("Generated invoiceNo: {}", generatedInvoiceNo.toString());
		}
		return generatedInvoiceNo.toString();
	}
}
