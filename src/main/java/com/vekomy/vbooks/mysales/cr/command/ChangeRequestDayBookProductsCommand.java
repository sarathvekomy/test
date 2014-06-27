package com.vekomy.vbooks.mysales.cr.command;

import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequestProducts;
/**
 * @author Ankit
 *
 * 
 */
public class ChangeRequestDayBookProductsCommand extends
		VbDayBookChangeRequestProducts {

	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 2594597470765894282L;

	/**
	 * String variable holds action.
	 */
	private String action;
	
	/**
	 * String variable holds returnQty.
	 */
	private String returnQty;
	
	private String productsCRRemarks;

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
	public String getReturnQty() {
		return returnQty;
	}

	/**
	 * @param returnQty the returnQty to set
	 */
	public void setReturnQty(String returnQty) {
		this.returnQty = returnQty;
	}

	public String getProductsCRRemarks() {
		return productsCRRemarks;
	}

	public void setProductsCRRemarks(String productsCRRemarks) {
		this.productsCRRemarks = productsCRRemarks;
	}
	
}
