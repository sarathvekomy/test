package com.vekomy.vbooks.organization.command;

import java.io.Serializable;


/**
 * @author Sudhakar
 * 
 * 
 */
public class OrganizationCommand implements Serializable{

	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -5517264990395867634L;
	/**
	 * String variable holds action
	 */
	private String action;
	/**
	 * int variable holds id
	 */
	private int id;
	/**
	 * String variable holds branchName
	 */
	private String branchName;
	/**
	 * String variable holds mainBranchName
	 */
	private String mainBranchName;
	/**
	 * String variable holds mainBranch
	 */
	private String mainBranch;
	/**
	 * String variable holds description
	 */
	private String description;
	/**
	 * String variable holds addressLine1
	 */
	private String addressLine1;
	/**
	 * String variable holds addressLine2
	 */
	private String addressLine2;
	/**
	 * String variable holds locality
	 */
	private String locality;
	/**
	 * String variable holds landmark
	 */
	private String landmark;
	/**
	 * String variable holds city
	 */
	private String city;
	/**
	 * String variable holds state
	 */
	private String state;
	/**
	 * String variable holds country
	 */
	private String country;
	/**
	 * String variable holds currencyFormat
	 */
	private String currencyFormat;
	/**
	 * String variable holds zipcode
	 */
	private String zipcode;
	/**
	 * String variable holds phone1
	 */
	private String phone1;
	/**
	 * String variable holds phone2
	 */
	private String phone2;
	/**
	 * String variable holds organizationCode
	 */
	private String organizationCode;
	/**
	 * String variable holds organizationName
	 */
	private String name;
	/**
	 * String variable holds usernamePrefix
	 */
	private String usernamePrefix;
	/**
	 * @return the usernamePrefix
	 */
	public String getUsernamePrefix() {
		return usernamePrefix;
	}
	/**
	 * @param usernamePrefix the usernamePrefix to set
	 */
	public void setUsernamePrefix(String usernamePrefix) {
		this.usernamePrefix = usernamePrefix;
	}
	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}
	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	/**
	 * @return the mainBranch
	 */
	public String getMainBranch() {
		return mainBranch;
	}
	/**
	 * @param mainBranch the mainBranch to set
	 */
	public void setMainBranch(String mainBranch) {
		this.mainBranch = mainBranch;
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
	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1() {
		return addressLine1;
	}
	/**
	 * @param addressLine1 the addressLine1 to set
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2() {
		return addressLine2;
	}
	/**
	 * @param addressLine2 the addressLine2 to set
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	/**
	 * @return the locality
	 */
	public String getLocality() {
		return locality;
	}
	/**
	 * @param locality the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}
	/**
	 * @return the landmark
	 */
	public String getLandmark() {
		return landmark;
	}
	/**
	 * @param landmark the landmark to set
	 */
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the currencyFormat
	 */
	public String getCurrencyFormat() {
		return currencyFormat;
	}
	/**
	 * @param currencyFormat the currencyFormat to set
	 */
	public void setCurrencyFormat(String currencyFormat) {
		this.currencyFormat = currencyFormat;
	}
	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}
	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	/**
	 * @return the phone1
	 */
	public String getPhone1() {
		return phone1;
	}
	/**
	 * @param phone1 the phone1 to set
	 */
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	/**
	 * @return the phone2
	 */
	public String getPhone2() {
		return phone2;
	}
	/**
	 * @param phone2 the phone2 to set
	 */
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the organizationCode
	 */
	public String getOrganizationCode() {
		return organizationCode;
	}
	/**
	 * @param organizationCode the organizationCode to set
	 */
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the mainBranchName
	 */
	public String getMainBranchName() {
		return mainBranchName;
	}
	/**
	 * @param mainBranchName the mainBranchName to set
	 */
	public void setMainBranchName(String mainBranchName) {
		this.mainBranchName = mainBranchName;
	}
	

}
