/**
 * com.vekomy.vbooks.mysales.command.JournalCommand.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 4, 2013
 */
package com.vekomy.vbooks.mysales.command;

import com.vekomy.vbooks.hibernate.model.VbJournal;

/**
 * @author Sudhakar
 *
 * 
 */
public class JournalCommand extends VbJournal {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -208705327006410424L;
	
	/**
	 * String variable holds action.
	 */
	private String action;

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	
	

}
