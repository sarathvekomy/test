/**
 * com.vekomy.vbooks.security.LoginDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 10, 2013
 */
package com.vekomy.vbooks.security;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbAddressTypes;
import com.vekomy.vbooks.hibernate.model.VbAuthority;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbJournalTypes;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbLoginTrack;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbPaymentTypes;
import com.vekomy.vbooks.hibernate.model.VbRole;
import com.vekomy.vbooks.hibernate.model.VbUserSetting;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This dao class is responsible to perform authentication of the logged in users.
 * 
 * @author Sudhakar
 * 
 */
public class LoginDao extends BaseDao {

	/**
	 * Logger variable holdes _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(LoginDao.class);

	/**
	 * This method is responsible to authenticate user credentials with the DB.
	 * 
	 * @param principal
	 * @param credentials
	 * @return vbLogin
	 * @throws ParseException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public VbLogin authenticate(String principal, String credentials) throws ParseException {
		
		if(_logger.isDebugEnabled()){
			_logger.debug("UserName: {} and Password: {}", principal, credentials);
		}
		
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();
		String lastLoginTime = dateFormat.format(date);

		Query query = session.createQuery("FROM VbLogin vbl WHERE vbl.username = :userName AND vbl.password = :password AND vbl.enabled = :enabled");
		query.setParameter("userName", principal);
		query.setParameter("password", PasswordEncryption.encryptPassword(credentials));
		query.setParameter("enabled", OrganizationUtils.LOGIN_ENABLED);
		Iterator<VbLogin> iterator3 = query.iterate();
		VbLogin vbLogin = null;
		if (query.list().size() != 0) {
			while (iterator3.hasNext()) {
				vbLogin = (VbLogin) iterator3.next();
				Iterator<VbAuthority> iterator = vbLogin.getVbAuthorities().iterator();
				while (iterator.hasNext()) {
					VbAuthority authority = (VbAuthority) iterator.next();
					authority.getVbRole().getRoleName();
				}
			}
			Query query1 = session.createQuery("FROM VbLoginTrack vlt WHERE vlt.username = :username");
			query1.setString("username", principal);
			if (query1.list().size() != 0) {
				Iterator<VbLoginTrack> ite = query1.iterate();
				while (ite.hasNext()) {
					VbLoginTrack vbLoginTrack = (VbLoginTrack) ite.next();
					vbLoginTrack.setUsername(principal);
					vbLoginTrack.setLastLoginTime(new Timestamp(dateFormat.parse(lastLoginTime).getTime()));
					if(_logger.isDebugEnabled()){
						_logger.debug("vbLoginTrack: {}", vbLoginTrack);
					}
					session.saveOrUpdate(vbLoginTrack);
				}
			} else {
				VbLoginTrack vbLoginTrack = new VbLoginTrack();
				vbLoginTrack.setUsername(principal);
				vbLoginTrack.setLastLoginTime(new Timestamp(dateFormat.parse(lastLoginTime).getTime()));
				if(_logger.isDebugEnabled()){
					_logger.debug("vbLoginTrack: {}", vbLoginTrack);
				}
				session.save(vbLoginTrack);
			}
		}
		txn.commit();
		session.close();
		
		if(_logger.isDebugEnabled()){
			_logger.debug("vbLogin: {}", vbLogin);
		}
		return vbLogin;
	}

	/**
	 * This method is responsible to change the user password for the first time login 
	 * or from user profile.
	 * 
	 * @param password
	 * @param username
	 * @return {@link Boolean}
	 * 
	 */
	public boolean changePassword(String password, String username) {
		Session session = this.getSession();
		Boolean isPasswordChanged = Boolean.FALSE;
		VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", username))
				.uniqueResult();
		
		if(login == null){
			isPasswordChanged = Boolean.FALSE;
		}else {
			Transaction txn = session.beginTransaction();
			login.setPassword(PasswordEncryption.encryptPassword(password));
			login.setFirstTime(OrganizationUtils.FIRST_TIME_LOGIN_NO);
			
			if(_logger.isDebugEnabled()){
				_logger.debug("Password have been changed for the username: {}", username);
			}
			session.update(login);
			txn.commit();
			isPasswordChanged = Boolean.TRUE;
		}
		return isPasswordChanged;	
	}

	/**
	 * This method is responsible to get the {@link VbEmployee} based on user name.
	 * 
	 * @param username
	 * @return vbEmployee
	 * 
	 */
	public VbEmployee getEmployee(String username) {
		Session session = this.getSession();
		VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("username", username))
				.uniqueResult();
		
		session.close();
		if(_logger.isDebugEnabled()){
			_logger.debug("vbEmployee : {}", vbEmployee);
		}
		return vbEmployee;
	}

	/**
	 * This method is responsible to get organization based on user name.
	 *  
	 * @param username - {@link String}
	 * @return vbOrganization - {@link VbOrganization}
	 * 
	 */
	public VbOrganization getEmployeeOrganization(String username) {
		Session session = this.getSession();
		VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("username", username))
				.uniqueResult();
		VbOrganization vbOrganization = null;
		if(vbEmployee != null){
			vbOrganization = vbEmployee.getVbOrganization();
			vbOrganization.getBranchName();
		}
		Hibernate.initialize(vbOrganization);
		session.close();
		
		if(_logger.isDebugEnabled()){
			_logger.debug("vbOrganization : {}", vbOrganization);
		}
		return vbOrganization;
	}

	/**
	 * This method is responsible to check either the login user is new user or not.
	 * 
	 * @param userName - {@link String}
	 * @return {@link Boolean}
	 * 
	 */
	public boolean isFirstTimeLogin(String userName) {
		Session session = this.getSession();
		Boolean isFistTime = Boolean.FALSE;
		VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("firstTime", OrganizationUtils.FIRST_TIME_LOGIN_YES))
				.uniqueResult();
		if (login != null) {
			isFistTime = Boolean.TRUE;
		}
		session.close();
		return isFistTime;
	}

	@SuppressWarnings("unchecked")
	public VbUserSetting getUserSettings(String username) {
		VbUserSetting vbUserSetting = null;
		Session session = this.getSession();
		Iterator<VbUserSetting> iterator3 = session.createCriteria(VbUserSetting.class)
				.add(Restrictions.eq("username", username))
				.list().iterator();
		while (iterator3.hasNext()) {
			vbUserSetting = (VbUserSetting) iterator3.next();
			vbUserSetting.getTheme();
		}
		session.close();
		if(_logger.isDebugEnabled()){
			_logger.debug("User setting for the username {} is: {}", username, vbUserSetting);
		}
		return vbUserSetting;
	}

	public boolean comparePasswords(String username, String oldPassword) {
		Session session = this.getSession();
		Boolean isEquals = Boolean.FALSE;
		String password = (String) session.createCriteria(VbLogin.class)
				.setProjection(Projections.property("password"))
				.add(Restrictions.eq("username", username))
				.add(Restrictions.eq("password", PasswordEncryption.encryptPassword(oldPassword)))
				.uniqueResult();
		if (password != null) {
			isEquals = Boolean.TRUE;
		}
		session.close();
		return isEquals;
	}

	public String getOldPassword(String username) {
		Session session = this.getSession();
		VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", username))
				.uniqueResult();
		String password = "";
		if (login != null) {
			password = login.getPassword();
		}
		session.close();
		if(_logger.isDebugEnabled()){
			_logger.debug("Old password for the username: {} is: {}", username, password);
		}
		return password;
	}
	
	public String getCreatedBy(String userName, VbOrganization organization) {
		Session session = this.getSession();
		String createdBy = (String) session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("createdBy"))
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		return createdBy;
	}
	
	/**
	 * This method is responsible to update {@link VbLogin}.
	 * 
	 * @param userName - {@link String}
	 * 
	 */
	public void updateWrongPwdCountForFail(String userName) {
		Session session = this.getSession();
		VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", userName))
				.uniqueResult();
		Transaction txn = session.beginTransaction();
		Integer loginCount;
		if(login != null) {
			loginCount = login.getWrongPasswordCount();
			login.setWrongPasswordCount(++loginCount);
			
			if (loginCount == OrganizationUtils.WRONG_PASSWORD_COUNT_LIMIT) {
				login.setEnabled(OrganizationUtils.LOGIN_DISABLED);
				login.setWrongPasswordCount(new Integer(0));
				VbOrganization organization = (VbOrganization) session.createCriteria(VbEmployee.class)
						.setProjection(Projections.property("vbOrganization"))
						.add(Restrictions.eq("username", userName))
						.uniqueResult();
				//For Alerts
				AlertManager.getInstance().fireSystemAlert(organization, userName, organization.getSuperUserName(), OrganizationUtils.ALERT_TYPE_WRONG_PWD_ENTRY, 
						Msg.get(MsgEnum.ALERT_TYPE_WRONG_PWD_ENTRY_MESSAGE));
			}
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating VbLogin: {}", login);
			}
			session.update(login);
		}
		txn.commit();
		session.close();
	}
	
	/**
	 * This method is responsible to update {@link VbLogin}.
	 * 
	 * @param userName - {@link String}
	 * 
	 */
	public void updateWrongPwdCountForSucces(String userName) {
		Session session = this.getSession();
		VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", userName))
				.uniqueResult();
		Transaction txn = session.beginTransaction();
		if(login != null) {
			login.setWrongPasswordCount(new Integer(0));
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating VbLogin: {}", login);
			}
			session.update(login);
		}
		txn.commit();
		session.close();
	}
	
	/**
	 * This method is responsible to check whether system lookups are configured or not.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isSystemLookupsConfigured - {@link Boolean}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Boolean checkForSystemLookups(String userName, VbOrganization organization) {
		Boolean isSystemLookupsConfigured = Boolean.FALSE;
		Session session = this.getSession();
		List<VbAddressTypes> addressTypes = session.createCriteria(VbAddressTypes.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		List<VbPaymentTypes> paymentTypes = session.createCriteria(VbPaymentTypes.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		List<VbJournalTypes> journalTypes = session.createCriteria(VbJournalTypes.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		List<VbRole> roles = session.createCriteria(VbRole.class).list();
		session.close();
		
		if(addressTypes.isEmpty() || paymentTypes.isEmpty() || journalTypes.isEmpty() || roles.isEmpty()) {
			isSystemLookupsConfigured = Boolean.FALSE;
		} else {
			isSystemLookupsConfigured = Boolean.TRUE;
		}
		return isSystemLookupsConfigured;
	}
	
	/**
	 * This method is responsible to get {@link VbOrganization} instance.
	 * 
	 * @param userName - {@link String} 
	 * @return organization - {@link VbOrganization}
	 * 
	 */
	public VbOrganization getVbOrganization(String userName) {
		Session session = this.getSession();
		VbOrganization organization = (VbOrganization) session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("vbOrganization"))
				.add(Restrictions.eq("username", userName))
				.uniqueResult();
		session.close();
		return organization;
	}
}
