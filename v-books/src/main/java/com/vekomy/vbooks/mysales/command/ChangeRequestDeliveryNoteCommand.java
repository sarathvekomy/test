package com.vekomy.vbooks.mysales.command;

import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteChangeRequest;
/**
 * This command class is responsible for getting request for Delivery Note CR.
 *  
 * @author Ankit
 * 
 * 
 */
public class ChangeRequestDeliveryNoteCommand extends
		VbDeliveryNoteChangeRequest {
	
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 2838537766615039672L;
	/**
	 * String variable holds action.
	 */
	private String action;
	/**
	 * String variable holds productName.
	 */
	private String productName;
	/**
	 * String variable holds productQty.
	 */
	private String productQuantity;
	/**
	 * String variable holds productCost.
	 */
	private String productCost;
	/**
	 * float variable holds totalCost.
	 */
	private String totalCost;
	/**
	 * float variable holds bonusQty.
	 */
	private String bonusQuantity;
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
	
	private String businessName;
	
	private String invoiceName;
	
	private String invoiceNumber;
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

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
	public String getProductQuantity() {
		return productQuantity;
	}

	/**
	 * @param productQty
	 *            the productQty to set
	 */
	public void setProductQuantity(String productQuantity) {
		this.productQuantity = productQuantity;
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
	 * @return the bonusQty
	 */
	public String getBonusQuantity() {
		return bonusQuantity;
	}

	/**
	 * @param bonusQty
	 *            the bonusQty to set
	 */
	public void setBonusQuantity(String bonusQuantity) {
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
