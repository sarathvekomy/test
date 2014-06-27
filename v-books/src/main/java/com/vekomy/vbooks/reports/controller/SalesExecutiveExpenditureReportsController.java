/**
 * com.vekomy.vbooks.reports.controller.SalesExecutiveExpenditureReportsController.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Dec 20, 2013
 */
package com.vekomy.vbooks.reports.controller;

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
import com.vekomy.vbooks.reports.result.SalesExecutiveExpenditureReportResult;
import com.vekomy.vbooks.spring.controller.JsonFormController;

/**
 * @author Swarupa
 *
 */
public class SalesExecutiveExpenditureReportsController extends JsonFormController {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SalesExecutiveExpenditureReportsController.class);

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
		String image = request.getSession().getServletContext().getRealPath("/");
		ReportsAction reportsAction = (ReportsAction) getAction(request, "reportsAction");
		ReportsCommand salesExecutiveExpenditureReportsCommand = (ReportsCommand)command;
		if(reportFormat == null) {
			reportFormat = salesExecutiveExpenditureReportsCommand.getReportFormat();
		}
		 List<SalesExecutiveExpenditureReportResult> list = reportsAction.getSalesExecutiveExpenditureReportData(salesExecutiveExpenditureReportsCommand);
	        JRDataSource salesExecutiveExpenditureWiseJRdataSource = new JRBeanCollectionDataSource(list);
	        String reportViewName = "SALES_EXECUTIVE_EXPENDITURE_WISE" + "-" + reportFormat;
	        Map<String, Object> parameterMap = new HashMap<String, Object>();
	        parameterMap.put("datasource", salesExecutiveExpenditureWiseJRdataSource);
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
