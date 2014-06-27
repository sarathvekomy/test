/**
 * com.vekomy.vbooks.mysales.controller.SalesReturnController.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 19, 2013
 */
package com.vekomy.vbooks.mysales.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.vekomy.vbooks.mysales.command.SalesReturnCommand;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.controller.JsonFormController;
/**
 * This controller is responsible to process the sales returns under sales module.
 * 
 * @author Sudhakar
 * 
 */
public class SalesReturnController extends JsonFormController {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SalesReturnController.class);
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject
	 * (javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) {
		return new SalesReturnCommand();
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
			ServletRequestDataBinder dataBinder) throws Exception {
		super.initBinder(request, dataBinder);
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
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		super.onSubmit(request, response, command, errors);
		IResult result = getAction(request, "salesReturnAction").process(command);
		ModelAndView modelAndView = new ModelAndView("jsonView");
		modelAndView.addObject("result", result);
		
		if(_logger.isDebugEnabled()){
			_logger.debug("ModelAndView: {}", modelAndView);
		}
		return modelAndView;
	}
}
