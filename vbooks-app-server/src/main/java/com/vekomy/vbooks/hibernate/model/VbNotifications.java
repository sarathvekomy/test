package com.vekomy.vbooks.hibernate.model;

// Generated Sep 11, 2013 11:07:56 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * VbNotifications generated by hbm2java
 */
public class VbNotifications implements java.io.Serializable {

	private Integer id;
	private String notificationType;
	private Set vbSystemAlertsNotificationses = new HashSet(0);
	private Set vbUserDefinedAlertsNotificationses = new HashSet(0);

	public VbNotifications() {
	}

	public VbNotifications(String notificationType) {
		this.notificationType = notificationType;
	}

	public VbNotifications(String notificationType,
			Set vbSystemAlertsNotificationses,
			Set vbUserDefinedAlertsNotificationses) {
		this.notificationType = notificationType;
		this.vbSystemAlertsNotificationses = vbSystemAlertsNotificationses;
		this.vbUserDefinedAlertsNotificationses = vbUserDefinedAlertsNotificationses;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNotificationType() {
		return this.notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public Set getVbSystemAlertsNotificationses() {
		return this.vbSystemAlertsNotificationses;
	}

	public void setVbSystemAlertsNotificationses(
			Set vbSystemAlertsNotificationses) {
		this.vbSystemAlertsNotificationses = vbSystemAlertsNotificationses;
	}

	public Set getVbUserDefinedAlertsNotificationses() {
		return this.vbUserDefinedAlertsNotificationses;
	}

	public void setVbUserDefinedAlertsNotificationses(
			Set vbUserDefinedAlertsNotificationses) {
		this.vbUserDefinedAlertsNotificationses = vbUserDefinedAlertsNotificationses;
	}

}
