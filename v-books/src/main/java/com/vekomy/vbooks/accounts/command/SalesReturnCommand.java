package com.vekomy.vbooks.accounts.command;

import com.vekomy.vbooks.hibernate.model.VbSalesReturn;

/**
 * This command class is a intermediate class for {@link VbSalesReturn}.
 * 
 * @author vinay
 * 
 */
public class SalesReturnCommand extends VbSalesReturn {

	/**
	 * long variable holdes serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

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
