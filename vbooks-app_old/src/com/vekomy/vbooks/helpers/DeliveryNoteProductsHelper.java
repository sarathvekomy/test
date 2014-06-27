/**
 * com.vekomy.vbooks.helpers.DeliveryNoteProductsHelper.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 26-Jul-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.helpers;

/**
 * @author nkr
 *
 */
public class DeliveryNoteProductsHelper {
	
	private Integer enteredQty;
	private Integer bonusEnteredQty;
	private Float	totalCost;
	private String	bonusReason;
	/**
	 * @return the enteredQty
	 */
	public Integer getEnteredQty() {
		return enteredQty;
	}
	/**
	 * @param enteredQty the enteredQty to set
	 */
	public void setEnteredQty(Integer enteredQty) {
		this.enteredQty = enteredQty;
	}
	/**
	 * @return the bonusEnteredQty
	 */
	public Integer getBonusEnteredQty() {
		return bonusEnteredQty;
	}
	/**
	 * @param bonusEnteredQty the bonusEnteredQty to set
	 */
	public void setBonusEnteredQty(Integer bonusEnteredQty) {
		this.bonusEnteredQty = bonusEnteredQty;
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
}
