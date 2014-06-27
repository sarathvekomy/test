package com.vekomy.vbooks.security;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbOrganization;

/**
 * @author Sudhakar
 *
 * 
 */
public class User implements Principal, Serializable {

	/**
	 * long variable holdes serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * String variable holds name.
	 */
	private String name;
	/**
	 * VbOrganization variable holds organization.
	 */
	private VbOrganization organization;
	/**
	 * VbEmployee variable holds vbEmployee.
	 */
	private VbEmployee vbEmployee;
	/**
	 * String variable holds theme.
	 */
	private String theme;

	/**
	 * ArrayList<String> variable holds roles.
	 */
	private ArrayList<String> roles;

	public User() {
	}

	@SuppressWarnings("rawtypes")
	public void prepareRoles() {
		roles = new ArrayList<String>();
		Iterator roleIterator = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator();
		while (roleIterator.hasNext()) {
			GrantedAuthority authority = (GrantedAuthority) roleIterator.next();
			roles.add(authority.getAuthority());
		}
	}

	public boolean hasRole(String role) {
		return roles.contains(role);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the organization
	 */
	public VbOrganization getOrganization() {
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(VbOrganization organization) {
		this.organization = organization;
	}

	/**
	 * @return the vbEmployee
	 */
	public VbEmployee getVbEmployee() {
		return vbEmployee;
	}

	/**
	 * @param vbEmployee the vbEmployee to set
	 */
	public void setVbEmployee(VbEmployee vbEmployee) {
		this.vbEmployee = vbEmployee;
	}

	/**
	 * @return the theme
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * @param theme the theme to set
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}

	/**
	 * @return the roles
	 */
	public ArrayList<String> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}


}
