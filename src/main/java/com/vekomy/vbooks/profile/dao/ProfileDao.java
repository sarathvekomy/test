/**
 * com.vekomy.vbooks.profile.dao.ProfileDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 15, 2013
 */
package com.vekomy.vbooks.profile.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbUserSetting;
import com.vekomy.vbooks.profile.command.ChangePasswordCommand;
import com.vekomy.vbooks.security.PasswordEncryption;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * @author Sudhakar
 *
 * 
 */
public class ProfileDao extends BaseDao {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ProfileDao.class);

	/**
	 * This method is responsible to change the theme of user.
	 * 
	 * @param theme - {@link String}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isSuccess - {@link Boolean}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public Boolean changeTheme(String theme, String userName,
			VbOrganization organization) throws DataAccessException {
		Boolean isSuccess = Boolean.FALSE;
		Session session = this.getSession();
		VbLogin vbLogin = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		if(vbLogin != null) {
			List<VbUserSetting> vbUserSettingList = session.createCriteria(VbUserSetting.class)
					.add(Restrictions.eq("username", userName))
					.list();
			Transaction transaction = session.beginTransaction();
			if(!vbUserSettingList.isEmpty()) {
				for (VbUserSetting vbUserSetting : vbUserSettingList) {
					vbUserSetting.setTheme(theme);
					
					if(_logger.isDebugEnabled()) {
						_logger.debug("Updating VbUserSetting");
					}
					session.update(vbUserSetting);
				}
				isSuccess = Boolean.TRUE;
			} else {
				VbUserSetting userSetting = new VbUserSetting();
				userSetting.setTheme(theme);
				userSetting.setUsername(userName);
				
				if(_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbUserSetting");
				}
				session.save(userSetting);
				isSuccess = Boolean.TRUE;
			}
			transaction.commit();
			session.close();
			return isSuccess;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to persist favorite theme of user.
	 * 
	 * @param favorite - {@link String}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isSuccess - {@link Boolean}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public Boolean saveFavorite(String favorite, String userName, VbOrganization organization) throws DataAccessException {
		Boolean isSuccess = Boolean.FALSE;
		Session session = this.getSession();
		VbLogin vbLogin = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		if(vbLogin != null) {
			isSuccess = Boolean.TRUE;
			Transaction transaction = session.beginTransaction();
			List<VbUserSetting> vbUserSettingList = session.createCriteria(VbUserSetting.class)
					.add(Restrictions.eq("username", userName))
					.list();
			
			VbUserSetting vbUserSetting = new VbUserSetting();
			for (VbUserSetting vbUserSetting2 : vbUserSettingList) {
				vbUserSetting.setTheme(vbUserSetting2.getTheme());
			}
			vbUserSetting.setFavorite(favorite);
			vbUserSetting.setUsername(userName);
			transaction.commit();
			session.close();
			return isSuccess;
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
	 * This method is responsible to get favorite theme of user.
	 * 
	 * @param userName - {@link String}
	 * @return favoriteList = {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<VbUserSetting> getFavorites(String userName) throws DataAccessException {
		Session session = this.getSession();
		List<VbUserSetting> favoriteList = session.createCriteria(VbUserSetting.class)
				.add(Restrictions.eq("username", userName))
				.list();
		session.close();
		
		if(!favoriteList.isEmpty()) {
			if(_logger.isDebugEnabled()){
				_logger.debug("{} records have been found.", favoriteList.size());
			}
			return favoriteList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to change the password of an existing user.
	 * 
	 * @param command - {@link ChangePasswordCommand}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isUpdated - {@link Boolean}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public Boolean changePassword(ChangePasswordCommand command,
			String userName, VbOrganization organization)
			throws DataAccessException {
		Boolean isUpdated = Boolean.FALSE;
		Session session = this.getSession();
		String oldPassword = command.getOldPassWord();
		String newPassword = command.getNewPassWord();
		VbLogin vbLogin = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("password", PasswordEncryption.encryptPassword(oldPassword)))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		
		if(vbLogin != null) {
			// Updating VbLogin with new password.
			Transaction transaction = session.beginTransaction();
			vbLogin.setPassword(PasswordEncryption.encryptPassword(newPassword));
			transaction.commit();
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating login credentials.");
			}
			isUpdated = Boolean.TRUE;
			session.close();
			return isUpdated;
		} else {
			session.close();
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	public String resetPassword(String newPassword, String userName) {
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		String changedPassword = null;
		Integer updatedEntities = session.createQuery(
				"UPDATE com.vekomy.vbooks.hibernate.model.VbLogin SET password =:password,firstTime =:firstTime WHERE username = :username")
		        .setString("username", userName)
		        .setString("password", PasswordEncryption.encryptPassword(newPassword))
		        .setCharacter("firstTime", OrganizationUtils.FIRST_TIME_LOGIN_YES)
		        .executeUpdate();
		transaction.commit();
		session.close();
		
		if (updatedEntities > 0) {
			changedPassword = userName + " password :" + newPassword;
			
			if(_logger.isDebugEnabled()){
				_logger.debug("Password have been changed for the userName: {}", userName);
			}
		}
		return changedPassword;
	}
}
