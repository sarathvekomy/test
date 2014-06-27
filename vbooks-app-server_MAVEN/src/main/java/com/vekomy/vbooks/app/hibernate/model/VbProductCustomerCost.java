package com.vekomy.vbooks.app.hibernate.model;

// Generated Jul 4, 2013 9:22:12 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * VbProductCustomerCost generated by hbm2java
 */
public class VbProductCustomerCost implements java.io.Serializable {

	private Integer id;
	private VbCustomer vbCustomer;
	private VbProduct vbProduct;
	private VbOrganization vbOrganization;
	private float cost;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;

	public VbProductCustomerCost() {
	}

	public VbProductCustomerCost(VbCustomer vbCustomer, VbProduct vbProduct,
			VbOrganization vbOrganization, float cost, Date modifiedOn) {
		this.vbCustomer = vbCustomer;
		this.vbProduct = vbProduct;
		this.vbOrganization = vbOrganization;
		this.cost = cost;
		this.modifiedOn = modifiedOn;
	}

	public VbProductCustomerCost(VbCustomer vbCustomer, VbProduct vbProduct,
			VbOrganization vbOrganization, float cost, Date createdOn,
			String createdBy, Date modifiedOn, String modifiedBy) {
		this.vbCustomer = vbCustomer;
		this.vbProduct = vbProduct;
		this.vbOrganization = vbOrganization;
		this.cost = cost;
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

	public VbCustomer getVbCustomer() {
		return this.vbCustomer;
	}

	public void setVbCustomer(VbCustomer vbCustomer) {
		this.vbCustomer = vbCustomer;
	}

	public VbProduct getVbProduct() {
		return this.vbProduct;
	}

	public void setVbProduct(VbProduct vbProduct) {
		this.vbProduct = vbProduct;
	}

	public VbOrganization getVbOrganization() {
		return this.vbOrganization;
	}

	public void setVbOrganization(VbOrganization vbOrganization) {
		this.vbOrganization = vbOrganization;
	}

	public float getCost() {
		return this.cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
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
