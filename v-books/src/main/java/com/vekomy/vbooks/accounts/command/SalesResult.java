package com.vekomy.vbooks.accounts.command;

public class SalesResult extends SalesCommand {
	/**
	 * long variable holdFs serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * String  variable holds formatted created Date of VbSalesBook .
	 */
	private String date;
	
	/**
	 * String  variable holds formatted opening balance of VbSalesBook .
	 */
	private String balanceOpening;
	
	/**
	 * String  variable holds formatted closing balance of VbSalesBook .
	 */
	private String balanceClosing;
	
	/**
	 * @return formatted created date
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

	/**
	 * @return formatted opening balance
	 */
	public String getBalanceOpening() {
		return balanceOpening;
	}

	/*
	 * @param opening Balance
	 *            the opening Balance to set
	 */
	public void setBalanceOpening(String balanceOpening) {
		this.balanceOpening = balanceOpening;
	}

	/**
	 * @return formatted closing Balance
	 */
	public String getBalanceClosing() {
		return balanceClosing;
	}

	/**
	 * @param closing balance
	 *            the closing balance to set
	 */
	public void setBalanceClosing(String balanceClosing) {
		this.balanceClosing = balanceClosing;
	}
	public String toString(){
		return new StringBuffer("[ date:").append(getDate())
				.append("balanceOpening :").append(getBalanceOpening())
				.append("balanceClosing :").append(getBalanceClosing()).append("]").toString();
	}

	
}
