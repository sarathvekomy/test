package com.vekomy.vbooks.hibernate.model;

// Generated Jul 16, 2013 11:21:50 AM by Hibernate Tools 3.4.0.CR1

/**
 * VbTrending generated by hbm2java
 */
public class VbTrending implements java.io.Serializable {

	private Integer id;
	private VbUserDefinedAlerts vbUserDefinedAlerts;
	private Long amountPersentage;
	private Long productPercentage;
	private String description;

	public VbTrending() {
	}

	public VbTrending(VbUserDefinedAlerts vbUserDefinedAlerts) {
		this.vbUserDefinedAlerts = vbUserDefinedAlerts;
	}

	public VbTrending(VbUserDefinedAlerts vbUserDefinedAlerts,
			Long amountPersentage, Long productPercentage, String description) {
		this.vbUserDefinedAlerts = vbUserDefinedAlerts;
		this.amountPersentage = amountPersentage;
		this.productPercentage = productPercentage;
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

	public Long getAmountPersentage() {
		return this.amountPersentage;
	}

	public void setAmountPersentage(Long amountPersentage) {
		this.amountPersentage = amountPersentage;
	}

	public Long getProductPercentage() {
		return this.productPercentage;
	}

	public void setProductPercentage(Long productPercentage) {
		this.productPercentage = productPercentage;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
