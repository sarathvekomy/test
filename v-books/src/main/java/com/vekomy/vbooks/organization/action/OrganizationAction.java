package com.vekomy.vbooks.organization.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.organization.command.OrganizationCommand;
import com.vekomy.vbooks.organization.command.OrganizationSuperUserCommand;
import com.vekomy.vbooks.organization.command.ValidateUserCommand;
import com.vekomy.vbooks.organization.dao.OrganizationDao;
import com.vekomy.vbooks.siteadmin.command.ManageUserResult;
import com.vekomy.vbooks.siteadmin.dao.SiteAdminDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;

/**
 * @author Sudhakar
 * 
 * 
 */
public class OrganizationAction extends BaseAction {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(OrganizationAction.class);
	/**
	 * HttpSession variable holds session
	 */
	private HttpSession session;
	/**
	 * String variable holds LOAD_ORGANIZATION.
	 */
	public static final String LOAD_ORGANIZATION = "load-organization";
	/**
	 * String variable holds SAVE_ORGANIZATION.
	 */
	public static final String SAVE_ORGANIZATION = "save-organization";
	/**
	 * String variable holds SAVE_ORGANIZATION_SUPER_USER
	 */
	public static final String SAVE_ORGANIZATION_SUPER_USER = "save-super-organization";
	/**
	 * String variable holds UPDATE_ORGANIZATION.
	 */
	public static final String UPDATE_ORGANIZATION = "update-organization";
	/**
	 * String variable holds DISABLE_ORGANIZATION.
	 */
	public static final String MODIFY_ORGANIZATION_STATUS = "modify-organization-status";
	/**
	 * String variable holds SAVE_DEPARTMENT.
	 */
	public static final String SAVE_DEPARTMENT = "save-department";
	/**
	 * String variable holds LOAD_DEPARTMENT.
	 */
	public static final String LOAD_DEPARTMENT = "load-department";
	/**
	 * String variable holds UPDATE_DEPARTMENT.
	 */
	public static final String UPDATE_DEPARTMENT = "update-department";
	/**
	 * String variable holds GET_ORGANIZATION_CODE.
	 */
	public static final String GET_ORGANIZATION_CODE = "get-organization-code";
	/**
	 * String variable holds GET_ORGANIZATION_NAMES.
	 */
	public static final String GET_ORGANIZATION_NAMES = "get-organization-names";
	/**
	 * String variable holds SEARCH_ORGANIZATION.
	 */
	public static final String SEARCH_ORGANIZATION = "search-organization";
	/**
	 * String variable holds VALIDATE_ORGANIZATION_CODE.
	 */
	public static final String VALIDATE_ORGANIZATION_CODE = "validate-organization-code";
	/**
	 * String variable holds LOAD_ORGANIZATION_GRADES.
	 */
	public static final String LOAD_ORGANIZATION_GRADES = "load-organization-grades";
	/**
	 * String variable holds VALIDATE_USER.
	 */
	public static final String VALIDATE_USER = "validate-username";

	/**
	 * 
	 */
	public OrganizationAction() {
	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tsb.nirvahak.spring.action.BaseAction#process(java.lang.Object)
	 */

	@SuppressWarnings("unused")
	public IResult process(Object form) throws Exception {
		ResultSuccess resultSuccess = new ResultSuccess();
		OrganizationCommand organizationCommand = null;
		OrganizationSuperUserCommand organizationSuperUserCommand = null;
		ValidateUserCommand validateUserCommand = null;
		if (form instanceof OrganizationCommand) {
			organizationCommand = (OrganizationCommand) form;
			if (SAVE_ORGANIZATION.equals(organizationCommand.getAction())) {
				session = request.getSession(Boolean.TRUE);
				session.setAttribute("save-basic", organizationCommand);
				resultSuccess.setMessage("Organization changed Succesful");
			} else if (LOAD_ORGANIZATION.equals(organizationCommand.getAction())) {
				OrganizationDao organizationDao = (OrganizationDao) getDao();
				VbOrganization vbOrganization = organizationDao.getOrganization(organizationCommand.getId());
				getUser().setOrganization(vbOrganization);
				resultSuccess.setData(vbOrganization);
				resultSuccess.setMessage("Organization changed Succesful");
			} else if (GET_ORGANIZATION_CODE.equals(organizationCommand.getAction())) {
				OrganizationDao organizationDao = (OrganizationDao) getDao();
				resultSuccess.setData(organizationDao.getOrganizationCode());
				resultSuccess.setMessage("Organization changed Succesful");
			} else if (UPDATE_ORGANIZATION.equals(organizationCommand.getAction())) {
				int id = Integer.parseInt(request.getParameter("id"));
				ApplicationContext hibernateContext = WebApplicationContextUtils
						.getWebApplicationContext(request.getSession().getServletContext());
				SiteAdminDao siteadminDao = (SiteAdminDao) hibernateContext.getBean("siteadminDao");
				siteadminDao.updateOrganization((OrganizationCommand)session.getAttribute("save-basic"),(OrganizationSuperUserCommand)session.getAttribute("save-detail"), getUsername(),id);
				resultSuccess.setMessage("Updated Successfully");
			} else if (MODIFY_ORGANIZATION_STATUS.equals(organizationCommand.getAction())) {
				OrganizationDao organizationDao = (OrganizationDao) getDao();
				String organizationStatus=request.getParameter("organizationStatus");
				organizationDao.modifyOrganizationStatus(organizationCommand,organizationStatus);
				resultSuccess.setMessage("Organization Disabled Successfully");
			}
			else if (VALIDATE_ORGANIZATION_CODE.equals(organizationCommand.getAction())) {
				OrganizationDao organizationDao = (OrganizationDao) getDao();
				resultSuccess.setData((String) organizationDao.validateOrganizationCode(organizationCommand));
				resultSuccess.setMessage("validateSuccessfully");
			} else if (GET_ORGANIZATION_NAMES.equals(organizationCommand.getAction())) {
				OrganizationDao organizationDao = (OrganizationDao) getDao();
				String mainBranchName = request.getParameter("mainBranchName");
				List<String> organizations = organizationDao.getMainBranches(mainBranchName,organizationCommand);
				if(organizations == null) {
					resultSuccess.setMessage("Main branch not available");
				}else {
					resultSuccess.setData(organizations);
					resultSuccess.setMessage("get organization names successfully");
				}
			}
			else if("save-organization-details".equals(organizationCommand.getAction())){
				ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession().getServletContext());
		          SiteAdminDao siteadminDao = (SiteAdminDao) hibernateContext.getBean("siteadminDao");
		          resultSuccess.setData(siteadminDao.saveOrganization((OrganizationCommand)session.getAttribute("save-basic"),(OrganizationSuperUserCommand)session.getAttribute("save-detail"), getUsername()));
		          resultSuccess.setMessage("Saved successfully");
			}
			else if (SEARCH_ORGANIZATION.equals(organizationCommand.getAction())) {
				OrganizationDao organizationDao = (OrganizationDao) getDao();
				HashMap<String, SearchFilterData> filter = new HashMap<String, SearchFilterData>();
				filter.put("username", new SearchFilterData("country", organizationCommand.getCountry(), SearchFilterData.TYPE_STRING_STR));
				filter.put("firstName", new SearchFilterData("mainBranch", organizationCommand.getMainBranch(), SearchFilterData.TYPE_STRING_STR));
				List<VbOrganization> list = organizationDao.searchOrganization(organizationCommand);
			if (list.isEmpty()) {
				resultSuccess.setMessage("No Records Found");
			} else {
				resultSuccess.setData(list);
				resultSuccess.setMessage("Search Successfully");
			}
			}
			else if ("get-disabled-organization-list".equals(organizationCommand.getAction())) {
				OrganizationDao organizationDao = (OrganizationDao) getDao();
				List<ManageUserResult> list = organizationDao.getDisabledOrganizationList();
			if (list.isEmpty()) {
				resultSuccess.setMessage("No Disabled Organization Found");
			} else {
				resultSuccess.setData(list);
				resultSuccess.setMessage("Disabled Oraganization Search Successfully");
			}
			}
		}else if(form instanceof OrganizationSuperUserCommand){
			organizationSuperUserCommand =(OrganizationSuperUserCommand) form;
			if(SAVE_ORGANIZATION_SUPER_USER.equals(organizationSuperUserCommand.getAction())){
				session = request.getSession(Boolean.TRUE);
			session.setAttribute("save-detail",organizationSuperUserCommand);	
			}
		}
		else {
			validateUserCommand = (ValidateUserCommand) form;
			if (VALIDATE_USER.equals(validateUserCommand.getAction())) {
				ApplicationContext hibernateContext = WebApplicationContextUtils
						.getWebApplicationContext(request.getSession().getServletContext());
				OrganizationDao organizationDao = (OrganizationDao)getDao();
				resultSuccess.setData((String) organizationDao.validateUsername(validateUserCommand));
				resultSuccess.setMessage("Username Validated Successfully.");
				}
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("resultSuccess: {}", resultSuccess);
		}
		return resultSuccess;
	}
}
