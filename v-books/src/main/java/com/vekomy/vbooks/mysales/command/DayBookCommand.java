/**
 * com.vekomy.vbooks.mysales.command.DayBookCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 22, 2013
 */
package com.vekomy.vbooks.mysales.command;

import com.vekomy.vbooks.hibernate.model.VbDayBook;

/**
 * @author Sudhakar
 * 
 * 
 */
public class DayBookCommand extends VbDayBook {

	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -341486518457296877L;
	/**
	 * String variable holds action.
	 */
	private String action;
	/**
	 * Float variable holds executiveAllowances.
	 */
	private Float executiveAllowances;
	/**
	 * Float variable holds driverAllowances.
	 */
	private Float driverAllowances;
	/**
	 * Float variable holds vehicleFuelExpenses.
	 */
	private Float vehicleFuelExpenses;
	/**
	 * Float variable holds vehicleMaintenanceExpenses.
	 */
	private Float vehicleMaintenanceExpenses;
	/**
	 * Float variable holds offloadingLoadingCharges.
	 */
	private Float offloadingLoadingCharges;
	/**
	 * Float variable holds dealerPartyExpenses.
	 */
	private Float dealerPartyExpenses;
	/**
	 * Float variable holds municipalCityCouncil.
	 */
	private Float municipalCityCouncil;
	/**
	 * Float variable holds miscellaneousExpenses.
	 */
	private Float miscellaneousExpenses;
	/**
	 * Float variable holds totalAllowances.
	 */
	private Float totalAllowances;
	/**
	 * Float variable holds customerTotalPayable.
	 */
	private Float customerTotalPayable;
	/**
	 * Float variable holds customerTotalReceived.
	 */
	private Float customerTotalReceived;
	/**
	 * Float variable holds customerTotalCredit.
	 */
	private Float customerTotalCredit;
	/**
	 * Float variable holds amountToBank.
	 */
	private Float amountToBank;
	/**
	 * Float variable holds amountToFactory.
	 */
	private Float amountToFactory;
	/**
	 * Float variable holds closingBalance.
	 */
	private Float closingBalance;
	/**
	 * String variable holds reasonMiscellaneousExpenses
	 */
	private String reasonMiscellaneousExpenses;
	/**
	 * String variable holds reasonAmountToBank
	 */
	private String reasonAmountToBank;
	
	/**
	 * float variable holds Opening Balance
	 */
	private float openingBalance;
	/**
	 * @return the openingBalance
	 */
	public float getOpeningBalance() {
		return openingBalance;
	}

	/**
	 * @param openingBalance the openingBalance to set
	 */
	public void setOpeningBalance(float openingBalance) {
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
	public Float getExecutiveAllowances() {
		return executiveAllowances;
	}

	/**
	 * @param executiveAllowances
	 *            the executiveAllowances to set
	 */
	public void setExecutiveAllowances(Float executiveAllowances) {
		this.executiveAllowances = executiveAllowances;
	}

	/**
	 * @return the driverAllowances
	 */
	public Float getDriverAllowances() {
		return driverAllowances;
	}

	/**
	 * @param driverAllowances
	 *            the driverAllowances to set
	 */
	public void setDriverAllowances(Float driverAllowances) {
		this.driverAllowances = driverAllowances;
	}

	/**
	 * @return the vehicleFuelExpenses
	 */
	public Float getVehicleFuelExpenses() {
		return vehicleFuelExpenses;
	}

	/**
	 * @param vehicleFuelExpenses
	 *            the vehicleFuelExpenses to set
	 */
	public void setVehicleFuelExpenses(Float vehicleFuelExpenses) {
		this.vehicleFuelExpenses = vehicleFuelExpenses;
	}

	/**
	 * @return the vehicleMaintenanceExpenses
	 */
	public Float getVehicleMaintenanceExpenses() {
		return vehicleMaintenanceExpenses;
	}

	/**
	 * @param vehicleMaintenanceExpenses
	 *            the vehicleMaintenanceExpenses to set
	 */
	public void setVehicleMaintenanceExpenses(Float vehicleMaintenanceExpenses) {
		this.vehicleMaintenanceExpenses = vehicleMaintenanceExpenses;
	}

	/**
	 * @return the offloadingLoadingCharges
	 */
	public Float getOffloadingLoadingCharges() {
		return offloadingLoadingCharges;
	}

	/**
	 * @param offloadingLoadingCharges
	 *            the offloadingLoadingCharges to set
	 */
	public void setOffloadingLoadingCharges(Float offloadingLoadingCharges) {
		this.offloadingLoadingCharges = offloadingLoadingCharges;
	}

	/**
	 * @return the dealerPartyExpenses
	 */
	public Float getDealerPartyExpenses() {
		return dealerPartyExpenses;
	}

	/**
	 * @param dealerPartyExpenses
	 *            the dealerPartyExpenses to set
	 */
	public void setDealerPartyExpenses(Float dealerPartyExpenses) {
		this.dealerPartyExpenses = dealerPartyExpenses;
	}

	/**
	 * @return the municipalCityCouncil
	 */
	public Float getMunicipalCityCouncil() {
		return municipalCityCouncil;
	}

	/**
	 * @param municipalCityCouncil
	 *            the municipalCityCouncil to set
	 */
	public void setMunicipalCityCouncil(Float municipalCityCouncil) {
		this.municipalCityCouncil = municipalCityCouncil;
	}

	/**
	 * @return the miscellaneousExpenses
	 */
	public Float getMiscellaneousExpenses() {
		return miscellaneousExpenses;
	}

	/**
	 * @param miscellaneousExpenses
	 *            the miscellaneousExpenses to set
	 */
	public void setMiscellaneousExpenses(Float miscellaneousExpenses) {
		this.miscellaneousExpenses = miscellaneousExpenses;
	}

	/**
	 * @return the totalAllowances
	 */
	public Float getTotalAllowances() {
		return totalAllowances;
	}

	/**
	 * @param totalAllowances
	 *            the totalAllowances to set
	 */
	public void setTotalAllowances(Float totalAllowances) {
		this.totalAllowances = totalAllowances;
	}

	/**
	 * @return the customerTotalPayable
	 */
	public Float getCustomerTotalPayable() {
		return customerTotalPayable;
	}

	/**
	 * @param customerTotalPayable
	 *            the customerTotalPayable to set
	 */
	public void setCustomerTotalPayable(Float customerTotalPayable) {
		this.customerTotalPayable = customerTotalPayable;
	}

	/**
	 * @return the customerTotalReceived
	 */
	public Float getCustomerTotalReceived() {
		return customerTotalReceived;
	}

	/**
	 * @param customerTotalReceived
	 *            the customerTotalReceived to set
	 */
	public void setCustomerTotalReceived(Float customerTotalReceived) {
		this.customerTotalReceived = customerTotalReceived;
	}

	/**
	 * @return the customerTotalCredit
	 */
	public Float getCustomerTotalCredit() {
		return customerTotalCredit;
	}

	/**
	 * @param customerTotalCredit
	 *            the customerTotalCredit to set
	 */
	public void setCustomerTotalCredit(Float customerTotalCredit) {
		this.customerTotalCredit = customerTotalCredit;
	}

	/**
	 * @return the amountToBank
	 */
	public Float getAmountToBank() {
		return amountToBank;
	}

	/**
	 * @param amountToBank
	 *            the amountToBank to set
	 */
	public void setAmountToBank(Float amountToBank) {
		this.amountToBank = amountToBank;
	}

	/**
	 * @return the amountToFactory
	 */
	public Float getAmountToFactory() {
		return amountToFactory;
	}

	/**
	 * @param amountToFactory
	 *            the amountToFactory to set
	 */
	public void setAmountToFactory(Float amountToFactory) {
		this.amountToFactory = amountToFactory;
	}

	/**
	 * @return the closingBalance
	 */
	public Float getClosingBalance() {
		return closingBalance;
	}

	/**
	 * @param closingBalance
	 *            the closingBalance to set
	 */
	public void setClosingBalance(Float closingBalance) {
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
