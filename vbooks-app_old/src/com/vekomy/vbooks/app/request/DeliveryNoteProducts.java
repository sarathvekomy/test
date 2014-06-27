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
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.vekomy.vbooks.app.response.DeliveryNoteProductResponse;

/**
 * @author nkr
 *
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryNoteProducts {

	private String 	productName;
	private String 	batchNumber;
	private Integer productQty;
	private Integer bonusQty;
	private String 	bonusReason;
	private Float 	productCost;
	private Float	totalCost;
	@JsonIgnore
	private Integer availableQty;
	
	public DeliveryNoteProducts(DeliveryNoteProductResponse product){
		productName  	=	product.getProductName();
		batchNumber	 	=	product.getBatchNumber();
		productCost  	=	product.getProductCost()==null?0:product.getProductCost();
		availableQty 	=  	product.getAvailableQty()==null?0:product.getAvailableQty();
		productQty 		=	0;
		bonusQty 		= 	0;
		totalCost 		= 	0f;		
	}
	public DeliveryNoteProducts(){		
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
	 * @return the productQty
	 */
	public Integer getProductQty() {
		return productQty;
	}
	/**
	 * @param productQty the productQty to set
	 */
	public void setProductQty(Integer productQty) {
		this.productQty = productQty;
	}
	/**
	 * @return the bonusQty
	 */
	public Integer getBonusQty() {
		return bonusQty;
	}
	/**
	 * @param bonusQty the bonusQty to set
	 */
	public void setBonusQty(Integer bonusQty) {
		this.bonusQty = bonusQty;
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
	 * @return the totalCost
	 */
	public Float getTotalCost() {
		return totalCost;
	}
	/**
	 * @param totalCost the totalCost to set
	 */
	public void setTotalCost(Float totalCost) {
		this.totalCost = totalCost;
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
}
