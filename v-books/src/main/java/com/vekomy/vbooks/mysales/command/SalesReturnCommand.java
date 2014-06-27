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
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 4540748771920791684L;

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
	private float cost;
	/**
	 * float variable holds totalCost.
	 */
	private float totalCost;
	/**
	 * float variable holds grandTotalCost
	 */
	private float grandTotalCost;

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
	 * @return the cost
	 */
	public float getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(float cost) {
		this.cost = cost;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer("[action:").append(action)
				.append(", productName:").append(productName)
				.append(", batchNumber:").append(batchNumber)
				.append(",damaged: ").append(damaged).append(",resalable: ")
				.append(resalable).append(", totalQty:").append(totalQty)
				.append(", cost:").append(getCost()).append(",totalCost:")
				.append(totalCost).append("]").toString();
	}
}
