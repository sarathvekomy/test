package com.vekomy.vbooks.spring.action;

/**
 * @author Sudhakar
 * 
 * 
 */
public class ResultSuccess implements IResult {

	/**
	 * String variable holds message.
	 */
	private String message;
	/**
	 * Object variable holds data.
	 */
	private Object data;
	/**
	 * String variable holds status.
	 */
	private String status;

	/**
	 * 
	 */
	public ResultSuccess() {
		// TODO Auto-generated constructor stub
	}

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
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Object data) {
		this.data = data;
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
		return new StringBuffer("[data: ").append(data).append(", status: ")
				.append(status).append(", message: ").append(message)
				.append("]").toString();
	}
}
