/**
 * com.vekomy.vbooks.reports.result.SalesExecutiveSalesWiseReportResult.java
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
public class SalesExecutiveSalesWiseReportResult implements Serializable {

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 5997097278399095617L;
	
	/**
	 * String variable holds Date
	 */
	private String createdOn;
	/**
	 * String variable holds businessName
	 */
	private String businessName;
	/**
	 * String variable holds locality
	 */
	private String locality;
	/**
	 * String variable holds productName
	 */
	private String productName;
	/**
	 * Integer variable holds productQty
	 */
	private Integer productQty;
	/**
	 * Float variable holds soldValue
	 */
	private Float soldValue;
	/**
	 * Float variable holds oldBalance
	 */
	private Float oldBalance;
	/**
	 * Float variable holds total
	 */
	private Float total;
	/**
	 * Float variable holds totalReceived
	 */
	private Float totalReceived;
	/**
	 * Float variable holds credit
	 */
	private Float credit;
	/**
	 * String variable holds invoiceNo
	 */
	private String invoiceNo;
	
	/**
	 * String variable holds salesExecutive
	 */
	private String salesExecutive;
	/**
	 * String variable holds businessName
	 */
	private String businessNameSummary;
	/**
	 * String variable holds reportType
	 */
	private String reportType;
	/**
	 * String variable holds startDate
	 */
	private String startDate;
	/**
	 * String variable holds endDate
	 */
	private String endDate;
	
	
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
	/**
	 * @return the locality
	 */
	public String getLocality() {
		return locality;
	}
	/**
	 * @param locality the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
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
	 * @return the productQty
	 */
	public Integer getProductQty() {
		return productQty;
	}
	/**
	 * @param productQty the productQty to set
	 */
	public void setProductQty(Integer productQty) {
		this.productQty = productQty;
	}
	/**
	 * @return the soldValue
	 */
	public Float getSoldValue() {
		return soldValue;
	}
	/**
	 * @param soldValue the soldValue to set
	 */
	public void setSoldValue(Float soldValue) {
		this.soldValue = soldValue;
	}
	/**
	 * @return the oldBalance
	 */
	public Float getOldBalance() {
		return oldBalance;
	}
	/**
	 * @param oldBalance the oldBalance to set
	 */
	public void setOldBalance(Float oldBalance) {
		this.oldBalance = oldBalance;
	}
	/**
	 * @return the total
	 */
	public Float getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(Float total) {
		this.total = total;
	}
	/**
	 * @return the totalReceived
	 */
	public Float getTotalReceived() {
		return totalReceived;
	}
	/**
	 * @param totalReceived the totalReceived to set
	 */
	public void setTotalReceived(Float totalReceived) {
		this.totalReceived = totalReceived;
	}
	/**
	 * @return the credit
	 */
	public Float getCredit() {
		return credit;
	}
	/**
	 * @param credit the credit to set
	 */
	public void setCredit(Float credit) {
		this.credit = credit;
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
	public String getSalesExecutive() {
		return salesExecutive;
	}
	public void setSalesExecutive(String salesExecutive) {
		this.salesExecutive = salesExecutive;
	}
	public String getBusinessNameSummary() {
		return businessNameSummary;
	}
	public void setBusinessNameSummary(String businessNameSummary) {
		this.businessNameSummary = businessNameSummary;
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
