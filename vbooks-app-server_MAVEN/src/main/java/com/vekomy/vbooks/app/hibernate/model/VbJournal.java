package com.vekomy.vbooks.app.hibernate.model;

// Generated Jul 4, 2013 9:22:12 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * VbJournal generated by hbm2java
 */
public class VbJournal implements java.io.Serializable {

	private Integer id;
	private VbSalesBook vbSalesBook;
	private VbOrganization vbOrganization;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private String businessName;
	private String invoiceNo;
	private boolean isApproved;
	private String journalType;
	private float amount;
	private String description;

	public VbJournal() {
	}

	public VbJournal(VbSalesBook vbSalesBook, VbOrganization vbOrganization,
			Date modifiedOn, String businessName, String invoiceNo,
			boolean isApproved, String journalType, float amount) {
		this.vbSalesBook = vbSalesBook;
		this.vbOrganization = vbOrganization;
		this.modifiedOn = modifiedOn;
		this.businessName = businessName;
		this.invoiceNo = invoiceNo;
		this.isApproved = isApproved;
		this.journalType = journalType;
		this.amount = amount;
	}

	public VbJournal(VbSalesBook vbSalesBook, VbOrganization vbOrganization,
			Date createdOn, String createdBy, Date modifiedOn,
			String modifiedBy, String businessName, String invoiceNo,
			boolean isApproved, String journalType, float amount,
			String description) {
		this.vbSalesBook = vbSalesBook;
		this.vbOrganization = vbOrganization;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.businessName = businessName;
		this.invoiceNo = invoiceNo;
		this.isApproved = isApproved;
		this.journalType = journalType;
		this.amount = amount;
		this.description = description;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VbSalesBook getVbSalesBook() {
		return this.vbSalesBook;
	}

	public void setVbSalesBook(VbSalesBook vbSalesBook) {
		this.vbSalesBook = vbSalesBook;
	}

	public VbOrganization getVbOrganization() {
		return this.vbOrganization;
	}

	public void setVbOrganization(VbOrganization vbOrganization) {
		this.vbOrganization = vbOrganization;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedOn() {
		return this.modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getBusinessName() {
		return this.businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public boolean isIsApproved() {
		return this.isApproved;
	}

	public void setIsApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getJournalType() {
		return this.journalType;
	}

	public void setJournalType(String journalType) {
		this.journalType = journalType;
	}

	public float getAmount() {
		return this.amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
