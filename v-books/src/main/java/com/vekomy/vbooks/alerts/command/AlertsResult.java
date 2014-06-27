/**
 * com.vekomy.vbooks.alerts.command.AlertsResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 18, 2013
 */
package com.vekomy.vbooks.alerts.command;

/**
 * @author Sudhakar
 *
 * 
 */
public class AlertsResult {
	/**
	 * String variable holds emailId.
	 */
	private String emailId;
	
	/**
	 * String variable holds mailRecipientType.
	 */
	private String mailRecipientType;
	/**
	 * Long variable holds amountPersentage.
	 */
	private Long amountPersentage;
	/**
	 * Long variable holds productPercentage.
	 */
	private Long productPercentage;
	/**
	 * float variable holds amount.
	 */
	private float amount;
	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}
	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	/**
	 * @return the mailRecipientType
	 */
	public String getMailRecipientType() {
		return mailRecipientType;
	}
	/**
	 * @param mailRecipientType the mailRecipientType to set
	 */
	public void setMailRecipientType(String mailRecipientType) {
		this.mailRecipientType = mailRecipientType;
	}
	/**
	 * @return the amountPersentage
	 */
	public Long getAmountPersentage() {
		return amountPersentage;
	}
	/**
	 * @param amountPersentage the amountPersentage to set
	 */
	public void setAmountPersentage(Long amountPersentage) {
		this.amountPersentage = amountPersentage;
	}
	/**
	 * @return the productPercentage
	 */
	public Long getProductPercentage() {
		return productPercentage;
	}
	/**
	 * @param productPercentage the productPercentage to set
	 */
	public void setProductPercentage(Long productPercentage) {
		this.productPercentage = productPercentage;
	}
	/**
	 * @return the amount
	 */
	public float getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
}
