package com.vekomy.vbooks.organization.command;

import com.vekomy.vbooks.spring.command.ActionSupport;

/**
 * @author Sudhakar
 * 
 * 
 */
public class ValidateUserCommand implements ActionSupport {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 7027220991209231364L;
	/**
	 * String variable holdes action.
	 */
	private String action;
	/**
	 * String variable holdes username.
	 */
	private String username;
	/**
	 * String variable holdes businessName.
	 */
	private String businessName;

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public String toString() {
		return new StringBuffer("[ action :").append(getAction())
				.append(",userName :").append(getUsername())
				.append(", businessName :").append(getBusinessName())
				.append("]").toString();
	}

}
