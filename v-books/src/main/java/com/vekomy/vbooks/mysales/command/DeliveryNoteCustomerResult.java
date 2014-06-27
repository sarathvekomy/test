/**
 * com.vekomy.vbooks.mysales.command.DeliveryNoteCustomerResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 8, 2013
 */
package com.vekomy.vbooks.mysales.command;

/**
 * @author swarupa
 *
 */
public class DeliveryNoteCustomerResult {
	
	/**
	 * String variable holds invoiceName
	 */
	private String invoiceName;
	/**
	 * String variable holds customerCredit
	 */
	private String customerCredit;
	/**
	 * String variable holds customerAdvance
	 */
	private String customerAdvance;
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
	 * @return the customerCredit
	 */
	public String getCustomerCredit() {
		return customerCredit;
	}
	/**
	 * @param customerCredit the customerCredit to set
	 */
	public void setCustomerCredit(String customerCredit) {
		this.customerCredit = customerCredit;
	}
	/**
	 * @return the customerAdvance
	 */
	public String getCustomerAdvance() {
		return customerAdvance;
	}
	/**
	 * @param customerAdvance the customerAdvance to set
	 */
	public void setCustomerAdvance(String customerAdvance) {
		this.customerAdvance = customerAdvance;
	}
	

}
