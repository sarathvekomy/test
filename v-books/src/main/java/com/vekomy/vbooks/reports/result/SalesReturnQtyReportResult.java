/**
 * com.vekomy.vbooks.reports.result.SalesReturnQtyReportResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Nov 6, 2013
 */
package com.vekomy.vbooks.reports.result;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Swarupa
 *
 */
public class SalesReturnQtyReportResult implements Serializable{

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 1334821927873853264L;
	
	/**
	 * String variable holds createdBy
	 */
	private String createdBy;
	/**
	 * Date variable holds createdDate
	 */
	private Date createdDate;
	/**
	 * String variable holds approvedBy
	 */
	private String approvedBy;
	/**
	 * Date variable holds approvedDate
	 */
	private Date approvedDate;
	/**
	 * String variable holds businessName
	 */
	private String businessName;
	/**
	 * String variable holds invoiceNo
	 */
	private String invoiceNo;
	/**
	 * String variable holds status
	 */
	private String status;
	/**
	 * String variable holds productName
	 */
	private String productName;
	/**
	 * String variable holds batchNumber
	 */
	private String batchNumber;
	/**
	 * Integer variable holds damagedQty
	 */
	private Integer damagedQty;
	/**
	 * Integer variable holds resalableQty
	 */
	private Integer resalableQty;
	/**
	 * Integer variable holds totalQty
	 */
	private Integer totalQty;
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the approvedBy
	 */
	public String getApprovedBy() {
		return approvedBy;
	}
	/**
	 * @param approvedBy the approvedBy to set
	 */
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	/**
	 * @return the approvedDate
	 */
	public Date getApprovedDate() {
		return approvedDate;
	}
	/**
	 * @param approvedDate the approvedDate to set
	 */
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the totalQty
	 */
	public Integer getTotalQty() {
		return totalQty;
	}
	/**
	 * @param totalQty the totalQty to set
	 */
	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}
 
}
