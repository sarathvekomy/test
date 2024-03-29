/**
 * com.vekomy.vbooks.app.response.LoginResponse.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 12, 2013
 */
package com.vekomy.vbooks.app.response;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author NKR
 * 
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse extends Response {
	/**
	 * 
	 */
	private static final long serialVersionUID = 894708483207170710L;

	/**
	 * VbOrganization variable holds vbOrganization.
	 */
	private Integer organizationId;

	/**
	 * String variable holds userName.
	 */
	private String userName;
	
	/**
	 * String variable holds allotmentType.
	 */
	private String allotmentType;

	/**
	 * String variable holds paymentTypes.
	 */
	private String paymentTypes;
	
	/**
	 * String variable holds journalTypes.
	 */
	private String journalTypes;
	
	/**
	 * String variable holds grandedDays.
	 */
	private String grandedDays;

	private boolean isFirstTimeLogin;
	
	private String versionName;
	
	private boolean isForceupdate;
	
	/**
	 * @return the organizationId
	 */
	public Integer getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId
	 *            the organizationId to set
	 */
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the paymentTypes
	 */
	public String getPaymentTypes() {
		return paymentTypes;
	}

	/**
	 * @param paymentTypes the paymentTypes to set
	 */
	public void setPaymentTypes(String paymentTypes) {
		this.paymentTypes = paymentTypes;
	}

	/**
	 * @return the journalTypes
	 */
	public String getJournalTypes() {
		return journalTypes;
	}

	/**
	 * @param journalTypes the journalTypes to set
	 */
	public void setJournalTypes(String journalTypes) {
		this.journalTypes = journalTypes;
	}

	/**
	 * @return the allotmentType
	 */
	public String getAllotmentType() {
		return allotmentType;
	}

	/**
	 * @param allotmentType the allotmentType to set
	 */
	public void setAllotmentType(String allotmentType) {
		this.allotmentType = allotmentType;
	}

	/**
	 * @return the grandedDays
	 */
	public String getGrandedDays() {
		return grandedDays;
	}

	/**
	 * @param grandedDays the grandedDays to set
	 */
	public void setGrandedDays(String grandedDays) {
		this.grandedDays = grandedDays;
	}

	/**
	 * @return the isFirstTimeLogin
	 */
	public boolean isFirstTimeLogin() {
		return isFirstTimeLogin;
	}

	/**
	 * @param isFirstTimeLogin the isFirstTimeLogin to set
	 */
	public void setFirstTimeLogin(boolean isFirstTimeLogin) {
		this.isFirstTimeLogin = isFirstTimeLogin;
	}

	/**
	 * @return the versionName
	 */
	public String getVersionName() {
		return versionName;
	}

	/**
	 * @param versionName the versionName to set
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/**
	 * @return the isForceupdate
	 */
	public boolean isForceupdate() {
		return isForceupdate;
	}

	/**
	 * @param isForceupdate the isForceupdate to set
	 */
	public void setForceupdate(boolean isForceupdate) {
		this.isForceupdate = isForceupdate;
	}
}
