/**
 * com.vekomy.vbooks.profile.controller.ProfileController.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 15, 2013
 */
package com.vekomy.vbooks.profile.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.vekomy.vbooks.profile.command.ChangePasswordCommand;
import com.vekomy.vbooks.profile.command.FavoriteCommand;
import com.vekomy.vbooks.profile.command.UserSettingCommand;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.controller.JsonFormController;
import com.vekomy.vbooks.util.ProfileActionEnum;

/**
 * @author Satish
 * 
 * 
 */
public class ProfileController extends JsonFormController {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ProfileController.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject
	 * (javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		ProfileActionEnum actionEnum = ProfileActionEnum.valueOf(request.getParameter("action"));
		Object command = null;
		switch (actionEnum) {
			case change_theme:
				command = new UserSettingCommand();
				break;
			case save_favorite:
				command = new FavoriteCommand();
				break;
			case change_password:
				command = new ChangePasswordCommand();
				break;
			case reset_password:
				command = new ChangePasswordCommand();
				break;
			default:
				command = new UserSettingCommand();
				break;
		}
		return command;
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
		
		IResult result = getAction(request, "profileAction").process(command);
		ModelAndView modelAndView = new ModelAndView("jsonView");
		modelAndView.addObject("result", result);
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("ModelAndView: {}", modelAndView);
		}
		return modelAndView;
	}

}
