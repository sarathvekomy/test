/**
 * com.vekomy.vbooks.customer.dao.CustomerDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 15, 2013
 */
package com.vekomy.vbooks.customer.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
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

import com.vekomy.vbooks.customer.command.CustomerChangeRequestCommand;
import com.vekomy.vbooks.customer.command.CustomerCommand;
import com.vekomy.vbooks.customer.command.CustomerResult;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerAdvanceInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerDetail;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbEmployeeCustomer;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.util.DirectoryUtil;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * @author Sudhakar
 * 
 * 
 */
public class CustomerDao extends BaseDao {

	/**
	 * Logger variable holdes _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(CustomerDao.class);

	/**
	 * This method is responsible for saving customer basic and additional
	 * details.
	 * 
	 * @param customer - {@link CustomerCommand}
	 * @param customerDetail - {@link VbCustomerDetail}
	 * @param organization - {@link VbOrganization}
	 * @param createdBy - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void saveCustomer(CustomerCommand customer,
			VbCustomerDetail customerDetail, VbOrganization organization,
			String createdBy) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			
			VbCustomer vbCustomer = new VbCustomer();
			Date createdDate = new Date();
			vbCustomer.setCustomerName(customer.getCustomerName());
			vbCustomer.setBusinessName(customer.getBusinessName());
			vbCustomer.setInvoiceName(customer.getInvoiceName());
			vbCustomer.setGender(customer.getGender());
			vbCustomer.setCreditLimit(customer.getCreditLimit());
			vbCustomer.setCreditOverdueDays(customer.getCreditOverdueDays());
			vbCustomer.setCreatedBy(createdBy);
			vbCustomer.setCreatedOn(createdDate);
			vbCustomer.setModifiedOn(createdDate);
			vbCustomer.setState(OrganizationUtils.CUSTOMER_ENABLED);
			vbCustomer.setVbOrganization(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting customer: {}", vbCustomer);
			}
			session.save(vbCustomer);
		
			VbCustomerDetail vbCustomerDetail = new VbCustomerDetail();
			vbCustomerDetail.setAddressLine1(customerDetail.getAddressLine1());
			String addressLine2 = customerDetail.getAddressLine2();
			if (!StringUtils.isEmpty(addressLine2)) {
				vbCustomerDetail.setAddressLine2(addressLine2);
			}
			String alternateMobile = customerDetail.getAlternateMobile();
			if (!StringUtils.isEmpty(alternateMobile)) {
				vbCustomerDetail.setAlternateMobile(alternateMobile);
			}
			vbCustomerDetail.setCity(customerDetail.getCity());
			String landMark = customerDetail.getLandmark();
			if (!StringUtils.isEmpty(landMark)) {
				vbCustomerDetail.setLandmark(landMark);
			}
			vbCustomerDetail.setLocality(customerDetail.getLocality());
			vbCustomerDetail.setMobile(customer.getMobile());
			vbCustomerDetail.setZipcode(customerDetail.getZipcode());
			if (customer.getEmail() != null) {
				vbCustomerDetail.setEmail(customer.getEmail());
			} else if (customerDetail.getEmail() != null) {
				vbCustomerDetail.setEmail(customerDetail.getEmail());
			}
			String directLine = customerDetail.getDirectLine();
			if (!StringUtils.isEmpty(directLine)) {
				vbCustomerDetail.setDirectLine(directLine);
			}
			vbCustomerDetail.setState(customerDetail.getState());
			vbCustomerDetail.setRegion(customerDetail.getRegion());
			vbCustomerDetail.setVbCustomer(vbCustomer);
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting CustomerDetail: {}", vbCustomerDetail);
			}
				session.save(vbCustomerDetail);
			txn.commit();
			
			// Creating Directory structure for customers to upload documents.
			DirectoryUtil.getInstance().createDirectoryStructure(organization.getName(), customer.getBusinessName());
		} catch (HibernateException exception) {
			if (txn != null) {
				txn.rollback();
			}
			String errorMessage = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);

			if (_logger.isErrorEnabled()) {
				_logger.error(errorMessage);
			}
			throw new DataAccessException(errorMessage);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible to perform search on customer module.
	 * 
	 * @param customerCommand - {@link CustomerCommand}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return customerDetailsList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException} 
	 */
	@SuppressWarnings({ "unchecked"})
	public List<CustomerResult> searchCustomer(CustomerCommand customerCommand, VbOrganization vbOrganization) throws DataAccessException {
		Session session = this.getSession();
		VbCustomerDetail vbCustomerDetail = null;
		Criteria criteria = session.createCriteria(VbCustomer.class);
		if (customerCommand != null) {
			Criteria crit=session.createCriteria(VbCustomerDetail.class).createAlias("vbCustomer","customer");
			if(!customerCommand.getLocality().isEmpty()){
				List<Integer> localities = crit.setProjection(Projections.property("customer.id"))
						.add(Restrictions.like("locality", customerCommand.getLocality(), MatchMode.START).ignoreCase()).list();
				if(!localities.isEmpty()){
					criteria.add(Restrictions.in("id", localities));
				}
			}
			if (!customerCommand.getCustomerName().isEmpty()) {
				criteria.add(Expression.like("customerName", customerCommand.getCustomerName(), MatchMode.START).ignoreCase());
			}
			if (!customerCommand.getBusinessName().isEmpty()) {
				criteria.add(Expression.like("businessName", customerCommand.getBusinessName(), MatchMode.START).ignoreCase());
			}
			if (!customerCommand.getInvoiceName().isEmpty()) {
				criteria.add(Expression.like("invoiceName", customerCommand.getInvoiceName(), MatchMode.START).ignoreCase());
			}
		}
		
		if (vbOrganization != null) {
			criteria.add(Expression.eq("vbOrganization", vbOrganization));
		}
		criteria.addOrder(Order.desc("state"));
		criteria.addOrder(Order.asc("businessName"));
		List<VbCustomer> customerList = criteria.list();
		
		if(!customerList.isEmpty()) {
			List<CustomerResult> customerDetailsList = new ArrayList<CustomerResult>();
			CustomerResult customerResult = null;
			for (VbCustomer vbCustomer : customerList) {
				customerResult = new CustomerResult();
				customerResult.setId(vbCustomer.getId());
				customerResult.setCustomerState(vbCustomer.getState());
				customerResult.setBusinessName(vbCustomer.getBusinessName());
				customerResult.setVbCustomer(vbCustomer);
				vbCustomerDetail = (VbCustomerDetail) session.createCriteria(VbCustomerDetail.class)
						.createAlias("vbCustomer", "customer")
						.add(Expression.eq("customer.id", vbCustomer.getId()))
						.add(Expression.eq("customer.vbOrganization", vbOrganization))
						.uniqueResult();
				customerResult.setMobile(vbCustomerDetail.getMobile());
				customerResult.setLocality(vbCustomerDetail.getLocality());
				if(vbCustomerDetail.getAlternateMobile()!= null){
					customerResult.setAlternateMobile(vbCustomerDetail.getAlternateMobile());
				}
				if(vbCustomerDetail.getEmail()!=null){
					customerResult.setEmail(vbCustomerDetail.getEmail());
				}
				customerResult.setCreditLimit(vbCustomer.getCreditLimit());
				customerResult.setCreditOverdueDays(vbCustomer.getCreditOverdueDays());
				customerDetailsList.add(customerResult);
			}
			session.close();
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", customerDetailsList.size());
			}
			return customerDetailsList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 * This Method Is Responsible to retrieve the Customer Records Based On Given Id
	 * 
	 * @param id - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return customer - {@link VbCustomer}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public VbCustomer getCustomer(int id , VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbCustomer customer = (VbCustomer) session.get(VbCustomer.class, id);
		session.close();
		
		if(customer != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Vbcustomer: {}", customer);
			}
			return customer;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()){
				_logger.error(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}


	/**
	 * This method is responsible to update customer.
	 * 
	 * @param customerCommand - {@link CustomerCommand}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public void editCustomer(CustomerCommand customerCommand,
			VbCustomerDetail customerDetail, VbOrganization organization,
			String userName) throws DataAccessException {
		Session session = this.getSession();
		VbCustomer vbCustomer = (VbCustomer) session.get(VbCustomer.class, customerCommand.getId());

		if (vbCustomer != null) {
			Transaction tx = session.beginTransaction();
			
			vbCustomer.setCustomerName(customerCommand.getCustomerName());
			vbCustomer.setBusinessName(customerCommand.getBusinessName());
			vbCustomer.setInvoiceName(customerCommand.getInvoiceName());
			vbCustomer.setCreditLimit(customerCommand.getCreditLimit());
			vbCustomer.setCreditOverdueDays(customerCommand.getCreditOverdueDays());
			vbCustomer.setModifiedBy(userName);
			Date modifiedDate = new Date();
			vbCustomer.setModifiedOn(modifiedDate);
			vbCustomer.setVbOrganization(organization);
			vbCustomer.setGender(customerCommand.getGender());

			if (_logger.isDebugEnabled()) {
				_logger.debug("Updating VbCustomer: {}", vbCustomer);
			}
			session.update(vbCustomer);
			
			VbCustomerDetail vbCustomerDetail = (VbCustomerDetail) session
					.createCriteria(VbCustomerDetail.class)
					.add(Expression.eq("vbCustomer", vbCustomer))
					.uniqueResult();
			if (vbCustomerDetail != null) {
				vbCustomerDetail.setVbCustomer(vbCustomer);
				vbCustomerDetail.setAddressLine1(customerDetail.getAddressLine1());
				vbCustomerDetail.setAddressLine2(customerDetail.getAddressLine2());
				vbCustomerDetail.setAlternateMobile(customerDetail.getAlternateMobile());
				vbCustomerDetail.setCity(customerDetail.getCity());
				vbCustomerDetail.setLandmark(customerDetail.getLandmark());
				vbCustomerDetail.setLocality(customerDetail.getLocality());
				vbCustomerDetail.setZipcode(customerDetail.getZipcode());
				vbCustomerDetail.setState(customerDetail.getState());
				vbCustomerDetail.setEmail(customerCommand.getEmail());
				vbCustomerDetail.setDirectLine(customerDetail.getDirectLine());
				vbCustomerDetail.setRegion(customerDetail.getRegion());
				vbCustomerDetail.setMobile(customerCommand.getMobile());

				if(_logger.isInfoEnabled()) {
					_logger.info("Business Name:{} {}", vbCustomer.getBusinessName(), Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				}
				session.update(vbCustomerDetail);
			}
			tx.commit();
			session.close();
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
			
			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}			
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 * This Method is Responsible to Check whether The Entered BusinessName is
	 * Duplicate Or Not
	 * 
	 * @param customerCommand - {@link CustomerCommand}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailable - {@link Boolean}
	 * @return isAvailable - {@link String}
	 */
	@SuppressWarnings({"unchecked" })
	public Boolean validateBusinessName(CustomerCommand customerCommand, VbOrganization organization) {
		Boolean isAvailable = Boolean.TRUE;
		Session session = this.getSession();
		List<VbCustomer> customerList = session.createCriteria(VbCustomer.class)
				.add(Expression.eq("businessName", customerCommand.getBusinessName()))
				.add(Expression.eq("vbOrganization", organization))
				.list();
		/*List<VbCustomerChangeRequest> customerCRList = session.createCriteria(VbCustomerChangeRequest.class)
				.add(Expression.eq("businessName", customerCommand.getBusinessName()))
				.add(Expression.eq("vbOrganization", organization))
				.list();*/
		session.close();
		if(!customerList.isEmpty()){
			isAvailable = Boolean.FALSE;
		}/*else if(!customerCRList.isEmpty()){
			isAvailable = Boolean.FALSE;
		}*/
		return isAvailable;
	}

	/**
	 * This method is responsible to get {@link VbCustomerDetail}.
	 * 
	 * @param customerCrCommand - {@link CustomerChangeRequestCommand}
	 * @param organization - {@link VbOrganization}
	 * @return customerDetail - {@link VbCustomerDetail}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public VbCustomerDetail getCustomerDetails(CustomerChangeRequestCommand customerCrCommand,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbCustomerDetail customerDetail = (VbCustomerDetail) session
				.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.add(Expression.eq("customer.businessName", customerCrCommand.getBusinessName()))
				.add(Expression.eq("customer.customerName", customerCrCommand.getCustomerName()))
				.add(Expression.eq("customer.gender", customerCrCommand.getGender()))
				.add(Expression.eq("customer.vbOrganization", organization))
				.uniqueResult();
		session.close();

		if (customerDetail != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("customerDetail: {}", customerDetail);
			}
			return customerDetail;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}


	/**
	 * This method is to retrieve the fullName based on given businessName.
	 * 
	 * @param businessName - {@link String}
	 * @return customerName - {@link String}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	public VbCustomer getFullname(String businessName, VbOrganization organization)
			throws DataAccessException {
		Session session = this.getSession();
		VbCustomer customer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Expression.eq("businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();

		if (customer != null) {
			if(_logger.isInfoEnabled()) {
				_logger.info("{} associated with businessName {}", customer, businessName);
			}
			return customer;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 * This method is responsible for displaying all businessNames in ascending order.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return businessNameList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<VbCustomer> getBusinessName(String businessName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("state", "enabled"))
				.add(Restrictions.like("businessName", businessName, MatchMode.START).ignoreCase())
				.addOrder(Order.asc("businessName"));
		List<VbCustomer> businessNameList = criteria.list();
		session.close();
		
		if(!businessNameList.isEmpty()) {
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} business names associated with organization {}", businessNameList.size(), organization.getName());
			}
			return businessNameList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	/**
	 * This method is responsible for displaying all assigned customer business name for login SE.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return businessNameList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<VbCustomer> getSEAssignedBusinessName(String businessName, VbOrganization organization,String userName) throws DataAccessException {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.like("username", userName));
		VbEmployee loginEmployee=(VbEmployee) criteria.uniqueResult();
		if(loginEmployee != null){
			//get all customer business name assigned to this current login SE from customer-assign-employee
			Criteria customerCriteria = session.createCriteria(VbEmployeeCustomer.class)
					.createAlias("vbEmployee", "employee")
					.add(Restrictions.eq("employee.id", loginEmployee.getId()))
					.add(Restrictions.eq("vbOrganization", organization));
			List<VbEmployeeCustomer> employeeCustomerList = customerCriteria.list();
			List<VbCustomer> businessNameList=new ArrayList<VbCustomer>();
			for(VbEmployeeCustomer employeeCustomer : employeeCustomerList){
				VbCustomer assignedCustomer=(VbCustomer)session.get(VbCustomer.class, employeeCustomer.getVbCustomer().getId());
				businessNameList.add(assignedCustomer);
			}
			session.close();
			if(!businessNameList.isEmpty()) {
				if(_logger.isDebugEnabled()) {
					_logger.debug("{} business names associated with organization {}", businessNameList.size(), organization.getName());
				}
			}
			return businessNameList;
		}
		 else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}

	
	/**
	 * This method is responsible to get {@link VbCustomer} instance.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return customer - {@link VbCustomer}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public VbCustomer getVbCustomer(String businessName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbCustomer customer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Expression.eq("businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		if(customer != null) {
			if(_logger.isDebugEnabled()) {
				_logger.debug("VbCustomer: {}", customer);
			}
			return customer;
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}


	/**
	 * This Method Is Responsible To Retrieve {@link VbCustomer}.
	 * 
	 * @param id - {@link Integer}
	 * @param organization - {@link VbCustomer}
	 * @return customerResult - {@link CustomerResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public CustomerResult getCustomerRecord(Integer id, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		CustomerResult customerResult = null;
		VbCustomer vbCustomer = (VbCustomer) session.get(VbCustomer.class, id);
		
		if(vbCustomer != null){
			VbCustomerDetail customerDetail = (VbCustomerDetail) session.createCriteria(VbCustomerDetail.class)
					.add(Restrictions.eq("vbCustomer", vbCustomer))
					.uniqueResult();
			customerResult = new CustomerResult();
			customerResult.setAddressLine1(customerDetail.getAddressLine1());
			customerResult.setAddressLine2(customerDetail.getAddressLine2());
			customerResult.setAlternateMobile(customerDetail.getAlternateMobile());
			customerResult.setBusinessName(vbCustomer.getBusinessName());
			customerResult.setCity(customerDetail.getCity());
			customerResult.setState(customerDetail.getState());
			customerResult.setCreditLimit(vbCustomer.getCreditLimit());
			customerResult.setCreditOverdueDays(vbCustomer.getCreditOverdueDays());
			customerResult.setInvoiceName(vbCustomer.getInvoiceName());
			customerResult.setCustomerName(vbCustomer.getCustomerName());
			customerResult.setEmail(customerDetail.getEmail());
			customerResult.setGender(vbCustomer.getGender());
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
	
	/**
	 * This method is responsible to get organization name based on id.
	 * 
	 * @param id - {@link Integer}
	 * @return organizationName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public String getOrganizationName(Integer id) throws DataAccessException{
		Session session = this.getSession();
		String organizationName = null;
		VbCustomer customer = (VbCustomer) session.get(VbCustomer.class, id);
		if(customer != null) {
			organizationName = customer.getVbOrganization().getName();
			session.close();
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Organization Name associated with customer {} is {} :", id, organizationName);
			}
			return organizationName;
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
	 * This method is responsible to get business name based on id.
	 * 
	 * @param id - {@link Integer}
	 * @return businessName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public String getBusinessNameById(Integer id) throws DataAccessException {
		String businessName = null;
		Session session = this.getSession();
		VbCustomer customer = (VbCustomer) session.get(VbCustomer.class, id);
		session.close();
		if(customer != null) {
			businessName = customer.getBusinessName();
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Businessname associated with id {} is {} :", id, businessName);
			}
			return businessName;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}


	/**
	 * This method is responsible for change the state of
	 * customer(enable,disable) based on credits and advance of respective
	 * customer is zero.
	 * 
	 * @param customerCommand - {@link CustomerCommand}
	 * @param customerStatusParam - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void modifyCustomerStatus(CustomerCommand customerCommand,
			String customerStatusParam, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbCustomer vbCustomer = (VbCustomer) session.get(VbCustomer.class, customerCommand.getId());
		if(vbCustomer != null) {
				Transaction tx = session.beginTransaction();
				if (customerStatusParam.equalsIgnoreCase(OrganizationUtils.CUSTOMER_DISABLED)) {
					vbCustomer.setState(OrganizationUtils.CUSTOMER_DISABLED);
				} else if (customerStatusParam.equalsIgnoreCase(OrganizationUtils.CUSTOMER_ENABLED)) {
					vbCustomer.setState(OrganizationUtils.CUSTOMER_ENABLED);
				}
				session.update(vbCustomer);
				tx.commit();
			    session.close();
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}
	
	/**This method is responsible for check customer credits if Customer is being disabled
	 * 
	 * @param customerCommand - {@link CustomerCommand}
	 * @param organization - {@link VbOrganization}
	 * @return isContains -{@link Boolean}
	 * @throws DataAccessException 
	 */
	public Boolean checkCustomerCredits(CustomerCommand customerCommand,
			VbOrganization organization) throws DataAccessException {
		Boolean isContains = Boolean.FALSE;
		Session session = this.getSession();
		VbCustomer vbCustomer = (VbCustomer) session.get(VbCustomer.class, customerCommand.getId());
		if(vbCustomer != null) {
			Query creditInfoQuery = session.createQuery(
					"FROM VbCustomerCreditInfo vb WHERE vb.createdOn IN ("
					+ "SELECT MIN(vbc.createdOn) FROM VbCustomerCreditInfo vbc WHERE "
					+ "vbc.businessName = :businessName AND vbc.vbOrganization = :vbOrganization AND vbc.due > :due)")
			.setParameter("businessName", vbCustomer.getBusinessName())
			.setParameter("vbOrganization", organization)
			.setParameter("due", new Float("0"));
			
			VbCustomerCreditInfo customerCreditInfo = getSingleResultOrNull(creditInfoQuery);
			
			Query advanceInfoQuery = (Query) session.createQuery("FROM VbCustomerAdvanceInfo vb WHERE vb.createdOn IN (" +
					"SELECT MIN(vbc.createdOn) FROM VbCustomerAdvanceInfo vbc WHERE vbc.businessName = :businessName AND " +
					"vbc.vbOrganization = :vbOrganization AND vbc.balance > :balance)")
					.setParameter("businessName", vbCustomer.getBusinessName())
					.setParameter("vbOrganization", organization)
					.setParameter("balance", new Float(0));
			
			VbCustomerAdvanceInfo customerAdvanceInfo = getSingleResultOrNull(advanceInfoQuery);
			if (customerCreditInfo == null && customerAdvanceInfo == null) {
				isContains = Boolean.TRUE;
			}
			session.close();
			return isContains;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}
	
	/**
	 * This method is used to check the business name availability.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isExists - {@link Boolean}
	 */
	public Boolean isBusinessNameExists(String businessName, VbOrganization organization) {
		Boolean isExists = Boolean.FALSE;
		Session session = this.getSession();
		VbCustomer customer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("businessName", businessName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		if(customer != null) {
			if(_logger.isDebugEnabled()) {
				_logger.debug("Business name already exists.");
			}
			isExists = Boolean.TRUE;
		}
		return isExists;
	}
	
	/**
	 * This method is responsible to check the given business name availability.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailability - {@link Boolean}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public Boolean checkBusinessNameAvailability(String businessName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Boolean isAvailability = Boolean.FALSE;
		VbCustomer vbCustomer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("businessName", businessName))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("state", "enabled"))
				.uniqueResult();
		session.close();
		if(vbCustomer != null) {
			isAvailability = Boolean.TRUE;
			return isAvailability;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error("No Business Names available with : {}", businessName);
			}
			throw new DataAccessException(errorMsg);
		}
	}

	
}