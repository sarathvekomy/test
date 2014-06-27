/**
 * com.vekomy.vbooks.exception.SecurityException.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 8, 2013
 */
package com.vekomy.vbooks.exception;

import com.vekomy.vbooks.util.SecurityUtil;

/**
 * This exception class is used to throw the exception from {@link SecurityUtil}.
 * 
 * @author Sudhakar
 * 
 */
public class SecurityException extends Exception {

	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 6224442904595397514L;

	/**
	 * Constructs a new exception with default constructor.
	 */
	public SecurityException() {
	}

	/**
	 * Constructs a new exception with parameterized constructor, {@link String}
	 * as parameter.
	 * 
	 * @param message - {@link String}
	 */
	public SecurityException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with parameterized constructor,
	 * {@link Throwable} as parameter.
	 * 
	 * @param cause - {@link Throwable}
	 */
	public SecurityException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new exception with parameterized constructor, {@link String}
	 * and {@link Throwable} as parameters.
	 * 
	 * @param message - {@link String}
	 * @param cause - {@link Throwable}
	 */
	public SecurityException(String message, Throwable cause) {
		super(message, cause);
	}
}
