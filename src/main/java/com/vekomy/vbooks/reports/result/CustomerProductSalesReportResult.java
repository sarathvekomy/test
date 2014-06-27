/**
 * com.vekomy.vbooks.reports.result.CustomerProductSalesReportResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 18, 2014
 */
package com.vekomy.vbooks.reports.result;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ankit
 *
 */
public class CustomerProductSalesReportResult implements Serializable{
	
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 3012883360518837204L;
	/**
	 * String variable holds productName
	 */
	private String productName;
	/**
	 * Integer variable holds quantity
	 */
	private Integer quantity;
	/**
	 * Float variable holds sellingPrice
	 */
	private Float sellingPrice;
	/**
	 * Float variable holds salesValue
	 */
	private Float salesValue;
	/**
	 * Integer variable holds salesExecutive
	 */
	private Integer salesReturnQty;
	/**
	 * Integer variable holds salesReturnValue
	 */
	private Float salesReturnValue;
	
	// add variable for displaying report summary as header in admin product wise report.
	/**
	 * String variable holds customerName
	 */
	private String customerName;
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
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Float getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(Float sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	public Float getSalesValue() {
		return salesValue;
	}
	public void setSalesValue(Float salesValue) {
		this.salesValue = salesValue;
	}
	public Integer getSalesReturnQty() {
		return salesReturnQty;
	}
	public void setSalesReturnQty(Integer salesReturnQty) {
		this.salesReturnQty = salesReturnQty;
	}
	public Float getSalesReturnValue() {
		return salesReturnValue;
	}
	public void setSalesReturnValue(Float salesReturnValue) {
		this.salesReturnValue = salesReturnValue;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
	
	