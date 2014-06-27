/**
 * com.vekomy.vbooks.app.request.DeliveryNoteProductsInfo.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 25-Jul-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.app.request;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;


/**
 * @author nkr
 *
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesReturnProduct {
	private String 	uid;
	private String 	productName;
	private String 	batchNumber;
	private Integer resaleQty;
	private Integer damagedQty;
	private String 	bonusReason;
	private Integer totalQty;
	
	
	public SalesReturnProduct(){
		resaleQty=0;
		damagedQty=0;
		totalQty=0;
		bonusReason="";
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
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
	 * @return the resaleQty
	 */
	public Integer getResaleQty() {
		return resaleQty;
	}
	/**
	 * @param resaleQty the resaleQty to set
	 */
	public void setResaleQty(Integer resaleQty) {
		this.resaleQty = resaleQty;
	}
	/**
	 * @return the damagedQty
	 */
	public Integer getDamagedQty() {
		return damagedQty;
	}
	/**
	 * @param damagedQty the damagedQty to set
	 */
	public void setDamagedQty(Integer damagedQty) {
		this.damagedQty = damagedQty;
	}
	/**
	 * @return the bonusReason
	 */
	public String getBonusReason() {
		return bonusReason;
	}
	/**
	 * @param bonusReason the bonusReason to set
	 */
	public void setBonusReason(String bonusReason) {
		this.bonusReason = bonusReason;
	}
	/**
	 * @return the totalQty
	 */
	public Integer getTotalQty() {
		return totalQty;
	}
	/**
	 * @param totalQty the totalQty to set
	 */
	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}	
}
