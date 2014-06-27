/**
 * com.vekomy.vbooks.app.request.LoginRequestForCAU.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Dec 17, 2013
 */
package com.vekomy.vbooks.app.request;

/**
 * @author Sudhakar
 * 
 */
public class LoginRequestForCAU extends Request {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -1937886925009260433L;
	/**
	 * String variable holds userName.
	 */
	private String userName;

	/**
	 * String variable holds password.
	 */
	private String password;

	/**
	 * String variable holds version.
	 */
	private String version;

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
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
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
}
