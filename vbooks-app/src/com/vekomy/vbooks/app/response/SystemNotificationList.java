/**
 * com.vekomy.vbooks.app.response.SystemNoticationListResponse.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 24, 2013
 */
package com.vekomy.vbooks.app.response;

import java.util.List;


/**
 * @author NKR
 *
 */
public class SystemNotificationList extends Response {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6879186989518824891L;
	
	List<SystemNotification> noticationList;

	public SystemNotificationList() {
	}
	
	public SystemNotificationList(List<SystemNotification> noticationList) {
		this.noticationList = noticationList;
	}

	public List<SystemNotification> getNoticationList() {
		return noticationList;
	}

	public void setNoticationList(List<SystemNotification> noticationList) {
		this.noticationList = noticationList;
	}	
}
