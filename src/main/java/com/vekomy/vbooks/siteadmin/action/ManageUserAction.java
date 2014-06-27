/**
 * com.vekomy.vbooks.siteadmin.action.ManageUserAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jun 26, 2013
 */
package com.vekomy.vbooks.siteadmin.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.employee.command.EmployeeResult;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.siteadmin.command.ManageUserAddressCommand;
import com.vekomy.vbooks.siteadmin.command.ManageUserBasicCommand;
import com.vekomy.vbooks.siteadmin.command.ManageUserCommand;
import com.vekomy.vbooks.siteadmin.command.ManageUserResult;
import com.vekomy.vbooks.siteadmin.dao.ManageUserDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * @author swarupa
 *
 */
public class ManageUserAction extends BaseAction {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ManageUserAction.class);
	/**
	 * HttpSession variable holds session.
	 */
	private HttpSession session;

	/* (non-Javadoc)
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@Override
	public IResult process(Object form)  {
		try {
			ManageUserAddressCommand manageUserAddressCommand = null;
			ManageUserBasicCommand manageUserBasicCommand = null;
			ManageUserCommand manageUserCommand = null;
			ManageUserDao manageUserDao = (ManageUserDao) getDao();
			ResultSuccess resultSuccess = new ResultSuccess();
			String resultSuccessStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
			String resultSuccessMessage = Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE);
			String persistSuccessMessage = Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE);
			session = request.getSession(Boolean.TRUE);
			String userName = getUsername();
			if(form instanceof ManageUserCommand) {
				manageUserCommand = (ManageUserCommand) form;
				String action = manageUserCommand.getAction();
				if("get-organizations".equals(action)) {
					List<String> organizationList = manageUserDao.getOrganizations();
					
					resultSuccess.setData(organizationList);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("save-manage-user".equals(action)) {
					manageUserDao.saveManageUser((ManageUserBasicCommand)session.getAttribute("manage-user-basic"), 
							(ManageUserAddressCommand)session.getAttribute("manage-user-address"), userName);
					
					// Removing data from session.
					removeUserDataFromSession(session);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(persistSuccessMessage);
				} else if("update-user".equals(action)) {
					manageUserDao.updateUser((ManageUserBasicCommand)session.getAttribute("manage-user-basic"), 
							(ManageUserAddressCommand)session.getAttribute("manage-user-address"), userName);
					
					// Removing data from session.
					removeUserDataFromSession(session);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if("get-assigned-user".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					List<String> organizations = manageUserDao.getAssignedOrganization(id);
					
					resultSuccess.setData(organizations);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("get-employee-data".equals(action)) {
					int id = Integer.parseInt(request.getParameter("id"));
					EmployeeResult employeeResult= manageUserDao.getEmployeeData(id);
					
					resultSuccess.setData(employeeResult);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("modify-manage-user-status".equals(action)) {
					Integer userId = Integer.parseInt(request.getParameter("id"));
					String userStatus = request.getParameter("userStatus");
					manageUserDao.modifyUserStatus(userId, userStatus, userName);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if ("get-disabled-users-list".equals(action)) {
					List<ManageUserResult> list = manageUserDao.getDisabledUsersList();
					
					resultSuccess.setData(list);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} //removing data from session on click of cancel button in Manage Group User add page.
				else if ("remove-manage-user-session-data".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					//Removing data from session.
					removeUserDataFromSession(session);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.REMOVE_DATA_FROM_SESSION_MESSAGE));
				}
			} else if(form instanceof ManageUserAddressCommand) {
				manageUserAddressCommand = (ManageUserAddressCommand) form;
				session.setAttribute("manage-user-address", manageUserAddressCommand);
				
				resultSuccess.setStatus(resultSuccessStatus);
				resultSuccess.setMessage(persistSuccessMessage);
			} else if(form instanceof ManageUserBasicCommand) {
				manageUserBasicCommand = (ManageUserBasicCommand) form;
				String action = manageUserBasicCommand.getAction();
				if ("manage-user-basic".equals(action)) {
					session.setAttribute("manage-user-basic", manageUserBasicCommand);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(persistSuccessMessage);
				} else if("search-user".equals(action)){
					HashMap<String, SearchFilterData> filter = new HashMap<String, SearchFilterData>();
					filter.put("username", new SearchFilterData("username", manageUserBasicCommand.getUsername(), SearchFilterData.TYPE_STRING_STR));
					filter.put("mobile", new SearchFilterData("employeeEmail", manageUserBasicCommand.getEmployeeEmail(), SearchFilterData.TYPE_STRING_STR));
					List<ManageUserResult> list = manageUserDao.searchUser(manageUserBasicCommand);
					
					resultSuccess.setData(list);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("delete-user".equals(action)){
					manageUserDao.deleteUser(manageUserBasicCommand);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE));
				}
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());
			
			if(_logger.isErrorEnabled()) {
				_logger.error("ResultError: {}", resultError);
			}
			return resultError;
		}
	}
	
	/**
	 * @param session - {@link HttpSession}
	 * 
	 * This method is responsible to remove data from session. 
	 */
	private void removeUserDataFromSession(HttpSession session){
		session.removeAttribute("manage-user-basic");
		session.removeAttribute("manage-user-address");
	}
	

}
