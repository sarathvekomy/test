 /**
 * com.vekomy.vbooks.reports.dao.TypeInfo.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Feb 5, 2014
 */
package com.vekomy.vbooks.reports.dao;


/**
 * @author priyanka
 *
 */
public class TypeInfo {

	String name;
	Class<?> clsType;
	public void addTypeInfo(String name,Class<?>classType){
		this.name = name;
		this.clsType = classType;
	}
}
