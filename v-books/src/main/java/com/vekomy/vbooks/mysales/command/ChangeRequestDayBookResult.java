package com.vekomy.vbooks.mysales.command;

import java.io.Serializable;
import java.util.Date;
/**
 * This Result class is responsible to store the results of Day Book CR.
 * 
 * @author Ankit.
 * 
 */
public class ChangeRequestDayBookResult implements Serializable {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 5868488721910347014L;
	
	private Integer id;
	
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
	 * @return the salesExecutive
	 */
	public String getSalesExecutive() {
		return salesExecutive;
	}

	/**
	 * @param salesExecutive the salesExecutive to set
	 */
	public void setSalesExecutive(String salesExecutive) {
		this.salesExecutive = salesExecutive;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	private String salesExecutive;
	
	private Date createdOn;
	/**
	 * Float variable holds presentPayable
	 */
	private String presentPayable;
	
	/**
	 * Float variable holds presentPayment
	 */
	private String presentPayment;
	/**
	 * String variable holds productName
	 */
	private String productName;
	/**
	 * String variable holds batchNumber
	 */
	private String batchNumber;
	/**
	 * String variable holds openingStock
	 */
	private String openingStock;
	
	/**
	 * String variable holds productsToCustomer
	 */
	private String productsToCustomer;
	
	/**
	 * String variable holds closingStock
	 */
	private String closingStock;
	
	/**
	 * String variable holds returnQty.
	 */
	private String returnQty;
	/**
	 * String variable holds productsToFactory.
	 */
	private String productsToFactory;
	
	public String getProductsToFactory() {
		return productsToFactory;
	}

	public void setProductsToFactory(String productsToFactory) {
		this.productsToFactory = productsToFactory;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
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
	 * @return the openingStock
	 */
	public String getOpeningStock() {
		return openingStock;
	}

	/**
	 * @param openingStock the openingStock to set
	 */
	public void setOpeningStock(String openingStock) {
		this.openingStock = openingStock;
	}

	/**
	 * @return the productsToCustomer
	 */
	public String getProductsToCustomer() {
		return productsToCustomer;
	}

	/**
	 * @param productsToCustomer the productsToCustomer to set
	 */
	public void setProductsToCustomer(String productsToCustomer) {
		this.productsToCustomer = productsToCustomer;
	}

	/**
	 * @return the presentPayable
	 */
	public String getPresentPayable() {
		return presentPayable;
	}

	/**
	 * @param presentPayable the presentPayable to set
	 */
	public void setPresentPayable(String presentPayable) {
		this.presentPayable = presentPayable;
	}

	/**
	 * @return the presentPayment
	 */
	public String getPresentPayment() {
		return presentPayment;
	}

	/**
	 * @param presentPayment the presentPayment to set
	 */
	public void setPresentPayment(String presentPayment) {
		this.presentPayment = presentPayment;
	}

	/**
	 * @return the closingStock
	 */
	public String getClosingStock() {
		return closingStock;
	}

	/**
	 * @param closingStock the closingStock to set
	 */
	public void setClosingStock(String closingStock) {
		this.closingStock = closingStock;
	}
	
	
	/**
	 * @return the returnQty
	 */
	public String getReturnQty() {
		return returnQty;
	}

	/**
	 * @param returnQty the returnQty to set
	 */
	public void setReturnQty(String returnQty) {
		this.returnQty = returnQty;
	}

	public String toString(){
		return new StringBuffer("[ presentPayable :").append(getPresentPayable()).
				append("presentPayment :").append(getPresentPayment()).append("]").toString();
	}
}
