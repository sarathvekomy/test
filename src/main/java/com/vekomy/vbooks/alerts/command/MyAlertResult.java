/**
 * com.vekomy.vbooks.alerts.command.MyAlertResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Sep 2, 2013
 */
package com.vekomy.vbooks.alerts.command;

/**
 * @author Sudhakar
 *
 */
public class MyAlertResult implements java.io.Serializable {
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -923155250083405411L;
	/**
	 * String variable holds alertName.
	 */
	private String alertName;
	/**
	 * String variable holds createdOn.
	 */
	private String createdOn;
	/**
	 * String variable holds createdBy.
	 */
	private String createdBy;
	/**
	 * String variable holds description.
	 */
	private String description;
	
	/**
	 * @return the alertName
	 */
	public String getAlertName() {
		return alertName;
	}
	/**
	 * @param alertType the alertName to set
	 */
	public void setAlertName(String alertType) {
		this.alertName = alertType;
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
	
	
}
