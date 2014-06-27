/**
 * com.vekomy.vbooks.accounts.command.AllotStockResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: may 9, 2013
 */
package com.vekomy.vbooks.accounts.command;

/**
 * @author swarupa
 *
 */
public class AllotStockResult implements java.io.Serializable {
	
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 1733316001290331827L;

	/**
	 * Integer variable holds id
	 */
	private Integer id;

	/**
	 * String variable holds salesExecutive.
	 */
	private String salesExecutive;
	/**
	 * Float variable holds Advance.
	 */
	private Float advance;
	/**
	 * Float variable holds OpeningBalance.
	 */
	private Float openingBalance;
	/**
	 * String variable holds productName.
	 */
	private String productName;
	/**
	 * String variable holds batchNumber
	 */
	private String batchNumber;
	/**
	 * Integer variable holds Integer.
	 */
	private Integer salesId;
	/**
	 * Float variable holds previousClosingBalance.
	 */
	private Float previousClosingBalance;
	/**
	 * Float variable holds closingBalance
	 */
	private Float closingBalance;
	/**
	 * Integer variable holds availableQuantity.
	 */
	private Integer availableQuantity;
	/**
	 * Integer variable holds qtyAllotted.
	 */
	private Integer qtyAllotted;
	/**
	 * Integer variable holds previousQtyClosingStock.
	 */
	private Integer previousQtyClosingStock;
	/**
	 * Integer variable holds closingStock
	 */
	private Integer closingStock;

	/**
	 * Integer variable holds qtyOpeningBalance.
	 */
	private Integer qtyOpeningBalance;
	
	/**
	 * Integer variable holds returnQty.
	 */
	private Integer returnQty;
	
	/**
	 * String variable holds remarks.
	 */
	private String remarks;
	
	/**
	 * String variable holds allotmentType
	 */
	private String allotmentType;
	
	/**
	 * String variable holds flag
	 */
	private String flag; 
	
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * @return the availableQuantity
	 */
	public Integer getAvailableQuantity() {
		return availableQuantity;
	}

	/**
	 * @param availableQuantity
	 *            the availableQuantity to set
	 */
	public void setAvailableQuantity(Integer availableQuantity) {
		this.availableQuantity = availableQuantity;
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
	 * @return the previousQtyClosingStock
	 */
	public Integer getPreviousQtyClosingStock() {
		return previousQtyClosingStock;
	}

	/**
	 * @param previousQtyClosingStock the previousQtyClosingStock to set
	 */
	public void setPreviousQtyClosingStock(Integer previousQtyClosingStock) {
		this.previousQtyClosingStock = previousQtyClosingStock;
	}

	/**
	 * @return the returnQty
	 */
	public Integer getReturnQty() {
		return returnQty;
	}

	/**
	 * @param returnQty the returnQty to set
	 */
	public void setReturnQty(Integer returnQty) {
		this.returnQty = returnQty;
	}

	public String toString(){
		return new StringBuffer("[ salesExecutive :").
				append(getSalesExecutive()).append("openingBalance :").
				append(getOpeningBalance()).append("advance :").
				append(getAdvance()).append("quantityAlloted :").
				append("availabileQuantity :").append(getAvailableQuantity()).append("]").toString();
	}

	

	/**
	 * @return the previousClosingBalance
	 */
	public Float getPreviousClosingBalance() {
		return previousClosingBalance;
	}

	/**
	 * @param previousClosingBalance the previousClosingBalance to set
	 */
	public void setPreviousClosingBalance(Float previousClosingBalance) {
		this.previousClosingBalance = previousClosingBalance;
	}

	/**
	 * @return the salesId
	 */
	public Integer getSalesId() {
		return salesId;
	}

	/**
	 * @param salesId
	 *            the salesId to set
	 */
	public void setSalesId(Integer salesId) {
		this.salesId = salesId;
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

	/**
	 * @return the closingBalance
	 */
	public Float getClosingBalance() {
		return closingBalance;
	}

	/**
	 * @param closingBalance the closingBalance to set
	 */
	public void setClosingBalance(Float closingBalance) {
		this.closingBalance = closingBalance;
	}

	/**
	 * @return the closingStock
	 */
	public Integer getClosingStock() {
		return closingStock;
	}

	/**
	 * @param closingStock the closingStock to set
	 */
	public void setClosingStock(Integer closingStock) {
		this.closingStock = closingStock;
	}

	/**
	 * @return the allotmentType
	 */
	public String getAllotmentType() {
		return allotmentType;
	}

	/**
	 * @param allotmentType the allotmentType to set
	 */
	public void setAllotmentType(String allotmentType) {
		this.allotmentType = allotmentType;
	}

	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
	

}
