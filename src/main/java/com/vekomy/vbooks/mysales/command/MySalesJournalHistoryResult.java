/**
 * com.vekomy.vbooks.mysales.command.MySalesJournalHistoryResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 4, 2013
 */
package com.vekomy.vbooks.mysales.command;

/**
 * @author Swarupa
 *
 */
public class MySalesJournalHistoryResult implements java.io.Serializable {
	
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -7073621244811352819L;

	/**
	 * String variable holds journalTransactionType
	 */
	private String journalTransactionType;
	
	/**
	 * String variable holds journalInvoicePattern
	 */
	private String journalInvoicePattern;
	
	/**
	 * Integer variable holds totalJournalTypeCount
	 */
	private Integer totalJournalTypeCount;
	
	/**
	 * Integer variable holds declinedJournalCount
	 */
	private Integer declinedJournalCount;
	
	/**
	 * Integer variable holds pendingJournalCount
	 */
	private Integer pendingJournalCount;
	
	/**
	 * Integer variable holds approvedJournalCount
	 */
	private Integer approvedJournalCount;
	
	/**
	 * String variable holds journalTxnStatus
	 */
	private String journalTxnStatus;

	/**
	 * @return journalTransactionType - {@link String}
	 */ 
	public String getJournalTransactionType() {
		return journalTransactionType;
	}

	/**
	 * @param journalTransactionType - {@link String}
	 */
	public void setJournalTransactionType(String journalTransactionType) {
		this.journalTransactionType = journalTransactionType;
	}

	/**
	 * @return journalInvoicePattern - {@link String}
	 */
	public String getJournalInvoicePattern() {
		return journalInvoicePattern;
	}

	/**
	 * @param journalInvoicePattern - {@link String}
	 */
	public void setJournalInvoicePattern(String journalInvoicePattern) {
		this.journalInvoicePattern = journalInvoicePattern;
	}

	/**
	 * @return totalJournalTypeCount - {@link Integer}
	 */
	public Integer getTotalJournalTypeCount() {
		return totalJournalTypeCount;
	}

	/**
	 * @param totalJournalTypeCount - {@link Integer}
	 */
	public void setTotalJournalTypeCount(Integer totalJournalTypeCount) {
		this.totalJournalTypeCount = totalJournalTypeCount;
	}

	/**
	 * @return declinedJournalCount - {@link Integer}
	 */
	public Integer getDeclinedJournalCount() {
		return declinedJournalCount;
	}

	/**
	 * @param declinedJournalCount {@link Integer}
	 */
	public void setDeclinedJournalCount(Integer declinedJournalCount) {
		this.declinedJournalCount = declinedJournalCount;
	}

	/**
	 * @return pendingJournalCount - {@link Integer}
	 */
	public Integer getPendingJournalCount() {
		return pendingJournalCount;
	}

	/**
	 * @param pendingJournalCount - {@link Integer}
	 */
	public void setPendingJournalCount(Integer pendingJournalCount) {
		this.pendingJournalCount = pendingJournalCount;
	}

	/**
	 * @return approvedJournalCount - {@link Integer}
	 */
	public Integer getApprovedJournalCount() {
		return approvedJournalCount;
	}

	/**
	 * @param approvedJournalCount - {@link Integer}
	 */
	public void setApprovedJournalCount(Integer approvedJournalCount) {
		this.approvedJournalCount = approvedJournalCount;
	}

	/**
	 * @return journalTxnStatus - {@link String}
	 */
	public String getJournalTxnStatus() {
		return journalTxnStatus;
	}

	/**
	 * @param journalTxnStatus - {@link String}
	 */
	public void setJournalTxnStatus(String journalTxnStatus) {
		this.journalTxnStatus = journalTxnStatus;
	}
}
