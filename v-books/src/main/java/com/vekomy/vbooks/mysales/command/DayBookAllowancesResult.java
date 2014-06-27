/**
 * com.vekomy.vbooks.mysales.command.DayBookAllowancesResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 17, 2013
 */
package com.vekomy.vbooks.mysales.command;

/**
 * @author Swarupa
 *
 */
public class DayBookAllowancesResult implements java.io.Serializable {
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 6058846264319590901L;
	/**
	 * Integer variable holds id.
	 */
	private Integer id;
	/**
	 * String variable holds createdOn.
	 */
	private String createdOn;
	/**
	 * String variable holds driverName
	 */
	private String driverName;
	/**
	 * String variable holds businessName
	 */
	private String businessName;
	/**
	 * String variable holds vehicleNo
	 */
	private String vehicleNo;
	/**
	 * String variable holds startingReading
	 */
	private String startingReading;
	/**
	 * String variable holds executiveAllowances
	 */
	private String executiveAllowances;
	/**
	 * String variable holds driverAllowances
	 */
	private String driverAllowances;
	/**
	 * String variable holds vehicleFuelExpenses
	 */
	private String vehicleFuelExpenses;
	/**
	 * String variable holds vehicleMeterReading
	 */
	private String vehicleMeterReading;
	/**
	 * String variable holds vehicleMaintenanceExpenses
	 */
	private String vehicleMaintenanceExpenses;
	/**
	 * String variable holds offLoadingCharges
	 */
	private String offLoadingCharges;
	/**
	 * String variable holds dealerPartyExpenses
	 */
	private String dealerPartyExpenses;
	/**
	 * String variable holds reasonDealerPartyExpenses
	 */
	private String reasonDealerPartyExpenses;
	/**
	 * String variable holds municipalCityCouncil
	 */
	private String municipalCityCouncil;
	/**
	 * String variable holds miscellaneousExpenses
	 */
	private String miscellaneousExpenses;
	/**
	 * String variable holds reasonMiscellaneousExpenses
	 */
	private String reasonMiscellaneousExpenses;
	/**
	 * String variable holds amountToBank
	 */
	private String amountToBank;
	/**
	 * String variable holds reasonAmountToBank
	 */
	private String reasonAmountToBank;
	/**
	 * String variable holds dayBookType
	 */
	private String dayBookType;
	/**
	 * String variable holds remarks
	 */
	private String remarks;
	/**
	 * String variable holds allowancesAmount
	 */
	private String allowancesAmount;
	/**
	 * String variable holds salesBookId
	 */
	private Integer salesBookId;
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
	 * @return the createdOn
	 */
	public String getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
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
	 * @return the executiveAllowances
	 */
	public String getExecutiveAllowances() {
		return executiveAllowances;
	}
	/**
	 * @param executiveAllowances the executiveAllowances to set
	 */
	public void setExecutiveAllowances(String executiveAllowances) {
		this.executiveAllowances = executiveAllowances;
	}
	/**
	 * @return the driverAllowances
	 */
	public String getDriverAllowances() {
		return driverAllowances;
	}
	/**
	 * @param driverAllowances the driverAllowances to set
	 */
	public void setDriverAllowances(String driverAllowances) {
		this.driverAllowances = driverAllowances;
	}
	/**
	 * @return the vehicleFuelExpenses
	 */
	public String getVehicleFuelExpenses() {
		return vehicleFuelExpenses;
	}
	/**
	 * @param vehicleFuelExpenses the vehicleFuelExpenses to set
	 */
	public void setVehicleFuelExpenses(String vehicleFuelExpenses) {
		this.vehicleFuelExpenses = vehicleFuelExpenses;
	}
	/**
	 * @return the vehicleMeterReading
	 */
	public String getVehicleMeterReading() {
		return vehicleMeterReading;
	}
	/**
	 * @param vehicleMeterReading the vehicleMeterReading to set
	 */
	public void setVehicleMeterReading(String vehicleMeterReading) {
		this.vehicleMeterReading = vehicleMeterReading;
	}
	/**
	 * @return the vehicleMaintenanceExpenses
	 */
	public String getVehicleMaintenanceExpenses() {
		return vehicleMaintenanceExpenses;
	}
	/**
	 * @param vehicleMaintenanceExpenses the vehicleMaintenanceExpenses to set
	 */
	public void setVehicleMaintenanceExpenses(String vehicleMaintenanceExpenses) {
		this.vehicleMaintenanceExpenses = vehicleMaintenanceExpenses;
	}
	/**
	 * @return the offloadingLoadingCharges
	 */
	public String getOffLoadingCharges() {
		return offLoadingCharges;
	}
	/**
	 * @param offloadingLoadingCharges the offloadingLoadingCharges to set
	 */
	public void setOffLoadingCharges(String offLoadingCharges) {
		this.offLoadingCharges = offLoadingCharges;
	}
	/**
	 * @return the dealerPartyExpenses
	 */
	public String getDealerPartyExpenses() {
		return dealerPartyExpenses;
	}
	/**
	 * @param dealerPartyExpenses the dealerPartyExpenses to set
	 */
	public void setDealerPartyExpenses(String dealerPartyExpenses) {
		this.dealerPartyExpenses = dealerPartyExpenses;
	}
	/**
	 * @return the reasonDealerPartyExpenses
	 */
	public String getReasonDealerPartyExpenses() {
		return reasonDealerPartyExpenses;
	}
	/**
	 * @param reasonDealerPartyExpenses the reasonDealerPartyExpenses to set
	 */
	public void setReasonDealerPartyExpenses(String reasonDealerPartyExpenses) {
		this.reasonDealerPartyExpenses = reasonDealerPartyExpenses;
	}
	/**
	 * @return the municipalCityCouncil
	 */
	public String getMunicipalCityCouncil() {
		return municipalCityCouncil;
	}
	/**
	 * @param municipalCityCouncil the municipalCityCouncil to set
	 */
	public void setMunicipalCityCouncil(String municipalCityCouncil) {
		this.municipalCityCouncil = municipalCityCouncil;
	}
	/**
	 * @return the miscellaneousExpenses
	 */
	public String getMiscellaneousExpenses() {
		return miscellaneousExpenses;
	}
	/**
	 * @param miscellaneousExpenses the miscellaneousExpenses to set
	 */
	public void setMiscellaneousExpenses(String miscellaneousExpenses) {
		this.miscellaneousExpenses = miscellaneousExpenses;
	}
	/**
	 * @return the reasonMiscellaneousExpenses
	 */
	public String getReasonMiscellaneousExpenses() {
		return reasonMiscellaneousExpenses;
	}
	/**
	 * @param reasonMiscellaneousExpenses the reasonMiscellaneousExpenses to set
	 */
	public void setReasonMiscellaneousExpenses(String reasonMiscellaneousExpenses) {
		this.reasonMiscellaneousExpenses = reasonMiscellaneousExpenses;
	}
	/**
	 * @return the amountToBank
	 */
	public String getAmountToBank() {
		return amountToBank;
	}
	/**
	 * @param amountToBank the amountToBank to set
	 */
	public void setAmountToBank(String amountToBank) {
		this.amountToBank = amountToBank;
	}
	/**
	 * @return the reasonAmountToBank
	 */
	public String getReasonAmountToBank() {
		return reasonAmountToBank;
	}
	/**
	 * @param reasonAmountToBank the reasonAmountToBank to set
	 */
	public void setReasonAmountToBank(String reasonAmountToBank) {
		this.reasonAmountToBank = reasonAmountToBank;
	}
	/**
	 * @return the dayBookType
	 */
	public String getDayBookType() {
		return dayBookType;
	}
	/**
	 * @param dayBookType the dayBookType to set
	 */
	public void setDayBookType(String dayBookType) {
		this.dayBookType = dayBookType;
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
	 * @return the allowancesAmount
	 */
	public String getAllowancesAmount() {
		return allowancesAmount;
	}
	/**
	 * @param allowancesAmount the allowancesAmount to set
	 */
	public void setAllowancesAmount(String allowancesAmount) {
		this.allowancesAmount = allowancesAmount;
	}
	
	public Integer getSalesBookId() {
		return salesBookId;
	}
	public void setSalesBookId(Integer salesBookId) {
		this.salesBookId = salesBookId;
	}
	
	
}
