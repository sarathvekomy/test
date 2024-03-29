package com.vekomy.vbooks.app.hibernate.model;

// Generated Jul 4, 2013 9:22:12 AM by Hibernate Tools 3.4.0.CR1

/**
 * VbDayBookProducts generated by hbm2java
 */
public class VbDayBookProducts implements java.io.Serializable {

	private Integer id;
	private VbDayBook vbDayBook;
	private String productName;
	private String batchNumber;
	private Integer openingStock;
	private Integer productsToCustomer;
	private Integer productsToFactory;
	private Integer closingStock;

	public VbDayBookProducts() {
	}

	public VbDayBookProducts(VbDayBook vbDayBook, String batchNumber) {
		this.vbDayBook = vbDayBook;
		this.batchNumber = batchNumber;
	}

	public VbDayBookProducts(VbDayBook vbDayBook, String productName,
			String batchNumber, Integer openingStock,
			Integer productsToCustomer, Integer productsToFactory,
			Integer closingStock) {
		this.vbDayBook = vbDayBook;
		this.productName = productName;
		this.batchNumber = batchNumber;
		this.openingStock = openingStock;
		this.productsToCustomer = productsToCustomer;
		this.productsToFactory = productsToFactory;
		this.closingStock = closingStock;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VbDayBook getVbDayBook() {
		return this.vbDayBook;
	}

	public void setVbDayBook(VbDayBook vbDayBook) {
		this.vbDayBook = vbDayBook;
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

	public Integer getOpeningStock() {
		return this.openingStock;
	}

	public void setOpeningStock(Integer openingStock) {
		this.openingStock = openingStock;
	}

	public Integer getProductsToCustomer() {
		return this.productsToCustomer;
	}

	public void setProductsToCustomer(Integer productsToCustomer) {
		this.productsToCustomer = productsToCustomer;
	}

	public Integer getProductsToFactory() {
		return this.productsToFactory;
	}

	public void setProductsToFactory(Integer productsToFactory) {
		this.productsToFactory = productsToFactory;
	}

	public Integer getClosingStock() {
		return this.closingStock;
	}

	public void setClosingStock(Integer closingStock) {
		this.closingStock = closingStock;
	}

}
