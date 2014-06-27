package com.vekomy.vbooks.hibernate.model;

// Generated Sep 11, 2013 11:07:56 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * VbSystemNotifications generated by hbm2java
 */
public class VbSystemNotifications implements java.io.Serializable {

	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 8178266990724874762L;
	private Integer id;
	private VbSalesBook vbSalesBook;
	private VbOrganization vbOrganization;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private String businessName;
	private String invoiceNo;
	private String notificationType;
	private String notificationStatus;
	private int flag;

	public VbSystemNotifications() {
	}

	public VbSystemNotifications(VbSalesBook vbSalesBook,
			VbOrganization vbOrganization, Date modifiedOn, int flag) {
		this.vbSalesBook = vbSalesBook;
		this.vbOrganization = vbOrganization;
		this.modifiedOn = modifiedOn;
		this.flag = flag;
	}

	public VbSystemNotifications(VbSalesBook vbSalesBook,
			VbOrganization vbOrganization, Date createdOn, String createdBy,
			Date modifiedOn, String modifiedBy, String businessName,
			String invoiceNo, String notificationType,
			String notificationStatus, int flag) {
		this.vbSalesBook = vbSalesBook;
		this.vbOrganization = vbOrganization;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.businessName = businessName;
		this.invoiceNo = invoiceNo;
		this.notificationType = notificationType;
		this.notificationStatus = notificationStatus;
		this.flag = flag;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VbSalesBook getVbSalesBook() {
		return this.vbSalesBook;
	}

	public void setVbSalesBook(VbSalesBook vbSalesBook) {
		this.vbSalesBook = vbSalesBook;
	}

	public VbOrganization getVbOrganization() {
		return this.vbOrganization;
	}

	public void setVbOrganization(VbOrganization vbOrganization) {
		this.vbOrganization = vbOrganization;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedOn() {
		return this.modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getBusinessName() {
		return this.businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getNotificationType() {
		return this.notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getNotificationStatus() {
		return this.notificationStatus;
	}

	public void setNotificationStatus(String notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	public int getFlag() {
		return this.flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
