/**
 * 
 */
package com.vekomy.vbooks.app.request;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author nkr
 *
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true) 
public class SalesReturnCR extends Request {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -2102863852990521097L;
	/**
	 * Integer variable holds organizationId.
	 */
	private Integer organizationId;
	/**
	 * String variable holds salesExecutive.
	 */
	private String salesExecutive;
	/**
	 * SalesReturn variable holds oldSalesReturn.
	 */
	SalesReturn oldSalesReturn;
	
	/**
	 * SalesReturn variable holds newSalesReturn.
	 */
	SalesReturn newSalesReturn;

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
	 * @return the oldSalesReturn
	 */
	public SalesReturn getOldSalesReturn() {
		return oldSalesReturn;
	}

	/**
	 * @param oldSalesReturn the oldSalesReturn to set
	 */
	public void setOldSalesReturn(SalesReturn oldSalesReturn) {
		this.oldSalesReturn = oldSalesReturn;
	}

	/**
	 * @return the newSalesReturn
	 */
	public SalesReturn getNewSalesReturn() {
		return newSalesReturn;
	}

	/**
	 * @param newSalesReturn the newSalesReturn to set
	 */
	public void setNewSalesReturn(SalesReturn newSalesReturn) {
		this.newSalesReturn = newSalesReturn;
	}
	
}
