package com.vekomy.vbooks.accounts.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.vekomy.vbooks.accounts.command.AllotStockCommand;
import com.vekomy.vbooks.accounts.command.SalesCommand;
import com.vekomy.vbooks.accounts.command.SalesReturnCommand;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.controller.JsonFormController;

/**
 * This controller is responsible to handle the Accounts module sales book
 * requests
 * 
 * @author vinay
 * 
 * 
 */

public class SalesBookController extends JsonFormController {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory
			.getLogger(SalesBookController.class);

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
		if ("search-sales-return-data-onload".equals(action)
				|| "search-sales-return".equals(action)) {
			return new SalesReturnCommand();
		} else if ("search-sales-data-onload".equals(action)
				|| "search-sales".equals(action)) {
			return new SalesCommand();
		} else if ("search-sales-data".equals(action)
				|| "get-closing-balance".equals(action)
				|| "get-product-details".equals(action)
				|| "show-hide-buttons".equals(action)
				|| "update-stock".equals(action)) {
			return new AllotStockCommand();
		} else {
			return new AllotStockCommand();
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
		IResult result = getAction(request, "accountsAction").process(command);
		ModelAndView modelAndView = new ModelAndView("jsonView");
		modelAndView.addObject("result", result);

		if (_logger.isDebugEnabled()) {
			_logger.debug("modelAndView: {}", modelAndView);
		}
		return modelAndView;
	}
}
