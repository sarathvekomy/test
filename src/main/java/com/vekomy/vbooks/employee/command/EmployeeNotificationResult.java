/**
 * com.vekomy.vbooks.employee.command.EmployeeNotificationResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Sep 4, 2013
 */
package com.vekomy.vbooks.employee.command;

/**
 * @author Sudhakar
 *
 */
public class EmployeeNotificationResult implements java.io.Serializable {
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 6351168056499320049L;
	/**
	 * String variable holds userName.
	 */
	private String userName;
	/**
	 * String variable holds employeeType.
	 */
	private String employeeType;
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
	 * @return the employeeType
	 */
	public String getEmployeeType() {
		return employeeType;
	}
	/**
	 * @param employeeType the employeeType to set
	 */
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}
}
