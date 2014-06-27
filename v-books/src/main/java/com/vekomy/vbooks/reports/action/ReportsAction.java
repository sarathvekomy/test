/**
 * com.vekomy.vbooks.reports.action.ReportsAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jun 11, 2013
 */
package com.vekomy.vbooks.reports.action;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.reports.command.ReportsCommand;
import com.vekomy.vbooks.reports.dao.ReportsDao;
import com.vekomy.vbooks.reports.result.CustomerWiseReportResult;
import com.vekomy.vbooks.reports.result.ProductWiseReportResult;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;

/**
 * This action class is responsible for perfoming the actions related to reports.
 * 
 * @author swarupa
 *
 */
public class ReportsAction extends BaseAction {

	/* (non-Javadoc)
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@Override
	public IResult process(Object form) throws Exception {
		// TODO Auto-generated method stub
		ResultSuccess resultSuccess = new ResultSuccess();
		if(form instanceof ReportsCommand) {
			ReportsCommand reportsCommand = (ReportsCommand) form;
			String action = reportsCommand.getAction();
			ReportsDao reportsDao = (ReportsDao) getDao();
			if ("get-all-business-names".equals(action)) {
				String businessName = request.getParameter("businessNameVal");
				List<String> businessNames = reportsDao.getAllBusinessNames(businessName , getOrganization());
				if (businessNames.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(businessNames);
				}
			} else if ("get-customer-product-name".equals(action)) {
				String businessName = request.getParameter("businessName");
				List<String> productNameList = reportsDao.getCustomerProductName(businessName, getOrganization());
				if (productNameList == null) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(productNameList);
				}
			} else if ("get-all-product-names".equals(action)) {
				VbOrganization organization = getOrganization(request.getParameter("organization"));
				List<String> productNameList = reportsDao.getAllProductNames(organization);
				if (productNameList == null) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(productNameList);
				}
			} else if("get-assigned-organizations".equals(action)) {
				List<String> organizationList = reportsDao.getAllAssignedOrganizations(getUsername());
				if(organizationList == null) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(organizationList);
				}
			} else if ("get-all-sales-executives".equals(action)) {
				List<String> salesExecutiveList = reportsDao.getAllSalesExecutives(getOrganization());
				if (salesExecutiveList == null) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(salesExecutiveList);
				}
			} 
		}
		
		return resultSuccess;
	}
	
	/**
	 * This method is responsible for getting the data for product wise report.
	 * 
	 * @param productWiseReportCommand - {@link ReportsCommand}
	 * @return reportResult - {@link ProductWiseReportResult}
	 */
	public List<ProductWiseReportResult> getProductWiseReportData(ReportsCommand productWiseReportCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(productWiseReportCommand.getOrganization());
		List<ProductWiseReportResult> reportResult = reportsDao.getProductWiseReportData(productWiseReportCommand, organization);
		return reportResult;
	}
	
	/**
	 * This method is responsible for getting the data for customer wise report.
	 * 
	 * @param customerWiseReportCommand - {@link ReportsCommand}
	 * @return reportResult - {@link CustomerWiseReportResult}
	 */
	public List<CustomerWiseReportResult> getCustomerWiseData(ReportsCommand customerWiseReportCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(customerWiseReportCommand.getOrganization());
		List<CustomerWiseReportResult> reportResult = reportsDao.getCustomerWiseData(customerWiseReportCommand, organization);
		return reportResult;
	}

	/**
	 * This method is used to get the {@link ReportsDao} instance.
	 * 
	 * @return reportsDao - {@link ReportsDao}
	 */
	private ReportsDao getReportsDao() {
		ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		ReportsDao reportsDao = (ReportsDao) hibernateContext.getBean("reportsDao");
		return reportsDao;
	}
	
	/**
	 * This method is responsible for getting the {@link VbOrganization} based on organizationName.
	 * 
	 * @param organizationName - {@link VbOrganization}
	 * @return organization - {@link VbOrganization}
	 */
	private VbOrganization getOrganization(String organizationName) {
		VbOrganization organization = null;
		ReportsDao reportsDao = getReportsDao();
		if(organizationName == null) {
			organization = getOrganization();
		} else {
			organization = reportsDao.getOrganization(organizationName);
		}
		return organization;
	}

}
