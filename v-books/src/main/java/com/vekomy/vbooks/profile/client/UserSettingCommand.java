package com.vekomy.vbooks.profile.client;

import com.vekomy.vbooks.hibernate.model.VbUserSetting;
import com.vekomy.vbooks.spring.command.ActionSupport;

/**
 * @author Sudhakar
 *
 * 
 */
public class UserSettingCommand extends VbUserSetting implements ActionSupport {

	/**
	 * long variable holdes serialVersionUID.
	 */
	private static final long serialVersionUID = -1935913828156976809L;
	/**
	 * String variable holdes action.
	 */
	private String action;
	
	public UserSettingCommand() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.vekomy.vbooks.spring.command.ActionSupport#getAction()
	 */
	@Override
	public String getAction() {
		// TODO Auto-generated method stub
		return action;
	}

	/* (non-Javadoc)
	 * @see com.vekomy.vbooks.spring.command.ActionSupport#setAction(java.lang.String)
	 */
	@Override
	public void setAction(String action) {
		this.action = action;
		
	}

	

}
