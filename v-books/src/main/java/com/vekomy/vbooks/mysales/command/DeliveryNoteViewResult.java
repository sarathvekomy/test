/**
 * com.vekomy.vbooks.mysales.command.DeliveryNoteViewResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 4, 2013
 */
package com.vekomy.vbooks.mysales.command;


import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts;
/**
 * @author Sudhakar
 *
 * 
 */
public class DeliveryNoteViewResult extends VbDeliveryNoteProducts implements Comparable<DeliveryNoteViewResult>{

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -6456072818634978950L;
	/**
	 * String variable holds product
	 */
	private String product;
	/**
	 * String variable holds costProduct
	 */
	private String costProduct;
	/**
	 * String variable holds totalCostProduct
	 */
	private String totalCostProduct;
	/**
	 * String variable holds qtyBonus
	 */
	private String qtyBonus;
	/**
	 * String variable holds qtyProduct
	 */
	private String qtyProduct;
	
	/**
	 * Integer variable holds salesBookId
	 */
	private Integer salesBookId;
	
	/**
	 * String variable holds crDescription
	 */
	private String crDescription;
	
	/**
	 * String variable holds grandTotal
	 */
	private String grandTotal;
	
	/**
	 * @return grandTotal
	 */
	public String getGrandTotal() {
		return grandTotal;
	}
	/**
	 * @param grandTotal
	 */
	public void setGrandTotal(String grandTotal) {
		this.grandTotal = grandTotal;
	}
	/**
	 * @return crDescription
	 */
	public String getCrDescription() {
		return crDescription;
	}
	/**
	 * @param crDescription
	 */
	public void setCrDescription(String crDescription) {
		this.crDescription = crDescription;
	}
	/**
	 * @return salesBookId
	 */
	public Integer getSalesBookId() {
		return salesBookId;
	}
	/**
	 * @param salesBookId
	 */
	public void setSalesBookId(Integer salesBookId) {
		this.salesBookId = salesBookId;
	}
	/**
	 * @return the qtyProduct
	 */
	public String getQtyProduct() {
		return qtyProduct;
	}
	/**
	 * @param qtyProduct the qtyProduct to set
	 */
	public void setQtyProduct(String qtyProduct) {
		this.qtyProduct = qtyProduct;
	}
	/**
	 * @return the qtyBonus
	 */
	public String getQtyBonus() {
		return qtyBonus;
	}
	/**
	 * @param qtyBonus the qtyBonus to set
	 */
	public void setQtyBonus(String qtyBonus) {
		this.qtyBonus = qtyBonus;
	}
	/**
	 * @return the costProduct
	 */
	public String getCostProduct() {
		return costProduct;
	}
	/**
	 * @param costProduct the costProduct to set
	 */
	public void setCostProduct(String costProduct) {
		this.costProduct = costProduct;
	}
	/**
	 * @return the totalCostProduct
	 */
	public String getTotalCostProduct() {
		return totalCostProduct;
	}
	/**
	 * @param totalCostProduct the totalCostProduct to set
	 */
	public void setTotalCostProduct(String totalCostProduct) {
		this.totalCostProduct = totalCostProduct;
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
	 * Float variable holds presentPayable
	 */
	private String presentPayable;
	/**
	 * Float variable holds previousCredit
	 */
	private String previousCredit;
	/**
	 * Float variable holds presentAdvance
	 */
	private String presentAdvance;
	/**
	 * Float variable holds totalPayable
	 */
	private String totalPayable;
	/**
	 * Float variable holds presentPayment
	 */
	private String presentPayment;
	/**
	 * Float variable holds balance
	 */
	private String balance;
	/**
	 * String variable holds paymentType
	 */
	private String paymentType;
	/**
	 * String variable holds chequeNo
	 */
	private String chequeNo;
	/**
	 * String variable holds bankName
	 */
	private String bankName;
	/**
	 * String variable holds branchName
	 */
	private String branchName;
	/**
	 * String variable holds bankLocation
	 */
	private String bankLocation;
	/**
	 * String variable holds businessName
	 */
	private String businessName;
	/**
	 * String variable holds invoiceName
	 */
	private String invoiceName;
	/**
	 * String variable holds invoiceNo
	 */
	private String invoiceNo;
	/**
	 * String variable holds createdDate
	 */
	private String createdDate;
	/**
	 * String variable holds salesExecutive
	 */
	private String salesExecutive;
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
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
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
	 * @return the presentPayable
	 */
	public String getPresentPayable() {
		return presentPayable;
	}
	/**
	 * @param presentPayable the presentPayable to set
	 */
	public void setPresentPayable(String presentPayable) {
		this.presentPayable = presentPayable;
	}
	/**
	 * @return the previousCredit
	 */
	public String getPreviousCredit() {
		return previousCredit;
	}
	/**
	 * @param previousCredit the previousCredit to set
	 */
	public void setPreviousCredit(String previousCredit) {
		this.previousCredit = previousCredit;
	}
	/**
	 * @return the presentAdvance
	 */
	public String getPresentAdvance() {
		return presentAdvance;
	}
	/**
	 * @param presentAdvance the presentAdvance to set
	 */
	public void setPresentAdvance(String presentAdvance) {
		this.presentAdvance = presentAdvance;
	}
	/**
	 * @return the totalPayable
	 */
	public String getTotalPayable() {
		return totalPayable;
	}
	/**
	 * @param totalPayable the totalPayable to set
	 */
	public void setTotalPayable(String totalPayable) {
		this.totalPayable = totalPayable;
	}
	/**
	 * @return the presentPayment
	 */
	public String getPresentPayment() {
		return presentPayment;
	}
	/**
	 * @param presentPayment the presentPayment to set
	 */
	public void setPresentPayment(String presentPayment) {
		this.presentPayment = presentPayment;
	}
	/**
	 * @return the balance
	 */
	public String getBalance() {
		return balance;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(String balance) {
		this.balance = balance;
	}
	/**
	 * @return the paymentType
	 */
	public String getPaymentType() {
		return paymentType;
	}
	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	/**
	 * @return the chequeNo
	 */
	public String getChequeNo() {
		return chequeNo;
	}
	/**
	 * @param chequeNo the chequeNo to set
	 */
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}
	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBankLocation() {
		return bankLocation;
	}
	public void setBankLocation(String bankLocation) {
		this.bankLocation = bankLocation;
	}
	@Override
	public int compareTo(DeliveryNoteViewResult result) {
		// TODO Auto-generated method stub
				int comparision=this.product.compareTo(result.getProduct()); 
				return comparision;
	}

}
