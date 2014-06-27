/**
 * com.vekomy.vbooks.app.request.CustomerCrRequest.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 19, 2013
 */
package com.vekomy.vbooks.app.request;

import com.vekomy.vbooks.app.response.CustomerInfo;


/**
 * @author NKR
 *
 */
public class CustomerCR extends Request {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 7370918281191331137L;
	
	/**
	 * Integer variable holds organizationId.
	 */
	private Integer organizationId;
	/**
	 * String variable holds salesExecutive.
	 */
	private String salesExecutive;
	/**
	 * DayBook variable holds oldCustomerInfo.
	 */
	private CustomerInfo oldCustomerInfo;
	/**
	 * DayBook variable holds newCustomerInfo.
	 */
	private CustomerInfo newCustomerInfo;
	/**
	 * @return the organizationId
	 */
	public Integer getOrganizationId() {
		return organizationId;
	}
	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	/**
	 * @return the salesExecutive
	 */
	public String getSalesExecutive() {
		return salesExecutive;
	}
	/**
	 * @param salesExecutive the salesExecutive to set
	 */
	public void setSalesExecutive(String salesExecutive) {
		this.salesExecutive = salesExecutive;
	}
	/**
	 * @return the oldCustomerInfo
	 */
	public CustomerInfo getOldCustomerInfo() {
		return oldCustomerInfo;
	}
	/**
	 * @param oldCustomerInfo the oldCustomerInfo to set
	 */
	public void setOldCustomerInfo(CustomerInfo oldCustomerInfo) {
		this.oldCustomerInfo = oldCustomerInfo;
	}
	/**
	 * @return the newCustomerInfo
	 */
	public CustomerInfo getNewCustomerInfo() {
		return newCustomerInfo;
	}
	/**
	 * @param newCustomerInfo the newCustomerInfo to set
	 */
	public void setNewCustomerInfo(CustomerInfo newCustomerInfo) {
		this.newCustomerInfo = newCustomerInfo;
	}
}
