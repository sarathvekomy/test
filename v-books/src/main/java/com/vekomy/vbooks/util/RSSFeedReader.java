/**
 * com.vekomy.vbooks.util.RSSFeedReader.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 29, 2013
 */
package com.vekomy.vbooks.util;

import java.io.Serializable;

/**
 * 
 * @author Sudhakar
 *
 */
public class RSSFeedReader implements Serializable{
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 5031808495131097328L;
	/**
	 * String variable holds date.
	 */
	private String date;
	/**
	 * String variable holds message.
	 */
	private String message;
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
