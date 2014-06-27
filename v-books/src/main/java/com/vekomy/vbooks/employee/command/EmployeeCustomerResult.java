/**
 * com.vekomy.vbooks.employee.command.EmployeeCustomerResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: may 9, 2013
 */

package com.vekomy.vbooks.employee.command;

/**
 * @author Sudhakar
 * 
 * 
 */
public class EmployeeCustomerResult extends EmployeeCommand implements	Comparable<EmployeeCustomerResult> {
	

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 2391370551694024450L;

	/**
	 * String variable holds userName
	 */
	public String userName;

	/**
	 * char variable holds isEnabled
	 */
	public char isEnabled;

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the isEnabled
	 */
	public char getIsEnabled() {
		return isEnabled;
	}

	/**
	 * @param isEnabled
	 *            the isEnabled to set
	 */
	public void setIsEnabled(char isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean equals(Object o) {
		if (o instanceof EmployeeCustomerResult) {
			EmployeeCustomerResult result = (EmployeeCustomerResult) o;
			if (this.userName.equals(result.userName)) {
				return true;
			}
		}
		return false;

	}

	public int hashCode() {
		return this.userName.hashCode();
	}

	@Override
	public int compareTo(EmployeeCustomerResult o) {
		if (o.isEnabled == '1') {
			return 1;
		} else
			return -1;
	}

}
