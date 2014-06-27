package com.vekomy.vbooks.mysales.cr.command;

import java.io.Serializable;

/**
 * @author Ankit
 *
 */
public class ChangeRequestDayBookBasicInfoCommand implements Serializable{
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -1596430459024927851L;
	/**
	 * String variable holds action
	 */
	private String action;
	/**
	 * String variable holds salesExecutive
	 */
	private String salesExecutive;
	/**
	 * Boolean variable holds isReturn
	 */
	private Boolean isReturn;
	/**
	 * String variable holds allotStockOpeningBalance
	 */
	private String allotStockOpeningBalance;
	
	/**
	 * String variable holds reportingManagerName
	 */
	private String reportingManager;
	/**
	 * String variable holds DayBookCRRemarks
	 */
	private String dayBookCRRemarks;
	
	public String getReportingManager() {
		return reportingManager;
	}
	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
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
	 * @return the salesExecutive
	 */
	public String getSalesExecutive() {
		return salesExecutive;
	}
	/**
	 * @param salesExecutive the salesExecutive to set
	 */
	public void setSalesExecutive(String salesExecutive) {
		this.salesExecutive = salesExecutive;
	}
	/**
	 * @return the isReturn
	 */
	public Boolean getIsReturn() {
		return isReturn;
	}
	/**
	 * @param isReturn the isReturn to set
	 */
	public void setIsReturn(Boolean isReturn) {
		this.isReturn = isReturn;
	}
	/**
	 * @return the allotStockOpeningBalance
	 */
	public String getAllotStockOpeningBalance() {
		return allotStockOpeningBalance;
	}
	/**
	 * @param allotStockOpeningBalance the allotStockOpeningBalance to set
	 */
	public void setAllotStockOpeningBalance(String allotStockOpeningBalance) {
		this.allotStockOpeningBalance = allotStockOpeningBalance;
	}
	public String getDayBookCRRemarks() {
		return dayBookCRRemarks;
	}
	public void setDayBookCRRemarks(String dayBookCRRemarks) {
		this.dayBookCRRemarks = dayBookCRRemarks;
	}
}
