package com.vekomy.vbooks.spring.action;

/**
 * @author Swarupa.
 *
 */
public class ResultError implements IResult {
	
	/**
	 * String variable holds message.
	 */
	private String message;
	/**
	 * String variable holds status.
	 */
	private String status;

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

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer("[status: ").append(status)
				.append(", message: ").append(message).append("]").toString();
	}
}
