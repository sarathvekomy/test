package com.vekomy.vbooks.hibernate.model;

// Generated Sep 11, 2013 11:07:56 AM by Hibernate Tools 3.4.0.CR1

/**
 * VbRoleModuleMapping generated by hbm2java
 */
public class VbRoleModuleMapping implements java.io.Serializable {

	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -8020862198485959741L;
	private Integer id;
	private VbRole vbRole;
	private VbModules vbModules;

	public VbRoleModuleMapping() {
	}

	public VbRoleModuleMapping(VbRole vbRole, VbModules vbModules) {
		this.vbRole = vbRole;
		this.vbModules = vbModules;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VbRole getVbRole() {
		return this.vbRole;
	}

	public void setVbRole(VbRole vbRole) {
		this.vbRole = vbRole;
	}

	public VbModules getVbModules() {
		return this.vbModules;
	}

	public void setVbModules(VbModules vbModules) {
		this.vbModules = vbModules;
	}

}
