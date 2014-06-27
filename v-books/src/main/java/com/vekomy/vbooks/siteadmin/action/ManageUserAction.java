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

import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.siteadmin.command.ManageUserAddressCommand;
import com.vekomy.vbooks.siteadmin.command.ManageUserBasicCommand;
import com.vekomy.vbooks.siteadmin.command.ManageUserCommand;
import com.vekomy.vbooks.siteadmin.command.ManageUserResult;
import com.vekomy.vbooks.siteadmin.dao.ManageUserDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;

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
	public IResult process(Object form) throws Exception {
		ManageUserAddressCommand manageUserAddressCommand = null;
		ManageUserBasicCommand manageUserBasicCommand = null;
		ManageUserCommand manageUserCommand = null;
		ManageUserDao manageUserDao = (ManageUserDao) getDao();
		ResultSuccess resultSuccess = new ResultSuccess();
		session = request.getSession(Boolean.TRUE);
		if(form instanceof ManageUserCommand) {
			manageUserCommand = (ManageUserCommand) form;
			String action = manageUserCommand.getAction();
			if("get-organizations".equals(action)) {
				List<String> organizationList = manageUserDao.getOrganizations();
				resultSuccess.setData(organizationList);
				resultSuccess.setMessage("Fetched Organizations successfully");
			} else if ("save-manage-user".equals(action)) {
				manageUserDao.saveManageUser((ManageUserBasicCommand)session.getAttribute("manage-user-basic"), (ManageUserAddressCommand)session.getAttribute("manage-user-address"), getUsername());
				removeUserDataFromSession();
				resultSuccess.setMessage("Saved successfully");
			}else if("update-user".equals(action)){
				manageUserDao.updateUser((ManageUserBasicCommand)session.getAttribute("manage-user-basic"), (ManageUserAddressCommand)session.getAttribute("manage-user-address"), getUsername());
				removeUserDataFromSession();
				resultSuccess.setMessage("Updated successfully");
			}else if("get-assigned-user".equals(action)){
				int id = Integer.parseInt(request.getParameter("id"));
				List<String> organizations=manageUserDao.getAssignedOrganization(id);
				resultSuccess.setData(organizations);
			}
		} else if(form instanceof ManageUserAddressCommand) {
			manageUserAddressCommand = (ManageUserAddressCommand) form;
			session.setAttribute("manage-user-address", manageUserAddressCommand);
		} else if(form instanceof ManageUserBasicCommand) {
			manageUserBasicCommand = (ManageUserBasicCommand) form;
			session.setAttribute("manage-user-basic", manageUserBasicCommand);
			String action =manageUserBasicCommand.getAction();
			 if("search-user".equals(action)){
				HashMap<String, SearchFilterData> filter = new HashMap<String, SearchFilterData>();
				filter.put("username", new SearchFilterData("username", manageUserBasicCommand.getUsername(), SearchFilterData.TYPE_STRING_STR));
				filter.put("mobile", new SearchFilterData("employeeEmail", manageUserBasicCommand.getEmployeeEmail(), SearchFilterData.TYPE_STRING_STR));
				List<ManageUserResult> list = manageUserDao.searchUser(manageUserBasicCommand);
			if (list.isEmpty()) {
				resultSuccess.setMessage("No Records Found");
			} else {
				resultSuccess.setData(list);
				resultSuccess.setMessage("Search completed successfully");
			}
			}else if("delete-user".equals(action)){
				manageUserDao.deleteUser(manageUserBasicCommand);
				resultSuccess.setMessage("User deleted successfully");
			}
		}
		if(_logger.isDebugEnabled()) {
			_logger.debug("resultSuccess: {}", resultSuccess);
		}
		return resultSuccess;
	}
	
	/*
	 * This method is responsible to remove data from session. 
	 */
	private void removeUserDataFromSession(){
		session = request.getSession(Boolean.TRUE);
		session.removeAttribute("manage-user-basic");
		session.removeAttribute("manage-user-address");
	}
	

}
