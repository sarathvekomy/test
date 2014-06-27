/**
 * com.vekomy.vbooks.customer.controller.CustomerChangeRequestController.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: may 9, 2013
 */
package com.vekomy.vbooks.customer.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
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

import com.vekomy.vbooks.customer.command.CustomerCRBusinessNamesHistoryResult;
import com.vekomy.vbooks.customer.command.CustomerChangeRequestCommand;
import com.vekomy.vbooks.customer.command.CustomerChangeRequestHistoryResult;
import com.vekomy.vbooks.customer.command.CustomerResult;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbCustomerChangeRequestDetails;
import com.vekomy.vbooks.hibernate.model.VbCustomerDetail;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbEmployeeCustomer;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.util.CRStatus;
import com.vekomy.vbooks.util.ENotificationTypes;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.StringUtil;

/**
 * This Class Is Responsible To Perform Database-Related Operations.
 * 
 * @author priyanka
 *
 */
public class CustomerChangeRequestDao extends BaseDao {
	/**
	 * Logger variable holdes _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(CustomerDao.class);

	/**
	 * This Method is Responsible for Retrieving The CustomerDetails In Case Of CustomerChangeRequest.
	 * 
	 * @param command - {@link CustomerChangeRequestCommand}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return customerDetail - {@link VbCustomerDetail}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public VbCustomerDetail getCustomerDetails(CustomerChangeRequestCommand command,VbOrganization vbOrganization) throws DataAccessException {
		Session session = this.getSession();
		VbCustomerDetail customerDetail = (VbCustomerDetail) session.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.add(Restrictions.eq("customer.businessName", command.getBusinessName()))
				.add(Restrictions.eq("customer.customerName", command.getCustomerName()))
				.add(Restrictions.eq("customer.gender", command.getGender()))
				.add(Restrictions.eq("customer.vbOrganization", vbOrganization))
				.uniqueResult();
		session.close();
		
		if(customerDetail != null){
			if(_logger.isDebugEnabled()){
				_logger.debug("customerDetail: {}", customerDetail);
			}
			return customerDetail;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if (_logger.isInfoEnabled()) {
				_logger.info(message);
			}
			throw new DataAccessException(message);
		}
	}
	
	/**
	 * This Method is Responsible For Saving CustomerChangeRequest Details.
	 * 
	 * @param customer - {@link CustomerChangeRequestCommand}
	 * @param customerChangeRequestCommand - {@link CustomerChangeRequestCommand}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void saveCustomerCrDetails(CustomerChangeRequestCommand customer,
			CustomerChangeRequestCommand customerChangeRequestCommand,
			VbOrganization organization, String userName) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date createdDate = new Date();
			String businessName = customer.getBusinessName();
			// Persisting customer change request basic info.
			VbCustomerChangeRequest vbCustomer = new VbCustomerChangeRequest();
			vbCustomer.setBusinessName(businessName);
			vbCustomer.setCrType(customer.getCrType());
			vbCustomer.setGender(customer.getGender());
			vbCustomer.setVbOrganization(organization);
			vbCustomer.setCreatedOn(createdDate);
			vbCustomer.setCreatedBy(userName);
			vbCustomer.setModifiedOn(new Date());
			vbCustomer.setStatus(CRStatus.PENDING.name());
			vbCustomer.setFlag(new Integer(1));

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting customer: {}", vbCustomer);
			}
			session.save(vbCustomer);
			
			// Persisting customer change request details.
			VbCustomerChangeRequestDetails vbCustomerCrDetail = new VbCustomerChangeRequestDetails();
			vbCustomerCrDetail.setCustomerName(customer.getCustomerName());
			vbCustomerCrDetail.setInvoiceName(customerChangeRequestCommand.getInvoiceName());
			vbCustomerCrDetail.setAddressLine1(customerChangeRequestCommand.getAddressLine1());
			String addressLine2 = customerChangeRequestCommand.getAddressLine2();
			if (!StringUtils.isEmpty(addressLine2)) {
				vbCustomerCrDetail.setAddressLine2(addressLine2);
			}
			vbCustomerCrDetail.setCity(customerChangeRequestCommand.getCity());
			String landMark = customerChangeRequestCommand.getLandmark();
			if (!StringUtils.isEmpty(landMark)) {
				vbCustomerCrDetail.setLandmark(landMark);
			}
			vbCustomerCrDetail.setLocality(customerChangeRequestCommand.getLocality());
			vbCustomerCrDetail.setMobile(customerChangeRequestCommand.getMobile());
			vbCustomerCrDetail.setZipcode(customerChangeRequestCommand.getZipcode());
			vbCustomerCrDetail.setEmail(customerChangeRequestCommand.getEmail());
			String directLine = customerChangeRequestCommand.getDirectLine();
			if (!StringUtils.isEmpty(directLine)) {
				vbCustomerCrDetail.setDirectLine(directLine);
			}
			String region = customerChangeRequestCommand.getRegion();
			if(!StringUtils.isEmpty(region)){
				vbCustomerCrDetail.setRegion(region);
			}
			String alternateMobile = customerChangeRequestCommand.getAlternateMobile();
			if(!StringUtils.isEmpty(alternateMobile)){
				vbCustomerCrDetail.setAlternateMobile(alternateMobile);
			}
			vbCustomerCrDetail.setState(customerChangeRequestCommand.getState());
			vbCustomerCrDetail.setCreatedBy(customerChangeRequestCommand.getCreatedBy());
			vbCustomerCrDetail.setCreatedDate(createdDate);
			vbCustomerCrDetail.setModifiedDate(createdDate);
			vbCustomerCrDetail.setModifiedBy(userName);
			vbCustomerCrDetail.setVbCustomerChangeRequest(vbCustomer);
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting CustomerDetail: {}", vbCustomerCrDetail);
			}
			session.save(vbCustomerCrDetail);
			
			// For Android Application.
			saveSystemNotificationForAndroid(session, userName, userName,
					organization, ENotificationTypes.CUSTOMER_CR.name(),
					CRStatus.PENDING.name(), null, businessName);
			txn.commit();
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String errorMsg = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}


	/**
	 * This Method Is Responsible to display All Records From VbCustomerChangerequestTable On Page Load.
	 * 
	 * @param customerCrCommand - {@link CustomerChangeRequestCommand}
	 * @return customerDetailsList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */ 
	@SuppressWarnings({ "unchecked" })
	public List<CustomerResult> getCrDetails(CustomerChangeRequestCommand customerCrCommand, VbOrganization organization) throws DataAccessException {
		Session session=this.getSession();
		CustomerResult customerResult = null;
		List<VbCustomerChangeRequestDetails> crDetailsList = session.createCriteria(VbCustomerChangeRequestDetails.class)
				.createAlias("vbCustomerChangeRequest", "customerChangeRequest")
				.add(Expression.eq("customerChangeRequest.vbOrganization", organization))
				.add(Expression.eq("customerChangeRequest.status", CRStatus.PENDING.name()))
				.add(Expression.eq("customerChangeRequest.flag", new Integer(1)))
				.list();
		
		if(!(crDetailsList.isEmpty())) {
			List<CustomerResult> customerDetailsList = new ArrayList<CustomerResult>();
			for (VbCustomerChangeRequestDetails customerChangeRequestDetails : crDetailsList) {
				VbCustomerChangeRequest crRequest = customerChangeRequestDetails.getVbCustomerChangeRequest();
				customerResult = new CustomerResult();
				customerResult.setId(crRequest.getId());
				customerResult.setBusinessName(crRequest.getBusinessName());
				customerResult.setCreatedBy(crRequest.getCreatedBy());
				customerResult.setLocality(customerChangeRequestDetails.getLocality());
				customerResult.setCreatedDate(crRequest.getCreatedOn());
				Boolean crType = crRequest.getCrType();
				if(crType == Boolean.TRUE){
					customerResult.setCrType("Existed Customer");
				} else {
					customerResult.setCrType("New Customer");
				}
				customerResult.setMobile(customerChangeRequestDetails.getMobile());
				String alternateMobile = customerChangeRequestDetails.getAlternateMobile(); 
				if(!StringUtils.isEmpty(alternateMobile)){
					customerResult.setAlternateMobile(alternateMobile);
				}
				String email = customerChangeRequestDetails.getEmail();
				if(!StringUtils.isEmpty(email)){
					customerResult.setEmail(email);
				}
				customerDetailsList.add(customerResult);
			}
			session.close();

			if (_logger.isInfoEnabled()) {
				_logger.info("{} pending customer CRs have been found for the organization: {}", customerDetailsList.size(), organization.getName());
			}
			return customerDetailsList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}


/**
 * This Method is To Approve The Customer Based On crType.and To Remove Data From Temp Tables.
 * 
 * @param id - {@link Integer}
 * @param organization - {@link VbOrganization}
 * @param userName - {@link String}
 * @param customerCrCommand - {@link CustomerChangeRequestCommand}
 * @throws DataAccessException - {@link DataAccessException}
 */
	public void approveOrDeclineCustomerCr(Integer id, String status,
			VbOrganization organization, String userName,
			CustomerChangeRequestCommand customerCrCommand)
			throws DataAccessException {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		Date date = new Date();
		VbCustomerChangeRequestDetails changeRequestDetails = (VbCustomerChangeRequestDetails) session
				.createCriteria(VbCustomerChangeRequestDetails.class)
				.createAlias("vbCustomerChangeRequest", "customerChangeRequest")
				.add(Expression.eq("customerChangeRequest.id", id))
				.add(Expression.eq("customerChangeRequest.vbOrganization", organization))
				.uniqueResult();
		VbCustomerChangeRequest customerChangeRequest = changeRequestDetails.getVbCustomerChangeRequest();
		if (customerChangeRequest != null) {
			try {
				String businessName = customerChangeRequest.getBusinessName();
				Integer creditOverdues = 0;
				float creditLimit = Float.parseFloat(customerCrCommand.getCreditLimit());
				if (customerCrCommand.getCreditOverdueDays() == "") {
					creditOverdues = new Integer(0);
				} else {
					creditOverdues = Integer.parseInt(customerCrCommand.getCreditOverdueDays());
				}
				customerChangeRequest.setModifiedBy(userName);
				customerChangeRequest.setModifiedOn(date);
				customerChangeRequest.setFlag(new Integer(0));
				if (CRStatus.APPROVED.name().equalsIgnoreCase(status)) {
					customerChangeRequest.setStatus(CRStatus.APPROVED.name());
					
					if (customerChangeRequest.getCrType() == Boolean.TRUE) {
						// Updating existing customer's CR
						VbCustomerDetail customerDetails = (VbCustomerDetail) session.createCriteria(VbCustomerDetail.class)
								.createAlias("vbCustomer", "customer")
								.add(Expression.eq("customer.businessName", businessName))
								.add(Expression.eq("customer.vbOrganization", organization))
								.uniqueResult();
						VbCustomer customer = customerDetails.getVbCustomer();
						if (customerDetails != null) {
							customer.setInvoiceName(changeRequestDetails.getInvoiceName());
							customer.setCustomerName(changeRequestDetails.getCustomerName());
							customer.setVbOrganization(organization);
							customer.setBusinessName(customerChangeRequest.getBusinessName());
							customer.setCreditLimit(creditLimit);
							customer.setCreditOverdueDays(creditOverdues);
							customer.setModifiedOn(date);
							customer.setState(OrganizationUtils.CUSTOMER_ENABLED);
							customer.setCreatedOn(date);
							customerDetails.setAddressLine1(changeRequestDetails.getAddressLine1());
							customerDetails.setAddressLine2(changeRequestDetails.getAddressLine2());
							customerDetails.setCity(changeRequestDetails.getCity());
							customerDetails.setEmail(changeRequestDetails.getEmail());
							if (changeRequestDetails.getLandmark() != null) {
								customerDetails.setLandmark(changeRequestDetails.getLandmark());
							}
							if (changeRequestDetails.getAlternateMobile() != null) {
								customerDetails.setAlternateMobile(changeRequestDetails.getAlternateMobile());
							}
							customerDetails.setLocality(changeRequestDetails.getLocality());
							customerDetails.setRegion(changeRequestDetails.getRegion());
							customerDetails.setMobile(changeRequestDetails.getMobile());
							customerDetails.setZipcode(changeRequestDetails.getZipcode());
							customerDetails.setDirectLine(changeRequestDetails.getDirectLine());
							customerDetails.setState(changeRequestDetails.getState());

							if (_logger.isDebugEnabled()) {
								_logger.debug("Updating VbCustomer: {}", customer);
							}
							session.update(customer);
							session.update(customerDetails);
							session.update(customerChangeRequest);
						}
					} else if (customerChangeRequest.getCrType() == Boolean.FALSE) {
						// Adding new customer's CR
						VbCustomerDetail customerDetails = new VbCustomerDetail();
						VbCustomer customer = new VbCustomer();
						if (customerDetails != null) {
							customer.setBusinessName(customerChangeRequest.getBusinessName());
							customer.setInvoiceName(changeRequestDetails.getInvoiceName());
							customer.setCustomerName(changeRequestDetails.getCustomerName());
							customer.setGender(customerChangeRequest.getGender());
							customer.setCreatedBy(userName);
							customer.setModifiedBy(userName);
							customer.setCreatedOn(date);
							customer.setState(OrganizationUtils.CUSTOMER_ENABLED);
							customer.setModifiedOn(date);
							customer.setVbOrganization(organization);
							customerDetails.setAddressLine1(changeRequestDetails.getAddressLine1());
							customerDetails.setAddressLine2(changeRequestDetails.getAddressLine2());
							customerDetails.setAlternateMobile(changeRequestDetails.getAlternateMobile());
							customerDetails.setCity(changeRequestDetails.getCity());
							customerDetails.setEmail(changeRequestDetails.getEmail());
							customerDetails.setRegion(changeRequestDetails.getRegion());
							customerDetails.setLandmark(changeRequestDetails.getLandmark());
							customerDetails.setLocality(changeRequestDetails.getLocality());
							customerDetails.setMobile(changeRequestDetails.getMobile());
							customerDetails.setZipcode(changeRequestDetails.getZipcode());
							customerDetails.setState(changeRequestDetails.getState());
							customerDetails.setVbCustomer(customer);
							customer.setCreditLimit(creditLimit);
							customer.setCreditOverdueDays(creditOverdues);
							if (_logger.isDebugEnabled()) {
								_logger.debug("Saving VbCustomer: {}", customer);
							}
							session.save(customer);
							session.save(customerDetails);
							session.update(customerChangeRequest);
						}

						// Assigning customers to Sales executive after approved.
						VbEmployeeCustomer employeeCustomer = new VbEmployeeCustomer();
						if (employeeCustomer != null) {
							VbEmployee employee = (VbEmployee) session.createCriteria(VbEmployee.class)
									.add(Restrictions.eq("username", customerChangeRequest.getCreatedBy()))
									.add(Restrictions.eq("vbOrganization", organization))
									.uniqueResult();
							employeeCustomer.setVbCustomer(customer);
							employeeCustomer.setVbEmployee(employee);
							employeeCustomer.setCreatedBy(userName);
							employeeCustomer.setCreatedOn(date);
							employeeCustomer.setModifiedOn(date);
							employeeCustomer.setVbOrganization(organization);

							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbEmployeeCustomer: {}", employeeCustomer);
							}
							session.save(employeeCustomer);
						}
					}
				} else {
					customerChangeRequest.setStatus(CRStatus.DECLINE.name());

					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating customerChangeRequest: {}", customerChangeRequest);
					}
					session.update(customerChangeRequest);
				}
				
				// For Android Application.
				saveSystemNotificationForAndroid(session, userName,
						customerChangeRequest.getCreatedBy(), organization,
						ENotificationTypes.CUSTOMER_CR.name(), status, null,
						businessName);
				txn.commit();
			} catch (HibernateException exception) {
				if(txn != null) {
					txn.rollback();
				}
				String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);

				if (_logger.isWarnEnabled()) {
					_logger.warn(message);
				}
				throw new DataAccessException(message);
			} finally {
				if(session != null) {
					session.close();
				}
			}
		} else {
			if(session != null) {
				session.close();
			}
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is Responsible To Search Records Based On The Given Criteria.
	 * 
	 * @param customerCommand - {@link CustomerChangeRequestCommand}
	 * @return customerDetailsList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerResult> searchCustomerCr(
			CustomerChangeRequestCommand customerCommand,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbCustomerChangeRequestDetails vbCustomerDetail = null;
		Criteria criteria = session.createCriteria(VbCustomerChangeRequest.class);
		Criteria criteria1 = session.createCriteria(VbCustomerChangeRequestDetails.class);
		if (customerCommand != null) {
			String customerName = customerCommand.getCustomerName();
			String businessName = customerCommand.getBusinessName();
			String invoiceName = customerCommand.getInvoiceName();
			if (!customerName.isEmpty()) {
				criteria1.add(Expression.like("customerName", customerName, MatchMode.START).ignoreCase());
			}
			if (!businessName.isEmpty()) {
				criteria.add(Expression.like("businessName", businessName, MatchMode.START).ignoreCase());
			}
			if (!invoiceName.isEmpty()) {
				criteria1.add(Expression.like("invoiceName", invoiceName, MatchMode.START).ignoreCase());
			}
		}
		criteria1.addOrder(Order.asc("customerName"));
		List<VbCustomerChangeRequest> customerList = criteria.list();
		if(!customerList.isEmpty()) {
			List<CustomerResult> customerDetailsList = new ArrayList<CustomerResult>();
			CustomerResult customerResult = null;
			for (VbCustomerChangeRequest customerChangeRequest : customerList) {
				customerResult = new CustomerResult();
				customerResult.setId(customerChangeRequest.getId());
				customerResult.setBusinessName(customerChangeRequest.getBusinessName());
				Boolean crType = customerChangeRequest.getCrType();
				if(crType == Boolean.TRUE){
					customerResult.setCrType("Existed Customer");
				} else {
					customerResult.setCrType("New Customer");
				}
				vbCustomerDetail = (VbCustomerChangeRequestDetails) session.createCriteria(VbCustomerChangeRequestDetails.class)
						.createAlias("vbCustomerChangeRequest", "customerChangeRequest")
						.add(Expression.eq("customerChangeRequest.id", customerChangeRequest.getId()))
						.add(Expression.eq("customerChangeRequest.vbOrganization", organization))
						.uniqueResult();
				customerResult.setMobile(vbCustomerDetail.getMobile());
				if(vbCustomerDetail.getAlternateMobile()!= null){
					customerResult.setAlternateMobile(vbCustomerDetail.getAlternateMobile());
				}
				customerResult.setCreatedBy(customerChangeRequest.getCreatedBy());
				if(vbCustomerDetail.getEmail()!=null){
					customerResult.setEmail(vbCustomerDetail.getEmail());
				}
				customerDetailsList.add(customerResult);
			}
			session.close();

			if (_logger.isDebugEnabled()) {
				_logger.debug("{} customer change requests have been found for the organization: {}", customerDetailsList.size(), organization.getName());
			}
			return customerDetailsList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}
	
	/**
	 * This Method is Responsible To retrieve Records Based On the Given Id.
	 * 
	 * @param id
	 * @return instance - {@link VbCustomerChangeRequest}
	 */
	public VbCustomerChangeRequest getCustomerCr(int id , VbOrganization vbOrganization) {
		Session session = this.getSession();
		
		VbCustomerChangeRequest instance = (VbCustomerChangeRequest) session.createCriteria(VbCustomerChangeRequest.class)
				.add(Expression.eq("id", id))
				.setFetchMode("vbCustomerChangeRequestDetailses", FetchMode.JOIN)
				.add(Expression.eq("vbOrganization", vbOrganization))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.uniqueResult();
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Vbcustomer: {}", instance);
		}
		return instance;
	}

	/**
	 * This method is responsible for checking unique customer change request type to avoid duplication.
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @param customerCrCommand - {@link CustomerChangeRequestCommand}
	 * @return isApproved - {@link String}
	 */
	public String checkCustomerCr(VbOrganization vbOrganization, String customerBusinessName) {
		Session session = this.getSession();
		String isApproved = null;
		VbCustomerChangeRequest instance = (VbCustomerChangeRequest) session.createCriteria(VbCustomerChangeRequest.class)
				.add(Restrictions.eq("businessName", customerBusinessName))
				.add(Restrictions.eq("vbOrganization", vbOrganization))
				.add(Restrictions.eq("status", CRStatus.PENDING.name()))
				.uniqueResult();
		session.close();
		
		if(instance != null){
			isApproved = "n";
		} else{
			isApproved = "y";
		}
		return isApproved;
	}

	/**
	 * This method is responsible to get {@link VbCustomerChangeRequest} based on id.
	 * 
	 * @param id - {@link Integer}
	 * @return createdBy - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public VbCustomerChangeRequest getVbCustomerChangeRequestById(Integer id) throws DataAccessException {
		Session session = this.getSession();
		VbCustomerChangeRequest vbCustomerChangeRequest = (VbCustomerChangeRequest) session.get(VbCustomerChangeRequest.class, id);
		session.close();
		if(vbCustomerChangeRequest != null) {
			return vbCustomerChangeRequest;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}
	
	/**
	 * This method is responsible for getting Approved,Declined,Pending count of
	 * Customer CR for New and Existing Customer CR
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return historyResults -{@link CustomerChangeRequestHistoryResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerChangeRequestHistoryResult> getCustomerChangeRequestHistory(
			VbOrganization organization, String userName)
			throws DataAccessException {
		Session session = this.getSession();
		// Approved, Decline, Pending status count of New Customer CR
		List<CustomerChangeRequestHistoryResult> historyResultList = new ArrayList<CustomerChangeRequestHistoryResult>();
		List<Object[]> newCustomerResultList = session.createCriteria(VbCustomerChangeRequest.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("crType"))
						.add(Projections.property("status"))
						.add(Projections.rowCount())
						.add(Projections.groupProperty("status")))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("crType", Boolean.FALSE))
				.list();
		
		if(!newCustomerResultList.isEmpty()) {
			CustomerChangeRequestHistoryResult historyResult = null;
			for (Object[] objects : newCustomerResultList) {
				historyResult = new CustomerChangeRequestHistoryResult();
				Boolean customerCRType = (Boolean) objects[0];
				String customerCRStatus = (String) objects[1];
				Integer count = (Integer) objects[2];
				if (Boolean.FALSE == customerCRType) {
					if (CRStatus.APPROVED.name().equalsIgnoreCase(customerCRStatus)) {
						historyResult.setCustomerType("New Customer");
						historyResult.setCustomerChangeStatus(customerCRStatus);
						historyResult.setApprovedNewCount(count);
					} else if (CRStatus.DECLINE.name().equalsIgnoreCase(customerCRStatus)) {
						historyResult.setCustomerType("New Customer");
						historyResult.setCustomerChangeStatus(customerCRStatus);
						historyResult.setDeclinedNewCount(count);
					} else if (CRStatus.PENDING.name().equalsIgnoreCase(customerCRStatus)) {
						historyResult.setCustomerType("New Customer");
						historyResult.setCustomerChangeStatus(customerCRStatus);
						historyResult.setPendingNewCount(count);
					}
				}
				historyResultList.add(historyResult);
			}
		}
		
		// Approved, Decline, Pending status count of existing customer
		List<Object[]> existingCustomerResultList = session.createCriteria(VbCustomerChangeRequest.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("crType"))
						.add(Projections.property("status"))
						.add(Projections.rowCount())
						.add(Projections.groupProperty("status")))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("crType", Boolean.TRUE))
				.list();
		if(!existingCustomerResultList.isEmpty()) {
			Boolean customerCRType = null;
			String customerCRStatus = null;
			Integer count = null;
			CustomerChangeRequestHistoryResult historyResult = null;
			for (Object[] objects : existingCustomerResultList) {
				historyResult = new CustomerChangeRequestHistoryResult();
				customerCRType = (Boolean) objects[0];
				customerCRStatus = (String) objects[1];
				count = (Integer) objects[2];
				if (Boolean.TRUE == customerCRType) {
					if (CRStatus.APPROVED.name().equalsIgnoreCase(customerCRStatus)) {
						historyResult.setCustomerType("Existing Customer");
						historyResult.setCustomerChangeStatus(customerCRStatus);
						historyResult.setApprovedExistingCount(count);
					} else if (CRStatus.DECLINE.name().equalsIgnoreCase(customerCRStatus)) {
						historyResult.setCustomerType("Existing Customer");
						historyResult.setCustomerChangeStatus(customerCRStatus);
						historyResult.setDeclinedExistingCount(count);
					} else if (CRStatus.PENDING.name().equalsIgnoreCase(customerCRStatus)) {
						historyResult.setCustomerType("Existing Customer");
						historyResult.setCustomerChangeStatus(customerCRStatus);
						historyResult.setPendingExistingCount(count);
					}
				}
				historyResultList.add(historyResult);
			}
		}
		session.close();
		
		if (!historyResultList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", historyResultList.size());
			}
			return historyResultList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to fetch BusinessNames from
	 * {@link VbCustomerChangeRequest} based on status and customerType
	 * 
	 * @param customerType - {@link String}
	 * @param status - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return businessNamesHistoryResults - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerCRBusinessNamesHistoryResult> getCustomerCRBusinessNamesHistory(
			String customerType, String status, VbOrganization organization,
			String userName) throws DataAccessException {
		Session session = this.getSession();
		Boolean crType;
		if ("New".equals(customerType)) {
			crType = Boolean.FALSE;
		} else {
			crType = Boolean.TRUE;
		}
		List<Object[]> resultList = session.createCriteria(VbCustomerChangeRequest.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("id"))
						.add(Projections.property("businessName"))
						.add(Projections.property("status"))
						.add(Projections.property("createdBy"))
						.add(Projections.property("createdOn"))
						.add(Projections.property("modifiedBy"))
						.add(Projections.property("modifiedOn")))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("crType", crType))
				.add(Restrictions.eq("status", status))
				.list();
		session.close();
		
		if (!resultList.isEmpty()) {
			Integer customerId = null;
			String customerBusinessNName = null;
			String customerStatus = null;
			String createdBy = null;
			Date createdOn = null;
			String modifiedBy = null;
			Date modifiedOn = null;
			CustomerCRBusinessNamesHistoryResult businessNamesHistoryResult = null;
			List<CustomerCRBusinessNamesHistoryResult> businessNamesHistoryResults = 
					new ArrayList<CustomerCRBusinessNamesHistoryResult>();
			for (Object[] objects : resultList) {
				customerId = (Integer) objects[0];
				customerBusinessNName = (String) objects[1];
				customerStatus = (String) objects[2];
				createdBy = (String) objects[3];
				createdOn = (Date) objects[4];
				modifiedBy = (String) objects[5];
				modifiedOn = (Date) objects[6];
				
				businessNamesHistoryResult = new CustomerCRBusinessNamesHistoryResult();
				businessNamesHistoryResult.setBusinessName(customerBusinessNName);
				businessNamesHistoryResult.setRequestedBy(createdBy);
				businessNamesHistoryResult.setRequestedDate(createdOn);
				businessNamesHistoryResult.setModifiedBy(modifiedBy);
				businessNamesHistoryResult.setModifiedDate(modifiedOn);
				businessNamesHistoryResult.setStatus(customerStatus);
				businessNamesHistoryResult.setId(customerId);

				businessNamesHistoryResults.add(businessNamesHistoryResult);
			}
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", businessNamesHistoryResults.size());
			}
			return businessNamesHistoryResults;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	
	/**This method is responsible for get previous Customer CR based on current businessName,and customerCRId
	 *  
	 * @param customerCRId - {@link Integer}
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param username - {@link String}
	 * @return customerCRMaxId - {@link Integer}
	 * @throws DataAccessException 
	 */
	public Integer getPreviousCustomerCRId(Integer customerCRId,String businessName, VbOrganization organization, String username) throws DataAccessException {
		Session session = this.getSession();
		Integer customerCRIds=new Integer(0);
		Query query = session.createQuery("SELECT MAX(vbcr.id) FROM VbCustomerChangeRequest vbcr where vbcr.businessName LIKE :businessName AND vbcr.flag= :flag AND vbcr.id < :customerCRId AND vbcr.vbOrganization = :vbOrganization")
				.setParameter("businessName", businessName)
				.setParameter("flag", new Integer(0))
				.setParameter("customerCRId", customerCRId)
				.setParameter("vbOrganization", organization);
		Integer customerCRMaxId = getSingleResultOrNull(query);
		session.close();
		
		if(customerCRMaxId != null){
			return customerCRMaxId;
		}else{
			return customerCRIds;
		}
	}

	/**
	 * This Method is Responsible to Check whether The Entered BusinessName while making CR for new customer is
	 * Duplicate Or Not
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailable - {@link Boolean}
	 */
	@SuppressWarnings({"unchecked" })
	public Boolean validateBusinessName(String businessName, VbOrganization organization) {
		Boolean isAvailable = Boolean.TRUE;
		Session session = this.getSession();
		List<VbCustomer> customerList = session.createCriteria(VbCustomer.class)
				.add(Expression.eq("businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.list();
		List<VbCustomerChangeRequest> customerCRList = session.createCriteria(VbCustomerChangeRequest.class)
				.add(Expression.eq("businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.list();
		session.close();
		if(!customerList.isEmpty()){
			isAvailable = Boolean.FALSE;
		}else if(!customerCRList.isEmpty()){
			isAvailable = Boolean.FALSE;
		}
		return isAvailable;
	}
	/**
	 * This Method is Responsible to Check whether The Approved New Customer CR BusinessName is
	 * Duplicate Or unique
	 * 
	 * @param customerCRId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailable - {@link Boolean}
	 */
	@SuppressWarnings("unchecked")
	public Boolean validateApprovedNewCustomerCRBusinessName(
			Integer customerCRId, VbOrganization organization) {
		Boolean isAvailable = Boolean.TRUE;
		Session session = this.getSession();
		VbCustomerChangeRequest customerChangeRequest = (VbCustomerChangeRequest) session.get(VbCustomerChangeRequest.class, customerCRId);
		// duplicate name possibility only for New Customer CR
		if (Boolean.FALSE == customerChangeRequest.getCrType()) {
			// new Customer
			List<VbCustomer> customerList = session.createCriteria(VbCustomer.class)
					.add(Expression.eq("businessName", customerChangeRequest.getBusinessName()))
					.add(Expression.eq("vbOrganization", organization))
					.list();
			session.close();
			if (!customerList.isEmpty()) {
				isAvailable = Boolean.FALSE;
			}
		} else {
			// existing customer
			isAvailable = Boolean.TRUE;
		}
		return isAvailable;
	}

	/**
	 * This method is responsible to get the customer CR data.
	 * 
	 * @param id - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return customerResult - {@link CustomerResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public CustomerResult getCustomerCrData(Integer id, String businessName, VbOrganization organization) throws DataAccessException {

		Session session = this.getSession();
		CustomerResult customerResult = null;
		VbCustomerChangeRequest vbCustomerCr = (VbCustomerChangeRequest) session.get(VbCustomerChangeRequest.class, id);
		
		VbCustomer vbCustomer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("businessName", businessName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		
		if(vbCustomerCr != null){
			VbCustomerChangeRequestDetails customerDetail = (VbCustomerChangeRequestDetails) session.createCriteria(VbCustomerChangeRequestDetails.class)
					.add(Restrictions.eq("vbCustomerChangeRequest", vbCustomerCr))
					.uniqueResult();
			customerResult = new CustomerResult();
			customerResult.setAddressLine1(customerDetail.getAddressLine1());
			customerResult.setAddressLine2(customerDetail.getAddressLine2());
			customerResult.setAlternateMobile(customerDetail.getAlternateMobile());
			customerResult.setBusinessName(vbCustomerCr.getBusinessName());
			customerResult.setCrType(StringUtil.booleanFormat(vbCustomerCr.getCrType()));
			customerResult.setCity(customerDetail.getCity());
			customerResult.setState(customerDetail.getState());
			customerResult.setInvoiceName(customerDetail.getInvoiceName());
			customerResult.setCustomerName(customerDetail.getCustomerName());
			customerResult.setEmail(customerDetail.getEmail());
			customerResult.setGender(vbCustomerCr.getGender());
			customerResult.setLandmark(customerDetail.getLandmark());
			customerResult.setLocality(customerDetail.getLocality());
			customerResult.setRegion(customerDetail.getRegion());
			customerResult.setDirectLine(customerDetail.getDirectLine());
			customerResult.setMobile(customerDetail.getMobile());
			customerResult.setZipcode(customerDetail.getZipcode());
			
			Float creditLimit = new Float("0.00");
			Integer creditOverdueDays = new Integer(0);
			if(vbCustomer != null) {
				creditLimit = vbCustomer.getCreditLimit();
				creditOverdueDays = vbCustomer.getCreditOverdueDays();
			}
			customerResult.setCreditLimit(creditLimit);
			customerResult.setCreditOverdueDays(creditOverdueDays);
			session.close();
			
			if(_logger.isDebugEnabled()){
				_logger.debug("CustomerResult: {}", customerResult);
			}
			return customerResult;
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	
	}
	
	
	/**
	 * This method is responsible to get the customer CR data.
	 * 
	 * @param id - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return customerResult - {@link CustomerResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public CustomerResult getCustomerCrDataForCredit(Integer id, VbOrganization organization) throws DataAccessException {

		Session session = this.getSession();
		CustomerResult customerResult = null;
		VbCustomerChangeRequest vbCustomerCr = (VbCustomerChangeRequest) session.get(VbCustomerChangeRequest.class, id);
		
		if(vbCustomerCr != null){
			VbCustomerChangeRequestDetails customerDetail = (VbCustomerChangeRequestDetails) session.createCriteria(VbCustomerChangeRequestDetails.class)
					.add(Restrictions.eq("vbCustomerChangeRequest", vbCustomerCr))
					.uniqueResult();
			customerResult = new CustomerResult();
			customerResult.setAddressLine1(customerDetail.getAddressLine1());
			customerResult.setAddressLine2(customerDetail.getAddressLine2());
			customerResult.setAlternateMobile(customerDetail.getAlternateMobile());
			customerResult.setBusinessName(vbCustomerCr.getBusinessName());
			customerResult.setCrType(StringUtil.booleanFormat(vbCustomerCr.getCrType()));
			customerResult.setCity(customerDetail.getCity());
			customerResult.setState(customerDetail.getState());
			customerResult.setInvoiceName(customerDetail.getInvoiceName());
			customerResult.setCustomerName(customerDetail.getCustomerName());
			customerResult.setEmail(customerDetail.getEmail());
			customerResult.setGender(vbCustomerCr.getGender());
			customerResult.setLandmark(customerDetail.getLandmark());
			customerResult.setLocality(customerDetail.getLocality());
			customerResult.setRegion(customerDetail.getRegion());
			customerResult.setDirectLine(customerDetail.getDirectLine());
			customerResult.setMobile(customerDetail.getMobile());
			customerResult.setZipcode(customerDetail.getZipcode());
			
			session.close();
			
			if(_logger.isDebugEnabled()){
				_logger.debug("CustomerResult: {}", customerResult);
			}
			return customerResult;
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	
	}
}