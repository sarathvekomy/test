/**
 * com.vekomy.vbooks.exception.ProcessingException.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 2, 2013
 */
package com.vekomy.vbooks.exception;

/**
 * This exception class is used to throw the exception from Action class.
 * 
 * @author Swarupa
 *
 */
public class ProcessingException extends Exception {
	
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -701009735700094641L;
	
	/**
	 * Constructs a new exception with default constructor.
	 */
	public ProcessingException() {
		super();
	}
	
	/**
	 * Constructs a new exception with parameterized constructor, {@link String}
	 * as parameter.
	 * 
	 * @param message - {@link String}
	 */
	public ProcessingException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new exception with parameterized constructor,
	 * {@link Throwable} as parameter.
	 * 
	 * @param cause - {@link Throwable}
	 */
	public ProcessingException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new exception with parameterized constructor, {@link String}
	 * and {@link Throwable} as parameters.
	 * 
	 * @param message - {@link String}
	 * @param cause - {@link Throwable}
	 */
	public ProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

}
