/**
 * com.vekomy.vbooks.reports.result.SalesExecutiveExpenditureReportResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Dec 20, 2013
 */
package com.vekomy.vbooks.reports.result;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Swarupa
 *
 */
public class SalesExecutiveExpenditureReportResult implements Serializable{

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -603417840644518902L;

	/**
	 * String variable holds expensesType
	 */
	private String expensesType;
	/**
	 * String variable holds details
	 */
	private String details;
	/**
	 * Float variable holds amount
	 */
	private Float amount;
	/**
	 * String variable holds createdOn
	 */
	private Date createdOn;
	
	/**
	 * String variable holds salesExecutive
	 */
	private String salesExecutive;
	/**
	 * String variable holds reportType
	 */
	private String reportType;
	/**
	 * String variable holds expenceTypeSummary
	 */
	private String expenceTypeSummary;
	/**
	 * String variable holds startDate
	 */
	private String startDate;
	/**
	 * String variable holds endDate
	 */
	private String endDate;
	
	
	/**
	 * @return the expensesType
	 */
	public String getExpensesType() {
		return expensesType;
	}
	/**
	 * @param expensesType the expensesType to set
	 */
	public void setExpensesType(String expensesType) {
		this.expensesType = expensesType;
	}
	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}
	/**
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}
	/**
	 * @return the amount
	 */
	public Float getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Float amount) {
		this.amount = amount;
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
	public String getExpenceTypeSummary() {
		return expenceTypeSummary;
	}
	public void setExpenceTypeSummary(String expenceTypeSummary) {
		this.expenceTypeSummary = expenceTypeSummary;
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
