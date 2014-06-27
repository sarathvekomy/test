package com.vekomy.vbooks.spring.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.vekomy.vbooks.spring.action.BaseAction;

@SuppressWarnings("deprecation")
public abstract class JsonFormController extends SimpleFormController {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.BaseCommandController#initBinder(
	 * javax.servlet.http.HttpServletRequest,
	 * org.springframework.web.bind.ServletRequestDataBinder)
	 */
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
		binder.registerCustomEditor(Date.class, editor);
	}

	/**
	 * This method id responsible to {@link BaseAction}.
	 * 
	 * @param request - {@link HttpServletRequest}
	 * @param actionName - {@link String}
	 * @return baseAction - {@link BaseAction}
	 * 
	 */
	protected BaseAction getAction(HttpServletRequest request, String actionName) {
		WebApplicationContext applicationContext = getWebApplicationContext();
		BaseAction baseAction = (BaseAction) applicationContext.getBean(actionName);
		baseAction.prepareDao(request);

		return baseAction;
	}
}
