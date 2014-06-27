/**
 * com.vekomy.vbooks.mysales.command.SalesBookCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 4, 2013
 */
package com.vekomy.vbooks.mysales.command;

import com.vekomy.vbooks.hibernate.model.VbSalesBook;
/**
 * @author priyanka
 *
 */
public class SalesBookCommand extends VbSalesBook {
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -8226792356921200843L;
	/**
	 * String variable holds action
	 */
	private String action;
	/**
	 * float variable holds advance
	 */
	private float advance;
	/**
	 * float variable holds openingBalance
	 */
	private float openingBalance;
	/**
	 * float variable holds customerTransactions
	 */
	private float customerTransactions;
	/**
	 * float variable holds selfAllowance
	 */
	private float selfAllowance;
	/**
	 * float variable holds amountToBank
	 */
	private float amountToBank;
	/**
	 * float variable holds amountToFactory
	 */
	private float amountToFactory;
	/**
	 * float variable holds closingBalance
	 */
	private float closingBalance;

	public SalesBookCommand() {

	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the advance
	 */
	public Float getAdvance() {
		return advance;
	}

	/**
	 * @param advance
	 *            the advance to set
	 */
	public void setAdvance(float advance) {
		this.advance = advance;
	}

	/**
	 * @return the openingBalance
	 */
	public Float getOpeningBalance() {
		return openingBalance;
	}

	/**
	 * @param openingBalance
	 *            the openingBalance to set
	 */
	public void setOpeningBalance(float openingBalance) {
		this.openingBalance = openingBalance;
	}

	/**
	 * @return the customerTransactions
	 */
	public float getCustomerTransactions() {
		return customerTransactions;
	}

	/**
	 * @param customerTransactions
	 *            the customerTransactions to set
	 */
	public void setCustomerTransactions(float customerTransactions) {
		this.customerTransactions = customerTransactions;
	}

	/**
	 * @return the selfAllowance
	 */
	public float getSelfAllowance() {
		return selfAllowance;
	}

	/**
	 * @param selfAllowance
	 *            the selfAllowance to set
	 */
	public void setSelfAllowance(float selfAllowance) {
		this.selfAllowance = selfAllowance;
	}

	/**
	 * @return the amountToBank
	 */
	public float getAmountToBank() {
		return amountToBank;
	}

	/**
	 * @param amountToBank
	 *            the amountToBank to set
	 */
	public void setAmountToBank(float amountToBank) {
		this.amountToBank = amountToBank;
	}

	/**
	 * @return the amountToFactory
	 */
	public float getAmountToFactory() {
		return amountToFactory;
	}

	/**
	 * @param amountToFactory
	 *            the amountToFactory to set
	 */
	public void setAmountToFactory(float amountToFactory) {
		this.amountToFactory = amountToFactory;
	}

	/**
	 * @return the closingBalance
	 */
	public Float getClosingBalance() {
		return closingBalance;
	}

	/**
	 * @param closingBalance
	 *            the closingBalance to set
	 */
	public void setClosingBalance(float closingBalance) {
		this.closingBalance = closingBalance;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer("action :").append(getAction())
				.append(", advance :").append(getAdvance())
				.append(",openingBalance :").append(getOpeningBalance())
				.append(",customerTransactios :").append(getCustomerTransactions())
				.append(",selfAllowance :")	.append(getSelfAllowance())
				.append(", amountToBank :").append(getAmountToBank())
				.append(", amountToFactory :").append(getAmountToFactory())
				.append(",closingBalance :").append(getClosingBalance()).append("]").toString();
	}
}
