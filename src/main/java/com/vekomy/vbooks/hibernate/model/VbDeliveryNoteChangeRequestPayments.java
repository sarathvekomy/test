package com.vekomy.vbooks.hibernate.model;

// Generated Sep 11, 2013 11:07:56 AM by Hibernate Tools 3.4.0.CR1

/**
 * VbDeliveryNoteChangeRequestPayments generated by hbm2java
 */
public class VbDeliveryNoteChangeRequestPayments implements
		java.io.Serializable {

	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -8826342908204488317L;
	private Integer id;
	private VbDeliveryNoteChangeRequest vbDeliveryNoteChangeRequest;
	private String presentPayable;
	private String previousCredit;
	private String presentAdvance;
	private String totalPayable;
	private String presentPayment;
	private String balance;
	private String paymentType;
	private String chequeNo;
	private String bankName;
	private String bankLocation;
	private String branchName;

	public VbDeliveryNoteChangeRequestPayments() {
	}

	public VbDeliveryNoteChangeRequestPayments(
			VbDeliveryNoteChangeRequest vbDeliveryNoteChangeRequest,
			String presentPayable, String previousCredit,
			String presentAdvance, String totalPayable, String presentPayment,
			String balance, String paymentType) {
		this.vbDeliveryNoteChangeRequest = vbDeliveryNoteChangeRequest;
		this.presentPayable = presentPayable;
		this.previousCredit = previousCredit;
		this.presentAdvance = presentAdvance;
		this.totalPayable = totalPayable;
		this.presentPayment = presentPayment;
		this.balance = balance;
		this.paymentType = paymentType;
	}

	public VbDeliveryNoteChangeRequestPayments(
			VbDeliveryNoteChangeRequest vbDeliveryNoteChangeRequest,
			String presentPayable, String previousCredit,
			String presentAdvance, String totalPayable, String presentPayment,
			String balance, String paymentType, String chequeNo,
			String bankName, String bankLocation, String branchName) {
		this.vbDeliveryNoteChangeRequest = vbDeliveryNoteChangeRequest;
		this.presentPayable = presentPayable;
		this.previousCredit = previousCredit;
		this.presentAdvance = presentAdvance;
		this.totalPayable = totalPayable;
		this.presentPayment = presentPayment;
		this.balance = balance;
		this.paymentType = paymentType;
		this.chequeNo = chequeNo;
		this.bankName = bankName;
		this.bankLocation = bankLocation;
		this.branchName = branchName;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VbDeliveryNoteChangeRequest getVbDeliveryNoteChangeRequest() {
		return this.vbDeliveryNoteChangeRequest;
	}

	public void setVbDeliveryNoteChangeRequest(
			VbDeliveryNoteChangeRequest vbDeliveryNoteChangeRequest) {
		this.vbDeliveryNoteChangeRequest = vbDeliveryNoteChangeRequest;
	}

	public String getPresentPayable() {
		return this.presentPayable;
	}

	public void setPresentPayable(String presentPayable) {
		this.presentPayable = presentPayable;
	}

	public String getPreviousCredit() {
		return this.previousCredit;
	}

	public void setPreviousCredit(String previousCredit) {
		this.previousCredit = previousCredit;
	}

	public String getPresentAdvance() {
		return this.presentAdvance;
	}

	public void setPresentAdvance(String presentAdvance) {
		this.presentAdvance = presentAdvance;
	}

	public String getTotalPayable() {
		return this.totalPayable;
	}

	public void setTotalPayable(String totalPayable) {
		this.totalPayable = totalPayable;
	}

	public String getPresentPayment() {
		return this.presentPayment;
	}

	public void setPresentPayment(String presentPayment) {
		this.presentPayment = presentPayment;
	}

	public String getBalance() {
		return this.balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getChequeNo() {
		return this.chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankLocation() {
		return this.bankLocation;
	}

	public void setBankLocation(String bankLocation) {
		this.bankLocation = bankLocation;
	}

	public String getBranchName() {
		return this.branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

}
