/**
 * com.vekomy.vbooks.employee.command.EmployeeCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: may 9, 2013
 */

package com.vekomy.vbooks.employee.command;

import com.vekomy.vbooks.hibernate.model.VbEmployee;

/**
 * @author Sudhakar
 * 
 * 
 */
public class EmployeeCommand extends VbEmployee {

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 317669098570110480L;
	/**
	 * String variable holdes action.
	 */
	private String action;
	/**
	 * String variable holdes password.
	 */
	private String password;
	/**
	 * String variable holdes genderspecific.
	 */
	private String genderspecific;
	/**
	 * String variable holdes mobile.
	 */
	private String mobile;
	/**
	 * String variable holdes alternateMobile.
	 */
	private String alternateMobile;
	/**
	 * String variable holdes residencePhone.
	 */
	private String residencePhone;
	/**
	 * String variable holdes bloodGroup.
	 */
	private String bloodGroup;
	/**
	 * String variable holdes placeOfBirth.
	 */
	private String placeOfBirth;
	/**
	 * String variable holdes nationality.
	 */
	private String nationality;
	/**
	 * String variable holdes addressLine1.
	 */
	private String addressLine1;
	/**
	 * String variable holdes addressLine2.
	 */
	private String addressLine2;
	/**
	 * String variable holdes locality.
	 */
	private String locality;
	/**
	 * String variable holdes landmark.
	 */
	private String landmark;
	/**
	 * String variable holdes city.
	 */
	private String city;
	/**
	 * String variable holdes state.
	 */
	private String state;
	/**
	 * String variable holdes zipcode.
	 */
	private String zipcode;
	/**
	 * String variable holdes addressType.
	 */
	private String addressType;
	/**
	 * String variable holds region
	 */
	private String region;
	
	/**
	 * String variable holds orgPrefix
	 */
	private String orgPrefix;

	/**
	 * @return addressLine1
	 */
	public String getAddressLine1() {
		return addressLine1;
	}

	/**
	 * @param addressLine1
	 *            the addressLine1 to set
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	/**
	 * @return addressLine2
	 */
	public String getAddressLine2() {
		return addressLine2;
	}

	/**
	 * @param addressLine2
	 *            the addressLine2 to set
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	/**
	 * @return locality
	 */
	public String getLocality() {
		return locality;
	}

	/**
	 * @param locality
	 *            ths locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}

	/**
	 * @return landmark
	 */
	public String getLandmark() {
		return landmark;
	}

	/**
	 * @param landmark
	 *            the landmark to set
	 */
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	/**
	 * @return city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipcode
	 *            the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return addressType
	 */
	public String getAddressType() {
		return addressType;
	}

	/**
	 * @param addressType
	 *            the addressType to set
	 */
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	/**
	 * @return mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return alternateMobile
	 */
	public String getAlternateMobile() {
		return alternateMobile;
	}

	/**
	 * @param alternateMobile
	 *            the alternateMobile to set
	 */
	public void setAlternateMobile(String alternateMobile) {
		this.alternateMobile = alternateMobile;
	}

	/**
	 * @return residencePhone
	 */
	public String getResidencePhone() {
		return residencePhone;
	}

	/**
	 * @param residencePhone
	 *            the residencePhone to set
	 */
	public void setResidencePhone(String residencePhone) {
		this.residencePhone = residencePhone;
	}

	/**
	 * @return bloodGroup
	 */
	public String getBloodGroup() {
		return bloodGroup;
	}

	/**
	 * @param bloodGroup
	 *            the bloodGroup to set
	 */
	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	/**
	 * @return placeOfBirth
	 */
	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	/**
	 * @param placeOfBirth
	 *            the placeOfBirth to set
	 */
	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	/**
	 * @return nationality
	 */
	public String getNationality() {
		return nationality;
	}

	/**
	 * @param nationality
	 *            the nationality to set
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	/**
	 * @return genderspecific
	 */
	public String getGenderspecific() {
		return genderspecific;
	}

	/**
	 * @param genderspecific
	 *            the genderspecific to set
	 */
	public void setGenderspecific(String genderspecific) {
		this.genderspecific = genderspecific;
	}

	public EmployeeCommand() {

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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the orgPrefix
	 */
	public String getOrgPrefix() {
		return orgPrefix;
	}

	/**
	 * @param orgPrefix the orgPrefix to set
	 */
	public void setOrgPrefix(String orgPrefix) {
		this.orgPrefix = orgPrefix;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer().append("[ action :").append(getAction())
				.append("password:").append(getPassword())
				.append(",placeOfBirth :").append(getPlaceOfBirth())
				.append(",bloodGroup :").append(getBloodGroup())
				.append(", addressLine1 :").append(getAddressLine1())
				.append(",addressLine2 :").append(getAddressLine2())
				.append(", locality :").append(getLocality())
				.append(",landmark :").append(getLandmark()).append(", city :")
				.append(getCity()).append(", state :").append(getState())
				.append(",zipcode :").append(getZipcode())
				.append(", addressType :").append(getAddressType())
				.append(", mobile :").append(getMobile())
				.append(",alternateMobile :").append(getAlternateMobile())
				.append(", residencePhone :").append(getResidencePhone())
				.append(", nationality :").append(getNationality()).append("]")
				.toString();
	}

}
