/**
 * com.vekomy.vbooks.mysales.dao.JournalDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 4, 2013
 */
package com.vekomy.vbooks.mysales.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerAdvanceInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditInfo;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbJournal;
import com.vekomy.vbooks.hibernate.model.VbJournalTypes;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.mysales.command.JournalCommand;
import com.vekomy.vbooks.util.CRStatus;
import com.vekomy.vbooks.util.DateUtils;

/**
 * This dao class is responsible to perform CURD operations on {@link VbJournal}.
 * 
 * @author Sudhakar
 *
 */
public class JournalDao extends MySalesBaseDao {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(JournalDao.class);
	/**
	 * String variable holds DAILY_SALES_EXECUTIVE.
	 */
	private static final String DAILY_SALES_EXECUTIVE = "Daily";
	
	/**
	 * This method is responsible to persist {@link VbJournal} into database.
	 * 
	 * @param command - {@link JournalCommand}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * 
	 */
	public void saveJournal(JournalCommand command, VbOrganization organization, String userName){
		Session session = this.getSession();
		Date date = new Date();
		Transaction txn = session.beginTransaction();
		VbJournal vbJournalinstance = new VbJournal();
		vbJournalinstance.setAmount(command.getAmount());
		vbJournalinstance.setBusinessName(command.getBusinessName());
		vbJournalinstance.setInvoiceName(command.getInvoiceName());
		vbJournalinstance.setCreatedBy(userName);
		vbJournalinstance.setCreatedOn(date);
		vbJournalinstance.setModifiedOn(date);
		vbJournalinstance.setDescription(command.getDescription());
		vbJournalinstance.setInvoiceNo(command.getInvoiceNo());
		vbJournalinstance.setStatus(CRStatus.PENDING.name());
		vbJournalinstance.setJournalType(command.getJournalType());
		vbJournalinstance.setVbOrganization(organization);
		vbJournalinstance.setVbSalesBook(getVbSalesBook(organization, userName));		
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbJournal: {}", vbJournalinstance);
		}
		session.save(vbJournalinstance);
		txn.commit();
		session.close();
	}
	
	/**
	 * This is method is responsible to get {@link VbSalesBook} instance.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param salesExecutive - {@link String}
	 * @return salesBook - {@link VbSalesBook}
	 * 
	 */
	private VbSalesBook getVbSalesBook(VbOrganization organization, String salesExecutive) {
		Session session = this.getSession();
		VbSalesBook salesBook = (VbSalesBook) session.createCriteria(VbSalesBook.class)
				.add(Restrictions.eq("salesExecutive", salesExecutive))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("flag", new Integer(1)))
				.uniqueResult();
		
		if(salesBook == null) {
			return null;
		}
		session.close();
		return salesBook;
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
		String generatedInvoiceNo = null;
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
			
			if(latestInvoiceNo == null) {
				generatedInvoiceNo = organization.getOrganizationCode().concat(invoiceNumberPattern).concat("1");
			} else {
				Integer length = invoiceNumberPattern.length();
				latestInvoiceNo = latestInvoiceNo.substring(latestInvoiceNo.indexOf(invoiceNumberPattern)+length, latestInvoiceNo.length());
				Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
				Integer invoiceNumber = ++invoiceNo;
				generatedInvoiceNo = organization.getOrganizationCode().concat(invoiceNumberPattern).concat(invoiceNumber.toString());
			}
			
		}
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("Generated invoiceNo: {}", generatedInvoiceNo);
		}
		return generatedInvoiceNo;
	}
	
	/**
	 * This method is used to get the invoiceName based on the businessName from {@link VbCustomer} .
	 * 
	 * @param businessName - {@link String}
	 * @return invoiceName - {@link String}
	 */
	public String getInvoiceName(String businessName, VbOrganization organization) {
		Session session = this.getSession();
		String invoiceName = (String) session.createCriteria(VbCustomer.class)
				.setProjection(Projections.property("invoiceName"))
				.add(Expression.eq("businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();

		if (invoiceName == null) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for invoiceName: {}", invoiceName);
			}
			return null;
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("Invoice Name associated with businessName is: {}", invoiceName);
		}
		return invoiceName;
	}
	
	/**
	 * This method is responsible to get all the un approved {@link VbJournal} based on {@link VbOrganization}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return journalsList - {@link List}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<VbJournal> getAllJournals(VbOrganization organization) {
		Session session = this.getSession();
		List<VbJournal> journalsList = session.createCriteria(VbJournal.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("status", CRStatus.PENDING.name()))
				.list();
		
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} are unapproved journals available for the organization: {}", journalsList.size(), organization.getName());
		}
		return journalsList;
	}
	
	/**
	 * This method is responsible to approve the raised {@link VbJournal} requests means
	 * updating {@link VbCustomerCreditInfo} if the business name have any credit
	 * or saving the amount in {@link VbCustomerAdvanceInfo}.
	 * 
	 * @param command - {@link JournalCommand}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return isApproved - {@link Boolean}
	 * 
	 */
	public Boolean approveOrDeclineJournal(Integer id, String status, VbOrganization organization, String userName) {
		Boolean isApproved = Boolean.FALSE;
		Date date = new Date();
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbJournal journal = (VbJournal) session.get(VbJournal.class, id);
		if(journal != null) {
			journal.setModifiedBy(userName);
			journal.setModifiedOn(date);
			
			if(CRStatus.APPROVED.name().equalsIgnoreCase(status)) {
				Float amount = journal.getAmount();
				Query query = null;
				VbCustomerCreditInfo creditInfo = null;
				String businessName = journal.getBusinessName();
				while (amount != 0) {
					// Updating previous credit of the businessName.
					query = session.createQuery("FROM VbCustomerCreditInfo vb WHERE vb.createdOn IN ("
									+ "SELECT MIN(vbc.createdOn) FROM VbCustomerCreditInfo vbc WHERE "
									+ "vbc.businessName = :businessName AND vbc.vbOrganization = :vbOrganization AND vbc.due > :due)")
									.setParameter("businessName", businessName)
									.setParameter("vbOrganization", organization)
									.setParameter("due", new Float("0"));
					creditInfo = getSingleResultOrNull(query);
					String invoiceNo = journal.getInvoiceNo();
					if(creditInfo != null) {
						creditInfo.setModifiedBy(userName);
						creditInfo.setModifiedOn(date);
						String existingInvoiceNo = creditInfo.getDebitTo();
						if(existingInvoiceNo == null) {
							creditInfo.setDebitTo(invoiceNo);
						} else {
							creditInfo.setDebitTo(existingInvoiceNo.concat(",").concat(invoiceNo));
						}
						Float due = creditInfo.getDue();
						if(amount < due) {
							creditInfo.setDue(due - amount);
							amount = new Float(0);
						} else {
							creditInfo.setDue(new Float(0));
							amount = amount - due;
						}
						
						if(_logger.isDebugEnabled()) {
							_logger.debug("Updating VbCustomerCreditInfo: {}", creditInfo);
						}
						session.update(creditInfo);
					} else {
						// Saving the remaining balance as advance for the businessName.
						saveCustomerAdvanceInfo(session, businessName, invoiceNo, organization, amount, userName);
						amount = new Float(0);
					}
				}
				
				journal.setStatus(CRStatus.APPROVED.name());
				isApproved = Boolean.TRUE;
			} else {
				journal.setStatus(CRStatus.DECLINE.name());
				isApproved = Boolean.FALSE;
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating VbJournal: {}", journal);
			}
			session.update(journal);
		}
		txn.commit();
		session.close();
		return isApproved;
	}
	
	/**
	 * This method is responsible to persist {@link VbCustomerAdvanceInfo}, if
	 * there is any balance available after crediting all the
	 * {@link VbCustomerCreditInfo} based on businessName and
	 * {@link VbOrganization} or persisting advance directly.
	 * 
	 * @param session - {@link Session}
	 * @param businessName - {@link String}
	 * @param invoiceNo - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param balance - {@link Float}
	 * 
	 */
	private void saveCustomerAdvanceInfo(Session session, String businessName,
			String invoiceNo, VbOrganization organization, Float balance, String userName) {
		VbCustomerAdvanceInfo advanceInfo = new VbCustomerAdvanceInfo();
		Date date = new Date();
		if (balance < 0) {
			advanceInfo.setAdvance(-(balance));
			advanceInfo.setBalance(-(balance));
		} else {
			advanceInfo.setAdvance(balance);
			advanceInfo.setBalance(balance);
		}
		advanceInfo.setBusinessName(businessName);
		advanceInfo.setCreatedBy(userName);
		advanceInfo.setCreatedOn(date);
		advanceInfo.setModifiedOn(date);
		advanceInfo.setCreditFrom(invoiceNo);
		advanceInfo.setVbOrganization(organization);

		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbCustomerAdvanceInfo: {}", advanceInfo);
		}
		session.save(advanceInfo);
	}
	/**
	 * This method is responsible to retrieve all the {@link VbJournal} from
	 * DB on criteria.
	 * 
	 * @param JournalCommand - {@link command}
	 * @param username - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return journalList - {@link List}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<VbJournal> getJournal(JournalCommand command, String username,
			VbOrganization organization) {
		Session session = this.getSession();
		Criteria criteria=session.createCriteria(VbJournal.class).createAlias("vbSalesBook","vb");;
		VbSalesBook salesBook=getSalesBookForSearch(session,organization, username);
		if(salesBook != null){
			if(salesBook.getAllotmentType().equals(DAILY_SALES_EXECUTIVE)){
				criteria.add(Restrictions.ge("createdOn", DateUtils.getBeforeTwoDays(new Date())));
			}else{
				
				Date createdDate = salesBook.getCreatedOn();
				List<Integer> listIds=session.createCriteria(VbSalesBook.class)
						              .setProjection(Projections.property("id"))
						              .add(Restrictions.between("createdOn",createdDate, DateUtils.getEndTimeStamp(new Date())))
						              .list();
				criteria.add(Expression.in("vb.id",listIds));
			}
		}else{
			criteria.add(Restrictions.ge("createdOn", DateUtils.getBeforeTwoDays(new Date())));
		}
		if (organization != null) {
			criteria.add(Restrictions.eq("vbOrganization", organization));
		}
		if (command != null) {
			if(!command.getJournalType().isEmpty()||command.getJournalType() != null){
				criteria.add(Restrictions.like("journalType",command.getJournalType(), MatchMode.START).ignoreCase());
			}
			if (!command.getBusinessName().isEmpty()) {
				criteria.add(Restrictions.like("businessName", command.getBusinessName(), MatchMode.START).ignoreCase());
			}
			if (command.getCreatedOn() != null) {
				criteria.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(command.getCreatedOn()), DateUtils.getEndTimeStamp(command.getCreatedOn())));
			}
		}
		criteria.addOrder(Order.asc("createdOn"));
		List<VbJournal> journalList=criteria.list();
		session.close();
		return journalList;
	}
	/**
	 * This method is responsible to get {@link VbSalesBook} based on
	 * {@link VbOrganization} and userName.
	 * 
	 * @param session - {@link Session}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return salesBook - {@link VbSalesBook}
	 * 
	 */
	private VbSalesBook getSalesBookForSearch(Session session, VbOrganization organization, String userName) {
		Query query = session
				.createQuery(
						"FROM VbSalesBook vb WHERE  (vb.createdOn,vb.cycleId) IN (SELECT MIN(vbs.createdOn),MAX(vbs.cycleId) FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutiveName AND vb.vbOrganization = :vbOrganization )")
				.setParameter("vbOrganization", organization)
				.setParameter("salesExecutiveName", userName);
		VbSalesBook salesBook = getSingleResultOrNull(query);
		return salesBook;
	}
	
	/**
	 * This method is responsible to get {@link VbJournal} based on
	 * journal id and userName.
	 * 
	 * @param journalId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return vbJournal - {@link VbJournal}
	 * 
	 */
	public VbJournal getJournalById(Integer journalId, VbOrganization organization, String userName) {
		Session session = this.getSession();
		VbJournal vbJournal=(VbJournal)session.get(VbJournal.class, journalId);
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Fetching VbJournal: {}", vbJournal);
		}
		return vbJournal;
	}
	
	/**
	 * This method is responsible to get {@link VbEmployee} based on
	 * {@link VbOrganization} and userName.
	 * 
	 * @param userName
	 * @param organization
	 * 
	 */
	public VbEmployee getSalesExecutiveFullName(String userName,VbOrganization organization) {
		Session session=this.getSession();
		VbEmployee employee = (VbEmployee) session
				.createCriteria(VbEmployee.class)
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("username", userName))
				.uniqueResult();
		session.close();
		
		return employee;
	}
	
	/**
	 * This method is responsible to get createdBy of {@link VbEmployee}.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return createdBy - {@link String}
	 * 
	 */
	public String getCreatedBy(String userName, VbOrganization organization) {
		String createdBy = null;
		VbEmployee employee = getSalesExecutiveFullName(userName, organization);
		if(employee != null) {
			createdBy = employee.getCreatedBy();
		}
		
		return createdBy;
	}
	
	/**
	 * This method is responsible to get createdBy of {@link VbJournal}.
	 * 
	 * @param id - {@link Integer}
	 * @return createdBy - {@link String}
	 * 
	 */
	public String getCreatedBy(Integer id) {
		String createdBy = null;
		Session session = this.getSession();
		if(id != null) {
			VbJournal journal = (VbJournal) session.get(VbJournal.class, id);
			createdBy = journal.getCreatedBy();
		}
		session.close();
		
		return createdBy;
	}
}
