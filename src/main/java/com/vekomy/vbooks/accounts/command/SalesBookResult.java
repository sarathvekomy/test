/**
 * com.vekomy.vbooks.accounts.command.SalesBookResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: may 9, 2013
 */
package com.vekomy.vbooks.accounts.command;

import java.util.Date;

import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;

/**
 * @author Swarupa
 *
 */
public class SalesBookResult extends VbSalesBookProducts implements	Comparable<SalesBookResult> {
	
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -3259917580952439385L;
	/**
	 * Float variable holds previousClosingBalance
	 * 
	 */
	public String product;

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product
	 *            the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	private Float previousClosingBalance;
	/**
	 * Integer variable holds previousClosingStock
	 */
	private Integer previousClosingStock;
	/**
	 * Float variable holds openingBalance
	 */
	private Float openingBalance;

	/**
	 * @return the previousClosingBalance
	 */
	public Float getPreviousClosingBalance() {
		return previousClosingBalance;
	}

	/**
	 * @param previousClosingBalance
	 *            the previousClosingBalance to set
	 */
	public void setPreviousClosingBalance(Float previousClosingBalance) {
		this.previousClosingBalance = previousClosingBalance;
	}

	/**
	 * @return the previousClosingStock
	 */
	public Integer getPreviousClosingStock() {
		return previousClosingStock;
	}

	/**
	 * @param previousClosingStock
	 *            the previousClosingStock to set
	 */
	public void setPreviousClosingStock(Integer previousClosingStock) {
		this.previousClosingStock = previousClosingStock;
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
	public void setOpeningBalance(Float openingBalance) {
		this.openingBalance = openingBalance;
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
	public void setAdvance(Float advance) {
		this.advance = advance;
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

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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
	public void setClosingBalance(Float closingBalance) {
		this.closingBalance = closingBalance;
	}

	/**
	 * @return the returnQty
	 */
	public Integer getReturnQty() {
		return returnQty;
	}

	/**
	 * @param returnQty
	 *            the returnQty to set
	 */
	public void setReturnQty(Integer returnQty) {
		this.returnQty = returnQty;
	}

	/**
	 * Float variable holds advance
	 */
	private Float advance;
	/**
	 * String variable holds salesExecutive
	 */
	private String salesExecutive;
	/**
	 * Date variable holds createdDate
	 */
	private Date createdDate;
	/**
	 * Float variable holds closingBalance
	 */
	private Float closingBalance;
	/**
	 * Integer variable holds returnQty
	 */
	private Integer returnQty;

	@Override
	public int compareTo(SalesBookResult result) {
		// TODO Auto-generated method stub
		int comparision = this.product.compareTo(result.getProduct());
		return comparision;
	}

}
