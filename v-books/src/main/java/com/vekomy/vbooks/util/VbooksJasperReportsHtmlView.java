/**
 * com.vekomy.web.util.OptimerJasperReportsHtmlView.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Created on: May 28, 2013
 */
package com.vekomy.vbooks.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;
import org.springframework.web.servlet.view.jasperreports.JasperReportsHtmlView;

/**
 * @author swarupa
 * 
 */
public class VbooksJasperReportsHtmlView extends JasperReportsHtmlView {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.view.jasperreports.
	 * AbstractJasperReportsSingleFormatView
	 * #renderReport(net.sf.jasperreports.engine.JasperPrint, java.util.Map,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void renderReport(JasperPrint populatedReport, Map model,
			HttpServletResponse response) throws Exception {
		if (model.containsKey("requestObject")) {
			HttpServletRequest request = (HttpServletRequest) model.get("requestObject");
			request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, populatedReport);
		}

		super.renderReport(populatedReport, model, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.view.jasperreports.
	 * AbstractJasperReportsSingleFormatView#createExporter()
	 */
	@Override
	protected JRExporter createExporter() {
		return super.createExporter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.view.jasperreports.
	 * AbstractJasperReportsSingleFormatView#useWriter()
	 */
	@Override
	protected boolean useWriter() {
		return super.useWriter();
	}

}
