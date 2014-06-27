/**
 * 
 */
package com.vekomy.vbooks.app.request;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author nkr
 *
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomersInfoRequest extends Request {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4261188551166808172L;

	Integer orgId;
	
	List<String> businessNames;

	/**
	 * @return the orgId
	 */
	public Integer getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return the businessNames
	 */
	public List<String> getBusinessNames() {
		return businessNames;
	}

	/**
	 * @param businessNames the businessNames to set
	 */
	public void setBusinessNames(List<String> businessNames) {
		this.businessNames = businessNames;
	}
}
