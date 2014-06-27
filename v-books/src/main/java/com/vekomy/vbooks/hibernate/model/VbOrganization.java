package com.vekomy.vbooks.hibernate.model;

// Generated Nov 19, 2013 10:32:53 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * VbOrganization generated by hbm2java
 */
public class VbOrganization implements java.io.Serializable {

	private Integer id;
	private String usernamePrefix;
	private String superUserName;
	private String name;
	private String organizationCode;
	private String fullName;
	private String branchName;
	private String mainBranch;
	private String description;
	private String addressLine1;
	private String addressLine2;
	private String locality;
	private String landmark;
	private String city;
	private String state;
	private String country;
	private String currencyFormat;
	private String zipcode;
	private String phone1;
	private String phone2;
	private String mobile;
	private String alternateMobile;
	private String email;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private Set<VbCustomerCreditInfo> vbCustomerCreditInfos = new HashSet<VbCustomerCreditInfo>(
			0);
	private Set<VbJournalTypes> vbJournalTypeses = new HashSet<VbJournalTypes>(
			0);
	private Set<VbLoginTrack> vbLoginTracks = new HashSet<VbLoginTrack>(0);
	private Set<VbDeliveryNote> vbDeliveryNotes = new HashSet<VbDeliveryNote>(0);
	private Set<VbCustomerChangeRequest> vbCustomerChangeRequests = new HashSet<VbCustomerChangeRequest>(
			0);
	private Set<VbEmployee> vbEmployees = new HashSet<VbEmployee>(0);
	private Set<VbSalesReturnChangeRequest> vbSalesReturnChangeRequests = new HashSet<VbSalesReturnChangeRequest>(
			0);
	private Set<VbDayBook> vbDayBooks = new HashSet<VbDayBook>(0);
	private Set<VbOrganizationMapping> vbOrganizationMappingsForMainBranchId = new HashSet<VbOrganizationMapping>(
			0);
	private Set<VbOrganizationMapping> vbOrganizationMappingsForSubBranchId = new HashSet<VbOrganizationMapping>(
			0);
	private Set<VbCustomerAdvanceInfo> vbCustomerAdvanceInfos = new HashSet<VbCustomerAdvanceInfo>(
			0);
	private Set<VbJournal> vbJournals = new HashSet<VbJournal>(0);
	private Set<VbLogin> vbLogins = new HashSet<VbLogin>(0);
	private Set<VbAssignOrganizations> vbAssignOrganizationses = new HashSet<VbAssignOrganizations>(
			0);
	private Set<VbSalesBook> vbSalesBooks = new HashSet<VbSalesBook>(0);
	private Set<VbUserDefinedAlerts> vbUserDefinedAlertses = new HashSet<VbUserDefinedAlerts>(
			0);
	private Set<VbInsystemAlertNotifications> vbInsystemAlertNotificationses = new HashSet<VbInsystemAlertNotifications>(
			0);
	private Set<VbCustomerDebitTransaction> vbCustomerDebitTransactions = new HashSet<VbCustomerDebitTransaction>(
			0);
	private Set<VbProductInventoryTransaction> vbProductInventoryTransactions = new HashSet<VbProductInventoryTransaction>(
			0);
	private Set<VbCashDayBookCr> vbCashDayBookCrs = new HashSet<VbCashDayBookCr>(
			0);
	private Set<VbDeliveryNoteChangeRequest> vbDeliveryNoteChangeRequests = new HashSet<VbDeliveryNoteChangeRequest>(
			0);
	private Set<VbSalesReturn> vbSalesReturns = new HashSet<VbSalesReturn>(0);
	private Set<VbSystemNotifications> vbSystemNotificationses = new HashSet<VbSystemNotifications>(
			0);
	private Set<VbDayBookChangeRequest> vbDayBookChangeRequests = new HashSet<VbDayBookChangeRequest>(
			0);
	private Set<VbCustomerCreditTransaction> vbCustomerCreditTransactions = new HashSet<VbCustomerCreditTransaction>(
			0);
	private Set<VbProduct> vbProducts = new HashSet<VbProduct>(0);
	private Set<VbAddressTypes> vbAddressTypeses = new HashSet<VbAddressTypes>(
			0);
	private Set<VbProductCategories> vbProductCategorieses = new HashSet<VbProductCategories>(
			0);
	private Set<VbProductCustomerCost> vbProductCustomerCosts = new HashSet<VbProductCustomerCost>(
			0);
	private Set<VbCustomer> vbCustomers = new HashSet<VbCustomer>(0);
	private Set<VbInvoiceNoPeriod> vbInvoiceNoPeriods = new HashSet<VbInvoiceNoPeriod>(
			0);
	private Set<VbEmployeeCustomer> vbEmployeeCustomers = new HashSet<VbEmployeeCustomer>(
			0);
	private Set<VbJournalChangeRequest> vbJournalChangeRequests = new HashSet<VbJournalChangeRequest>(
			0);
	private Set<VbCashDayBook> vbCashDayBooks = new HashSet<VbCashDayBook>(0);
	private Set<VbSystemAlerts> vbSystemAlertses = new HashSet<VbSystemAlerts>(
			0);
	private Set<VbPaymentTypes> vbPaymentTypeses = new HashSet<VbPaymentTypes>(
			0);

	public VbOrganization() {
	}

	public VbOrganization(String userNamePrefix, String superUserName,
			String name, String organizationCode, String fullName,
			String branchName, String mainBranch, String addressLine1,
			String locality, String city, String state, String country,
			String currencyFormat, String zipcode, String email, Date modifiedOn) {
		this.usernamePrefix = userNamePrefix;
		this.superUserName = superUserName;
		this.name = name;
		this.organizationCode = organizationCode;
		this.fullName = fullName;
		this.branchName = branchName;
		this.mainBranch = mainBranch;
		this.addressLine1 = addressLine1;
		this.locality = locality;
		this.city = city;
		this.state = state;
		this.country = country;
		this.currencyFormat = currencyFormat;
		this.zipcode = zipcode;
		this.email = email;
		this.modifiedOn = modifiedOn;
	}

	public VbOrganization(String userNamePrefix, String superUserName,
			String name, String organizationCode, String fullName,
			String branchName, String mainBranch, String description,
			String addressLine1, String addressLine2, String locality,
			String landmark, String city, String state, String country,
			String currencyFormat, String zipcode, String phone1,
			String phone2, String mobile, String alternateMobile, String email,
			Date createdOn, String createdBy, Date modifiedOn,
			String modifiedBy, Set<VbCustomerCreditInfo> vbCustomerCreditInfos,
			Set<VbJournalTypes> vbJournalTypeses,
			Set<VbLoginTrack> vbLoginTracks,
			Set<VbDeliveryNote> vbDeliveryNotes,
			Set<VbCustomerChangeRequest> vbCustomerChangeRequests,
			Set<VbEmployee> vbEmployees,
			Set<VbSalesReturnChangeRequest> vbSalesReturnChangeRequests,
			Set<VbDayBook> vbDayBooks,
			Set<VbOrganizationMapping> vbOrganizationMappingsForMainBranchId,
			Set<VbOrganizationMapping> vbOrganizationMappingsForSubBranchId,
			Set<VbCustomerAdvanceInfo> vbCustomerAdvanceInfos,
			Set<VbJournal> vbJournals, Set<VbLogin> vbLogins,
			Set<VbAssignOrganizations> vbAssignOrganizationses,
			Set<VbSalesBook> vbSalesBooks,
			Set<VbUserDefinedAlerts> vbUserDefinedAlertses,
			Set<VbInsystemAlertNotifications> vbInsystemAlertNotificationses,
			Set<VbCustomerDebitTransaction> vbCustomerDebitTransactions,
			Set<VbProductInventoryTransaction> vbProductInventoryTransactions,
			Set<VbCashDayBookCr> vbCashDayBookCrs,
			Set<VbDeliveryNoteChangeRequest> vbDeliveryNoteChangeRequests,
			Set<VbSalesReturn> vbSalesReturns,
			Set<VbSystemNotifications> vbSystemNotificationses,
			Set<VbDayBookChangeRequest> vbDayBookChangeRequests,
			Set<VbCustomerCreditTransaction> vbCustomerCreditTransactions,
			Set<VbProduct> vbProducts, Set<VbAddressTypes> vbAddressTypeses,
			Set<VbProductCategories> vbProductCategorieses,
			Set<VbProductCustomerCost> vbProductCustomerCosts,
			Set<VbCustomer> vbCustomers,
			Set<VbInvoiceNoPeriod> vbInvoiceNoPeriods,
			Set<VbEmployeeCustomer> vbEmployeeCustomers,
			Set<VbJournalChangeRequest> vbJournalChangeRequests,
			Set<VbCashDayBook> vbCashDayBooks,
			Set<VbSystemAlerts> vbSystemAlertses,
			Set<VbPaymentTypes> vbPaymentTypeses) {
		this.usernamePrefix = userNamePrefix;
		this.superUserName = superUserName;
		this.name = name;
		this.organizationCode = organizationCode;
		this.fullName = fullName;
		this.branchName = branchName;
		this.mainBranch = mainBranch;
		this.description = description;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.locality = locality;
		this.landmark = landmark;
		this.city = city;
		this.state = state;
		this.country = country;
		this.currencyFormat = currencyFormat;
		this.zipcode = zipcode;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.mobile = mobile;
		this.alternateMobile = alternateMobile;
		this.email = email;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.vbCustomerCreditInfos = vbCustomerCreditInfos;
		this.vbJournalTypeses = vbJournalTypeses;
		this.vbLoginTracks = vbLoginTracks;
		this.vbDeliveryNotes = vbDeliveryNotes;
		this.vbCustomerChangeRequests = vbCustomerChangeRequests;
		this.vbEmployees = vbEmployees;
		this.vbSalesReturnChangeRequests = vbSalesReturnChangeRequests;
		this.vbDayBooks = vbDayBooks;
		this.vbOrganizationMappingsForMainBranchId = vbOrganizationMappingsForMainBranchId;
		this.vbOrganizationMappingsForSubBranchId = vbOrganizationMappingsForSubBranchId;
		this.vbCustomerAdvanceInfos = vbCustomerAdvanceInfos;
		this.vbJournals = vbJournals;
		this.vbLogins = vbLogins;
		this.vbAssignOrganizationses = vbAssignOrganizationses;
		this.vbSalesBooks = vbSalesBooks;
		this.vbUserDefinedAlertses = vbUserDefinedAlertses;
		this.vbInsystemAlertNotificationses = vbInsystemAlertNotificationses;
		this.vbCustomerDebitTransactions = vbCustomerDebitTransactions;
		this.vbProductInventoryTransactions = vbProductInventoryTransactions;
		this.vbCashDayBookCrs = vbCashDayBookCrs;
		this.vbDeliveryNoteChangeRequests = vbDeliveryNoteChangeRequests;
		this.vbSalesReturns = vbSalesReturns;
		this.vbSystemNotificationses = vbSystemNotificationses;
		this.vbDayBookChangeRequests = vbDayBookChangeRequests;
		this.vbCustomerCreditTransactions = vbCustomerCreditTransactions;
		this.vbProducts = vbProducts;
		this.vbAddressTypeses = vbAddressTypeses;
		this.vbProductCategorieses = vbProductCategorieses;
		this.vbProductCustomerCosts = vbProductCustomerCosts;
		this.vbCustomers = vbCustomers;
		this.vbInvoiceNoPeriods = vbInvoiceNoPeriods;
		this.vbEmployeeCustomers = vbEmployeeCustomers;
		this.vbJournalChangeRequests = vbJournalChangeRequests;
		this.vbCashDayBooks = vbCashDayBooks;
		this.vbSystemAlertses = vbSystemAlertses;
		this.vbPaymentTypeses = vbPaymentTypeses;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getUsernamePrefix() {
		return usernamePrefix;
	}

	public void setUsernamePrefix(String usernamePrefix) {
		this.usernamePrefix = usernamePrefix;
	}

	public String getSuperUserName() {
		return this.superUserName;
	}

	public void setSuperUserName(String superUserName) {
		this.superUserName = superUserName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrganizationCode() {
		return this.organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getBranchName() {
		return this.branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getMainBranch() {
		return this.mainBranch;
	}

	public void setMainBranch(String mainBranch) {
		this.mainBranch = mainBranch;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddressLine1() {
		return this.addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return this.addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getLocality() {
		return this.locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getLandmark() {
		return this.landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrencyFormat() {
		return this.currencyFormat;
	}

	public void setCurrencyFormat(String currencyFormat) {
		this.currencyFormat = currencyFormat;
	}

	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getPhone1() {
		return this.phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return this.phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAlternateMobile() {
		return this.alternateMobile;
	}

	public void setAlternateMobile(String alternateMobile) {
		this.alternateMobile = alternateMobile;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedOn() {
		return this.modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Set<VbCustomerCreditInfo> getVbCustomerCreditInfos() {
		return this.vbCustomerCreditInfos;
	}

	public void setVbCustomerCreditInfos(
			Set<VbCustomerCreditInfo> vbCustomerCreditInfos) {
		this.vbCustomerCreditInfos = vbCustomerCreditInfos;
	}

	public Set<VbJournalTypes> getVbJournalTypeses() {
		return this.vbJournalTypeses;
	}

	public void setVbJournalTypeses(Set<VbJournalTypes> vbJournalTypeses) {
		this.vbJournalTypeses = vbJournalTypeses;
	}

	public Set<VbLoginTrack> getVbLoginTracks() {
		return this.vbLoginTracks;
	}

	public void setVbLoginTracks(Set<VbLoginTrack> vbLoginTracks) {
		this.vbLoginTracks = vbLoginTracks;
	}

	public Set<VbDeliveryNote> getVbDeliveryNotes() {
		return this.vbDeliveryNotes;
	}

	public void setVbDeliveryNotes(Set<VbDeliveryNote> vbDeliveryNotes) {
		this.vbDeliveryNotes = vbDeliveryNotes;
	}

	public Set<VbCustomerChangeRequest> getVbCustomerChangeRequests() {
		return this.vbCustomerChangeRequests;
	}

	public void setVbCustomerChangeRequests(
			Set<VbCustomerChangeRequest> vbCustomerChangeRequests) {
		this.vbCustomerChangeRequests = vbCustomerChangeRequests;
	}

	public Set<VbEmployee> getVbEmployees() {
		return this.vbEmployees;
	}

	public void setVbEmployees(Set<VbEmployee> vbEmployees) {
		this.vbEmployees = vbEmployees;
	}

	public Set<VbSalesReturnChangeRequest> getVbSalesReturnChangeRequests() {
		return this.vbSalesReturnChangeRequests;
	}

	public void setVbSalesReturnChangeRequests(
			Set<VbSalesReturnChangeRequest> vbSalesReturnChangeRequests) {
		this.vbSalesReturnChangeRequests = vbSalesReturnChangeRequests;
	}

	public Set<VbDayBook> getVbDayBooks() {
		return this.vbDayBooks;
	}

	public void setVbDayBooks(Set<VbDayBook> vbDayBooks) {
		this.vbDayBooks = vbDayBooks;
	}

	public Set<VbOrganizationMapping> getVbOrganizationMappingsForMainBranchId() {
		return this.vbOrganizationMappingsForMainBranchId;
	}

	public void setVbOrganizationMappingsForMainBranchId(
			Set<VbOrganizationMapping> vbOrganizationMappingsForMainBranchId) {
		this.vbOrganizationMappingsForMainBranchId = vbOrganizationMappingsForMainBranchId;
	}

	public Set<VbOrganizationMapping> getVbOrganizationMappingsForSubBranchId() {
		return this.vbOrganizationMappingsForSubBranchId;
	}

	public void setVbOrganizationMappingsForSubBranchId(
			Set<VbOrganizationMapping> vbOrganizationMappingsForSubBranchId) {
		this.vbOrganizationMappingsForSubBranchId = vbOrganizationMappingsForSubBranchId;
	}

	public Set<VbCustomerAdvanceInfo> getVbCustomerAdvanceInfos() {
		return this.vbCustomerAdvanceInfos;
	}

	public void setVbCustomerAdvanceInfos(
			Set<VbCustomerAdvanceInfo> vbCustomerAdvanceInfos) {
		this.vbCustomerAdvanceInfos = vbCustomerAdvanceInfos;
	}

	public Set<VbJournal> getVbJournals() {
		return this.vbJournals;
	}

	public void setVbJournals(Set<VbJournal> vbJournals) {
		this.vbJournals = vbJournals;
	}

	public Set<VbLogin> getVbLogins() {
		return this.vbLogins;
	}

	public void setVbLogins(Set<VbLogin> vbLogins) {
		this.vbLogins = vbLogins;
	}

	public Set<VbAssignOrganizations> getVbAssignOrganizationses() {
		return this.vbAssignOrganizationses;
	}

	public void setVbAssignOrganizationses(
			Set<VbAssignOrganizations> vbAssignOrganizationses) {
		this.vbAssignOrganizationses = vbAssignOrganizationses;
	}

	public Set<VbSalesBook> getVbSalesBooks() {
		return this.vbSalesBooks;
	}

	public void setVbSalesBooks(Set<VbSalesBook> vbSalesBooks) {
		this.vbSalesBooks = vbSalesBooks;
	}

	public Set<VbUserDefinedAlerts> getVbUserDefinedAlertses() {
		return this.vbUserDefinedAlertses;
	}

	public void setVbUserDefinedAlertses(
			Set<VbUserDefinedAlerts> vbUserDefinedAlertses) {
		this.vbUserDefinedAlertses = vbUserDefinedAlertses;
	}

	public Set<VbInsystemAlertNotifications> getVbInsystemAlertNotificationses() {
		return this.vbInsystemAlertNotificationses;
	}

	public void setVbInsystemAlertNotificationses(
			Set<VbInsystemAlertNotifications> vbInsystemAlertNotificationses) {
		this.vbInsystemAlertNotificationses = vbInsystemAlertNotificationses;
	}

	public Set<VbCustomerDebitTransaction> getVbCustomerDebitTransactions() {
		return this.vbCustomerDebitTransactions;
	}

	public void setVbCustomerDebitTransactions(
			Set<VbCustomerDebitTransaction> vbCustomerDebitTransactions) {
		this.vbCustomerDebitTransactions = vbCustomerDebitTransactions;
	}

	public Set<VbProductInventoryTransaction> getVbProductInventoryTransactions() {
		return this.vbProductInventoryTransactions;
	}

	public void setVbProductInventoryTransactions(
			Set<VbProductInventoryTransaction> vbProductInventoryTransactions) {
		this.vbProductInventoryTransactions = vbProductInventoryTransactions;
	}

	public Set<VbCashDayBookCr> getVbCashDayBookCrs() {
		return this.vbCashDayBookCrs;
	}

	public void setVbCashDayBookCrs(Set<VbCashDayBookCr> vbCashDayBookCrs) {
		this.vbCashDayBookCrs = vbCashDayBookCrs;
	}

	public Set<VbDeliveryNoteChangeRequest> getVbDeliveryNoteChangeRequests() {
		return this.vbDeliveryNoteChangeRequests;
	}

	public void setVbDeliveryNoteChangeRequests(
			Set<VbDeliveryNoteChangeRequest> vbDeliveryNoteChangeRequests) {
		this.vbDeliveryNoteChangeRequests = vbDeliveryNoteChangeRequests;
	}

	public Set<VbSalesReturn> getVbSalesReturns() {
		return this.vbSalesReturns;
	}

	public void setVbSalesReturns(Set<VbSalesReturn> vbSalesReturns) {
		this.vbSalesReturns = vbSalesReturns;
	}

	public Set<VbSystemNotifications> getVbSystemNotificationses() {
		return this.vbSystemNotificationses;
	}

	public void setVbSystemNotificationses(
			Set<VbSystemNotifications> vbSystemNotificationses) {
		this.vbSystemNotificationses = vbSystemNotificationses;
	}

	public Set<VbDayBookChangeRequest> getVbDayBookChangeRequests() {
		return this.vbDayBookChangeRequests;
	}

	public void setVbDayBookChangeRequests(
			Set<VbDayBookChangeRequest> vbDayBookChangeRequests) {
		this.vbDayBookChangeRequests = vbDayBookChangeRequests;
	}

	public Set<VbCustomerCreditTransaction> getVbCustomerCreditTransactions() {
		return this.vbCustomerCreditTransactions;
	}

	public void setVbCustomerCreditTransactions(
			Set<VbCustomerCreditTransaction> vbCustomerCreditTransactions) {
		this.vbCustomerCreditTransactions = vbCustomerCreditTransactions;
	}

	public Set<VbProduct> getVbProducts() {
		return this.vbProducts;
	}

	public void setVbProducts(Set<VbProduct> vbProducts) {
		this.vbProducts = vbProducts;
	}

	public Set<VbAddressTypes> getVbAddressTypeses() {
		return this.vbAddressTypeses;
	}

	public void setVbAddressTypeses(Set<VbAddressTypes> vbAddressTypeses) {
		this.vbAddressTypeses = vbAddressTypeses;
	}

	public Set<VbProductCategories> getVbProductCategorieses() {
		return this.vbProductCategorieses;
	}

	public void setVbProductCategorieses(
			Set<VbProductCategories> vbProductCategorieses) {
		this.vbProductCategorieses = vbProductCategorieses;
	}

	public Set<VbProductCustomerCost> getVbProductCustomerCosts() {
		return this.vbProductCustomerCosts;
	}

	public void setVbProductCustomerCosts(
			Set<VbProductCustomerCost> vbProductCustomerCosts) {
		this.vbProductCustomerCosts = vbProductCustomerCosts;
	}

	public Set<VbCustomer> getVbCustomers() {
		return this.vbCustomers;
	}

	public void setVbCustomers(Set<VbCustomer> vbCustomers) {
		this.vbCustomers = vbCustomers;
	}

	public Set<VbInvoiceNoPeriod> getVbInvoiceNoPeriods() {
		return this.vbInvoiceNoPeriods;
	}

	public void setVbInvoiceNoPeriods(Set<VbInvoiceNoPeriod> vbInvoiceNoPeriods) {
		this.vbInvoiceNoPeriods = vbInvoiceNoPeriods;
	}

	public Set<VbEmployeeCustomer> getVbEmployeeCustomers() {
		return this.vbEmployeeCustomers;
	}

	public void setVbEmployeeCustomers(
			Set<VbEmployeeCustomer> vbEmployeeCustomers) {
		this.vbEmployeeCustomers = vbEmployeeCustomers;
	}

	public Set<VbJournalChangeRequest> getVbJournalChangeRequests() {
		return this.vbJournalChangeRequests;
	}

	public void setVbJournalChangeRequests(
			Set<VbJournalChangeRequest> vbJournalChangeRequests) {
		this.vbJournalChangeRequests = vbJournalChangeRequests;
	}

	public Set<VbCashDayBook> getVbCashDayBooks() {
		return this.vbCashDayBooks;
	}

	public void setVbCashDayBooks(Set<VbCashDayBook> vbCashDayBooks) {
		this.vbCashDayBooks = vbCashDayBooks;
	}

	public Set<VbSystemAlerts> getVbSystemAlertses() {
		return this.vbSystemAlertses;
	}

	public void setVbSystemAlertses(Set<VbSystemAlerts> vbSystemAlertses) {
		this.vbSystemAlertses = vbSystemAlertses;
	}

	public Set<VbPaymentTypes> getVbPaymentTypeses() {
		return this.vbPaymentTypeses;
	}

	public void setVbPaymentTypeses(Set<VbPaymentTypes> vbPaymentTypeses) {
		this.vbPaymentTypeses = vbPaymentTypeses;
	}

}
