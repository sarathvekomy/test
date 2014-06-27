/**
 * com.vekomy.vbooks.app.request.LoginRequest.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 16, 2013
 */
package com.vekomy.vbooks.app.request;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author NKR
 * 
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest extends Request {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7790546172590790201L;

	/**
	 * String variable holds userName.
	 */
	private String userName;

	/**
	 * String variable holds password.
	 */
	private String password;

	
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
