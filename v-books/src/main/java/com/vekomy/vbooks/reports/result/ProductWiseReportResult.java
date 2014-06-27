/**
 * com.vekomy.vbooks.mysales.command.ProductWiseReportResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jun 11, 2013
 */
package com.vekomy.vbooks.reports.result;

/**
 * @author swarupa
 *
 */
public class ProductWiseReportResult {
	
	/**
	 * String variable holds createdOn
	 */
	private String createdOn;
	/**
	 * String variable holds productName
	 */
	private String productName;
	/**
	 * String variable holds batchNumber
	 */
	private String batchNumber;
	/**
	 * Integer variable holds inwardsQty
	 */
	private Integer inwardsQty;
	/**
	 * Integer variable holds outwardsQty
	 */
	private Integer outwardsQty;
	/**
	 * Integer variable holds availableQty
	 */
	private Integer availableQty;
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
	/**
	 * @return the inwardsQty
	 */
	public Integer getInwardsQty() {
		return inwardsQty;
	}
	/**
	 * @param inwardsQty the inwardsQty to set
	 */
	public void setInwardsQty(Integer inwardsQty) {
		this.inwardsQty = inwardsQty;
	}
	/**
	 * @return the outwardsQty
	 */
	public Integer getOutwardsQty() {
		return outwardsQty;
	}
	/**
	 * @param outwardsQty the outwardsQty to set
	 */
	public void setOutwardsQty(Integer outwardsQty) {
		this.outwardsQty = outwardsQty;
	}
	/**
	 * @return the availableQty
	 */
	public Integer getAvailableQty() {
		return availableQty;
	}
	/**
	 * @param availableQty the availableQty to set
	 */
	public void setAvailableQty(Integer availableQty) {
		this.availableQty = availableQty;
	}

}
