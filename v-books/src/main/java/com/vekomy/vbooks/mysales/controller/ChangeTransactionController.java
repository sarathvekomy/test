/**
 * com.vekomy.vbooks.mysales.controller.ChangeTransactionController.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: June 18, 2013
 */
package com.vekomy.vbooks.mysales.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookAllowancesCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookAmountCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookBasicInfoCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookProductsCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookVehicleDetailsCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDeliveryNoteCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDeliveryNoteProductCommand;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.controller.JsonFormController;

/**
 * This controller is responsible to handle the Transaction Change Request.
 * 
 * @author ankit
 *
 */
public class ChangeTransactionController extends JsonFormController {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ChangeTransactionController.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject
	 * (javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String action = request.getParameter("action");
		if ("save-product-info".equals(action)) {
			return new ChangeRequestDeliveryNoteProductCommand();
		} 
		else if("daybook-basic-info".equals(action)){
			return new ChangeRequestDayBookBasicInfoCommand();
		}
		else if("daybook-allowances".equals(action)){
			return new ChangeRequestDayBookAllowancesCommand();
		}else if("daybook-amount".equals(action)) {
			return new ChangeRequestDayBookAmountCommand();
		} else if("daybook-products".equals(action)) {
			return new ChangeRequestDayBookProductsCommand();
		}
		else if("vehicle-details".equals(action)) {
			return new ChangeRequestDayBookVehicleDetailsCommand();
		}
		else {
			return new ChangeRequestDeliveryNoteCommand();
		}
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
		IResult result = getAction(request, "changeTransactionAction").process(command);
		ModelAndView modelAndView = new ModelAndView("jsonView");
		modelAndView.addObject("result", result);
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("ModelAndView: {}", modelAndView);
		}
		return modelAndView;
	}
}
