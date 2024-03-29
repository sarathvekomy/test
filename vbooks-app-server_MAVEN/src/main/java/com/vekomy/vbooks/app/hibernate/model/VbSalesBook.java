package com.vekomy.vbooks.app.hibernate.model;

// Generated Jul 4, 2013 9:22:12 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * VbSalesBook generated by hbm2java
 */
public class VbSalesBook implements java.io.Serializable {

	private Integer id;
	private VbOrganization vbOrganization;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private String salesExecutive;
	private Float advance;
	private Float openingBalance;
	private Float closingBalance;
	private Integer flag;
	private Integer cycleId;
	private String allotmentType;
	private Set vbDayBookChangeRequests = new HashSet(0);
	private Set vbJournals = new HashSet(0);
	private Set vbDayBooks = new HashSet(0);
	private Set vbDeliveryNotes = new HashSet(0);
	private Set vbDeliveryNoteChangeRequests = new HashSet(0);
	private Set vbSalesReturns = new HashSet(0);
	private Set vbSalesBookProductses = new HashSet(0);
	private Set vbSalesReturnChangeRequests = new HashSet(0);

	public VbSalesBook() {
	}

	public VbSalesBook(VbOrganization vbOrganization, Date modifiedOn,
			String salesExecutive, String allotmentType) {
		this.vbOrganization = vbOrganization;
		this.modifiedOn = modifiedOn;
		this.salesExecutive = salesExecutive;
		this.allotmentType = allotmentType;
	}

	public VbSalesBook(VbOrganization vbOrganization, Date createdOn,
			String createdBy, Date modifiedOn, String modifiedBy,
			String salesExecutive, Float advance, Float openingBalance,
			Float closingBalance, Integer flag, Integer cycleId,
			String allotmentType, Set vbDayBookChangeRequests, Set vbJournals,
			Set vbDayBooks, Set vbDeliveryNotes,
			Set vbDeliveryNoteChangeRequests, Set vbSalesReturns,
			Set vbSalesBookProductses, Set vbSalesReturnChangeRequests) {
		this.vbOrganization = vbOrganization;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.salesExecutive = salesExecutive;
		this.advance = advance;
		this.openingBalance = openingBalance;
		this.closingBalance = closingBalance;
		this.flag = flag;
		this.cycleId = cycleId;
		this.allotmentType = allotmentType;
		this.vbDayBookChangeRequests = vbDayBookChangeRequests;
		this.vbJournals = vbJournals;
		this.vbDayBooks = vbDayBooks;
		this.vbDeliveryNotes = vbDeliveryNotes;
		this.vbDeliveryNoteChangeRequests = vbDeliveryNoteChangeRequests;
		this.vbSalesReturns = vbSalesReturns;
		this.vbSalesBookProductses = vbSalesBookProductses;
		this.vbSalesReturnChangeRequests = vbSalesReturnChangeRequests;
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

	public String getSalesExecutive() {
		return this.salesExecutive;
	}

	public void setSalesExecutive(String salesExecutive) {
		this.salesExecutive = salesExecutive;
	}

	public Float getAdvance() {
		return this.advance;
	}

	public void setAdvance(Float advance) {
		this.advance = advance;
	}

	public Float getOpeningBalance() {
		return this.openingBalance;
	}

	public void setOpeningBalance(Float openingBalance) {
		this.openingBalance = openingBalance;
	}

	public Float getClosingBalance() {
		return this.closingBalance;
	}

	public void setClosingBalance(Float closingBalance) {
		this.closingBalance = closingBalance;
	}

	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getCycleId() {
		return this.cycleId;
	}

	public void setCycleId(Integer cycleId) {
		this.cycleId = cycleId;
	}

	public String getAllotmentType() {
		return this.allotmentType;
	}

	public void setAllotmentType(String allotmentType) {
		this.allotmentType = allotmentType;
	}

	public Set getVbDayBookChangeRequests() {
		return this.vbDayBookChangeRequests;
	}

	public void setVbDayBookChangeRequests(Set vbDayBookChangeRequests) {
		this.vbDayBookChangeRequests = vbDayBookChangeRequests;
	}

	public Set getVbJournals() {
		return this.vbJournals;
	}

	public void setVbJournals(Set vbJournals) {
		this.vbJournals = vbJournals;
	}

	public Set getVbDayBooks() {
		return this.vbDayBooks;
	}

	public void setVbDayBooks(Set vbDayBooks) {
		this.vbDayBooks = vbDayBooks;
	}

	public Set getVbDeliveryNotes() {
		return this.vbDeliveryNotes;
	}

	public void setVbDeliveryNotes(Set vbDeliveryNotes) {
		this.vbDeliveryNotes = vbDeliveryNotes;
	}

	public Set getVbDeliveryNoteChangeRequests() {
		return this.vbDeliveryNoteChangeRequests;
	}

	public void setVbDeliveryNoteChangeRequests(Set vbDeliveryNoteChangeRequests) {
		this.vbDeliveryNoteChangeRequests = vbDeliveryNoteChangeRequests;
	}

	public Set getVbSalesReturns() {
		return this.vbSalesReturns;
	}

	public void setVbSalesReturns(Set vbSalesReturns) {
		this.vbSalesReturns = vbSalesReturns;
	}

	public Set getVbSalesBookProductses() {
		return this.vbSalesBookProductses;
	}

	public void setVbSalesBookProductses(Set vbSalesBookProductses) {
		this.vbSalesBookProductses = vbSalesBookProductses;
	}

	public Set getVbSalesReturnChangeRequests() {
		return this.vbSalesReturnChangeRequests;
	}

	public void setVbSalesReturnChangeRequests(Set vbSalesReturnChangeRequests) {
		this.vbSalesReturnChangeRequests = vbSalesReturnChangeRequests;
	}

}
