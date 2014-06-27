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
 */
public class AlertsResult implements java.io.Serializable {
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = 6835117667479422364L;
	/**
	 * String variable holds emailId.
	 */
	private String emailId;
	/**
	 * String variable holds salesType
	 */
	private String salesType;
	/**
	 * String variable holds salesPage
	 */
	private String salesPage;
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
	 * String variable holds notificationType
	 */
	private String notificationType;
	/**
	 * String variable holds description
	 */
	private String description;
	/**
	 * String variable holds userName
	 */
	private String userName;
	/**
	 * String variable holds alertName
	 */
	private String alertName;
	/**
	 * String variable holds alertType
	 */
	private String alertType;
	/**
	 * String variable holds role
	 */
	private String role;
	/**
	 * Boolean variable holds mailsTo
	 */
	private Boolean mailsTo;
	/**
	 * Boolean variable holds mailsCc
	 */
	private Boolean mailsCc;
	/**
	 * Boolean variable holds mailsBcc
	 */
	private Boolean mailsBcc;
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
	/**
	 * @return the notificationType
	 */
	public String getNotificationType() {
		return notificationType;
	}
	/**
	 * @param notificationType the notificationType to set
	 */
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	/**
	 * @return the mailsTo
	 */
	public Boolean getMailsTo() {
		return mailsTo;
	}
	/**
	 * @param mailsTo the mailsTo to set
	 */
	public void setMailsTo(Boolean mailsTo) {
		this.mailsTo = mailsTo;
	}
	/**
	 * @return the mailsCc
	 */
	public Boolean getMailsCc() {
		return mailsCc;
	}
	/**
	 * @param mailsCc the mailsCc to set
	 */
	public void setMailsCc(Boolean mailsCc) {
		this.mailsCc = mailsCc;
	}
	/**
	 * @return the mailsBcc
	 */
	public Boolean getMailsBcc() {
		return mailsBcc;
	}
	/**
	 * @param mailsBcc the mailsBcc to set
	 */
	public void setMailsBcc(Boolean mailsBcc) {
		this.mailsBcc = mailsBcc;
	}
	/**
	 * @return the salesType
	 */
	public String getSalesType() {
		return salesType;
	}
	/**
	 * @param salesType the salesType to set
	 */
	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}
	/**
	 * @return the salesPage
	 */
	public String getSalesPage() {
		return salesPage;
	}
	/**
	 * @param salesPage the salesPage to set
	 */
	public void setSalesPage(String salesPage) {
		this.salesPage = salesPage;
	}
	
	
}
