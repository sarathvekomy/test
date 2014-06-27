package com.vekomy.vbooks.employee.command;

public class EmployeeCustomerResult extends EmployeeCommand {
	 /**
		 * long variable holds serialVersionUID.
		 */
		private static final long serialVersionUID = -1943350569953766089L;
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
		public boolean equals(Object o){
			if(o instanceof EmployeeCustomerResult){
				EmployeeCustomerResult result=(EmployeeCustomerResult) o;
				if(this.userName.equals(result.userName)){
					return true;
				}
			}
					return false;
			
			
		}
		public int hashCode(){
			return this.userName.hashCode();
		}
		

}
