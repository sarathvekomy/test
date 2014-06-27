/**
 * com.vekomy.vbooks.app.response.CustomerCrResponse.java
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
 * @author Sudhakar
 *
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerInfoList extends Response {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6494765931784349822L;
	
	List<CustomerInfo> customerInfoList;

	/**
	 * @return the customerInfoList
	 */
	public List<CustomerInfo> getCustomerInfoList() {
		return customerInfoList;
	}

	/**
	 * @param customerInfoList the customerInfoList to set
	 */
	public void setCustomerInfoList(List<CustomerInfo> customerInfoList) {
		this.customerInfoList = customerInfoList;
	}
}
