package com.vekomy.vbooks.siteadmin.command;

import com.vekomy.vbooks.spring.command.ActionSupport;

/**
 * @author Satish
 * 
 * 
 */
public class ModuleSettingsCommand implements ActionSupport {
	private String[] adminModules;
	/**
	 * String[] variable holdes managementModules.
	 */
	private String[] managementModules;
	/**
	 * String[] variable holdes teacherModules.
	 */
	private String[] teacherModules;

	/**
	 * @return the adminModules
	 */
	public String[] getAdminModules() {
		return adminModules;
	}

	/**
	 * @param adminModules
	 *            the adminModules to set
	 */
	public void setAdminModules(String[] adminModules) {
		this.adminModules = adminModules;
	}

	/**
	 * @return the managementModules
	 */
	public String[] getManagementModules() {
		return managementModules;
	}

	/**
	 * @param managementModules
	 *            the managementModules to set
	 */
	public void setManagementModules(String[] managementModules) {
		this.managementModules = managementModules;
	}

	/**
	 * @return the teacherModules
	 */
	public String[] getTeacherModules() {
		return teacherModules;
	}

	/**
	 * @param teacherModules
	 *            the teacherModules to set
	 */
	public void setTeacherModules(String[] teacherModules) {
		this.teacherModules = teacherModules;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.command.ActionSupport#getAction()
	 */
	@Override
	public String getAction() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vekomy.vbooks.spring.command.ActionSupport#setAction(java.lang.String
	 * )
	 */
	@Override
	public void setAction(String action) {

	}

	public String toString() {
		return new StringBuffer("[ action :").append(getAction())
				.append(",adminModules :").append(getAdminModules())
				.append(", managementModules : ")
				.append(getManagementModules()).append(", teacherModules :")
				.append(getTeacherModules()).append("]").toString();
	}

}
