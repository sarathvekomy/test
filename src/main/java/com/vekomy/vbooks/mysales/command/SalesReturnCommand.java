/**
 * com.vekomy.vbooks.mysales.command.SalesReturnCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 19, 2013
 */
package com.vekomy.vbooks.mysales.command;

import com.vekomy.vbooks.hibernate.model.VbSalesReturn;

/**
 * @author Sudhakar
 * 
 * 
 */
public class SalesReturnCommand extends VbSalesReturn {

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 7897773291778992468L;
	/**
	 * String variable holds action.
	 */
	private String action;
	/**
	 * String variable holds productName.
	 */
	private String productName;
	/**
	 * String variable holds batchNumber.
	 */
	private String batchNumber;
	/**
	 * Integer variable holds returnQty.
	 */
	private Integer totalQty;
	/**
	 * Integer variable holds damaged.
	 */
	private Integer damaged;
	/**
	 * Integer variable holds resalable.
	 */
	private Integer resalable;
	/**
	 * float variable holds cost.
	 */
	private float resalableCost;
	/**
	 * float variable holds damagedCost
	 */
	private float damagedCost;
	/**
	 * Float variable holds totalResalableCost
	 */
	private Float totalResalableCost;
	/**
	 * Float variable holds totalDamagedCost
	 */
	private Float totalDamagedCost;
	/**
	 * float variable holds totalCost.
	 */
	private float totalCost;
	/**
	 * float variable holds grandTotalCost
	 */
	private float grandTotalCost;
	
	/**
	 * String variable holds remarks
	 */
	private String remarks;

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
	 * @return the totalQty
	 */
	public Integer getTotalQty() {
		return totalQty;
	}

	/**
	 * @param totalQty
	 *            the totalQty to set
	 */
	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}

	/**
	 * @return the damaged
	 */
	public Integer getDamaged() {
		return damaged;
	}

	/**
	 * @param damaged
	 *            the damaged to set
	 */
	public void setDamaged(Integer damaged) {
		this.damaged = damaged;
	}

	/**
	 * @return the resalable
	 */
	public Integer getResalable() {
		return resalable;
	}

	/**
	 * @param resalable
	 *            the resalable to set
	 */
	public void setResalable(Integer resalable) {
		this.resalable = resalable;
	}


	/**
	 * @return the resalableCost
	 */
	public float getResalableCost() {
		return resalableCost;
	}

	/**
	 * @param resalableCost the resalableCost to set
	 */
	public void setResalableCost(float resalableCost) {
		this.resalableCost = resalableCost;
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
	 * @return the totalResalableCost
	 */
	public Float getTotalResalableCost() {
		return totalResalableCost;
	}

	/**
	 * @param totalResalableCost the totalResalableCost to set
	 */
	public void setTotalResalableCost(Float totalResalableCost) {
		this.totalResalableCost = totalResalableCost;
	}

	/**
	 * @return the totalDamagedCost
	 */
	public Float getTotalDamagedCost() {
		return totalDamagedCost;
	}

	/**
	 * @param totalDamagedCost the totalDamagedCost to set
	 */
	public void setTotalDamagedCost(Float totalDamagedCost) {
		this.totalDamagedCost = totalDamagedCost;
	}

	/**
	 * @return the totalCost
	 */
	public float getTotalCost() {
		return totalCost;
	}

	/**
	 * @param totalCost
	 *            the totalCost to set
	 */
	public void setTotalCost(float totalCost) {
		this.totalCost = totalCost;
	}

	/**
	 * @return the grandTotalCost
	 */
	public float getGrandTotalCost() {
		return grandTotalCost;
	}

	/**
	 * @param grandTotalCost the grandTotalCost to set
	 */
	public void setGrandTotalCost(float grandTotalCost) {
		this.grandTotalCost = grandTotalCost;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
