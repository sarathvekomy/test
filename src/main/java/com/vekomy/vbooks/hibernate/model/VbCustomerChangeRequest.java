package com.vekomy.vbooks.hibernate.model;

// Generated Sep 11, 2013 11:07:56 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * VbCustomerChangeRequest generated by hbm2java
 */
public class VbCustomerChangeRequest implements java.io.Serializable {

	private Integer id;
	private VbOrganization vbOrganization;
	private String businessName;
	private Character gender;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private Boolean crType;
	private String status;
	private Integer flag;
	private Set vbCustomerChangeRequestDetailses = new HashSet(0);

	public VbCustomerChangeRequest() {
	}

	public VbCustomerChangeRequest(VbOrganization vbOrganization,
			String businessName, Date modifiedOn) {
		this.vbOrganization = vbOrganization;
		this.businessName = businessName;
		this.modifiedOn = modifiedOn;
	}

	public VbCustomerChangeRequest(VbOrganization vbOrganization,
			String businessName, Character gender, Date createdOn,
			String createdBy, Date modifiedOn, String modifiedBy,
			Boolean crType, String status, Integer flag,
			Set vbCustomerChangeRequestDetailses) {
		this.vbOrganization = vbOrganization;
		this.businessName = businessName;
		this.gender = gender;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.crType = crType;
		this.status = status;
		this.flag = flag;
		this.vbCustomerChangeRequestDetailses = vbCustomerChangeRequestDetailses;
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

	public Boolean getCrType() {
		return this.crType;
	}

	public void setCrType(Boolean crType) {
		this.crType = crType;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Set getVbCustomerChangeRequestDetailses() {
		return this.vbCustomerChangeRequestDetailses;
	}

	public void setVbCustomerChangeRequestDetailses(
			Set vbCustomerChangeRequestDetailses) {
		this.vbCustomerChangeRequestDetailses = vbCustomerChangeRequestDetailses;
	}

}
