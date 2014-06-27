/**
 * com.vekomy.vbooks.reports.result.SLECustomerWiseSalesResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 10, 2014
 */
package com.vekomy.vbooks.reports.result;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ankit
 *
 */
public class SLECustomerWiseSalesResult implements Serializable{
	
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 3012883360518837204L;
	
	/**
	 * String variable holds customerName
	 */
	private String customerName;
	/**
	 * Float variable holds openingBalance
	 */
	private Float openingBalance;
	/**
	 * Float variable holds salesValue
	 */
	private Float salesValue;
	/**
	 * Float variable holds spotCollection
	 */
	private Float spotCollection;
	/**
	 * Float variable holds cashCollectionAmount
	 */
	private Float cashCollectionAmount;
	/**
	 * Float variable holds salesReturnValue
	 */
	private Float salesReturnValue;
	/**
	 * Float variable holds currentSalesBalance
	 */
	private Float discountValue;
	/**
	 * Float variable holds closingBalace
	 */
	private Float closingBalace;

	// add variable for displaying report summary as header in SLE Customer Wise Sales Report.
	/**
	 * String variable holds productNameSummary
	 */
	private String salesExecutive;
	/**
	 * String variable holds customerName
	 */
	private String customerNameSummary;
	/**
	 * String variable holds region
	 */
	private String region;
	/**
	 * String variable holds startDate
	 */
	private String startDate;
	/**
	 * String variable holds endDate
	 */
	private String endDate;
	
	private String reportTypeSummary;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Float getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(Float openingBalance) {
		this.openingBalance = openingBalance;
	}
	public Float getSalesValue() {
		return salesValue;
	}
	public void setSalesValue(Float salesValue) {
		this.salesValue = salesValue;
	}
	public Float getSpotCollection() {
		return spotCollection;
	}
	public void setSpotCollection(Float spotCollection) {
		this.spotCollection = spotCollection;
	}
	public Float getCashCollectionAmount() {
		return cashCollectionAmount;
	}
	public void setCashCollectionAmount(Float cashCollectionAmount) {
		this.cashCollectionAmount = cashCollectionAmount;
	}
	public Float getSalesReturnValue() {
		return salesReturnValue;
	}
	public void setSalesReturnValue(Float salesReturnValue) {
		this.salesReturnValue = salesReturnValue;
	}
	public Float getDiscountValue() {
		return discountValue;
	}
	public void setDiscountValue(Float discountValue) {
		this.discountValue = discountValue;
	}
	public Float getClosingBalace() {
		return closingBalace;
	}
	public void setClosingBalace(Float closingBalace) {
		this.closingBalace = closingBalace;
	}
	public String getSalesExecutive() {
		return salesExecutive;
	}
	public void setSalesExecutive(String salesExecutive) {
		this.salesExecutive = salesExecutive;
	}
	public String getCustomerNameSummary() {
		return customerNameSummary;
	}
	public void setCustomerNameSummary(String customerNameSummary) {
		this.customerNameSummary = customerNameSummary;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getReportTypeSummary() {
		return reportTypeSummary;
	}
	public void setReportTypeSummary(String reportTypeSummary) {
		this.reportTypeSummary = reportTypeSummary;
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
	
	