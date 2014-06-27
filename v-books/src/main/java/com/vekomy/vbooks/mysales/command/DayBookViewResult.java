/**
 * com.vekomy.vbooks.mysales.command.DayBookViewResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jun 20, 2013
 */
package com.vekomy.vbooks.mysales.command;

import java.util.List;

import com.vekomy.vbooks.hibernate.model.VbDayBookProducts;

/**
 * @author Sudhakar
 *
 */
public class DayBookViewResult extends VbDayBookProducts implements	Comparable<DayBookViewResult> {

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -8337647709019671077L;
	/**
	 * String variable holds openingBalance
	 */
	private String openingBalance;
	/**
	 * String variable holds product
	 */
	private String product;
	/**
	 * List<String> variable holds depositedAmounts
	 */
	private List<String> depositedAmounts;
	/**
	 * String variable holds totalExpenses
	 */
	private String totalExpenses;
	/**
	 * Float String holds reasonMiscellaneousExpenses
	 */
	private String reasonMiscellaneousExpenses;
	/**
	 * String holds totalPayable
	 */
	private String totalPayable;
	/**
	 * String holds totalRecieved
	 */
	private String totalRecieved;
	/**
	 * String holds balance
	 */
	private String balance;
	/**
	 * String holds amountToBank
	 */
	private String amountToBank;
	/**
	 * String holds amountToFactory
	 */
	private String amountToFactory;
	/**
	 * Float String holds closingBalance
	 */
	private String closingBalance;
	/**
	 * String String holds reasonAmountToBank
	 */
	private String reasonAmountToBank;
	/**
	 * String String holds salesExecutive
	 */
	private String salesExecutive;
	/**
	 * String holds createdDate
	 */
	private String createdDate;
	/**
	 * String String holds vehicleNo
	 */
	private String vehicleNo;
	/**
	 * Float String holds startingReading
	 */
	private Float startingReading;
	/**
	 * Float String holds endingReading
	 */
	private Float endingReading;
	
	private String startDate;
	
	private String crProductsToFactory;
	
	private String crClosingStock;
	
	private String crStartingReading;
	
	private String crEndingReading;
	
	private String crOpeningStock;
	
	private String crProductsToCustomer;
	
	
	
	public String getCrProductsToCustomer() {
		return crProductsToCustomer;
	}

	public void setCrProductsToCustomer(String crProductsToCustomer) {
		this.crProductsToCustomer = crProductsToCustomer;
	}

	public String getCrProductsToFactory() {
		return crProductsToFactory;
	}

	public void setCrProductsToFactory(String crProductsToFactory) {
		this.crProductsToFactory = crProductsToFactory;
	}

	public String getCrClosingStock() {
		return crClosingStock;
	}

	public void setCrClosingStock(String crClosingStock) {
		this.crClosingStock = crClosingStock;
	}

	public String getCrStartingReading() {
		return crStartingReading;
	}

	public void setCrStartingReading(String crStartingReading) {
		this.crStartingReading = crStartingReading;
	}

	public String getCrEndingReading() {
		return crEndingReading;
	}

	public void setCrEndingReading(String crEndingReading) {
		this.crEndingReading = crEndingReading;
	}

	public String getCrOpeningStock() {
		return crOpeningStock;
	}

	public void setCrOpeningStock(String crOpeningStock) {
		this.crOpeningStock = crOpeningStock;
	}

	private String currentDate;
	
	private Integer salesBookId;
	
	private String crDescription;
	
	public String getCrDescription() {
		return crDescription;
	}

	public void setCrDescription(String crDescription) {
		this.crDescription = crDescription;
	}

	public Integer getSalesBookId() {
		return salesBookId;
	}

	public void setSalesBookId(Integer salesBookId) {
		this.salesBookId = salesBookId;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	/**
	 * Boolean variable holds isReturn
	 */
	private Boolean isReturn;
	
	private String allotmentType;
	
	public String getAllotmentType() {
		return allotmentType;
	}

	public void setAllotmentType(String allotmentType) {
		this.allotmentType = allotmentType;
	}

	public Boolean getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Boolean isReturn) {
		this.isReturn = isReturn;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * String String holds remarks
	 */
	private String remarks;
	/**
	 * String String holds driverName
	 */
	private String driverName;
	/**
	 * String variable holds amounts
	 */
	private String depositAmounts;

	/**
	 * String variable holds executiveAllowances.
	 */
	private String executiveAllowances;
	/**
	 * String variable holds driverAllowances.
	 */
	private String driverAllowances;

	/**
	 * String variable holds dayBookNo.
	 */
	private String dayBookNo;

	/**
	 * String variable holds reportingManager.
	 */
	private String reportingManager;

	/**
	 * String variable holds basicInfoRemarks.
	 */
	private String basicInfoRemarks;

	/**
	 * String variable holds allowancesRemarks.
	 */
	private String allowancesRemarks;
	/**
	 * String variable holds amountRemarks.
	 */
	private String amountRemarks;
	/**
	 * String variable holds vehicleDetailRemarks.
	 */
	private String vehicleDetailRemarks;

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
	 * Integer variable holds returnQty.
	 */
	private Integer returnQty;
	/**
	 * @return the returnQty
	 */
	public Integer getReturnQty() {
		return returnQty;
	}

	/**
	 * @param returnQty the returnQty to set
	 */
	public void setReturnQty(Integer returnQty) {
		this.returnQty = returnQty;
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

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the depositedAmounts
	 */
	public List<String> getDepositedAmounts() {
		return depositedAmounts;
	}

	/**
	 * @param depositedAmounts the depositedAmounts to set
	 */
	public void setDepositedAmounts(List<String> depositedAmounts) {
		this.depositedAmounts = depositedAmounts;
	}

	/**
	 * @return the totalExpenses
	 */
	public String getTotalExpenses() {
		return totalExpenses;
	}

	/**
	 * @param totalExpenses the totalExpenses to set
	 */
	public void setTotalExpenses(String totalExpenses) {
		this.totalExpenses = totalExpenses;
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
	 * @return the totalPayable
	 */
	public String getTotalPayable() {
		return totalPayable;
	}

	/**
	 * @param totalPayable the totalPayable to set
	 */
	public void setTotalPayable(String totalPayable) {
		this.totalPayable = totalPayable;
	}

	/**
	 * @return the totalRecieved
	 */
	public String getTotalRecieved() {
		return totalRecieved;
	}

	/**
	 * @param totalRecieved the totalRecieved to set
	 */
	public void setTotalRecieved(String totalRecieved) {
		this.totalRecieved = totalRecieved;
	}

	/**
	 * @return the balance
	 */
	public String getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(String balance) {
		this.balance = balance;
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
	 * @return the amountToFactory
	 */
	public String getAmountToFactory() {
		return amountToFactory;
	}

	/**
	 * @param amountToFactory the amountToFactory to set
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
	 * @param closingBalance the closingBalance to set
	 */
	public void setClosingBalance(String closingBalance) {
		this.closingBalance = closingBalance;
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
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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
	 * @return the depositAmounts
	 */
	public String getDepositAmounts() {
		return depositAmounts;
	}

	/**
	 * @param depositAmounts the depositAmounts to set
	 */
	public void setDepositAmounts(String depositAmounts) {
		this.depositAmounts = depositAmounts;
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
	 * @return the dayBookNo
	 */
	public String getDayBookNo() {
		return dayBookNo;
	}

	/**
	 * @param dayBookNo the dayBookNo to set
	 */
	public void setDayBookNo(String dayBookNo) {
		this.dayBookNo = dayBookNo;
	}

	/**
	 * @return the reportingManager
	 */
	public String getReportingManager() {
		return reportingManager;
	}

	/**
	 * @param reportingManager the reportingManager to set
	 */
	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}

	/**
	 * @return the basicInfoRemarks
	 */
	public String getBasicInfoRemarks() {
		return basicInfoRemarks;
	}

	/**
	 * @param basicInfoRemarks the basicInfoRemarks to set
	 */
	public void setBasicInfoRemarks(String basicInfoRemarks) {
		this.basicInfoRemarks = basicInfoRemarks;
	}

	/**
	 * @return the allowancesRemarks
	 */
	public String getAllowancesRemarks() {
		return allowancesRemarks;
	}

	/**
	 * @param allowancesRemarks the allowancesRemarks to set
	 */
	public void setAllowancesRemarks(String allowancesRemarks) {
		this.allowancesRemarks = allowancesRemarks;
	}

	/**
	 * @return the amountRemarks
	 */
	public String getAmountRemarks() {
		return amountRemarks;
	}

	/**
	 * @param amountRemarks the amountRemarks to set
	 */
	public void setAmountRemarks(String amountRemarks) {
		this.amountRemarks = amountRemarks;
	}

	/**
	 * @return the vehicleDetailRemarks
	 */
	public String getVehicleDetailRemarks() {
		return vehicleDetailRemarks;
	}

	/**
	 * @param vehicleDetailRemarks the vehicleDetailRemarks to set
	 */
	public void setVehicleDetailRemarks(String vehicleDetailRemarks) {
		this.vehicleDetailRemarks = vehicleDetailRemarks;
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
	public String getOffloadingLoadingCharges() {
		return offloadingLoadingCharges;
	}

	/**
	 * @param offloadingLoadingCharges the offloadingLoadingCharges to set
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
	 * @param dealerPartyExpenses the dealerPartyExpenses to set
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

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DayBookViewResult result) {
		int comparision = this.product.compareTo(result.getProduct());
		return comparision;
	}

}
