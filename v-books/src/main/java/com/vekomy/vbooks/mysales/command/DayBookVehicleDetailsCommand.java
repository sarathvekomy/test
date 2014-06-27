/**
 * com.vekomy.vbooks.mysales.command.DayBookVehicleDetailsCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jun 20, 2013
 */
package com.vekomy.vbooks.mysales.command;

/**
 * @author swarupa
 *
 */
public class DayBookVehicleDetailsCommand {
	
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
	 * Float variable holds startingReading
	 */
	private Float startingReading;
	/**
	 * Float variable holds endingReading
	 */
	private Float endingReading;
	/**
	 * String variable holds remarks
	 */
	private String remarks;
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
	public Float getStartingReading() {
		return startingReading;
	}
	/**
	 * @param startingReading the startingReading to set
	 */
	public void setStartingReading(Float startingReading) {
		this.startingReading = startingReading;
	}
	/**
	 * @return the endingReading
	 */
	public Float getEndingReading() {
		return endingReading;
	}
	/**
	 * @param endingReading the endingReading to set
	 */
	public void setEndingReading(Float endingReading) {
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
	
	

}
