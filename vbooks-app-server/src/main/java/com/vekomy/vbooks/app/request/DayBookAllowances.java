/**
 * com.vekomy.vbooks.app.request.Allowances.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 05-Sep-2013
 *
 * @author nkr
 *
 *
 */

package com.vekomy.vbooks.app.request;

/**
 * @author nkr
 * 
 */
public class DayBookAllowances {
	
	long 	rowid;
	String 	allowancesType;
	float 	amt;
	String 	remarks;
	String 	reserved;

	public long getRowid() {
		return rowid;
	}

	public void setRowid(long rowid) {
		this.rowid = rowid;
	}

	public String getAllowancesType() {
		return allowancesType;
	}

	public void setAllowancesType(String allowancesType) {
		this.allowancesType = allowancesType;
	}

	public float getAmt() {
		return amt;
	}

	public void setAmt(float amt) {
		this.amt = amt;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

}
