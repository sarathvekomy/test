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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

import com.vekomy.vbooks.employee.command.EmployeeCommand;
import com.vekomy.vbooks.employee.command.EmployeeNotificationResult;
import com.vekomy.vbooks.employee.command.EmployeeResult;
import com.vekomy.vbooks.employee.command.LoginTrackResult;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbAddressTypes;
import com.vekomy.vbooks.hibernate.model.VbAuthority;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbEmployeeAddress;
import com.vekomy.vbooks.hibernate.model.VbEmployeeDetail;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbLoginTrack;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbRole;
import com.vekomy.vbooks.security.PasswordEncryption;
import com.vekomy.vbooks.util.CRStatus;
import com.vekomy.vbooks.util.DropDownUtil;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * This dao class is responsible to perform CRUD operations on DB.
 * 
 * @author Sudhakar
 * 
 * 
 */
public class EmployeeDao extends BaseDao {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(EmployeeDao.class);

	/**
	 * This method is Responsible For persisting Employee details.
	 * 
	 * @param vbEmployee - {@link EmployeeCommand}
	 * @param vbEmployeeDetail - {@link VbEmployeeDetail}
	 * @param vbEmployeeAddress - {@link VbEmployeeAddress}
	 * @param username - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	public void saveEmployee(EmployeeCommand vbEmployee,
			VbEmployeeDetail vbEmployeeDetail,
			VbEmployeeAddress vbEmployeeAddress, String username,
			VbOrganization organization) throws DataAccessException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = this.getSession();
			transaction = session.beginTransaction();
			// Creating login credentials.
			VbLogin vbLogin = new VbLogin();
			vbLogin.setUsername(vbEmployee.getOrgPrefix().concat(vbEmployee.getUsername()));
			vbLogin.setPassword(PasswordEncryption.encryptPassword(vbEmployee.getPassword()));
			vbLogin.setEnabled(OrganizationUtils.LOGIN_ENABLED);
			vbLogin.setFirstTime(OrganizationUtils.FIRST_TIME_LOGIN_YES);
			vbLogin.setWrongPasswordCount(new Integer(0));
			vbLogin.setVbOrganization(organization);
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbLogin");
			}
			session.save(vbLogin);
			
			// Assigning roles to employee.
			VbRole userRole = (VbRole) session.get(VbRole.class, OrganizationUtils.ROLE_USER);
			VbAuthority vbAuthorityUser = new VbAuthority();
			vbAuthorityUser.setVbLogin(vbLogin);
			vbAuthorityUser.setVbRole(userRole);
			session.save(vbAuthorityUser);
			Integer role = 0;
			Integer employeeHierarchyLevel = 0;
			String employeeType = vbEmployee.getEmployeeType();
			Integer grantedDays = new Integer(0);
			if ("MGT".equalsIgnoreCase(employeeType)) {
				role = OrganizationUtils.ROLE_MANAGEMENT;
				employeeHierarchyLevel = OrganizationUtils.EMPLOYEE_TYP_MANAGEMENT;
			} else if ("SLE".equalsIgnoreCase(employeeType)) {
				grantedDays = vbEmployee.getGrantedDays();
				role = OrganizationUtils.ROLE_SALESEXECUTIVE;
				employeeHierarchyLevel = OrganizationUtils.EMPLOYEE_TYP_SALES_EXECUTIVE;
			} else if ("ACC".equalsIgnoreCase(employeeType)) {
				role = OrganizationUtils.ROLE_ACCOUNTANT;
				employeeHierarchyLevel = OrganizationUtils.EMPLOYEE_TYP_ACOUNTANT;
			}

			VbRole secondRole = (VbRole) session.get(VbRole.class, role);
			VbAuthority vbAuthoritySecond = new VbAuthority();
			vbAuthoritySecond.setVbLogin(vbLogin);
			if (secondRole != null) {
				vbAuthoritySecond.setVbRole(secondRole);
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbAuthority for the username: {}", username);
			}
			session.save(vbAuthoritySecond);
			
			// Persisting basic info.
			VbEmployee instance = new VbEmployee();
			instance.setVbOrganization(organization);
			instance.setUsername(vbEmployee.getOrgPrefix().concat(vbEmployee.getUsername()));
			instance.setFirstName(vbEmployee.getFirstName());
			instance.setMiddleName(vbEmployee.getMiddleName());
			instance.setLastName(vbEmployee.getLastName());
			instance.setEmployeeHierarchyLevel(employeeHierarchyLevel);
			instance.setGrantedDays(grantedDays);
			String employeeNo = generateEmployeeNo(session, organization, employeeType);
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
				_logger.debug("Persisting vbEmployee");
			}
			session.save(instance);
			
			// Persisting additional info.
			VbEmployeeDetail instanceDetail = new VbEmployeeDetail();
			instanceDetail.setMobile(vbEmployeeDetail.getMobile());
			instanceDetail.setDirectLine(vbEmployeeDetail.getDirectLine());
			instanceDetail.setAlternateMobile(vbEmployeeDetail.getAlternateMobile());
			instanceDetail.setBloodGroup(vbEmployeeDetail.getBloodGroup());
			instanceDetail.setNationality(vbEmployeeDetail.getNationality());
			instanceDetail.setPassportNumber(vbEmployeeDetail.getPassportNumber());
			instanceDetail.setVbEmployee(instance);
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbEmployeeDetail");
			}
			session.save(instanceDetail);
				
			// Persisting address info.
			VbEmployeeAddress instanceAddress = new VbEmployeeAddress();
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
				_logger.debug("Persisting VbEmployeeAddress");
			}
			session.save(instanceAddress);
			transaction.commit();
		} catch (HibernateException exception) {
			if(transaction != null) {
				transaction.rollback();
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
	 * This method is responsible to check either {@link VbEmployee} exist or not.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isExist - {@link Boolean}
	 */
	public Boolean isEmployeeExist(String userName, VbOrganization organization) {
		Boolean isExist = Boolean.FALSE;
		Session session = this.getSession();
		VbEmployee employee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		if(employee != null) {
			isExist = Boolean.TRUE;
		}
		
		return isExist;
	}
	/**
	 * This method is responsible for updating employee details.
	 * 
	 * @param employeeCommand - {@link EmployeeCommand}
	 * @param vbOrganization - {@link VbOrganization}
	 * @param modifiedBy - {@link String}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void updateEmployee(EmployeeCommand employee,
			VbEmployeeDetail employeeDetail, VbEmployeeAddress employeeAddress,
			String modifiedBy, VbOrganization vbOrganization) throws DataAccessException {
		Session session = this.getSession();
		VbEmployee vbEmployee = (VbEmployee) session.get(VbEmployee.class, employee.getId());
		if (vbEmployee != null) {
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				String userName = employee.getUsername();
				String employeeType = employee.getEmployeeType();
				
				// Updating Authorities.
				VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
						.add(Restrictions.eq("username", userName))
						.add(Restrictions.eq("vbOrganization", vbOrganization))
						.uniqueResult();
				List<VbAuthority> authorities = session.createCriteria(VbAuthority.class)
						.add(Restrictions.eq("vbLogin", login))
						.list();
				Integer employeeHierarchyLevel = 0;
				Integer grantedDays = new Integer(0);
				for (VbAuthority vbAuthority : authorities) {
					VbRole vbRole = vbAuthority.getVbRole();
					if(!(OrganizationUtils.ROLE_USER_STR.equals(vbRole.getRoleName()))) {
						Integer role = 0;
						if ("MGT".equalsIgnoreCase(employeeType)) {	
							role = OrganizationUtils.ROLE_MANAGEMENT;
							employeeHierarchyLevel = OrganizationUtils.EMPLOYEE_TYP_MANAGEMENT;
						} else if ("SLE".equalsIgnoreCase(employeeType)) {
							grantedDays = employee.getGrantedDays();
							role = OrganizationUtils.ROLE_SALESEXECUTIVE;
							employeeHierarchyLevel = OrganizationUtils.EMPLOYEE_TYP_SALES_EXECUTIVE;
						} else if ("ACC".equalsIgnoreCase(employeeType)) {
							role = OrganizationUtils.ROLE_ACCOUNTANT;
							employeeHierarchyLevel = OrganizationUtils.EMPLOYEE_TYP_ACOUNTANT;
						} else if ("Super Management".equalsIgnoreCase(employeeType)) {
							role = OrganizationUtils.SUPER_MANAGEMENT;
							employeeHierarchyLevel = new Integer(0);
						}

						VbRole secondRole = (VbRole) session.get(VbRole.class, role);
						vbAuthority.setVbLogin(login);
						vbAuthority.setVbRole(secondRole);
						
						if(_logger.isDebugEnabled()) {
							_logger.debug("Updating role of {}", userName);
						}
						session.update(vbAuthority);
					}
				}
				
				// Updating basic info.
				vbEmployee.setUsername(userName);
				vbEmployee.setFirstName(employee.getFirstName());
				vbEmployee.setEmployeeEmail(employee.getEmployeeEmail());
				vbEmployee.setEmployeeNumber(employee.getEmployeeNumber());
				vbEmployee.setEmployeeType(employeeType);
				vbEmployee.setEmployeeHierarchyLevel(employeeHierarchyLevel);
				vbEmployee.setGrantedDays(grantedDays);
				vbEmployee.setLastName(employee.getLastName());
				vbEmployee.setModifiedOn(new Date());
				vbEmployee.setModifiedBy(modifiedBy);
				vbEmployee.setMiddleName(employee.getMiddleName());
				vbEmployee.setVbOrganization(vbOrganization);
				if ("Male".equalsIgnoreCase(employee.getGenderspecific())) {
					vbEmployee.setGender('M');
				} else {
					vbEmployee.setGender('F');
				}

				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbEmployee");
				}
				session.update(vbEmployee);
				
				// Updating additional info.
				VbEmployeeDetail vbEmployeeDetail = (VbEmployeeDetail) session
						.createCriteria(VbEmployeeDetail.class)
						.add(Expression.eq("vbEmployee", vbEmployee))
						.uniqueResult();
				vbEmployeeDetail.setAlternateMobile(employeeDetail.getAlternateMobile());
				vbEmployeeDetail.setBloodGroup(employeeDetail.getBloodGroup());
				vbEmployeeDetail.setMobile(employeeDetail.getMobile());
				vbEmployeeDetail.setNationality(employeeDetail.getNationality());
				vbEmployeeDetail.setDirectLine(employeeDetail.getDirectLine());
				vbEmployeeDetail.setPassportNumber(employeeDetail.getPassportNumber());

				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VBEmployeeDetail");
				}
				session.update(vbEmployeeDetail);
				
				// Updating address info.
				VbEmployeeAddress vbEmployeeAddress = (VbEmployeeAddress) session
						.createCriteria(VbEmployeeAddress.class)
						.add(Expression.eq("vbEmployee", vbEmployee))
						.uniqueResult();
				vbEmployeeAddress.setAddressLine1(employeeAddress.getAddressLine1());
				vbEmployeeAddress.setAddressLine2(employeeAddress.getAddressLine2());
				vbEmployeeAddress.setAddressType(employeeAddress.getAddressType());
				vbEmployeeAddress.setCity(employeeAddress.getCity());
				vbEmployeeAddress.setLandmark(employeeAddress.getLandmark());
				vbEmployeeAddress.setLocality(employeeAddress.getLocality());
				vbEmployeeAddress.setZipcode(employeeAddress.getZipcode());
				vbEmployeeAddress.setState(employeeAddress.getState());

				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbEmployeeAddress");
				}
				session.update(vbEmployeeAddress);
				tx.commit();
			} catch (HibernateException exception) {
				if(tx != null) {
					tx.rollback();
				}
				String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
				
				if(_logger.isErrorEnabled()) {
					_logger.warn(message);
				}
				throw new DataAccessException(message);
			} finally {
				if(session != null) {
					session.close();
				}
			}
		} else {
			session.close();
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if (_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		} 
	}

	/**
	 * This method is responsible for disabling login credentials of {@link VbEmployee}
	 * 
	 * @param employeeCommand - {@link EmployeeCommand}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	public void deleteEmployee(EmployeeCommand employeeCommand,
			VbOrganization vbOrganization) throws DataAccessException {
		Session session = this.getSession();
		VbEmployee vbEmployee = (VbEmployee) session.get(VbEmployee.class, employeeCommand.getId());	
		if(vbEmployee != null) {
			Transaction tx = session.beginTransaction();
			// Disabling login credentials.
			String userName = vbEmployee.getUsername();
			VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
					.add(Expression.eq("username", userName))
					.add(Restrictions.eq("vbOrganization", vbOrganization))
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
	 * This Method Is Responsible To Apply Search Criteria On All The Records
	 * 
	 * @param command - {@link EmployeeCommand}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return staffDetailList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<EmployeeResult> searchEmployee(EmployeeCommand command,String logggedinUser,
			VbOrganization vbOrganization) throws DataAccessException {
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
		criteria.addOrder(Order.asc("employeeHierarchyLevel"));
		criteria.addOrder(Order.asc("username"));
		List<VbEmployee> staffList = criteria.list();
		if(!staffList.isEmpty()) {
			List<EmployeeResult> staffDetailList = new ArrayList<EmployeeResult>();
			EmployeeResult staffResult = null;
			VbEmployee loggedInEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
					.add(Restrictions.eq("username", logggedinUser))
					.add(Restrictions.eq("vbOrganization", vbOrganization))
					.uniqueResult();
			
			Integer loggedInUserHierarchyLevel = loggedInEmployee.getEmployeeHierarchyLevel();
			Integer employeeHierarchyLevel = 0;
			Boolean showDisable;
			String isEnabled=null;
			for (VbEmployee vbEmployee : staffList) {
				showDisable = Boolean.FALSE;
				employeeHierarchyLevel = vbEmployee.getEmployeeHierarchyLevel();
				staffResult = new EmployeeResult();
				if(loggedInUserHierarchyLevel < employeeHierarchyLevel) {
					showDisable = Boolean.TRUE;
				}
				isEnabled = (String) session.createCriteria(VbLogin.class)
						.setProjection(Projections.property("enabled"))
						.add(Restrictions.eq("vbOrganization", vbOrganization))
						.add(Restrictions.eq("username",vbEmployee.getUsername()))
						.uniqueResult()
						.toString();
				if (isEnabled != null) {
					staffResult.setIsEnabled(isEnabled);
				}
				staffResult.setId(vbEmployee.getId());
				staffResult.setFirstName(vbEmployee.getFirstName());
				staffResult.setLastName(vbEmployee.getLastName());
				staffResult.setUsername(vbEmployee.getUsername());
				staffResult.setShowDisable(String.valueOf(showDisable));
				staffResult.setEmployeeType(vbEmployee.getEmployeeType().toLowerCase());
				staffResult.setEmployeeTypeByString(DropDownUtil.getDropDown(DropDownUtil.EMPLOYEE_TYPE, vbEmployee.getEmployeeType()));
				staffResult.setLoginEmployeeType(loggedInEmployee.getEmployeeType());
				VbEmployeeDetail vbEmployeeDetail = (VbEmployeeDetail) session.createCriteria(VbEmployeeDetail.class)
						.add(Expression.eq("vbEmployee", vbEmployee))
						.uniqueResult();

				if (vbEmployeeDetail != null) {
					staffResult.setMobile(vbEmployeeDetail.getMobile());
				}
				staffDetailList.add(staffResult);
			}
			Collections.sort(staffDetailList);
			session.close();

			if (_logger.isInfoEnabled()) {
				_logger.info("{} employees have been found with in the organization: {}", staffDetailList.size(), vbOrganization.getName());
			}
			return staffDetailList;
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
     * This method is responsible to get all the disabled Employee from {@link VbLogin}.
     * 
     * @param organization - {@link VbOrganization}
     * @param username - {@link String}
     * @return disabledEmployee - {@link List}
	 * @throws DataAccessException  - {@link DataAccessException}
     * 
     */
    @SuppressWarnings("unchecked")
	public List<EmployeeResult> getDisabledEmployeeList(VbOrganization organization,String username) throws DataAccessException{
    	Session session = this.getSession();
    	List<VbLogin> vbLogins = session.createCriteria(VbLogin.class)
    			.add(Expression.eq("enabled", OrganizationUtils.LOGIN_DISABLED))
    			.list();
    	session.close();
    	if(vbLogins != null){
    		//Disabled organization list iteration with all employee under that organization
    		List<EmployeeResult> disabledEmployee = new ArrayList<EmployeeResult>();
    		for(VbLogin vbLogin : vbLogins){
    			EmployeeResult loginResults=new EmployeeResult();
    			loginResults.setUserName(vbLogin.getUsername());
    			disabledEmployee.add(loginResults);
    		}
    		
    		if(_logger.isInfoEnabled()) {
    			_logger.info("{} logins have been disabled under organization: {}", disabledEmployee.size(), organization.getName());
    		}
    		return disabledEmployee;
    	}else{
    		String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
    		
    		if(_logger.isWarnEnabled()) {
    			_logger.warn(message);
    		}
    		throw new DataAccessException(message);
    	}
    }
    
    /**
     * This method is responsible to Disable {@link VbEmployee} credentials from {@link VbLogin}.
     *  
     * @param employeeCommand - {@link EmployeeCommand}
     * @param employeeStatus - {@link String}
     * @return isModified - {@link Boolean}
     * @throws DataAccessException  - {@link DataAccessException}
     * 
     */
	public void modifyEmployeeStatus(EmployeeCommand employeeCommand,String employeeStatus,VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Integer id = employeeCommand.getId();
		VbEmployee employee = (VbEmployee) session.get(VbEmployee.class, id);
		if(employee != null) {
			Transaction tx = session.beginTransaction();
				VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
						.add(Expression.eq("username", employee.getUsername()))
						.uniqueResult();
				if (OrganizationUtils.LOGIN_ENABLED_STRING.equalsIgnoreCase(employeeStatus)) {
					login.setEnabled(OrganizationUtils.LOGIN_ENABLED);
					
					if (_logger.isInfoEnabled()) {
						_logger.info("Enabling login credentials of {}", employee.getUsername());
					}
				} else {
					login.setEnabled(OrganizationUtils.LOGIN_DISABLED);
					
					if (_logger.isInfoEnabled()) {
						_logger.info("Disabling login credentials of {}", employee.getUsername());
					}
			    }
				session.update(login);
				tx.commit();
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
	 * This method is used to get the employee based on the id.
	 * 
	 * @param id - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return VbEmployee - {@link VbEmployee}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public VbEmployee getEmployee(int id, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbEmployee employee = (VbEmployee) session.get(VbEmployee.class, id);
		session.close();
		
		if (employee != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("VbEmployee: {}", employee);
			}
			return employee;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}
	/**
	 * This method is used to get the employee based on the id.
	 * 
	 * @param id - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return VbEmployee - {@link VbEmployee}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public EmployeeResult getEmployeeData(int id, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbEmployee employee = (VbEmployee) session.get(VbEmployee.class,id);
		if (employee != null) {
			VbEmployeeDetail employeeDetails = (VbEmployeeDetail) session.createCriteria(VbEmployeeDetail.class)
					.add(Restrictions.eq("vbEmployee", employee)) 
					.uniqueResult();
			
			VbEmployeeAddress employeeAddress = (VbEmployeeAddress) session.createCriteria(VbEmployeeAddress.class)
					.add(Restrictions.eq("vbEmployee", employee)) 
					.uniqueResult();
			
			EmployeeResult employeeResult = new EmployeeResult();
			employeeResult.setAddressLine1(employeeAddress.getAddressLine1());
			employeeResult.setAddressLine2(employeeAddress.getAddressLine2());
			employeeResult.setAddressType(employeeAddress.getAddressType());
			employeeResult.setGrantedDays(employee.getGrantedDays());
			employeeResult.setDirectLine(employeeDetails.getDirectLine());
			employeeResult.setAlternateMobile(employeeDetails.getAlternateMobile());
			employeeResult.setBloodGroup(employeeDetails.getBloodGroup());
			employeeResult.setCity(employeeAddress.getCity());
			employeeResult.setEmployeeNumber(employee.getEmployeeNumber());
			employeeResult.setEmployeeEmail(employee.getEmployeeEmail());
			employeeResult.setEmployeeType(employee.getEmployeeType());
			employeeResult.setFirstName(employee.getFirstName());
			employeeResult.setGender(employee.getGender());
			employeeResult.setLandmark(employeeAddress.getLandmark());
			employeeResult.setLocality(employeeAddress.getLocality());
			employeeResult.setMiddleName(employee.getMiddleName());
			employeeResult.setLastName(employee.getLastName());
			employeeResult.setMobile(employeeDetails.getMobile());
			employeeResult.setNationality(employeeDetails.getNationality());
			employeeResult.setPassPortNumber(employeeDetails.getPassportNumber());
			employeeResult.setState(employeeAddress.getState());
			employeeResult.setUserName(employee.getUsername());
			employeeResult.setZipcode(employeeAddress.getZipcode());
			session.close();
			
			if (_logger.isInfoEnabled()) {
				_logger.info("VbEmployee: {}", employeeResult);
			}
			return employeeResult;
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
	 * This Method Is Responsible To display List Of Employee Details Based On
	 * Given Id
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @return staffList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<VbEmployee> getEmployeeList(VbOrganization vbOrganization) throws DataAccessException {
		Session session = this.getSession();
		List<VbEmployee> staffList = session.createCriteria(VbEmployee.class)
				.add(Expression.eq("vbOrganization", vbOrganization))
				.list();
		session.close();
		
		if(staffList != null) {
			if (_logger.isInfoEnabled()) {
				_logger.info("{} employees found under the Organization: {}", staffList.size(), vbOrganization.getName());
			}
			return staffList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to load {@link VbOrganization}.
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @return organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public VbOrganization loadOrganization(VbOrganization vbOrganization) throws DataAccessException {
		Session session = this.getSession();
		VbOrganization organization = (VbOrganization) session.get(VbOrganization.class, vbOrganization.getId());
		session.close();
		
		if(organization != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("vbOrganization: {}", organization);
			}
			return organization;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}
	
	
	/**
	 * This method is responsible to get all the employee types based on {@link VbOrganization}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return employeeTypes - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getEmployeeTypes(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> employeeTypes = session.createCriteria(VbRole.class)
				.setProjection(Projections.property("description"))
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		session.close();
		
		if(employeeTypes != null) {
			if(_logger.isInfoEnabled()) {
				_logger.info("{} employee types have been found under organization: {}", employeeTypes.size(), organization.getName());
			}
			return employeeTypes;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}
	
	/**
	 * This method is responsible to generate employee number based on {@link VbOrganization} and employee Types.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param employeeType - {@link String}
	 * @return employeeNo - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public String generateEmployeeNumber(VbOrganization organization, String employeeType) throws DataAccessException{
		Session session = null;
		try {
			session = this.getSession();
			String employeeNo = generateEmployeeNo(session, organization, employeeType);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Generated employeeNo: {}", employeeNo);
			}
			return employeeNo;
		} catch (HibernateException exception) {
			String errorMsg = Msg.get(MsgEnum.EMPLOYEE_NUMBER_GENERATION_FAILURE_MESSAGE);
			
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
	 * This method is responsible to get all {@link VbLoginTrack} details of an {@link VbEmployee}.
	 * 
	 * @param id - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return trackResults - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<LoginTrackResult> getLoginTracks(Integer id, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbEmployee employee = (VbEmployee) session.get(VbEmployee.class, id);
		if(employee != null) {
			String userName = employee.getUsername();
			List<LoginTrackResult> trackResults = new ArrayList<LoginTrackResult>();
			List<VbLoginTrack> loginTracks = session.createCriteria(VbLoginTrack.class)
					.add(Restrictions.eq("username", userName))
					.add(Restrictions.eq("vbOrganization", organization))
					.list();
			session.close();
			if(! (loginTracks.isEmpty())) {
				LoginTrackResult trackResult = null;
				for (VbLoginTrack vbLoginTrack : loginTracks) {
					trackResult = new LoginTrackResult();
					trackResult.setUserName(userName);
					trackResult.setLoginTime(vbLoginTrack.getLastLoginTime());
					trackResults.add(trackResult);
				}
			} else {
				String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
				
				if(_logger.isWarnEnabled()) {
					_logger.warn(errorMsg);
				}
				throw new DataAccessException(errorMsg);
			}
			
			if(_logger.isInfoEnabled()) {
				_logger.info("{} login records have been found for {}", trackResults.size(), userName);
			}
			return trackResults;
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
	 * This method is responsible to prepare {@link EmployeeNotificationResult}
	 * instance for notifications.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return result - {@link EmployeeNotificationResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public EmployeeNotificationResult getEmployeeNotificationResult(
			String userName, VbOrganization organization)
			throws DataAccessException {
		Session session = this.getSession();
		String employeeType = (String) session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("employeeType"))
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		if (employeeType != null) {
			EmployeeNotificationResult result = new EmployeeNotificationResult();
			result.setEmployeeType(employeeType);
			result.setUserName(userName);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("EmployeeNotificationResult: {}", result);
			}
			return result;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is responsible to check user name is available or not.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailable - {@link String}
	 */
	public String checkUserNameVaialability(String userName, VbOrganization organization) {
		String isAvailable = "y";
		Session session = this.getSession();
		VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		if(login != null) {
			isAvailable = "n";
		}
		return isAvailable;
	}

	/**This method is responsible for checking if SE has any credits or products with him to disable him.
	 * 
	 * @param id - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return canDisable - {@link Boolean}
	 * @throws DataAccessException
	 */
	public Boolean checkForSalesExecutiveCredits(Integer id,VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Boolean canDisable=Boolean.FALSE;
		String errorMsg = null;
		final int countValueZero=Integer.valueOf(0);
		try {
			VbEmployee employee = (VbEmployee) session.get(VbEmployee.class, id);
			//Cheking for daybook close
			Integer dayBookNotClosedCount = (Integer) session.createQuery("select count(*) from VbSalesBook vb  where vb.salesExecutive =:salesExecutive and vb.flag =:flag AND vb.vbOrganization =:vbOrganization")
			                                                 .setParameter("salesExecutive", employee.getUsername())
			                                                 .setParameter("vbOrganization", organization)
			                                                 .setParameter("flag",Integer.valueOf(1)).uniqueResult();
			if(dayBookNotClosedCount == countValueZero){
				//getting recent sales id,to check for credits.
				Integer maxSalesId =(Integer) session.createQuery("select MAX(vbs.id) from VbSalesBook vbs where vbs.salesExecutive =:salesExecutive and vbs.flag =:flag AND vbs.vbOrganization =:organization")
						                             .setParameter("salesExecutive", employee.getUsername())
						                             .setParameter("organization", organization)
						                             .setParameter("flag",countValueZero).uniqueResult();
				if(maxSalesId != null){
					//Checking if SLE has closing balance with him
					Integer closingBalanceCount = (Integer) session.createQuery("select count(*) from VbSalesBook vbsb where vbsb.id =:id and vbsb.closingBalance > :closingBalance  AND vbsb.vbOrganization =:vbOrganization")
                                                         .setParameter("vbOrganization", organization)
                                                         .setParameter("closingBalance", new Float(0))
                                                         .setParameter("id",maxSalesId).uniqueResult();
					if(closingBalanceCount == countValueZero){
						//Checking if SLE has products with him
						Integer closingQtyProductCount = (Integer) session.createQuery("select count(*) from VbSalesBookProducts vbp where vbp.vbSalesBook.id =:id and vbp.qtyClosingBalance > :qtyClosingBalance")
                                                                          .setParameter("qtyClosingBalance",countValueZero)
                                                                          .setParameter("id",maxSalesId).uniqueResult();
						if(closingQtyProductCount == countValueZero){
							//Checking whether SLE has any awaiting sales return requests.
							Integer salesReturnPendingCount = (Integer) session.createQuery("select count(*) from VbSalesReturn vbsr where vbsr.createdBy =:salesExecutive and vbsr.status =:status and vbsr.vbOrganization =:vbOrganization")
                                                                               .setParameter("status",CRStatus.PENDING.name())
                                                                               .setParameter("salesExecutive", employee.getUsername())
                                                                               .setParameter("vbOrganization", organization)
                                                                               .uniqueResult();
							if(salesReturnPendingCount == countValueZero){
								//Checking whether SLE has any awaiting sales return change requests.
								Integer salesReturnCrPendingCount = (Integer) session.createQuery("select count(*) from VbSalesReturnChangeRequest vbcr where vbcr.createdBy=:salesExecutive and vbcr.status =:status and vbcr.vbOrganization =:vbOrganization")
                                                                                          .setParameter("status",CRStatus.PENDING.name())
                                                                                          .setParameter("vbOrganization", organization)
                                                                                          .setParameter("salesExecutive", employee.getUsername())
                                                                                          .uniqueResult();
								if(salesReturnCrPendingCount == countValueZero){
									//sending status to disable SLE
									canDisable=Boolean.TRUE;
								}else{
									errorMsg = Msg.get(MsgEnum.EMPLOYEE_HAS_CHANGE_REQUEST_PENDING);
									if (_logger.isWarnEnabled()) {
										_logger.warn(errorMsg);
									}
									throw new DataAccessException(errorMsg);
								}
							}else{
								errorMsg = Msg.get(MsgEnum.EMPLOYEE_HAS_REQUEST_PENDING);
								if (_logger.isWarnEnabled()) {
									_logger.warn(errorMsg);
								}
								throw new DataAccessException(errorMsg);
							}
							
						}else{
							errorMsg = Msg.get(MsgEnum.EMPLOYEE_HAS_PRODUCTS_CLOSING);
							if (_logger.isWarnEnabled()) {
								_logger.warn(errorMsg);
							}
							throw new DataAccessException(errorMsg);
						}
					}else{
						errorMsg = Msg.get(MsgEnum.EMPLOYEE_HAS_CLOSING_BALANCE);
						if (_logger.isWarnEnabled()) {
							_logger.warn(errorMsg);
						}
						throw new DataAccessException(errorMsg);
					}
				}else{
					//sending status to disable SLE
					canDisable=Boolean.TRUE;
				}
			}else{
				errorMsg = Msg.get(MsgEnum.EMPLOYEE_DAY_BOOK_NOT_CLOSED);
				if (_logger.isWarnEnabled()) {
					_logger.warn(errorMsg);
				}
				throw new DataAccessException(errorMsg);
			}
		} catch (HibernateException e) {
			errorMsg = Msg.get(MsgEnum.RESULT_DISABLE_FAILURE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return canDisable;
	}
	/**
	 * This method is responsible for returning configured addresstype for import functionlity
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAddressTypes(VbOrganization organization){
		Session session = this.getSession();
		List<String> addressTypes=session.createCriteria(VbAddressTypes.class)
				                 .add(Restrictions.eq("vbOrganization", organization))
				                 .list();
		session.close();
		return addressTypes;
	}
}