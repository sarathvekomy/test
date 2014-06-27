package com.vekomy.vbooks.mysales.cr.command;
/**
 * @author Ankit
 * 
 * 
 */
public class ChangeRequestSalesReturnResult extends
		ChangeRequestSalesReturnCommand {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 8411374496450498131L;

	/**
	 * String  variable holds formatted sum of totalcost of salesreturn products .
	 */
	private String total;
    
	/**
	 * String  variable holds formatted created date of VbSalesReturn .
	 */
	private String date;

	/**
	 * @return the sum of totalcost
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * @param totalcost
	 *            the totalcost to set
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	/**
	 * @return the created Date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param created Date
	 *            the created Date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	public String toString(){
		return new StringBuffer("[ total:").append(getTotal()).
				append("date :").append(getDate()).append("]").toString();
	}


}
