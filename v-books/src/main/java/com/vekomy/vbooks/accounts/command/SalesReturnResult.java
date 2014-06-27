/**
 * com.vekomy.vbooks.accounts.command.AllotStockCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: may 9, 2013
 */
package com.vekomy.vbooks.accounts.command;

/**
 * @author Sudhakar
 *
 */
public class SalesReturnResult extends SalesReturnCommand {


	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -7605691015563822686L;

	/**
	 * String  variable holds formatted sum of totalcost of salesreturn products .
	 */
	private String totalCost;
    
	/**
	 * String  variable holds formatted created date of VbSalesReturn .
	 */
	private String date;

	/**
	 * @return the sum of totalcost
	 */
	public String getTotalCost() {
		return totalCost;
	}

	/**
	 * @param totalcost
	 *            the totalcost to set
	 */
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	/**
	 * @return created Date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	public String toString(){
		return new StringBuffer("[totalCost :").append(getTotalCost()).
				append("date :").append(getDate()).
				append("]").toString();
	}

}
