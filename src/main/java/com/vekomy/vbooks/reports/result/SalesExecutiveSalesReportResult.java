/**
 * com.vekomy.vbooks.reports.result.SalesExecutiveSalesReportResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 12, 2014
 */
package com.vekomy.vbooks.reports.result;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ankit
 *
 */
public class SalesExecutiveSalesReportResult implements Serializable{
	
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 3012883360518837204L;
	
	/**
	 * String variable holds salesExecutive
	 */
	private String salesExecutive;
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
	 * Float variable holds discountValue
	 */
	private Float discountValue;
	/**
	 * Float variable holds closingBalance
	 */
	private Float closingBalance;

	// add variable for displaying report summary as header in admin product wise report.
	/**
	 * String variable holds productNameSummary
	 */
	private String salesExecutiveSummary;
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
	
	
	public String getSalesExecutive() {
		return salesExecutive;
	}
	public void setSalesExecutive(String salesExecutive) {
		this.salesExecutive = salesExecutive;
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
	public Float getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(Float closingBalance) {
		this.closingBalance = closingBalance;
	}
	public String getSalesExecutiveSummary() {
		return salesExecutiveSummary;
	}
	public void setSalesExecutiveSummary(String salesExecutiveSummary) {
		this.salesExecutiveSummary = salesExecutiveSummary;
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
	
	