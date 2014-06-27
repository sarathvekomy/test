/**
 * com.vekomy.vbooks.employee.action.EmployeeAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 10, 2013
 */
package com.vekomy.vbooks.employee.action;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.employee.command.EmployeeCommand;
import com.vekomy.vbooks.employee.command.EmployeeNotificationResult;
import com.vekomy.vbooks.employee.command.EmployeeResult;
import com.vekomy.vbooks.employee.command.LoginTrackResult;
import com.vekomy.vbooks.employee.dao.EmployeeDao;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbEmployeeAddress;
import com.vekomy.vbooks.hibernate.model.VbEmployeeDetail;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.RSSFeedReader;
import com.vekomy.vbooks.util.RssFeedReaderUtil;

/**
 * This action class is responsible to process the employee activities.
 * 
 * @author Sudhakar
 * 
 * 
 */
public class EmployeeAction extends BaseAction {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(EmployeeAction.class);
	
	/**
	 * String variable holds EMPLOYEE_BASIC.
	 */
	private static final String EMPLOYEE_BASIC = "employee-basic";
	/**
	 * String variable holds EMPLOYEE_DETAILS.
	 */
	private static final String EMPLOYEE_DETAILS = "employee-detail";
	/**
	 * String variable holds EMPLOYEE_ADDRESS.
	 */
	private static final String EMPLOYEE_ADDRESS = "employee-address";
	/**
	 * HttpSession variable holds session.
	 */
	private HttpSession session;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	public IResult process(Object form) {
		try {
			EmployeeCommand employeeCommand = null;
			ResultSuccess resultSuccess = new ResultSuccess();
			String resultSuccessStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
			String resuleSuccessMessage = Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE);
			String persistSuccessMessage = Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE);
			if (form instanceof EmployeeCommand) {
				employeeCommand = (EmployeeCommand) form;
				String action = employeeCommand.getAction();
				EmployeeDao employeeDao = (EmployeeDao) getDao();
				VbOrganization organization = getOrganization();
				String userName = getUsername();
				if ("save-basic".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					String employeeNo = employeeDao.generateEmployeeNumber( organization, employeeCommand.getEmployeeType());
					employeeCommand.setEmployeeNumber(employeeNo);
					employeeCommand.setVbOrganization(organization);
					session.setAttribute(EMPLOYEE_BASIC, employeeCommand);
					
					resultSuccess.setMessage(persistSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("save-employee".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					employeeDao.saveEmployee((EmployeeCommand) session.getAttribute(EMPLOYEE_BASIC),
							(VbEmployeeDetail) session.getAttribute(EMPLOYEE_DETAILS),
							(VbEmployeeAddress) session.getAttribute(EMPLOYEE_ADDRESS), userName, organization);

					// removing the session data.
					removeEmployeeDataFromSession(session);
					
					resultSuccess.setMessage(persistSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("save-cancel".equals(action)) {
					removeEmployeeDataFromSession(request.getSession(Boolean.TRUE));
					resultSuccess.setMessage(Msg.get(MsgEnum.CANCELED_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("search-employee".equals(action)) {
					HashMap<String, Object> filter = new HashMap<String, Object>();
					filter.put("username", new SearchFilterData("username", employeeCommand.getUsername(), SearchFilterData.TYPE_STRING_STR));
					filter.put("firstName", new SearchFilterData("firstName", employeeCommand.getFirstName(), SearchFilterData.TYPE_STRING_STR));
					List<EmployeeResult> list = employeeDao.searchEmployee(employeeCommand,userName, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("update-employee".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					employeeDao.updateEmployee((EmployeeCommand) session.getAttribute(EMPLOYEE_BASIC),
							(VbEmployeeDetail) session.getAttribute(EMPLOYEE_DETAILS),
							(VbEmployeeAddress) session.getAttribute(EMPLOYEE_ADDRESS),
							userName, organization);
					
					// removing the session data.
					removeEmployeeDataFromSession(session);
					
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
				}else if("get-employee-data".equals(action)){
					int id = Integer.parseInt(request.getParameter("id"));
					EmployeeResult employeeResult= employeeDao.getEmployeeData(id,organization);
					resultSuccess.setData(employeeResult);
					resultSuccess.setMessage("Records Fetched Successfully");
				} else if ("delete-employee".equals(action)) {
					employeeDao.deleteEmployee(employeeCommand , organization);
					resultSuccess.setMessage(Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("modify-employee-status".equals(action)) {
					String employeeStatus = request.getParameter("employeeStatusParam");
					employeeDao.modifyEmployeeStatus(employeeCommand, employeeStatus,organization);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("check-credits-for-sales-executive".equals(action)) {
				try{
					Boolean result=employeeDao.checkForSalesExecutiveCredits(employeeCommand.getId(),organization);
					resultSuccess.setData(result);
					resultSuccess.setStatus(resultSuccessStatus);
				}catch (DataAccessException e) {
					resultSuccess.setMessage(e.getMessage());
				}
					
					
				} 
				else if ("get-disabled-employee-list".equals(action)) {
					List<EmployeeResult> list = employeeDao.getDisabledEmployeeList(organization, userName);
					
					resultSuccess.setData(list);
					resultSuccess.setMessage(Msg.get(MsgEnum.LOGIN_DISABLED_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
				}  else if ("get-login-details".equals(action)) {
					Integer id = Integer.valueOf(request.getParameter("id"));
					List<LoginTrackResult> list = employeeDao.getLoginTracks(id, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resuleSuccessMessage);
				} else if ("rss-feed-lookup".equals(action)) {
					List<RSSFeedReader> feedList = RssFeedReaderUtil.getFeedMessages(userName);
					
					resultSuccess.setData(feedList);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("get-loggedin-user".equals(action)) {
					EmployeeNotificationResult result = employeeDao.getEmployeeNotificationResult(userName, organization);
					
					resultSuccess.setData(result);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resuleSuccessMessage);
				} else if ("validate-username".equals(action)) {
					String userName1 = request.getParameter("username");
					String isAailable = employeeDao.checkUserNameVaialability(userName1, organization);
					
					resultSuccess.setData(isAailable);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resuleSuccessMessage);
					} /*else if("get-organization-prefix".equals(action)) {
						resultSuccess.setData(organization.getUsernamePrefix());
						resultSuccess.setStatus(resultSuccessStatus);
						resultSuccess.setMessage(resuleSuccessMessage);
					}*/
			}else if (form instanceof VbEmployeeDetail) {
				VbEmployeeDetail employeeDetail = (VbEmployeeDetail) form;
				session = request.getSession(Boolean.TRUE);
				session.setAttribute("employee-detail", employeeDetail);

				resultSuccess.setMessage(persistSuccessMessage);
				resultSuccess.setStatus(resultSuccessStatus);
			} else if (form instanceof VbEmployeeAddress) {
				VbEmployeeAddress employeeAddress = (VbEmployeeAddress) form;
				session = request.getSession(Boolean.TRUE);
				session.setAttribute("employee-address", employeeAddress);
				
				resultSuccess.setMessage(persistSuccessMessage);
				resultSuccess.setStatus(resultSuccessStatus);
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());
			
			if(_logger.isErrorEnabled()) {
				_logger.error("resultError: {}", resultError);
			}
			return resultError;
		}
	}
	
	/**
	 * This method is responsible to remove data from session.
	 * 
	 * @param session - {@link HttpSession}
	 */
	private void removeEmployeeDataFromSession(HttpSession session){
		session.removeAttribute(EMPLOYEE_BASIC);
		session.removeAttribute(EMPLOYEE_DETAILS);
		session.removeAttribute(EMPLOYEE_ADDRESS);
	}
	
	
	/**
	 * This inner class is to perform customized sorting on {@link EmployeeResult}. 
	 * 
	 * @author Sudhakar
	 *
	 */
	class EmployeeComparator implements Comparator<EmployeeResult> {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(EmployeeResult str1, EmployeeResult str2) {
			String str3 = str1.getFirstName();
			String str4 = str2.getFirstName();
			return str3.compareToIgnoreCase(str4);
		}

	}

}
