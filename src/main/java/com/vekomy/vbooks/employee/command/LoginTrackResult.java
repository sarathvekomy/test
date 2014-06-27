/**
 * com.vekomy.vbooks.employee.command.LoginTrackResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 17, 2013
 */
package com.vekomy.vbooks.employee.command;

import java.io.Serializable;

/**
 * @author Sudhakar
 *
 */
public class LoginTrackResult implements Serializable{
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 2437838913830683290L;
	/**
	 * String variable holds userName.
	 */
	private String userName;
	/**
	 * String variable holds loginTime.
	 */
	private String loginTime;
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
	 * @return the loginTime
	 */
	public String getLoginTime() {
		return loginTime;
	}
	/**
	 * @param loginTime the loginTime to set
	 */
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
}
