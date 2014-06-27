package com.vekomy.vbooks.siteadmin.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.organization.command.OrganizationCommand;
import com.vekomy.vbooks.organization.command.ValidateUserCommand;
import com.vekomy.vbooks.organization.dao.OrganizationDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * @author Satish
 * 
 * 
 */
public class SiteAdminAction extends BaseAction {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SiteAdminAction.class);
	/**
	 * String variable holds LOAD_ORGANIZATION.
	 */
	public static final String LOAD_ORGANIZATION = "load-organization";
	/**
	 * String variable holds SAVE_ORGANIZATION.
	 */
	public static final String SAVE_ORGANIZATION = "save-organization";
	/**
	 * String variable holds SAVE_GRADE.
	 */
	public static final String SAVE_GRADE = "save-grade";
	/**
	 * String variable holds UPDATE_ORGANIZATION.
	 */
	public static final String UPDATE_ORGANIZATION = "update-organization";
	/**
	 * String variable holds SAVE_CLASSES.
	 */
	public static final String SAVE_CLASSES = "save-classes";
	/**
	 * String variable holds LOAD_CLASSES.
	 */
	public static final String LOAD_CLASSES = "load-classes";
	/**
	 * String variable holds GET_ORGANIZATION_CODE.
	 */
	public static final String GET_ORGANIZATION_CODE = "get-organization-code";
	/**
	 * String variable holds LOAD_ORGANIZATION_GRADES.
	 */
	public static final String LOAD_ORGANIZATION_GRADES = "load-organization-grades";
	/**
	 * String variable holds VALIDATE_USER.
	 */
	public static final String VALIDATE_USER = "validate-username";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	public IResult process(Object form) {
		try {
			ResultSuccess resultSuccess = new ResultSuccess();
			OrganizationDao organizationDao = (OrganizationDao) getDao();
			OrganizationCommand organizationCommand = null;
			ValidateUserCommand validateUserCommand = null;
			if (form instanceof OrganizationCommand) {
				organizationCommand = (OrganizationCommand) form;
				String action = organizationCommand.getAction();
				if (LOAD_ORGANIZATION.equals(action)) {
					VbOrganization snOrganization = organizationDao.getOrganization(organizationCommand.getId());
					getUser().setOrganization(snOrganization);
					
					resultSuccess.setData(snOrganization);
					resultSuccess.setStatus(OrganizationUtils.RESULT_STATUS_SUCCESS);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if (GET_ORGANIZATION_CODE.equals(action)) {
					resultSuccess.setData(organizationDao.getOrganizationCode());
					resultSuccess.setStatus(OrganizationUtils.RESULT_STATUS_SUCCESS);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				}
			} else {
				validateUserCommand = (ValidateUserCommand) form;
				if (VALIDATE_USER.equals(validateUserCommand.getAction())) {
					String result = organizationDao.validateUsername(validateUserCommand);
					
					resultSuccess.setData((result));
					resultSuccess.setStatus(OrganizationUtils.RESULT_STATUS_SUCCESS);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				}
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("resultError: {}", resultError);
			}
			return resultError;
		}
	}
}
