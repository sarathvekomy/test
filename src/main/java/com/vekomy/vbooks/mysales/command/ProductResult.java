/**
 * com.vekomy.vbooks.mysales.command.ProductResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 4, 2013
 */
package com.vekomy.vbooks.mysales.command;

import java.io.Serializable;

/**
 * @author Ankit
 *
 */
public class ProductResult implements Serializable {
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 5062938672944121085L;
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
	private Integer productQty;
	/**
	 * Float variable holds productCost
	 */
	private String productCost;
	/**
	 * Float variable holds totalCost
	 */
	private String totalCost;
	/**
	 * Integer variable holds qtyAllotted
	 */
	private String availableQuantity;

	/**
	 * Integer variable holds bonus
	 */
	private Integer bonus;
	
	/**
	 * String variable holds bonusReason
	 */
	private String bonusReason;
	
	/**
	 * Float variable holds grandTotal
	 */
	private Float grandTotal;
	
	/**
	 * @return grandTotal - {@link Float}
	 */
	public Float getGrandTotal() {
		return grandTotal;
	}

	/**
	 * @param grandTotal - {@link Float}
	 */
	public void setGrandTotal(Float grandTotal) {
		this.grandTotal = grandTotal;
	}

	/**
	 * @return bonus - {@link Integer}
	 */
	public Integer getBonus() {
		return bonus;
	}

	/**
	 * @param bonus - {@link Integer}
	 */
	public void setBonus(Integer bonus) {
		this.bonus = bonus;
	}

	/**
	 * @return bonusReason - {@link String}
	 */
	public String getBonusReason() {
		return bonusReason;
	}

	/**
	 * @param bonusReason - {@link String}
	 */
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
	public Integer getProductQty() {
		return productQty;
	}

	/**
	 * @param productQty
	 *            the productQty to set
	 */
	public void setProductQty(Integer productQty) {
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
