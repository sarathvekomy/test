/**
 * com.vekomy.vbooks.mysales.command.DayBookAmountCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jun 20, 2013
 */
package com.vekomy.vbooks.mysales.command;

import com.vekomy.vbooks.hibernate.model.VbDayBookAmount;

/**
 * @author swarupa
 *
 */
public class DayBookAmountCommand extends VbDayBookAmount {
	
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -7806842381421025822L;
	/**
	 * String variable holds action
	 */
	private String action;
	/**
	 * Float variable holds openingBalance
	 */
	private Float openingBalance;
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
	 * @return the openingBalance
	 */
	public Float getOpeningBalance() {
		return openingBalance;
	}
	/**
	 * @param openingBalance the openingBalance to set
	 */
	public void setOpeningBalance(Float openingBalance) {
		this.openingBalance = openingBalance;
	}

}
