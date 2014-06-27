/**
 * com.vekomy.vbooks.app.response.DeliveryNoteResponse.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 16, 2013
 */
package com.vekomy.vbooks.app.response;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author NKR
 * 
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerAmountInfo extends Response {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2986603114500335980L;
	
	private String uid;
	
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
	private Double creditAmount;
	/**
	 * Float variable holds advanceAmount.
	 */
	private Double advanceAmount;

	/**
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}

	/**
	 * @param businessName
	 *            the businessName to set
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
	 * @param invoiceName
	 *            the invoiceName to set
	 */
	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	/**
	 * @return the creditAmount
	 */
	public Double getCreditAmount() {
		return creditAmount;
	}

	/**
	 * @param creditAmount
	 *            the creditAmount to set
	 */
	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}

	/**
	 * @return the advanceAmount
	 */
	public Double getAdvanceAmount() {
		return advanceAmount;
	}

	/**
	 * @param advanceAmount
	 *            the advanceAmount to set
	 */
	public void setAdvanceAmount(Double advanceAmount) {
		this.advanceAmount = advanceAmount;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

}
