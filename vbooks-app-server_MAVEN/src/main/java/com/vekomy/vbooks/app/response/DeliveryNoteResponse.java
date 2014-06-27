/**
 * com.vekomy.vbooks.app.response.DeliveryNoteResponse.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 12, 2013
 */
package com.vekomy.vbooks.app.response;


/**
 * @author Sudhakar
 *
 */
public class DeliveryNoteResponse extends Response {
	/**
	 * String variable holds businessName.
	 */
	private String businessName;
	/**
	 * String variable holds invoiceName.
	 */
	private String invoiceName;
	/**
	 * Float variable holds creditAmount.
	 */
	private Float creditAmount;
	/**
	 * Float variable holds advanceAmount.
	 */
	private Float advanceAmount;
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
	 * @return the creditAmount
	 */
	public Float getCreditAmount() {
		return creditAmount;
	}
	/**
	 * @param creditAmount the creditAmount to set
	 */
	public void setCreditAmount(Float creditAmount) {
		this.creditAmount = creditAmount;
	}
	/**
	 * @return the advanceAmount
	 */
	public Float getAdvanceAmount() {
		return advanceAmount;
	}
	/**
	 * @param advanceAmount the advanceAmount to set
	 */
	public void setAdvanceAmount(Float advanceAmount) {
		this.advanceAmount = advanceAmount;
	}
	
	
}
