/**
 * com.vekomy.vbooks.reports.result.FactoryProductWiseReportResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 7, 2014
 */
package com.vekomy.vbooks.reports.result;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ankit
 *
 */
public class FactoryProductWiseReportResult implements Serializable{
	
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 3012883360518837204L;
	/**
	 * String variable holds createdOn
	 */
	private String createdOn;
	/**
	 * Integer variable holds production
	 */
	private Integer production;
	/**
	 * Integer variable holds dispatch
	 */
	private Integer dispatch;
	/**
	 * String variable holds dispatchNote
	 */
	private String dispatchNote;
	/**
	 * String variable holds salesExecutive
	 */
	private String salesExecutive;
	/**
	 * Integer variable holds salesExecutive
	 */
	private Integer salesReturnQty;
	
	/**
	 * Integer variable holds openingStock
	 */
	private Integer openingStock;
	/**
	 * Integer variable holds closingStock
	 */
	private Integer closingStock;
	
	// add variable for displaying report summary as header in admin product wise report.
	/**
	 * String variable holds productNameSummary
	 */
	private String productNameSummary;
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
	
	
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public Integer getProduction() {
		return production;
	}
	public void setProduction(Integer production) {
		this.production = production;
	}
	public Integer getDispatch() {
		return dispatch;
	}
	public void setDispatch(Integer dispatch) {
		this.dispatch = dispatch;
	}
	public String getDispatchNote() {
		return dispatchNote;
	}
	public void setDispatchNote(String dispatchNote) {
		this.dispatchNote = dispatchNote;
	}
	public String getSalesExecutive() {
		return salesExecutive;
	}
	public void setSalesExecutive(String salesExecutive) {
		this.salesExecutive = salesExecutive;
	}
	public Integer getSalesReturnQty() {
		return salesReturnQty;
	}
	public void setSalesReturnQty(Integer salesReturnQty) {
		this.salesReturnQty = salesReturnQty;
	}
	public String getProductNameSummary() {
		return productNameSummary;
	}
	public void setProductNameSummary(String productNameSummary) {
		this.productNameSummary = productNameSummary;
	}
	public String getReportTypeSummary() {
		return reportTypeSummary;
	}
	public void setReportTypeSummary(String reportTypeSummary) {
		this.reportTypeSummary = reportTypeSummary;
	}
	public Integer getOpeningStock() {
		return openingStock;
	}
	public void setOpeningStock(Integer openingStock) {
		this.openingStock = openingStock;
	}
	public Integer getClosingStock() {
		return closingStock;
	}
	public void setClosingStock(Integer closingStock) {
		this.closingStock = closingStock;
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
	
	