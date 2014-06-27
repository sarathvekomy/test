/**
 * com.vekomy.vbooks.customer.command.CustomerChangeRequestHistoryResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: may 9, 2013
 */
package com.vekomy.vbooks.customer.command;

/**
 * @author Sudhakar
 *
 */
public class CustomerChangeRequestHistoryResult implements java.io.Serializable {

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 1646992326389803074L;

	/**
	 * String variable holds customerType.
	 */
	private String customerType;

	/**
	 * Integer variable holds approvedNewCount.
	 */
	private Integer approvedNewCount;

	/**
	 * Integer variable holds declinedNewCount.
	 */
	private Integer declinedNewCount;

	/**
	 * Integer variable holds pendingNewCount.
	 */
	private Integer pendingNewCount;

	/**
	 * Integer variable holds approvedExistingCount.
	 */
	private Integer approvedExistingCount;

	/**
	 * Integer variable holds declinedExistingCount.
	 */
	private Integer declinedExistingCount;

	/**
	 * Integer variable holds pendingExistingCount.
	 */
	private Integer pendingExistingCount;

	/**
	 * String variable holds customerChangeStatus.
	 */
	private String customerChangeStatus;

	/**
	 * @return the customerType
	 */
	public String getCustomerType() {
		return customerType;
	}

	/**
	 * @param customerType the customerType to set
	 */
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	/**
	 * @return the approvedNewCount
	 */
	public Integer getApprovedNewCount() {
		return approvedNewCount;
	}

	/**
	 * @param approvedNewCount the approvedNewCount to set
	 */
	public void setApprovedNewCount(Integer approvedNewCount) {
		this.approvedNewCount = approvedNewCount;
	}

	/**
	 * @return the declinedNewCount
	 */
	public Integer getDeclinedNewCount() {
		return declinedNewCount;
	}

	/**
	 * @param declinedNewCount the declinedNewCount to set
	 */
	public void setDeclinedNewCount(Integer declinedNewCount) {
		this.declinedNewCount = declinedNewCount;
	}

	/**
	 * @return the pendingNewCount
	 */
	public Integer getPendingNewCount() {
		return pendingNewCount;
	}

	/**
	 * @param pendingNewCount the pendingNewCount to set
	 */
	public void setPendingNewCount(Integer pendingNewCount) {
		this.pendingNewCount = pendingNewCount;
	}

	/**
	 * @return the approvedExistingCount
	 */
	public Integer getApprovedExistingCount() {
		return approvedExistingCount;
	}

	/**
	 * @param approvedExistingCount the approvedExistingCount to set
	 */
	public void setApprovedExistingCount(Integer approvedExistingCount) {
		this.approvedExistingCount = approvedExistingCount;
	}

	/**
	 * @return the declinedExistingCount
	 */
	public Integer getDeclinedExistingCount() {
		return declinedExistingCount;
	}

	/**
	 * @param declinedExistingCount the declinedExistingCount to set
	 */
	public void setDeclinedExistingCount(Integer declinedExistingCount) {
		this.declinedExistingCount = declinedExistingCount;
	}

	/**
	 * @return the pendingExistingCount
	 */
	public Integer getPendingExistingCount() {
		return pendingExistingCount;
	}

	/**
	 * @param pendingExistingCount the pendingExistingCount to set
	 */
	public void setPendingExistingCount(Integer pendingExistingCount) {
		this.pendingExistingCount = pendingExistingCount;
	}

	/**
	 * @return the customerChangeStatus
	 */
	public String getCustomerChangeStatus() {
		return customerChangeStatus;
	}

	/**
	 * @param customerChangeStatus the customerChangeStatus to set
	 */
	public void setCustomerChangeStatus(String customerChangeStatus) {
		this.customerChangeStatus = customerChangeStatus;
	}
	
	
}
