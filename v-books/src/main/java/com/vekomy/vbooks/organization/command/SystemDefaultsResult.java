package com.vekomy.vbooks.organization.command;

import java.io.Serializable;

/**
 * @author priyanka
 * 
 */
public class SystemDefaultsResult implements Serializable {

	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -5473094839813546597L;
	/**
	 * String variable holds value
	 */
	private String value;
	/**
	 * String variable holds period
	 */
	private String period;
	/**
	 * int variable holds id
	 */
	private int id;
	/**
	 * String variable holds description
	 */
	private String description;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the period
	 */
	public String getPeriod() {
		return period;
	}

	/**
	 * @param period
	 *            the period to set
	 */
	public void setPeriod(String period) {
		this.period = period;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
