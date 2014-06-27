package com.vekomy.vbooks.siteadmin.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.organization.command.OrganizationCommand;
import com.vekomy.vbooks.organization.command.ValidateUserCommand;
import com.vekomy.vbooks.organization.dao.OrganizationDao;
import com.vekomy.vbooks.siteadmin.dao.SiteAdminDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;

/**
 * @author Satish
 *
 * 
 */
public class SiteAdminAction extends BaseAction {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SiteAdminAction.class);
	/**
	 * String variable holds LOAD_ORGANIZATION.
	 */
	public static final String LOAD_ORGANIZATION = "load-organization";
	/**
	 * String variable holds SAVE_ORGANIZATION.
	 */
	public static final String SAVE_ORGANIZATION = "save-organization";
	/**
	 * String variable holds SAVE_GRADE.
	 */
	public static final String SAVE_GRADE = "save-grade";
	/**
	 * String variable holds UPDATE_ORGANIZATION.
	 */
	public static final String UPDATE_ORGANIZATION = "update-organization";
	/**
	 * String variable holds SAVE_CLASSES.
	 */
	public static final String SAVE_CLASSES = "save-classes";
	/**
	 * String variable holds LOAD_CLASSES.
	 */
	public static final String LOAD_CLASSES = "load-classes";
	/**
	 * String variable holds GET_ORGANIZATION_CODE.
	 */
	public static final String GET_ORGANIZATION_CODE = "get-organization-code";
	/**
	 * String variable holds LOAD_ORGANIZATION_GRADES.
	 */
	public static final String LOAD_ORGANIZATION_GRADES="load-organization-grades";
	/**
	 * String variable holds VALIDATE_USER.
	 */
	public static final String VALIDATE_USER="validate-username";

	/**
	 * 
	 */
	public SiteAdminAction() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tsb.nirvahak.spring.action.BaseAction#process(java.lang.Object)
	 */
	
	public IResult process(Object form) throws Exception {
		ResultSuccess resultSuccess = new ResultSuccess();
		OrganizationCommand organizationCommand = null;
		ValidateUserCommand validateUserCommand=null;
		if (form instanceof OrganizationCommand) {
			organizationCommand = (OrganizationCommand) form;
			if (SAVE_ORGANIZATION.equals(organizationCommand.getAction())) {
				ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request
				        .getSession().getServletContext());
				SiteAdminDao siteadminDao = (SiteAdminDao) hibernateContext.getBean("siteadminDao");
				//resultSuccess.setData(siteadminDao.saveOrganization(organizationCommand, getUsername()));
				resultSuccess.setMessage("Organization changed Succesful");
			} else if (LOAD_ORGANIZATION.equals(organizationCommand.getAction())) {
				OrganizationDao organizationDao = (OrganizationDao) getDao();
				VbOrganization snOrganization = organizationDao.getOrganization(organizationCommand.getId());
				getUser().setOrganization(snOrganization);
				resultSuccess.setData(snOrganization);
				resultSuccess.setMessage("Organization changed Succesful");
			} else if (GET_ORGANIZATION_CODE.equals(organizationCommand.getAction())) {
				OrganizationDao organizationDao = (OrganizationDao) getDao();
				resultSuccess.setData(organizationDao.getOrganizationCode());
				resultSuccess.setMessage("Organization changed Succesful");
			}
		} 
		  else {
		    validateUserCommand =(ValidateUserCommand) form;
		    if(VALIDATE_USER.equals(validateUserCommand.getAction())) {
		    ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request
                    .getSession().getServletContext());
            OrganizationDao organizationDao = (OrganizationDao) hibernateContext.getBean("organizationDao");
            resultSuccess.setData((String)organizationDao.validateUsername(validateUserCommand));
            resultSuccess.setMessage("Username Validated Successfully.");
		    }
        }
		if(_logger.isDebugEnabled()){
			_logger.debug("resultSuccess: {}", resultSuccess);
		}
		return resultSuccess;
	}


}
