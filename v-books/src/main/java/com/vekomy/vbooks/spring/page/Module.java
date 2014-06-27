package com.vekomy.vbooks.spring.page;

import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class Module {

	/**
	 * String variable holdes name.
	 */
	private String name;

	/**
	 * Page variable holdes defaulPage.
	 */
	private Page defaulPage;

	/**
	 * String variable holdes label.
	 */
	private String label;

	/**
	 * String variable holdes icon.
	 */
	private String icon;

	/**
	 * LinkedHashMap variable holdes pages.
	 */
	private LinkedHashMap pages = new LinkedHashMap();

	public Module() {
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the defaulPage
	 */
	public Page getDefaulPage() {
		return defaulPage;
	}

	/**
	 * @param defaulPage
	 *            the defaulPage to set
	 */
	public void setDefaulPage(Page defaulPage) {
		this.defaulPage = defaulPage;
	}

	/**
	 * @return the pages
	 */
	public Set getPageKeyset() {
		return pages.keySet();
	}

	/**
	 * @return the pages
	 */
	public Page getPage(HttpServletRequest request, String key) {
		if(StringUtils.isEmpty(key)){
			return null;
		}
		String beanName = (String) pages.get(key);
		if(StringUtils.isEmpty(beanName)){
			return null;
		}
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession()
						.getServletContext());
		Page page = (Page) applicationContext.getBean(beanName);
		return page;
	}

	/**
	 * @param pages
	 *            the pages to set
	 */
	public void setPages(Properties pages) {
		this.pages.putAll(pages);
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

}
