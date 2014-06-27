package com.vekomy.vbooks.siteadmin.dao;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbAuthority;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbEmployeeAddress;
import com.vekomy.vbooks.hibernate.model.VbEmployeeDetail;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbOrganizationMapping;
import com.vekomy.vbooks.hibernate.model.VbRole;
import com.vekomy.vbooks.organization.command.OrganizationCommand;
import com.vekomy.vbooks.organization.command.OrganizationSuperUserCommand;
import com.vekomy.vbooks.security.PasswordEncryption;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * @author Sudhakar
 * 
 * 
 */
public class SiteAdminDao extends BaseDao {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SiteAdminDao.class);

	/**
	 * This method is responsible to persist {@link VbOrganization}.
	 * 
	 * @param organizationCommand - {@link OrganizationCommand}
	 * @param organizationSuperUserCommand - {@link OrganizationSuperUserCommand}
	 * @param username - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void saveOrganization(OrganizationCommand organizationCommand,
			OrganizationSuperUserCommand organizationSuperUserCommand,
			String username)
			throws com.vekomy.vbooks.exception.DataAccessException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = this.getSession();
			transaction = session.beginTransaction();
			Date date = new Date();
			String superUserName = organizationSuperUserCommand.getSuperUsername().toString();
			VbOrganization organization = new VbOrganization();
			organization.setAddressLine1(organizationCommand.getAddressLine1());
			organization.setAddressLine2(organizationCommand.getAddressLine2());
			organization.setBranchName(organizationCommand.getBranchName());
			organization.setFullName(organizationSuperUserCommand.getFullName());
			organization.setMainBranch(organizationCommand.getMainBranch());
			organization.setCity(organizationCommand.getCity());
			organization.setCountry(organizationCommand.getCountry());
			organization.setCurrencyFormat(organizationCommand.getCurrencyFormat());
			organization.setDescription(organizationCommand.getDescription());
			organization.setLandmark(organizationCommand.getLandmark());
			organization.setLocality(organizationCommand.getLocality());
			organization.setName(organizationCommand.getName());
			organization.setPhone1(organizationCommand.getPhone1());
			organization.setPhone2(organizationCommand.getPhone2());
			organization.setMobile(organizationSuperUserCommand.getMobile());
			organization.setAlternateMobile(organizationSuperUserCommand.getAlternateMobile());
			organization.setUsernamePrefix(organizationCommand.getUsernamePrefix());
			organization.setZipcode(organizationCommand.getZipcode());
			organization.setOrganizationCode(organizationCommand.getOrganizationCode());
			organization.setState(organizationCommand.getState());
			organization.setEmail(organizationSuperUserCommand.getEmail());
			organization.setCreatedBy(username);
			organization.setCreatedOn(date);
			organization.setModifiedOn(date);
			organization.setSuperUserName(superUserName);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting organization: {}", organization);
			}
			session.save(organization);
				
			VbOrganization vbOrganization = (VbOrganization) session.createCriteria(VbOrganization.class)
					.add(Restrictions.eq("name", organizationCommand.getMainBranchName()))
					.uniqueResult();
			
			VbOrganizationMapping organizationMapping = new VbOrganizationMapping();
			organizationMapping.setVbOrganizationByMainBranchId(vbOrganization);
			organizationMapping.setVbOrganizationBySubBranchId(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting organizationMapping: {}", organizationMapping);
			}
			session.save(organizationMapping);
			// We enforce to create Super Management User
			// Organizations can login as this user and modify rest of the data
			VbLogin vbLogin = new VbLogin();
			vbLogin.setUsername(superUserName);
			vbLogin.setPassword(PasswordEncryption.encryptPassword(organizationSuperUserCommand.getPassword()));
			vbLogin.setEnabled(OrganizationUtils.LOGIN_ENABLED);
			vbLogin.setFirstTime(OrganizationUtils.FIRST_TIME_LOGIN_YES);
			vbLogin.setWrongPasswordCount(new Integer(0));
			vbLogin.setVbOrganization(organization);
			session.save(vbLogin);

			VbRole userRole = (VbRole) session.get(VbRole.class, OrganizationUtils.ROLE_USER);
			VbAuthority vbAuthorityUser = new VbAuthority();
			vbAuthorityUser.setVbLogin(vbLogin);
			vbAuthorityUser.setVbRole(userRole);
			session.save(vbAuthorityUser);

			VbRole superMgtRole = (VbRole) session.get(VbRole.class, OrganizationUtils.SUPER_MANAGEMENT);
			VbAuthority vbAuthorityMgmt = new VbAuthority();
			vbAuthorityMgmt.setVbLogin(vbLogin);
			vbAuthorityMgmt.setVbRole(superMgtRole);
			session.save(vbAuthorityMgmt);

			VbEmployee vbEmployee = new VbEmployee();
			vbEmployee.setVbOrganization(organization);
			vbEmployee.setUsername(superUserName);
			String[] employeeName = organizationSuperUserCommand.getFullName().split(" ");
			// Setting first, middle and last names of an employee from full name.
			vbEmployee.setFirstName(employeeName[0]);
			vbEmployee.setLastName(" ");
			if (employeeName.length == 2) {
				vbEmployee.setFirstName(employeeName[0]);
				vbEmployee.setLastName(employeeName[1]);
			} else if (employeeName.length > 2) {
				vbEmployee.setFirstName(employeeName[0]);
				vbEmployee.setMiddleName(employeeName[1]);
				vbEmployee.setLastName(employeeName[2]);
			}
			vbEmployee.setEmployeeType(OrganizationUtils.ROLE_SUPER_MANAGEMENT);
			vbEmployee.setCreatedOn(date);
			vbEmployee.setModifiedOn(date);
			String employeeNo = generateEmployeeNo(session, organization, vbEmployee.getEmployeeType());
			vbEmployee.setEmployeeNumber(employeeNo);
			vbEmployee.setEmployeeEmail(organizationSuperUserCommand.getEmail());
			vbEmployee.setEmployeeHierarchyLevel(new Integer(0));
			session.save(vbEmployee);

			VbEmployeeAddress employeeAddress = new VbEmployeeAddress();
			employeeAddress.setAddressLine1(organizationCommand.getAddressLine1());
			employeeAddress.setAddressLine2(organizationCommand.getAddressLine2());
			employeeAddress.setAddressType(OrganizationUtils.ADDRESS_TYPE_PRIMARY);
			employeeAddress.setCity(organizationCommand.getCity());
			employeeAddress.setLandmark(organizationCommand.getLandmark());
			employeeAddress.setLocality(organizationCommand.getLocality());
			employeeAddress.setState(organizationCommand.getState());
			employeeAddress.setZipcode(organizationCommand.getZipcode());
			employeeAddress.setVbEmployee(vbEmployee);
			session.save(employeeAddress);

			VbEmployeeDetail employeeDetail = new VbEmployeeDetail();
			employeeDetail.setAlternateMobile(organizationSuperUserCommand.getAlternateMobile());
			employeeDetail.setBloodGroup("");
			employeeDetail.setMobile(organizationSuperUserCommand.getMobile());
			employeeDetail.setNationality(organizationCommand.getCountry());
			employeeDetail.setPassportNumber("");
			employeeDetail.setVbEmployee(vbEmployee);
			session.save(employeeDetail);
			transaction.commit();
		} catch (HibernateException exception) {
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DataAccessException(Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE));
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible for updating organization details.
	 * 
	 * @param organizationCommand - {@link OrganizationCommand}
	 * @param organizationSuperUserCommand - {@link OrganizationSuperUserCommand}
	 * @param username - {@link String}
	 * @param id - {@link Integer}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void updateOrganization(OrganizationCommand organizationCommand,
			OrganizationSuperUserCommand organizationSuperUserCommand,
			String username, Integer id) throws DataAccessException {
		Session session = this.getSession();
		VbOrganization vbOrganization = (VbOrganization) session.get(VbOrganization.class, id);
		String mainBranchName = null;
		if (vbOrganization != null) {
			mainBranchName = organizationCommand.getMainBranchName();
			Transaction transaction = null;
			try {
				if(mainBranchName != null){
					VbOrganizationMapping organizationMapping = (VbOrganizationMapping) session.createCriteria(VbOrganizationMapping.class)
							.add(Restrictions.eq("vbOrganizationBySubBranchId", vbOrganization))
							.uniqueResult();
					VbOrganization organizationMainBranch = (VbOrganization) session.createCriteria(VbOrganization.class)
							                                       .add(Restrictions.eq("name", mainBranchName))
							                                       .uniqueResult();
					if (organizationMapping != null) {
						organizationMapping.setVbOrganizationByMainBranchId(organizationMainBranch);
						session.update(organizationMapping);
					}
				}
				
				transaction = session.beginTransaction();
				vbOrganization.setAddressLine1(organizationCommand.getAddressLine1());
				vbOrganization.setAddressLine2(organizationCommand.getAddressLine2());
				vbOrganization.setBranchName(organizationCommand.getBranchName());
				
				vbOrganization.setFullName(organizationSuperUserCommand.getFullName());
				vbOrganization.setMainBranch(organizationCommand.getMainBranch());
				vbOrganization.setCity(organizationCommand.getCity());
				vbOrganization.setCountry(organizationCommand.getCountry());
				vbOrganization.setCurrencyFormat(organizationCommand.getCurrencyFormat());
				vbOrganization.setDescription(organizationCommand.getDescription());
				vbOrganization.setLandmark(organizationCommand.getLandmark());
				vbOrganization.setLocality(organizationCommand.getLocality());
				vbOrganization.setName(organizationCommand.getName());
				vbOrganization.setPhone1(organizationCommand.getPhone1());
				vbOrganization.setPhone2(organizationCommand.getPhone2());
				vbOrganization.setMobile(organizationSuperUserCommand.getMobile());
				vbOrganization.setAlternateMobile(organizationSuperUserCommand.getAlternateMobile());
				vbOrganization.setUsernamePrefix(organizationCommand.getUsernamePrefix());
				vbOrganization.setZipcode(organizationCommand.getZipcode());
				vbOrganization.setOrganizationCode(organizationCommand.getOrganizationCode());
				vbOrganization.setState(organizationCommand.getState());
				vbOrganization.setEmail(organizationSuperUserCommand.getEmail());
				//Concatination done at adction class itself.
				
				/*String superUserName = new StringBuffer(organizationCommand.getUsernamePrefix()).append(".")
						.append(organizationSuperUserCommand.getSuperUsername()).toString();*/
				vbOrganization.setSuperUserName(organizationSuperUserCommand.getSuperUsername().toString());
				
				if(_logger.isDebugEnabled()) {
					_logger.debug("Updating VbOrganization");
				}
				session.update(vbOrganization);
				transaction.commit();
			} catch (HibernateException exception) {
				if(transaction != null) {
					transaction.rollback();
				}
				throw new DataAccessException(Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE));
			} finally {
				if(session != null) {
					session.close();
				}
			}
		} else {
			if(session != null) {
				session.close();
			}
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
}
