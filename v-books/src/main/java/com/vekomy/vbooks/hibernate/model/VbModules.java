package com.vekomy.vbooks.hibernate.model;

// Generated Nov 19, 2013 10:32:53 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * VbModules generated by hbm2java
 */
public class VbModules implements java.io.Serializable {

	private Integer id;
	private String moduleName;
	private Set<VbRoleModuleMapping> vbRoleModuleMappings = new HashSet<VbRoleModuleMapping>(
			0);

	public VbModules() {
	}

	public VbModules(String moduleName) {
		this.moduleName = moduleName;
	}

	public VbModules(String moduleName,
			Set<VbRoleModuleMapping> vbRoleModuleMappings) {
		this.moduleName = moduleName;
		this.vbRoleModuleMappings = vbRoleModuleMappings;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Set<VbRoleModuleMapping> getVbRoleModuleMappings() {
		return this.vbRoleModuleMappings;
	}

	public void setVbRoleModuleMappings(
			Set<VbRoleModuleMapping> vbRoleModuleMappings) {
		this.vbRoleModuleMappings = vbRoleModuleMappings;
	}

}
