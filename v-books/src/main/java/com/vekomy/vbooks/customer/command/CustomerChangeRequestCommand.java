/**
 * com.vekomy.vbooks.customer.command.CustomerChangeRequestCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: may 9, 2013
 */
package com.vekomy.vbooks.customer.command;

import com.vekomy.vbooks.hibernate.model.VbCustomerChangeRequest;

/**This command class is a intermediate class for {@link VbCustomerChangeRequest}.
 * 
 * @author priyanka
 *
 */
public class CustomerChangeRequestCommand extends VbCustomerChangeRequest {
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 1404004452239469496L;
	/**
	 * String variable holds action
	 */
	private String action;
	/**
	 * String variable holds fullname
	 */
	private String customerName;
	/**
	 * String variable holds invoiceName
	 */
	private String invoiceName;
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
	 * String variable holds creditLimit
	 */
	
	private String creditLimit;
	/**
	 * String variable holds region
	 */
	private String region;
	/**
	 * String variable holds creditOverdueDays
	 */
	private String creditOverdueDays;
	
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
	 * String variable holds email
	 */
	private String email;
	/**
	 * String variable holds directLine
	 */
	private String directLine;
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
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the invoiceName
	 */
	public String getInvoiceName() {
		return invoiceName;
	}
	/**
	 * @param invoiceName the invoiceName to set
	 */
	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
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
	 * @return the creditLimit
	 */
	public String getCreditLimit() {
		return creditLimit;
	}
	/**
	 * @param creditLimit the creditLimit to set
	 */
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}
	/**
	 * @return the creditOverdueDays
	 */
	public String getCreditOverdueDays() {
		return creditOverdueDays;
	}
	/**
	 * @param creditOverdueDays the creditOverdueDays to set
	 */
	public void setCreditOverdueDays(String creditOverdueDays) {
		this.creditOverdueDays = creditOverdueDays;
	}
	/**
	 * @return the addressType
	 */
	public String getAddressType() {
		return addressType;
	}
	/**
	 * @param addressType the addressType to set
	 */
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * @return the alternateMobile
	 */
	public String getAlternateMobile() {
		return alternateMobile;
	}
	/**
	 * @param alternateMobile the alternateMobile to set
	 */
	public void setAlternateMobile(String alternateMobile) {
		this.alternateMobile = alternateMobile;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the directLine
	 */
	public String getDirectLine() {
		return directLine;
	}
	/**
	 * @param directLine the directLine to set
	 */
	public void setDirectLine(String directLine) {
		this.directLine = directLine;
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
	
}
