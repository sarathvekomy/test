package com.vekomy.vbooks.mysales.cr.command;

import java.io.Serializable;
/**
 * This command class is responsible for getting product list results for Delivery Note CR.
 *  
 * @author Ankit
 * 
 * 
 */
public class ChangeRequestProductResult implements Serializable {
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 8758646333134683759L;
	/**
	 * String variable holds productName
	 */
	private String productName;
	/**
	 * String variable holds batchNumber
	 */
	private String batchNumber;
	/**
	 * String variable holds productQty
	 */
	private String productQty;
	/**
	 * String variable holds productCost
	 */
	private String productCost;
	/**
	 * String variable holds totalCost
	 */
	private String totalCost;
	/**
	 * String variable holds qtyAllotted
	 */
	private String availableQuantity;

	private Integer bonus;
	
	private String bonusReason;
	
	private Float grandTotal;
	
	public Float getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Float grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Integer getBonus() {
		return bonus;
	}

	public void setBonus(Integer bonus) {
		this.bonus = bonus;
	}

	public String getBonusReason() {
		return bonusReason;
	}

	public void setBonusReason(String bonusReason) {
		this.bonusReason = bonusReason;
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
	 * @param batchNumber the batchNumber to set
	 */
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	/**
	 * @return the productQty
	 */
	public String getProductQty() {
		return productQty;
	}

	/**
	 * @param productQty
	 *            the productQty to set
	 */
	public void setProductQty(String productQty) {
		this.productQty = productQty;
	}

	/**
	 * @return the productCost
	 */
	public String getProductCost() {
		return productCost;
	}

	/**
	 * @param productCost
	 *            the productCost to set
	 */
	public void setProductCost(String productCost) {
		this.productCost = productCost;
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
	 * @return the availableQuantity
	 */
	public String getAvailableQuantity() {
		return availableQuantity;
	}

	/**
	 * @param qtyAllotted
	 *            the qtyAllotted to set
	 */
	public void setAvailableQuantity(String availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer("[ productame:").append(getProductName())
				.append("productCost :").append(getProductCost())
				.append("totalCost :").append(getTotalCost())
				.append("availableQuantity :").append(getAvailableQuantity())
				.append("]").toString();
	}

}
