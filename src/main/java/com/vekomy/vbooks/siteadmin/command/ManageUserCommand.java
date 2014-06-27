/**
 * com.vekomy.vbooks.siteadmin.command.ManageUserCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jun 26, 2013
 */
package com.vekomy.vbooks.siteadmin.command;

import java.io.Serializable;



/**
 * @author swarupa
 *
 */
public class ManageUserCommand implements Serializable{
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 2253684897218563022L;
	/**
	 * String variable holds action
	 */
	private String action;
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
}
