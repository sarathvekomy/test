/**
 * com.vekomy.vbooks.app.request.DeliveryNoteRequest.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 25-Jul-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.app.request;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author nkr
 *
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryNoteRequest extends Request {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7862981706611473140L;
	private Integer organizationId;
	
	private String invoiceNo;
	private String createdOn;
	private String businessName;
	private String invoiceName;
	private String createdBy;
	
	private float previousCredit;
	private float presentAdvance;
	private float presentPayable;
	private float totalPayable;
	private float presentPayment;
	private float balance;
	
	private String paymentType;
	private String chequeNo;
	private String bankName;
	private String branchName;
	
	List<DeliveryNoteProducts> products;	
	
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
	 * @return the previousCredit
	 */
	public float getPreviousCredit() {
		return previousCredit;
	}
	/**
	 * @param previousCredit the previousCredit to set
	 */
	public void setPreviousCredit(float previousCredit) {
		this.previousCredit = previousCredit;
	}
	/**
	 * @return the presentAdvance
	 */
	public float getPresentAdvance() {
		return presentAdvance;
	}
	/**
	 * @param presentAdvance the presentAdvance to set
	 */
	public void setPresentAdvance(float presentAdvance) {
		this.presentAdvance = presentAdvance;
	}
	/**
	 * @return the presentPayable
	 */
	public float getPresentPayable() {
		return presentPayable;
	}
	/**
	 * @param presentPayable the presentPayable to set
	 */
	public void setPresentPayable(float presentPayable) {
		this.presentPayable = presentPayable;
	}
	/**
	 * @return the totalPayable
	 */
	public float getTotalPayable() {
		return totalPayable;
	}
	/**
	 * @param totalPayable the totalPayable to set
	 */
	public void setTotalPayable(float totalPayable) {
		this.totalPayable = totalPayable;
	}
	/**
	 * @return the presentPayment
	 */
	public float getPresentPayment() {
		return presentPayment;
	}
	/**
	 * @param presentPayment the presentPayment to set
	 */
	public void setPresentPayment(float presentPayment) {
		this.presentPayment = presentPayment;
	}
	/**
	 * @return the balance
	 */
	public float getBalance() {
		return balance;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(float balance) {
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
	/**
	 * @return the products
	 */
	public List<DeliveryNoteProducts> getProducts() {
		return products;
	}
	/**
	 * @param products the products to set
	 */
	public void setProducts(List<DeliveryNoteProducts> products) {
		this.products = products;
	}
	/**
	 * @return the organizationId
	 */
	public Integer getOrganizationId() {
		return organizationId;
	}
	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
}
