/**
 * com.vekomy.vbooks.customer.command.CustomerCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 15, 2013
 */
package com.vekomy.vbooks.customer.command;

import com.vekomy.vbooks.hibernate.model.VbCustomer;

/**
 * This command class is a intermediate class for {@link VbCustomer}.
 * 
 * @author Sudhakar
 * 
 */
public class CustomerCommand extends VbCustomer {

	/**
	 * long variable holdes serialVersionUID.
	 */
	private static final long serialVersionUID = -7635460977072686808L;

	/**
	 * String variable holdes action.
	 */
	private String action;
	/**
	 * Integer variable holds id
	 */
	private Integer id;
	/**
	 * 
	 */
	private Float creditLimit;
	/**
	 * 
	 */
	private Integer creditOverdueDays;
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
	 * String variable holds zipcode
	 */
	private String zipcode;
	/**
	 * String variable holds email
	 */
	private String email;
	/**
	 * String variable holds addressType
	 */
	private String addressType;
	/**
	 * String variable holds mobile
	 */
	private String mobile;
	/**
	 * String variable holds alternateMobile
	 */
	private String alternateMobile;
	/**
	 * String variable holds residencePhone
	 */
	private String residencePhone;
	/**
	 * String variable holds region
	 */
	private String region;


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.hibernate.model.VbCustomer#getId()
	 */
	public Integer getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vekomy.vbooks.hibernate.model.VbCustomer#setId(java.lang.Integer)
	 */
	public void setId(Integer id) {
		this.id = id;
	}

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
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CustomerCommand() {

	}

	/**
	 * @param id
	 */
	public CustomerCommand(Integer id) {
		this.id = id;
	}

	/**
	 * @param parameter
	 */
	public CustomerCommand(String parameter) {

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
	 * @return creditLimit
	 */
	public Float getCreditLimit() {
		return creditLimit;
	}

	/**
	 * @param creditLimit
	 *            the creditLimit to set
	 */
	public void setCreditLimit(Float creditLimit) {
		this.creditLimit = creditLimit;
	}

	/**
	 * @return creditOverdueDays
	 */
	public Integer getCreditOverdueDays() {
		return creditOverdueDays;
	}

	/**
	 * @param creditOverdueDays
	 *            the creditOverdueDays to set
	 */
	public void setCreditOverdueDays(Integer creditOverdueDays) {
		this.creditOverdueDays = creditOverdueDays;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer().append("[ action :").append(getAction())
				.append(", id :").append(getId()).append(",creditLimit :")
				.append(getCreditLimit()).append(",creditOverdueDays :")
				.append(getCreditOverdueDays()).append(", addressLine1 :")
				.append(getAddressLine1()).append(", addressLine2 :")
				.append(getAddressLine2()).append(", locality :")
				.append(getLocality()).append(", landmark :")
				.append(getLandmark()).append(", city :").append(getCity())
				.append(", state :").append(getState())
				.append(", Zipcode :").append(getZipcode()).
				append(", mobile :").append(getMobile())
				.append(", alternateMobile :").append(getResidencePhone())
				.append(getAlternateMobile()).append(", residencePhone :")
				.append(", email :").append(getEmail())
				.append(", region :").append(getRegion())
				.append("]").toString();
	}

}