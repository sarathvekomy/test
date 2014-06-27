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
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	@SuppressWarnings("unchecked")
	public List<String> getOrganizations() {
		Session session = this.getSession();
		List<String> organizationList = null;
		organizationList = session.createCriteria(VbOrganization.class)
				.setProjection(Projections.property("name"))
				.add(Restrictions.ne("name", "User Group")).list();
		if (_logger.isDebugEnabled()) {
			_logger.debug("Organizations list is {}: ", organizationList);
		}
		return organizationList;
	}

	/**
	 * This Method is to Assign Organizations
	 * 
	 * @param manageUserBasicCommand
	 * @param manageUserCommand
	 * @param userName
	 */
	public void saveManageUser(ManageUserBasicCommand manageUserBasicCommand,
			ManageUserAddressCommand manageUserCommand, String userName) {
		Session session = this.getSession();
		Date date = new Date();
		// Persisting Management User Credentials.
		VbLogin vbLogin = new VbLogin();
		vbLogin.setFirstTime(OrganizationUtils.FIRST_TIME_LOGIN_YES);
		vbLogin.setUsername(manageUserBasicCommand.getUsername());
		vbLogin.setPassword(PasswordEncryption.encryptPassword(manageUserBasicCommand.getPassword()));
		vbLogin.setEnabled(OrganizationUtils.LOGIN_ENABLED);

		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbLogin: {}", vbLogin);
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
			_logger.debug("Persisting vbAuthority: {}", vbAuthorityUser);
		}
		session.save(vbAuthorityUser);

		VbAuthority vbAuthorityGroupHead = new VbAuthority();
		VbRole vbRole = (VbRole) session.createCriteria(VbRole.class)
				.add(Restrictions.eq("roleName", "ROLE_GROUPHEAD"))
				.uniqueResult();
		vbAuthorityGroupHead.setVbLogin(vbLogin);
		vbAuthorityGroupHead.setVbRole(vbRole);

		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting vbAuthority: {}", vbAuthorityGroupHead);
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
		vbEmployee.setFirstName(employeeName[0]);
		vbEmployee.setLastName(" ");
		if(employeeName.length == 2) {
		   vbEmployee.setFirstName(employeeName[0]);
		   vbEmployee.setLastName(employeeName[1]);
		} else if(employeeName.length > 2){
		   vbEmployee.setFirstName(employeeName[0]);
		   vbEmployee.setMiddleName(employeeName[1]);
		   vbEmployee.setLastName(employeeName[2]);
		}
		
		vbEmployee.setGender(manageUserBasicCommand.getGender());
		vbEmployee.setUsername(manageUserBasicCommand.getUsername());
		vbEmployee.setEmployeeType("ROLE_GROUPHEAD");
		vbEmployee.setVbOrganization(getOrganization());

		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting vbEmployee: {}", vbEmployee);
		}
		session.save(vbEmployee);

		// Persisting Management User Employment Details.
		VbEmployeeDetail vbEmployeeDetail = new VbEmployeeDetail();
		vbEmployeeDetail.setAlternateMobile(manageUserBasicCommand.getAlternateMobile());
		vbEmployeeDetail.setBloodGroup(manageUserBasicCommand.getBloodGroup());
		vbEmployeeDetail.setDirectLine(manageUserBasicCommand.getDirectLine());
		vbEmployeeDetail.setMobile(manageUserBasicCommand.getMobile());
		vbEmployeeDetail.setNationality(manageUserBasicCommand.getNationality());
		vbEmployeeDetail.setPassportNumber(manageUserBasicCommand
				.getPassportNumber());
		vbEmployeeDetail.setVbEmployee(vbEmployee);

		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting vbEmployeeDetail: {}", vbEmployeeDetail);
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
			_logger.debug("Persisting vbEmployeeAddress: {}", vbEmployeeAddress);
		}
		session.save(vbEmployeeAddress);

		// Assigning Organizations to management user.
		String[] organizations = manageUserCommand.getOrganizations()
				.split(",");
		List<String> organizationList = Arrays.asList(organizations);
		VbAssignOrganizations vbAssignOrganizations = null;
		for (String organizationName : organizationList) {
			vbAssignOrganizations = new VbAssignOrganizations();
			VbOrganization organization = (VbOrganization) session
					.createCriteria(VbOrganization.class)
					.add(Restrictions.eq("name", organizationName))
					.uniqueResult();
			vbAssignOrganizations.setCreatedBy(userName);
			vbAssignOrganizations.setCreatedOn(date);
			vbAssignOrganizations.setModifiedOn(date);
			vbAssignOrganizations.setVbEmployee(vbEmployee);
			vbAssignOrganizations.setVbOrganization(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbAssignOrganizations: {}", vbAssignOrganizations);
			}
			session.save(vbAssignOrganizations);
		}
	}

	/**
	 * This method is responsible to get {@link VbOrganization} for User Group.
	 * 
	 * @return organization - {@link VbOrganization}
	 * 
	 */
	private VbOrganization getOrganization() {
		Session session = this.getSession();
		VbOrganization organization = (VbOrganization) session
				.createCriteria(VbOrganization.class)
				.add(Restrictions.eq("name", "User Group"))
				.uniqueResult();

		if (organization == null) {
			return null;
		}

		return organization;
	}

	/**
	 * This Method Is To Search All Assigned Organization
	 * 
	 * @param manageUserCommand
	 * @return {@link ManageUserResult}
	 */
	@SuppressWarnings("unchecked")
	public List<ManageUserResult> searchUser(
			ManageUserBasicCommand manageUserCommand) {
		if (_logger.isDebugEnabled()) {
			_logger.debug("OrganizationCommand: {}", manageUserCommand);
		}
		Session session = this.getSession();
		ArrayList<ManageUserResult> userList = new ArrayList<ManageUserResult>();
		VbOrganization vbOrganization = getOrganization();
		Criteria empCriteria = session.createCriteria(VbEmployee.class).createAlias("vbOrganization", "organization");
		if (manageUserCommand != null) {
			String username = manageUserCommand.getUsername();
			String firstName = manageUserCommand.getFullName();
			String organizationName = manageUserCommand.getOrganizationName();
			if (!StringUtils.isEmpty(username)) {
				empCriteria.add(Restrictions.like("username", username, MatchMode.START).ignoreCase());
			}
			if (!StringUtils.isEmpty(firstName)) {
				empCriteria.add(Restrictions.like("firstName", firstName, MatchMode.START).ignoreCase());
			}
			if(!StringUtils.isEmpty(organizationName)){
				List<Integer> orgCriteria=session.createCriteria(VbOrganization.class)
	                    .setProjection(Projections.property("id"))
	                    .add(Restrictions.like("name", organizationName, MatchMode.START).ignoreCase())
	                    .list();
				if(!orgCriteria.isEmpty()){
						List<Integer> empId=session.createCriteria(VbAssignOrganizations.class)
								          .createAlias("vbOrganization", "organization")
								          .setProjection(Projections.property("vbEmployee.id"))
								          .add(Restrictions.in("organization.id", orgCriteria))
								          .list();
						if(!empId.isEmpty()){
							empCriteria.add(Restrictions.in("id",empId));
						}
				}
			}
		}
		ManageUserResult manageUserResult = null;
		empCriteria.add(Restrictions.eq("vbOrganization", vbOrganization));
		List<VbEmployee> userDetails = empCriteria.list();
		VbEmployee employee = null;
		for (VbEmployee vbEmployee : userDetails) {
			manageUserResult = new ManageUserResult();
			manageUserResult.setId(vbEmployee.getId());
			manageUserResult.setFullName(vbEmployee.getFirstName());
			manageUserResult.setUsername(vbEmployee.getUsername());
			manageUserResult.setEmployeeEmail(vbEmployee.getEmployeeEmail());
			employee = (VbEmployee) session.get(VbEmployee.class,
					vbEmployee.getId());
			VbEmployeeDetail vbEmployeeDetail = (VbEmployeeDetail) session
					.createCriteria(VbEmployeeDetail.class)
					.add(Expression.eq("vbEmployee", employee)).uniqueResult();

			if (vbEmployeeDetail != null) {
				manageUserResult.setMobile(vbEmployeeDetail.getMobile());
			}
			userList.add(manageUserResult);
		}
		if (_logger.isDebugEnabled()) {
			_logger.debug("organizationList: {}", userList);
		}
		session.close();
		return userList;
	}

	/**
	 * This Method Is To retrieve The UserDetails Based on given id
	 * 
	 * @param id
	 * @return {@link VbEmployee}
	 */
	public VbEmployee getUser(int id) {

		Session session = this.getSession();
		VbEmployee instance = (VbEmployee) session.get(VbEmployee.class, id);

		if (_logger.isDebugEnabled()) {
			_logger.debug("VbEmployee: {}", instance);
		}
		return instance;
	}

	/**
	 * This Method is to delete assignedOrganizations
	 * 
	 * @param manageUserBasicCommand
	 */
	public void deleteUser(ManageUserBasicCommand manageUserBasicCommand) {
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		VbEmployee employee = (VbEmployee) session.get(VbEmployee.class, manageUserBasicCommand.getId());
		if (employee != null) {
			String userName = employee.getUsername();
			VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
					.add(Restrictions.eq("username", userName))
					.uniqueResult();
			if (login != null) {
				login.setEnabled(OrganizationUtils.LOGIN_DISABLED);
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("Login have been disabled for the user name: {}",
						userName);
			}
			session.update(login);
		}
		tx.commit();
		session.close();
	}

	/**
	 * This method is to Change or Update existed details
	 * 
	 * @param manageUserBasicCommand
	 * @param manageUserCommand
	 * @param username
	 */
	public void updateUser(ManageUserBasicCommand manageUserBasicCommand,
			ManageUserAddressCommand manageUserCommand, String username) {
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		VbOrganization vbOrganization = getOrganization();
		Date date = new Date();
		VbEmployee vbEmployee = (VbEmployee) session
				.createCriteria(VbEmployee.class)
				.add(Expression.eq("id", manageUserBasicCommand.getId()))
				.add(Expression.eq("vbOrganization", vbOrganization))
				.uniqueResult();

		if (vbEmployee != null) {
			vbEmployee.setUsername(manageUserBasicCommand.getUsername());
			vbEmployee.setFirstName(manageUserBasicCommand.getFullName());
			vbEmployee.setEmployeeEmail(manageUserBasicCommand.getEmployeeEmail());
			vbEmployee.setModifiedOn(new Date());
			vbEmployee.setModifiedBy(username);
			vbEmployee.setVbOrganization(vbOrganization);
			vbEmployee.setGender(manageUserBasicCommand.getGender());

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
				.add(Expression.eq("employee.id", manageUserBasicCommand.getId()))
				.add(Expression.eq("employee.vbOrganization", vbOrganization))
				.uniqueResult();

		if (vbEmployeeDetail != null) {
			vbEmployeeDetail.setAlternateMobile(manageUserBasicCommand.getAlternateMobile());
			vbEmployeeDetail.setBloodGroup(manageUserBasicCommand.getBloodGroup());
			vbEmployeeDetail.setMobile(manageUserBasicCommand.getMobile());
			vbEmployeeDetail.setNationality(manageUserBasicCommand.getNationality());
			vbEmployeeDetail.setDirectLine(manageUserBasicCommand.getDirectLine());
			vbEmployeeDetail.setPassportNumber(manageUserBasicCommand.getPassportNumber());

			if (_logger.isDebugEnabled()) {
				_logger.debug("Updating VBEmployeeDetail: {}", vbEmployeeDetail);
			}
			session.saveOrUpdate(vbEmployeeDetail);

			VbEmployeeAddress vbEmployeeAddress = (VbEmployeeAddress) session
					.createCriteria(VbEmployeeAddress.class)
					.createAlias("vbEmployee", "employee")
					.add(Expression.eq("employee.id", manageUserBasicCommand.getId()))
					.add(Expression.eq("employee.vbOrganization", vbOrganization)).uniqueResult();

			if (vbEmployeeAddress != null) {
				vbEmployeeAddress.setAddressLine1(manageUserCommand.getAddressLine1());
				vbEmployeeAddress.setAddressLine2(manageUserCommand.getAddressLine2());
				vbEmployeeAddress.setAddressType(manageUserCommand.getAddressType());
				vbEmployeeAddress.setCity(manageUserCommand.getCity());
				vbEmployeeAddress.setLandmark(manageUserCommand.getLandmark());
				vbEmployeeAddress.setLocality(manageUserCommand.getLocality());
				vbEmployeeAddress.setZipcode(manageUserCommand.getZipcode());
				vbEmployeeAddress.setState(manageUserCommand.getState());

				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbEmployeeAddress: {}", vbEmployeeAddress);
				}
				session.saveOrUpdate(vbEmployeeAddress);
			}
		}
		// Assigning Organizations to management user.
		String[] organizations = manageUserCommand.getOrganizations().split(",");
		List<String> organizationList = Arrays.asList(organizations);
		VbAssignOrganizations vbAssignOrganizations = null;
		Integer assignorgId = (Integer) session
				.createCriteria(VbAssignOrganizations.class)
				.createAlias("vbEmployee", "employee")
				.setProjection(Projections.property("id"))
				.add(Expression.eq("employee.id", manageUserBasicCommand.getId()))
				.add(Expression.eq("employee.vbOrganization", vbOrganization))
				.uniqueResult();

		for (String organizationName : organizationList) {
			vbAssignOrganizations = new VbAssignOrganizations();
			VbOrganization organization = (VbOrganization) session
					.createCriteria(VbOrganization.class)
					.add(Restrictions.eq("name", organizationName))
					// .add(Expression.eq("id", manageUserBasicCommand.getId()))
					// .add(Expression.eq("employee.vbOrganization",
					// vbOrganization))
					.uniqueResult();
			vbAssignOrganizations = (VbAssignOrganizations) session.get(VbAssignOrganizations.class, assignorgId);
			vbAssignOrganizations.setCreatedBy(username);
			vbAssignOrganizations.setCreatedOn(date);
			vbAssignOrganizations.setModifiedOn(date);
			vbAssignOrganizations.setVbEmployee(vbEmployee);
			vbAssignOrganizations.setVbOrganization(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbAssignOrganizations: {}", vbAssignOrganizations);
			}
			session.update(vbAssignOrganizations);
		}
		tx.commit();
		session.close();
	}

	/*
	 * *This Method is To retrieve Assigned organizations based on particular id
	 * 
	 * @param id
	 * 
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAssignedOrganization(int id) {
		Session session = this.getSession();
		ArrayList<String> organizationList = (ArrayList<String>) session
				.createCriteria(VbAssignOrganizations.class)
				.createAlias("vbOrganization", "organization")
				.setProjection(Projections.property("organization.name"))
				.add(Restrictions.eq("vbEmployee", getUser(id))).list();

		return organizationList;
	}
}
