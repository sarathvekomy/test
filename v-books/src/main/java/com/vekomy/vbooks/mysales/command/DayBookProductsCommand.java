/**
 * com.vekomy.vbooks.mysales.command.DayBookProductsCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jun 6, 2013
 */
package com.vekomy.vbooks.mysales.command;

import com.vekomy.vbooks.hibernate.model.VbDayBookProducts;

/**
 * @author Sudhakar
 *
 */
public class DayBookProductsCommand extends VbDayBookProducts {

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 2594597470765894282L;

	/**
	 * String variable holds action.
	 */
	private String action;
	
	/**
	 * Integer variable holds returnQty.
	 */
	private Integer returnQty;

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
	 * @return the returnQty
	 */
	public Integer getReturnQty() {
		return returnQty;
	}

	/**
	 * @param returnQty the returnQty to set
	 */
	public void setReturnQty(Integer returnQty) {
		this.returnQty = returnQty;
	}
	
}
