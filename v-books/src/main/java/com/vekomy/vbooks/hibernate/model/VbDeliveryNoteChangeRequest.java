package com.vekomy.vbooks.hibernate.model;

// Generated Jul 16, 2013 11:21:50 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * VbDeliveryNoteChangeRequest generated by hbm2java
 */
public class VbDeliveryNoteChangeRequest implements java.io.Serializable {

	private Integer id;
	private VbSalesBook vbSalesBook;
	private VbOrganization vbOrganization;
	private String invoiceNo;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private String businessName;
	private String invoiceName;
	private String status;
	private String crDescription;
	private Set vbDeliveryNoteChangeRequestProductses = new HashSet(0);
	private Set vbDeliveryNoteChangeRequestPaymentses = new HashSet(0);

	public VbDeliveryNoteChangeRequest() {
	}

	public VbDeliveryNoteChangeRequest(VbSalesBook vbSalesBook,
			VbOrganization vbOrganization, String invoiceNo, Date modifiedOn,
			String businessName) {
		this.vbSalesBook = vbSalesBook;
		this.vbOrganization = vbOrganization;
		this.invoiceNo = invoiceNo;
		this.modifiedOn = modifiedOn;
		this.businessName = businessName;
	}

	public VbDeliveryNoteChangeRequest(VbSalesBook vbSalesBook,
			VbOrganization vbOrganization, String invoiceNo, Date createdOn,
			String createdBy, Date modifiedOn, String modifiedBy,
			String businessName, String invoiceName, String status,
			String crDescription, Set vbDeliveryNoteChangeRequestProductses,
			Set vbDeliveryNoteChangeRequestPaymentses) {
		this.vbSalesBook = vbSalesBook;
		this.vbOrganization = vbOrganization;
		this.invoiceNo = invoiceNo;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.businessName = businessName;
		this.invoiceName = invoiceName;
		this.status = status;
		this.crDescription = crDescription;
		this.vbDeliveryNoteChangeRequestProductses = vbDeliveryNoteChangeRequestProductses;
		this.vbDeliveryNoteChangeRequestPaymentses = vbDeliveryNoteChangeRequestPaymentses;
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

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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

	public String getInvoiceName() {
		return this.invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCrDescription() {
		return this.crDescription;
	}

	public void setCrDescription(String crDescription) {
		this.crDescription = crDescription;
	}

	public Set getVbDeliveryNoteChangeRequestProductses() {
		return this.vbDeliveryNoteChangeRequestProductses;
	}

	public void setVbDeliveryNoteChangeRequestProductses(
			Set vbDeliveryNoteChangeRequestProductses) {
		this.vbDeliveryNoteChangeRequestProductses = vbDeliveryNoteChangeRequestProductses;
	}

	public Set getVbDeliveryNoteChangeRequestPaymentses() {
		return this.vbDeliveryNoteChangeRequestPaymentses;
	}

	public void setVbDeliveryNoteChangeRequestPaymentses(
			Set vbDeliveryNoteChangeRequestPaymentses) {
		this.vbDeliveryNoteChangeRequestPaymentses = vbDeliveryNoteChangeRequestPaymentses;
	}

}