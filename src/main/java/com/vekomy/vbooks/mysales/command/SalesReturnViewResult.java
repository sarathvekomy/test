/**
 * com.vekomy.vbooks.mysales.command.SalesReturnViewResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: May 13, 2013
 */
package com.vekomy.vbooks.mysales.command;

import java.util.Date;

import com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts;

/**
 * @author Sudhakar
 *
 */
public class SalesReturnViewResult extends VbSalesReturnProducts implements Comparable<SalesReturnViewResult>{

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -2995187425359633980L;
	/**
	 * String variable holds invoiceNo
	 */
	/**
	 * String variable holds invoiceNo.
	 */
	private String invoiceNo;
	/**
	 * String variable holds resaleCost.
	 */
	private String resaleCost;
	/**
	 * String variable holds damageCost.
	 */
	private String damageCost;
	/**
	 * String variable holds businessName
	 */
	private String businessName;
	/**
	 * Date variable holds createdDate
	 */
	private String invoiceName;
	/**
	 * Float variable holds productsGrandTotal
	 */
	private String productsGrandTotal;
	/**
	 * String variable holds productsGrandTotal
	 */
	private String product;
	/**
	 * String variable holds productTotalCost.
	 */
	private String productTotalCost;
	/**
	 * String variable holds status
	 */
	private String status;
	/**
	 * String variable holds salesExecutive.
	 */
	private String salesExecutive;
	/**
	 * String variable holds productCost.
	 */
	private String productCost;
	
	/**
	 * String variable holds remarks.
	 */
	private String remarks;
	
	/**
	 * String variable holds CrDescription.
	 */
	private String CrDescription;
	/**
	 * String variable holds createdString.
	 */
	private String createdDate;
	
	/**
	 * Date variable holds createdOn
	 */
	private Date createdOn;
	
	/**
	 * String variable holds damagedQty
	 */
	private String damagedQty;
	
	/**
	 * String variable holds resaleQty
	 */
	private String resaleQty;
	
	/**
	 * String variable holds totalProductQty
	 */
	private String totalProductQty;
	
	/**
	 * String variable holds createdBy
	 */
	private String createdBy;
	
	/** 
	 * @return createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return damagedQty
	 */
	public String getDamagedQty() {
		return damagedQty;
	}

	/**
	 * @param damagedQty
	 */
	public void setDamagedQty(String damagedQty) {
		this.damagedQty = damagedQty;
	}

	/**
	 * @return resaleQty
	 */
	public String getResaleQty() {
		return resaleQty;
	}

	/**
	 * @param resaleQty
	 */
	public void setResaleQty(String resaleQty) {
		this.resaleQty = resaleQty;
	}

	/**
	 * @return totalProductQty
	 */
	public String getTotalProductQty() {
		return totalProductQty;
	}

	/**
	 * @param totalProductQty
	 */
	public void setTotalProductQty(String totalProductQty) {
		this.totalProductQty = totalProductQty;
	}

	/**
	 * @return createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the resaleCost
	 */
	public String getResaleCost() {
		return resaleCost;
	}

	public String getCrDescription() {
		return CrDescription;
	}

	public void setCrDescription(String crDescription) {
		CrDescription = crDescription;
	}

	/**
	 * @param resaleCost the resaleCost to set
	 */
	public void setResaleCost(String resaleCost) {
		this.resaleCost = resaleCost;
	}

	/**
	 * @return the damageCost
	 */
	public String getDamageCost() {
		return damageCost;
	}

	/**
	 * @param damageCost the damageCost to set
	 */
	public void setDamageCost(String damageCost) {
		this.damageCost = damageCost;
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
	 * @return the invoiceName
	 */
	public String getInvoiceName() {
		return invoiceName;
	}

	/**
	 * @param invoiceName the invoiceName to set
	 */
	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	/**
	 * @return the productsGrandTotal
	 */
	public String getProductsGrandTotal() {
		return productsGrandTotal;
	}

	/**
	 * @param productsGrandTotal the productsGrandTotal to set
	 */
	public void setProductsGrandTotal(String productsGrandTotal) {
		this.productsGrandTotal = productsGrandTotal;
	}

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the productTotalCost
	 */
	public String getProductTotalCost() {
		return productTotalCost;
	}

	/**
	 * @param productTotalCost the productTotalCost to set
	 */
	public void setProductTotalCost(String productTotalCost) {
		this.productTotalCost = productTotalCost;
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
	 * @return the productCost
	 */
	public String getProductCost() {
		return productCost;
	}

	/**
	 * @param productCost the productCost to set
	 */
	public void setProductCost(String productCost) {
		this.productCost = productCost;
	}
	
	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public int compareTo(SalesReturnViewResult result) {
		int comparision=this.product.compareTo(result.getProduct()); 
		return comparision;
	}

}
