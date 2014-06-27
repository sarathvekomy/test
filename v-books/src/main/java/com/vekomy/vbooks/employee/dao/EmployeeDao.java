/**
 * com.vekomy.vbooks.employee.dao.EmployeeDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 10, 2013
 */
package com.vekomy.vbooks.employee.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.employee.command.EmployeeCommand;
import com.vekomy.vbooks.employee.command.EmployeeResult;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbAuthority;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbEmployeeAddress;
import com.vekomy.vbooks.hibernate.model.VbEmployeeDetail;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbRole;
import com.vekomy.vbooks.security.PasswordEncryption;
import com.vekomy.vbooks.util.DropDownUtil;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * This dao is responsible to perform CRUD operations on DB.
 * 
 * @author Sudhakar
 * 
 * 
 */
public class EmployeeDao extends BaseDao {

	/**
	 * Logger variable holdes _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(EmployeeDao.class);

	/**
	 * this method is Responsible For persisting Employee Deatils
	 * 
	 * @param vbEmployee
	 * @param vbEmployeeDetail
	 * @param vbEmployeeAddress
	 * @param username
	 * @param vbOrganization
	 * @return isSaved
	 */
	public Boolean saveEmployee(EmployeeCommand vbEmployee,
			VbEmployeeDetail vbEmployeeDetail,
			VbEmployeeAddress vbEmployeeAddress, String username,
			VbOrganization vbOrganization) {

		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		Boolean isSaved = Boolean.FALSE;

		VbLogin vbLogin = new VbLogin();
		vbLogin.setUsername(vbEmployee.getUsername());
		vbLogin.setPassword(PasswordEncryption.encryptPassword(vbEmployee.getPassword()));
		vbLogin.setEnabled(OrganizationUtils.LOGIN_ENABLED);
		vbLogin.setFirstTime(OrganizationUtils.FIRST_TIME_LOGIN_YES);
		vbLogin.setWrongPasswordCount(new Integer(0));

		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbLogin: {}", vbLogin);
		}
		session.save(vbLogin);

		VbRole userRole = (VbRole) session.get(VbRole.class, OrganizationUtils.ROLE_USER);
		VbAuthority vbAuthorityUser = new VbAuthority();
		vbAuthorityUser.setVbLogin(vbLogin);
		vbAuthorityUser.setVbRole(userRole);
		session.save(vbAuthorityUser);
		Integer role = 0;
		/*if (vbEmployee.getEmployeeType().equals("SAD")) {
			role = OrganizationUtils.ROLE_SITEADMIN;
		} else*/ if (vbEmployee.getEmployeeType().equals("MGT")) {
			role = OrganizationUtils.ROLE_MANAGEMENT;
		} else if (vbEmployee.getEmployeeType().equals("SLE")) {
			role = OrganizationUtils.ROLE_SALESEXECUTIVE;
		} else if (vbEmployee.getEmployeeType().equals("ACC")) {
			role = OrganizationUtils.ROLE_ACCOUNTANT;
		}

		VbRole secondRole = (VbRole) session.get(VbRole.class, role);
		VbAuthority vbAuthoritySecond = new VbAuthority();
		vbAuthoritySecond.setVbLogin(vbLogin);
		if (secondRole != null) {
			vbAuthoritySecond.setVbRole(secondRole);
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting vbAuthority for the username: {} is: {}", username, vbAuthoritySecond);
		}
		session.save(vbAuthoritySecond);

		VbEmployee instance = new VbEmployee();
		if (instance != null) {
			instance.setVbOrganization(vbOrganization);
			instance.setUsername(vbEmployee.getUsername());
			instance.setFirstName(vbEmployee.getFirstName());
			instance.setMiddleName(vbEmployee.getMiddleName());
			instance.setLastName(vbEmployee.getLastName());
			String employeeType = vbEmployee.getEmployeeType();
			String employeeNo = generateEmployeeNo(session, vbOrganization, employeeType);
			if(!employeeNo.isEmpty()){
				instance.setEmployeeNumber(employeeNo);
			}
			instance.setEmployeeType(employeeType);
			instance.setGender(vbEmployee.getGender());
			instance.setEmployeeEmail(vbEmployee.getEmployeeEmail());
			instance.setCreatedBy(username);
			instance.setCreatedOn(new Date());
			instance.setModifiedOn(new Date());
			instance.setEmployeeEmail(vbEmployee.getEmployeeEmail());

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbEmployee: {}", instance);
			}
			session.save(instance);
			isSaved = Boolean.TRUE;
		}

		VbEmployeeDetail instanceDetail = new VbEmployeeDetail();
		if (instanceDetail != null) {
			instanceDetail.setMobile(vbEmployeeDetail.getMobile());
			instanceDetail.setDirectLine(vbEmployeeDetail.getDirectLine());
			instanceDetail.setAlternateMobile(vbEmployeeDetail.getAlternateMobile());
			instanceDetail.setBloodGroup(vbEmployeeDetail.getBloodGroup());
			instanceDetail.setNationality(vbEmployeeDetail.getNationality());
			instanceDetail.setPassportNumber(vbEmployeeDetail.getPassportNumber());
			
			instanceDetail.setVbEmployee(instance);
			session.save(instanceDetail);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbEmployeeDetail: {}", instanceDetail);
			}
			isSaved = Boolean.TRUE;
		}

		VbEmployeeAddress instanceAddress = new VbEmployeeAddress();
		if (instanceAddress != null) {
			instanceAddress.setAddressLine1(vbEmployeeAddress.getAddressLine1());
			instanceAddress.setAddressLine2(vbEmployeeAddress.getAddressLine2());
			instanceAddress.setLocality(vbEmployeeAddress.getLocality());
			instanceAddress.setLandmark(vbEmployeeAddress.getLandmark());
			instanceAddress.setCity(vbEmployeeAddress.getCity());
			instanceAddress.setState(vbEmployeeAddress.getState());
			instanceAddress.setZipcode(vbEmployeeAddress.getZipcode());
			instanceAddress.setAddressType(vbEmployeeAddress.getAddressType());
			instanceAddress.setVbEmployee(instance);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbEmployeeAddress: {}", instanceAddress);
			}
			session.save(instanceAddress);
			isSaved = Boolean.TRUE;
		}
		transaction.commit();
		session.close();
		return isSaved;
	}

	/**
	 * This Method Is Responsible For Updating Employee Details
	 * 
	 * @param employeeCommand
	 * @param vbOrganization
	 * @param modifiedBy
	 */
	public void updateEmployee(EmployeeCommand employee,
			VbEmployeeDetail employeeDetail, VbEmployeeAddress employeeAddress,
			String modifiedBy, VbOrganization vbOrganization) {
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		VbEmployee vbEmployee = (VbEmployee) session
				.createCriteria(VbEmployee.class)
				.add(Expression.eq("id", employee.getId()))
				.add(Expression.eq("vbOrganization", vbOrganization))
				.uniqueResult();

		if (vbEmployee != null) {
			vbEmployee.setUsername(employee.getUsername());
			vbEmployee.setFirstName(employee.getFirstName());
			vbEmployee.setEmployeeEmail(employee.getEmployeeEmail());
			vbEmployee.setEmployeeNumber(employee.getEmployeeNumber());
			vbEmployee.setEmployeeType(employee.getEmployeeType());
			vbEmployee.setLastName(employee.getLastName());
			vbEmployee.setModifiedOn(new Date());
			vbEmployee.setModifiedBy(modifiedBy);
			vbEmployee.setMiddleName(employee.getMiddleName());
			vbEmployee.setVbOrganization(vbOrganization);
			if (employee.getGenderspecific() == "Male") {
				vbEmployee.setGender('M');
			} else {
				vbEmployee.setGender('F');
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("Updating VbEmployee: {}", vbEmployee);
			}
			session.saveOrUpdate(vbEmployee);
		} else {
			if (_logger.isErrorEnabled()) {
				_logger.error("Employee Not Found to updated.");
			}
		}

		VbEmployeeDetail vbEmployeeDetail = (VbEmployeeDetail) session
				.createCriteria(VbEmployeeDetail.class)
				.createAlias("vbEmployee", "employee")
				.add(Expression.eq("employee.id", employee.getId()))
				.add(Expression.eq("employee.vbOrganization", vbOrganization))
				.uniqueResult();

		if (vbEmployeeDetail != null) {
			vbEmployeeDetail.setAlternateMobile(employeeDetail.getAlternateMobile());
			vbEmployeeDetail.setBloodGroup(employeeDetail.getBloodGroup());
			vbEmployeeDetail.setMobile(employeeDetail.getMobile());
			vbEmployeeDetail.setNationality(employeeDetail.getNationality());
			vbEmployeeDetail.setDirectLine(employeeDetail.getDirectLine());
			vbEmployeeDetail.setPassportNumber(employeeDetail.getPassportNumber());

			if (_logger.isDebugEnabled()) {
				_logger.debug("Updating VBEmployeeDetail: {}", vbEmployeeDetail);
			}
			session.saveOrUpdate(vbEmployeeDetail);

			VbEmployeeAddress vbEmployeeAddress = (VbEmployeeAddress) session
					.createCriteria(VbEmployeeAddress.class)
					.createAlias("vbEmployee", "employee")
					.add(Expression.eq("employee.id", employee.getId()))
					.add(Expression.eq("employee.vbOrganization", vbOrganization))
					.uniqueResult();

			if (vbEmployeeAddress != null) {
				vbEmployeeAddress.setAddressLine1(employeeAddress.getAddressLine1());
				vbEmployeeAddress.setAddressLine2(employeeAddress.getAddressLine2());
				vbEmployeeAddress.setAddressType(employeeAddress.getAddressType());
				vbEmployeeAddress.setCity(employeeAddress.getCity());
				vbEmployeeAddress.setLandmark(employeeAddress.getLandmark());
				vbEmployeeAddress.setLocality(employeeAddress.getLocality());
				vbEmployeeAddress.setZipcode(employeeAddress.getZipcode());
				vbEmployeeAddress.setState(employeeAddress.getState());

				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbEmployeeAddress: {}", vbEmployeeAddress);
				}
				session.saveOrUpdate(vbEmployeeAddress);
			}
		}
		tx.commit();
		session.close();

	}

	/**
	 * This method is responsible for disabling login credentials of {@link VbEmployee}
	 * 
	 * @param employeeCommand
	 */
	public void deleteEmployee(EmployeeCommand employeeCommand,
			VbOrganization vbOrganization) {
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		
		VbEmployee vbEmployee = (VbEmployee) session.get(VbEmployee.class, employeeCommand.getId());		
		// Disabling login credentials.
		String userName = vbEmployee.getUsername();
		VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Expression.eq("username", userName))
				.uniqueResult();
		
		if(login != null) {
			login.setEnabled(OrganizationUtils.LOGIN_DISABLED);
			
			if(_logger.isDebugEnabled()){
				_logger.debug("{} have been disabled.", userName);
			}
			session.update(login);
		}
		tx.commit();
		session.close();
	}

	/**
	 * This Method Is Responsible To Apply Search Criteria On All The Records
	 * 
	 * @param command
	 * @param vbOrganization
	 * @return instance
	 */
	@SuppressWarnings("unchecked")
	public List<EmployeeResult> searchEmployee(EmployeeCommand command,
			VbOrganization vbOrganization) {
		if (_logger.isDebugEnabled()) {
			_logger.debug("EmployeeCommand: {}", command);
			_logger.debug("vbOrganization: {}", vbOrganization);
		}
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbEmployee.class);
		if (command != null) {
			String firstName = command.getFirstName();
			String lastName = command.getLastName();
			String userName = command.getUsername();
			String employeeType = command.getEmployeeType();
			if (!StringUtils.isEmpty(firstName)) {
				criteria.add(Expression.like("firstName", firstName, MatchMode.START).ignoreCase());
			}
			if (!StringUtils.isEmpty(lastName)) {
				criteria.add(Expression.like("lastName", lastName, MatchMode.START).ignoreCase());
			}
			if (!StringUtils.isEmpty(userName)) {
				criteria.add(Expression.like("username", userName, MatchMode.START).ignoreCase());
			}
			if (!StringUtils.isEmpty(employeeType)) {
				if (employeeType.equals("All")) {
				} else {
					criteria.add(Expression.like("employeeType", employeeType, MatchMode.START).ignoreCase());
				}

			}
		}

		if (vbOrganization != null) {
			criteria.add(Expression.eq("vbOrganization", vbOrganization));
		}

		List<VbEmployee> staffList = criteria.list();
		List<EmployeeResult> staffDetailList = new ArrayList<EmployeeResult>();
		EmployeeResult staffResult = null;
		for (VbEmployee vbEmployee : staffList) {
			staffResult = new EmployeeResult();
			staffResult.setId(vbEmployee.getId());
			staffResult.setFirstName(vbEmployee.getFirstName());
			staffResult.setLastName(vbEmployee.getLastName());
			staffResult.setUsername(vbEmployee.getUsername());
			staffResult.setEmployeeType(vbEmployee.getEmployeeType());
			staffResult.setEmployeeTypeByString(DropDownUtil.getDropDown(DropDownUtil.EMPLOYEE_TYPE, vbEmployee.getEmployeeType()));

			VbEmployeeDetail vbEmployeeDetail = (VbEmployeeDetail) session
					.createCriteria(VbEmployeeDetail.class)
					.createAlias("vbEmployee", "employee")
					.add(Expression.eq("employee.id", vbEmployee.getId()))
					.add(Expression.eq("employee.vbOrganization", vbOrganization))
					.uniqueResult();

			if (vbEmployeeDetail != null) {
				staffResult.setMobile(vbEmployeeDetail.getMobile());
			}
			staffDetailList.add(staffResult);
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("staffDetailList: {}", staffDetailList);
		}
		return staffDetailList;
	}

	/**
	 * This method is used to get the employee based on the id.
	 * 
	 * @param id
	 * @param organization
	 * @return VbEmployee
	 */
	public VbEmployee getEmployee(int id, VbOrganization organization) {
		Session session = this.getSession();
		VbEmployee instance = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Expression.eq("id", id))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		if (_logger.isDebugEnabled()) {
			_logger.debug("VbEmployee: {}", instance);
		}
		return instance;
	}

	/**
	 * This Method Is Responsible To display List Of Employee Details Based On
	 * Given Id
	 * 
	 * @param vbOrganization
	 * @return staffList
	 */
	@SuppressWarnings("unchecked")
	public List<VbEmployee> getEmployeeList(VbOrganization vbOrganization) {
		Session session = this.getSession();
		List<VbEmployee> staffList = session.createCriteria(VbEmployee.class)
				.add(Expression.eq("vbOrganization", vbOrganization)).list();
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("StaffList: {} for the VbOrganization: {}", staffList, vbOrganization);
		}
		return staffList;
	}

	/**
	 * @param vbOrganization
	 * @return vbOrganization1
	 */
	public VbOrganization loadOrganization(VbOrganization vbOrganization) {
		Session session = this.getSession();
		VbOrganization vbOrganization1 = (VbOrganization) session.get(VbOrganization.class, vbOrganization.getId());
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("vbOrganization: {}", vbOrganization1);
		}
		return vbOrganization1;
	}
	
	
	/**
	 * This method is responsible to get all the employee types based on {@link VbOrganization}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return employeeTypes - {@link List}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getEmployeeTypes(VbOrganization organization) {
		Session session = this.getSession();
		List<String> employeeTypes = session.createCriteria(VbRole.class)
				.setProjection(Projections.property("description"))
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		return employeeTypes;
	}
	public String generateEmployeeNumber(VbOrganization organization,String employeeType){
		Session session = this.getSession();
	String employeeNo = generateEmployeeNo(session, organization, employeeType);;
		return employeeNo;
	}
}
