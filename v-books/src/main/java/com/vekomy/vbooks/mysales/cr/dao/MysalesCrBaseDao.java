/**
 * com.vekomy.vbooks.mysales.dao.MySalesBaseDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 30 September, 2013
 */
package com.vekomy.vbooks.mysales.cr.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbCustomerAdvanceInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditInfo;
import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbInvoiceNoPeriod;
import com.vekomy.vbooks.hibernate.model.VbJournalChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnChangeRequest;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.OrganizationUtils;
/**
 * This base class is responsible to provide common MySales CR's methods. 
 * 
 * @author Ankit
 *
 * 
 */
public class MysalesCrBaseDao extends BaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(MysalesCrBaseDao.class);
	/**
	 * This method is responsible to generate invoice no for {@link VbCustomerCreditInfo}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return generatedInvoiceNo - {@link String}
	 * 
	 */
	public String generateCustomerCreditInfoInvoiceNumber(VbOrganization organization) {
		Session session = this.getSession();
		String formattedDate = DateUtils.format2(new Date());
		Query query = session.createQuery(
						"SELECT vb.invoiceNo FROM VbCustomerCreditInfo vb WHERE vb.createdOn IN (SELECT MAX(vbdn.createdOn) FROM VbCustomerCreditInfo vbdn "
						+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.invoiceNo LIKE :invoiceNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("invoiceNo", "%CR%");
		String latestInvoiceNo = getSingleResultOrNull(query);
		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType", OrganizationUtils.TXN_TYPE_CUSTOMER_CREDIT))
				.uniqueResult();
		StringBuffer generatedInvoiceNo = new StringBuffer();
		if (latestInvoiceNo == null) {
			if(invoiceNoPeriod != null) {
				generatedInvoiceNo.append(organization.getOrganizationCode()).append(OrganizationUtils.SEPERATOR).append(OrganizationUtils.TXN_TYPE_CUSTOMER_CREDIT).append(OrganizationUtils.SEPERATOR).append(formattedDate).append(OrganizationUtils.SEPERATOR).append(invoiceNoPeriod.getStartValue());
			} else {
				generatedInvoiceNo.append(organization.getOrganizationCode()).append(OrganizationUtils.SEPERATOR).append(OrganizationUtils.TXN_TYPE_CUSTOMER_CREDIT).append(OrganizationUtils.SEPERATOR).append(formattedDate).append(OrganizationUtils.SEPERATOR).append("0001");
			}
			
		} else {
			String[] latestInvoiceNoArray = latestInvoiceNo.split("/");
			latestInvoiceNo = latestInvoiceNoArray[3];
			if(latestInvoiceNo.contains("#"))
			{
				latestInvoiceNo = latestInvoiceNo.substring(0, latestInvoiceNo.indexOf("#")).trim();
			}
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			String newInvoiceNo = resetInvoiceNo(session, invoiceNoPeriod, organization, invoiceNo);
			generatedInvoiceNo = generatedInvoiceNo.append(organization.getOrganizationCode()).append(OrganizationUtils.SEPERATOR).append(OrganizationUtils.TXN_TYPE_CUSTOMER_CREDIT).append(OrganizationUtils.SEPERATOR).append(formattedDate).append(OrganizationUtils.SEPERATOR).append(newInvoiceNo);
		}
		session.close();
		
		if (_logger.isInfoEnabled()) {
			_logger.info("InvoiceNo genertaed for customer credit is: {}", generatedInvoiceNo);
		}
		return generatedInvoiceNo.toString();
	}
	/**
	 * This method is responsible to generate invoice no for {@link VbCustomerAdvanceInfo}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return generatedInvoiceNo - {@link String}
	 * 
	 */
	public String generateCustomerAdvanceInfoInvoiceNumber(VbOrganization organization) {
		Session session = this.getSession();
		String formattedDate = DateUtils.format2(new Date());
		Query query = session.createQuery(
						"SELECT vb.invoiceNo FROM VbCustomerAdvanceInfo vb WHERE vb.createdOn IN (SELECT MAX(vbdn.createdOn) FROM VbCustomerAdvanceInfo vbdn "
						+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.invoiceNo LIKE :invoiceNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("invoiceNo", "%AD%");
		String latestInvoiceNo = getSingleResultOrNull(query);
		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType", OrganizationUtils.TXN_TYPE_ADVANCE))
				.uniqueResult();
		StringBuffer generatedInvoiceNo = new StringBuffer();
		if (latestInvoiceNo == null) {
			if(invoiceNoPeriod != null) {
				generatedInvoiceNo.append(organization.getOrganizationCode()).append(OrganizationUtils.SEPERATOR).append(OrganizationUtils.TXN_TYPE_ADVANCE).append(OrganizationUtils.SEPERATOR).append(formattedDate).append(OrganizationUtils.SEPERATOR).append(invoiceNoPeriod.getStartValue());
			} else {
				generatedInvoiceNo.append(organization.getOrganizationCode()).append(OrganizationUtils.SEPERATOR).append(OrganizationUtils.TXN_TYPE_ADVANCE).append(OrganizationUtils.SEPERATOR).append(formattedDate).append(OrganizationUtils.SEPERATOR).append("0001");
			}
		} else {
			String[] latestInvoiceNoArray = latestInvoiceNo.split("/");
			latestInvoiceNo = latestInvoiceNoArray[3];
			if(latestInvoiceNo.contains("#"))
			{
				latestInvoiceNo = latestInvoiceNo.substring(0, latestInvoiceNo.indexOf("#")).trim();
			}
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			String newInvoiceNo = resetInvoiceNo(session, invoiceNoPeriod, organization, invoiceNo);
			generatedInvoiceNo.append(organization.getOrganizationCode()).append(OrganizationUtils.SEPERATOR).append(OrganizationUtils.TXN_TYPE_ADVANCE).append(OrganizationUtils.SEPERATOR).append(formattedDate).append(OrganizationUtils.SEPERATOR).append(newInvoiceNo);
		}
		session.close();
		
		if (_logger.isInfoEnabled()) {
			_logger.info("InvoiceNo generated for customer advance is: {}", generatedInvoiceNo);
		}
		return generatedInvoiceNo.toString();
	}
	/**
	 * This method is responsible to get created by from my-sales change requests
	 * .  
	 * @param id - {@link Integer}
	 * @return createdBy - {@link String}
	 * 
	 */
	public String getCreatedBy(Integer id, String crType) {
		Session session = this.getSession();
		String createdBy = null;
		if(OrganizationUtils.CR_TYPE_DELIVERY_NOTE.equalsIgnoreCase(crType)) {
			VbDeliveryNoteChangeRequest changeRequest = (VbDeliveryNoteChangeRequest) session.get(VbDeliveryNoteChangeRequest.class, id);
			createdBy = changeRequest.getCreatedBy();
		} else if (OrganizationUtils.CR_TYPE_SALES_RETURNS.equalsIgnoreCase(crType)) {
			VbSalesReturnChangeRequest changeRequest = (VbSalesReturnChangeRequest) session.get(VbSalesReturnChangeRequest.class, id);
			createdBy = changeRequest.getCreatedBy();
		} else if (OrganizationUtils.CR_TYPE_JOURNAL.equalsIgnoreCase(crType)) {
			VbJournalChangeRequest changeRequest = (VbJournalChangeRequest) session.get(VbJournalChangeRequest.class, id);
			createdBy = changeRequest.getCreatedBy();
		} else if (OrganizationUtils.CR_TYPE_DAY_BOOK.equalsIgnoreCase(crType)) {
			VbDayBookChangeRequest changeRequest = (VbDayBookChangeRequest) session.get(VbDayBookChangeRequest.class, id);
			createdBy = changeRequest.getCreatedBy();
		}
		session.close();
		return createdBy;
	}
	/**This method is responsible for calculate minutes difference between Sales creation time and Transaction CR access time.
	 *  
	 * @param currentTime -{@link Date}
	 * @param deliveryNoteCreationTime -{@link Date}
	 * @return totalMinutes -{@link Integer}
	 */
	public Integer calculateMinDiff(Date currentTime,Date deliveryNoteCreationTime){
		//in milliseconds
		long minsDiff=0;
		minsDiff = currentTime.getTime() - deliveryNoteCreationTime.getTime();
	 int minDiff= (int) minsDiff / (60 * 1000) % 60;
	 int hourDiff= (int) minsDiff / (60 * 60 * 1000) % 24;
	 int secondsDiff= (int) minsDiff / 1000 % 60;;
	 int convertHourToMinutes=(int) Math.round(hourDiff * 60);
	 int convertSecondsToMinutes=(int) Math.round(secondsDiff / 60);
	 int totalMinutes=minDiff+convertHourToMinutes+convertSecondsToMinutes;
	 //System.out.println("Minutes Diff:"+totalMinutes);
	 return totalMinutes;
	}
	/**
	 * This method is responsible to get {@link VbEmployee} based on
	 * {@link VbOrganization} and userName.
	 * 
	 * @param session
	 * @param organization
	 * @param userName
	 * @return employee
	 * 
	 */
	public VbEmployee getSalesExecutiveFullNameSalesReturn(String userName,VbOrganization organization) {
		Session session=this.getSession();
		VbEmployee employee = (VbEmployee) session
				.createCriteria(VbEmployee.class)
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("username", userName))
				.uniqueResult();
		return employee;
	}
	/**
	 * This method is used to get the full name of an employee.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return full Name - {@link List}
	 */
	
	public String getEmployeeFullName(String userName,VbOrganization organization){
		Session session=this.getSession();
		VbEmployee employee = (VbEmployee) session
				.createCriteria(VbEmployee.class)
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("username", userName))
				.uniqueResult();
		
		if(employee != null){
			String fullName = employee.getFirstName()+" "+employee.getLastName();
			session.close();
			if (_logger.isDebugEnabled()) {
				_logger.debug("Full Name: {}", fullName);
			}
			return fullName;
		}else{
			return null;
		}
	}
}
