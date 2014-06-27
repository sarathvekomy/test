/**
 * com.vekomy.vbooks.mysales.command.MySalesInvoicesHistoryResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 4, 2013
 */
package com.vekomy.vbooks.mysales.command;

import java.util.Date;

/**
 * @author Swarupa
 *
 */
public class MySalesInvoicesHistoryResult implements java.io.Serializable {
	
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -6498270414856071632L;

	/**
	 * Integer variable holds id
	 */
	private Integer id;
	
	/**
	 * String variable holds invoiceNumber
	 */
	private String invoiceNumber;
	
	/**
	 * String variable holds requestedBy
	 */
	private String requestedBy;
	
	/**
	 * Date variable holds requestedDate
	 */
	private Date requestedDate;
	
	/**
	 * String variable holds modifiedBy
	 */
	private String modifiedBy;
	
	/**
	 * Date variable holds modifiedDate
	 */
	private Date modifiedDate;
	/**
	 * String variable holds status
	 */
	private String status;
	
	/**
	 * @return id - {@link Integer}
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id - {@link Integer}
	 */
	public void setId(Integer id) {
		this.id = id;
	}


	/**
	 * @return invoiceNumber - {@link String}
	 */
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	/**
	 * @param invoiceNumber - {@link String}
	 */
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	/**
	 * @return requestedBy - {@link String}
	 */
	public String getRequestedBy() {
		return requestedBy;
	}

	/**
	 * @param requestedBy - {@link String}
	 */
	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}
	

	/**
	 * @return modifiedBy - {@link String}
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy - {@link String}
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return status - {@link String}
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status - {@link String}
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return requestedDate - {@link Date}
	 */
	public Date getRequestedDate() {
		return requestedDate;
	}

	/**
	 * @param requestedDate - {@link Date}
	 */
	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	/**
	 * @return modifiedDate - {@link Date}
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate - {@link Date}
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
