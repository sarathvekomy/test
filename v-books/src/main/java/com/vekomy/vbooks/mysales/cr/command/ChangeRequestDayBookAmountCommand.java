package com.vekomy.vbooks.mysales.cr.command;

import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequestAmount;
/**
 * @author Ankit
 *
 */
public class ChangeRequestDayBookAmountCommand extends
		VbDayBookChangeRequestAmount {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -7806842381421025822L;
	/**
	 * String variable holds action
	 */
	private String action;
	/**
	 * String variable holds openingBalance
	 */
	private String openingBalance;
	
	/**
	 * String variable holds reportingManagerName
	 */
	private String reportingManagerName;
	
	private String amountsCRRemarks;
	
	
	public String getReportingManagerName() {
		return reportingManagerName;
	}
	public void setReportingManagerName(String reportingManagerName) {
		this.reportingManagerName = reportingManagerName;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the openingBalance
	 */
	public String getOpeningBalance() {
		return openingBalance;
	}
	/**
	 * @param openingBalance the openingBalance to set
	 */
	public void setOpeningBalance(String openingBalance) {
		this.openingBalance = openingBalance;
	}
	public String getAmountsCRRemarks() {
		return amountsCRRemarks;
	}
	public void setAmountsCRRemarks(String amountsCRRemarks) {
		this.amountsCRRemarks = amountsCRRemarks;
	}
	
}
