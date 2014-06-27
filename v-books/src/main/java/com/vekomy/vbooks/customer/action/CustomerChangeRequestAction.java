/**
 * com.vekomy.vbooks.customer.action.CustomerChangeRequestAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 15, 2013
 */
package com.vekomy.vbooks.customer.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.customer.command.CustomerCRBusinessNamesHistoryResult;
import com.vekomy.vbooks.customer.command.CustomerChangeRequestCommand;
import com.vekomy.vbooks.customer.command.CustomerChangeRequestHistoryResult;
import com.vekomy.vbooks.customer.command.CustomerResult;
import com.vekomy.vbooks.customer.dao.CustomerChangeRequestDao;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbCustomerChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbCustomerDetail;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.DirectoryUtil;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * This Action Is Responsible to Perform CustomerChangeRequest Operations.
 * 
 * @author priyanka
 * 
 */
public class CustomerChangeRequestAction extends BaseAction {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(CustomerAction.class);
	/**
	 * String variable holds CUSTOMER_CR_BASIC.
	 */
	private static final String CUSTOMER_CR_BASIC = "customerCr-basic";
	/**
	 * String variable holds CUSTOMER_CR_DETAIL.
	 */
	private static final String CUSTOMER_CR_DETAIL = "customerCr-detail";
	/**
	 * HttpSession variable holds session.
	 */
	private HttpSession session;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@Override
	public IResult process(Object form){
		try {
			CustomerChangeRequestCommand customerCrCommand = null;
			ResultSuccess resultSuccess = new ResultSuccess();
			CustomerChangeRequestDao customerDao = (CustomerChangeRequestDao) getDao();
			if (form instanceof CustomerChangeRequestCommand) {
				String userName = getUsername();
				VbOrganization organization = getOrganization();
				String resultSuccessStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
				String resultSuccessMessage = Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE);
				customerCrCommand = (CustomerChangeRequestCommand) form;
				String action = customerCrCommand.getAction();
				if ("save-basiccr".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					session.setAttribute(CUSTOMER_CR_BASIC, customerCrCommand);
					if (customerCrCommand.getCrType() == Boolean.TRUE) {
						VbCustomerDetail customerDetail = customerDao.getCustomerDetails(customerCrCommand,getOrganization());
						resultSuccess.setData(customerDetail);
						resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
						resultSuccess.setStatus(resultSuccessStatus);
					} else {
						resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
						resultSuccess.setStatus(resultSuccessStatus);
					}
				} else if ("save-crdetail".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					session.setAttribute(CUSTOMER_CR_DETAIL, customerCrCommand);
					
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("save-customer-cr".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					CustomerChangeRequestCommand customerChangeRequestCommand = (CustomerChangeRequestCommand) session
							.getAttribute(CUSTOMER_CR_BASIC);
					customerDao.saveCustomerCrDetails(customerChangeRequestCommand, 
							(CustomerChangeRequestCommand) session.getAttribute(CUSTOMER_CR_DETAIL),
							organization, userName);

					//For Alerts.
					String alertMysalesPage = null;
					if(Boolean.TRUE == customerChangeRequestCommand.getCrType()) {
						alertMysalesPage = OrganizationUtils.ALERT_MYSALES_PAGE_EXISTING_CUSTOMER_CR;
					} else {
						alertMysalesPage = OrganizationUtils.ALERT_MYSALES_PAGE_NEW_CUSTOMER_CR;
					}
					String defaultToRecipient = organization.getSuperUserName();
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, 
							OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_APPROVALS,
							alertMysalesPage, Msg.get(MsgEnum.ALERT_TYPE_CUSTOMER_CHANGE_REQUEST_RAISED_MESSAGE));
					
					 _logger.info("Firing an alert for Customer Change Request");
					 
					// Removing session data.
					removeCustomerCrDataFromSession(session);
					
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("search-cr-onload".equals(action)) {
					List<CustomerResult> list = customerDao.getCrDetails(customerCrCommand, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("search-customer-cr".equals(action)) {
					List<CustomerResult> list = customerDao.searchCustomerCr(customerCrCommand, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				}else if ("validate-new-customer-CR-businessName".equals(action)) {
					String businessName=request.getParameter("businessName");
					Boolean isBusinessNameAvailable = customerDao.validateBusinessName(businessName, organization);
					resultSuccess.setData(isBusinessNameAvailable);
					resultSuccess.setMessage(Msg.get(MsgEnum.BUSINESS_NAME_AVAILABILITY_RESULT_MESSAGE));
				} 
				else if ("validate-duplicate-customer-business-name".equals(action)) {
					Integer customerCRId=Integer.valueOf(request.getParameter("customerCRId"));
					Boolean isBusinessNameAvailable = customerDao.validateApprovedNewCustomerCRBusinessName(customerCRId, organization);
					resultSuccess.setData(isBusinessNameAvailable);
					resultSuccess.setMessage(Msg.get(MsgEnum.BUSINESS_NAME_AVAILABILITY_RESULT_MESSAGE));
				} 
				else if ("approve-customer-cr".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					String status = request.getParameter("status");
					Float creditLimit=Float.valueOf(request.getParameter("creditLimit"));
					Integer creditOverdueDays=Integer.valueOf(request.getParameter("creditOverdueDays"));
					customerCrCommand.setCreditLimit(creditLimit.toString());
					customerCrCommand.setCreditOverdueDays(creditOverdueDays.toString());
					customerDao.approveOrDeclineCustomerCr(id, status, organization, userName, customerCrCommand);
					
					//For Alerts.
					VbCustomerChangeRequest customerChangeRequest = customerDao.getVbCustomerChangeRequestById(id);
					String alertMysalesPage = null;
					if(Boolean.TRUE == customerChangeRequest.getCrType()) {
						alertMysalesPage = OrganizationUtils.ALERT_MYSALES_PAGE_EXISTING_CUSTOMER_CR;
					} else {
						alertMysalesPage = OrganizationUtils.ALERT_MYSALES_PAGE_NEW_CUSTOMER_CR;
						
						// Creating directory structure for new customer.
						DirectoryUtil.getInstance().createDirectoryStructure(organization.getName(), customerCrCommand.getBusinessName());
					}
					String defaultToRecipient = customerChangeRequest.getCreatedBy();
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, 
							OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_APPROVALS,
							alertMysalesPage, Msg.get(MsgEnum.ALERT_TYPE_CUSTOMER_CHANGE_REQUEST_APPROVED_MESSAGE));
					 _logger.info("Firing an alert for Customer Change Request");
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if ("check-customer-cr-type".equals(action)) {
					String customerBusinessName = request.getParameter("customerBusinessName");
					String result = customerDao.checkCustomerCr(organization, customerBusinessName);
					
					resultSuccess.setData(result);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-customer-cr-history".equals(action)) {
					List<CustomerChangeRequestHistoryResult> customerChangeRequestHistory = customerDao.getCustomerChangeRequestHistory(organization, userName);
					
					resultSuccess.setData(customerChangeRequestHistory);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("get-customer-cr-businessNames-history".equals(action)) {
		        	String customerType = request.getParameter("customerType");
		        	String status = request.getParameter("status");
					List<CustomerCRBusinessNamesHistoryResult> customerBusinessNameHistory = customerDao.getCustomerCRBusinessNamesHistory(customerType, status, organization, userName);
					
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setData(customerBusinessNameHistory);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("get-previous-customer-cr-id".equals(action)) {
		        	String businessName=request.getParameter("businessName");
		        	Integer customerCRId=Integer.valueOf(request.getParameter("customerCRId"));
		        	Integer result = customerDao.getPreviousCustomerCRId(customerCRId, businessName, organization, userName);
					resultSuccess.setData(result);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("get-customer-change-request-data".equals(action)) {
					Integer id =Integer.parseInt(request.getParameter("id"));
					String businessName = request.getParameter("businessName");
					CustomerResult crResult = customerDao.getCustomerCrData(id, businessName, organization);
					
					resultSuccess.setData(crResult);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if("get-customer-change-request-credit-data".equals(action)) {
					Integer id =Integer.parseInt(request.getParameter("id"));
					CustomerResult crResult = customerDao.getCustomerCrDataForCredit(id, organization);
					
					resultSuccess.setData(crResult);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
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

	/*
	 * This method is responsible to remove data from session.
	 */
	private void removeCustomerCrDataFromSession(HttpSession session) {
		session.removeAttribute(CUSTOMER_CR_BASIC);
		session.removeAttribute(CUSTOMER_CR_DETAIL);
	}
}
