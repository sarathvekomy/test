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

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;

import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbAddressTypes;
import com.vekomy.vbooks.hibernate.model.VbAuthority;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbInvoiceNoPeriod;
import com.vekomy.vbooks.hibernate.model.VbJournalTypes;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbLoginTrack;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbPaymentTypes;
import com.vekomy.vbooks.hibernate.model.VbRole;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbUserSetting;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * This dao class is responsible to perform authentication of the logged in users.
 * 
 * @author Sudhakar
 * 
 */
public class LoginDao extends BaseDao {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(LoginDao.class);

	/**
	 * This method is responsible to authenticate user credentials with the DB.
	 * 
	 * @param principal - {@link String}
	 * @param credentials - {@link String}
	 * @return vbLogin {@link VbLogin}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public VbLogin authenticate(String principal, String credentials){
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			VbLogin vbLogin=null;
			String message = null;
			Query query = session.createQuery("FROM VbLogin vbl WHERE vbl.username = :userName AND vbl.password = :password");
			query.setParameter("userName", principal);
			query.setParameter("password", PasswordEncryption.encryptPassword(credentials));
			Iterator<VbLogin> iterator3 = query.iterate();
			vbLogin =(VbLogin) query.uniqueResult();
			VbOrganization organization = null;
			if(vbLogin != null){
				if(OrganizationUtils.LOGIN_ENABLED == vbLogin.getEnabled()){
					while (iterator3.hasNext()) {
						vbLogin = (VbLogin) iterator3.next();
						organization = vbLogin.getVbOrganization();
						Iterator<VbAuthority> iterator = vbLogin.getVbAuthorities().iterator();
						while (iterator.hasNext()) {
							VbAuthority authority = (VbAuthority) iterator.next();
							authority.getVbRole().getRoleName();
						}
					}
					// Saving LoginTrack Details.
					VbLoginTrack vbLoginTrack = new VbLoginTrack();
					vbLoginTrack.setUsername(principal);
					vbLoginTrack.setLastLoginTime(DateUtils.formatDateWithTimestamp(new Date()));
					vbLoginTrack.setVbOrganization(organization);
						
					if(_logger.isDebugEnabled()){
						_logger.debug("Saving vbLoginTrack");
					}
					session.save(vbLoginTrack);
				} else{
					message = Msg.get(MsgEnum.DISABLED_USER_ERROR_MSG);
					if (_logger.isErrorEnabled()) {
						_logger.error(message);
					}
					throw new DisabledException(message);
				}
			} else{
				try{
					organization = (VbOrganization) session.createCriteria(VbLogin.class)
							.setProjection(Projections.property("vbOrganization"))
							.add(Restrictions.eq("username", principal))
							.uniqueResult();
					updateWrongPwdCountForFail(principal, organization);
				} catch (DisabledException exception) {
					throw new DisabledException(exception.getMessage());
				} catch (DataAccessException exception) {
					if(_logger.isErrorEnabled()) {
						_logger.error(exception.getMessage());
					}
				}
				message = Msg.get(MsgEnum.LOGIN_INVALID_CREDENTIALS);
				if (_logger.isErrorEnabled()) {
					_logger.error(message);
				}
				throw new BadCredentialsException(message);
			}
			txn.commit();
			return vbLogin;
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(exception.getMessage());
			}
			throw new BadCredentialsException(Msg.get(MsgEnum.LOGIN_INVALID_CREDENTIALS));
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * This method is responsible to change the user password for the first time login 
	 * or from user profile.
	 * 
	 * @param password - {@link String}
	 * @param username - {@link String}
	 * @return isPasswordChanged - {@link Boolean}
	 * 
	 */
	public boolean changePassword(String password, String username, VbOrganization organization) {
		Session session = this.getSession();
		Boolean isPasswordChanged = Boolean.FALSE;
		VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", username))
				.add(Restrictions.eq("vbOrganization", organization))
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
	public VbEmployee getEmployee(String username, VbOrganization organization) {
		Session session = this.getSession();
		VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("username", username))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
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
	public boolean isFirstTimeLogin(String userName, VbOrganization organization) {
		Session session = this.getSession();
		Boolean isFistTime = Boolean.FALSE;
		VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("vbOrganization", organization))
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

	public boolean comparePasswords(String username, String oldPassword, VbOrganization organization) {
		Session session = this.getSession();
		Boolean isEquals = Boolean.FALSE;
		String password = (String) session.createCriteria(VbLogin.class)
				.setProjection(Projections.property("password"))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("username", username))
				.add(Restrictions.eq("password", PasswordEncryption.encryptPassword(oldPassword)))
				.uniqueResult();
		if (password != null) {
			isEquals = Boolean.TRUE;
		}
		session.close();
		return isEquals;
	}

	public String getOldPassword(String username, VbOrganization organization) {
		Session session = this.getSession();
		VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", username))
				.add(Restrictions.eq("vbOrganization", organization))
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
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public void updateWrongPwdCountForFail(String userName, VbOrganization organization) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		VbLogin login = null;
		Boolean isException = Boolean.FALSE;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			login = (VbLogin) session.createCriteria(VbLogin.class)
					.add(Restrictions.eq("username", userName))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			Integer loginCount;
			if (login != null) {
				String message = null;
				loginCount = login.getWrongPasswordCount();
				login.setWrongPasswordCount(++loginCount);
				if (OrganizationUtils.WARNING_PASSWORD_COUNT.equals(loginCount)) {
					// Throwing a message as warning for exceeding maximum limit for wrong password count
					isException = Boolean.TRUE;
					message = Msg.get(MsgEnum.WARNING_PASSWORD_COUNT_MESSAGE);
				} else if (OrganizationUtils.WRONG_PASSWORD_COUNT_LIMIT.equals(loginCount)) {
					login.setEnabled(OrganizationUtils.LOGIN_DISABLED);
					login.setWrongPasswordCount(new Integer(0));
					// For Alerts
					AlertManager.getInstance().fireSystemAlert(organization, userName, organization.getSuperUserName(), 
									OrganizationUtils.ALERT_TYPE_WRONG_PWD_ENTRY, Msg.get(MsgEnum.ALERT_TYPE_WRONG_PWD_ENTRY_MESSAGE));
					// Throwing a message for disabled account as exceeding Max limit for wrong password count
					isException = Boolean.TRUE;
					message = Msg.get(MsgEnum.DISABLED_USER_ERROR_MSG);
				}
				session.update(login);
				txn.commit();
				
				if(isException) {
					throw new DisabledException(message);
				}
			}
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
			
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
	 * This method is responsible to update {@link VbLogin}.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 */
	public void updateWrongPwdCountForSucces(String userName, VbOrganization organization) {
		Session session = this.getSession();
		VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		
		if(login != null) {
			Transaction txn = session.beginTransaction();
			login.setWrongPasswordCount(new Integer(0));
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating VbLogin: {}", login);
			}
			session.update(login);
			txn.commit();
		}
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
		// Address Types
		List<VbAddressTypes> addressTypes = session.createCriteria(VbAddressTypes.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		// Payments Types
		List<VbPaymentTypes> paymentTypes = session.createCriteria(VbPaymentTypes.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		// Journal Types
		List<VbJournalTypes> journalTypes = session.createCriteria(VbJournalTypes.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		// InvoiceNoPeriod
		List<VbInvoiceNoPeriod> invoiceNoPeriods = session.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		List<VbRole> roles = session.createCriteria(VbRole.class).list();
		session.close();
		
		if(addressTypes.isEmpty() || paymentTypes.isEmpty() || journalTypes.isEmpty() || roles.isEmpty() || invoiceNoPeriods.isEmpty()) {
			isSystemLookupsConfigured = Boolean.FALSE;
		} else {
			isSystemLookupsConfigured = Boolean.TRUE;
		}
		return isSystemLookupsConfigured;
	}
	
	/**
	 * This method is responsible to get {@link VbAuthority} based on userName.
	 * 
	 * @param userName - {@link String}
	 * @return isSiteAdmin - {@link Boolean}
	 */
	public Boolean getVbAuthority(Integer roleId, String userName, String password) {
		Boolean isSiteAdmin = Boolean.FALSE;
		Session session = this.getSession();
		VbRole role = (VbRole) session.get(VbRole.class, roleId);
		VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("password", PasswordEncryption.encryptPassword(password)))
				.uniqueResult();
		if(role != null && login != null) {
			VbAuthority vbAuthority = (VbAuthority) session.createCriteria(VbAuthority.class)
					.add(Restrictions.eq("vbRole", role))
					.add(Restrictions.eq("vbLogin", login))
					.uniqueResult();
			if(vbAuthority != null) {
				isSiteAdmin = Boolean.TRUE;
			}
		}
		session.close();
		
		return isSiteAdmin;
	}
	
	/**
	 * This method is responsible to get allotment based on userName and organization.
	 * 
	 * @param user - {@link String}
	 * @return isAlloted - {@link Boolean}
	 */
	public Boolean isAlloted(String user,VbOrganization organization){
		Boolean isAlloted = Boolean.FALSE;
		Session session = this.getSession();
		VbSalesBook vbSalesBook = (VbSalesBook) session.createCriteria(VbSalesBook.class)
				.add(Restrictions.eq("salesExecutive", user))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("flag", new Integer(1))).uniqueResult();
		session.close();
		if(vbSalesBook != null){
			isAlloted = Boolean.TRUE;
		}
		return isAlloted;
	}
}
