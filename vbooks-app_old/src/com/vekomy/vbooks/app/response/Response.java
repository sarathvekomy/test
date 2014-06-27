/**
 * com.vekomy.vbooks.app.response.Response.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 16, 2013
 */
package com.vekomy.vbooks.app.response;

import java.io.Serializable;

/**
 * @author NKR
 * 
 */
public class Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8621558879959602410L;

	/**
	 * String variable holds message.
	 */
	private String message;

	/**
	 * Integer variable holds statusCode.
	 */
	private Integer statusCode;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the statusCode
	 */
	public Integer getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode
	 *            the statusCode to set
	 */
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

}
