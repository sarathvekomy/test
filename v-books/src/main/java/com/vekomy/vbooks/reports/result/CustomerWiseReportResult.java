/**
 * com.vekomy.vbooks.mysales.command.CustomerWiseReportResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: June 03, 2013
 */
package com.vekomy.vbooks.reports.result;

import java.io.Serializable;


/**
 * This Result class is responsible to store the report results.
 * 
 * @author Swarupa.
 * 
 */
public class CustomerWiseReportResult implements Serializable{
	
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -7566591662546757274L;
	/**
	 * String variable holds productName
	 */
	private String productName;
	/**
	 * String variable holds batchNumber
	 */
	private String batchNumber;
	/**
	 * String variable holds businessName
	 */
	private String businessName;
	/**
	 * Integer variable holds productQty
	 */
	private int productQty;
	/**
	 * Float variable holds productCost
	 */
	private Float productCost;
	/**
	 * Float variable holds totalCost
	 */
	private Float totalCost;
	/**
	 * String variable holds createdOn
	 */
	private String createdOn;
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	/**
	 * @return the productQty
	 */
	public int getProductQty() {
		return productQty;
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
	 * @param productQty the productQty to set
	 */
	public void setProductQty(int productQty) {
		this.productQty = productQty;
	}
	/**
	 * @return the productCost
	 */
	public Float getProductCost() {
		return productCost;
	}
	/**
	 * @param productCost the productCost to set
	 */
	public void setProductCost(Float productCost) {
		this.productCost = productCost;
	}
	/**
	 * @return the totalCost
	 */
	public Float getTotalCost() {
		return totalCost;
	}
	/**
	 * @param totalCost the totalCost to set
	 */
	public void setTotalCost(Float totalCost) {
		this.totalCost = totalCost;
	}
	/**
	 * @return the createdOn
	 */
	public String getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	/**
	 * @return the batchNumber
	 */
	public String getBatchNumber() {
		return batchNumber;
	}
	/**
	 * @param batchNumber the batchNumber to set
	 */
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}


}
