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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.reports.command.ReportsCommand;
import com.vekomy.vbooks.reports.dao.ReportsDao;
import com.vekomy.vbooks.reports.result.CustomerWiseReportResult;
import com.vekomy.vbooks.reports.result.DynamicReportResult;
import com.vekomy.vbooks.reports.result.ProductWiseReportResult;
import com.vekomy.vbooks.reports.result.SalesExecutiveExpenditureReportResult;
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
	@SuppressWarnings("unchecked")
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
				@SuppressWarnings("rawtypes")
				ReportsDao reportsDao = (ReportsDao) getDao();
				if ("get-all-business-names".equals(action)) {
					String businessName = request.getParameter("businessNameVal");
					List<String> businessNames = reportsDao.getAllBusinessNames(businessName , organization);
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setData(businessNames);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("get-customer-product-name".equals(action)) {
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
					List<String> salesExecutiveList = reportsDao.getAllSalesExecutives(organization);
					
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setData(salesExecutiveList);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if ("get-all-business-names-based-sales-executive".equals(action)) {
					String salesExecutive  = request.getParameter("salesExecutiveVal");
					String businessName = request.getParameter("businessNameVal");
					List<String> businessNameList = reportsDao.getAllBusinessaNamesBasedSalesExecutive(businessName, salesExecutive, organization);
					
					resultSuccess.setMessage(resuleSuccessMessage);
					resultSuccess.setData(businessNameList);
					resultSuccess.setStatus(resultStatusSuccess);
				} else if("check-business-name-availability".equals(action)) {
					String businessName = request.getParameter("businessName");
					Boolean isAvailability = reportsDao.checkBusinessNameAvailability(businessName, organization);
					
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
					
				}else if("get-dn-data".equals(action)){
					//List<DeliveryNoteReportResult>dnData = (List<DeliveryNoteReportResult>) reportsDao.getDnData(organization);
					//resultSuccess.setData(dnData);
					
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
	@SuppressWarnings("rawtypes")
	public List<ProductWiseReportResult> getProductWiseReportData(ReportsCommand productWiseReportCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(productWiseReportCommand.getOrganization());
		@SuppressWarnings("unchecked")
		List<ProductWiseReportResult> reportResult = reportsDao.getProductWiseReportData(productWiseReportCommand, organization);
		return reportResult;
	}
	
	/**
	 * This method is responsible for getting the data for customer wise report.
	 * 
	 * @param customerWiseReportCommand - {@link ReportsCommand}
	 * @return reportResult - {@link CustomerWiseReportResult}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	@SuppressWarnings("rawtypes")
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
	@SuppressWarnings("rawtypes")
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<SalesReturnQtyReportResult> getSalesReturnReportData(ReportsCommand salesReturnReportsCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(salesReturnReportsCommand.getOrganization());
		List<SalesReturnQtyReportResult> reportResult = reportsDao.getSalesReturnReportsData(salesReturnReportsCommand, organization);
		return reportResult;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<SalesExecutiveExpenditureReportResult> getSalesExecutiveExpenditureReportData(ReportsCommand salesExecutiveExpenditureReportsCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(salesExecutiveExpenditureReportsCommand.getOrganization());
		List<SalesExecutiveExpenditureReportResult> reportResult = reportsDao.getSalesExecutiveExpenditureReportsData(salesExecutiveExpenditureReportsCommand, organization);
		return reportResult;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<SalesExecutiveSalesWiseReportResult> getSalesExecutiveSalesWiseReportData(ReportsCommand salesExecutiveExpenditureReportsCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(salesExecutiveExpenditureReportsCommand.getOrganization());
		List<SalesExecutiveSalesWiseReportResult> reportResult = reportsDao.getSalesExecutiveSalesWiseReportsData(salesExecutiveExpenditureReportsCommand, organization);
		return reportResult;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<VbDeliveryNotePayments> getSalesExecutiveSalesWiseSubReportsData(ReportsCommand salesExecutiveExpenditureReportsCommand) {
		ReportsDao reportsDao = getReportsDao();
		VbOrganization organization = getOrganization(salesExecutiveExpenditureReportsCommand.getOrganization());
		List<VbDeliveryNotePayments> reportResult = reportsDao.getSalesExecutiveSalesWiseSubReportsData(salesExecutiveExpenditureReportsCommand, organization);
		return reportResult;
	}


	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public List<DynamicReportResult> getDnData(String criteria,
			String inputFields, String outPutFields, String transactionType) throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException, IllegalArgumentException, JsonParseException, JsonMappingException, IOException, ParseException {
		ReportsDao reportsDao = getReportsDao();
		List<DynamicReportResult>dynamicData = null;
		VbOrganization organization = getOrganization();
		if(criteria!=null){
			String[] criterias = criteria.split(",");
			String[] inputs = inputFields.split(",");
			Map<String, String>criteriaMap = new TreeMap<>();
			ObjectMapper objectMapper = new ObjectMapper();
			criteriaMap= objectMapper.readValue(criteria, new org.codehaus.jackson.type.TypeReference<HashMap<String,String>>() {});
			if(transactionType.equals("Delivery Note")||transactionType.equals("Cash Collections")){
				dynamicData = reportsDao.getDynamicDnData(criteriaMap,inputFields,outPutFields,organization);
			}else if(transactionType.equals("Sales Return")){
				dynamicData = reportsDao.getDynamicSrData(criteriaMap, inputFields, outPutFields, organization);
			}else if(transactionType.equals("Journal")){
				dynamicData = reportsDao.getDynamicJournalData(criteriaMap,inputFields,outPutFields,organization);
			}else if("select".equals(transactionType)){
				dynamicData = reportsDao.getCommonData(criteriaMap,inputFields,outPutFields,organization);
			}
		}
		
			
		
		return dynamicData;
	}

}
