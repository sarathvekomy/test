package com.vekomy.vbooks.profile.client;

import com.vekomy.vbooks.hibernate.model.VbUserSetting;
import com.vekomy.vbooks.spring.command.ActionSupport;

/**
 * @author Sudhakar
 *
 * 
 */
public class ChangePasswordCommand extends VbUserSetting implements ActionSupport {

	/**
	 * long variable holdes serialVersionUID.
	 */
	private static final long serialVersionUID = -3408469458678516201L;
	/**
	 * String variable holdes action.
	 */
	private String action;
	/**
	 * String variable holdes userName.
	 */
	private String userName;
	/**
	 * String variable holdes newPassWord.
	 */
	private String newPassWord;
	/**
	 * String variable holdes oldPassWord.
	 */
	private String oldPassWord;
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the newPassWord
	 */
	public String getNewPassWord() {
		return newPassWord;
	}
	/**
	 * @param newPassWord the newPassWord to set
	 */
	public void setNewPassWord(String newPassWord) {
		this.newPassWord = newPassWord;
	}
	/**
	 * @return the oldPassWord
	 */
	public String getOldPassWord() {
		return oldPassWord;
	}
	/**
	 * @param oldPassWord the oldPassWord to set
	 */
	public void setOldPassWord(String oldPassWord) {
		this.oldPassWord = oldPassWord;
	}
	
	
}
