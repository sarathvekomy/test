/**
 * com.vekomy.vbooks.customer.command.CustomerResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 15, 2013
 */

package com.vekomy.vbooks.customer.command;

import java.util.Date;

import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbOrganization;

/**
 * This Class Is Responsible to hold CustomerResultData.
 * @author priyanka
 *
 */
public class CustomerResult implements java.io.Serializable {
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 553654820338520893L;
	/**
	 * Integer variable holds id.
	 */
	private Integer id;
	/**
	 * VbCustomer variable holds vbCustomer.
	 */
	private VbCustomer vbCustomer;
	/**
	 * String variable holds addressLine1.
	 */
	private String addressLine1;
	/**
	 * String variable holds addressLine2.
	 */
	private String addressLine2;
	/**
	 * String variable holds locality.
	 */
	private String locality;
	/**
	 * String variable holds landmark.
	 */
	private String landmark;
	/**
	 * String variable holds city.
	 */
	private String city;
	/**
	 * String variable holds state.
	 */
	private String state;
	/**
	 * String variable holds zipcode
	 */
	private String zipcode;
	/**
	 * String variable holds mobile.
	 */
	private String mobile;
	/**
	 * String variable holds alternateMobile.
	 */
	private String alternateMobile;
	/**
	 * String variable holds residencePhone.
	 */
	private String residencePhone;
	/**
	 * String variable holds email.
	 */
	private String email;
	/**
	 * VbOrganization variable holds vbOrganization.
	 */
	private VbOrganization vbOrganization;
	/**
	 * String variable holds businessName.
	 */
	private String businessName;
	/**
	 * String variable holds invoiceName.
	 */
	private String invoiceName;
	/**
	 * String variable holds fullname.
	 */
	private String customerName;
	/**
	 * Character variable holds gender.
	 */
	private Character gender;
	/**
	 * Float variable holds creditLimit.
	 */
	private Float creditLimit;
	/**
	 * Integer variable holds creditOverdueDays.
	 */
	private Integer creditOverdueDays;
	/**
	 * String variable holds createdBy.
	 */
	private String createdBy;
	/**
	 * Date variable holds createdDate.
	 */
	private Date createdDate;
	/**
	 * String variable holds modifiedBy.
	 */
	private String modifiedBy;
	/**
	 * Date variable holds modifiedDate.
	 */
	private Date modifiedDate;
	/**
	 * String variable holds crType.
	 */
	private String crType;
	
	/**
	 * String variable holds region
	 */
	private String region;
	/**
	 * String variable holds directLine
	 */
	private String directLine;
	
	/**
	 * String variable holds customerState
	 */
	private String customerState;
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
	 * @return the vbCustomer
	 */
	public VbCustomer getVbCustomer() {
		return vbCustomer;
	}
	/**
	 * @param vbCustomer the vbCustomer to set
	 */
	public void setVbCustomer(VbCustomer vbCustomer) {
		this.vbCustomer = vbCustomer;
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
	 * @return the residencePhone
	 */
	public String getResidencePhone() {
		return residencePhone;
	}
	/**
	 * @param residencePhone the residencePhone to set
	 */
	public void setResidencePhone(String residencePhone) {
		this.residencePhone = residencePhone;
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
	 * @return the vbOrganization
	 */
	public VbOrganization getVbOrganization() {
		return vbOrganization;
	}
	/**
	 * @param vbOrganization the vbOrganization to set
	 */
	public void setVbOrganization(VbOrganization vbOrganization) {
		this.vbOrganization = vbOrganization;
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
	 * @return the gender
	 */
	public Character getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(Character gender) {
		this.gender = gender;
	}
	/**
	 * @return the creditLimit
	 */
	public Float getCreditLimit() {
		return creditLimit;
	}
	/**
	 * @param creditLimit the creditLimit to set
	 */
	public void setCreditLimit(Float creditLimit) {
		this.creditLimit = creditLimit;
	}
	/**
	 * @return the creditOverdueDays
	 */
	public Integer getCreditOverdueDays() {
		return creditOverdueDays;
	}
	/**
	 * @param creditOverdueDays the creditOverdueDays to set
	 */
	public void setCreditOverdueDays(Integer creditOverdueDays) {
		this.creditOverdueDays = creditOverdueDays;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}
	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}
	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	/**
	 * @return the crType
	 */
	public String getCrType() {
		return crType;
	}
	/**
	 * @param crType the crType to set
	 */
	public void setCrType(String crType) {
		this.crType = crType;
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
	public String getCustomerState() {
		return customerState;
	}
	public void setCustomerState(String customerState) {
		this.customerState = customerState;
	}
	

}
