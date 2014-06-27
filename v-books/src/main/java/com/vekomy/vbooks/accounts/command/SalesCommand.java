/**
 * com.vekomy.vbooks.accounts.command.SalesCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: may 9, 2013
 */
package com.vekomy.vbooks.accounts.command;

import com.vekomy.vbooks.hibernate.model.VbSalesBook;

/**
 * @author Swarupa
 *
 */
public class SalesCommand extends VbSalesBook {

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 3577144162193610292L;
	/**
	 * String variable holds action.
	 */
	private String action;

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

	public String toString() {
		return new StringBuffer("action :").append(getAction()).append("]")
				.toString();
	}

}
