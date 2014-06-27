package com.vekomy.vbooks.mysales.cr.command;

import java.io.Serializable;

/**This command class is responsible for getting request for Delivery Note Product CR.
 * 
 * @author Ankit
 * 
 * 
 */

public class ChangeRequestDeliveryNoteProductCommand implements Serializable{
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 6045955352786136238L;
	/**
	 * String variable holds action.
	 */
	private String action;
	/**
	 * String variable holds presnetPayable.
	 */
	private String presentPayable;
	/**
	 * String variable holds presentCredit.
	 */
	private String presentCredit;
	/**
	 * String variable holds previousCredit.
	 */
	private String previousCredit;
	/**
	 * String variable holds presentAdvance.
	 */
	private String presentAdvance;
	/**
	 * String variable holds totalPayable.
	 */
	private String totalPayable;
	/**
	 * String variable holds presentPayment.
	 */
	private String presentPayment;
	/**
	 * String variable holds totalCredit.
	 */
	private String balance;
	/**
	 * String variable holds paymentType.
	 */
	private String paymentType;
	
	/**
	 * String variable holds CR description.
	 */
	private String description;
	
	/**
	 * String variable holds bankName.
	 */
	private String bankName;
	/**
	 * String variable holds branchName.
	 */
	private String branchName;
	/**
	 * String variable holds ChequeNo.
	 */
	private String ChequeNo;
	
	/**
	 * String variable holds Bank Location.
	 */
	private String bankLocation;
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
	 * @return the presnetPayable
	 */
	public String getPresentPayable() {
		return presentPayable;
	}

	/**
	 * @param presnetPayable
	 *            the presnetPayable to set
	 */
	public void setPresentPayable(String presentPayable) {
		this.presentPayable = presentPayable;
	}

	/**
	 * @return the presentCredit
	 */
	public String getPresentCredit() {
		return presentCredit;
	}

	/**
	 * @param presentCredit
	 *            the presentCredit to set
	 */
	public void setPresentCredit(String presentCredit) {
		this.presentCredit = presentCredit;
	}

	/**
	 * @return the presentAdvance
	 */
	public String getPresentAdvance() {
		return presentAdvance;
	}

	/**
	 * @param presentAdvance
	 *            the presentAdvance to set
	 */
	public void setPresentAdvance(String presentAdvance) {
		this.presentAdvance = presentAdvance;
	}

	/**
	 * @return the totalPayable
	 */
	public String getTotalPayable() {
		return totalPayable;
	}

	/**
	 * @param totalPayable
	 *            the totalPayable to set
	 */
	public void setTotalPayable(String totalPayable) {
		this.totalPayable = totalPayable;
	}

	/**
	 * @return the presentPayment
	 */
	public String getPresentPayment() {
		return presentPayment;
	}

	/**
	 * @param presentPayment
	 *            the presentPayment to set
	 */
	public void setPresentPayment(String presentPayment) {
		this.presentPayment = presentPayment;
	}

	/**
	 * @return the totalCredit
	 */
	public String getBalance() {
		return balance;
	}

	/**
	 * @param totalCredit
	 *            the totalCredit to set
	 */
	public void setBalance(String balance) {
		this.balance = balance;
	}

	/**
	 * @return the paymentType
	 */
	public String getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType
	 *            the paymentType to set
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the previousCredit
	 */
	public String getPreviousCredit() {
		return previousCredit;
	}

	/**
	 * @param previousCredit
	 *            the previousCredit to set
	 */
	public void setPreviousCredit(String previousCredit) {
		this.previousCredit = previousCredit;
	}
	
	
	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	/**
	 * @return the chequeNo
	 */
	public String getChequeNo() {
		return ChequeNo;
	}

	/**
	 * @param chequeNo the chequeNo to set
	 */
	public void setChequeNo(String chequeNo) {
		ChequeNo = chequeNo;
	}

	public String getBankLocation() {
		return bankLocation;
	}

	public void setBankLocation(String bankLocation) {
		this.bankLocation = bankLocation;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer("[ action :").append(getAction())
				.append(",presentPayable :").append(getPresentPayable())
				.append(", presentCredit :").append(getPresentCredit())
				.append(", previousCredit :").append(getPreviousCredit())
				.append(", presentAdvance :").append(getPresentAdvance())
				.append(",totalPayable :").append(getTotalPayable())
				.append(", presentPayment :").append(getPresentPayment())
				.append(", balance :").append(getBalance())
				.append(", paymentType :").append(getPaymentType()).append("]")
				.toString();
	}

}
