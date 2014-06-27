/**
 * com.vekomy.vbooks.mysales.command.DeliveryNoteCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 17, 2013
 */
package com.vekomy.vbooks.mysales.command;

import com.vekomy.vbooks.hibernate.model.VbDeliveryNote;

/**
 * @author Sudhakar
 * 
 */
public class DeliveryNoteCommand extends VbDeliveryNote {
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 2821382467254920993L;
	/**
	 * String variable holds action.
	 */
	private String action;
	/**
	 * String variable holds productName.
	 */
	private String productName;
	/**
	 * int variable holds productQty.
	 */
	private int productQuantity;
	/**
	 * float variable holds productCost.
	 */
	private float productCost;
	/**
	 * float variable holds totalCost.
	 */
	private float totalCost;
	/**
	 * int variable holds bonusQty.
	 */
	private int bonusQuantity;
	/**
	 * String variable holds bonusReason.
	 */
	private String bonusReason;
	/**
	 * String variable holds batchNumer
	 */
	private String batchNumer;
	
	/**
	 * String variable holds forPayments.
	 */
	private String forPayments;
	
	/**
	 * String variable holds businessName
	 */
	private String businessName;
	
	/**
	 * String variable holds invoiceName
	 */
	private String invoiceName;
	
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

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
	 * @return the productQty
	 */
	public int getProductQuantity() {
		return productQuantity;
	}

	/**
	 * @param productQty
	 *            the productQty to set
	 */
	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	/**
	 * @return the productCost
	 */
	public float getProductCost() {
		return productCost;
	}

	/**
	 * @param productCost
	 *            the productCost to set
	 */
	public void setProductCost(float productCost) {
		this.productCost = productCost;
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
	 * @return the bonusQty
	 */
	public int getBonusQuantity() {
		return bonusQuantity;
	}

	/**
	 * @param bonusQty
	 *            the bonusQty to set
	 */
	public void setBonusQuantity(int bonusQuantity) {
		this.bonusQuantity = bonusQuantity;
	}

	/**
	 * @return the bonusReason
	 */
	public String getBonusReason() {
		return bonusReason;
	}

	/**
	 * @param bonusReason
	 *            the bonusReason to set
	 */
	public void setBonusReason(String bonusReason) {
		this.bonusReason = bonusReason;
	}
	
	

	/**
	 * @return the batchNumer
	 */
	public String getBatchNumer() {
		return batchNumer;
	}

	/**
	 * @param batchNumer the batchNumer to set
	 */
	public void setBatchNumer(String batchNumer) {
		this.batchNumer = batchNumer;
	}
	
	
	/**
	 * @return the forPayments
	 */
	public String getForPayments() {
		return forPayments;
	}

	/**
	 * @param forPayments the forPayments to set
	 */
	public void setForPayments(String forPayments) {
		this.forPayments = forPayments;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer("[ action :").append(getAction())
				.append(", productName :").append(getProductName())
				.append(",productQuantity :").append(getProductQuantity())
				.append(", productCost :").append(getProductCost())
				.append(", totalCost :").append(getTotalCost())
				.append(",bonusQuantity :").append(getBonusQuantity())
				.append(",bonusReason").append(getBonusReason()).append("]")
				.toString();

	}

}
