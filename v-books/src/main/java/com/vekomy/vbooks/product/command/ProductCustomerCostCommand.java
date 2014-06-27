package com.vekomy.vbooks.product.command;

import java.util.List;

import com.vekomy.vbooks.hibernate.model.VbProductCustomerCost;

/**
 * This command class is responsible for getting request from product customer
 * cost.
 * 
 * @author Ankit
 * 
 * 
 */

public class ProductCustomerCostCommand extends VbProductCustomerCost {

	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	public ProductCustomerCostCommand() {

	}

	public ProductCustomerCostCommand(Integer id) {
		this.id = id;
	}

	/**
	 * String variable holds action.
	 */
	private String action;

	/**
	 * String variable holds customer name.
	 */
	private String businessName;

	/**
	 * String variable holds product name.
	 */
	private String productName;
	
	/**
	 * String variable holds batch number.
	 */
	private String batchNumber;
	/**
	 * Integer variable holds product Id.
	 */
	private Integer id;

	/**
	 * List<Float> holds customer cost
	 */
	private List<Float> customerCost;

	/**
	 * List<String> holds product names
	 */
	private List<String> productNames;

	/**
	 * @return productNames
	 */
	public List<String> getProductNames() {
		return productNames;
	}

	/**
	 * @param productNames
	 *            the product names to set
	 */
	public void setProductNames(List<String> productNames) {
		this.productNames = productNames;
	}

	/**
	 * @return customer cost
	 */
	public List<Float> getCustomerCost() {
		return customerCost;
	}

	/**
	 * @param customerCost
	 *            customer cost to set
	 */
	public void setCustomerCost(List<Float> customerCost) {
		this.customerCost = customerCost;
	}

	/**
	 * @return the Id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param Id
	 *            the Id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the action
	 */

	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the customer name
	 */
	public String getBusinessName() {
		return businessName;
	}

	/**
	 * @param customerName
	 *            the customer name to set
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	/**
	 * @return the product name
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName
	 *            the product name to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return the batch number
	 */
	public String getBatchNumber() {
		return batchNumber;
	}
	/**
	 * @param batchNumber
	 *            the product batch number to set
	 */
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer("action :").append(getAction())
				.append(",productName :").append(getProductName())
				.append(", productNames :").append(getProductNames())
				.append(",businessName :").append(getBusinessName())
				.append(", cost :").append(getCost()).append(", id :")
				.append(getId()).append("]").toString();
	}

}
