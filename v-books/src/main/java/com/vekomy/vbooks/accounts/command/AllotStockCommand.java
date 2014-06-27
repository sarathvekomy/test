/**
 * 
 */
package com.vekomy.vbooks.accounts.command;

import com.vekomy.vbooks.hibernate.model.VbSalesBook;

/**
 * @author rajesh
 * 
 */
public class AllotStockCommand extends VbSalesBook {

	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -3656487270697554867L;
	/**
	 * String variable holds action.
	 */
	private String action;
	/**
	 * String variable holds productName.
	 */
	private String productName;
	/**
	 * String variable holds batchNumber
	 */
	private String batchNumber;
	/**
	 * Integer variable holds qtyAllotted.
	 */
	private Integer qtyAllotted;
	/**
	 * Integer variable holds qtyOpeningBalance.
	 */
	private Integer qtyOpeningBalance;
	/**
	 * Integer variable holds qtyClosingBalance.
	 */
	private Integer qtyClosingBalance;
	/**
	 * String variable holds productBatch.
	 */
	private String productBatch;
	/**
	 * Integer variable holds qtySold.
	 */
	private Integer qtySold;
	/**
	 * Integer variable holds qtyToFactory
	 */
	private Integer qtyToFactory;
	/**
	 * Integer variable holds availableQuantity.
	 */
	private Integer availableQuantity;
	/**
	 * String variable holds remarks
	 */
	private String remarks;

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
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName
	 *            the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	
	/**
	 * @return the batchNumber
	 */
	public String getBatchNumber() {
		return batchNumber;
	}

	/**
	 * @param batchNumber the batchNumber to set
	 */
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	/**
	 * @return the qtyAllotted
	 */
	public Integer getQtyAllotted() {
		return qtyAllotted;
	}

	/**
	 * @param qtyAllotted
	 *            the qtyAllotted to set
	 */
	public void setQtyAllotted(Integer qtyAllotted) {
		this.qtyAllotted = qtyAllotted;
	}

	/**
	 * @return the qtyOpeningBalance
	 */
	public Integer getQtyOpeningBalance() {
		return qtyOpeningBalance;
	}

	/**
	 * @param qtyOpeningBalance
	 *            the qtyOpeningBalance to set
	 */
	public void setQtyOpeningBalance(Integer qtyOpeningBalance) {
		this.qtyOpeningBalance = qtyOpeningBalance;
	}

	/**
	 * @return the qtyClosingBalance
	 */
	public Integer getQtyClosingBalance() {
		return qtyClosingBalance;
	}

	/**
	 * @param qtyClosingBalance
	 *            the qtyClosingBalance to set
	 */
	public void setQtyClosingBalance(Integer qtyClosingBalance) {
		this.qtyClosingBalance = qtyClosingBalance;
	}

	/**
	 * @return the productBatch
	 */
	public String getProductBatch() {
		return productBatch;
	}

	/**
	 * @param productBatch
	 *            the productBatch to set
	 */
	public void setProductBatch(String productBatch) {
		this.productBatch = productBatch;
	}

	/**
	 * @return the qtySold
	 */
	public Integer getQtySold() {
		return qtySold;
	}

	/**
	 * @param qtySold
	 *            the qtySold to set
	 */
	public void setQtySold(Integer qtySold) {
		this.qtySold = qtySold;
	}

	/**
	 * @return the qtyToFactory
	 */
	public Integer getQtyToFactory() {
		return qtyToFactory;
	}

	/**
	 * @param qtyToFactory
	 *            the qtyToFactory to set
	 */
	public void setQtyToFactory(Integer qtyToFactory) {
		this.qtyToFactory = qtyToFactory;
	}
	

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String toString() {
		return new StringBuffer("[action :").append(getAction())
				.append(",productName :").append(getProductName())
				.append(",qtyAllotted :").append(getQtyAllotted())
				.append(",qtyOpeningBalance :").append(getQtyOpeningBalance())
				.append(",qtyClosingBalance :").append(getQtyClosingBalance())
				.append(",productBatch :").append(getProductBatch())
				.append(",qtySlod :").append(getQtySold())
				.append(",qtyToFactory ,").append(getQtyToFactory())
				.append("]").toString();
	}

	/**
	 * @return the availableQuantity
	 */
	public Integer getAvailableQuantity() {
		return availableQuantity;
	}

	/**
	 * @param availableQuantity the availableQuantity to set
	 */
	public void setAvailableQuantity(Integer availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

}
