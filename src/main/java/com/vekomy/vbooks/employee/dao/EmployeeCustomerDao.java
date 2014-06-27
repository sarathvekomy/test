/**
 * com.vekomy.vbooks.employee.dao.EmployeeCustomerDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: May 16, 2013
 */
package com.vekomy.vbooks.employee.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.employee.command.EmployeeCustomerResult;
import com.vekomy.vbooks.employee.command.EmployeeCustomersCommand;
import com.vekomy.vbooks.employee.command.EmployeeResult;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerDetail;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbEmployeeCustomer;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.util.ENotificationTypes;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * This dao class is responsible to provide association between
 * {@link VbEmployee} and {@link VbCustomer}
 * 
 * @author Vinay
 * 
 * 
 */
public class EmployeeCustomerDao extends BaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(EmployeeCustomerDao.class);

	/**
	 * This method is responsible to get all the {@link VbEmployee} presented in
	 * the {@link VbOrganization}.
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @return employees - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getEmployees(VbOrganization vbOrganization, String employeeName) throws DataAccessException {
		Session session = this.getSession();
		List<String> employees = session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("username"))
				.add(Expression.eq("employeeType", "SLE"))
				.add(Expression.eq("vbOrganization", vbOrganization))
				.add(Expression.like("username", employeeName, MatchMode.START))
				.list();
		if(!(employees.isEmpty())) {
			List<String> enabledEmployees = session.createCriteria(VbLogin.class)
					.setProjection(Projections.property("username"))
					.add(Restrictions.eq("enabled", OrganizationUtils.LOGIN_ENABLED))
					.add(Restrictions.in("username", employees))
					.list();
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} Enabled SalesExecutive's were found under the organization: {}", enabledEmployees.size(), vbOrganization.getName());
			}
			session.close();
			return enabledEmployees;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/** 
	 * This method id responsible for get the all the localities in {@link VbOrganization}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return localitiesList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getLocalityList(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> localitiesList = session.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.setProjection(Projections.distinct(Projections.property("locality")))
				.add(Restrictions.eq("customer.vbOrganization", organization))
				.add(Restrictions.eq("customer.state",OrganizationUtils.CUSTOMER_ENABLED))
				.list();
		session.close();
		
		if(!(localitiesList.isEmpty())) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} localities available present in the organization {}.", localitiesList.size(), organization);
			}
			return localitiesList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}
	
	/**
	 * This method is responsible to all the {@link VbCustomer} available in
	 * particular locality.
	 * 
	 * @param employeeName - {@link String}
	 * @return businessNames - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getCustomerLocalities(VbOrganization organization,String locality) throws DataAccessException {
		Session session = this.getSession();
		List<String> localitiesList = session.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.setProjection(Projections.distinct(Projections.property("locality")))
				.add(Expression.eq("customer.vbOrganization", organization))
				.add(Expression.like("locality", locality, MatchMode.START))
				.list();
		session.close();
		
		if(!(localitiesList.isEmpty())) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} localities available present in the organization {}.", localitiesList.size(), organization);
			}
			return localitiesList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to get all the business names available in
	 * particular locality based on {@link VbOrganization}.
	 * 
	 * @param locality - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return businessNames - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBusinessNames(String locality, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbCustomerDetail> customerDetails = session.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.add(Restrictions.eq("locality", locality))
				.add(Restrictions.eq("customer.state", OrganizationUtils.CUSTOMER_ENABLED))
				.add(Restrictions.eq("customer.vbOrganization", organization))
				.list();
		
		if (customerDetails != null) {
			VbCustomer customer = null;
			List<String> businessNames = new ArrayList<String>();
			for (VbCustomerDetail vbCustomerDetail : customerDetails) {
				customer = vbCustomerDetail.getVbCustomer();
				businessNames.add(customer.getBusinessName());
			}
			session.close();
			
			if (!businessNames.isEmpty()) {
				if (_logger.isDebugEnabled()) {
					_logger.debug("{} customers available in {}", businessNames.size(), locality);
				}
				return businessNames;
			} else {
				String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
				
				if(_logger.isWarnEnabled()) {
					_logger.warn(message);
				}
				throw new DataAccessException(message);
			}
		} else {
			session.close();
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method is to search Assigned Customers To the given employee And
	 * vice versa
	 * 
	 * @param employeeCustomersCommand - {@link EmployeeCustomersCommand}
	 * @return employeeCustomerlist - {@link EmployeeResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<EmployeeCustomerResult> searchAssignedCustomer(
			EmployeeCustomersCommand employeeCustomersCommand,
			VbOrganization organization) throws DataAccessException {
		String username = employeeCustomersCommand.getUserName();
		String businessName = employeeCustomersCommand.getBusinessName();
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbEmployeeCustomer.class);
		criteria.createAlias("vbEmployee", "employee");
		criteria.createAlias("vbCustomer", "customer");
		if (employeeCustomersCommand != null) {
			if (username != null) {
				criteria.add(Restrictions.like("employee.username", username, MatchMode.START).ignoreCase());
			}
			if (businessName != null) {
				criteria.add(Restrictions.like("customer.businessName", businessName, MatchMode.START).ignoreCase());
			}
		}
		if (organization != null) {
			criteria.add(Restrictions.eq("vbOrganization", organization));
		}
		criteria.addOrder(Order.desc("createdOn"));
		List<VbEmployeeCustomer> employeeList = criteria.list();
		if(!employeeList.isEmpty()){
			List<EmployeeCustomerResult> resultList = new ArrayList<EmployeeCustomerResult>();
			EmployeeCustomerResult employeeResult = null;
			String isEnabled = null;
			for (VbEmployeeCustomer vbEmployeeCustomer : employeeList) {
				employeeResult = new EmployeeCustomerResult();
				isEnabled = (String) session.createCriteria(VbLogin.class)
						.setProjection(Projections.property("enabled"))
						.add(Restrictions.eq("vbOrganization", organization))
						.add(Restrictions.eq("username", vbEmployeeCustomer.getVbEmployee().getUsername()))
						.uniqueResult()
						.toString();
				if (isEnabled != null) {
					employeeResult.setIsEnabled(isEnabled.charAt(0));
				}
				employeeResult.setUserName(vbEmployeeCustomer.getVbEmployee().getUsername());
				employeeResult.setCreatedOn(vbEmployeeCustomer.getModifiedOn());
				employeeResult.setFirstName(vbEmployeeCustomer.getVbEmployee().getFirstName());
				employeeResult.setId(vbEmployeeCustomer.getVbEmployee().getId());
				resultList.add(employeeResult);
			}
			List<EmployeeCustomerResult> employeeCustomerlist = new ArrayList<EmployeeCustomerResult>(
					new HashSet<EmployeeCustomerResult>(resultList));
			Collections.sort(employeeCustomerlist);
			if (_logger.isInfoEnabled()) {
				_logger.info("{} business names have been assigned.", employeeCustomerlist.size());
			}
			session.close();
			return employeeCustomerlist;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method is Responsible to Retrieve assignedCustomers Based On The
	 * Given id
	 * 
	 * @param id
	 * @return {@link VbCustomerDetail,VbEmployeeCustomer}
	 */
	@SuppressWarnings("unchecked")
	public List<VbCustomerDetail> getEmployeeCustomersForView(int id, VbOrganization vbOrganization) {
		Session session = this.getSession();
		List<Integer> customerId = session.createCriteria(VbEmployeeCustomer.class)
				.createAlias("vbEmployee", "employee")
				.createAlias("vbCustomer", "customer")
				.setProjection(Projections.property("customer.id"))
				.add(Restrictions.eq("employee.id", id))
				.add(Restrictions.eq("vbOrganization", vbOrganization))
				.list();
		List<VbCustomerDetail> customerList = null;
		if (!customerId.isEmpty()) {
			customerList = session.createCriteria(VbCustomerDetail.class)
					.createAlias("vbCustomer","customer")
					.add(Restrictions.in("customer.id", customerId))
					.list();
		} 
		session.close();
		return customerList;
	}

	/**
	 * This Method is Responsible to delete {@link VbEmployeeCustomer}s For an {@link VbEmployee}.
	 * 
	 * @param customerIds - {@link String}
	 * @param salesExecutive - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void deleteEmployeeCustomer(String customerIds,
			String salesExecutive, String userName, VbOrganization organization)
			throws DataAccessException {
		Session session = this.getSession();
		List<Integer> customerIdsList = new ArrayList<Integer>();
		List<String> customerList = Arrays.asList(customerIds.split(","));
		for (String id : customerList) {
			customerIdsList.add(Integer.valueOf(id));
		}
		List<VbEmployeeCustomer> employeeCustomerList = session.createCriteria(VbEmployeeCustomer.class)
				.createAlias("vbCustomer","customer")
				.createAlias("vbEmployee", "employee")
				.add(Restrictions.eq("employee.username", salesExecutive))
				.add(Restrictions.in("customer.id",customerIdsList))
				.list();
		
		if(!employeeCustomerList.isEmpty()) {
			String businessName = null;
			StringBuffer businessNames = null;
			Transaction tx = session.beginTransaction();
			for(VbEmployeeCustomer employeeCustomer:employeeCustomerList){
				businessName = employeeCustomer.getVbCustomer().getBusinessName();
				if(businessNames == null) {
					businessNames = new StringBuffer(businessName);
				} else {
					businessNames.append(",").append(businessName);
				}
				session.delete(employeeCustomer);
			}
			
			// For Android Application.
			saveSystemNotificationForAndroid(session, userName, salesExecutive, 
					organization, ENotificationTypes.DEASSIGN_CUSTOMER.name(),
					"N/A", "N/A", businessNames.toString());
			
			tx.commit();
			session.close();
		} else {
			session.close();
			String message = Msg.get(MsgEnum.DELETE_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
		
	}

	/**
	 * This Method is Responsible to De-Assigned {@link VbCustomer} For an {@link VbEmployee}.
	 * 
	 * @param businessName - {@link String}
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	public void deAssignEmployeeCustomer(String businessName,
			String salesExecutive, VbOrganization organization, String userName)
			throws DataAccessException {
		Session session=this.getSession();
		VbCustomer customer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("businessName",businessName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		if(customer != null) {
			Transaction tx = session.beginTransaction();
			VbEmployeeCustomer	vbEmployeeCustomer = (VbEmployeeCustomer) session.createCriteria(VbEmployeeCustomer.class)
					.createAlias("vbCustomer", "customer")
					.createAlias("vbEmployee", "employee")
					.add(Restrictions.eq("customer.businessName", businessName))
					.add(Restrictions.eq("employee.username",salesExecutive))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			session.delete(vbEmployeeCustomer);
			
			// For Android Application.
			saveSystemNotificationForAndroid(session, userName, vbEmployeeCustomer.getVbEmployee().getUsername(),
					organization, ENotificationTypes.DEASSIGN_CUSTOMER.name(), "N/A", "N/A", businessName);
			
			tx.commit();
			session.close();
		} else {
			session.close();
			String message = Msg.get(MsgEnum.DELETE_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}
	
	/**
	 * This Method is Responsible For Retrieving BusinessNames Based on Employee UserName
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param id - {@link Integer}
	 * @return businessNames - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBusinessNamesByEmployee(String userName,
			VbOrganization organization, Integer id) throws DataAccessException {
		Session session=this.getSession();
		VbEmployee employee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		
		if(employee != null) {
			List<String> businessNames  = session.createCriteria(VbEmployeeCustomer.class)
					.createAlias("vbCustomer", "customer")
					.setProjection(Projections.property("customer.businessName"))
					.add(Restrictions.eq("vbEmployee", employee))
					.add(Restrictions.eq("vbOrganization", organization))
					.list();
			session.close();
			
			if(_logger.isInfoEnabled()){
				_logger.info("{} businessNames have been assigned for {}",businessNames.size(), userName);
			}
			return businessNames;
		} else {
			session.close();
			String message = Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is Responsible For Updating {@link VbEmployeeCustomer}.
	 * 
	 * @param employeeCustomersCommand - {@link EmployeeCustomersCommand}
	 * @param vbOrganization - {@link VbOrganization}
	 * @param businessNames - {@link String}
	 * @param createdBy - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void updateEmployeeCustomer(
			EmployeeCustomersCommand employeeCustomersCommand,
			VbOrganization vbOrganization, String businessNames,
			String createdBy) throws DataAccessException {
		Session session = null;
		Transaction tx = null;
		try {
			session = this.getSession();
	        tx = session.beginTransaction();
	        String userName = employeeCustomersCommand.getUserName();
	        List<String> businessNamesList = Arrays.asList(businessNames.split(","));
	        VbEmployeeCustomer employeeCustomer = null;
	        VbEmployee employee = null;
	        VbCustomer customer = null;
	        for (String businessName : businessNamesList) {
	        	employeeCustomer = (VbEmployeeCustomer) session.createCriteria(VbEmployeeCustomer.class)
	        			.createAlias("vbEmployee", "employee")
	            		.createAlias("vbCustomer", "customer")
	            		.add(Restrictions.eq("employee.username", userName))
	            		.add(Restrictions.eq("customer.businessName", businessName))
	            		.add(Restrictions.eq("employee.vbOrganization", vbOrganization))
	            		.add(Restrictions.eq("customer.vbOrganization", vbOrganization))
	            		.uniqueResult();
	        	if(employeeCustomer == null) {
	        		employeeCustomer = new VbEmployeeCustomer();
	        		employeeCustomer.setCreatedBy(createdBy);
	        		employeeCustomer.setCreatedOn(new Date());
	        		customer = (VbCustomer) session.createCriteria(VbCustomer.class)
	        			 .add(Restrictions.eq("businessName", businessName))
	        			 .add(Restrictions.eq("vbOrganization", vbOrganization))
	        			 .uniqueResult();
	        		employeeCustomer.setVbCustomer(customer);
	        		employee = (VbEmployee) session.createCriteria(VbEmployee.class)
	        			 .add(Restrictions.eq("username", userName))
	        			 .add(Restrictions.eq("vbOrganization", vbOrganization))
	        			 .uniqueResult();
	        		employeeCustomer.setVbEmployee(employee);
	        		employeeCustomer.setVbOrganization(vbOrganization);
	        		
	        		if(_logger.isDebugEnabled()){
	        			_logger.debug("Assigning new customer to {}", userName);
	        		}
	        		session.save(employeeCustomer);
	        	 }
	         }
	        // For Android Application.
			saveSystemNotificationForAndroid(session, userName, employee.getUsername(), vbOrganization, 
					ENotificationTypes.ASSIGN_CUSTOMER.name(), "N/A", "N/A", businessNames);
	         tx.commit();
		} catch (HibernateException exception) {
			if(tx != null) {
				tx.rollback();
			}
			String errorMsg = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		} finally {
			if (session != null) {
				session.close();
			}
		}
     }

	/**
	 * This Method is Responsible To Check Whether This Customer is Already Assigned To Someone or Not 
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailable - {@link Boolean}
	 */
	public Boolean checkBusinessNameAvailability(String businessName, String salesExecutive, VbOrganization organization)  {
		Session session = this.getSession();
		Boolean isAvailable = Boolean.FALSE;
		VbCustomer customer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("businessName",businessName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		if (customer != null) {
			VbEmployeeCustomer vbEmployeeCustomer =  (VbEmployeeCustomer) session.createCriteria(VbEmployeeCustomer.class)
					.createAlias("vbCustomer", "customer")
					.createAlias("vbEmployee", "employee")
					.add(Restrictions.eq("customer.businessName", businessName))
					.add(Restrictions.eq("employee.username", salesExecutive))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			if (vbEmployeeCustomer != null) {
				isAvailable = Boolean.TRUE;
			}
		}
		session.close();
		
		return isAvailable;
	}
	
	/**
	 * This Method is Responsible To Check Whether This Customer is Already
	 * Assigned To Someone or Not.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isSelected - {@link Boolean}
	 */
	@SuppressWarnings("unchecked")
	public Boolean getEmployeesForSelectedBusinessName(String businessName,
			VbOrganization organization) {
		Session session = this.getSession();
		Boolean isUserExists = Boolean.FALSE;
		List<String> usernames = session.createCriteria(VbEmployeeCustomer.class)
				.createAlias("vbEmployee", "employee")
				.setProjection(Projections.property("employee.username"))
				.createAlias("vbCustomer", "customer")
				.add(Restrictions.eq("customer.businessName", businessName))
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		session.close();
		
		if (!usernames.isEmpty()) {
			isUserExists = Boolean.TRUE;
		}

		return isUserExists;
	}
	/**
	 * This method is responsible for saving customers for 
	 *  a SalesExecutive.
	 * 
	 * @param employeeName - {@link String}
	 * @param BusinessNameString - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return isSaved - {@link Boolean}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	public Boolean saveSingleEmployeeCustomer(String employeeName,
			String BusinessNameString, VbOrganization organization,
			String userName) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			Boolean isSaved = Boolean.FALSE;
			txn = session.beginTransaction();
			VbEmployee employee = (VbEmployee) session.createCriteria(VbEmployee.class)
					.add(Expression.eq("username", employeeName))
					.add(Expression.eq("vbOrganization", organization))
					.uniqueResult();
			if (employee != null) {
				VbCustomer customer = null;
				String[] businessStrings = BusinessNameString.split(",");
				List<String> businessNames = Arrays.asList(businessStrings);
				for (String businessName : businessNames) {
					customer = (VbCustomer) session.createCriteria(VbCustomer.class)
							.add(Expression.eq("businessName", businessName))
							.add(Expression.eq("vbOrganization", organization))
							.uniqueResult();
					if(customer != null){
						VbEmployeeCustomer vbEmployeeCustomers = (VbEmployeeCustomer) session.createCriteria(VbEmployeeCustomer.class)
								.add(Restrictions.eq("vbEmployee", employee))
								.add(Restrictions.eq("vbCustomer", customer))
								.add(Restrictions.eq("vbOrganization", organization))
								.uniqueResult();
						if (vbEmployeeCustomers == null) {
							VbEmployeeCustomer employeeCustomer = new VbEmployeeCustomer();
							employeeCustomer.setVbCustomer(customer);
							employeeCustomer.setVbEmployee(employee);
							employeeCustomer.setVbOrganization(organization);
							employeeCustomer.setCreatedOn(new Date());
							employeeCustomer.setCreatedBy(userName);
							employeeCustomer.setModifiedOn(new Date());
							employeeCustomer.setModifiedBy(userName);

							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbEmployeeCustomer: {}", employeeCustomer);
							}
							session.save(employeeCustomer);
						}
						isSaved = Boolean.TRUE;
					} else {
						String message = Msg.get(MsgEnum.ASSIGN_CUSTOMER_PERSIST_FAILURE_MESSAGE);
						
						if(_logger.isWarnEnabled()) {
							_logger.warn(message);
						}
						throw new DataAccessException(message);
					}
				}
			}
			// For Android Application.
			saveSystemNotificationForAndroid(session, userName, employee.getUsername(), organization, 
					ENotificationTypes.ASSIGN_CUSTOMER.name(), "N/A", "N/A", BusinessNameString);
			txn.commit();
			return isSaved;
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

	@SuppressWarnings("unchecked")
	public Boolean isEnabledEmployeeExist(String businessName,
			VbOrganization organization) {
		Boolean isEnabledEmployeeExist = Boolean.FALSE;
		Session session = this.getSession();
		List<String> usernames = session
				.createCriteria(VbEmployeeCustomer.class)
				.createAlias("vbEmployee", "employee")
				.setProjection(Projections.property("employee.username"))
				.createAlias("vbCustomer", "customer")
				.add(Restrictions.eq("customer.businessName", businessName))
				.add(Restrictions.eq("vbOrganization", organization)).list();
		if (!usernames.isEmpty()) {
			List<VbLogin> listUsernames = session.createCriteria(VbLogin.class)
					.add(Restrictions.eq("enabled", OrganizationUtils.LOGIN_ENABLED))
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.in("username", usernames))
					.list();
			
			if (!listUsernames.isEmpty()) {
				isEnabledEmployeeExist = Boolean.TRUE;
			}
		}
		session.close();
		return isEnabledEmployeeExist;
	}
}