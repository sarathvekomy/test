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
import com.vekomy.vbooks.customer.command.CustomerResult;
import com.vekomy.vbooks.customer.dao.CustomerDao;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerDetail;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;

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
	 * HttpSession variable holds session.
	 */
	private HttpSession session;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IResult process(Object form) throws Exception {
		CustomerCommand customerCommand = null;
		VbCustomerDetail customerDetail = null;
		ResultSuccess resultSuccess = new ResultSuccess();
		CustomerDao customerDao = (CustomerDao) getDao();
		if (form instanceof CustomerCommand) {
			customerCommand = (CustomerCommand) form;
			String action = customerCommand.getAction();
			if ("save-basic".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				session.setAttribute("customer-basic", customerCommand);
				resultSuccess.setMessage("Saved Successfully");
			} else if ("save-customer".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				customerDao.saveCustomer((CustomerCommand) session.getAttribute("customer-basic"),
						(VbCustomerDetail) session.getAttribute("customer-detail"),
						getOrganization(), getUsername());

				// Removing session data.
				removeCustomerDataFromSession();
				resultSuccess.setMessage("Saved Successfully");
			} else if ("delete-customer".equals(action)) {
				customerDao.deleteCustomer(customerCommand, getOrganization());
				resultSuccess.setMessage("Deleted Successfully");
			} else if ("edit-customer".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				customerDao.editCustomer((CustomerCommand) session.getAttribute("customer-basic"),
						(VbCustomerDetail) session.getAttribute("customer-detail"),
						getOrganization(), getUsername());
				resultSuccess.setMessage("Updated Successfully");
			} else if ("search-customer".equals(action)) {
				HashMap hashMap = new HashMap();
				hashMap.put("invoiceName", new SearchFilterData("invoiceName", customerCommand.getInvoiceName(), SearchFilterData.TYPE_STRING_STR));
				List<CustomerResult> list = customerDao.searchCustomer(customerCommand, getOrganization());
				if(list.isEmpty()){
					resultSuccess.setMessage("No result found");
				} else {
					resultSuccess.setData(list);
					resultSuccess.setMessage("Saved Successfully");
				}
			} else if ("validate-businessName".equals(action)) {
				resultSuccess.setData((String) customerDao.validateBusinessName(customerCommand, getOrganization()));
				resultSuccess.setMessage("BusinessName Validated Successfully.");
			} else if("get-customer-full-name".equals(action)){
				String businessName =request.getParameter("businessName");
				resultSuccess.setData((String) customerDao.getFullname(businessName, getOrganization()));
				resultSuccess.setMessage("FullName Retrieved Successfully");
			} else if("get-business-name".equals(action)){
				String businessName=request.getParameter("businessNameVal");
				List<VbCustomer> businessNames = customerDao.getBusinessName(businessName, getOrganization());
				if (businessNames.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
					resultSuccess.setData(businessNames);
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(businessNames);
				}
			}
		} else if (form instanceof VbCustomerDetail) {
			customerDetail = (VbCustomerDetail) form;
			session = request.getSession(true);
			session.setAttribute("customer-detail", customerDetail);
			resultSuccess.setMessage("Saved Successfully");
		} 
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("resultSuccess: {}", resultSuccess);
		}
		return resultSuccess;
	}

	/*
	 * This method is responsible to remove data from session.
	 */
	private void removeCustomerDataFromSession() {
		session = request.getSession(Boolean.TRUE);
		session.removeAttribute("customer-basic");
		session.removeAttribute("customer-detail");
		session.removeAttribute("customer-credit");
	}

}
