package com.vekomy.vbooks.hibernate.model;

// Generated Nov 19, 2013 10:32:53 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * VbEmployeeCustomer generated by hbm2java
 */
public class VbEmployeeCustomer implements java.io.Serializable {

	private Integer id;
	private VbEmployee vbEmployee;
	private VbCustomer vbCustomer;
	private VbOrganization vbOrganization;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;

	public VbEmployeeCustomer() {
	}

	public VbEmployeeCustomer(VbEmployee vbEmployee, VbCustomer vbCustomer,
			VbOrganization vbOrganization, Date modifiedOn) {
		this.vbEmployee = vbEmployee;
		this.vbCustomer = vbCustomer;
		this.vbOrganization = vbOrganization;
		this.modifiedOn = modifiedOn;
	}

	public VbEmployeeCustomer(VbEmployee vbEmployee, VbCustomer vbCustomer,
			VbOrganization vbOrganization, Date createdOn, String createdBy,
			Date modifiedOn, String modifiedBy) {
		this.vbEmployee = vbEmployee;
		this.vbCustomer = vbCustomer;
		this.vbOrganization = vbOrganization;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VbEmployee getVbEmployee() {
		return this.vbEmployee;
	}

	public void setVbEmployee(VbEmployee vbEmployee) {
		this.vbEmployee = vbEmployee;
	}

	public VbCustomer getVbCustomer() {
		return this.vbCustomer;
	}

	public void setVbCustomer(VbCustomer vbCustomer) {
		this.vbCustomer = vbCustomer;
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

}
