package com.vekomy.vbooks.hibernate.model;

// Generated Nov 19, 2013 10:32:53 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * VbCustomerCreditTransaction generated by hbm2java
 */
public class VbCustomerCreditTransaction implements java.io.Serializable {

	private Integer id;
	private VbOrganization vbOrganization;
	private String creditFrom;
	private String debitTo;
	private Float amount;
	private String businessName;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private Integer flag;

	public VbCustomerCreditTransaction() {
	}

	public VbCustomerCreditTransaction(VbOrganization vbOrganization,
			Date modifiedOn) {
		this.vbOrganization = vbOrganization;
		this.modifiedOn = modifiedOn;
	}

	public VbCustomerCreditTransaction(VbOrganization vbOrganization,
			String creditFrom, String debitTo, Float amount,
			String businessName, Date createdOn, String createdBy,
			Date modifiedOn, String modifiedBy, Integer flag) {
		this.vbOrganization = vbOrganization;
		this.creditFrom = creditFrom;
		this.debitTo = debitTo;
		this.amount = amount;
		this.businessName = businessName;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.flag = flag;
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

	public String getCreditFrom() {
		return this.creditFrom;
	}

	public void setCreditFrom(String creditFrom) {
		this.creditFrom = creditFrom;
	}

	public String getDebitTo() {
		return this.debitTo;
	}

	public void setDebitTo(String debitTo) {
		this.debitTo = debitTo;
	}

	public Float getAmount() {
		return this.amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public String getBusinessName() {
		return this.businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
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

	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}
