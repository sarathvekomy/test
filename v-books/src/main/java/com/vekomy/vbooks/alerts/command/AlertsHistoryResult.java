/**
 * com.vekomy.vbooks.alerts.command.AlertsHistoryResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 25, 2013
 */
package com.vekomy.vbooks.alerts.command;

/**
 * @author swarupa
 *
 */
public class AlertsHistoryResult implements java.io.Serializable {
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -4870790098475594049L;
	/**
	 * Integer variable holds id
	 */
	private Integer id;
	/**
	 * String variable holds createdUser
	 */
	private String createdUser;
	/**
	 * String variable holds alertName
	 */
	private String alertName;
	/**
	 * String variable holds description
	 */
	private String description;
	/**
	 * String variable holds createdOn
	 */
	private String createdOn;
	
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
	 * @return the createdUser
	 */
	public String getCreatedUser() {
		return createdUser;
	}
	/**
	 * @param createdUser the createdUser to set
	 */
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	/**
	 * @return the alertName
	 */
	public String getAlertName() {
		return alertName;
	}
	/**
	 * @param alertName the alertName to set
	 */
	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the createdOn
	 */
	public String getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	

}
