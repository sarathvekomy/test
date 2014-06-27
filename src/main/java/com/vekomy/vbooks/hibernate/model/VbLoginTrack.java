package com.vekomy.vbooks.hibernate.model;

// Generated Sep 11, 2013 11:07:56 AM by Hibernate Tools 3.4.0.CR1

/**
 * VbLoginTrack generated by hbm2java
 */
public class VbLoginTrack implements java.io.Serializable {

	private Integer id;
	private VbOrganization vbOrganization;
	private String username;
	private String lastLoginTime;

	public VbLoginTrack() {
	}

	public VbLoginTrack(VbOrganization vbOrganization, String username) {
		this.vbOrganization = vbOrganization;
		this.username = username;
	}

	public VbLoginTrack(VbOrganization vbOrganization, String username,
			String lastLoginTime) {
		this.vbOrganization = vbOrganization;
		this.username = username;
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VbOrganization getVbOrganization() {
		return this.vbOrganization;
	}

	public void setVbOrganization(VbOrganization vbOrganization) {
		this.vbOrganization = vbOrganization;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

}
