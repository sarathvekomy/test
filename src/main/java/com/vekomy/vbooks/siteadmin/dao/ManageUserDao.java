/**
 * com.vekomy.vbooks.siteadmin.dao.ManageUserDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jun 26, 2013
 */
package com.vekomy.vbooks.siteadmin.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.employee.command.EmployeeResult;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbAssignOrganizations;
import com.vekomy.vbooks.hibernate.model.VbAuthority;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbEmployeeAddress;
import com.vekomy.vbooks.hibernate.model.VbEmployeeDetail;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbRole;
import com.vekomy.vbooks.security.PasswordEncryption;
import com.vekomy.vbooks.siteadmin.command.ManageUserAddressCommand;
import com.vekomy.vbooks.siteadmin.command.ManageUserBasicCommand;
import com.vekomy.vbooks.siteadmin.command.ManageUserResult;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * @author swarupa
 * 
 */
public class ManageUserDao extends BaseDao {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ManageUserDao.class);

	/**
	 * This method is responsible to get all configured {@link VbOrganization}.
	 * 
	 * @return organizationList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getOrganizations() throws DataAccessException {
		Session session = this.getSession();
		List<String> organizationList = session.createCriteria(VbOrganization.class)
				.setProjection(Projections.property("name"))
				.add(Restrictions.ne("name", "User Group"))
				.list();
		if(!organizationList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", organizationList.size());
			}
			return organizationList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()){
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is to assign {@link VbOrganization} to a Group user.
	 * 
	 * @param manageUserBasicCommand - {@link ManageUserBasicCommand}
	 * @param manageUserCommand - {@link ManageUserAddressCommand}
	 * @param userName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void saveManageUser(ManageUserBasicCommand manageUserBasicCommand,
			ManageUserAddressCommand manageUserCommand, String userName) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date date = new Date();
			// Persisting Management User Credentials.
			VbOrganization manageUserOrganization = getOrganization(session);
			VbLogin vbLogin = new VbLogin();
			vbLogin.setFirstTime(OrganizationUtils.FIRST_TIME_LOGIN_YES);
			vbLogin.setUsername(manageUserBasicCommand.getUsername());
			vbLogin.setPassword(PasswordEncryption.encryptPassword(manageUserBasicCommand.getPassword()));
			vbLogin.setEnabled(OrganizationUtils.LOGIN_ENABLED);
			vbLogin.setVbOrganization(manageUserOrganization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbLogin");
			}
			session.save(vbLogin);

			// Assigning authorities.
			VbRole userRole = (VbRole) session.createCriteria(VbRole.class)
					.add(Restrictions.eq("roleName", "ROLE_USER"))
					.uniqueResult();
			VbAuthority vbAuthorityUser = new VbAuthority();
			vbAuthorityUser.setVbLogin(vbLogin);
			vbAuthorityUser.setVbRole(userRole);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbAuthority");
			}
			session.save(vbAuthorityUser);

			VbAuthority vbAuthorityGroupHead = new VbAuthority();
			VbRole vbRole = (VbRole) session.createCriteria(VbRole.class)
					.add(Restrictions.eq("roleName", "ROLE_GROUPHEAD"))
					.uniqueResult();
			vbAuthorityGroupHead.setVbLogin(vbLogin);
			vbAuthorityGroupHead.setVbRole(vbRole);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbAuthority");
			}
			session.save(vbAuthorityGroupHead);

			// Persisting Management User Employment Details.
			VbEmployee vbEmployee = new VbEmployee();
			vbEmployee.setCreatedBy(userName);
			vbEmployee.setCreatedOn(date);
			vbEmployee.setModifiedOn(date);
			vbEmployee.setEmployeeEmail(manageUserBasicCommand.getEmployeeEmail());
			
			// Setting first, middle and last names of an employee from full name.
			String fullName = manageUserBasicCommand.getFullName();
			String[] employeeName = fullName.split(" ");
			vbEmployee.setFirstName(employeeName[0].trim());
			if(employeeName.length == 2) {
			   vbEmployee.setLastName(employeeName[1].trim());
			} else if(employeeName.length == 3){
			   vbEmployee.setMiddleName(employeeName[1].trim());
			   vbEmployee.setLastName(employeeName[2].trim());
			} else {
				vbEmployee.setLastName("");
			}
			vbEmployee.setGender(manageUserBasicCommand.getGender());
			vbEmployee.setUsername(manageUserBasicCommand.getUsername());
			String employeeType = "GROUPHEAD";
			VbOrganization vbOrganization = getOrganization(session);
			String employeeNo = generateEmployeeNo(session, vbOrganization, employeeType);
			vbEmployee.setEmployeeNumber(employeeNo);
			vbEmployee.setEmployeeType(employeeType);
			vbEmployee.setEmployeeHierarchyLevel(new Integer(0));
			vbEmployee.setVbOrganization(vbOrganization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbEmployee");
			}
			session.save(vbEmployee);

			// Persisting Management User Employment Details.
			VbEmployeeDetail vbEmployeeDetail = new VbEmployeeDetail();
			vbEmployeeDetail.setAlternateMobile(manageUserBasicCommand.getAlternateMobile());
			vbEmployeeDetail.setBloodGroup(manageUserBasicCommand.getBloodGroup());
			vbEmployeeDetail.setDirectLine(manageUserBasicCommand.getDirectLine());
			vbEmployeeDetail.setMobile(manageUserBasicCommand.getMobile());
			vbEmployeeDetail.setNationality(manageUserBasicCommand.getNationality());
			vbEmployeeDetail.setPassportNumber(manageUserBasicCommand.getPassportNumber());
			vbEmployeeDetail.setVbEmployee(vbEmployee);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbEmployeeDetail");
			}
			session.save(vbEmployeeDetail);

			// Persisting Management User Employment Address.
			VbEmployeeAddress vbEmployeeAddress = new VbEmployeeAddress();
			vbEmployeeAddress.setAddressLine1(manageUserCommand.getAddressLine1());
			vbEmployeeAddress.setAddressLine2(manageUserCommand.getAddressLine2());
			vbEmployeeAddress.setAddressType(manageUserCommand.getAddressType());
			vbEmployeeAddress.setCity(manageUserCommand.getCity());
			vbEmployeeAddress.setLandmark(manageUserCommand.getLandmark());
			vbEmployeeAddress.setLocality(manageUserCommand.getLocality());
			vbEmployeeAddress.setState(manageUserCommand.getState());
			vbEmployeeAddress.setZipcode(manageUserCommand.getZipcode());
			vbEmployeeAddress.setVbEmployee(vbEmployee);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbEmployeeAddress");
			}
			session.save(vbEmployeeAddress);

			// Assigning Organizations to management user.
			String[] organizations = manageUserCommand.getOrganizations().split(",");
			List<String> organizationList = Arrays.asList(organizations);
			VbAssignOrganizations vbAssignOrganizations = null;
			for (String organizationName : organizationList) {
				vbAssignOrganizations = new VbAssignOrganizations();
				VbOrganization organization = (VbOrganization) session.createCriteria(VbOrganization.class)
						.add(Restrictions.eq("name", organizationName))
						.uniqueResult();
				vbAssignOrganizations.setCreatedBy(userName);
				vbAssignOrganizations.setCreatedOn(date);
				vbAssignOrganizations.setModifiedOn(date);
				vbAssignOrganizations.setVbEmployee(vbEmployee);
				vbAssignOrganizations.setVbOrganization(organization);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting vbAssignOrganizations");
				}
				session.save(vbAssignOrganizations);
			}
			txn.commit();
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String message = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);
			if(_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible to get {@link VbOrganization} for User Group.
	 * 
	 * @return organization - {@link VbOrganization}
	 * 
	 */
	private VbOrganization getOrganization(Session session) {
		VbOrganization organization = (VbOrganization) session.createCriteria(VbOrganization.class)
				.add(Restrictions.eq("name", "User Group"))
				.uniqueResult();

		return organization;
	}

	/**
	 * This method is to search all assigned {@link VbOrganization} of a Group user.
	 * 
	 * @param manageUserCommand - {@link ManageUserBasicCommand}
	 * @return {@link ManageUserResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<ManageUserResult> searchUser(ManageUserBasicCommand manageUserCommand) throws DataAccessException {
		Session session = this.getSession();
		VbOrganization vbOrganization = getOrganization(session);
		Criteria criteria = session.createCriteria(VbEmployee.class);
		if (manageUserCommand != null) {
			String username = manageUserCommand.getUsername();
			String fullName = manageUserCommand.getFullName().trim();
			String organizationName = manageUserCommand.getOrganizationName();
			if (!StringUtils.isEmpty(username)) {
				criteria.add(Restrictions.like("username", username, MatchMode.START).ignoreCase());
			}
			if (!StringUtils.isEmpty(fullName)) {
				if(fullName.contains(" ")) {
					String[] fullNameArray = fullName.split(" ");
					if(fullNameArray.length == 3) {
						criteria.add(Restrictions.like("firstName", fullNameArray[0], MatchMode.START).ignoreCase());
						criteria.add(Restrictions.like("middleName", fullNameArray[1], MatchMode.START).ignoreCase());
						criteria.add(Restrictions.like("lastName", fullNameArray[2], MatchMode.START).ignoreCase());
					} else if (fullNameArray.length == 2) {
						criteria.add(Restrictions.like("firstName", fullNameArray[0], MatchMode.START).ignoreCase());
						criteria.add(Restrictions.like("lastName", fullNameArray[1], MatchMode.START).ignoreCase());
					}
				} else {
					criteria.add(Restrictions.like("firstName", fullName, MatchMode.START).ignoreCase());
				}
			}
			if(!StringUtils.isEmpty(organizationName)){
				List<VbOrganization> organizationList = session.createCriteria(VbOrganization.class)
	                    .add(Restrictions.like("name", organizationName, MatchMode.START).ignoreCase())
	                    .list();
				if (!organizationList.isEmpty()) {
					List<Integer> empId = session.createCriteria(VbAssignOrganizations.class)
							.setProjection(Projections.property("vbEmployee.id"))
							.add(Restrictions.in("vbOrganization", organizationList))
							.list();
					if (!empId.isEmpty()) {
						criteria.add(Restrictions.in("id", empId));
					} else {
						session.close();
						String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
						
						if(_logger.isWarnEnabled()){
							_logger.warn(message);
						}
						throw new DataAccessException(message);
					}
				} else {
					session.close();
					String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
					
					if(_logger.isWarnEnabled()){
						_logger.warn(message);
					}
					throw new DataAccessException(message);
				}
			}
		}
		criteria.add(Restrictions.eq("vbOrganization", vbOrganization));
		List<VbEmployee> userDetails = criteria.list();
		
		if(!userDetails.isEmpty()) {
			VbEmployee vbEmployee = null;
			VbEmployeeDetail vbEmployeeDetail = null;
			ManageUserResult manageUserResult = null;
			ArrayList<ManageUserResult> manageUserList = new ArrayList<ManageUserResult>();
			for (VbEmployee employee : userDetails) {
				manageUserResult = new ManageUserResult();
				manageUserResult.setId(employee.getId());
				String middleName = employee.getMiddleName();
				if(middleName != null) {
					manageUserResult.setFullName(employee.getFirstName() + ' ' + middleName + ' ' + employee.getLastName());
				} else {
					manageUserResult.setFullName(employee.getFirstName() + ' ' + employee.getLastName());
				}
				manageUserResult.setUsername(employee.getUsername());
				manageUserResult.setEmployeeEmail(employee.getEmployeeEmail());
				vbEmployee = (VbEmployee) session.get(VbEmployee.class, employee.getId());
				vbEmployeeDetail = (VbEmployeeDetail) session.createCriteria(VbEmployeeDetail.class)
						.add(Expression.eq("vbEmployee", vbEmployee))
						.uniqueResult();
				if (vbEmployeeDetail != null) {
					manageUserResult.setMobile(vbEmployeeDetail.getMobile());
				}
				manageUserList.add(manageUserResult);
			}
			session.close();
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", manageUserList.size());
			}
			return manageUserList;
		} else {
			session.close();
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()){
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}
	/**
	 * This method is used to get the employee based on the id.
	 * 
	 * @param id - {@link Integer}
	 * @return VbEmployee - {@link VbEmployee}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public EmployeeResult getEmployeeData(Integer id) throws DataAccessException {
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
	 * This method is to disable login credentials of a Group user.
	 * 
	 * @param manageUserBasicCommand - {@link ManageUserBasicCommand}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void deleteUser(ManageUserBasicCommand manageUserBasicCommand) throws DataAccessException {
		Session session = this.getSession();
		VbEmployee employee = (VbEmployee) session.get(VbEmployee.class, manageUserBasicCommand.getId());
		if (employee != null) {
			Transaction tx = session.beginTransaction();
			String userName = employee.getUsername();
			VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
					.add(Restrictions.eq("username", userName))
					.uniqueResult();
			if (login != null) {
				login.setEnabled(OrganizationUtils.LOGIN_DISABLED);
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("Login have been disabled for the user: {}", userName);
			}
			session.update(login);
			tx.commit();
			session.close();
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()){
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is to modify the Group user's details.
	 * 
	 * @param manageUserBasicCommand - {@link ManageUserBasicCommand}
	 * @param manageUserCommand - {@link ManageUserAddressCommand}
	 * @param userName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void updateUser(ManageUserBasicCommand manageUserBasicCommand,
			ManageUserAddressCommand manageUserCommand, String userName) throws DataAccessException {
		Session session = this.getSession();
		VbEmployee vbEmployee = (VbEmployee) session.get(VbEmployee.class, manageUserBasicCommand.getId());
		if (vbEmployee != null) {
			Transaction tx = null;
			try {
				Date date = new Date();
				VbOrganization vbOrganization = getOrganization(session);
				tx = session.beginTransaction();
				
				String fullName = manageUserBasicCommand.getFullName();
				String[] employeeName = fullName.split(" ");
				vbEmployee.setFirstName(employeeName[0].trim());
				if(employeeName.length == 2) {
				   vbEmployee.setMiddleName(null);
				   vbEmployee.setLastName(employeeName[1].trim());
				} else if(employeeName.length == 3 ){
				   vbEmployee.setMiddleName(employeeName[1].trim());
				   vbEmployee.setLastName(employeeName[2].trim());
				}
				vbEmployee.setUsername(manageUserBasicCommand.getUsername());
				vbEmployee.setEmployeeEmail(manageUserBasicCommand.getEmployeeEmail());
				vbEmployee.setModifiedOn(new Date());
				vbEmployee.setModifiedBy(userName);
				vbEmployee.setVbOrganization(vbOrganization);
				vbEmployee.setGender(manageUserBasicCommand.getGender());

				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbEmployee");
				}
				session.saveOrUpdate(vbEmployee);
				
				VbEmployeeDetail vbEmployeeDetail = (VbEmployeeDetail) session.createCriteria(VbEmployeeDetail.class)
						.add(Expression.eq("vbEmployee", vbEmployee))
						.uniqueResult();
				vbEmployeeDetail.setAlternateMobile(manageUserBasicCommand.getAlternateMobile());
				vbEmployeeDetail.setBloodGroup(manageUserBasicCommand.getBloodGroup());
				vbEmployeeDetail.setMobile(manageUserBasicCommand.getMobile());
				vbEmployeeDetail.setNationality(manageUserBasicCommand.getNationality());
				vbEmployeeDetail.setDirectLine(manageUserBasicCommand.getDirectLine());
				vbEmployeeDetail.setPassportNumber(manageUserBasicCommand.getPassportNumber());

				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VBEmployeeDetail");
				}
				session.saveOrUpdate(vbEmployeeDetail);

				VbEmployeeAddress vbEmployeeAddress = (VbEmployeeAddress) session.createCriteria(VbEmployeeAddress.class)
						.add(Expression.eq("vbEmployee", vbEmployee))
						.uniqueResult();
				vbEmployeeAddress.setAddressLine1(manageUserCommand.getAddressLine1());
				vbEmployeeAddress.setAddressLine2(manageUserCommand.getAddressLine2());
				vbEmployeeAddress.setAddressType(manageUserCommand.getAddressType());
				vbEmployeeAddress.setCity(manageUserCommand.getCity());
				vbEmployeeAddress.setLandmark(manageUserCommand.getLandmark());
				vbEmployeeAddress.setLocality(manageUserCommand.getLocality());
				vbEmployeeAddress.setZipcode(manageUserCommand.getZipcode());
				vbEmployeeAddress.setState(manageUserCommand.getState());

				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbEmployeeAddress");
				}
				session.saveOrUpdate(vbEmployeeAddress);
				
				//Deleting the existing organizations.
				List<VbAssignOrganizations> existingOrgList = session.createCriteria(VbAssignOrganizations.class)
						.add(Expression.eq("vbEmployee", vbEmployee))
						.list();
				for (VbAssignOrganizations vbAssignOrganizations : existingOrgList) {
					session.delete(vbAssignOrganizations);
				}
				// Assigning Organizations to management user.
				String[] organizations = manageUserCommand.getOrganizations().split(",");
				List<String> organizationList = Arrays.asList(organizations);
				VbAssignOrganizations vbAssignOrganizations = null;
				VbOrganization organization = null;
				for (String organizationName : organizationList) {
					vbAssignOrganizations = new VbAssignOrganizations();
					organization = (VbOrganization) session.createCriteria(VbOrganization.class)
							.add(Restrictions.eq("name", organizationName))
							.uniqueResult();
					vbAssignOrganizations.setCreatedBy(userName);
					vbAssignOrganizations.setCreatedOn(date);
					vbAssignOrganizations.setModifiedOn(date);
					vbAssignOrganizations.setVbEmployee(vbEmployee);
					vbAssignOrganizations.setVbOrganization(organization);

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting vbAssignOrganizations");
					}
					session.save(vbAssignOrganizations);
				}
				tx.commit();
			} catch (HibernateException exception) {
				if(tx != null) {
					tx.rollback();
				}
				String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
				
				if (_logger.isErrorEnabled()) {
					_logger.error(message);
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
	 * This method is to get all {@link VbAssignOrganizations} of a Group user based on id.
	 * 
	 * @param id - {@link Integer}
	 * @return organizationList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAssignedOrganization(Integer id) throws DataAccessException {
		Session session = this.getSession();
		VbEmployee vbEmployee = (VbEmployee) session.get(VbEmployee.class, id);
		ArrayList<String> organizationList = (ArrayList<String>) session.createCriteria(VbAssignOrganizations.class)
				.createAlias("vbOrganization", "organization")
				.setProjection(Projections.property("organization.name"))
				.add(Restrictions.eq("vbEmployee", vbEmployee))
				.list();
		session.close();
		
		if(!organizationList.isEmpty()) {
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", organizationList.size());
			}
			return organizationList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if (_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}
	/**
     * This method is responsible to Disable {@link VbAssignOrganization}, {@link VbLogin} and
     * {@link VbEmployee} from DB.
     * 
     * @param userId - {@link Integer}
     * @param userStatus - {@link String}
     * @param userName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
     * 
     */
	public void modifyUserStatus(Integer userId, String userStatus, String userName) throws DataAccessException {
    	Session session = this.getSession();
		//get manage user record from AssignOrganizations.
		VbEmployee manageUser = (VbEmployee) session.get(VbEmployee.class, userId);
		if(manageUser != null) {
			//make disable this employee userName in Login.
			VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
					.add(Expression.eq("username", manageUser.getUsername()))
					.add(Restrictions.eq("vbOrganization", getOrganization(session)))
					.uniqueResult();
			if(login != null) {
				Transaction tx = session.beginTransaction();
				//if user clicked enable button from UI
				if("Enabled".equals(userStatus)){
					login.setEnabled(OrganizationUtils.LOGIN_ENABLED);
				} else{ 
					//if user clicked disable button from UI
					if("Disabled".equals(userStatus)){
						login.setEnabled(OrganizationUtils.LOGIN_DISABLED);
				    }
				}
				
				if(_logger.isDebugEnabled()) {
					_logger.debug("Modifying login credentials for Manage user: {}", manageUser.getUsername());
				}
				session.update(login);
				tx.commit();
				session.close();
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
     * This method is responsible to get all the disabled Users from {@link VbLogin}.
     * 
     * @return disabledUsers - {@link List}
     * @throws DataAccessException - {@link DataAccessException}
     * 
     */
    @SuppressWarnings("unchecked")
	public List<ManageUserResult> getDisabledUsersList() throws DataAccessException{
    	Session session = this.getSession();
    	List<VbLogin> vbLoginsList = session.createCriteria(VbLogin.class)
    			.add(Expression.eq("enabled", OrganizationUtils.LOGIN_DISABLED))
    			.add(Restrictions.eq("vbOrganization", getOrganization(session)))
    			.list();
    	session.close();
    	
    	if(vbLoginsList != null){
    		ManageUserResult loginResults = null;
    		List<ManageUserResult> disabledUsersList = new ArrayList<ManageUserResult>();
    		for(VbLogin vbLogin : vbLoginsList){
    			loginResults = new ManageUserResult();
    			loginResults.setDisabledLoginUserName(vbLogin.getUsername());
    			
    			disabledUsersList.add(loginResults);
    		}
    		
    		if(_logger.isDebugEnabled()) {
    			_logger.debug("{} records have been found.", disabledUsersList.size());
    		}
    		return disabledUsersList;
    	} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if (_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
    }
}