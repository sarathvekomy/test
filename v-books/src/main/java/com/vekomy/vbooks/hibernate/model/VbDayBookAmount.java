package com.vekomy.vbooks.hibernate.model;

// Generated Nov 19, 2013 10:32:53 AM by Hibernate Tools 3.4.0.CR1

/**
 * VbDayBookAmount generated by hbm2java
 */
public class VbDayBookAmount implements java.io.Serializable {

	private Integer id;
	private VbDayBook vbDayBook;
	private Float executiveAllowances;
	private Float driverAllowances;
	private Float vehicleFuelExpenses;
	private Float vehicleMeterReading;
	private Float vehicleMaintenanceExpenses;
	private Float offloadingLoadingCharges;
	private Float dealerPartyExpenses;
	private String reasonDealerPartyExpenses;
	private Float municipalCityCouncil;
	private Float miscellaneousExpenses;
	private String reasonMiscellaneousExpenses;
	private Float totalAllowances;
	private String allowancesRemarks;
	private Float customerTotalPayable;
	private Float customerTotalReceived;
	private Float customerTotalCredit;
	private Float amountToBank;
	private Float amountToFactory;
	private Float closingBalance;
	private String amountsRemarks;

	public VbDayBookAmount() {
	}

	public VbDayBookAmount(VbDayBook vbDayBook) {
		this.vbDayBook = vbDayBook;
	}

	public VbDayBookAmount(VbDayBook vbDayBook, Float executiveAllowances,
			Float driverAllowances, Float vehicleFuelExpenses,
			Float vehicleMeterReading, Float vehicleMaintenanceExpenses,
			Float offloadingLoadingCharges, Float dealerPartyExpenses,
			String reasonDealerPartyExpenses, Float municipalCityCouncil,
			Float miscellaneousExpenses, String reasonMiscellaneousExpenses,
			Float totalAllowances, String allowancesRemarks,
			Float customerTotalPayable, Float customerTotalReceived,
			Float customerTotalCredit, Float amountToBank,
			Float amountToFactory, Float closingBalance, String amountsRemarks) {
		this.vbDayBook = vbDayBook;
		this.executiveAllowances = executiveAllowances;
		this.driverAllowances = driverAllowances;
		this.vehicleFuelExpenses = vehicleFuelExpenses;
		this.vehicleMeterReading = vehicleMeterReading;
		this.vehicleMaintenanceExpenses = vehicleMaintenanceExpenses;
		this.offloadingLoadingCharges = offloadingLoadingCharges;
		this.dealerPartyExpenses = dealerPartyExpenses;
		this.reasonDealerPartyExpenses = reasonDealerPartyExpenses;
		this.municipalCityCouncil = municipalCityCouncil;
		this.miscellaneousExpenses = miscellaneousExpenses;
		this.reasonMiscellaneousExpenses = reasonMiscellaneousExpenses;
		this.totalAllowances = totalAllowances;
		this.allowancesRemarks = allowancesRemarks;
		this.customerTotalPayable = customerTotalPayable;
		this.customerTotalReceived = customerTotalReceived;
		this.customerTotalCredit = customerTotalCredit;
		this.amountToBank = amountToBank;
		this.amountToFactory = amountToFactory;
		this.closingBalance = closingBalance;
		this.amountsRemarks = amountsRemarks;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VbDayBook getVbDayBook() {
		return this.vbDayBook;
	}

	public void setVbDayBook(VbDayBook vbDayBook) {
		this.vbDayBook = vbDayBook;
	}

	public Float getExecutiveAllowances() {
		return this.executiveAllowances;
	}

	public void setExecutiveAllowances(Float executiveAllowances) {
		this.executiveAllowances = executiveAllowances;
	}

	public Float getDriverAllowances() {
		return this.driverAllowances;
	}

	public void setDriverAllowances(Float driverAllowances) {
		this.driverAllowances = driverAllowances;
	}

	public Float getVehicleFuelExpenses() {
		return this.vehicleFuelExpenses;
	}

	public void setVehicleFuelExpenses(Float vehicleFuelExpenses) {
		this.vehicleFuelExpenses = vehicleFuelExpenses;
	}

	public Float getVehicleMeterReading() {
		return this.vehicleMeterReading;
	}

	public void setVehicleMeterReading(Float vehicleMeterReading) {
		this.vehicleMeterReading = vehicleMeterReading;
	}

	public Float getVehicleMaintenanceExpenses() {
		return this.vehicleMaintenanceExpenses;
	}

	public void setVehicleMaintenanceExpenses(Float vehicleMaintenanceExpenses) {
		this.vehicleMaintenanceExpenses = vehicleMaintenanceExpenses;
	}

	public Float getOffloadingLoadingCharges() {
		return this.offloadingLoadingCharges;
	}

	public void setOffloadingLoadingCharges(Float offloadingLoadingCharges) {
		this.offloadingLoadingCharges = offloadingLoadingCharges;
	}

	public Float getDealerPartyExpenses() {
		return this.dealerPartyExpenses;
	}

	public void setDealerPartyExpenses(Float dealerPartyExpenses) {
		this.dealerPartyExpenses = dealerPartyExpenses;
	}

	public String getReasonDealerPartyExpenses() {
		return this.reasonDealerPartyExpenses;
	}

	public void setReasonDealerPartyExpenses(String reasonDealerPartyExpenses) {
		this.reasonDealerPartyExpenses = reasonDealerPartyExpenses;
	}

	public Float getMunicipalCityCouncil() {
		return this.municipalCityCouncil;
	}

	public void setMunicipalCityCouncil(Float municipalCityCouncil) {
		this.municipalCityCouncil = municipalCityCouncil;
	}

	public Float getMiscellaneousExpenses() {
		return this.miscellaneousExpenses;
	}

	public void setMiscellaneousExpenses(Float miscellaneousExpenses) {
		this.miscellaneousExpenses = miscellaneousExpenses;
	}

	public String getReasonMiscellaneousExpenses() {
		return this.reasonMiscellaneousExpenses;
	}

	public void setReasonMiscellaneousExpenses(
			String reasonMiscellaneousExpenses) {
		this.reasonMiscellaneousExpenses = reasonMiscellaneousExpenses;
	}

	public Float getTotalAllowances() {
		return this.totalAllowances;
	}

	public void setTotalAllowances(Float totalAllowances) {
		this.totalAllowances = totalAllowances;
	}

	public String getAllowancesRemarks() {
		return this.allowancesRemarks;
	}

	public void setAllowancesRemarks(String allowancesRemarks) {
		this.allowancesRemarks = allowancesRemarks;
	}

	public Float getCustomerTotalPayable() {
		return this.customerTotalPayable;
	}

	public void setCustomerTotalPayable(Float customerTotalPayable) {
		this.customerTotalPayable = customerTotalPayable;
	}

	public Float getCustomerTotalReceived() {
		return this.customerTotalReceived;
	}

	public void setCustomerTotalReceived(Float customerTotalReceived) {
		this.customerTotalReceived = customerTotalReceived;
	}

	public Float getCustomerTotalCredit() {
		return this.customerTotalCredit;
	}

	public void setCustomerTotalCredit(Float customerTotalCredit) {
		this.customerTotalCredit = customerTotalCredit;
	}

	public Float getAmountToBank() {
		return this.amountToBank;
	}

	public void setAmountToBank(Float amountToBank) {
		this.amountToBank = amountToBank;
	}

	public Float getAmountToFactory() {
		return this.amountToFactory;
	}

	public void setAmountToFactory(Float amountToFactory) {
		this.amountToFactory = amountToFactory;
	}

	public Float getClosingBalance() {
		return this.closingBalance;
	}

	public void setClosingBalance(Float closingBalance) {
		this.closingBalance = closingBalance;
	}

	public String getAmountsRemarks() {
		return this.amountsRemarks;
	}

	public void setAmountsRemarks(String amountsRemarks) {
		this.amountsRemarks = amountsRemarks;
	}

}
