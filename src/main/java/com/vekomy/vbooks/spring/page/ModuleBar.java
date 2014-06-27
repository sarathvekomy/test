package com.vekomy.vbooks.spring.page;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ModuleBar implements Serializable{
	
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -8130552030255349495L;
	/**
	 * Map variable holds moduleMap.
	 */
	private final Map moduleMap = new HashMap();
	/**
	 * Page variable holds defaulPage.
	 */
	private Module defaulModule;
	
	/**
	 * @return the defaulModule
	 */
	public Module getDefaulModule() {
		return defaulModule;
	}

	/**
	 * @param defaulModule
	 *            the defaulModule to set
	 */
	public void setDefaulModule(Module defaulModule) {
		this.defaulModule = defaulModule;
	}


	public ModuleBar() {
	}

	/**
     * @param modules the modules to set
     */
    public void setModules(Properties modules) {
    	this.moduleMap.putAll(modules);
    }

    
    /**
     * @return the moduleMap
     */
    public Set getModuleKeyset() {
    	return moduleMap.keySet();
    }
    
	/**
     * @return the modules
     */
    public Module getModule(HttpServletRequest request, String key) {
    	if(StringUtils.isEmpty(key)){
    		return null;
    	}
    	String beanName = (String)moduleMap.get(key);
    	if(StringUtils.isEmpty(beanName)){
    		return null;
    	}
    	WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		Module module = (Module) applicationContext.getBean(beanName);
    	return module;
    }

}
