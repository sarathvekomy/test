package com.vekomy.vbooks.product.command;

import java.util.Date;

import com.vekomy.vbooks.hibernate.model.VbProduct;

public class ProductCustomerCostViewResult extends VbProduct implements Comparable<ProductCustomerCostViewResult>{

	
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 3253121198951608555L;
	/**
	 * String variable holds product.
	 */
	private String product;
	/**
	 * String variable holds businessName.
	 */
	private String businessName;
	/**
	 * String variable holds invoiceName.
	 */
	private String invoiceName;
	/**
	 * String variable holds customerName.
	 */
	private String customerName;
	/**
	 * Character variable holds gender.
	 */
	private Character gender;
	/**
	 * String variable holds createdBy.
	 */
	private String createdBy;
	/**
	 * Date variable holds modifiedOn.
	 */
	private Date modifiedOn;
	/**
	 * String variable holds modifiedBy.
	 */
	private String modifiedBy;
	/**
	 * float variable holds cost.
	 */
	private float cost;
	
	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}
	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}
	/**
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}
	/**
	 * @param businessName the businessName to set
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	/**
	 * @return the invoiceName
	 */
	public String getInvoiceName() {
		return invoiceName;
	}
	/**
	 * @param invoiceName the invoiceName to set
	 */
	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the gender
	 */
	public Character getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(Character gender) {
		this.gender = gender;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the modifiedOn
	 */
	public Date getModifiedOn() {
		return modifiedOn;
	}
	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}
	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	/**
	 * @return the cost
	 */
	public float getCost() {
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(float cost) {
		this.cost = cost;
	}
	@Override
	public int compareTo(ProductCustomerCostViewResult result) {
		int comparision=this.product.compareTo(result.getProduct()); 
		return comparision;
	}
}
