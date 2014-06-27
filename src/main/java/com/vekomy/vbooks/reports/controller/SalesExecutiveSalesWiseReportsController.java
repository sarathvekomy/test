/**
 * com.vekomy.vbooks.reports.controller.SalesExecutiveSalesWiseReportsController.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Dec 20, 2013
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

import com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments;
import com.vekomy.vbooks.reports.action.ReportsAction;
import com.vekomy.vbooks.reports.command.ReportsCommand;
import com.vekomy.vbooks.reports.result.SalesExecutiveSalesWiseReportResult;
import com.vekomy.vbooks.spring.controller.JsonFormController;
import com.vekomy.vbooks.util.DateUtils;

/**
 * @author Swarupa
 *
 */
public class SalesExecutiveSalesWiseReportsController extends JsonFormController {


	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SalesExecutiveSalesWiseReportsController.class);

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
		List<SalesExecutiveSalesWiseReportResult> reportSummaryResults = null;
		String image = request.getSession().getServletContext().getRealPath("/");
		ReportsAction reportsAction = (ReportsAction) getAction(request, "reportsAction");
		ReportsCommand salesExecutiveReportsCommand = (ReportsCommand)command;
		if(reportFormat == null) {
			reportFormat = salesExecutiveReportsCommand.getReportFormat();
		}
		reportSummaryResults = new ArrayList<SalesExecutiveSalesWiseReportResult>();
		SalesExecutiveSalesWiseReportResult reportSummaryResult = new SalesExecutiveSalesWiseReportResult();
		reportSummaryResult.setSalesExecutive(salesExecutiveReportsCommand.getSalesExecutive());
		reportSummaryResult.setBusinessNameSummary(salesExecutiveReportsCommand.getBusinessName());
		
		String reportType = salesExecutiveReportsCommand.getReportType();
		String reportTypeSummary = "";
		if(reportType.equalsIgnoreCase("select")){
			reportSummaryResult.setReportType(reportTypeSummary);
		}else{
			reportSummaryResult.setReportType(reportType);
		}
		Date startDate = salesExecutiveReportsCommand.getStartDate();
		String formatStartDate = DateUtils.format(startDate);
		reportSummaryResult.setStartDate(formatStartDate);
		Date eDate = salesExecutiveReportsCommand.getEndDate();
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
		
		JRDataSource salesExecutiveSalesWiseReportHeaderSumary = new JRBeanCollectionDataSource(reportSummaryResults);
		
		 List<SalesExecutiveSalesWiseReportResult> list = reportsAction.getSalesExecutiveSalesWiseReportData(salesExecutiveReportsCommand);
		 List<VbDeliveryNotePayments> subReportList = reportsAction.getSalesExecutiveSalesWiseSubReportsData(salesExecutiveReportsCommand);
	        JRDataSource salesExecutiveWiseJRdataSource = new JRBeanCollectionDataSource(list);
	        JRDataSource salesExecutiveWiseSubJRdataSource = new JRBeanCollectionDataSource(subReportList);
	        
	        String reportViewName = "SALES_EXECUTIVE_SALES_WISE" + "-" + reportFormat;
	        Map<String, Object> parameterMap = new HashMap<String, Object>();
	        parameterMap.put("JasperMasterReportDatasource1", salesExecutiveWiseJRdataSource);
	        parameterMap.put("JasperSubReportDatasource1", salesExecutiveWiseSubJRdataSource);
	        parameterMap.put("datasource", salesExecutiveSalesWiseReportHeaderSumary);
	        parameterMap.put("OptimerImage", image);
			parameterMap.put("MediationImage", image);
			parameterMap.put("requestObject", request);
			
			ModelAndView modelAndView = new ModelAndView(reportViewName, parameterMap);
			if(_logger.isDebugEnabled()){
				_logger.debug("ModelAndView: {}", modelAndView);
			}
	        return modelAndView;
	}




}
