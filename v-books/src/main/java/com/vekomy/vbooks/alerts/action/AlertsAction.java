/**
 * com.vekomy.vbooks.alerts.action.AlertsAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 15, 2013
 */
package com.vekomy.vbooks.alerts.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.command.AlertsCommand;
import com.vekomy.vbooks.alerts.command.ViewAlertResult;
import com.vekomy.vbooks.alerts.dao.AlertsDao;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbUserDefinedAlertsNotifications;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;

/**
 * @author swarupa
 *
 */
public class AlertsAction extends BaseAction {
	/**
	 * HttpSession variable holds session
	 */
	private HttpSession session;
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(AlertsAction.class);
	
	/**
	 * HashMap<String,String> variable holds assigncustomerMap.
	 */
	HashMap<String, String> assignMultipleGroups = new HashMap<String, String>();
	/* (non-Javadoc)
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IResult process(Object form) throws Exception {
		AlertsCommand alertsCommand = null;
		ResultSuccess resultSuccess = new ResultSuccess();
		AlertsDao alertDao = (AlertsDao) getDao();
		String userName = getUsername();
		VbOrganization organization = getOrganization();
		if(form instanceof AlertsCommand) {
			alertsCommand = (AlertsCommand) form;
			String action = alertsCommand.getAction();
			if("get-alert-type".equals(action)) {
				List<String> alertTypeList = alertDao.getAlertType();
				if(alertTypeList.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setData(alertTypeList);
					resultSuccess.setMessage("Fetched Records Successfully");
				}
			}else if("user-defined-alerts-form".equals(action)){
				session =request.getSession(Boolean.TRUE);
				String salesTypes = request.getParameter("salesTypeArray");
				if(salesTypes!=""){
					session.setAttribute("sales-types",salesTypes);
				}
				
				session.setAttribute("save-user-defined", alertsCommand);
			}else if("save-user-defined-alerts".equals(action)){
				String notificationType = request.getParameter("notificationType");
				String group = request.getParameter("group");
				String userNames = request.getParameter("usersArray");
				assignMultipleGroups = (HashMap<String, String>) session.getAttribute("assignMultipleGroups");
				if(assignMultipleGroups == null) {
					alertDao.saveUserDefinedAlerts(notificationType,group,userNames,(AlertsCommand)session.getAttribute("save-user-defined"),(VbUserDefinedAlertsNotifications)session.getAttribute("user-defined-alert-notification"),getOrganization(),getUsername(),( String)session.getAttribute("sales-types"));
					resultSuccess.setMessage("User Defined Alerts Saved Succesfully");
				} else {
					if(!("".equals(group))) {
						assignMultipleGroups.put(group, userNames);
					}
					alertDao.saveUserDefinedAlertForMultipleGroups((AlertsCommand)session.getAttribute("save-user-defined"), assignMultipleGroups, notificationType, organization, userName);
					resultSuccess.setMessage("User Defined Alerts Saved Succesfully");
					removeGroupsDataFromSession(session);
					assignMultipleGroups.clear();
				}
			}
			else if("get-user-alert-types".equals(action)){
				List<String> userAlertTypesList = alertDao.getUserDefinedAlerts();
				resultSuccess.setData(userAlertTypesList);
				resultSuccess.setMessage("User Defined Alert Categories Fetched Successfully");
			}else if("get-approval-types".equals(action)){
				String value =request.getParameter("value");
				List<String> approvalTypes =alertDao.getApprovalTypes(value, organization);
				resultSuccess.setData(approvalTypes);
			}else if("get-mysales-types".equals(action)){
				List<String> mySalesTypes = alertDao.getMySalesTypes();
				resultSuccess.setData(mySalesTypes);
			}else if("get-transaction-cr-types".equals(action)){
			     String value = request.getParameter("value");
				List<String> transactionTypes =alertDao.getTransactionTypes(value,organization);
				resultSuccess.setData(transactionTypes);
			}
			else if("get-groups".equals(action)) {
				List<String> groupList = alertDao.getConfiguredGroups(organization);
				if(groupList.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setData(groupList);
					resultSuccess.setMessage("Fetched Records Successfully");
				}
			} else if("get-associated-users".equals(action)) {
				String group = request.getParameter("group");
				List<String> usersList = alertDao.getAssociatedUsers(group, organization);
				if(usersList.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setData(usersList);
					resultSuccess.setMessage("Fetched Records Successfully");
				}
			} else if("save-system-defined-alert".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				String notificationType = request.getParameter("notificationType");
				String group = request.getParameter("group");
				String userNames = request.getParameter("usersArray");
				HashMap<String, String> sessionMap = (HashMap<String, String>) session.getAttribute("assignMultipleGroups");
				if(sessionMap == null) {
					alertDao.saveSystemDefinedAlert(alertsCommand, notificationType, group, userNames, organization, userName);
					resultSuccess.setMessage("Saved Successfully");
				} else {
					if(!("".equals(group))) {
						sessionMap.put(group, userNames);
					}
					alertDao.saveSystemDefinedAlertForMultipleGroups(alertsCommand, sessionMap, notificationType, organization, userName);
					resultSuccess.setMessage("Saved Successfully");
					removeGroupsDataFromSession(session);
					assignMultipleGroups.clear();
				}
				
			} else if ("fire-alert-for-multiple-group".equals(action)) {
				String usersArray = request.getParameter("usersArray");
				String group = request.getParameter("group");
				assignMultipleGroups.put(group, usersArray);
				session = request.getSession(Boolean.TRUE);
				session.setAttribute("assignMultipleGroups", assignMultipleGroups);
			} else if("cancel-form".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				removeGroupsDataFromSession(session);
				assignMultipleGroups.clear();
			}else if("cancel-user-form".equals(action)){
				session = request.getSession(Boolean.TRUE);
				removeUserDefinedDataFromSession(session);
			} else if("get-alert-category".equals(action)) {
				List<String> alertCategoryList = alertDao.getAlertCategory();
				if(alertCategoryList.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setData(alertCategoryList);
					resultSuccess.setMessage("Fetched Records Successfully");
				}
			} else if("get-alert-type-based-on-category".equals(action)) {
				String alertCategory = request.getParameter("alertCategory");
				List<String> alertTypeList = alertDao.getAlertType(alertCategory);
				if(alertTypeList.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setData(alertTypeList);
					resultSuccess.setMessage("Fetched Records Successfully");
				}
			} else if("view-alerts".equals(action)) {
				String alertCategory = null;
				String alertType = null;
				List<ViewAlertResult> alertList = alertDao.getAllAlerts(alertCategory, alertType, organization);
				if(alertList.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setData(alertList);
					resultSuccess.setMessage("Fetched Records Successfully");
				}
			} else if("search-criteria-for-alerts".equals(action)) {
				String alertCategory = request.getParameter("alertCategory");
				String alertType = request.getParameter("alertType");
				List<ViewAlertResult> alertList = alertDao.getAllAlerts(alertCategory, alertType, organization);
				if(alertList.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setData(alertList);
					resultSuccess.setMessage("Fetched Records Successfully");
				}
			}else if("delete-system-alerts".equals(action)){
				int id = Integer.parseInt(request.getParameter("id"));
				alertDao.deleteSystemAlerts(id);
				resultSuccess.setMessage("Deleted Successfully");
			}else if("delete-user-alerts".equals(action)){
				int id = Integer.parseInt(request.getParameter("id"));
				alertDao.deleteUserAlerts(id);
				resultSuccess.setMessage("Deleted Successfully");
			} else if("enable-or-disable-alert".equals(action)) {
				String checkBoxId = request.getParameter("checkBoxId");
				String checkBoxStatus = request.getParameter("checkBoxStatus");
				String category = request.getParameter("category");
				Integer id = Integer.parseInt(checkBoxId);
				Boolean status = Boolean.parseBoolean(checkBoxStatus);
				alertDao.enableOrDisableAlert(id, status, category);
			}
			
		}else if(form instanceof VbUserDefinedAlertsNotifications){
			VbUserDefinedAlertsNotifications vbUserDefinedAlertsNotifications = (VbUserDefinedAlertsNotifications) form;
			String action = request.getParameter("action");
			String notificationType = request.getParameter("notificationType");
			String group = request.getParameter("group");
			String userNames = request.getParameter("usersArray");
			 if("save-user-defined-alert-notification".equals(action)){
				 session = request.getSession(Boolean.TRUE);
				 session.setAttribute("notification-types", notificationType);
				 session.setAttribute("group", group);
				 session.setAttribute("userNames", userNames);
				 session.setAttribute("user-defined-alert-notification", vbUserDefinedAlertsNotifications);
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
	private void removeGroupsDataFromSession(HttpSession session){
		session.removeAttribute("assignMultipleGroups"); 
	}
	private void removeUserDefinedDataFromSession(HttpSession session){
		session.removeAttribute("user-defined-alert-notification");
		session.removeAttribute("save-user-defined");
		session.removeAttribute("assignMultipleGroups"); 
	}
}
