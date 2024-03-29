package com.vekomy.vbooks.app.hibernate.model;

// Generated Jul 4, 2013 9:22:12 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * VbCustomerChangeRequestDetails generated by hbm2java
 */
public class VbCustomerChangeRequestDetails implements java.io.Serializable {

	private Integer id;
	private VbCustomerChangeRequest vbCustomerChangeRequest;
	private String customerName;
	private String invoiceName;
	private String addressLine1;
	private String addressLine2;
	private String region;
	private String locality;
	private String landmark;
	private String city;
	private String state;
	private String zipcode;
	private String addressType;
	private String mobile;
	private String alternateMobile;
	private String email;
	private String directLine;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;

	public VbCustomerChangeRequestDetails() {
	}

	public VbCustomerChangeRequestDetails(
			VbCustomerChangeRequest vbCustomerChangeRequest,
			String customerName, String invoiceName, String region,
			String locality, String city, String state, String zipcode,
			String addressType, String mobile, String email, Date modifiedDate) {
		this.vbCustomerChangeRequest = vbCustomerChangeRequest;
		this.customerName = customerName;
		this.invoiceName = invoiceName;
		this.region = region;
		this.locality = locality;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
		this.addressType = addressType;
		this.mobile = mobile;
		this.email = email;
		this.modifiedDate = modifiedDate;
	}

	public VbCustomerChangeRequestDetails(
			VbCustomerChangeRequest vbCustomerChangeRequest,
			String customerName, String invoiceName, String addressLine1,
			String addressLine2, String region, String locality,
			String landmark, String city, String state, String zipcode,
			String addressType, String mobile, String alternateMobile,
			String email, String directLine, String createdBy,
			Date createdDate, String modifiedBy, Date modifiedDate) {
		this.vbCustomerChangeRequest = vbCustomerChangeRequest;
		this.customerName = customerName;
		this.invoiceName = invoiceName;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.region = region;
		this.locality = locality;
		this.landmark = landmark;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
		this.addressType = addressType;
		this.mobile = mobile;
		this.alternateMobile = alternateMobile;
		this.email = email;
		this.directLine = directLine;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VbCustomerChangeRequest getVbCustomerChangeRequest() {
		return this.vbCustomerChangeRequest;
	}

	public void setVbCustomerChangeRequest(
			VbCustomerChangeRequest vbCustomerChangeRequest) {
		this.vbCustomerChangeRequest = vbCustomerChangeRequest;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getInvoiceName() {
		return this.invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
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

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
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

	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getAddressType() {
		return this.addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
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

	public String getDirectLine() {
		return this.directLine;
	}

	public void setDirectLine(String directLine) {
		this.directLine = directLine;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
