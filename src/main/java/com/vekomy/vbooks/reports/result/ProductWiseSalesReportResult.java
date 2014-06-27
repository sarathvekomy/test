/**
 * com.vekomy.vbooks.reports.result.ProductWiseSalesReportResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 8, 2014
 */
package com.vekomy.vbooks.reports.result;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ankit
 *
 */
public class ProductWiseSalesReportResult implements Serializable{
	
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 3012883360518837204L;
	
	/**
	 * String variable holds invoiceNumber
	 */
	private String invoiceNumber;
	/**
	 * String variable holds production
	 */
	private String productName;
	/**
	 * Integer variable holds salesQty
	 */
	private Integer salesQty;
	/**
	 * Float variable holds salesAmount
	 */
	private Float salesAmount;
	/**
	 * Float variable holds totalSalesValue
	 */
	private Float totalSalesValue;
	/**
	 * Float variable holds spotCollection
	 */
	private Float spotCollection;
	/**
	 * Float variable holds currentSalesBalance
	 */
	private Float currentSalesBalance;

	// add variable for displaying report summary as header in admin product wise report.
	/**
	 * String variable holds productNameSummary
	 */
	private String salesExecutive;
	/**
	 * String variable holds reportTypeSummary
	 */
	private String reportTypeSummary;
	/**
	 * String variable holds startDate
	 */
	private String startDate;
	/**
	 * String variable holds endDate
	 */
	private String endDate;
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getSalesQty() {
		return salesQty;
	}
	public void setSalesQty(Integer salesQty) {
		this.salesQty = salesQty;
	}
	public Float getSalesAmount() {
		return salesAmount;
	}
	public void setSalesAmount(Float salesAmount) {
		this.salesAmount = salesAmount;
	}
	public Float getSpotCollection() {
		return spotCollection;
	}
	public void setSpotCollection(Float spotCollection) {
		this.spotCollection = spotCollection;
	}
	public Float getCurrentSalesBalance() {
		return currentSalesBalance;
	}
	public void setCurrentSalesBalance(Float currentSalesBalance) {
		this.currentSalesBalance = currentSalesBalance;
	}
	
	public String getSalesExecutive() {
		return salesExecutive;
	}
	public void setSalesExecutive(String salesExecutive) {
		this.salesExecutive = salesExecutive;
	}
	public String getReportTypeSummary() {
		return reportTypeSummary;
	}
	public void setReportTypeSummary(String reportTypeSummary) {
		this.reportTypeSummary = reportTypeSummary;
	}
	public Float getTotalSalesValue() {
		return totalSalesValue;
	}
	public void setTotalSalesValue(Float totalSalesValue) {
		this.totalSalesValue = totalSalesValue;
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
	
	