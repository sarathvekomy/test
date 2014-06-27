package com.vekomy.vbooks.employee.command;

import com.vekomy.vbooks.hibernate.model.VbEmployeeCustomer;

public class EmployeeCustomersCommand extends VbEmployeeCustomer {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -1374489498446316997L;
	/**
	 * String variable holds action
	 */
	String action;
	/**
	 * String variable holds userName.
	 */
	String userName;
	/**
	 * String variable holds locality.
	 */
	String locality;
	/**
	 * String variable holds businessName. 
	 */
	String businessName;
	
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the locality
	 */
	public String getLocality() {
		return locality;
	}
	/**
	 * @param locality the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}
	/**
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}
	/**
	 * @param businessName the businessName to set
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	
	
}
