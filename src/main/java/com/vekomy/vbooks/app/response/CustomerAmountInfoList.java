/**
 * com.vekomy.vbooks.app.response.CustmourListResponse.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 16, 2013
 */
package com.vekomy.vbooks.app.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author NKR
 * 
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerAmountInfoList extends Response {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<CustomerAmountInfo> custmourList;

	public CustomerAmountInfoList() {
	}
	
	public CustomerAmountInfoList(List<CustomerAmountInfo> businessNames) {
		custmourList = businessNames;
	}

	public List<CustomerAmountInfo> getCustmourList() {
		return custmourList;
	}

	public void setCustmourList(List<CustomerAmountInfo> custmourList) {
		this.custmourList = custmourList;
	}	
}
