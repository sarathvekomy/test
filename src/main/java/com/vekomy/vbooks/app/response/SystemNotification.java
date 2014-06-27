/**
 * com.vekomy.vbooks.app.response.SystemNoticationResponse.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 24, 2013
 */
package com.vekomy.vbooks.app.response;


/**
 * @author Sudhakar
 *
 */
public class SystemNotification extends Response {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -4744325760271731653L;
	/**
	 * Integer variable holds id.
	 */
	private Integer id;
	/**
	 * String variable holds businessName.
	 */
	private String businessName;
	/**
	 * String variable holds invoiceNo.
	 */
	private String invoiceNo;
	/**
	 * String variable holds notificationType.
	 */
	private String notificationType;
	/**
	 * String variable holds notificationStatus.
	 */
	private String notificationStatus;
	
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
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}
	/**
	 * @param businessName the businessName to set
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	/**
	 * @return the invoiceNo
	 */
	public String getInvoiceNo() {
		return invoiceNo;
	}
	/**
	 * @param invoiceNo the invoiceNo to set
	 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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
	 * @return the notificationStatus
	 */
	public String getNotificationStatus() {
		return notificationStatus;
	}
	/**
	 * @param notificationStatus the notificationStatus to set
	 */
	public void setNotificationStatus(String notificationStatus) {
		this.notificationStatus = notificationStatus;
	}
	
	
}
