package com.vekomy.vbooks.profile.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbUserSetting;
import com.vekomy.vbooks.security.PasswordEncryption;
import com.vekomy.vbooks.security.User;
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
	
	public ProfileDao() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean changeTheme(String theme, String userName) {
		if(_logger.isDebugEnabled()){
			_logger.debug("Theme :{} for the userName: {}", theme, userName);
		}
		boolean success = false;
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery("FROM com.vekomy.vbooks.hibernate.model.VbLogin WHERE username = :username");
		query.setString("username", userName);

		Iterator iterator3 = query.iterate();

		VbLogin vbLogin = null;
		while (iterator3.hasNext()) {
			vbLogin = (VbLogin) iterator3.next();

			Query userSettingQuery = session.createQuery("FROM com.vekomy.vbooks.hibernate.model.VbUserSetting where username = :username");
			userSettingQuery.setString("username", userName);

			Iterator<VbUserSetting> iterator = userSettingQuery.iterate();
			VbUserSetting vbUserSetting = null;
			if (iterator.hasNext()) {
				// Assumption we have one only setting object per login
				while (iterator.hasNext()) {
					vbUserSetting = (VbUserSetting) iterator.next();
					vbUserSetting.setTheme(theme);
					session.saveOrUpdate(vbUserSetting);
				}
				success = true;
			} else {
				vbUserSetting = new VbUserSetting();
				vbUserSetting.setTheme(theme);
				vbUserSetting.setUsername(userName);
				session.save(vbUserSetting);
				success = true;
			}
			if(_logger.isDebugEnabled()){
				_logger.debug("Updating vbUserSetting: {} for the userName: {}", vbUserSetting, userName);
			}
		}
		
		transaction.commit();
		session.close();

		return success;
	}

	@SuppressWarnings("rawtypes")
	public Object saveFavorite(String favorite, String userName) {
		if(_logger.isDebugEnabled()){
			_logger.debug("Favorite: {} added for the userName: {}", favorite, userName);
		}
		boolean success = false;
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery("FROM com.vekomy.vbooks.hibernate.model.VbLogin WHERE username = :username");
		query.setString("username", userName);

		Iterator iterator3 = query.iterate();

		VbLogin vbLogin = null;
		while (iterator3.hasNext()) {
			vbLogin = (VbLogin) iterator3.next();
			VbUserSetting vbUserSetting = new VbUserSetting();

			Query userSettingQuery = session
			        .createQuery("FROM com.vekomy.vbooks.hibernate.model.VbUserSetting WHERE username = :username");
			userSettingQuery.setString("username", userName);
			Iterator iterator4 = userSettingQuery.iterate();

			if (iterator4.hasNext()) {
				vbUserSetting.setTheme(((VbUserSetting) iterator4.next()).getTheme());
			}

			vbUserSetting.setFavorite(favorite);
			vbUserSetting.setUsername(userName);
			if(_logger.isDebugEnabled()){
				_logger.debug("Persting vbUserSetting: {}", vbUserSetting);
			}
			session.save(vbUserSetting);
			success = true;
		}

		transaction.commit();
		session.close();

		return success;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<VbUserSetting> getFavorites(String username) {
		List<VbUserSetting> favoriteList = null;
		Session session = this.getSession();
		Query query = session.createQuery("FROM com.vekomy.vbooks.hibernate.model.VbUserSetting as a WHERE a.username = :username");
		query.setString("username", username);
		favoriteList = query.list();
		Iterator iterator = favoriteList.iterator();
		while (iterator.hasNext()) {
			VbUserSetting snUserSetting = (VbUserSetting) iterator.next();
			snUserSetting.getFavorite();
		}
		session.close();
		if(_logger.isDebugEnabled()){
			_logger.debug("favoriteList: {}", favoriteList);
		}
		return favoriteList;
	}

	public boolean changePassword(String oldPassword, String newPassword, String userName) {
		
		if(_logger.isDebugEnabled()){
			//_logger.debug("oldPassword: {} and newPassword: {} for the userName: {}", oldPassword, newPassword, userName);
		}
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		int updatedEntities = session
		        .createQuery("UPDATE com.vekomy.vbooks.hibernate.model.VbLogin SET password = :newPassword WHERE username = :username AND password = :oldPassword")
		        .setString("username", userName)
		        .setString("oldPassword", PasswordEncryption.encryptPassword(oldPassword))
		        .setString("newPassword", PasswordEncryption.encryptPassword(newPassword)).executeUpdate();
		if (updatedEntities > 0) {
			if(_logger.isDebugEnabled()){
				_logger.debug("Password have been changed for the userName: {}", userName);
			}
			return true;
		}
		transaction.commit();
		session.close();
		return false;
	}

	public String resetPassword(String newPassword, String userName) {

		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		String changedPassword = null;
		int updatedEntities = session
		        .createQuery("UPDATE com.vekomy.vbooks.hibernate.model.VbLogin SET password =:password,firstTime =:firstTime WHERE username = :username")
		        .setString("username", userName).setString("password", PasswordEncryption.encryptPassword(newPassword))
		        .setCharacter("firstTime", OrganizationUtils.FIRST_TIME_LOGIN_YES).executeUpdate();
		if (updatedEntities > 0) {
			changedPassword = userName + " password :" + newPassword;
			
			if(_logger.isDebugEnabled()){
				_logger.debug("Password have been changed for the userName: {}", userName);
			}
		}
		transaction.commit();
		session.close();
		return changedPassword;
	}

	public String getEmail(User user) {
		Session session = this.getSession();
		String emailId = "";
		Query q = null;
			q = session
			        .createQuery("SELECT employee.employeeEmail FROM com.vekomy.vbooks.hibernate.model.Vbemployee as employee WHERE employee.username = :username");
			q.setParameter("username", user.getName());
		if (q.setMaxResults(1).uniqueResult() != null) {
			emailId = PasswordEncryption.decrypt(q.setMaxResults(1).uniqueResult().toString());
		}
		
		if(_logger.isDebugEnabled()){
			_logger.debug("Mail id for the username: {} is: {}", user.getName(), emailId);
		}
		return emailId;
	}

	public String getEmail(String userName) {
		Session session = this.getSession();
		String emailId = "";
		Query q = null;
		q = session
		        .createQuery("SELECT employee.employeeEmail FROM com.vekomy.vbooks.hibernate.model.Vbemployee as employee WHERE employee.username = :username");
		q.setParameter("username", userName);
		if (q.setMaxResults(1).uniqueResult() != null) {
			emailId = PasswordEncryption.decrypt(q.setMaxResults(1).uniqueResult().toString());
		}
		
		if(_logger.isDebugEnabled()){
			_logger.debug("Mail id for the username: {} is: {}", userName, emailId);
		}
		return emailId;
	}

}
