package com.vekomy.vbooks.hibernate.model;

// Generated Sep 11, 2013 11:07:56 AM by Hibernate Tools 3.4.0.CR1

/**
 * VbEmployeeDetail generated by hbm2java
 */
public class VbEmployeeDetail implements java.io.Serializable {

	private Integer id;
	private VbEmployee vbEmployee;
	private String mobile;
	private String alternateMobile;
	private String directLine;
	private String bloodGroup;
	private String nationality;
	private String passportNumber;

	public VbEmployeeDetail() {
	}

	public VbEmployeeDetail(VbEmployee vbEmployee, String mobile,
			String bloodGroup, String nationality, String passportNumber) {
		this.vbEmployee = vbEmployee;
		this.mobile = mobile;
		this.bloodGroup = bloodGroup;
		this.nationality = nationality;
		this.passportNumber = passportNumber;
	}

	public VbEmployeeDetail(VbEmployee vbEmployee, String mobile,
			String alternateMobile, String directLine, String bloodGroup,
			String nationality, String passportNumber) {
		this.vbEmployee = vbEmployee;
		this.mobile = mobile;
		this.alternateMobile = alternateMobile;
		this.directLine = directLine;
		this.bloodGroup = bloodGroup;
		this.nationality = nationality;
		this.passportNumber = passportNumber;
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

	public String getDirectLine() {
		return this.directLine;
	}

	public void setDirectLine(String directLine) {
		this.directLine = directLine;
	}

	public String getBloodGroup() {
		return this.bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getNationality() {
		return this.nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getPassportNumber() {
		return this.passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

}
