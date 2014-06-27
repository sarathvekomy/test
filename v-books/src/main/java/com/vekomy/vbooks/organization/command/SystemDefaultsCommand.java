/**
 * com.vekomy.vbooks.customer.controller.CustomerController.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: june 11th, 2013
 */
package com.vekomy.vbooks.organization.command;

import java.io.Serializable;

/**
 * @author priyanka
 *
 */
public class SystemDefaultsCommand implements Serializable{
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -8213862477392121078L;
	/**
	 * String variable holds action
	 */
	private String action;
	/**
	 * String variable holds value
	 */
	private String value;
	/**
	 * String variable holds types
	 */
	private String types;
	/**
	 * String variable holds description
	 */
	private String description;
	/**
	 * String variable holds period
	 */
	private String period;
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the types
	 */
	public String getTypes() {
		return types;
	}
	/**
	 * @param types the types to set
	 */
	public void setTypes(String types) {
		this.types = types;
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
	 * @return the period
	 */
	public String getPeriod() {
		return period;
	}
	/**
	 * @param period the period to set
	 */
	public void setPeriod(String period) {
		this.period = period;
	}

}
