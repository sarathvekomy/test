package com.vekomy.vbooks.employee.command;

/**
 * @author Sudhakar
 *
 * 
 */
public class EmployeeResult extends EmployeeCommand {
    /**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -1943350569953766089L;
	/**
	 * String variable holds employeeTypeByString.
	 */
	private String employeeTypeByString;
	
	public String userName;
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
	

}
