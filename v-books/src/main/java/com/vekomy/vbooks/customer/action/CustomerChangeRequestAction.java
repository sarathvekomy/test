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
import com.vekomy.vbooks.customer.command.CustomerChangeRequestCommand;
import com.vekomy.vbooks.customer.command.CustomerResult;
import com.vekomy.vbooks.customer.dao.CustomerChangeRequestDao;
import com.vekomy.vbooks.hibernate.model.VbCustomerChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbCustomerDetail;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.Msg.MsgEnum;

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
	 * HttpSession variable holds session.
	 */
	private HttpSession session;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public IResult process(Object form) throws Exception {
		CustomerChangeRequestCommand customerCrCommand = null;
		ResultSuccess resultSuccess = new ResultSuccess();
		CustomerChangeRequestDao customerDao = (CustomerChangeRequestDao) getDao();
		if (form instanceof CustomerChangeRequestCommand) {
			String userName = getUsername();
			VbOrganization organization = getOrganization();
			customerCrCommand = (CustomerChangeRequestCommand) form;
			String action = customerCrCommand.getAction();
			if ("save-basiccr".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				session.setAttribute("customerCr-basic", customerCrCommand);
				if (customerCrCommand.getCrType() == Boolean.TRUE) {
					VbCustomerDetail customerDetail2 = customerDao.getCustomerDetails(customerCrCommand);
					if (customerDetail2 == null) {
						resultSuccess.setMessage("No records found");
					} else {
						resultSuccess.setData(customerDetail2);
						resultSuccess.setMessage("Record retrived successfully");
					}
				}
			} else if ("save-crdetail".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				session.setAttribute("customerCr-detail", customerCrCommand);
			} else if ("save-customer-cr".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				CustomerChangeRequestCommand customerChangeRequestCommand = (CustomerChangeRequestCommand) session.getAttribute("customerCr-basic");
				customerDao.saveCustomerCrDetails(customerChangeRequestCommand,
						(CustomerChangeRequestCommand) session.getAttribute("customerCr-detail"),
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
				resultSuccess.setMessage("Saved Successfully");
			} else if ("search-cr-onload".equals(action)) {
				List<CustomerResult> list = customerDao.getCrDetails(customerCrCommand, organization);
				if (list.isEmpty()) {
					resultSuccess.setMessage("Records not found");
				} else {
					resultSuccess.setData(list);
					resultSuccess.setMessage("Search Successfull");
				}
			} else if ("search-customer-cr".equals(action)) {
				List list = customerDao.searchCustomerCr(customerCrCommand, organization);
				if (list.isEmpty()) {
					resultSuccess.setMessage("Records not found");
				} else {
					resultSuccess.setData(list);
					resultSuccess.setMessage("Search Successfull");
				}
			} else if ("approve-customer-cr".equals(action)) {
				Integer id = Integer.parseInt(request.getParameter("id"));
				String status = request.getParameter("status");
				customerDao.approveOrDeclineCustomerCr(id, status, organization, userName, customerCrCommand);
				
				//For Alerts.
				VbCustomerChangeRequest customerChangeRequest = customerDao.getVbCustomerChangeRequestById(id);
				String alertMysalesPage = null;
				if(Boolean.TRUE == customerChangeRequest.getCrType()) {
					alertMysalesPage = OrganizationUtils.ALERT_MYSALES_PAGE_EXISTING_CUSTOMER_CR;
				} else {
					alertMysalesPage = OrganizationUtils.ALERT_MYSALES_PAGE_NEW_CUSTOMER_CR;
				}
				String defaultToRecipient = customerChangeRequest.getCreatedBy();
				AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, 
						OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_APPROVALS,
						alertMysalesPage, Msg.get(MsgEnum.ALERT_TYPE_CUSTOMER_CHANGE_REQUEST_APPROVED_MESSAGE));
				 _logger.info("Firing an alert for Customer Change Request");
				
				resultSuccess.setMessage("Approved Suuccessfully");
			}
			else if ("check-customer-cr-type".equals(action)) {
				VbCustomerChangeRequest vbCustomerChangeRequest=customerDao.checkCustomerCr(organization, customerCrCommand);
				if (vbCustomerChangeRequest==null) {
					resultSuccess.setData(0);
				} else {
					resultSuccess.setData(vbCustomerChangeRequest);
					resultSuccess.setMessage("Search Successfull");
				}
			}
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("resultSuccess: {}", resultSuccess);
		}
		return resultSuccess;
	}

	/*
	 * This method is responsible to remove data from session.
	 */
	private void removeCustomerCrDataFromSession(HttpSession session) {
		session.removeAttribute("customerCr-basic");
		session.removeAttribute("customerCr-detail");
	}
}
