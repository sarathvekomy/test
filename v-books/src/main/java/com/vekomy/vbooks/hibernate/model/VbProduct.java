package com.vekomy.vbooks.hibernate.model;

// Generated Jul 16, 2013 11:21:50 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * VbProduct generated by hbm2java
 */
public class VbProduct implements java.io.Serializable {

	private Integer id;
	private VbOrganization vbOrganization;
	private String productName;
	private String batchNumber;
	private String productCategory;
	private String brand;
	private String model;
	private String description;
	private float costPerQuantity;
	private Integer quantityArrived;
	private Integer quantityAtWarehouse;
	private Integer availableQuantity;
	private Integer totalQuantity;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private Set vbProductCustomerCosts = new HashSet(0);

	public VbProduct() {
	}

	public VbProduct(VbOrganization vbOrganization, String productName,
			String batchNumber, String brand, String model,
			float costPerQuantity, Date modifiedOn) {
		this.vbOrganization = vbOrganization;
		this.productName = productName;
		this.batchNumber = batchNumber;
		this.brand = brand;
		this.model = model;
		this.costPerQuantity = costPerQuantity;
		this.modifiedOn = modifiedOn;
	}

	public VbProduct(VbOrganization vbOrganization, String productName,
			String batchNumber, String productCategory, String brand,
			String model, String description, float costPerQuantity,
			Integer quantityArrived, Integer quantityAtWarehouse,
			Integer availableQuantity, Integer totalQuantity, Date createdOn,
			String createdBy, Date modifiedOn, String modifiedBy,
			Set vbProductCustomerCosts) {
		this.vbOrganization = vbOrganization;
		this.productName = productName;
		this.batchNumber = batchNumber;
		this.productCategory = productCategory;
		this.brand = brand;
		this.model = model;
		this.description = description;
		this.costPerQuantity = costPerQuantity;
		this.quantityArrived = quantityArrived;
		this.quantityAtWarehouse = quantityAtWarehouse;
		this.availableQuantity = availableQuantity;
		this.totalQuantity = totalQuantity;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.vbProductCustomerCosts = vbProductCustomerCosts;
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

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBatchNumber() {
		return this.batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getProductCategory() {
		return this.productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getCostPerQuantity() {
		return this.costPerQuantity;
	}

	public void setCostPerQuantity(float costPerQuantity) {
		this.costPerQuantity = costPerQuantity;
	}

	public Integer getQuantityArrived() {
		return this.quantityArrived;
	}

	public void setQuantityArrived(Integer quantityArrived) {
		this.quantityArrived = quantityArrived;
	}

	public Integer getQuantityAtWarehouse() {
		return this.quantityAtWarehouse;
	}

	public void setQuantityAtWarehouse(Integer quantityAtWarehouse) {
		this.quantityAtWarehouse = quantityAtWarehouse;
	}

	public Integer getAvailableQuantity() {
		return this.availableQuantity;
	}

	public void setAvailableQuantity(Integer availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public Integer getTotalQuantity() {
		return this.totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
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

	public Set getVbProductCustomerCosts() {
		return this.vbProductCustomerCosts;
	}

	public void setVbProductCustomerCosts(Set vbProductCustomerCosts) {
		this.vbProductCustomerCosts = vbProductCustomerCosts;
	}

}