package com.vekomy.vbooks.hibernate.model;

// Generated Sep 11, 2013 11:07:56 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * VbRole generated by hbm2java
 */
public class VbRole implements java.io.Serializable {

	private Integer id;
	private String roleName;
	private String description;
	private Set vbSystemAlertsNotificationses = new HashSet(0);
	private Set vbAuthorities = new HashSet(0);
	private Set vbUserDefinedAlertsNotificationses = new HashSet(0);
	private Set vbRoleModuleMappings = new HashSet(0);

	public VbRole() {
	}

	public VbRole(String roleName) {
		this.roleName = roleName;
	}

	public VbRole(String roleName, String description,
			Set vbSystemAlertsNotificationses, Set vbAuthorities,
			Set vbUserDefinedAlertsNotificationses, Set vbRoleModuleMappings) {
		this.roleName = roleName;
		this.description = description;
		this.vbSystemAlertsNotificationses = vbSystemAlertsNotificationses;
		this.vbAuthorities = vbAuthorities;
		this.vbUserDefinedAlertsNotificationses = vbUserDefinedAlertsNotificationses;
		this.vbRoleModuleMappings = vbRoleModuleMappings;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set getVbSystemAlertsNotificationses() {
		return this.vbSystemAlertsNotificationses;
	}

	public void setVbSystemAlertsNotificationses(
			Set vbSystemAlertsNotificationses) {
		this.vbSystemAlertsNotificationses = vbSystemAlertsNotificationses;
	}

	public Set getVbAuthorities() {
		return this.vbAuthorities;
	}

	public void setVbAuthorities(Set vbAuthorities) {
		this.vbAuthorities = vbAuthorities;
	}

	public Set getVbUserDefinedAlertsNotificationses() {
		return this.vbUserDefinedAlertsNotificationses;
	}

	public void setVbUserDefinedAlertsNotificationses(
			Set vbUserDefinedAlertsNotificationses) {
		this.vbUserDefinedAlertsNotificationses = vbUserDefinedAlertsNotificationses;
	}

	public Set getVbRoleModuleMappings() {
		return this.vbRoleModuleMappings;
	}

	public void setVbRoleModuleMappings(Set vbRoleModuleMappings) {
		this.vbRoleModuleMappings = vbRoleModuleMappings;
	}

}
