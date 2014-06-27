/**
 * com.vekomy.vbooks.mysales.command.DayBookResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: May 10, 2013
 */
package com.vekomy.vbooks.mysales.command;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * This Result class is responsible to store the results.
 * 
 * @author Swarupa.
 * 
 */
public class DayBookResult implements Serializable{
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -3700975108532436813L;
	/**
	 * Integer variable holds id
	 */
	private Integer id;
	/**
	 * String variable holds invoiceNo
	 */
	private String invoiceNo;
	/**
	 * String variable holds salesExecutive.
	 */
	private String salesExecutive;
	
	/**
	 * Date variable holds createdOn.
	 */
	private Date createdOn;
	/**
	 * Float variable holds presentPayable
	 */
	private String presentPayable;
	
	/**
	 * Float variable holds presentPayment
	 */
	private String presentPayment;
	/**
	 * String variable holds productName
	 */
	private String productName;
	/**
	 * String variable holds batchNumber
	 */
	private String batchNumber;
	/**
	 * Integer variable holds openingStock
	 */
	private Integer openingStock;
	/**
	 * Integer variable holds productsToCustomer
	 */
	private Integer productsToCustomer;
	
	/**
	 * Integer variable holds closingStock
	 */
	private Integer closingStock;
	/**
	 * Integer variable holds returnQty.
	 */
	private Integer returnQty;
	/**
	 * Integer variable holds productsToFactory.
	 */
	private Integer productsToFactory;
	/**
	 * List<DayBookResult> variable holds proBookResults.
	 */
	private List<DayBookResult> productResults;
	/**
	 * String variable holds reportingManager.
	 */
	private String reportingManager;
	/**
	 * String variable holds startDate.
	 */
	private String startDate;
	/**
	 * Float variable holds openingBalance.
	 */
	private Float openingBalance;
	/**
	 * DayBookResult variable holds customerTotalPayable.
	 */
	private DayBookResult customerTotalPayable;
	/**
	 * Boolean variable holds allotmentType.
	 */
	private Boolean allotmentType;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the invoiceNo
	 */
	public String getInvoiceNo() {
		return invoiceNo;
	}
	/**
	 * @param invoiceNo the invoiceNo to set
	 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	/**
	 * @return the presentPayable
	 */
	public String getPresentPayable() {
		return presentPayable;
	}
	/**
	 * @param presentPayable the presentPayable to set
	 */
	public void setPresentPayable(String presentPayable) {
		this.presentPayable = presentPayable;
	}
	/**
	 * @return the presentPayment
	 */
	public String getPresentPayment() {
		return presentPayment;
	}
	/**
	 * @param presentPayment the presentPayment to set
	 */
	public void setPresentPayment(String presentPayment) {
		this.presentPayment = presentPayment;
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
	 * @return the openingStock
	 */
	public Integer getOpeningStock() {
		return openingStock;
	}
	/**
	 * @param openingStock the openingStock to set
	 */
	public void setOpeningStock(Integer openingStock) {
		this.openingStock = openingStock;
	}
	/**
	 * @return the productsToCustomer
	 */
	public Integer getProductsToCustomer() {
		return productsToCustomer;
	}
	/**
	 * @param productsToCustomer the productsToCustomer to set
	 */
	public void setProductsToCustomer(Integer productsToCustomer) {
		this.productsToCustomer = productsToCustomer;
	}
	/**
	 * @return the closingStock
	 */
	public Integer getClosingStock() {
		return closingStock;
	}
	/**
	 * @param closingStock the closingStock to set
	 */
	public void setClosingStock(Integer closingStock) {
		this.closingStock = closingStock;
	}
	/**
	 * @return the returnQty
	 */
	public Integer getReturnQty() {
		return returnQty;
	}
	/**
	 * @param returnQty the returnQty to set
	 */
	public void setReturnQty(Integer returnQty) {
		this.returnQty = returnQty;
	}
	/**
	 * @return the productsToFactory
	 */
	public Integer getProductsToFactory() {
		return productsToFactory;
	}
	/**
	 * @param productsToFactory the productsToFactory to set
	 */
	public void setProductsToFactory(Integer productsToFactory) {
		this.productsToFactory = productsToFactory;
	}
	/**
	 * @return the productResults
	 */
	public List<DayBookResult> getProductResults() {
		return productResults;
	}
	/**
	 * @param productResults the productResults to set
	 */
	public void setProductResults(List<DayBookResult> productResults) {
		this.productResults = productResults;
	}
	/**
	 * @return the reportingManager
	 */
	public String getReportingManager() {
		return reportingManager;
	}
	/**
	 * @param reportingManager the reportingManager to set
	 */
	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the openingBalance
	 */
	public Float getOpeningBalance() {
		return openingBalance;
	}
	/**
	 * @param openingBalance the openingBalance to set
	 */
	public void setOpeningBalance(Float openingBalance) {
		this.openingBalance = openingBalance;
	}
	/**
	 * @return the customerTotalPayable
	 */
	public DayBookResult getCustomerTotalPayable() {
		return customerTotalPayable;
	}
	/**
	 * @param customerTotalPayable the customerTotalPayable to set
	 */
	public void setCustomerTotalPayable(DayBookResult customerTotalPayable) {
		this.customerTotalPayable = customerTotalPayable;
	}
	/**
	 * @return the allotmentType
	 */
	public Boolean getAllotmentType() {
		return allotmentType;
	}
	/**
	 * @param allotmentType the allotmentType to set
	 */
	public void setAllotmentType(Boolean allotmentType) {
		this.allotmentType = allotmentType;
	}
	
}
