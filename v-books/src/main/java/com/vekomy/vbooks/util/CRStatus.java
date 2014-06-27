/**
 * com.vekomy.vbooks.util.CRStatus.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 8, 2013
 */
package com.vekomy.vbooks.util;

/**
 * 
 * This enum class is responsible to represent the status of any change request.
 * 
 * @author Sudhakar
 * 
 */
public enum CRStatus {
	PENDING("Pending"),
	APPROVED("Approved"),
	DECLINE("Decline");
	
	private String status;
	
	private CRStatus(String status) {
		this.status = status;
	}
}
