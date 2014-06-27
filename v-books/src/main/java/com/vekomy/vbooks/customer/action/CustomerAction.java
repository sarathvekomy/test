/**
 * com.vekomy.vbooks.customer.action.CustomerAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 15, 2013
 */
package com.vekomy.vbooks.customer.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.customer.command.CustomerCommand;
import com.vekomy.vbooks.customer.command.CustomerDocumetResults;
import com.vekomy.vbooks.customer.command.CustomerResult;
import com.vekomy.vbooks.customer.dao.CustomerDao;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.exception.ProcessingException;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerDetail;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.DirectoryUtil;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * This action class is responsible to perform customer related operations.
 * 
 * @author Sudhakar
 */
public class CustomerAction extends BaseAction {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(CustomerAction.class);
	
	/**
	 * String variable holds CUSTOMER_BASIC.
	 */
	private static final String CUSTOMER_BASIC = "customer-basic";
	
	/**
	 * String variable holds CUSTOMER_DETAILS.
	 */
	private static final String CUSTOMER_DETAILS = "customer-detail";
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
	public IResult process(Object form) {
		try {
			ResultSuccess resultSuccess = new ResultSuccess();
			CustomerCommand customerCommand = null;
			VbCustomerDetail customerDetail = null;
			CustomerDao customerDao = (CustomerDao) getDao();
			String resultStatusSuccess = OrganizationUtils.RESULT_STATUS_SUCCESS;
			if (form instanceof CustomerCommand) {
				customerCommand = (CustomerCommand) form;
				String action = customerCommand.getAction();
				String userName = getUsername();
				VbOrganization organization = getOrganization();
				if ("save-basic".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					session.setAttribute(CUSTOMER_BASIC, customerCommand);
					
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("save-customer".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					CustomerCommand command = (CustomerCommand) session.getAttribute(CUSTOMER_BASIC);
					customerDao.saveCustomer(command, (VbCustomerDetail) session.getAttribute(CUSTOMER_DETAILS), organization, userName);
					
					// Removing session data.
					removeCustomerDataFromSession(session);
					
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("modify-customer-status".equals(customerCommand.getAction())) {
					 String customerStatusParam=request.getParameter("customerStatusParam");
					 customerDao.modifyCustomerStatus(customerCommand,customerStatusParam,getOrganization());
					 resultSuccess.setStatus(resultStatusSuccess);
					 resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("check-customer-credits".equals(customerCommand.getAction())) {
					 Boolean result=customerDao.checkCustomerCredits(customerCommand,getOrganization());
					 resultSuccess.setData(result);
					 resultSuccess.setStatus(resultStatusSuccess);
					 resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} 
				else if("get-customer-details".equals(action)){
					Integer id =Integer.parseInt(request.getParameter("id"));
					CustomerResult customerResult =customerDao.getCustomerRecord(id,organization);
					
					resultSuccess.setData(customerResult);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("edit-customer".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					customerDao.editCustomer((CustomerCommand) session.getAttribute(CUSTOMER_BASIC),
							(VbCustomerDetail) session.getAttribute(CUSTOMER_DETAILS), organization, userName);
					
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("search-customer".equals(action)) {
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("invoiceName", new SearchFilterData("invoiceName", customerCommand.getInvoiceName(), SearchFilterData.TYPE_STRING_STR));
					List<CustomerResult> list = customerDao.searchCustomer(customerCommand, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("validate-businessName".equals(action)) {
					Boolean isBusinessNameAvailable = customerDao.validateBusinessName(customerCommand, organization);
					
					resultSuccess.setData(isBusinessNameAvailable);
					resultSuccess.setMessage(Msg.get(MsgEnum.BUSINESS_NAME_AVAILABILITY_RESULT_MESSAGE));
					resultSuccess.setStatus(resultStatusSuccess);
				} else if("get-customer-full-name".equals(action)){
					String businessName =request.getParameter("businessName");
					VbCustomer customer = customerDao.getFullname(businessName, organization);
					
					resultSuccess.setData(customer);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultStatusSuccess);
				} else if("get-business-name".equals(action)){
					String businessName=request.getParameter("businessNameVal");
					List<VbCustomer> businessNames = customerDao.getBusinessName(businessName, organization);
					
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setData(businessNames);
				} else if("get-SE-assigned-business-name".equals(action)){
					String businessName=request.getParameter("businessNameVal");
					List<VbCustomer> businessNames = customerDao.getSEAssignedBusinessName(businessName, organization,userName);
					
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setData(businessNames);
				} 
				else if ("get-organization-name".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					String organizationName = customerDao.getOrganizationName(id);
					
					resultSuccess.setData(organizationName);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("get-customer-documents".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					String businessName = customerDao.getBusinessNameById(id);
					List<CustomerDocumetResults> filesList = DirectoryUtil.getInstance().getAllFiles(organization.getName(), businessName);
					
					resultSuccess.setData(filesList);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("delete-customer-document".equals(action)){
					String filePath = request.getParameter("filePath");
					DirectoryUtil.getInstance().deleteFile(filePath);
					
					resultSuccess.setMessage(Msg.get(MsgEnum.DOCUMENT_DELETE_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultStatusSuccess);
				} else if("check-business-name-availability".equals(action)) {
					String businessName = request.getParameter("businessName");
					Boolean isAvailability = customerDao.checkBusinessNameAvailability(businessName, organization);
					
					resultSuccess.setData(isAvailability);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultStatusSuccess);
				}
			} else if (form instanceof VbCustomerDetail) {
				customerDetail = (VbCustomerDetail) form;
				session = request.getSession(Boolean.TRUE);
				session.setAttribute(CUSTOMER_DETAILS, customerDetail);
				
				resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				resultSuccess.setStatus(resultStatusSuccess);
			} 
			
			_logger.info("ResultSuccess: {}", resultSuccess);
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());
			
			if (_logger.isErrorEnabled()) {
				_logger.error("ResultError: {}", resultError);
			}
			return resultError;
		} catch (ProcessingException exception) {
			ResultError resultError = getResultError(exception.getMessage());
			
			if (_logger.isErrorEnabled()) {
				_logger.error("ResultError: {}", resultError);
			}
			return resultError;
		}
	}

	/*
	 * This method is responsible to remove data from session.
	 */
	private void removeCustomerDataFromSession(HttpSession session) {
		session.removeAttribute(CUSTOMER_BASIC);
		session.removeAttribute(CUSTOMER_DETAILS);
	}
}