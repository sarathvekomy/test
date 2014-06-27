/**
 * com.vekomy.vbooks.mysales.command.MySalesHistoryResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 4, 2013
 */

package com.vekomy.vbooks.mysales.command;

/**
 * @author Sudhakar
 *
 */
public class MySalesHistoryResult implements java.io.Serializable {
	
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -7711720419720445164L;

	/**
	 * String variable holds deliveryNoteTransactionType
	 */
	private String deliveryNoteTransactionType;
	
	/**
	 * Integer variable holds approvedDNCount
	 */
	private Integer approvedDNCount;
	
	/**
	 * Integer variable holds declinedDNCount
	 */
	private Integer declinedDNCount;
	
	/**
	 * Integer variable holds pendingDNCount
	 */
	private Integer pendingDNCount;
	
    /**
     * Integer variable holds approvedCOLLECTIONCount
     */
    private Integer approvedCOLLECTIONCount;
	
	/**
	 * Integer variable holds declinedCOLLECTIONCount
	 */
	private Integer declinedCOLLECTIONCount;
	
	/**
	 * Integer variable holds pendingCOLLECTIONCount
	 */
	private Integer pendingCOLLECTIONCount;
	
	/**
	 * String variable holds deliveryNoteTxnStatus
	 */
	private String deliveryNoteTxnStatus;

	/**
	 * @return deliveryNoteTransactionType - {@link String}
	 */
	public String getDeliveryNoteTransactionType() {
		return deliveryNoteTransactionType;
	}

	/**
	 * @param deliveryNoteTransactionType deliveryNoteTransactionType - {@link String}
	 */
	public void setDeliveryNoteTransactionType(String deliveryNoteTransactionType) {
		this.deliveryNoteTransactionType = deliveryNoteTransactionType;
	}

	/**
	 * @return approvedDNCount - {@link Integer}
	 */
	public Integer getApprovedDNCount() {
		return approvedDNCount;
	}

	/**
	 * @param approvedDNCount - {@link Integer}
	 */
	public void setApprovedDNCount(Integer approvedDNCount) {
		this.approvedDNCount = approvedDNCount;
	}

	/**
	 * @return declinedDNCount - {@link Integer}
	 */
	public Integer getDeclinedDNCount() {
		return declinedDNCount;
	}

	/**
	 * @param declinedDNCount - {@link Integer}
	 */
	public void setDeclinedDNCount(Integer declinedDNCount) {
		this.declinedDNCount = declinedDNCount;
	}

	/**
	 * @return pendingDNCount - {@link Integer}
	 */
	public Integer getPendingDNCount() {
		return pendingDNCount;
	}

	/**
	 * @param pendingDNCount - {@link Integer}
	 */
	public void setPendingDNCount(Integer pendingDNCount) {
		this.pendingDNCount = pendingDNCount;
	}

	/**
	 * @return approvedCOLLECTIONCount - {@link Integer}
	 */
	public Integer getApprovedCOLLECTIONCount() {
		return approvedCOLLECTIONCount;
	}

	/**
	 * @param approvedCOLLECTIONCount - {@link Integer}
	 */
	public void setApprovedCOLLECTIONCount(Integer approvedCOLLECTIONCount) {
		this.approvedCOLLECTIONCount = approvedCOLLECTIONCount;
	}

	/**
	 * @return declinedCOLLECTIONCount - {@link Integer}
	 */
	public Integer getDeclinedCOLLECTIONCount() {
		return declinedCOLLECTIONCount;
	}

	/**
	 * @param declinedCOLLECTIONCount - {@link Integer}
	 */
	public void setDeclinedCOLLECTIONCount(Integer declinedCOLLECTIONCount) {
		this.declinedCOLLECTIONCount = declinedCOLLECTIONCount;
	}

	/**
	 * @return pendingCOLLECTIONCount - {@link Integer}
	 */
	public Integer getPendingCOLLECTIONCount() {
		return pendingCOLLECTIONCount;
	}

	/**
	 * @param pendingCOLLECTIONCount - {@link Integer}
	 */
	public void setPendingCOLLECTIONCount(Integer pendingCOLLECTIONCount) {
		this.pendingCOLLECTIONCount = pendingCOLLECTIONCount;
	}

	/**
	 * @return deliveryNoteTxnStatus - {@link Integer}
	 */
	public String getDeliveryNoteTxnStatus() {
		return deliveryNoteTxnStatus;
	}

	/**
	 * @param deliveryNoteTxnStatus - {@link Integer}
	 */
	public void setDeliveryNoteTxnStatus(String deliveryNoteTxnStatus) {
		this.deliveryNoteTxnStatus = deliveryNoteTxnStatus;
	}
	
}
