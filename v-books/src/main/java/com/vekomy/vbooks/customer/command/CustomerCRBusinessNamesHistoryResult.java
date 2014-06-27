/**
 * com.vekomy.vbooks.customer.command.CustomerCRBusinessNamesHistoryResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 15, 2013
 */
package com.vekomy.vbooks.customer.command;

import java.util.Date;

/**
 * @author Sudhakar
 *
 */
public class CustomerCRBusinessNamesHistoryResult implements java.io.Serializable {
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -3288352049033718496L;
	/**
	 * Integer variable holds id.
	 */
	private Integer id;
	/**
	 * String variable holds businessName.
	 */
	private String businessName;
	/**
	 * String variable holds requestedBy.
	 */
	private String requestedBy;
	/**
	 * Date variable holds requestedDate.
	 */
	private Date requestedDate;
	/**
	 * String variable holds modifiedBy.
	 */
	private String modifiedBy;
	/**
	 * Date variable holds modifiedDate.
	 */
	private Date modifiedDate;
	/**
	 * String variable holds status.
	 */
	private String status;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * @return the requestedBy
	 */
	public String getRequestedBy() {
		return requestedBy;
	}
	/**
	 * @param requestedBy the requestedBy to set
	 */
	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}
	/**
	 * @return the requestedDate
	 */
	public Date getRequestedDate() {
		return requestedDate;
	}
	/**
	 * @param requestedDate the requestedDate to set
	 */
	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}
	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}
	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}
	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
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

}
