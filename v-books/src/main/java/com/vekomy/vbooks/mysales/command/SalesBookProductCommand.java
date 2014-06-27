package com.vekomy.vbooks.mysales.command;

import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;

/**
 * @author rajesh
 * 
 */
public class SalesBookProductCommand extends VbSalesBookProducts {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8699803638469424600L;

	private String action;
	private String productName;

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
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName
	 *            the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String toString() {
		return new StringBuffer("[ action :").append(getAction())
				.append(",productName :").append(getProductName()).append("]")
				.toString();
	}

}
