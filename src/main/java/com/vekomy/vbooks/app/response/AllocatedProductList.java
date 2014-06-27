/**
 * com.vekomy.vbooks.app.response.CustmourListResponse.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 19, 2013
 */
package com.vekomy.vbooks.app.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author nkr
 *
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllocatedProductList  extends Response {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8667143400621711790L;

	List<AllocatedProduct> productList;

	private float advanceAmt;
	
	public AllocatedProductList() {
	}
	
	public float getAdvanceAmt() {
		return advanceAmt;
	}


	public void setAdvanceAmt(float advanceAmt) {
		this.advanceAmt = advanceAmt;
	}


	public AllocatedProductList(List<AllocatedProduct> productList) {
		this.productList = productList;
	}
	
	public List<AllocatedProduct> getProductList() {
		return productList;
	}

	public void setProductList(List<AllocatedProduct> productList) {
		this.productList = productList;
	}
}
