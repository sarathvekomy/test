/**
 * com.vekomy.vbooks.app.authenticate.dao.AuthenticateDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 12, 2013
 */
package com.vekomy.vbooks.app.authenticate.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.app.base.BaseDao;
import com.vekomy.vbooks.app.hibernate.model.VbAuthority;
import com.vekomy.vbooks.app.hibernate.model.VbEmployee;
import com.vekomy.vbooks.app.hibernate.model.VbLogin;
import com.vekomy.vbooks.app.hibernate.model.VbLoginTrack;
import com.vekomy.vbooks.app.hibernate.model.VbOrganization;
import com.vekomy.vbooks.app.hibernate.model.VbRole;
import com.vekomy.vbooks.app.request.LoginRequest;
import com.vekomy.vbooks.app.response.LoginResponse;
import com.vekomy.vbooks.app.utils.ApplicationConstants;

/**
 * @author Sudhakar
 *
 */
public class AuthenticateDao extends BaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(AuthenticateDao.class);
	
	/**
	 * This method is responsible to authenticate the user credentials.
	 * 
	 * @param request - {@link LoginRequest}
	 * @return response - {@link LoginResponse}
	 */
	@SuppressWarnings("unchecked")
	public LoginResponse authenticate(LoginRequest request) {
		LoginResponse response = new LoginResponse();
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		String userName = request.getUserName();
		VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("password", request.getPassword()))
				.add(Restrictions.eq("enabled", ApplicationConstants.LOGIN_ENABLED))
				.uniqueResult();
		
		if(login != null) {
			VbRole role = (VbRole) session.get(VbRole.class, ApplicationConstants.SALES_EXECUTIVE_ROLE);
			List<VbAuthority> authorities = session.createCriteria(VbAuthority.class)
					.add(Restrictions.eq("vbLogin", login))
					.add(Restrictions.eq("vbRole", role))
					.list();
			VbOrganization organization = (VbOrganization) session.createCriteria(VbEmployee.class)
					.setProjection(Projections.property("vbOrganization"))
					.add(Restrictions.eq("username", userName))
					.uniqueResult();
			if(!authorities.isEmpty()) {
				response.setStatusCode(new Integer(200));
				response.setMessage("Login and Authorisation Success");
				response.setUserName(userName);
				response.setOrganizationId(organization.getId());
			} else {
				response.setStatusCode(new Integer(300));
				response.setMessage("Authorisation Fail");
			}
			
			VbLoginTrack loginTrack = (VbLoginTrack) session.createCriteria(VbLoginTrack.class)
					.add(Restrictions.eq("username", userName))
					.uniqueResult();
			if(loginTrack != null) {
				loginTrack.setLastLoginTime(new Date());
				
				if(_logger.isDebugEnabled()) {
					_logger.debug("Updating VbLoginTrack: {}", loginTrack);
				}
				session.update(loginTrack);
			}
		} else {
			response.setStatusCode(new Integer(300));
			response.setMessage("Authentication Fail");
		}
		
		txn.commit();
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("LoginResponse: {}", response);
		}
		return response;
	}
}
