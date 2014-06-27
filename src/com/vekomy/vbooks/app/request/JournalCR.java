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
public class JournalCR  extends Request {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -8788959686798890276L;
	/**
	 * Integer variable holds organizationId.
	 */
	private Integer organizationId;
	/**
	 * String variable holds salesExecutive.
	 */
	private String salesExecutive;
	/**
	 * Journal variable holds oldJournal.
	 */
	Journal oldJournal;
	
	/**
	 * Journal variable holds newtJournal.
	 */
	Journal newJournal;

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
	 * @return the oldJournal
	 */
	public Journal getOldJournal() {
		return oldJournal;
	}

	/**
	 * @param oldJournal the oldJournal to set
	 */
	public void setOldJournal(Journal oldJournal) {
		this.oldJournal = oldJournal;
	}

	/**
	 * @return the newJournal
	 */
	public Journal getNewJournal() {
		return newJournal;
	}

	/**
	 * @param newJournal the newJournal to set
	 */
	public void setNewJournal(Journal newJournal) {
		this.newJournal = newJournal;
	}
}
