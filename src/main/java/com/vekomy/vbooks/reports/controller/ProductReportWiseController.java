/**
 * com.vekomy.vbooks.reports.controller.ProductReportWiseController.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 16, 2014
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
import com.vekomy.vbooks.reports.result.ProductReportResult;
import com.vekomy.vbooks.spring.controller.JsonFormController;
import com.vekomy.vbooks.util.DateUtils;

/**
 * This controller class is responsible for fetching the data for Product Report.
 * 
 * @author Ankit
 *
 */
public class ProductReportWiseController extends JsonFormController {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ProductReportWiseController.class);

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
		List<ProductReportResult> reportSummaryResults = null;
		String image = request.getSession().getServletContext().getRealPath("/");
		ReportsAction reportsAction = (ReportsAction) getAction(request, "reportsAction");
		ReportsCommand productReportWiseReportCommand = (ReportsCommand)command;
		if(reportFormat == null) {
			reportFormat = productReportWiseReportCommand.getReportFormat();
		}
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String reportViewName = null;
		reportSummaryResults = new ArrayList<ProductReportResult>();
		ProductReportResult reportSummaryResult = new ProductReportResult();
		String reportType = productReportWiseReportCommand.getReportType();
		String reportTypeSummary = "";
		if(reportType.equalsIgnoreCase("select")){
			reportSummaryResult.setReportTypeSummary(reportTypeSummary);
		}else{
			reportSummaryResult.setReportTypeSummary(reportType);
		}
		    Date startDate = productReportWiseReportCommand.getStartDate();
		    String formatStartDate = DateUtils.format(startDate);
			reportSummaryResult.setStartDate(formatStartDate);
			Date eDate = productReportWiseReportCommand.getEndDate();
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
			
			 JRDataSource factoryProductWiseJRdataSource = new JRBeanCollectionDataSource(reportSummaryResults);
		     
			List<ProductReportResult> productReportWiseSubreportList = reportsAction.getProductReportData(productReportWiseReportCommand);
		    JRDataSource productReportWiseSubreportJRdataSource = new JRBeanCollectionDataSource(productReportWiseSubreportList);
		        reportViewName = "PRODUCT_REPORT" + "-" + reportFormat;
		       parameterMap.put("JasperProductReportWiseWiseSubReportDatasource", productReportWiseSubreportJRdataSource);
		        parameterMap.put("datasource", factoryProductWiseJRdataSource);
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
