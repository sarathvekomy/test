package com.vekomy.vbooks.accounts.command;

public class SalesReturnResult extends SalesReturnCommand {

	/**
	 * long variable holdFs serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

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
