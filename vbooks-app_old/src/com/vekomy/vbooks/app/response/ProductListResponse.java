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
public class ProductListResponse  extends Response {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8667143400621711790L;

	List<DeliveryNoteProductResponse> productList;

	public ProductListResponse() {
	}
	
	public ProductListResponse(List<DeliveryNoteProductResponse> productList) {
		this.productList = productList;
	}
	public List<DeliveryNoteProductResponse> getProductList() {
		return productList;
	}

	public void setProductList(List<DeliveryNoteProductResponse> productList) {
		this.productList = productList;
	}
}
