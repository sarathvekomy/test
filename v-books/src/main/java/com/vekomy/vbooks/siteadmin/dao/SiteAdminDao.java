package com.vekomy.vbooks.siteadmin.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

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
	 * @param organizationSuperUserCommand 
	 * @param username - {@link String}
	 * @return isSaved - {@link Boolean}
	 * 
	 */
	public Boolean saveOrganization(OrganizationCommand organizationCommand, OrganizationSuperUserCommand organizationSuperUserCommand, String username) {
			Session session = this.getSession();
			Transaction transaction = session.beginTransaction();
			boolean isSaved = false;
			Date date = new Date();
			String mobile = organizationSuperUserCommand.getMobile();
			String alternateMobile = organizationSuperUserCommand.getAlternateMobile();
			String directLine = organizationCommand.getPhone1();
			String nationality = organizationCommand.getCountry();
			String address1 = organizationCommand.getAddressLine1();
			String address2 = organizationCommand.getAddressLine2();
			String locality = organizationCommand.getLocality();
			String landmark = organizationCommand.getLandmark();
			String city = organizationCommand.getCity();
			String state = organizationCommand.getState();
			String zipcode = organizationCommand.getZipcode();
			String fullName = organizationSuperUserCommand.getFullName();
			VbOrganization organization = new VbOrganization();
			if (organization != null) {
				organization.setAddressLine1(address1);
				organization.setAddressLine2(address2);
				organization.setBranchName(organizationCommand.getBranchName());
				organization.setFullName(fullName);
				organization.setMainBranch(organizationCommand.getMainBranch());
				organization.setCity(city);
				organization.setCountry(nationality);
				organization.setCurrencyFormat(organizationCommand.getCurrencyFormat());
				organization.setDescription(organizationCommand.getDescription());
				organization.setLandmark(landmark);
				organization.setLocality(locality);
				organization.setName(organizationCommand.getName());
				organization.setPhone1(directLine);
				organization.setPhone2(alternateMobile);
				organization.setPhone3(mobile);
				organization.setZipcode(zipcode);
				organization.setOrganizationCode(organizationCommand.getOrganizationCode());
				organization.setState(state);
				organization.setEmail(organizationSuperUserCommand.getEmail());
				organization.setCreatedBy(username);
				organization.setCreatedOn(date);
				organization.setModifiedOn(date);
				organization.setSuperUserName(organizationSuperUserCommand.getSuperUsername());
				
				if(_logger.isDebugEnabled()){
					_logger.debug("Persisting organization: {}", organization);
				}
				session.save(organization);
				
				VbOrganization vbOrganization = (VbOrganization) session.createCriteria(VbOrganization.class)
							.add(Restrictions.eq("name", organizationCommand.getMainBranchName()))
							.uniqueResult();
				VbOrganizationMapping organizationMapping = new VbOrganizationMapping();
				organizationMapping.setVbOrganizationByMainBranchId(vbOrganization);
				organizationMapping.setVbOrganizationBySubBranchId(organization);
					
				if(_logger.isDebugEnabled()){
					_logger.debug("Persisting organizationMapping: {}", organizationMapping);
				}
				session.save(organizationMapping);
				isSaved = Boolean.TRUE;
			}
			
			// We enforce to create Super Management User
			// Organizations can login as this user and modify rest of the data
			VbLogin vbLogin = new VbLogin();
			vbLogin.setUsername(organizationSuperUserCommand.getSuperUsername());
			vbLogin.setPassword(PasswordEncryption.encryptPassword(organizationSuperUserCommand.getPassword()));
			vbLogin.setEnabled(OrganizationUtils.LOGIN_ENABLED);
			vbLogin.setFirstTime(OrganizationUtils.FIRST_TIME_LOGIN_YES);
			vbLogin.setWrongPasswordCount(new Integer(0));
			session.save(vbLogin);
			
			VbRole userRole = (VbRole) session.get("com.vekomy.vbooks.hibernate.model.VbRole", OrganizationUtils.ROLE_USER);
			VbAuthority vbAuthorityUser = new VbAuthority();
			vbAuthorityUser.setVbLogin(vbLogin);
			vbAuthorityUser.setVbRole(userRole);
			session.save(vbAuthorityUser);
			
			VbRole mgmtRole = (VbRole) session
			        .get("com.vekomy.vbooks.hibernate.model.VbRole", OrganizationUtils.ROLE_MANAGEMENT);
			VbAuthority vbAuthorityMgmt = new VbAuthority();
			vbAuthorityMgmt.setVbLogin(vbLogin);
			vbAuthorityMgmt.setVbRole(mgmtRole);
			session.save(vbAuthorityMgmt);
			
			VbEmployee  vbEmployee = new VbEmployee();
			vbEmployee.setVbOrganization(organization);
			vbEmployee.setUsername(organizationSuperUserCommand.getSuperUsername());
			String[] employeeName = fullName.split(" ");
			// Setting first, middle and last names of an employee from full name.
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
			
			vbEmployee.setEmployeeType("MGT");
			vbEmployee.setCreatedOn(date);
			vbEmployee.setModifiedOn(date);
			String employeeNo = generateEmployeeNo(session, organization, vbEmployee.getEmployeeType());
			vbEmployee.setEmployeeNumber(employeeNo);
			vbEmployee.setEmployeeEmail(organizationSuperUserCommand.getEmail());
			vbEmployee.setGender('M');
			session.save(vbEmployee);
			
			VbEmployeeAddress employeeAddress = new VbEmployeeAddress();
			employeeAddress.setAddressLine1(address1);
			employeeAddress.setAddressLine2(address2);
			employeeAddress.setAddressType("primary");
			employeeAddress.setCity(city);
			employeeAddress.setLandmark(landmark);
			employeeAddress.setLocality(locality);
			employeeAddress.setState(state);
			employeeAddress.setZipcode(zipcode);
			employeeAddress.setVbEmployee(vbEmployee);
			session.save(employeeAddress);
			
			VbEmployeeDetail employeeDetail = new VbEmployeeDetail();
			employeeDetail.setAlternateMobile(alternateMobile);
			employeeDetail.setBloodGroup("");
			employeeDetail.setMobile(mobile);
			employeeDetail.setNationality(nationality);
			employeeDetail.setPassportNumber("");
			employeeDetail.setVbEmployee(vbEmployee);
			session.save(employeeDetail);
			transaction.commit();
			session.close();
			
			return isSaved;
			}
	/**
	 * This method is responsible for updating organization details.
	 * 
	 * @param organizationCommand - {@link OrganizationCommand}
	 * @param organizationSuperUserCommand 
	 * @param username - {@link String}
	 * @param id 
	 * @throws DataAccessException - {@link DataAccessException}
	 * @throws java.sql.SQLException - {@link SQLException}
	 * @throws ParseException - {@link ParseException}
	 */
	public void updateOrganization(OrganizationCommand organizationCommand, OrganizationSuperUserCommand organizationSuperUserCommand, String username, int id) throws DataAccessException,
    java.sql.SQLException, ParseException {

			Session session = this.getSession();
			Transaction transaction = session.beginTransaction();
			VbOrganization vbOrganization = (VbOrganization) session
					.createCriteria(VbOrganization.class)
					.add(Expression.eq("id", id))
					.uniqueResult();
			if (vbOrganization != null) {
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
				vbOrganization.setPhone3(organizationSuperUserCommand.getAlternateMobile());
				vbOrganization.setZipcode(organizationCommand.getZipcode());
				vbOrganization.setOrganizationCode(organizationCommand.getOrganizationCode());
				vbOrganization.setState(organizationCommand.getState());
				vbOrganization.setEmail(organizationSuperUserCommand.getEmail());
				vbOrganization.setSuperUserName(organizationSuperUserCommand.getSuperUsername());
				session.update(vbOrganization);
				
			}
			transaction.commit();
			session.close();
	}
}
