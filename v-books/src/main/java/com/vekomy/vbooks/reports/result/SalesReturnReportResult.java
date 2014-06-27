/**
 * com.vekomy.vbooks.reports.result.SalesReturnReportResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 3, 2013
 */
package com.vekomy.vbooks.reports.result;

import java.io.Serializable;
import java.util.Date;

/**
 * @author swarupa
 *
 */
public class SalesReturnReportResult implements Serializable{
	
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 4932221268267006938L;
	/**
	 * Date variable holds createdOn
	 */
	private Date createdOn;
	/**
	 * String variable holds invoiceNo
	 */
	private String invoiceNo;
	/**
	 * Float variable holds totalCost
	 */
	private Float totalCost;
	
	/**
	 * String variable holds type
	 */
	private String type;
	
	/**
	 * String variable holds businessName
	 */
	private String businessName;
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
	 * @return the totalCost
	 */
	public Float getTotalCost() {
		return totalCost;
	}
	/**
	 * @param totalCost the totalCost to set
	 */
	public void setTotalCost(Float totalCost) {
		this.totalCost = totalCost;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	

}
