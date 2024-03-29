package com.vekomy.vbooks.hibernate.model;

// Generated Nov 19, 2013 10:32:53 AM by Hibernate Tools 3.4.0.CR1

/**
 * VbMySales generated by hbm2java
 */
public class VbMySales implements java.io.Serializable {

	private Integer id;
	private VbUserDefinedAlerts vbUserDefinedAlerts;
	private VbAlertTypeMySalesPage vbAlertTypeMySalesPage;
	private VbAlertTypeMySales vbAlertTypeMySales;
	private String description;

	public VbMySales() {
	}

	public VbMySales(VbUserDefinedAlerts vbUserDefinedAlerts,
			VbAlertTypeMySalesPage vbAlertTypeMySalesPage,
			VbAlertTypeMySales vbAlertTypeMySales) {
		this.vbUserDefinedAlerts = vbUserDefinedAlerts;
		this.vbAlertTypeMySalesPage = vbAlertTypeMySalesPage;
		this.vbAlertTypeMySales = vbAlertTypeMySales;
	}

	public VbMySales(VbUserDefinedAlerts vbUserDefinedAlerts,
			VbAlertTypeMySalesPage vbAlertTypeMySalesPage,
			VbAlertTypeMySales vbAlertTypeMySales, String description) {
		this.vbUserDefinedAlerts = vbUserDefinedAlerts;
		this.vbAlertTypeMySalesPage = vbAlertTypeMySalesPage;
		this.vbAlertTypeMySales = vbAlertTypeMySales;
		this.description = description;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VbUserDefinedAlerts getVbUserDefinedAlerts() {
		return this.vbUserDefinedAlerts;
	}

	public void setVbUserDefinedAlerts(VbUserDefinedAlerts vbUserDefinedAlerts) {
		this.vbUserDefinedAlerts = vbUserDefinedAlerts;
	}

	public VbAlertTypeMySalesPage getVbAlertTypeMySalesPage() {
		return this.vbAlertTypeMySalesPage;
	}

	public void setVbAlertTypeMySalesPage(
			VbAlertTypeMySalesPage vbAlertTypeMySalesPage) {
		this.vbAlertTypeMySalesPage = vbAlertTypeMySalesPage;
	}

	public VbAlertTypeMySales getVbAlertTypeMySales() {
		return this.vbAlertTypeMySales;
	}

	public void setVbAlertTypeMySales(VbAlertTypeMySales vbAlertTypeMySales) {
		this.vbAlertTypeMySales = vbAlertTypeMySales;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
