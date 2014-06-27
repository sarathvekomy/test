/**
 * com.vekomy.vbooks.mysales.command.DeliveryNoteProductCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 18, 2013
 */
package com.vekomy.vbooks.mysales.command;

/**
 * @author Swarupa
 * 
 * 
 */
public class DeliveryNoteProductCommand {

	/**
	 * String variable holds action.
	 */
	private String action;
	/**
	 * float variable holds presnetPayable.
	 */
	private float presentPayable;
	/**
	 * float variable holds presentCredit.
	 */
	private float presentCredit;
	/**
	 * float variable holds previousCredit.
	 */
	private float previousCredit;
	/**
	 * float variable holds presentAdvance.
	 */
	private float presentAdvance;
	/**
	 * float variable holds totalPayable.
	 */
	private float totalPayable;
	/**
	 * float variable holds presentPayment.
	 */
	private float presentPayment;
	/**
	 * float variable holds totalCredit.
	 */
	private float balance;
	/**
	 * String variable holds paymentType.
	 */
	private String paymentType;
	
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
	 * String variable holds businessName1.
	 */
	private String businessName1;
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
	public float getPresentPayable() {
		return presentPayable;
	}

	/**
	 * @param presnetPayable
	 *            the presnetPayable to set
	 */
	public void setPresentPayable(float presentPayable) {
		this.presentPayable = presentPayable;
	}

	/**
	 * @return the presentCredit
	 */
	public float getPresentCredit() {
		return presentCredit;
	}

	/**
	 * @param presentCredit
	 *            the presentCredit to set
	 */
	public void setPresentCredit(float presentCredit) {
		this.presentCredit = presentCredit;
	}

	/**
	 * @return the presentAdvance
	 */
	public float getPresentAdvance() {
		return presentAdvance;
	}

	/**
	 * @param presentAdvance
	 *            the presentAdvance to set
	 */
	public void setPresentAdvance(float presentAdvance) {
		this.presentAdvance = presentAdvance;
	}

	/**
	 * @return the totalPayable
	 */
	public float getTotalPayable() {
		return totalPayable;
	}

	/**
	 * @param totalPayable
	 *            the totalPayable to set
	 */
	public void setTotalPayable(float totalPayable) {
		this.totalPayable = totalPayable;
	}

	/**
	 * @return the presentPayment
	 */
	public float getPresentPayment() {
		return presentPayment;
	}

	/**
	 * @param presentPayment
	 *            the presentPayment to set
	 */
	public void setPresentPayment(float presentPayment) {
		this.presentPayment = presentPayment;
	}

	/**
	 * @return the totalCredit
	 */
	public float getBalance() {
		return balance;
	}

	/**
	 * @param totalCredit
	 *            the totalCredit to set
	 */
	public void setBalance(float balance) {
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
	 * @return the previousCredit
	 */
	public float getPreviousCredit() {
		return previousCredit;
	}

	/**
	 * @param previousCredit
	 *            the previousCredit to set
	 */
	public void setPreviousCredit(float previousCredit) {
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

	/**
	 * @return the businessName1
	 */
	public String getBusinessName1() {
		return businessName1;
	}

	/**
	 * @param businessName1 the businessName1 to set
	 */
	public void setBusinessName1(String businessName1) {
		this.businessName1 = businessName1;
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
