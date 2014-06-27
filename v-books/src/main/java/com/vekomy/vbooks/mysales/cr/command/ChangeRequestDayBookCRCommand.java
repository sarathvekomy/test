package com.vekomy.vbooks.mysales.cr.command;

public class ChangeRequestDayBookCRCommand extends ChangeRequestDayBookCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3227259887179124548L;

	
	/**
	 * String variable holds vehicleNo
	 */
	private String vehicleNo;
	/**
	 * String variable holds driverName
	 */
	/**
	 * String variable holds startingReading
	 */
	private String startingReading;
	/**
	 * String variable holds endingReading
	 */
	private String endingReading;
	/**
	 * String variable holds remarks
	 */
	private String amountsCRRemarks;
	
	private String dayBookCRRemarks;
	
	private String allowancesCRRemarks;
	
	private String productsCRRemarks;
	
	/**
	 * String variable holds driverName
	 */
	private String driverName;
	
	/**
	 * String variable holds salesExecutive
	 */
	private String salesExecutive;
	/**
	 * String variable holds allotStockOpeningBalance
	 */
	private String allotStockOpeningBalance;
	
	public String getSalesExecutive() {
		return salesExecutive;
	}
	public void setSalesExecutive(String salesExecutive) {
		this.salesExecutive = salesExecutive;
	}
	public String getAllotStockOpeningBalance() {
		return allotStockOpeningBalance;
	}
	public void setAllotStockOpeningBalance(String allotStockOpeningBalance) {
		this.allotStockOpeningBalance = allotStockOpeningBalance;
	}
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getStartingReading() {
		return startingReading;
	}
	public void setStartingReading(String startingReading) {
		this.startingReading = startingReading;
	}
	public String getEndingReading() {
		return endingReading;
	}
	public void setEndingReading(String endingReading) {
		this.endingReading = endingReading;
	}
	public String getAmountsCRRemarks() {
		return amountsCRRemarks;
	}
	public void setAmountsCRRemarks(String amountsCRRemarks) {
		this.amountsCRRemarks = amountsCRRemarks;
	}
	public String getDayBookCRRemarks() {
		return dayBookCRRemarks;
	}
	public void setDayBookCRRemarks(String dayBookCRRemarks) {
		this.dayBookCRRemarks = dayBookCRRemarks;
	}
	public String getAllowancesCRRemarks() {
		return allowancesCRRemarks;
	}
	public void setAllowancesCRRemarks(String allowancesCRRemarks) {
		this.allowancesCRRemarks = allowancesCRRemarks;
	}
	public String getProductsCRRemarks() {
		return productsCRRemarks;
	}
	public void setProductsCRRemarks(String productsCRRemarks) {
		this.productsCRRemarks = productsCRRemarks;
	}
	
}
