package com.vekomy.vbooks.hibernate.model;

// Generated Sep 11, 2013 11:07:56 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * VbCustomer generated by hbm2java
 */
public class VbCustomer implements java.io.Serializable {

	private Integer id;
	private VbOrganization vbOrganization;
	private String businessName;
	private String invoiceName;
	private String customerName;
	private Character gender;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private Float creditLimit;
	private Integer creditOverdueDays;
	private String state;
	private Set vbProductCustomerCosts = new HashSet(0);
	private Set vbEmployeeCustomers = new HashSet(0);
	private Set vbCustomerDetails = new HashSet(0);

	public VbCustomer() {
	}

	public VbCustomer(VbOrganization vbOrganization, String businessName,
			String invoiceName, String customerName, Date modifiedOn,
			String state) {
		this.vbOrganization = vbOrganization;
		this.businessName = businessName;
		this.invoiceName = invoiceName;
		this.customerName = customerName;
		this.modifiedOn = modifiedOn;
		this.state = state;
	}

	public VbCustomer(VbOrganization vbOrganization, String businessName,
			String invoiceName, String customerName, Character gender,
			Date createdOn, String createdBy, Date modifiedOn,
			String modifiedBy, Float creditLimit, Integer creditOverdueDays,
			String state, Set vbProductCustomerCosts, Set vbEmployeeCustomers,
			Set vbCustomerDetails) {
		this.vbOrganization = vbOrganization;
		this.businessName = businessName;
		this.invoiceName = invoiceName;
		this.customerName = customerName;
		this.gender = gender;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.creditLimit = creditLimit;
		this.creditOverdueDays = creditOverdueDays;
		this.state = state;
		this.vbProductCustomerCosts = vbProductCustomerCosts;
		this.vbEmployeeCustomers = vbEmployeeCustomers;
		this.vbCustomerDetails = vbCustomerDetails;
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

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Character getGender() {
		return this.gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
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

	public Float getCreditLimit() {
		return this.creditLimit;
	}

	public void setCreditLimit(Float creditLimit) {
		this.creditLimit = creditLimit;
	}

	public Integer getCreditOverdueDays() {
		return this.creditOverdueDays;
	}

	public void setCreditOverdueDays(Integer creditOverdueDays) {
		this.creditOverdueDays = creditOverdueDays;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Set getVbProductCustomerCosts() {
		return this.vbProductCustomerCosts;
	}

	public void setVbProductCustomerCosts(Set vbProductCustomerCosts) {
		this.vbProductCustomerCosts = vbProductCustomerCosts;
	}

	public Set getVbEmployeeCustomers() {
		return this.vbEmployeeCustomers;
	}

	public void setVbEmployeeCustomers(Set vbEmployeeCustomers) {
		this.vbEmployeeCustomers = vbEmployeeCustomers;
	}

	public Set getVbCustomerDetails() {
		return this.vbCustomerDetails;
	}

	public void setVbCustomerDetails(Set vbCustomerDetails) {
		this.vbCustomerDetails = vbCustomerDetails;
	}

}
