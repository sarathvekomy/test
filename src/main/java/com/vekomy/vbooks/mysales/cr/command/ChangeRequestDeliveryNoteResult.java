package com.vekomy.vbooks.mysales.cr.command;
/**
 * This Result command class is responsible for getting Delivery Note Result CR.
 * 
 * @author Ankit
 * 
 * 
 */
public class ChangeRequestDeliveryNoteResult extends
		ChangeRequestDeliveryNoteCommand {
	
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -2313998109672075492L;

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
