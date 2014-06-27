package com.vekomy.vbooks.mysales.command;

import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequestAmount;
/**
 * @author Ankit
 *
 */
public class ChangeRequestDayBookAllowancesCommand extends
		VbDayBookChangeRequestAmount {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -5883454357048486692L;
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
