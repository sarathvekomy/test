package com.vekomy.vbooks.hibernate.model;

// Generated Nov 19, 2013 10:32:53 AM by Hibernate Tools 3.4.0.CR1

/**
 * VbExcessCash generated by hbm2java
 */
public class VbExcessCash implements java.io.Serializable {

	private Integer id;
	private VbUserDefinedAlerts vbUserDefinedAlerts;
	private float amount;
	private String description;

	public VbExcessCash() {
	}

	public VbExcessCash(VbUserDefinedAlerts vbUserDefinedAlerts, float amount) {
		this.vbUserDefinedAlerts = vbUserDefinedAlerts;
		this.amount = amount;
	}

	public VbExcessCash(VbUserDefinedAlerts vbUserDefinedAlerts, float amount,
			String description) {
		this.vbUserDefinedAlerts = vbUserDefinedAlerts;
		this.amount = amount;
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

	public float getAmount() {
		return this.amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
