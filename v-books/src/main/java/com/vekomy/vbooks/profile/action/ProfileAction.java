/**
 * com.vekomy.vbooks.profile.action.ProfileAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 15, 2013
 */
package com.vekomy.vbooks.profile.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.profile.command.ChangePasswordCommand;
import com.vekomy.vbooks.profile.command.FavoriteCommand;
import com.vekomy.vbooks.profile.command.UserSettingCommand;
import com.vekomy.vbooks.profile.dao.ProfileDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.spring.command.ActionSupport;
import com.vekomy.vbooks.util.KeyGenerationUtils;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.ProfileActionEnum;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * @author Sudhakar
 *
 */
public class ProfileAction extends BaseAction {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ProfileAction.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tsb.nirvahak.spring.action.BaseAction#process(java.lang.Object)
	 */
	@SuppressWarnings("unused")
	public IResult process(Object form) {
		try {
			Object command = null;
			VbOrganization organization = getOrganization();
			String userName = getUsername();
			ResultSuccess resultSuccess = new ResultSuccess();
			String resultSuccessStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
			ActionSupport actionSupport = (ActionSupport) form;
			ProfileActionEnum actionEnum = ProfileActionEnum.valueOf(actionSupport.getAction());
			ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
			ProfileDao profileDao = (ProfileDao) hibernateContext.getBean("profileDao");
			switch (actionEnum) {
				case change_theme:
					UserSettingCommand userSettingCommand = (UserSettingCommand) form;
					Boolean result = profileDao.changeTheme(userSettingCommand.getTheme(), userName, organization);

					resultSuccess.setData(result);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
					break;
				case save_favorite:
					FavoriteCommand favoriteCommand = (FavoriteCommand) form;
					String favorite = "index.jsp?module=" + favoriteCommand.getModule()
							+ "&page=" + favoriteCommand.getPage() + "&pageLink="
							+ favoriteCommand.getPageLink() + "&favTitle="
							+ favoriteCommand.getFavTitle();
					Boolean isSaved = profileDao.saveFavorite(favorite, userName, organization);
					
					resultSuccess.setData(isSaved);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
					break;
				case change_password:
					ChangePasswordCommand changePasswordCommand = (ChangePasswordCommand) form;
					Boolean isUpdated = profileDao.changePassword(changePasswordCommand, userName, organization);
					
					resultSuccess.setData(isUpdated);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
					break;
				case reset_password:
					changePasswordCommand = (ChangePasswordCommand) form;
					String newPassword = KeyGenerationUtils.generatePassword(getOrganization().getName(), getOrganization().getBranchName(), 3);
					String changedpassword = profileDao.resetPassword(newPassword, changePasswordCommand.getUserName());
					
					resultSuccess.setData(changedpassword);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
					break;
				default:
					command = new UserSettingCommand();
					break;
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());
			
			if(_logger.isErrorEnabled()) {
				_logger.error("ResultError: {}", resultError);
			}
			return resultError;
		}
	}
}
