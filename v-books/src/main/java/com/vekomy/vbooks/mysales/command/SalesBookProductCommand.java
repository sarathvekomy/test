/**
 * com.vekomy.vbooks.mysales.command.SalesBookProductCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 4, 2013
 */
package com.vekomy.vbooks.mysales.command;

import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;

/**
 * @author rajesh
 * 
 */
public class SalesBookProductCommand extends VbSalesBookProducts {

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 8135331550506340618L;
	/**
	 * String variable holds action
	 */
	private String action;
	/**
	 * String variable holds productName
	 */
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
