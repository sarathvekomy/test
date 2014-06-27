/**
 * com.vekomy.vbooks.mysales.command.DeliveryNoteResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 18, 2013
 */
package com.vekomy.vbooks.mysales.command;

/**
 * @author Swarupa
 *
 */
public class DeliveryNoteResult extends DeliveryNoteCommand {

	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 8803279099601610588L;

	/**
	 * String variable holds formatted created date of VbDeliveryNote .
	 */
	private String date;

	/**
	 * String variable holds formatted balance of VbDeliveryNotePayment .
	 */
	private String balance;
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

	/**
	 * @return balance
	 */
	public String getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	/* (non-Javadoc)
	 * @see com.vekomy.vbooks.mysales.command.DeliveryNoteCommand#toString()
	 */
	public String toString(){
		return new StringBuffer("[ date:").append(getDate())
				.append("balance :").append(getBalance()).append("]").toString();
	}

}
