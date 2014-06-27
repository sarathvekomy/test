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
public class DayBookCR  extends Request {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 7780192827208813190L;
	/**
	 * Integer variable holds organizationId.
	 */
	private Integer organizationId;
	/**
	 * String variable holds salesExecutive.
	 */
	private String salesExecutive;
	/**
	 * DayBook variable holds oldDayBook.
	 */
	private DayBook oldDayBook;
	/**
	 * DayBook variable holds newtDayBook.
	 */
	private DayBook newDayBook;
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
	 * @return the oldDayBook
	 */
	public DayBook getOldDayBook() {
		return oldDayBook;
	}
	/**
	 * @param oldDayBook the oldDayBook to set
	 */
	public void setOldDayBook(DayBook oldDayBook) {
		this.oldDayBook = oldDayBook;
	}
	/**
	 * @return the newDayBook
	 */
	public DayBook getNewDayBook() {
		return newDayBook;
	}
	/**
	 * @param newDayBook the newDayBook to set
	 */
	public void setNewDayBook(DayBook newDayBook) {
		this.newDayBook = newDayBook;
	}	
}
