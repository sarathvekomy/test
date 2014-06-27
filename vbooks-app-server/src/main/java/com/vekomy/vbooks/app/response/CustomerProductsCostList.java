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
public class CustomerProductsCostList  extends Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4848139135799887960L;
	
	List<CustomerProductsCost> custproductsCostList;

	public CustomerProductsCostList() {
	}

	public CustomerProductsCostList(List<CustomerProductsCost> custproductsCostList) {
		this.custproductsCostList = custproductsCostList;
	}
	public List<CustomerProductsCost> getCustproductsCostList() {
		return custproductsCostList;
	}

	public void setCustproductsCostList(List<CustomerProductsCost> custproductsCostList) {
		this.custproductsCostList = custproductsCostList;
	}
}