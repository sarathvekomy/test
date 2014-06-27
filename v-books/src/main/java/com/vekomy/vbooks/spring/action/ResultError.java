package com.vekomy.vbooks.spring.action;


public class ResultError implements IResult {
	
	private String message;
	private String description;
	private String type="fail";


    /**
	 * 
	 */
	public ResultError(String message, String description) {
        this.description = description;
        this.message = message;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
