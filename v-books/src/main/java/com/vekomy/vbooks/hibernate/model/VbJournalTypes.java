package com.vekomy.vbooks.hibernate.model;

// Generated Jul 16, 2013 11:21:50 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * VbJournalTypes generated by hbm2java
 */
public class VbJournalTypes implements java.io.Serializable {

	private Integer id;
	private VbOrganization vbOrganization;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private String journalType;
	private String invoiceNo;
	private String description;

	public VbJournalTypes() {
	}

	public VbJournalTypes(VbOrganization vbOrganization, Date modifiedOn,
			String journalType) {
		this.vbOrganization = vbOrganization;
		this.modifiedOn = modifiedOn;
		this.journalType = journalType;
	}

	public VbJournalTypes(VbOrganization vbOrganization, Date createdOn,
			String createdBy, Date modifiedOn, String modifiedBy,
			String journalType, String invoiceNo, String description) {
		this.vbOrganization = vbOrganization;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.journalType = journalType;
		this.invoiceNo = invoiceNo;
		this.description = description;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getJournalType() {
		return this.journalType;
	}

	public void setJournalType(String journalType) {
		this.journalType = journalType;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
