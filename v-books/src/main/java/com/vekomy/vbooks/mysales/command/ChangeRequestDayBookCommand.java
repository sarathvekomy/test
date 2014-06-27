package com.vekomy.vbooks.mysales.command;

import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequest;
/**
 * @author Ankit
 * 
 * 
 */
public class ChangeRequestDayBookCommand extends VbDayBookChangeRequest {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -341486518457296877L;
	/**
	 * String variable holds action.
	 */
	private String action;
	/**
	 * String variable holds executiveAllowances.
	 */
	private String executiveAllowances;
	/**
	 * String variable holds driverAllowances.
	 */
	private String driverAllowances;
	/**
	 * String variable holds vehicleFuelExpenses.
	 */
	private String vehicleFuelExpenses;
	/**
	 * String variable holds vehicleMaintenanceExpenses.
	 */
	private String vehicleMaintenanceExpenses;
	/**
	 * String variable holds offloadingLoadingCharges.
	 */
	private String offloadingLoadingCharges;
	/**
	 * String variable holds dealerPartyExpenses.
	 */
	private String dealerPartyExpenses;
	/**
	 * String variable holds municipalCityCouncil.
	 */
	private String municipalCityCouncil;
	/**
	 * String variable holds miscellaneousExpenses.
	 */
	private String miscellaneousExpenses;
	/**
	 * String variable holds totalAllowances.
	 */
	private String totalAllowances;
	/**
	 * String variable holds customerTotalPayable.
	 */
	private String customerTotalPayable;
	/**
	 * String variable holds customerTotalReceived.
	 */
	private String customerTotalReceived;
	/**
	 * String variable holds customerTotalCredit.
	 */
	private String customerTotalCredit;
	/**
	 * String variable holds amountToBank.
	 */
	private String amountToBank;
	/**
	 * String variable holds amountToFactory.
	 */
	private String amountToFactory;
	/**
	 * String variable holds closingBalance.
	 */
	private String closingBalance;
	/**
	 * String variable holds reasonMiscellaneousExpenses
	 */
	private String reasonMiscellaneousExpenses;
	/**
	 * String variable holds reasonAmountToBank
	 */
	private String reasonAmountToBank;
	
	/**
	 * String variable holds Opening Balance
	 */
	private String openingBalance;
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

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the executiveAllowances
	 */
	public String getExecutiveAllowances() {
		return executiveAllowances;
	}

	/**
	 * @param executiveAllowances
	 *            the executiveAllowances to set
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
	 * @param driverAllowances
	 *            the driverAllowances to set
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
	 * @param vehicleFuelExpenses
	 *            the vehicleFuelExpenses to set
	 */
	public void setVehicleFuelExpenses(String vehicleFuelExpenses) {
		this.vehicleFuelExpenses = vehicleFuelExpenses;
	}

	/**
	 * @return the vehicleMaintenanceExpenses
	 */
	public String getVehicleMaintenanceExpenses() {
		return vehicleMaintenanceExpenses;
	}

	/**
	 * @param vehicleMaintenanceExpenses
	 *            the vehicleMaintenanceExpenses to set
	 */
	public void setVehicleMaintenanceExpenses(String vehicleMaintenanceExpenses) {
		this.vehicleMaintenanceExpenses = vehicleMaintenanceExpenses;
	}

	/**
	 * @return the offloadingLoadingCharges
	 */
	public String getOffloadingLoadingCharges() {
		return offloadingLoadingCharges;
	}

	/**
	 * @param offloadingLoadingCharges
	 *            the offloadingLoadingCharges to set
	 */
	public void setOffloadingLoadingCharges(String offloadingLoadingCharges) {
		this.offloadingLoadingCharges = offloadingLoadingCharges;
	}

	/**
	 * @return the dealerPartyExpenses
	 */
	public String getDealerPartyExpenses() {
		return dealerPartyExpenses;
	}

	/**
	 * @param dealerPartyExpenses
	 *            the dealerPartyExpenses to set
	 */
	public void setDealerPartyExpenses(String dealerPartyExpenses) {
		this.dealerPartyExpenses = dealerPartyExpenses;
	}

	/**
	 * @return the municipalCityCouncil
	 */
	public String getMunicipalCityCouncil() {
		return municipalCityCouncil;
	}

	/**
	 * @param municipalCityCouncil
	 *            the municipalCityCouncil to set
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
	 * @param miscellaneousExpenses
	 *            the miscellaneousExpenses to set
	 */
	public void setMiscellaneousExpenses(String miscellaneousExpenses) {
		this.miscellaneousExpenses = miscellaneousExpenses;
	}

	/**
	 * @return the totalAllowances
	 */
	public String getTotalAllowances() {
		return totalAllowances;
	}

	/**
	 * @param totalAllowances
	 *            the totalAllowances to set
	 */
	public void setTotalAllowances(String totalAllowances) {
		this.totalAllowances = totalAllowances;
	}

	/**
	 * @return the customerTotalPayable
	 */
	public String getCustomerTotalPayable() {
		return customerTotalPayable;
	}

	/**
	 * @param customerTotalPayable
	 *            the customerTotalPayable to set
	 */
	public void setCustomerTotalPayable(String customerTotalPayable) {
		this.customerTotalPayable = customerTotalPayable;
	}

	/**
	 * @return the customerTotalReceived
	 */
	public String getCustomerTotalReceived() {
		return customerTotalReceived;
	}

	/**
	 * @param customerTotalReceived
	 *            the customerTotalReceived to set
	 */
	public void setCustomerTotalReceived(String customerTotalReceived) {
		this.customerTotalReceived = customerTotalReceived;
	}

	/**
	 * @return the customerTotalCredit
	 */
	public String getCustomerTotalCredit() {
		return customerTotalCredit;
	}

	/**
	 * @param customerTotalCredit
	 *            the customerTotalCredit to set
	 */
	public void setCustomerTotalCredit(String customerTotalCredit) {
		this.customerTotalCredit = customerTotalCredit;
	}

	/**
	 * @return the amountToBank
	 */
	public String getAmountToBank() {
		return amountToBank;
	}

	/**
	 * @param amountToBank
	 *            the amountToBank to set
	 */
	public void setAmountToBank(String amountToBank) {
		this.amountToBank = amountToBank;
	}

	/**
	 * @return the amountToFactory
	 */
	public String getAmountToFactory() {
		return amountToFactory;
	}

	/**
	 * @param amountToFactory
	 *            the amountToFactory to set
	 */
	public void setAmountToFactory(String amountToFactory) {
		this.amountToFactory = amountToFactory;
	}

	/**
	 * @return the closingBalance
	 */
	public String getClosingBalance() {
		return closingBalance;
	}

	/**
	 * @param closingBalance
	 *            the closingBalance to set
	 */
	public void setClosingBalance(String closingBalance) {
		this.closingBalance = closingBalance;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer("action :").append(getAction())
				.append(",executiveAllowances :")
				.append(getExecutiveAllowances()).append(",driverAllowances :")
				.append(getDriverAllowances())
				.append(", vehicleFuelExpenses :")
				.append(getVehicleFuelExpenses())
				.append(", vehicleMaintenanceExpenses :")
				.append(getVehicleMaintenanceExpenses())
				.append(", offloadingCharges :")
				.append(getOffloadingLoadingCharges())
				.append(",dealerPartyExpenses :")
				.append(getDealerPartyExpenses())
				.append(", muncipalityExpenses :")
				.append(getMunicipalCityCouncil())
				.append(", totalAllowances :").append(getTotalAllowances())
				.append(", customerTotalPayable :")
				.append(getCustomerTotalPayable())
				.append(", customerTotalRecieved :")
				.append(getCustomerTotalReceived())
				.append(",customerTotalCredit :")
				.append(getCustomerTotalCredit()).append(",amountToBank :")
				.append(getAmountToBank()).append(", amountToFactory :")
				.append(getAmountToFactory()).append(", closingBalance :")
				.append(getClosingBalance())
				.append(" , reasonMiscellaneousExpenses :").append(getReasonMiscellaneousExpenses())
				.append(", reasonAmountToBank :").append(getReasonAmountToBank())
				.append("]").toString();

	}
}
