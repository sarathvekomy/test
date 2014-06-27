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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.reports.command.ReportsCommand;
import com.vekomy.vbooks.reports.dao.ReportsDao;
import com.vekomy.vbooks.reports.result.CustomerProductSalesReportResult;
import com.vekomy.vbooks.reports.result.CustomerWiseReportResult;
import com.vekomy.vbooks.reports.result.FactoryProductWiseReportResult;
import com.vekomy.vbooks.reports.result.ProductReportResult;
import com.vekomy.vbooks.reports.result.ProductWiseReportResult;
import com.vekomy.vbooks.reports.result.ProductWiseSalesReportResult;
import com.vekomy.vbooks.reports.result.SLECustomerWiseSalesResult;
import com.vekomy.vbooks.reports.result.SLEProductReportResult;
import com.vekomy.vbooks.reports.result.SalesExecutiveExpenditureReportResult;
import com.vekomy.vbooks.reports.result.SalesExecutiveSalesReportResult;
import com.vekomy.vbooks.reports.result.SalesExecutiveSalesWiseReportResult;
import com.vekomy.vbooks.reports.result.SalesReturnQtyReportResult;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * This action class is responsible for perfoming the actions related to reports.
 * 
 * @author swarupa
 *
 */
public class ReportsAction extends BaseAction {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ReportsAction.class);

	/* (non-Javadoc)
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@Override
	public IResult process(Object form)  {
		try {
			ResultSuccess resultSuccess = new ResultSuccess();
			String resultStatusSuccess = OrganizationUtils.RESULT_STATUS_SUCCESS;
			String resuleSuccessMessage = Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE);
			if(form instanceof ReportsCommand) {
				VbOrganization organization = getOrganization();
				ReportsCommand reportsCommand = (ReportsCommand) form;
				String action = reportsCommand.getAction();
				ReportsDao reportsDao = (ReportsDao) getDao();
				if ("get-all-business-names".equals(action)) {
					String businessName = request.getParameter("businessNameVal");
					List<String> businessNames = reportsDao.getAllBusinessNames(businessName , organization);
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setData(businessNames);
					resultSuccess.setStatus(resultStatusSuccess);
				} 
				else if ("get-customer-region-name".equals(action)) {
					String customerName = request.getParameter("customerName");
					String regionName = reportsDao.getCustomerRegionName(customerName, organization);
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setData(regionName);
					resultSuccess.setStatus(resultStatusSuccess);
				} 
				else if ("get-customer-product-name".equals(action)) {
					String businessName = request.getParameter("businessName");
					List<String> productNameList = reportsDao.getCustomerProductName(businessName, organization);
					
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setData(productNameList);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("get-all-product-names".equals(action)) {
					VbOrganization vbOrganization = getOrganization(request.getParameter("organization"));
					List<String> productNameList = reportsDao.getAllProductNames(vbOrganization);
					
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setData(productNameList);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if("get-assigned-organizations".equals(action)) {
					List<String> organizationList = reportsDao.getAllAssignedOrganizations(getUsername());
					
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setData(organizationList);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if("get-all-organizations".equals(action)) {
					List<String> organizationList = reportsDao.getAllOrganizations();
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setData(organizationList);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("get-all-sales-executives".equals(action)) {
					String businessName = request.getParameter("businessNameVal");
					List<String> salesExecutiveList = reportsDao.getAllSalesExecutives(businessName,organization);
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setData(salesExecutiveList);
					resultSuccess.setStatus(resultStatusSuccess);
				}
				// action added for SLE Customer Wise Sales new report
				else if ("get-all-sales-executives-basedon-customername".equals(action)) {
					String customerName = request.getParameter("customerNameVal");
					List<String> salesExecutiveList = reportsDao.getAllSalesExecutivesBasedOnCustomerName(customerName,organization);
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setData(salesExecutiveList);
					resultSuccess.setStatus(resultStatusSuccess);
				}
				else if ("get-all-customer-names-based-sales-executive".equals(action)) {
					String salesExecutive  = request.getParameter("salesExecutiveVal");
					String businessName = request.getParameter("businessNameVal");
					List<String> customerNameList = reportsDao.getAllCustomerNamesBasedSalesExecutive(businessName, salesExecutive, organization);
					
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setData(customerNameList);
					resultSuccess.setStatus(resultStatusSuccess);
				} 
				else if ("get-all-regions-based-customer-salesexecutive-name".equals(action)) {
					String businessNameVal  = request.getParameter("businessNameVal");
					String salesExecutiveVal = request.getParameter("salesExecutiveVal");
					String regionNameVal = request.getParameter("regionNameVal");
					List<String> customerNameList = reportsDao.getAllRegionNamesBasedOnCustomerNameOrSalesExecutive(businessNameVal, regionNameVal,salesExecutiveVal, organization);
					
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setData(customerNameList);
					resultSuccess.setStatus(resultStatusSuccess);
				} 
				else if ("get-all-business-names-based-sales-executive".equals(action)) {
					String salesExecutive  = request.getParameter("salesExecutiveVal");
					String businessName = request.getParameter("businessNameVal");
					List<String> businessNameList = reportsDao.getAllBusinessaNamesBasedSalesExecutive(businessName, salesExecutive, organization);
					
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setData(businessNameList);
					resultSuccess.setStatus(resultStatusSuccess);
				} 
				else if("check-business-name-availability".equals(action)) {
					String businessName = request.getParameter("businessName");
					Boolean isAvailability = reportsDao.checkBusinessNameAvailability(businessName, organization);
					resultSuccess.setData(isAvailability);
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				}
				// action added for new report - SLE Customer Wise Sales Report
					else if("check-customer-name-availability".equals(action)) {
						String customerName = request.getParameter("customerName");
						Boolean isAvailability = reportsDao.checkCustomerNameAvailability(customerName, organization);
						resultSuccess.setData(isAvailability);
						resultSuccess.setMessage(resuleSuccessMessage);
						resultSuccess.setStatus(resultStatusSuccess);
				} else if("check-business-name-availability-based-on-sales-executive".equals(action)) {
					String businessName = request.getParameter("businessName");
					String salesExecutive = request.getParameter("salesExecutive");
					Boolean isAvailability = reportsDao.checkBusinessNameAvailabilityBasedOnSalesExecutive(salesExecutive, businessName, organization);
					
					resultSuccess.setData(isAvailability);
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if("check-sales-executive-availability".equals(action)) {
					String salesExecutive = request.getParameter("salesExecutive");
					Boolean isAvailability = reportsDao.checkSalesExecutiveAvailability(salesExecutive, organization);
					
					resultSuccess.setData(isAvailability);
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if("get-batch-number-associated-product".equals(action)) {
					String productName = request.getParameter("productNameVal");
					String businessName = request.getParameter("businessNameVal");
					List<String> batchNumberList = reportsDao.getBatchNumberAssociatedWithProduct(productName, businessName, organization);
					
					resultSuccess.setData(batchNumberList);
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setStatus(resultStatusSuccess);
					
				}
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
			
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());
			
			if(_logger.isWarnEnabled()) {
				_logger.warn("ResultError: {}", resultError);
			}
			return resultError;
		}
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
	
	
	// add methods for New Reports
	/**
	 * This method is responsible for getting the data for factory product wise report.
	 * 
	 * @param factoryProductWiseReportCommand - {@link ReportsCommand}
	 * @return reportResult - {@link FactoryProductWiseReportResult}
	 */
	public List<FactoryProductWiseReportResult> getFactoryProductWiseReportData(ReportsCommand factoryProductWiseReportCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(factoryProductWiseReportCommand.getOrganization());
		List<FactoryProductWiseReportResult> reportResult = reportsDao.getFactoryProductWiseReportData(factoryProductWiseReportCommand, organization);
		return reportResult;
	}
	
	/**
	 * This method is responsible for getting the data for factory product wise report (Product Opening Stock and Closing Stock).
	 * 
	 * @param factoryProductWiseReportCommand - {@link ReportsCommand}
	 * @return reportResult - {@link FactoryProductWiseReportResult}
	 */
	public List<FactoryProductWiseReportResult> getFactoryProductWiseStockReportData(ReportsCommand factoryProductWiseReportCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(factoryProductWiseReportCommand.getOrganization());
		List<FactoryProductWiseReportResult> reportResult = reportsDao.getFactoryProductWiseStockReportData(factoryProductWiseReportCommand, organization);
		return reportResult;
	}
	
	/**
	 * This method is responsible for getting the data for  product wise sales report .
	 * 
	 * @param productWiseSalesReportCommand - {@link ReportsCommand}
	 * @return reportResult - {@link productWiseSalesReportCommand}
	 */
	public List<ProductWiseSalesReportResult> getProductWiseSalesReportData(ReportsCommand productWiseSalesReportCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(productWiseSalesReportCommand.getOrganization());
		List<ProductWiseSalesReportResult> reportResult = reportsDao.getProductWiseSalesReportData(productWiseSalesReportCommand, organization);
		return reportResult;
	}
	/**
	 * This method is responsible for getting the data for  product wise sales sub report for summary section from vbDeliveryNotePayments .
	 * 
	 * @param productWiseSalesReportCommand - {@link ReportsCommand}
	 * @return reportResult - {@link productWiseSalesReportCommand}
	 */
	public List<VbDeliveryNotePayments> getProductWiseSalesWiseSubReportsData(ReportsCommand productWiseSalesReportCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(productWiseSalesReportCommand.getOrganization());
		List<VbDeliveryNotePayments> reportResult = reportsDao.getProductWiseSalesSubReportData(productWiseSalesReportCommand, organization);
		return reportResult;
	}

	/**
	 * This method is responsible for getting the data for SLE Customer wise sales report .
	 * 
	 * @param sleCustomerWiseSalesReportCommand - {@link ReportsCommand}
	 * @return reportResult - {@link productWiseSalesReportCommand}
	 */
	public List<SLECustomerWiseSalesResult> getsleCustomerWiseSalesReportData(ReportsCommand sleCustomerWiseSalesReportCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(sleCustomerWiseSalesReportCommand.getOrganization());
		List<SLECustomerWiseSalesResult> reportResult = reportsDao.getSLECustomerWiseSalesReportData(sleCustomerWiseSalesReportCommand, organization);
		return reportResult;
	}
	
	/**
	 * This method is responsible for getting the data for  SLE Sales report .
	 * 
	 * @param salesExecutiveSalesReportCommand - {@link ReportsCommand}
	 * @return reportResult - {@link productWiseSalesReportCommand}
	 */
	public List<SalesExecutiveSalesReportResult> getSalesExecutiveSalesReportData(ReportsCommand salesExecutiveSalesReportCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(salesExecutiveSalesReportCommand.getOrganization());
		List<SalesExecutiveSalesReportResult> reportResult = reportsDao.getSalesExecutiveSalesWiseReportData(salesExecutiveSalesReportCommand, organization);
		return reportResult;
	}
	
	/**
	 * This method is responsible for getting the data for new Product Report.
	 * 
	 * @param productReportWiseReportCommand - {@link ReportsCommand}
	 * @return reportResult - {@link productWiseSalesReportCommand}
	 * @throws DataAccessException 
	 */
	public List<ProductReportResult> getProductReportData(ReportsCommand productReportWiseReportCommand) throws DataAccessException {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(productReportWiseReportCommand.getOrganization());
		List<ProductReportResult> reportResult = reportsDao.getProductReportWiseReportData(productReportWiseReportCommand, organization);
		return reportResult;
	}
	
	/**
	 * This method is responsible for getting the data for Customer Product Sales Report.
	 * 
	 * @param customerProductSalesWiseReportCommand - {@link ReportsCommand}
	 * @return reportResult - {@link productWiseSalesReportCommand}
	 * @throws DataAccessException 
	 */
	public List<CustomerProductSalesReportResult> getCustomerProductSalesReportData(ReportsCommand customerProductSalesWiseReportCommand) throws DataAccessException {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(customerProductSalesWiseReportCommand.getOrganization());
		List<CustomerProductSalesReportResult> reportResult = reportsDao.getCustomerProductSalesReportData(customerProductSalesWiseReportCommand, organization);
		return reportResult;
	}
	
	/**
	 * This method is responsible for getting the data for Product Report Sales Executive.
	 * 
	 * @param sleProductReportSalesWiseReportCommand - {@link ReportsCommand}
	 * @return reportResult - {@link productWiseSalesReportCommand}
	 * @throws DataAccessException 
	 */
	public List<SLEProductReportResult> getSLEProductReportSalesReportData(ReportsCommand sleProductReportSalesWiseReportCommand) throws DataAccessException {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(sleProductReportSalesWiseReportCommand.getOrganization());
		List<SLEProductReportResult> reportResult = reportsDao.getSLEProductReportSalesReportData(sleProductReportSalesWiseReportCommand, organization);
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

	public List<SalesReturnQtyReportResult> getSalesReturnReportData(ReportsCommand salesReturnReportsCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(salesReturnReportsCommand.getOrganization());
		List<SalesReturnQtyReportResult> reportResult = reportsDao.getSalesReturnReportsData(salesReturnReportsCommand, organization);
		return reportResult;
	}

	public List<SalesExecutiveExpenditureReportResult> getSalesExecutiveExpenditureReportData(ReportsCommand salesExecutiveExpenditureReportsCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(salesExecutiveExpenditureReportsCommand.getOrganization());
		List<SalesExecutiveExpenditureReportResult> reportResult = reportsDao.getSalesExecutiveExpenditureReportsData(salesExecutiveExpenditureReportsCommand, organization);
		return reportResult;
	}

	public List<SalesExecutiveSalesWiseReportResult> getSalesExecutiveSalesWiseReportData(ReportsCommand salesExecutiveExpenditureReportsCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(salesExecutiveExpenditureReportsCommand.getOrganization());
		List<SalesExecutiveSalesWiseReportResult> reportResult = reportsDao.getSalesExecutiveSalesWiseReportsData(salesExecutiveExpenditureReportsCommand, organization);
		return reportResult;
	}
	
	public List<VbDeliveryNotePayments> getSalesExecutiveSalesWiseSubReportsData(ReportsCommand salesExecutiveExpenditureReportsCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(salesExecutiveExpenditureReportsCommand.getOrganization());
		List<VbDeliveryNotePayments> reportResult = reportsDao.getSalesExecutiveSalesWiseSubReportsData(salesExecutiveExpenditureReportsCommand, organization);
		return reportResult;
	}

}
