package com.vekomy.vbooks.app.hibernate.model;

// Generated Jul 4, 2013 9:22:12 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * VbDeliveryNote generated by hbm2java
 */
public class VbDeliveryNote implements java.io.Serializable {

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
	private Set vbDeliveryNotePaymentses = new HashSet(0);
	private Set vbDeliveryNoteProductses = new HashSet(0);

	public VbDeliveryNote() {
	}

	public VbDeliveryNote(VbSalesBook vbSalesBook,
			VbOrganization vbOrganization, String invoiceNo, Date modifiedOn,
			String businessName) {
		this.vbSalesBook = vbSalesBook;
		this.vbOrganization = vbOrganization;
		this.invoiceNo = invoiceNo;
		this.modifiedOn = modifiedOn;
		this.businessName = businessName;
	}

	public VbDeliveryNote(VbSalesBook vbSalesBook,
			VbOrganization vbOrganization, String invoiceNo, Date createdOn,
			String createdBy, Date modifiedOn, String modifiedBy,
			String businessName, String invoiceName,
			Set vbDeliveryNotePaymentses, Set vbDeliveryNoteProductses) {
		this.vbSalesBook = vbSalesBook;
		this.vbOrganization = vbOrganization;
		this.invoiceNo = invoiceNo;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.businessName = businessName;
		this.invoiceName = invoiceName;
		this.vbDeliveryNotePaymentses = vbDeliveryNotePaymentses;
		this.vbDeliveryNoteProductses = vbDeliveryNoteProductses;
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

	public Set getVbDeliveryNotePaymentses() {
		return this.vbDeliveryNotePaymentses;
	}

	public void setVbDeliveryNotePaymentses(Set vbDeliveryNotePaymentses) {
		this.vbDeliveryNotePaymentses = vbDeliveryNotePaymentses;
	}

	public Set getVbDeliveryNoteProductses() {
		return this.vbDeliveryNoteProductses;
	}

	public void setVbDeliveryNoteProductses(Set vbDeliveryNoteProductses) {
		this.vbDeliveryNoteProductses = vbDeliveryNoteProductses;
	}

}
