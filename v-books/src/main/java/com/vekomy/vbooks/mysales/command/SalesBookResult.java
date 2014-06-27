/**
 * com.vekomy.vbooks.mysales.command.SalesBookResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 4, 2013
 */
package com.vekomy.vbooks.mysales.command;

/**
 * @author rajesh
 * 
 */
public class SalesBookResult extends SalesBookCommand {
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 2273840220104953207L;

	/**
	 * String variable holds salesNumber.
	 */
	private String salesNumber;

	/**
	 * String variable holds salesExecutive.
	 */
	private String salesExecutive;

	/**
	 * @return the salesNumber
	 */
	public String getSalesNumber() {
		return salesNumber;
	}

	/**
	 * @param salesNumber
	 *            the salesNumber to set
	 */
	public void setSalesNumber(String salesNumber) {
		this.salesNumber = salesNumber;
	}

	/**
	 * @return the salesExecutive
	 */
	public String getSalesExecutive() {
		return salesExecutive;
	}

	/**
	 * @param salesExecutive
	 *            the salesExecutive to set
	 */
	public void setSalesExecutive(String salesExecutive) {
		this.salesExecutive = salesExecutive;
	}

	public String toString() {
		return new StringBuffer("[salesNumber :").append(getSalesNumber())
				.append("salesExecutive :").append(getSalesExecutive())
				.append("]").toString();
	}

}
