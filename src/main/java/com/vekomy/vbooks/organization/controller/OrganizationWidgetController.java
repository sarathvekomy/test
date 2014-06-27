package com.vekomy.vbooks.organization.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.vekomy.vbooks.organization.action.OrganizationAction;
import com.vekomy.vbooks.organization.command.OrganizationCommand;
import com.vekomy.vbooks.organization.command.OrganizationSuperUserCommand;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.controller.JsonFormController;

/**
 * @author Satish
 * 
 * 
 */
public class OrganizationWidgetController extends JsonFormController {

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
		if(OrganizationAction.SAVE_ORGANIZATION_SUPER_USER.equals(action) || OrganizationAction.SAVE_ORGANIZATION_EDIT_SUPER_USER.equals(action)){
			return new OrganizationSuperUserCommand();
		} else{
			return new OrganizationCommand();
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
		IResult result = getAction(request, "organizationAction").process(
				command);

		ModelAndView modelAndView = new ModelAndView("jsonView");
		modelAndView.addObject("result", result);

		return modelAndView;
	}

}
