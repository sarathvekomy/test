package com.vekomy.vbooks.hibernate.model;

// Generated Sep 11, 2013 11:07:56 AM by Hibernate Tools 3.4.0.CR1

/**
 * VbSalesReturnProducts generated by hbm2java
 */
public class VbSalesReturnProducts implements java.io.Serializable {

	private Integer id;
	private VbSalesReturn vbSalesReturn;
	private String productName;
	private String batchNumber;
	private Integer damaged;
	private Integer resalable;
	private Integer totalQty;
	private Float resalableCost;
	private Float damagedCost;
	private float totalCost;

	public VbSalesReturnProducts() {
	}

	public VbSalesReturnProducts(VbSalesReturn vbSalesReturn,
			String productName, String batchNumber, float totalCost) {
		this.vbSalesReturn = vbSalesReturn;
		this.productName = productName;
		this.batchNumber = batchNumber;
		this.totalCost = totalCost;
	}

	public VbSalesReturnProducts(VbSalesReturn vbSalesReturn,
			String productName, String batchNumber, Integer damaged,
			Integer resalable, Integer totalQty, Float resalableCost,
			Float damagedCost, float totalCost) {
		this.vbSalesReturn = vbSalesReturn;
		this.productName = productName;
		this.batchNumber = batchNumber;
		this.damaged = damaged;
		this.resalable = resalable;
		this.totalQty = totalQty;
		this.resalableCost = resalableCost;
		this.damagedCost = damagedCost;
		this.totalCost = totalCost;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VbSalesReturn getVbSalesReturn() {
		return this.vbSalesReturn;
	}

	public void setVbSalesReturn(VbSalesReturn vbSalesReturn) {
		this.vbSalesReturn = vbSalesReturn;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBatchNumber() {
		return this.batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Integer getDamaged() {
		return this.damaged;
	}

	public void setDamaged(Integer damaged) {
		this.damaged = damaged;
	}

	public Integer getResalable() {
		return this.resalable;
	}

	public void setResalable(Integer resalable) {
		this.resalable = resalable;
	}

	public Integer getTotalQty() {
		return this.totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}

	public Float getResalableCost() {
		return this.resalableCost;
	}

	public void setResalableCost(Float resalableCost) {
		this.resalableCost = resalableCost;
	}

	public Float getDamagedCost() {
		return this.damagedCost;
	}

	public void setDamagedCost(Float damagedCost) {
		this.damagedCost = damagedCost;
	}

	public float getTotalCost() {
		return this.totalCost;
	}

	public void setTotalCost(float totalCost) {
		this.totalCost = totalCost;
	}

}