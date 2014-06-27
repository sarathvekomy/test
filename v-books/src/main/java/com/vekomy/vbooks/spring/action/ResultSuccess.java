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
	 * String variable holds type.
	 */
	private String type = "success";

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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer("[message: ").append(getMessage())
				.append(", data: ").append(getData())
				.append(", type: ").append(getType()).append("]").toString();
	}
}
