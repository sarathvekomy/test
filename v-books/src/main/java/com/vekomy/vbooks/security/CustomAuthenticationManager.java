package com.vekomy.vbooks.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.hibernate.model.VbAuthority;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbUserSetting;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * @author Satish
 * 
 */
public class CustomAuthenticationManager implements AuthenticationManager {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(CustomAuthenticationManager.class);
	/**
	 * LoginDao variable holds loginDao.
	 */
	private LoginDao loginDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.AuthenticationManager#
	 * authenticate(org.springframework.security.core.Authentication)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String userName = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();
		VbLogin login = null;
		ArrayList<GrantedAuthority> grantedAuthorities = null;
		VbOrganization organization = null;
		try {
			login = loginDao.authenticate(userName, password);
		} catch (DisabledException exception) {
			throw new DisabledException(exception.getMessage());
		} catch (BadCredentialsException exception) {
			throw new BadCredentialsException(exception.getMessage());
		} 
		
		Authentication auth = null;
		if (login != null) {
			organization = login.getVbOrganization();
			Set<VbAuthority> roles = login.getVbAuthorities();
			grantedAuthorities = new ArrayList<GrantedAuthority>();
			for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
				VbAuthority vbAuthority = (VbAuthority) iterator.next();
				GrantedAuthority authority = new GrantedAuthorityImpl(vbAuthority.getVbRole().getRoleName());
				grantedAuthorities.add(authority);
			}
			User user = new User();
			user.setName(login.getUsername());
			user.setOrganization(organization);
			VbEmployee employee = loginDao.getEmployee(userName, organization);
			if(employee != null) {
				user.setVbEmployee(employee);
			}
			VbUserSetting setting = loginDao.getUserSettings(user.getName());
			if (setting != null) {
				user.setTheme(setting.getTheme());
			}
			auth = new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), grantedAuthorities);
			Boolean isSiteAdmin = loginDao.getVbAuthority(OrganizationUtils.ROLE_SITEADMIN, userName, password);
			if(!isSiteAdmin) {
				Boolean isSystemLookupsConfigured = loginDao.checkForSystemLookups(userName, organization);
				Boolean isSuperMgtUser = loginDao.getVbAuthority(OrganizationUtils.SUPER_MANAGEMENT, userName, password);
				if(!isSystemLookupsConfigured && isSuperMgtUser) {
					AlertManager.getInstance().fireSystemAlert(organization, userName, userName, 
							OrganizationUtils.ALERT_TYPE_SYSTEM_DEFAULTS, Msg.get(MsgEnum.ALERT_TYPE_SYSTEM_DEFAULTS_MESSAGE));
				}
			}
			// Updating VbLogin, if login is success.
			loginDao.updateWrongPwdCountForSucces(userName, organization);
		} 
		
		if(_logger.isDebugEnabled()){
			_logger.debug("authentication : {}", auth);
		}
		return auth;
	}

	/**
	 * @return the loginDao
	 */
	public LoginDao getLoginDao() {
		return loginDao;
	}

	/**
	 * @param loginDao
	 *            the loginDao to set
	 */
	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}

}
