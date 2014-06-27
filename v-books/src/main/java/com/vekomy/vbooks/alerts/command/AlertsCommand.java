/**
 * com.vekomy.vbooks.alerts.command.AlertsCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 15, 2013
 */
package com.vekomy.vbooks.alerts.command;

/**
 * @author swarupa
 *
 */
/**
 * @author priyanka
 *
 */
public class AlertsCommand {
	
	/**
	 * String variable holds action
	 */
	private String action;
	/**
	 * String variable holds alertName
	 */
	private String alertName;
	/**
	 * String variable holds alertType
	 */
	private String alertType;
	
	/**
	 * String variable holds description
	 */
	private String description;
	/**
	 * String variable holds alertMysales
	 */
	private String alertMySales;
    /**
     * String variable holds alertMySalesPage
     */
    private String alertMySalesPage;
    /**
     * String variable holds amountPercentage
     */
    private Long amountPercentage;
    /**
     * Long variable holds productPercentage
     */
    private Long productPercentage;
    /**
     * float variable holds amount
     */
    private float amount;
	
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the alertName
	 */
	public String getAlertName() {
		return alertName;
	}

	/**
	 * @param alertName the alertName to set
	 */
	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}

	/**
	 * @return the alertType
	 */
	public String getAlertType() {
		return alertType;
	}

	/**
	 * @param alertType the alertType to set
	 */
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the alertMySales
	 */
	public String getAlertMySales() {
		return alertMySales;
	}

	/**
	 * @param alertMySales the alertMySales to set
	 */
	public void setAlertMySales(String alertMySales) {
		this.alertMySales = alertMySales;
	}

	/**
	 * @return the alertMySalesPage
	 */
	public String getAlertMySalesPage() {
		return alertMySalesPage;
	}

	/**
	 * @param alertMySalesPage the alertMySalesPage to set
	 */
	public void setAlertMySalesPage(String alertMySalesPage) {
		this.alertMySalesPage = alertMySalesPage;
	}

	/**
	 * @return the amountPercentage
	 */
	public Long getAmountPercentage() {
		return amountPercentage;
	}

	/**
	 * @param amountPercentage the amountPercentage to set
	 */
	public void setAmountPercentage(Long amountPercentage) {
		this.amountPercentage = amountPercentage;
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
