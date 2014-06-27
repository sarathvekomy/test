package com.vekomy.vbooks.mysales.cr.command;

import com.vekomy.vbooks.hibernate.model.VbJournalChangeRequest;
/**
 * @author Ankit
 *
 * 
 */
public class ChangeRequestJournalCommand extends VbJournalChangeRequest {
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
