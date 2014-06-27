/**
 * com.vekomy.vbooks.customer.controller.CustomerController.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 15, 2013
 */
package com.vekomy.vbooks.customer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.vekomy.vbooks.customer.command.CustomerCommand;
import com.vekomy.vbooks.hibernate.model.VbCustomerDetail;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.controller.JsonFormController;

/**
 * This controller is responsible to handel the customer module requests
 * 
 * @author priyanka
 * 
 * 
 */
public class CustomerController extends JsonFormController {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(CustomerController.class);

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
		if ("save-detail".equals(action)) {
			return new VbCustomerDetail();
		} else if ("edit-customer".equals(action)) {
			return new CustomerCommand();
		} else if("delete-customer".equals(action)){
			return new CustomerCommand();
		} else if("validate-businessName".equals(action)){
			return new CustomerCommand();
		}else if("save-basic".equals(action)){
			return new CustomerCommand();
		}
		else if("save-customer".equals(action)){
			return new CustomerCommand();
		}else if("get-customer-full-name".equals(action)){
			return new CustomerCommand();
		}else if("get-business-name".equals(action)){
			return new CustomerCommand();
		}
		else {
			return new CustomerCommand();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#
	 * processFormSubmission(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object,
	 * org.springframework.validation.BindException)
	 */
	@SuppressWarnings("deprecation")
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		super.onSubmit(request, response, command, errors);
		IResult result = getAction(request, "customerAction").process(command);
		ModelAndView modelAndView = new ModelAndView("jsonView");
		modelAndView.addObject("result", result);
		
		if(_logger.isDebugEnabled()){
			_logger.debug("modelAndView: {}", modelAndView);
		}
		return modelAndView;
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
		IResult result = getAction(request, "customerAction").process(command);
		ModelAndView modelAndView = new ModelAndView("jsonView");
		modelAndView.addObject("result", result);
		
		if(_logger.isDebugEnabled()){
			_logger.debug("modelAndView: {}", modelAndView);
		}
		return modelAndView;
	}

}
