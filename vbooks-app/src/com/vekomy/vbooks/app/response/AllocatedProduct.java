/**
 * com.vekomy.vbooks.app.response.DeliveryNoteProductResponse.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 19, 2013
 */
package com.vekomy.vbooks.app.response;

/**
 * @author Sudhakar
 *
 */
public class AllocatedProduct extends Response {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -1887168466454655861L;
	
	private String uid;
	
	/**
	 * String variable holds productName.
	 */
	private String productName;
	/**
	 * String variable holds batchNumber.
	 */
	private String batchNumber;
	/**
	 * Float variable holds productCost.
	 */
	private Float productCost;
	/**
	 * Integer variable holds availableQty.
	 */
	private Integer availableQty;
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
	 * @return the productCost
	 */
	public Float getProductCost() {
		return productCost;
	}
	/**
	 * @param productCost the productCost to set
	 */
	public void setProductCost(Float productCost) {
		this.productCost = productCost;
	}
	/**
	 * @return the availableQty
	 */
	public Integer getAvailableQty() {
		return availableQty;
	}
	/**
	 * @param availableQty the availableQty to set
	 */
	public void setAvailableQty(Integer availableQty) {
		this.availableQty = availableQty;
	}
	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}	
}
