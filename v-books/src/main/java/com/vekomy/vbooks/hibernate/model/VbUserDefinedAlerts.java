package com.vekomy.vbooks.hibernate.model;
// Generated Nov 19, 2013 10:32:53 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * VbUserDefinedAlerts generated by hbm2java
 */
public class VbUserDefinedAlerts implements java.io.Serializable {

	private Integer id;
	private VbOrganization vbOrganization;
	private VbAlertType vbAlertType;
	private String alertName;
	private Boolean activeInactive;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private String description;
	private Set<VbMySales> vbMySaleses = new HashSet<VbMySales>(0);
	private Set<VbUserDefinedAlertsNotifications> vbUserDefinedAlertsNotificationses = new HashSet<VbUserDefinedAlertsNotifications>(
			0);
	private Set<VbTrending> vbTrendings = new HashSet<VbTrending>(0);
	private Set<VbExcessCash> vbExcessCashs = new HashSet<VbExcessCash>(0);
	private Set<VbUserDefinedAlertsHistory> vbUserDefinedAlertsHistories = new HashSet<VbUserDefinedAlertsHistory>(
			0);

	public VbUserDefinedAlerts() {
	}

	public VbUserDefinedAlerts(VbAlertType vbAlertType, String alertName,
			Date modifiedOn) {
		this.vbAlertType = vbAlertType;
		this.alertName = alertName;
		this.modifiedOn = modifiedOn;
	}

	public VbUserDefinedAlerts(
			VbOrganization vbOrganization,
			VbAlertType vbAlertType,
			String alertName,
			Boolean activeInactive,
			Date createdOn,
			String createdBy,
			Date modifiedOn,
			String modifiedBy,
			String description,
			Set<VbMySales> vbMySaleses,
			Set<VbUserDefinedAlertsNotifications> vbUserDefinedAlertsNotificationses,
			Set<VbTrending> vbTrendings, Set<VbExcessCash> vbExcessCashs,
			Set<VbUserDefinedAlertsHistory> vbUserDefinedAlertsHistories) {
		this.vbOrganization = vbOrganization;
		this.vbAlertType = vbAlertType;
		this.alertName = alertName;
		this.activeInactive = activeInactive;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.description = description;
		this.vbMySaleses = vbMySaleses;
		this.vbUserDefinedAlertsNotificationses = vbUserDefinedAlertsNotificationses;
		this.vbTrendings = vbTrendings;
		this.vbExcessCashs = vbExcessCashs;
		this.vbUserDefinedAlertsHistories = vbUserDefinedAlertsHistories;
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

	public VbAlertType getVbAlertType() {
		return this.vbAlertType;
	}

	public void setVbAlertType(VbAlertType vbAlertType) {
		this.vbAlertType = vbAlertType;
	}

	public String getAlertName() {
		return this.alertName;
	}

	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}

	public Boolean getActiveInactive() {
		return this.activeInactive;
	}

	public void setActiveInactive(Boolean activeInactive) {
		this.activeInactive = activeInactive;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedOn() {
		return this.modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<VbMySales> getVbMySaleses() {
		return this.vbMySaleses;
	}

	public void setVbMySaleses(Set<VbMySales> vbMySaleses) {
		this.vbMySaleses = vbMySaleses;
	}

	public Set<VbUserDefinedAlertsNotifications> getVbUserDefinedAlertsNotificationses() {
		return this.vbUserDefinedAlertsNotificationses;
	}

	public void setVbUserDefinedAlertsNotificationses(
			Set<VbUserDefinedAlertsNotifications> vbUserDefinedAlertsNotificationses) {
		this.vbUserDefinedAlertsNotificationses = vbUserDefinedAlertsNotificationses;
	}

	public Set<VbTrending> getVbTrendings() {
		return this.vbTrendings;
	}

	public void setVbTrendings(Set<VbTrending> vbTrendings) {
		this.vbTrendings = vbTrendings;
	}

	public Set<VbExcessCash> getVbExcessCashs() {
		return this.vbExcessCashs;
	}

	public void setVbExcessCashs(Set<VbExcessCash> vbExcessCashs) {
		this.vbExcessCashs = vbExcessCashs;
	}

	public Set<VbUserDefinedAlertsHistory> getVbUserDefinedAlertsHistories() {
		return this.vbUserDefinedAlertsHistories;
	}

	public void setVbUserDefinedAlertsHistories(
			Set<VbUserDefinedAlertsHistory> vbUserDefinedAlertsHistories) {
		this.vbUserDefinedAlertsHistories = vbUserDefinedAlertsHistories;
	}

}