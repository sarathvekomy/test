/**
 * com.vekomy.vbooks.app.response.CustmourListResponse.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 19, 2013
 */
package com.vekomy.vbooks.app.request;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.vekomy.vbooks.app.response.Response;

/**
 * @author nkr
 *
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class DayBookProduct  extends Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 770142076128333773L;
	
	private String productName;
	
	private String batchNumber;
	
	private Integer openingStockQty;
	
	private Integer salesReturnStockQty;
	
	private Integer soldStockQty;
	
	private Integer returnFactoryStockQty;
	
	private Integer closeingStockQty;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Integer getOpeningStockQty() {
		return openingStockQty;
	}

	public void setOpeningStockQty(Integer openingStockQty) {
		this.openingStockQty = openingStockQty;
	}

	public Integer getSalesReturnStockQty() {
		return salesReturnStockQty;
	}

	public void setSalesReturnStockQty(Integer salesReturnStockQty) {
		this.salesReturnStockQty = salesReturnStockQty;
	}

	public Integer getSoldStockQty() {
		return soldStockQty;
	}

	public void setSoldStockQty(Integer soldStockQty) {
		this.soldStockQty = soldStockQty;
	}

	public Integer getReturnFactoryStockQty() {
		return returnFactoryStockQty;
	}

	public void setReturnFactoryStockQty(Integer returnFactoryStockQty) {
		this.returnFactoryStockQty = returnFactoryStockQty;
	}

	public Integer getCloseingStockQty() {
		return closeingStockQty;
	}

	public void setCloseingStockQty(Integer closeingStockQty) {
		this.closeingStockQty = closeingStockQty;
	}
}