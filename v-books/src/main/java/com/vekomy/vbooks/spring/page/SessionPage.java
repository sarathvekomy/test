package com.vekomy.vbooks.spring.page;

import java.io.Serializable;

public class SessionPage implements Serializable{

	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -6479513314035585832L;

	/**
	 * String variable holdes module.
	 */
	private String module;

	/**
	 * String variable holdes page.
	 */
	private String page;

	/**
	 * String variable holdes pageLink.
	 */
	private String pageLink;

	public SessionPage() {
	}

	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * @param module
	 *            the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * @return the page
	 */
	public String getPage() {
		return page;
	}

	/**
	 * @param page
	 *            the page to set
	 */
	public void setPage(String page) {
		this.page = page;
	}

	/**
	 * @return the pageLink
	 */
	public String getPageLink() {
		return pageLink;
	}

	/**
	 * @param pageLink
	 *            the pageLink to set
	 */
	public void setPageLink(String pageLink) {
		this.pageLink = pageLink;
	}

}
