/**
 * com.vekomy.vbooks.exception.DataAccessException.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Oct 08, 2013
 */
package com.vekomy.vbooks.app.exception;

/**
 * This exception class is used to throw the exception from DAO class.
 *  
 * @author Sudhakar
 *
 */
public class DataAccessException extends Exception {

	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -8640190258765993059L;

	/**
	 * Constructs a new exception with default constructor.
	 */
	public DataAccessException() {
		super();
	}

	/**
	 * Constructs a new exception with parameterized constructor, {@link String}
	 * as parameter.
	 * 
	 * @param message - {@link String}
	 */
	public DataAccessException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with parameterized constructor,
	 * {@link Throwable} as parameter.
	 * 
	 * @param cause - {@link Throwable}
	 */
	public DataAccessException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new exception with parameterized constructor, {@link String}
	 * and {@link Throwable} as parameters.
	 * 
	 * @param message - {@link String}
	 * @param cause - {@link Throwable}
	 */
	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
