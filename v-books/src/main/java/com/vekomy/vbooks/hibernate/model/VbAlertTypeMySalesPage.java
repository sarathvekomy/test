package com.vekomy.vbooks.hibernate.model;


// Generated Nov 19, 2013 10:32:53 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * VbAlertTypeMySalesPage generated by hbm2java
 */
public class VbAlertTypeMySalesPage implements java.io.Serializable {

	private Integer id;
	private VbAlertTypeMySales vbAlertTypeMySales;
	private String alertMySalesPage;
	private Set<VbMySales> vbMySaleses = new HashSet<VbMySales>(0);

	public VbAlertTypeMySalesPage() {
	}

	public VbAlertTypeMySalesPage(VbAlertTypeMySales vbAlertTypeMySales,
			String alertMySalesPage) {
		this.vbAlertTypeMySales = vbAlertTypeMySales;
		this.alertMySalesPage = alertMySalesPage;
	}

	public VbAlertTypeMySalesPage(VbAlertTypeMySales vbAlertTypeMySales,
			String alertMySalesPage, Set<VbMySales> vbMySaleses) {
		this.vbAlertTypeMySales = vbAlertTypeMySales;
		this.alertMySalesPage = alertMySalesPage;
		this.vbMySaleses = vbMySaleses;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VbAlertTypeMySales getVbAlertTypeMySales() {
		return this.vbAlertTypeMySales;
	}

	public void setVbAlertTypeMySales(VbAlertTypeMySales vbAlertTypeMySales) {
		this.vbAlertTypeMySales = vbAlertTypeMySales;
	}

	public String getAlertMySalesPage() {
		return this.alertMySalesPage;
	}

	public void setAlertMySalesPage(String alertMySalesPage) {
		this.alertMySalesPage = alertMySalesPage;
	}

	public Set<VbMySales> getVbMySaleses() {
		return this.vbMySaleses;
	}

	public void setVbMySaleses(Set<VbMySales> vbMySaleses) {
		this.vbMySaleses = vbMySaleses;
	}

}
