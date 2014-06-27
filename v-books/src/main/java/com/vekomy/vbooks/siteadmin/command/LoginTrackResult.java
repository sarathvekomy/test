package com.vekomy.vbooks.siteadmin.command;

import java.util.Date;



public class LoginTrackResult {
	

	private String username;
	private Date lastLoginTime;
	
	
	public LoginTrackResult(String username, Date lastLoginTime) {
		this.username = username;
		this.lastLoginTime = lastLoginTime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String toString(){
		return new StringBuffer("[ userName :").append(getUsername()).
				append("lastLoginTime :").append(getLastLoginTime()).
				append("]").toString();
	}
	
	
}
