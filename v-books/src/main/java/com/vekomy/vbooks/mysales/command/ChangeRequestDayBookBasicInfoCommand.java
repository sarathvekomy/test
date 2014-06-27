package com.vekomy.vbooks.mysales.command;
/**
 * @author Ankit
 *
 */
public class ChangeRequestDayBookBasicInfoCommand {
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
	

	
}
