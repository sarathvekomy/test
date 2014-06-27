/**
 * com.vekomy.vbooks.reports.controller.SLECustomerWiseSalesController.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 10, 2014
 */
package com.vekomy.vbooks.reports.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.vekomy.vbooks.reports.action.ReportsAction;
import com.vekomy.vbooks.reports.command.ReportsCommand;
import com.vekomy.vbooks.reports.result.SLECustomerWiseSalesResult;
import com.vekomy.vbooks.spring.controller.JsonFormController;
import com.vekomy.vbooks.util.DateUtils;

/**
 * This controller class is responsible for fetching the data for SLE Customer Wise Sales Report.
 * 
 * @author Ankit
 *
 */
public class SLECustomerWiseSalesController extends JsonFormController {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SLECustomerWiseSalesController.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject
	 * (javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
			return new ReportsCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vekomy.vbooks.spring.controller.JsonFormController#initBinder(javax
	 * .servlet.http.HttpServletRequest,
	 * org.springframework.web.bind.ServletRequestDataBinder)
	 */
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#isFormSubmission(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected boolean isFormSubmission(HttpServletRequest request) {
		return true;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax
	 * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, org.springframework.validation.BindException)
	 */
	@SuppressWarnings("deprecation")
	public ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		super.onSubmit(request, response, command, errors);
		String reportFormat = request.getParameter("reportFmt");
		List<SLECustomerWiseSalesResult> reportSummaryResults = null;
		String image = request.getSession().getServletContext().getRealPath("/");
		ReportsAction reportsAction = (ReportsAction) getAction(request, "reportsAction");
		ReportsCommand sleCustomerWiseSalesReportCommand = (ReportsCommand)command;
		if(reportFormat == null) {
			reportFormat = sleCustomerWiseSalesReportCommand.getReportFormat();
		}
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String reportViewName = null;
		reportSummaryResults = new ArrayList<SLECustomerWiseSalesResult>();
		SLECustomerWiseSalesResult reportSummaryResult = new SLECustomerWiseSalesResult();
		
		String reportType = sleCustomerWiseSalesReportCommand.getReportType();
		String reportTypeSummary = "";
		if(reportType.equalsIgnoreCase("select")){
			reportSummaryResult.setReportTypeSummary(reportTypeSummary);
		}else{
			reportSummaryResult.setReportTypeSummary(reportType);
		}
		    reportSummaryResult.setSalesExecutive(sleCustomerWiseSalesReportCommand.getSalesExecutive());
			reportSummaryResult.setCustomerNameSummary(sleCustomerWiseSalesReportCommand.getBusinessName());
			reportSummaryResult.setRegion(sleCustomerWiseSalesReportCommand.getRegion());
			Date startDate = sleCustomerWiseSalesReportCommand.getStartDate();
			String formatStartDate = DateUtils.format(startDate);
			reportSummaryResult.setStartDate(formatStartDate);
			Date eDate = sleCustomerWiseSalesReportCommand.getEndDate();
			Date endDate = null;
			if(eDate != null) {
				endDate = DateUtils.getEndTimeStamp(eDate);
			}else{
				if(reportType.equalsIgnoreCase("Daily")){
					endDate = startDate;
				}else if(reportType.equalsIgnoreCase("Weekly")){
					endDate = DateUtils.getDateAfterSevenDays(startDate);
				}else if(reportType.equalsIgnoreCase("Monthly")){
					endDate = DateUtils.getAfterThirtyDays(startDate);
				}
			}
			String formatEndDate = DateUtils.format(endDate);
			reportSummaryResult.setEndDate(formatEndDate);
			reportSummaryResults.add(reportSummaryResult);
			
			 JRDataSource sleCustomerWiseSalesJRdataSource = new JRBeanCollectionDataSource(reportSummaryResults);
		   List<SLECustomerWiseSalesResult> sleCustomerWiseSalesSubreportList = reportsAction.getsleCustomerWiseSalesReportData(sleCustomerWiseSalesReportCommand);
		     JRDataSource sleCustomerWiseSalesJRdataSource1 = new JRBeanCollectionDataSource(sleCustomerWiseSalesSubreportList);
		        reportViewName = "SLE_CUSTOMER_WISE_SALES" + "-" + reportFormat;
		        parameterMap.put("JasperSLECustomerWiseSalesSubReportDatasource", sleCustomerWiseSalesJRdataSource1);
		        parameterMap.put("datasource", sleCustomerWiseSalesJRdataSource);
		        parameterMap.put("OptimerImage", image);
				parameterMap.put("MediationImage", image);
				parameterMap.put("requestObject", request);
	
			 //reportViewName bean has been declared in the jasper-views.xml file
			ModelAndView modelAndView = new ModelAndView(reportViewName, parameterMap);
			if(_logger.isDebugEnabled()){
				_logger.debug("ModelAndView: {}", modelAndView);
			}
	        return modelAndView;
	}

}
