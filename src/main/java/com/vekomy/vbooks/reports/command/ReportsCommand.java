/**
 * com.vekomy.vbooks.mysales.command.TodaySalesReportCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: May 28, 2013
 */
package com.vekomy.vbooks.reports.command;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Swarupa
 * 
 * 
 */
public class ReportsCommand implements Serializable{
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 5437934744784451372L;
	/**
	 * String variable holds action
	 */
	private String action;
	/**
	 * String variable holds productName
	 */
	private String productName;
	/**
	 * String variable holds batchNumber
	 */
	private String businessName;
	/**
	 * String variable holds startDate
	 */
	private Date startDate;
	/**
	 * String variable holds endDate
	 */
	private Date endDate;
	/**
	 * String variable holds reportType
	 */
	private String reportType;
	/**
	 * String variable holds reportFormat
	 */
	private String reportFormat;
	/**
	 * String variable holds salesExecutive
	 */
	private String salesExecutive;
	/**
	 * String variable holds organization
	 */
	private String organization;
	/**
	 * String variable holds batchNumer
	 */
	private String batchNumber;
	/**
	 * String variable holds crStatus
	 */
	private String crStatus;
	/**
	 * String variable holds resalableOperator
	 */
	private String resalableOperator;
	/**
	 * Integer variable holds resalableQty
	 */
	private Integer resalableQty;
	/**
	 * String variable holds damagedOperator
	 */
	private String damagedOperator;
	/**
	 * Integer variable holds damagedQty
	 */
	private Integer damagedQty;
	
	/**
	 * String variable holds allowanceType
	 */
	private String allowanceType;
	
	private String headerStartDate;
	
	private String headerEndDate;
	
	/**
	 * VbOrganization variable holds organization
	 */
	//private VbOrganization organization;
	
	// add variable for SLE Customer wise Sales new report
	/**
	 * String variable holds customerName
	 */
	private String customerName;
	/**
	 * String variable holds region
	 */
	private String region;
	
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}
	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	/**
	 * @return the reportFormat
	 */
	public String getReportFormat() {
		return reportFormat;
	}
	/**
	 * @param reportFormat the reportFormat to set
	 */
	public void setReportFormat(String reportFormat) {
		this.reportFormat = reportFormat;
	}
	/**
	 * @return the salesExecutive
	 */
	public String getSalesExecutive() {
		return salesExecutive;
	}
	/**
	 * @param salesExecutive the salesExecutive to set
	 */
	public void setSalesExecutive(String salesExecutive) {
		this.salesExecutive = salesExecutive;
	}
	/**
	 * @return the organization
	 */
	public String getOrganization() {
		return organization;
	}
	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	/**
	 * @return the crStatus
	 */
	public String getCrStatus() {
		return crStatus;
	}
	/**
	 * @param crStatus the crStatus to set
	 */
	public void setCrStatus(String crStatus) {
		this.crStatus = crStatus;
	}
	/**
	 * @return the resalableOperator
	 */
	public String getResalableOperator() {
		return resalableOperator;
	}
	/**
	 * @param resalableOperator the resalableOperator to set
	 */
	public void setResalableOperator(String resalableOperator) {
		this.resalableOperator = resalableOperator;
	}
	/**
	 * @return the resalableQty
	 */
	public Integer getResalableQty() {
		return resalableQty;
	}
	/**
	 * @param resalableQty the resalableQty to set
	 */
	public void setResalableQty(Integer resalableQty) {
		this.resalableQty = resalableQty;
	}
	/**
	 * @return the damagedOperator
	 */
	public String getDamagedOperator() {
		return damagedOperator;
	}
	/**
	 * @param damagedOperator the damagedOperator to set
	 */
	public void setDamagedOperator(String damagedOperator) {
		this.damagedOperator = damagedOperator;
	}
	/**
	 * @return the damagedQty
	 */
	public Integer getDamagedQty() {
		return damagedQty;
	}
	/**
	 * @param damagedQty the damagedQty to set
	 */
	public void setDamagedQty(Integer damagedQty) {
		this.damagedQty = damagedQty;
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
	 * @return the allowanceType
	 */
	public String getAllowanceType() {
		return allowanceType;
	}
	/**
	 * @param allowanceType the allowanceType to set
	 */
	public void setAllowanceType(String allowanceType) {
		this.allowanceType = allowanceType;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getHeaderStartDate() {
		return headerStartDate;
	}
	public void setHeaderStartDate(String headerStartDate) {
		this.headerStartDate = headerStartDate;
	}
	public String getHeaderEndDate() {
		return headerEndDate;
	}
	public void setHeaderEndDate(String headerEndDate) {
		this.headerEndDate = headerEndDate;
	}
	

}
