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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
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
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerDetail;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbEmployeeCustomer;
import com.vekomy.vbooks.hibernate.model.VbOrganization;

/**
 * This dao class is responsible to provide association between
 * {@link VbEmployee} and {@link VbCustomer}
 * 
 * @author Sudhakar
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
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getEmployees(VbOrganization vbOrganization,String employeeName) {
		Session session = this.getSession();
		List<String> employees = session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("username"))
				.add(Expression.eq("employeeType", "SLE"))
				.add(Expression.eq("vbOrganization", vbOrganization))
				.add(Expression.like("username", employeeName, MatchMode.START))
				.addOrder(Order.asc("username"))
				.list();
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("Employees: {}", employees);
		}
		return employees;
	}

	/** This method id responsible for get the all the localities in current organization.
	 * 
	 * @param organization
	 * @return localitiesList
	 */
	@SuppressWarnings("unchecked")
	public List<String> getLocalityList(VbOrganization organization) {
		Session session = this.getSession();
		List<String> localitiesList = session.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.setProjection(Projections.distinct(Projections.property("locality")))
				.add(Restrictions.eq("customer.vbOrganization", organization))
				.list();
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} distinct localities available present in the organization {}.", localitiesList.size(), organization);
		}
		return localitiesList;
	}
	/**
	 * This method is responsible to all the {@link VbCustomer} available in
	 * particular locality.
	 * 
	 * @param employeeName
	 *            - {@link String}
	 * @return businessNames - {@link List}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getCustomerLocalities(VbOrganization organization,String locality) {
		Session session = this.getSession();
		List<String> localitiesList = session
				.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.setProjection(Projections.distinct(Projections.property("locality")))
				.add(Expression.eq("customer.vbOrganization", organization))
				.add(Expression.like("locality", locality, MatchMode.START))
				.list();
		session.close();
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} distinct localities available present in the organization {}.", localitiesList.size(), organization);
		}
		return localitiesList;
	}

	/**
	 * This method is responsible to get all the business names available in
	 * particular locality based on {@link VbOrganization}.
	 * 
	 * @param locality
	 *            - {@link String}
	 * @param organization
	 *            - {@link VbOrganization}
	 * @return businessNames - {@link List}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBusinessNames(String locality, VbOrganization organization) {
		Session session = this.getSession();
		List<VbCustomerDetail> customerDetails = session.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.add(Restrictions.eq("locality", locality))
				.add(Restrictions.eq("customer.vbOrganization", organization))
				.list();
		List<String> businessNames = new ArrayList<String>();
		if (customerDetails != null) {
			VbCustomer customer = null;
			for (VbCustomerDetail vbCustomerDetail : customerDetails) {
				customer = vbCustomerDetail.getVbCustomer();
				businessNames.add(customer.getBusinessName());
			}
		}
		session.close();
		if (businessNames.isEmpty()) {
			if (_logger.isErrorEnabled()) {
				_logger.debug("Cusotmers not available at {}", locality);
			}
			return null;
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("{} customers available at {}", businessNames.size(),
					locality);
		}
		return businessNames;
	}

	/**
	 * This Method is to search Assigned Customers To the given employee And
	 * vice versa
	 * 
	 * @param employeeCustomersCommand
	 *            - {@link EmployeeCustomersCommand}
	 * @return employeeCustomerlist - {@link EmployeeResult}
	 */
	@SuppressWarnings("unchecked")
	public List<EmployeeCustomerResult> searchAssignedCustomer(
			EmployeeCustomersCommand employeeCustomersCommand,
			VbOrganization organization) {
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
		criteria.addOrder(Order.asc("createdOn"));
		List<VbEmployeeCustomer> employeeList = criteria.list();
		List<EmployeeCustomerResult> resultList = new ArrayList<EmployeeCustomerResult>();
		EmployeeCustomerResult employeeResult = null;
		for (VbEmployeeCustomer vbEmployeeCustomer : employeeList) {
			employeeResult = new EmployeeCustomerResult();
			employeeResult.setUserName(vbEmployeeCustomer.getVbEmployee().getUsername());
			employeeResult.setCreatedOn(vbEmployeeCustomer.getModifiedOn());
			employeeResult.setFirstName(vbEmployeeCustomer.getVbEmployee().getFirstName());
			employeeResult.setId(vbEmployeeCustomer.getVbEmployee().getId());
			resultList.add(employeeResult);
		}
		List<EmployeeCustomerResult> employeeCustomerlist = new ArrayList<EmployeeCustomerResult>(
				new HashSet<EmployeeCustomerResult>(resultList));
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("EmployeeResult: {}", employeeCustomerlist);
		}
		return employeeCustomerlist;
	}

	/**
	 * This Method is Responsible to Retrieve assignedCustomers Based On The
	 * Given id
	 * 
	 * @param id
	 * @return {@link VbCustomerDetail,VbEmployeeCustomer}
	 */
	@SuppressWarnings("unchecked")
	public List<VbCustomerDetail> getEmployeeCustomer(int id,VbOrganization vbOrganization) {
		Session session = this.getSession();
	 List<Integer> customerId=session.createCriteria(VbEmployeeCustomer.class)
			              .createAlias("vbEmployee","employee")
			              .createAlias("vbCustomer","customer")
			              .setProjection(Projections.property("customer.id"))
			              .add(Restrictions.eq("employee.id", id))
			              .add(Restrictions.eq("vbOrganization", vbOrganization))
			              .list();
	 if(! customerId.isEmpty()){
		 Criteria crit=session.createCriteria(VbCustomerDetail.class).createAlias("vbCustomer","customer")
		                       .add(Restrictions.in("customer.id",customerId));
		 List<VbCustomerDetail> customerList=crit.list();
		 session.close();
         return customerList;
	 }
	 else
		session.close();
		return null;
	}

	/**
	 * This Method is Responsible to delete Assigned Customers For an Employee
	 * 
	 * @param id
	 */
	@SuppressWarnings("unchecked")
	public void deleteEmployeeCustomer(String customerIdList) {
		List<Integer> list=new ArrayList<Integer>();
		for(String id:customerIdList.split(",")){
			list.add(new Integer(id));
		}
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		List<VbEmployeeCustomer> employeeCustomerList=session.createCriteria(VbEmployeeCustomer.class)
				               .createAlias("vbCustomer","customer")
				               .add(Restrictions.in("customer.id",list))
				               .list();
		for(VbEmployeeCustomer employeeCustomer:employeeCustomerList){
			session.delete(employeeCustomer);
		}
		
		tx.commit();
		session.close();
	}

	/**
	 * This Method is Responsible to De-Assigned Customers For an Employee
	 * @param businessName
	 * @param organization
	 */
	public void deAssignEmployeeCustomer(String businessName, VbOrganization organization) {
		Session session=this.getSession();
		Transaction tx = session.beginTransaction();
		VbCustomer customer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("businessName",businessName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		//List<VbEmployeeCustomer>vbEmployeeCustomers = null;
		VbEmployeeCustomer	vbEmployeeCustomer = (VbEmployeeCustomer) session.createCriteria(VbEmployeeCustomer.class)
					.createAlias("vbCustomer", "customer")
					.add(Restrictions.eq("vbCustomer", customer))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
		session.delete(vbEmployeeCustomer);
		tx.commit();
		//return salesExecutiveName;
		/*Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		VbEmployeeCustomer vbEmployeeCustomer = (VbEmployeeCustomer) session.get(VbEmployeeCustomer.class, id);
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Deleting Vbcustomer: {}", vbEmployeeCustomer);
		}
		session.delete(vbEmployeeCustomer);
		tx.commit();*/

	}
	/**
	 * This Method is Responsible For Retrieving BusinessNames Based on Employee UserName
	 * 
	 * @param userName
	 * @param organization
	 * @param id
	 * @return businessNames
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBusinessNamesByEmployee(String userName, VbOrganization organization, int id){
		Session session=this.getSession();
		
		VbEmployee employee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		
		List<String> businessNames = null;
		if(employee != null) {
			businessNames  = session.createCriteria(VbEmployeeCustomer.class)
					.createAlias("vbCustomer", "customer")
					.setProjection(Projections.property("customer.businessName"))
					.add(Restrictions.eq("vbEmployee", employee))
					.add(Restrictions.eq("vbOrganization", organization))
					.list();
		}
		session.close();
		if(_logger.isDebugEnabled()){
			_logger.debug("{} businessNames have been assigned for {}",businessNames.size(), userName);
		}
		return businessNames;
	}

	/**
	 * This Method Is Responsible For Updating AssignedCustomers
	 * 
	 * @param employeeCustomersCommand
	 * @param vbOrganization
	 * @param businessNames
	 * @param createdBy
	 */
	public void updateEmployeeCustomer(
			EmployeeCustomersCommand employeeCustomersCommand, VbOrganization vbOrganization, String businessNames, String createdBy) {
         Session session=this.getSession();
         Transaction tx=session.beginTransaction();
         String userName=employeeCustomersCommand.getUserName();
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
         tx.commit();
         session.close();
     }

	/**This Method is Responsible To Check Whether This Customer is Already Assigned To Someone or Not 
	 * 
	 * @param businessName
	 * @param organization
	 * @return vbEmployeeCustomers
	 */
	public String checkBusinessNameAvailability(String businessName, VbOrganization organization) {
		Session session=this.getSession();
		VbCustomer customer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("businessName",businessName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		//List<VbEmployeeCustomer>vbEmployeeCustomers = null;
		String salesExecutiveName = null;
		if(customer != null) {
		VbEmployeeCustomer	vbEmployeeCustomer = (VbEmployeeCustomer) session.createCriteria(VbEmployeeCustomer.class)
					.createAlias("vbCustomer", "customer")
					.add(Restrictions.eq("vbCustomer", customer))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
		if(vbEmployeeCustomer != null){
			salesExecutiveName = vbEmployeeCustomer.getVbEmployee().getUsername();	
		}
		}
		session.close();
		return salesExecutiveName;
	}
	
	
	/** 
	 * This method is responsible for Persisting assign Customer when multiple Customers assigned to one SalesExecutive.
	 * 
	 * @param employeeName
	 * @param map
	 * @param organization
	 * @param userName
	 * @return isSaveds
	 */
	@SuppressWarnings("unchecked")
	public boolean saveEmployeeCustomer(String employeeName, HashMap<String, String> map, VbOrganization organization, String userName) {
		Session session = this.getSession();
		Boolean isSaved = Boolean.FALSE;
		Transaction txn = session.beginTransaction();
		VbEmployee employee = (VbEmployee) session
				.createCriteria(VbEmployee.class)
				.add(Expression.eq("username", employeeName))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		VbEmployeeCustomer employeeCustomer = null;
		if (employee != null) {
		for(Map.Entry<String, String> entry : map.entrySet()) {
			String mapString = map.get(entry.getKey());
			String[] mapStrings = mapString.split(",");
			VbCustomer customer =  null;
			List<String> businessNames = Arrays.asList(mapStrings);
			for (String businessName : businessNames) {
				customer = (VbCustomer) session
						.createCriteria(VbCustomer.class)
						.add(Expression.eq("businessName", businessName))
						.add(Expression.eq("vbOrganization", organization))
						.uniqueResult();
				
				List<VbEmployeeCustomer>vbEmployeeCustomers=session.createCriteria(VbEmployeeCustomer.class)
						.add(Restrictions.eq("vbEmployee", employee))
						.add(Restrictions.eq("vbCustomer", customer))
						.add(Restrictions.eq("vbOrganization", organization))
						.list();
				
				if(vbEmployeeCustomers.size() == 0){
						employeeCustomer = new VbEmployeeCustomer();
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
			}
			isSaved = Boolean.TRUE;
		}
	}
		txn.commit();
		session.close();
		return isSaved;
	}
	/** 
	 * This method is responsible for Persisting assign Customer when Single Customer assigned to one SalesExecutive.
	 * 
	 * @param employeeName
	 * @param BusinessNameString
	 * @param organization
	 * @param userName
	 * @return isSaved
	 */
	@SuppressWarnings("unchecked")
	public boolean saveSingleEmployeeCustomer(String employeeName,String BusinessNameString, VbOrganization organization, String userName) {
		Session session = this.getSession();
		Boolean isSaved = Boolean.FALSE;
		Transaction txn = session.beginTransaction();
		VbEmployee employee = (VbEmployee) session
				.createCriteria(VbEmployee.class)
				.add(Expression.eq("username", employeeName))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		VbEmployeeCustomer employeeCustomer = null;
		if (employee != null) {
			VbCustomer customer =  null;
			String[] businessStrings = BusinessNameString.split(",");
			List<String> businessNames = Arrays.asList(businessStrings);
			for (String businessName : businessNames) {
				customer = (VbCustomer) session
						.createCriteria(VbCustomer.class)
						.add(Expression.eq("businessName", businessName))
						.add(Expression.eq("vbOrganization", organization))
						.uniqueResult();
				List<VbEmployeeCustomer> vbEmployeeCustomers = session.createCriteria(VbEmployeeCustomer.class)
						.add(Restrictions.eq("vbEmployee", employee))
						.add(Restrictions.eq("vbCustomer", customer))
						.add(Restrictions.eq("vbOrganization", organization))
						.list();
				if(vbEmployeeCustomers.size() == 0){
					employeeCustomer = new VbEmployeeCustomer();
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
			}
			isSaved = Boolean.TRUE;
		}
		txn.commit();
		session.close();
		return isSaved;
	}
}