package com.vekomy.vbooks.mysales.command;
/**
 * @author Ankit
 *
 */
public class ChangeRequestDayBookVehicleDetailsCommand {

	/**
	 * String variable holds action
	 */
	private String action;
	
	/**
	 * String variable holds vehicleNo
	 */
	private String vehicleNo;
	/**
	 * String variable holds driverName
	 */
	private String driverName;
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
	private String remarks;
	
	/**
	 * String variable holds CR description
	 */
	private String description;
	/**
	 * @return the vehicleNo
	 */
	public String getVehicleNo() {
		return vehicleNo;
	}
	/**
	 * @param vehicleNo the vehicleNo to set
	 */
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	/**
	 * @return the driverName
	 */
	public String getDriverName() {
		return driverName;
	}
	/**
	 * @param driverName the driverName to set
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	/**
	 * @return the startingReading
	 */
	public String getStartingReading() {
		return startingReading;
	}
	/**
	 * @param startingReading the startingReading to set
	 */
	public void setStartingReading(String startingReading) {
		this.startingReading = startingReading;
	}
	/**
	 * @return the endingReading
	 */
	public String getEndingReading() {
		return endingReading;
	}
	/**
	 * @param endingReading the endingReading to set
	 */
	public void setEndingReading(String endingReading) {
		this.endingReading = endingReading;
	}
	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
