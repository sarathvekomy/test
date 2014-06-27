/**
 * com.vekomy.vbooks.app.response.ProductsResponse.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 26, 2013
 */
package com.vekomy.vbooks.app.response;


/**
 * @author Sudhakar
 *
 */
public class CustomerProductsCost extends Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2169211354085103026L;

	private String custId;
	
	private String productId;
	
	private String cost;
	
	/**
	 * @return the custId
	 */
	public String getCustId() {
		return custId;
	}

	/**
	 * @param custId the custId to set
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the cost
	 */
	public String getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(String cost) {
		this.cost = cost;
	}
}
