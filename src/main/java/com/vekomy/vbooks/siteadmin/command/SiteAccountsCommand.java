package com.vekomy.vbooks.siteadmin.command;

import com.vekomy.vbooks.spring.command.ActionSupport;

public class SiteAccountsCommand implements ActionSupport {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 1149490023376814337L;
	/**
	 * int variable holds id
	 */
	private int schoolId;
	/**
	 * String variable holds smsLimit
	 */
	private String smsLimit;
	/**
	 * String variable holds action
	 */
	private String action;
	/**
	 * String variable holds nirvahakAccountKey
	 */
	private String nirvahakAccountKey;
	/**
	 * String variable holds startingDate
	 */
	private String startingDate;
	/**
	 * String variable holds endingDate
	 */
	private String endingDate;

	/**
	 * @return startingDate
	 */
	public String getStartingDate() {
		return startingDate;
	}

	/**
	 * @param startingDate
	 *           the startingDate to set
	 */
	public void setStartingDate(String startingDate) {
		this.startingDate = startingDate;
	}

	/**
	 * @return endingDate
	 */ 
	public String getEndingDate() {
		return endingDate;
	}

	/**
	 * @param endingDate
	 *            the endingDate to set
	 */
	public void setEndingDate(String endingDate) {
		this.endingDate = endingDate;
	}

	/**
	 * @return schoolId
	 */
	public int getSchoolId() {
		return schoolId;
	}

	/**
	 * @param schoolId
	 *         the schoolId to set
	 */
	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}

	/**
	 * @return smsLimit
	 */
	public String getSmsLimit() {
		return smsLimit;
	}

	/**
	 * @param smsLimit
	 *   the smsLimit to set
	 */
	public void setSmsLimit(String smsLimit) {
		this.smsLimit = smsLimit;
	}

	/* (non-Javadoc)
	 * @see com.vekomy.vbooks.spring.command.ActionSupport#getAction()
	 */
	public String getAction() {
		return action;
	}

	/**  
	 * @return nirvahakAccountKey
	 */
	public String getNirvahakAccountKey() {
		return nirvahakAccountKey;
	}

	/**
	 * @param nirvahakAccountKey
	 *         the nirvahakAccountKeyto set
	 */
	public void setNirvahakAccountKey(String nirvahakAccountKey) {
		this.nirvahakAccountKey = nirvahakAccountKey;
	}

	/* (non-Javadoc)
	 * @see com.vekomy.vbooks.spring.command.ActionSupport#setAction(java.lang.String)
	 */
	public void setAction(String action) {
		this.action = action;
	}

	public String toString() {
		return new StringBuffer("action :").append(getAction())
				.append(", smsLimit :").append(getSmsLimit())
				.append(", schoolId :").append(getSchoolId())
				.append(", startingDate :").append(getStartingDate())
				.append(", endingDate :").append(getEndingDate()).append("]")
				.toString();
	}

}
