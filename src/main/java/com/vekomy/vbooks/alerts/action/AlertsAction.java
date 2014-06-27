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

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.command.AlertsCommand;
import com.vekomy.vbooks.alerts.command.AlertsHistoryResult;
import com.vekomy.vbooks.alerts.command.AlertsResult;
import com.vekomy.vbooks.alerts.command.MyAlertResult;
import com.vekomy.vbooks.alerts.command.ViewAlertResult;
import com.vekomy.vbooks.alerts.dao.AlertsDao;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbUserDefinedAlertsNotifications;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

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
	public IResult process(Object form) {
		String resultStatusFailure = OrganizationUtils.RESULT_STATUS_FAILURE;
		try {
			AlertsCommand alertsCommand = null;
			ResultSuccess resultSuccess = new ResultSuccess();
			String resultSuccessMessage = Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE);
			String resultStatusSuccess = OrganizationUtils.RESULT_STATUS_SUCCESS;
			String persistSuccessMessage = Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE);
			String deleteSuccessMessage = Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE);
			String updateSuccessMessage = Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE);
			AlertsDao alertDao = (AlertsDao) getDao();
			String userName = getUsername();
			VbOrganization organization = getOrganization();
			if (form instanceof AlertsCommand) {
				alertsCommand = (AlertsCommand) form;
				String editAction = request.getParameter("editaction");
				String action = alertsCommand.getAction();
				if ("get-alert-type".equals(action)) {
					List<String> alertTypeList = alertDao.getAlertType();
					
					resultSuccess.setData(alertTypeList);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("user-defined-alerts-form".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					String salesTypes = request.getParameter("salesTypeArray");
					if (salesTypes != "") {
						session.setAttribute("sales-types", salesTypes);
					}
					session.setAttribute("save-user-defined", alertsCommand);
					
					resultSuccess.setMessage(persistSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("save-user-defined-alerts".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					String notificationType = request.getParameter("notificationType");
					String group = request.getParameter("group");
					String userNames = request.getParameter("usersArray");
					assignMultipleGroups = (HashMap<String, String>) session.getAttribute("assignMultipleGroups");
					if (assignMultipleGroups == null) {
						alertDao.saveUserDefinedAlerts(notificationType, group,	userNames,(AlertsCommand) session.getAttribute("save-user-defined"),
								(VbUserDefinedAlertsNotifications) session.getAttribute("user-defined-alert-notification"),	organization, userName,
								(String) session.getAttribute("sales-types"));
						
					} else {
						if (!("".equals(group))) {
							assignMultipleGroups.put(group, userNames);
						}
						alertDao.saveUserDefinedAlertForMultipleGroups((AlertsCommand) session.getAttribute("save-user-defined"), assignMultipleGroups, notificationType, organization, userName,(String) session.getAttribute("sales-types"));
						removeGroupsDataFromSession(session);
						assignMultipleGroups.clear();
					}
					
					resultSuccess.setMessage(persistSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("update-system-defined-alerts".equals(editAction)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					session = request.getSession(Boolean.TRUE);
					String notificationType = request.getParameter("notificationType");
					String group = request.getParameter("group");
					String userNames = request.getParameter("usersArray");
					HashMap<String, String> sessionMap = (HashMap<String, String>) session.getAttribute("assignMultipleGroups");
					if (sessionMap == null) {
						alertDao.updateSystemDefinedAlert(id, alertsCommand, notificationType, group, userNames, organization, userName);
					} else {
						if (!("".equals(group))) {
							sessionMap.put(group, userNames);
						}
						alertDao.updateSystemDefinedAlertForMultipleGroups(id, alertsCommand, sessionMap, notificationType,	organization, userName);
						removeGroupsDataFromSession(session);
						assignMultipleGroups.clear();
					}
					
					resultSuccess.setMessage(updateSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("get-user-alert-types".equals(action)) {
					List<String> userAlertTypesList = alertDao.getUserDefinedAlerts();
					
					resultSuccess.setData(userAlertTypesList);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("get-approval-types".equals(action)) {
					List<String> approvalTypes = alertDao.getMysalesTypes(request.getParameter("value"), organization);
					
					resultSuccess.setData(approvalTypes);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("get-mysales-types".equals(action)) {
					List<String> mySalesTypes = alertDao.getMySalesTypes();
					
					resultSuccess.setData(mySalesTypes);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("get-transaction-cr-types".equals(action)) {
					List<String> transactionTypes = alertDao.getMysalesTypes(request.getParameter("value"), organization);
					
					resultSuccess.setData(transactionTypes);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("get-groups".equals(action)) {
					List<String> groupList = alertDao.getConfiguredGroups(organization);
					
					resultSuccess.setData(groupList);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("get-associated-users".equals(action)) {
					String group = request.getParameter("group");
					List<String> usersList = alertDao.getAssociatedUsers(group, organization);
					
					resultSuccess.setData(usersList);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("save-system-defined-alert".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					String notificationType = request.getParameter("notificationType");
					String group = request.getParameter("group");
					String userNames = request.getParameter("usersArray");
					HashMap<String, String> sessionMap = (HashMap<String, String>) session.getAttribute("assignMultipleGroups");
					if (sessionMap == null) {
						alertDao.saveSystemDefinedAlert(alertsCommand, notificationType, group, userNames, organization, userName);
					} else {
						if (!("".equals(group))) {
							sessionMap.put(group, userNames);
						}
						alertDao.saveSystemDefinedAlertForMultipleGroups(alertsCommand, sessionMap, notificationType, organization, userName);
						
						removeGroupsDataFromSession(session);
						assignMultipleGroups.clear();
					}
					
					resultSuccess.setMessage(persistSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("fire-alert-for-multiple-group".equals(action)) {
					String usersArray = request.getParameter("usersArray");
					String group = request.getParameter("group");
					if(assignMultipleGroups == null) {
						assignMultipleGroups = new HashMap<String, String>();
					}
					assignMultipleGroups.put(group, usersArray);
					session = request.getSession(Boolean.TRUE);
					session.setAttribute("assignMultipleGroups", assignMultipleGroups);
				} else if ("cancel-form".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					removeGroupsDataFromSession(session);
					assignMultipleGroups.clear();
				} else if ("cancel-user-form".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					removeUserDefinedDataFromSession(session);
				} else if ("get-alert-category".equals(action)) {
					List<String> alertCategoryList = alertDao.getAlertCategory();
					
					resultSuccess.setData(alertCategoryList);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("get-alert-type-based-on-category".equals(action)) {
					String alertCategory = request.getParameter("alertCategory");
					List<String> alertTypeList = alertDao.getAlertType(alertCategory);
					
					resultSuccess.setData(alertTypeList);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("view-alerts".equals(action)) {
					String alertCategory = null;
					String alertType = null;
					List<ViewAlertResult> alertList = alertDao.getAllAlerts(alertCategory, alertType, organization);
					
					resultSuccess.setData(alertList);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("search-criteria-for-alerts".equals(action)) {
					String alertCategory = request.getParameter("alertCategory");
					String alertType = request.getParameter("alertType");
					List<ViewAlertResult> alertList = alertDao.getAllAlerts(alertCategory, alertType, organization);
					
					resultSuccess.setData(alertList);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("delete-system-alerts".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					alertDao.deleteSystemAlerts(id);
					
					resultSuccess.setMessage(deleteSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("delete-user-alerts".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					alertDao.deleteUserAlerts(id);
					
					resultSuccess.setMessage(deleteSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("enable-or-disable-alert".equals(action)) {
					String checkBoxId = request.getParameter("checkBoxId");
					String checkBoxStatus = request	.getParameter("checkBoxStatus");
					String category = request.getParameter("category");
					Integer id = Integer.parseInt(checkBoxId);
					Boolean status = Boolean.parseBoolean(checkBoxStatus);
					alertDao.enableOrDisableAlert(id, status, category);
					
					resultSuccess.setMessage(updateSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("get-system-alerts-data".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					List<AlertsResult> systemAlertsNotifications = alertDao.getSystemAlertsDataByid(id);
					
					resultSuccess.setData(systemAlertsNotifications);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if("get-user-alert-data-by-id".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					AlertsResult alertsResult = alertDao.getUserAlertsData(id);
					
					resultSuccess.setData(alertsResult);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if("get-system-defined-alerts-data".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					List<AlertsResult> userAlertData = alertDao.getUserAlertsDataById(id);
					
					resultSuccess.setData(userAlertData);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if("get-system-alert-data-by-id".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					AlertsResult alertsResult = alertDao.getSystemAlertsData(id);
					
					resultSuccess.setData(alertsResult);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("get-alertHistory-records-count".equals(action)) {
					String alertCategory = null;
					String alertType = null;
					String alertName = null;
					String startDate = request.getParameter("startDate");
					String endDate = request.getParameter("endDate");
					Integer recordsCount = alertDao.getAlertsHistoryRecordsCount(alertCategory, alertType, alertName, organization, startDate, endDate);
					if(recordsCount > 0) {
						resultSuccess.setData(recordsCount);
						resultSuccess.setMessage("Total Number of Records are:"+recordsCount);
						resultSuccess.setStatus(resultStatusSuccess);
					} else {
						resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
						resultSuccess.setStatus(OrganizationUtils.RESULT_STATUS_FAILURE);
					}
				} else if ("alerts-history".equals(action)) {
					String alertCategory = null;
					String alertType = null;
					String alertName = null;
					Integer pageNumber=Integer.valueOf(request.getParameter("pageNumber"));
					String startDate = request.getParameter("startDate");
					String endDate = request.getParameter("endDate");
					List<AlertsHistoryResult> alertHistoryList = null;
					alertHistoryList = alertDao.getAlertsHistory(pageNumber,alertCategory, alertType, alertName, organization, startDate, endDate);
					
					resultSuccess.setData(alertHistoryList);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("search-criteria-for-alerts-history".equals(action)) {
					String alertCategory = request.getParameter("alertCategory");
					String alertType = request.getParameter("alertType");
					String startDate = request.getParameter("startDate");
					String endDate = request.getParameter("endDate");
					String alertName = request.getParameter("alertName");
					Integer pageNumber=Integer.valueOf(request.getParameter("pageNumber"));
					List<AlertsHistoryResult> alertList = alertDao.getAlertsHistory(pageNumber,alertCategory, alertType, alertName, organization, startDate, endDate);
					
					resultSuccess.setData(alertList);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("get-myalerts-records-count".equals(action)) {
					Integer recordsCount=alertDao.getRecordsCount(getOrganization(),getUsername());
					if(recordsCount > 0){
						resultSuccess.setData(recordsCount);
						resultSuccess.setMessage("Total Number of Records are:"+recordsCount);
						resultSuccess.setStatus(resultStatusSuccess);
					}else{
						resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
						resultSuccess.setStatus(OrganizationUtils.RESULT_STATUS_FAILURE);
					}
				} else if ("get-my-alert-history".equals(action)) {
					Integer alertsPageNumber=Integer.valueOf(request.getParameter("pageNumber"));
					List<MyAlertResult> resultList = alertDao.getMyAlerts(alertsPageNumber,userName, organization);
					
					resultSuccess.setData(resultList);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("get-user-alerts-data".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					List<AlertsResult> userDefinedAlerts = alertDao.getUserDefinedAlertsData(id);
					
					if(userDefinedAlerts != null){
						resultSuccess.setData(userDefinedAlerts);
						resultSuccess.setMessage(resultSuccessMessage);
						resultSuccess.setStatus(resultStatusSuccess);
					}
				} else if("update-user-defined-alerts".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					String notificationType = request.getParameter("notificationType");
					String groups = request.getParameter("groupArray");
					String userNames = request.getParameter("usersArray");
					assignMultipleGroups = (HashMap<String, String>) session.getAttribute("assignMultipleGroups");
					if (assignMultipleGroups == null) {
						alertDao.updateUserDefinedAlerts(id,notificationType, groups,userNames,(AlertsCommand) session.getAttribute("save-user-defined"),
								(VbUserDefinedAlertsNotifications) session.getAttribute("user-defined-alert-notification"),	organization, userName,
								(String) session.getAttribute("sales-types"));
						
					} else {
						if (!("".equals(groups))) {
							assignMultipleGroups.put(groups, userNames);
						}
						alertDao.updateUserDefinedAlertForMultipleGroups(id,(AlertsCommand) session.getAttribute("save-user-defined"), assignMultipleGroups, notificationType, organization, userName,(String) session.getAttribute("sales-types"));
						removeGroupsDataFromSession(session);
						assignMultipleGroups.clear();
					}
					resultSuccess.setMessage(updateSuccessMessage);
				} else if("get-list-users-for-this-record".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					List<AlertsResult> userNames = alertDao.getUserNamesForThisAlert(id,organization);
					
					resultSuccess.setData(userNames);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				}

			} else if (form instanceof VbUserDefinedAlertsNotifications) {
				VbUserDefinedAlertsNotifications vbUserDefinedAlertsNotifications = (VbUserDefinedAlertsNotifications) form;
				String action = request.getParameter("action");
				String notificationType = request.getParameter("notificationType");
				String group = request.getParameter("group");
				String gropValue = null;
				String userNameValues = null;
				String userNameValue = null;
				String userNames = request.getParameter("usersArray");
				if ("save-user-defined-alert-notification".equals(action)) {
					
					assignMultipleGroups = (HashMap<String, String>) session.getAttribute("assignMultipleGroups");
					if(assignMultipleGroups != null){
						for (Map.Entry<String, String> entry : assignMultipleGroups.entrySet()) {
							gropValue =  entry.getKey();
							userNameValue = entry.getValue();
							/*	sessionGroupData = (String) session.getAttribute("group");
								sessionUserData = (String) session.getAttribute("userNames");
								if(sessionGroupData != null){
									gropValue = sessionGroupData.concat(entry.getKey());
									session.setAttribute("group", gropValue);
								}else{
									gropValue =  entry.getKey();
									session.setAttribute("group", gropValue);
								}
								if(sessionUserData != null){
									userNameValues = sessionUserData.concat(entry.getValue());
									session.setAttribute("userNames", userNameValues);
								}else{
									userNameValue = entry.getValue();
									session.setAttribute("userNames", userNameValue);
								}*/
							if(userNames != ""){
								userNameValues = userNameValue.concat(userNames);
								session.setAttribute("userNames", userNameValues);
							}else{
								session.setAttribute("userNames", userNameValue);
							}
							if(group != null){
								gropValue = gropValue.concat(group);
								session.setAttribute("group", gropValue);
							}else{
								session.setAttribute("group", gropValue);
							}
						}
					}else{
						session.setAttribute("group", group);
						session.setAttribute("userNames", userNames);
					}
					session = request.getSession(Boolean.TRUE);
					session.setAttribute("notification-types", notificationType);
					session.setAttribute("user-defined-alert-notification",	vbUserDefinedAlertsNotifications);
					resultSuccess.setMessage(persistSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				}
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;

		} catch (DataAccessException exception) {
			if(assignMultipleGroups != null) {
				assignMultipleGroups.clear();
			}
			ResultError resultError = new ResultError();
			resultError.setMessage(exception.getMessage());
			resultError.setStatus(resultStatusFailure);
			
			if(_logger.isErrorEnabled()) {
				_logger.error("result: {}", resultError);
			}
			return resultError;
		} catch (ParseException exception) {
			ResultError resultError = new ResultError();
			resultError.setMessage(exception.getMessage());
			resultError.setStatus(resultStatusFailure);
			
			if(_logger.isErrorEnabled()) {
				_logger.error("result: {}", resultError);
			}
			return resultError;
		}
	}
	
	/**
	 * This method is responsible to remove the data from session.
	 * 
	 * @param session - {@link HttpSession}
	 */
	private void removeGroupsDataFromSession(HttpSession session){
		session.removeAttribute("assignMultipleGroups"); 
	}
	/**
	 * This method is responsible to remove the data from session.
	 * 
	 * @param session - {@link HttpSession}
	 */
	private void removeUserDefinedDataFromSession(HttpSession session){
		session.removeAttribute("user-defined-alert-notification");
		session.removeAttribute("save-user-defined");
		session.removeAttribute("assignMultipleGroups"); 
	}
}
