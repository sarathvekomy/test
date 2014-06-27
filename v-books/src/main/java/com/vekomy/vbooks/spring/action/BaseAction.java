package com.vekomy.vbooks.spring.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vekomy.vbooks.hibernate.IHibernateDao;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.security.User;

/**
 * @author Satish
 *
 * 
 */
public abstract class BaseAction {

	public static final String ACTION_SAVE = "action-save";
	public static final String ACTION_SEARCH = "action-search";
	public static final String ACTION_UPDATE = "action-update";
	public static final String ACTION_CLEAR = "action-clear";
	public static final String ACTION_CANCEL = "action-cancel";
	
	protected HttpServletRequest request;

	private String daoName;

	protected IHibernateDao dao;
	
	
	public BaseAction() {
		// TODO Auto-generated constructor stub
	}

	public abstract IResult process(Object form) throws Exception;

	public void prepareDao(HttpServletRequest request) {
		if (dao == null) {
			ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
			dao = (IHibernateDao) hibernateContext.getBean(daoName);
		}
		this.request = request;
	}
	
	public VbOrganization getOrganization() {
		User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user.getOrganization();
	}

	public String getUsername() {
		User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user.getName();
	}

	public User getUser() {
		return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * @return the dao
	 */
	public IHibernateDao getDao() {
		return dao;
	}

	/**
	 * @return the daoName
	 */
	public String getDaoName() {
		return daoName;
	}

	/**
	 * @param daoName
	 *            the daoName to set
	 */
	public void setDaoName(String daoName) {
		this.daoName = daoName;
	}
}
