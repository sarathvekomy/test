/**
 * com.vekomy.vbooks.reports.controller.SalesWiseReportsController.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jun 12, 2013
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
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import com.vekomy.vbooks.reports.command.ReportsCommand;
import com.vekomy.vbooks.reports.dao.ReportsDao;
import com.vekomy.vbooks.reports.result.DeliveryNoteCollectionReportResult;
import com.vekomy.vbooks.reports.result.DeliveryNoteReportResult;
import com.vekomy.vbooks.reports.result.JournalReportResult;
import com.vekomy.vbooks.reports.result.SalesReturnReportResult;
import com.vekomy.vbooks.reports.result.SalesWiseReportResult;
import com.vekomy.vbooks.spring.controller.JsonFormController;

/**
 * @author swarupa
 *
 */
public class SalesWiseReportsController extends JsonFormController {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SalesWiseReportsController.class);

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
		ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		ReportsDao reportsDao = (ReportsDao) hibernateContext.getBean("reportsDao");
		ReportsCommand salesWiseReportCommand = (ReportsCommand)command;
		if(reportFormat == null) {
			reportFormat = salesWiseReportCommand.getReportFormat();
		}
		 List<SalesWiseReportResult> list = reportsDao.getSalesWiseReportData(salesWiseReportCommand);
		 List<DeliveryNoteReportResult> firstSubReportList = reportsDao.getFirstSubReportData(salesWiseReportCommand);
		 List<SalesReturnReportResult> secondSubReportList = reportsDao.getSecondSubReportData(salesWiseReportCommand);
		 List<DeliveryNoteCollectionReportResult> thirdSubReportList = reportsDao.getThirdSubReportData(salesWiseReportCommand);
		 List<JournalReportResult> fourthSubReportList = reportsDao.getFourthSubReportData(salesWiseReportCommand);
		 
		 
		 JRDataSource salesWiseJRdataSource = new JRBeanCollectionDataSource(list);
	     JRDataSource firstSubReportDatasource = new JRBeanCollectionDataSource(firstSubReportList);
	     JRDataSource secondSubReportDatasource = new JRBeanCollectionDataSource(secondSubReportList);
	     JRDataSource thirdSubReportDatasource = new JRBeanCollectionDataSource(thirdSubReportList);
	     JRDataSource fourthSubReportDatasource = new JRBeanCollectionDataSource(fourthSubReportList);
	        String reportViewName = "SALES_WISE" + "-" + reportFormat;
	        Map<String, Object> parameterMap = new HashMap<String, Object>();
	        parameterMap.put("JasperSubReportDatasource1", firstSubReportDatasource);
	        parameterMap.put("JasperSubReportDatasource2", secondSubReportDatasource);
	        parameterMap.put("JasperSubReportDatasource3", thirdSubReportDatasource);
	        parameterMap.put("JasperSubReportDatasource4", fourthSubReportDatasource);
	        parameterMap.put("datasource", salesWiseJRdataSource);
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
