package com.vekomy.vbooks.hibernate.model;

// Generated Jul 16, 2013 11:21:50 AM by Hibernate Tools 3.4.0.CR1

/**
 * VbEmployeeAddress generated by hbm2java
 */
public class VbEmployeeAddress implements java.io.Serializable {

	private Integer id;
	private VbEmployee vbEmployee;
	private String addressLine1;
	private String addressLine2;
	private String locality;
	private String landmark;
	private String city;
	private String state;
	private String zipcode;
	private String addressType;

	public VbEmployeeAddress() {
	}

	public VbEmployeeAddress(VbEmployee vbEmployee, String addressLine1,
			String locality, String city, String state, String zipcode,
			String addressType) {
		this.vbEmployee = vbEmployee;
		this.addressLine1 = addressLine1;
		this.locality = locality;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
		this.addressType = addressType;
	}

	public VbEmployeeAddress(VbEmployee vbEmployee, String addressLine1,
			String addressLine2, String locality, String landmark, String city,
			String state, String zipcode, String addressType) {
		this.vbEmployee = vbEmployee;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.locality = locality;
		this.landmark = landmark;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
		this.addressType = addressType;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VbEmployee getVbEmployee() {
		return this.vbEmployee;
	}

	public void setVbEmployee(VbEmployee vbEmployee) {
		this.vbEmployee = vbEmployee;
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

}
