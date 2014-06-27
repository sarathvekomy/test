/**
 * com.vekomy.vbooks.reports.result.DeliveryNoteCollectionReportResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 4, 2013
 */
package com.vekomy.vbooks.reports.result;

import java.io.Serializable;
import java.util.Date;

/**
 * @author swarupa
 *
 */
public class DeliveryNoteCollectionReportResult implements Serializable{
	
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -5596721181078165752L;
	/**
	 * String variable holds createdOn
	 */
	private Date createdOn;
	/**
	 * String variable holds invoiceNo
	 */
	private String invoiceNo;
	/**
	 * Float variable holds presentPayment
	 */
	private Float presentPayment;
	
	/**
	 * String variable holds businessName
	 */
	private String businessName;
	/**
	 * String variable holds type
	 */
	private String type;
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
	 * @return the presentPayment
	 */
	public Float getPresentPayment() {
		return presentPayment;
	}
	/**
	 * @param presentPayment the presentPayment to set
	 */
	public void setPresentPayment(Float presentPayment) {
		this.presentPayment = presentPayment;
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
	

}
