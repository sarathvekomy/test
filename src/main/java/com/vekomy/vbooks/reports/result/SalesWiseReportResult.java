/**
 * com.vekomy.vbooks.reports.result.SalesWiseReportResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jun 12, 2013
 */
package com.vekomy.vbooks.reports.result;

import java.io.Serializable;
import java.util.Date;


/**
 * @author swarupa
 *
 */
public class SalesWiseReportResult implements Serializable{

	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -8243983221376044248L;
	/**
	 * String variable holds productName
	 */
	private String productName;
	/**
	 * String variable holds batchNumber
	 */
	private String batchNumber;
	/**
	 * int variable holds productQty
	 */
	private int productQty;
	/**
	 * Integer variable holds bonusQty
	 */
	private Integer bonusQty;
	/**
	 * String variable holds productCost
	 */
	private String productCost;
	/**
	 * String variable holds totalCost
	 */
	private String totalCost;
	/**
	 * String variable holds createdOn
	 */
	private String createdOn;
	
	/**
	 * String variable holds businessName
	 */
	private String businessName;
	/**
	 * String variable holds salesExecutive
	 */
	private String salesExecutive;
	/**
	 * String variable holds reportType
	 */
	private String reportType;
	/**
	 * String variable holds startDate
	 */
	private String startDate;
	/**
	 * String variable holds Date
	 */
	private String endDate;
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
	 * @return the productQty
	 */
	public int getProductQty() {
		return productQty;
	}
	/**
	 * @param productQty the productQty to set
	 */
	public void setProductQty(int productQty) {
		this.productQty = productQty;
	}
	/**
	 * @return the bonusQty
	 */
	public Integer getBonusQty() {
		return bonusQty;
	}
	/**
	 * @param bonusQty the bonusQty to set
	 */
	public void setBonusQty(Integer bonusQty) {
		this.bonusQty = bonusQty;
	}
	/**
	 * @return the productCost
	 */
	public String getProductCost() {
		return productCost;
	}
	/**
	 * @param productCost the productCost to set
	 */
	public void setProductCost(String productCost) {
		this.productCost = productCost;
	}
	/**
	 * @return the totalCost
	 */
	public String getTotalCost() {
		return totalCost;
	}
	/**
	 * @param totalCost the totalCost to set
	 */
	public void setTotalCost(String totalCost) {
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
	public String getSalesExecutive() {
		return salesExecutive;
	}
	public void setSalesExecutive(String salesExecutive) {
		this.salesExecutive = salesExecutive;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
