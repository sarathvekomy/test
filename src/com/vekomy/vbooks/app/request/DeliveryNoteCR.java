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
public class DeliveryNoteCR extends Request {

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
	 * DeliveryNote variable holds oldDeliveryNote.
	 */
	private DeliveryNote oldDeliveryNote;
	/**
	 * DeliveryNote variable holds newtDeliveryNote.
	 */
	private DeliveryNote newDeliveryNote;
	
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
	 * @return the oldDeliveryNote
	 */
	public DeliveryNote getOldDeliveryNote() {
		return oldDeliveryNote;
	}

	/**
	 * @param oldDeliveryNote the oldDeliveryNote to set
	 */
	public void setOldDeliveryNote(DeliveryNote oldDeliveryNote) {
		this.oldDeliveryNote = oldDeliveryNote;
	}

	/**
	 * @return the newDeliveryNote
	 */
	public DeliveryNote getNewDeliveryNote() {
		return newDeliveryNote;
	}

	/**
	 * @param newDeliveryNote the newDeliveryNote to set
	 */
	public void setNewDeliveryNote(DeliveryNote newDeliveryNote) {
		this.newDeliveryNote = newDeliveryNote;
	}	
}