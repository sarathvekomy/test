package com.vekomy.vbooks.spring.command;

import java.io.Serializable;

/**
 * @author Satish
 *
 * 
 */
public interface ActionSupport extends Serializable{
	
	public static final String GET = "get";
	public static final String SAVE = "save";
	public static final String EDIT = "edit";
	public static final String DELETE = "delete";
	
	public String getAction();
	
	public void setAction(String action);

}
