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
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -1390334705983228510L;
	/**
	 * String variable holds productName.
	 */
	private String productName;
	/**
	 * String variable holds batchNumber.
	 */
	private String batchNumber;
	/**
	 * String variable holds totalCost.
	 */
	private String totalCost;

	/**
	 * Integer variable holds damagedQty
	 */
	private Integer damagedQty;
	
	/**
	 * Integer variable holds resaleableQty
	 */
	private Integer resaleableQty;
	
	/**
	 * float variable holds damagedCost
	 */
	private float damagedCost;
	
	/**
	 * float variable holds resaleableCost
	 */
	private float resaleableCost;
	/**
	 * Integer variable holds totalQty
	 */
	private Integer totalQty;
	
	/**
	 * @return damagedQty - {@link Integer}
	 */
	public Integer getdamagedQty() {
		return damagedQty;
	}

	/**
	 * @param damagedQty {@link Integer}
	 */
	public void setdamagedQty(Integer damagedQty) {
		this.damagedQty = damagedQty;
	}

	/**
	 * @return resaleableQty - {@link Integer}
	 */
	public Integer getResaleableQty() {
		return resaleableQty;
	}

	/**
	 * @param resaleableQty - {@link Integer}
	 */
	public void setResaleableQty(Integer resaleableQty) {
		this.resaleableQty = resaleableQty;
	}


	/**
	 * @return totalQty - {@link Integer}
	 */
	public Integer getTotalQty() {
		return totalQty;
	}

	/**
	 * @param totalQty - {@link Integer}
	 */
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

	/**
	 * @return the damagedQty
	 */
	public Integer getDamagedQty() {
		return damagedQty;
	}

	/**
	 * @param damagedQty the damagedQty to set
	 */
	public void setDamagedQty(Integer damagedQty) {
		this.damagedQty = damagedQty;
	}

	/**
	 * @return the damagedCost
	 */
	public float getDamagedCost() {
		return damagedCost;
	}

	/**
	 * @param damagedCost the damagedCost to set
	 */
	public void setDamagedCost(float damagedCost) {
		this.damagedCost = damagedCost;
	}

	/**
	 * @return the resaleableCost
	 */
	public float getResaleableCost() {
		return resaleableCost;
	}

	/**
	 * @param resaleableCost the resaleableCost to set
	 */
	public void setResaleableCost(float resaleableCost) {
		this.resaleableCost = resaleableCost;
	}
	
}
