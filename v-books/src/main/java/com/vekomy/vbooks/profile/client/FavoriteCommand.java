package com.vekomy.vbooks.profile.client;

import com.vekomy.vbooks.spring.command.ActionSupport;

public class FavoriteCommand implements ActionSupport {

	/**
	 * String variable holdes action.
	 */
	private String action;
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
	/**
	 * String variable holdes favTitle.
	 */
	private String favTitle;

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
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

	/**
	 * @return the favTitle
	 */
	public String getFavTitle() {
		return favTitle;
	}

	/**
	 * @param favTitle
	 *            the favTitle to set
	 */
	public void setFavTitle(String favTitle) {
		this.favTitle = favTitle;
	}

}
