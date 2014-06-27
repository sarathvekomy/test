package com.vekomy.vbooks.profile.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vekomy.vbooks.profile.client.ChangePasswordCommand;
import com.vekomy.vbooks.profile.client.FavoriteCommand;
import com.vekomy.vbooks.profile.client.UserSettingCommand;
import com.vekomy.vbooks.profile.dao.ProfileDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.spring.command.ActionSupport;
import com.vekomy.vbooks.util.KeyGenerationUtils;

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
	public IResult process(Object form) throws Exception {
		ResultSuccess resultSuccess = new ResultSuccess();
		ActionSupport actionSupport = (ActionSupport) form;
		ProfileActionEnum actionEnum = ProfileActionEnum.valueOf(actionSupport.getAction());
		Object command = null;
		ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession()
		        .getServletContext());
		ProfileDao profileDao = (ProfileDao) hibernateContext.getBean("profileDao");
		switch (actionEnum) {
		case change_theme:
			UserSettingCommand userSettingCommand = (UserSettingCommand) form;
			resultSuccess.setData(profileDao.changeTheme(userSettingCommand.getTheme(), getUsername()));
			resultSuccess.setMessage("Theme changed Succesful");
			break;
		case save_favorite:
			FavoriteCommand favoriteCommand = (FavoriteCommand) form;
			String favorite = "index.jsp?module=" + favoriteCommand.getModule() + "&page=" + favoriteCommand.getPage()
			        + "&pageLink=" + favoriteCommand.getPageLink() + "&favTitle=" + favoriteCommand.getFavTitle();
			resultSuccess.setData(profileDao.saveFavorite(favorite, getUsername()));
			resultSuccess.setMessage("Favorite Save Successful");
			break;
		case change_password:
			ChangePasswordCommand changePasswordCommand = (ChangePasswordCommand) form;
			if (profileDao.changePassword(changePasswordCommand.getOldPassWord(),
			        changePasswordCommand.getNewPassWord(), getUsername())) {
				resultSuccess.setMessage("Password Changed");
				
			} else {
				resultSuccess.setMessage("Failed");
			}
			break;
		case reset_password:
			changePasswordCommand = (ChangePasswordCommand) form;
			String newPassword = KeyGenerationUtils.generatePassword(getOrganization().getName(),getOrganization().getBranchName() , 3);
			resultSuccess.setData(profileDao.resetPassword(newPassword, changePasswordCommand.getUserName()));
//			mailNotification.sendMessage(OrganizationUtils.NEW_PASSWORD_PREFIX+newPassword,OrganizationUtils.PASSWORD_NOTIFICATION, profileDao.getEmail(changePasswordCommand.getUserName()));
			resultSuccess.setMessage("Password  Changed");
			break;
		default:
			command = new UserSettingCommand();
			break;
		}
		
		if(_logger.isDebugEnabled()){
			_logger.debug("resultSuccess: {}", resultSuccess);
		}
		return resultSuccess;
	}

}
