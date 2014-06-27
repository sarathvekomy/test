package com.vekomy.vbooks.mysales.command;

import com.vekomy.vbooks.hibernate.model.VbSalesReturnChangeRequest;
/**
 * @author Ankit
 * 
 * 
 */
public class ChangeRequestSalesReturnCommand extends VbSalesReturnChangeRequest {
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
	 * String variable holds returnQty.
	 */
	private String totalQty;
	/**
	 * String variable holds damaged.
	 */
	private String damaged;
	/**
	 * String variable holds resalable.
	 */
	private String resalable;
	/**
	 * String variable holds cost.
	 */
	private String cost;
	/**
	 * String variable holds totalCost.
	 */
	private String totalCost;
	/**
	 * String variable holds grandTotalCost
	 */
	private String grandTotalCost;

	/**
	 * String variable holds CR Description
	 */
	private String description;

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
	public String getTotalQty() {
		return totalQty;
	}

	/**
	 * @param totalQty
	 *            the totalQty to set
	 */
	public void setTotalQty(String totalQty) {
		this.totalQty = totalQty;
	}

	/**
	 * @return the damaged
	 */
	public String getDamaged() {
		return damaged;
	}

	/**
	 * @param damaged
	 *            the damaged to set
	 */
	public void setDamaged(String damaged) {
		this.damaged = damaged;
	}

	/**
	 * @return the resalable
	 */
	public String getResalable() {
		return resalable;
	}

	/**
	 * @param resalable
	 *            the resalable to set
	 */
	public void setResalable(String resalable) {
		this.resalable = resalable;
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

	/**
	 * @return the grandTotalCost
	 */
	public String getGrandTotalCost() {
		return grandTotalCost;
	}

	/**
	 * @param grandTotalCost the grandTotalCost to set
	 */
	public void setGrandTotalCost(String grandTotalCost) {
		this.grandTotalCost = grandTotalCost;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
