/**
 * com.vekomy.vbooks.mysales.command.SalesReturnsResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: May 13, 2013
 */
package com.vekomy.vbooks.mysales.command;

import java.io.Serializable;

/**
 * 
 * @author Sudhakar
 * 
 * 
 */
public class SalesReturnsResult implements Serializable {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -2654650752399714466L;

	/**
	 * String variable holds productName.
	 */
	private String productName;
	/**
	 * String variable holds batchNumber.
	 */
	private String batchNumber;
	/**
	 * Float variable holds cost.
	 */
	private String cost;

	/**
	 * String variable holds totalCost.
	 */
	private String totalCost;

	private Integer damagedQty;
	
	private Integer resaleableQty;
	
	private Integer totalQty;
	
	public Integer getdamagedQty() {
		return damagedQty;
	}

	public void setdamagedQty(Integer damagedQty) {
		this.damagedQty = damagedQty;
	}

	public Integer getResaleableQty() {
		return resaleableQty;
	}

	public void setResaleableQty(Integer resaleableQty) {
		this.resaleableQty = resaleableQty;
	}


	public Integer getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
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

	/**
	 * @return the batchNumber
	 */
	public String getBatchNumber() {
		return batchNumber;
	}

	/**
	 * @param batchNumber
	 *            the batchNumber to set
	 */
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	/**
	 * @return the cost
	 */
	public String getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(String cost) {
		this.cost = cost;
	}

	/**
	 * @return the totalCost
	 */
	public String getTotalCost() {
		return totalCost;
	}

	/**
	 * @param totalCost
	 *            the totalCost to set
	 */
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer("[productName: ").append(productName)
				.append(",batchNumber: ").append(batchNumber).append(",cost: ")
				.append(cost).append("]").toString();
	}
}
