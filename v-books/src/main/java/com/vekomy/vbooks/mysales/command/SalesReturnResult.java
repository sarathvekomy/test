/**
 * com.vekomy.vbooks.mysales.command.SalesReturnResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 19, 2013
 */
package com.vekomy.vbooks.mysales.command;

import java.util.List;

/**
 * @author Swarupa
 *
 */
public class SalesReturnResult extends SalesReturnCommand {

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -6800021811867254490L;

	/**
	 * String  variable holds formatted sum of totalcost of salesreturn products .
	 */
	private String total;
    
	/**
	 * String  variable holds formatted created date of VbSalesReturn .
	 */
	private String date;
	
	/**
	 * Strin variable holds invoiceName
	 */
	private String invoiceName;
	
	/**
	 * List<SalesReturnsResult> variable holds salesReturnResults
	 */
	private List<SalesReturnsResult> salesReturnResults;

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
	 * @return the invoiceName
	 */
	public String getInvoiceName() {
		return invoiceName;
	}

	/**
	 * @param invoiceName the invoiceName to set
	 */
	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	/**
	 * @return the salesReturnResults
	 */
	public List<SalesReturnsResult> getSalesReturnResults() {
		return salesReturnResults;
	}

	/**
	 * @param salesReturnResults the salesReturnResults to set
	 */
	public void setSalesReturnResults(List<SalesReturnsResult> salesReturnResults) {
		this.salesReturnResults = salesReturnResults;
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
