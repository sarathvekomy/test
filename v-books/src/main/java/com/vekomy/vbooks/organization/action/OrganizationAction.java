package com.vekomy.vbooks.organization.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.organization.command.OrganizationCommand;
import com.vekomy.vbooks.organization.command.OrganizationSuperUserCommand;
import com.vekomy.vbooks.organization.dao.OrganizationDao;
import com.vekomy.vbooks.siteadmin.command.ManageUserResult;
import com.vekomy.vbooks.siteadmin.command.OrganizationResult;
import com.vekomy.vbooks.siteadmin.dao.SiteAdminDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

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
	 * String variable holds LOAD_ORGANIZATION.
	 */
	public static final String Empty = "";
	/**
	 * String variable holds SAVE_ORGANIZATION.
	 */
	public static final String SAVE_ORGANIZATION = "save-organization";
	/**
	 * String variable holds SAVE_ORGANIZATION_SUPER_USER
	 */
	public static final String SAVE_ORGANIZATION_SUPER_USER = "save-super-organization";
	/**
	 * String variable holds SAVE_ORGANIZATION_EDIT_SUPER_USER
	 */
	public static final String SAVE_ORGANIZATION_EDIT_SUPER_USER = "save-edit-super-organization";
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
	 * String variable holds GET_ORGANIZATION_NAMES.
	 */
	public static final String GET_ALL_ORGANIZATION_NAMES = "get-all-organization-names";
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
	 * String variable holds GET_ALL_EMPLOYEES.
	 */
	public static final String GET_ALL_EMPLOYEES = "get-all-employees";
	public static final String CHECK_USER_NAME_PREFIX_AVAILABILITY = "check-username-prefix-availability";


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	public IResult process(Object form)  {
		try{
			ResultSuccess resultSuccess = new ResultSuccess();
			OrganizationCommand organizationCommand = null;
			OrganizationSuperUserCommand organizationSuperUserCommand = null;
			String resultStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
			OrganizationDao organizationDao = (OrganizationDao) getDao();
			if (form instanceof OrganizationCommand) {
				organizationCommand = (OrganizationCommand) form;
				String action = organizationCommand.getAction();
				if (SAVE_ORGANIZATION.equals(action)) {
					session = request.getSession(Boolean.TRUE);
					session.setAttribute("save-basic", organizationCommand);
					
					resultSuccess.setStatus(resultStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if (LOAD_ORGANIZATION.equals(action)) {
					VbOrganization vbOrganization = organizationDao.getOrganization(organizationCommand.getId());
					getUser().setOrganization(vbOrganization);
					
					resultSuccess.setData(vbOrganization);
					resultSuccess.setStatus(resultStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if (GET_ORGANIZATION_CODE.equals(action)) {
					resultSuccess.setData(organizationDao.getOrganizationCode());
					resultSuccess.setStatus(resultStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if (UPDATE_ORGANIZATION.equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					getSiteAdminDao().updateOrganization((OrganizationCommand)session.getAttribute("save-basic"),
							(OrganizationSuperUserCommand)session.getAttribute("save-detail"), getUsername(), id);
					
					// Removing data from Session.
			        removeSessionData(session);
			        
					resultSuccess.setStatus(resultStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if (MODIFY_ORGANIZATION_STATUS.equals(action)) {
					String organizationStatus = request.getParameter("organizationStatus");
					String mainBranch = request.getParameter("mainBranch");
					organizationDao.modifyOrganizationStatus(mainBranch,organizationCommand, organizationStatus);
					
					resultSuccess.setStatus(resultStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if (VALIDATE_ORGANIZATION_CODE.equals(action)) {
					String result = organizationDao.validateOrganizationCode(organizationCommand);
					
					resultSuccess.setData(result);
					resultSuccess.setStatus(resultStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if (GET_ORGANIZATION_NAMES.equals(action)) {
					String mainBranchName = request.getParameter("mainBranchName");
					List<String> organizations = organizationDao.getMainBranches(mainBranchName, organizationCommand);
					
					resultSuccess.setData(organizations);
					resultSuccess.setStatus(resultStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if (GET_ALL_ORGANIZATION_NAMES.equals(action)) {
					String mainBranchName =Empty;
					List<String> organizations = organizationDao.getMainBranches(mainBranchName, organizationCommand);
					
					resultSuccess.setData(organizations);
					resultSuccess.setStatus(resultStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("save-organization-details".equals(action)){
			        getSiteAdminDao().saveOrganization((OrganizationCommand)session.getAttribute("save-basic"),
			        		(OrganizationSuperUserCommand)session.getAttribute("save-detail"), getUsername());
			        
			        // Removing data from Session.
			        removeSessionData(session);
			        
			        resultSuccess.setStatus(resultStatus);
			        resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if (SEARCH_ORGANIZATION.equals(action)) {
					HashMap<String, SearchFilterData> filter = new HashMap<String, SearchFilterData>();
					filter.put("username", new SearchFilterData("country", organizationCommand.getCountry(), SearchFilterData.TYPE_STRING_STR));
					filter.put("firstName", new SearchFilterData("mainBranch", organizationCommand.getMainBranch(), SearchFilterData.TYPE_STRING_STR));
					List<VbOrganization> list = organizationDao.searchOrganization(organizationCommand);
					
					resultSuccess.setData(list);
					resultSuccess.setStatus(resultStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("get-disabled-organization-list".equals(action)) {
					List<ManageUserResult> list = organizationDao.getDisabledOrganizationList();
				
					resultSuccess.setData(list);
					resultSuccess.setStatus(resultStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if ("validate-organization-super-user-name".equals(action)) {
					String userName=request.getParameter("superUser");
					String result = organizationDao.validateOrgSuperUserName(userName, getOrganization());
					
					resultSuccess.setData(result);
					resultSuccess.setStatus(resultStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				}else if(CHECK_USER_NAME_PREFIX_AVAILABILITY.equals(action)){
					String userPrefix = request.getParameter("usernamePrefix");
					Boolean isAvailable = organizationDao.checkUeserPrefixAvailabilty(userPrefix,getOrganization());
					resultSuccess.setData(isAvailable);
				}else if("get-organization-data".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					OrganizationResult organizationResult = organizationDao.getOrganizationData(id);
					if(organizationResult != null){
						resultSuccess.setData(organizationResult);
					}
				}
			} else if(form instanceof OrganizationSuperUserCommand){
				organizationSuperUserCommand =(OrganizationSuperUserCommand) form;
				if(SAVE_ORGANIZATION_SUPER_USER.equals(organizationSuperUserCommand.getAction())){
					session = request.getSession(Boolean.TRUE);
					OrganizationCommand orgCommand=(OrganizationCommand) session.getAttribute("save-basic");
					String superUserName = new StringBuffer(orgCommand.getUsernamePrefix()).append(".")
							.append(organizationSuperUserCommand.getSuperUsername()).toString();
					organizationSuperUserCommand.setSuperUsername(superUserName);
					session.setAttribute("save-detail",organizationSuperUserCommand);	
					resultSuccess.setStatus(resultStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				}
				if(SAVE_ORGANIZATION_EDIT_SUPER_USER.equals(organizationSuperUserCommand.getAction())){
					session = request.getSession(Boolean.TRUE);
					session.setAttribute("save-detail",organizationSuperUserCommand);	
					resultSuccess.setStatus(resultStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				}
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());

			if (_logger.isErrorEnabled()) {
				_logger.error("resultError: {}", resultError);
			}
			return resultError;
		}
		catch (DisabledException disabledException) {
			ResultError resultError = getResultError(disabledException.getMessage());
			resultError.setStatus("EnableWarning");
			if (_logger.isErrorEnabled()) {
				_logger.error("resultError: {}", resultError);
			}
			return resultError;
		}
	}
	
	/**
	 * this method is responsible to remove data from session.
	 *  
	 * @param session - {@link HttpSession}
	 */
	private void removeSessionData(HttpSession session) {
		if(session != null) {
			session.removeAttribute("save-basic");
			session.removeAttribute("save-detail");
		}
	}
	
	/**
	 * This method is responsible to return {@link SiteAdminDao} instance from {@link ApplicationContext}.
	 * 
	 * @return siteadminDao - {@link SiteAdminDao}
	 */
	private SiteAdminDao getSiteAdminDao() {
		ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		SiteAdminDao siteadminDao = (SiteAdminDao) hibernateContext.getBean("siteadminDao");
		
		return siteadminDao;
	}
}
