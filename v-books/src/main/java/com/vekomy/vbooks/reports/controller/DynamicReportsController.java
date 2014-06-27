package com.vekomy.vbooks.reports.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.vekomy.vbooks.mysales.controller.DeliveryNoteController;
import com.vekomy.vbooks.reports.action.ReportsAction;
import com.vekomy.vbooks.reports.command.ReportsCommand;
import com.vekomy.vbooks.reports.result.DynamicReportResult;
import com.vekomy.vbooks.spring.controller.JsonFormController;

public class DynamicReportsController extends JsonFormController {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(DeliveryNoteController.class);

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
	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		super.onSubmit(request, response, command, errors);
		ReportsCommand dynamicReportsCommand = (ReportsCommand) command;
		String image = request.getSession().getServletContext().getRealPath("/");
		List<DynamicReportResult>dynamicReportData = new ArrayList<>();
		ReportsAction reportsAction = (ReportsAction) getAction(request, "reportsAction");
		String reportFormat = dynamicReportsCommand.getReportFormat();
		String transactionType = dynamicReportsCommand.getTransactionType();
		String criteria = dynamicReportsCommand.getCriteria();
		String inputFields = dynamicReportsCommand.getInputs();
		String outPutFields = dynamicReportsCommand.getOutputs();
		dynamicReportData = reportsAction.getDnData(criteria,inputFields,outPutFields,transactionType);
		
		JRDataSource dynamicJRdataSource = new JRBeanCollectionDataSource(dynamicReportData);
		String reportViewName = "DYNAMIC" + "-" + reportFormat;
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("datasource", dynamicJRdataSource);
		parameterMap.put("OptimerImage", image);
		parameterMap.put("MediationImage", image);
		parameterMap.put("requestObject", request);

		ModelAndView modelAndView = new ModelAndView(reportViewName, parameterMap);
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("ModelAndView: {}", modelAndView);
		}
		return modelAndView;
		
	/*super.onSubmit(request, response, command, errors);
		String reportFormat = request.getParameter("reportFmt");
		String image = request.getSession().getServletContext().getRealPath("/");
		ReportsAction reportsAction = (ReportsAction) getAction(request, "reportsAction");
		ReportsCommand customerWiseReportCommand = (ReportsCommand) command;
		if (reportFormat == null) {
			reportFormat = customerWiseReportCommand.getReportFormat();
		}
		List<CustomerWiseReportResult> list = reportsAction.customerWiseReportCommand);
		JRDataSource customerWiseJRdataSource = new JRBeanCollectionDataSource(list);
		String reportViewName = "CUSTOMER_WISE" + "-" + reportFormat;
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("datasource", customerWiseJRdataSource);
		parameterMap.put("OptimerImage", image);
		parameterMap.put("MediationImage", image);
		parameterMap.put("requestObject", request);

		ModelAndView modelAndView = new ModelAndView(reportViewName, parameterMap);
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("ModelAndView: {}", modelAndView);
		}
		return modelAndView;
	*/}

}
