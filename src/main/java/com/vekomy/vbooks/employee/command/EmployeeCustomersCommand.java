/**
 * com.vekomy.vbooks.employee.command.EmployeeCustomersCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: may 9, 2013
 */

package com.vekomy.vbooks.employee.command;

import com.vekomy.vbooks.hibernate.model.VbEmployeeCustomer;

/**
 * @author Sudhakar
 * 
 * 
 */
public class EmployeeCustomersCommand extends VbEmployeeCustomer {
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 6693641110754006970L;
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
	
	String salesExecutive;
	
	/**
	 * @return the salesExecutive
	 */
	public String getSalesExecutive() {
		return salesExecutive;
	}
	/**
	 * @param salesExecutive the salesExecutive to set
	 */
	public void setSalesExecutive(String salesExecutive) {
		this.salesExecutive = salesExecutive;
	}
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
