/**
 * com.vekomy.vbooks.employee.command.EmployeeResult.java
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
public class EmployeeResult extends EmployeeCommand implements Comparable<EmployeeResult>{
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -6246363788091137216L;
	/**
	 * String variable holds employeeTypeByString.
	 */
	private String employeeTypeByString;
	/**
	 * String variable holds passPortNumber
	 */
	private String passPortNumber;
	/**
	 * String variable holds isEnabled
	 */
	private String isEnabled;
	
	/**
	 * @return the isEnabled
	 */
	public String getIsEnabled() {
		return isEnabled;
	}
	/**
	 * @param isEnabled the isEnabled to set
	 */
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}
	/**
	 * String variable holds isLoggedInUser.
	 */
	public String isLoggedInUser;
	
	/**
	 * String variable holds directLine
	 */
	private String directLine;
	
	/**
	 * String variable holds showDisable.
	 */
	private String showDisable;
	/**
	 * @return the isLoggedInUser
	 */
	public String getIsLoggedInUser() {
		return isLoggedInUser;
	}
	/**
	 * @param isLoggedInUser the isLoggedInUser to set
	 */
	public void setIsLoggedInUser(String isLoggedInUser) {
		this.isLoggedInUser = isLoggedInUser;
	}
	/**
	 * String variable holds userName
	 */
	public String userName;
	private String loginEmployeeType;
	
	/**
	 * @return the loginEmployeeType
	 */
	public String getLoginEmployeeType() {
		return loginEmployeeType;
	}
	/**
	 * @param loginEmployeeType the loginEmployeeType to set
	 */
	public void setLoginEmployeeType(String loginEmployeeType) {
		this.loginEmployeeType = loginEmployeeType;
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
     * String variable holds residencePhone.
     */
    private String residencePhone;
    /**
     * String variable holds businessName
     */
    public String businessName;
	/**
	 * @return the employeeTypeByString
	 */
	public String getEmployeeTypeByString() {
		return employeeTypeByString;
	}
	/**
	 * @param employeeTypeByString the employeeTypeByString to set
	 */
	public void setEmployeeTypeByString(String employeeTypeByString) {
		this.employeeTypeByString = employeeTypeByString;
	}
	/**
	 * @return the residencePhone
	 */
	public String getResidencePhone() {
		return residencePhone;
	}
	/**
	 * @param residencePhone the residencePhone to set
	 */
	public void setResidencePhone(String residencePhone) {
		this.residencePhone = residencePhone;
	}
	
	/**
	 * @return the showDisable
	 */
	public String getShowDisable() {
		return showDisable;
	}
	/**
	 * @param showDisable the showDisable to set
	 */
	public void setShowDisable(String showDisable) {
		this.showDisable = showDisable;
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
	/* (non-Javadoc)
	 * @see com.vekomy.vbooks.employee.command.EmployeeCommand#toString()
	 */
	public String toString(){
		return new StringBuffer("[ employeeTypeByString :").
				append(getEmployeeTypeByString()).append("residencePhone :").
				append(getResidencePhone()).append("]").toString();
	}
	/*public boolean equals(Object o){
		if(o instanceof EmployeeResult){
			EmployeeResult result=(EmployeeResult) o;
			if(this.userName.equals(result.userName)){
				return true;
			}
		}
				return false;
		
		
	}
	public int hashCode(){
		return this.userName.hashCode();
	}*/
	/**
	 * @return the passPortNumber
	 */
	public String getPassPortNumber() {
		return passPortNumber;
	}
	/**
	 * @param passPortNumber the passPortNumber to set
	 */
	public void setPassPortNumber(String passPortNumber) {
		this.passPortNumber = passPortNumber;
	}
	/**
	 * @return the directLine
	 */
	public String getDirectLine() {
		return directLine;
	}
	/**
	 * @param directLine the directLine to set
	 */
	public void setDirectLine(String directLine) {
		this.directLine = directLine;
	}
	@Override
	public int compareTo(EmployeeResult o) {
		if(o.isEnabled.equalsIgnoreCase("1")){
			return 1;
		}else
		return -1;
	}
}
