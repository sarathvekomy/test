/**
 * com.vekomy.vbooks.customer.command.CustomerDocumetResults.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 24, 2013
 */
package com.vekomy.vbooks.customer.command;

import java.io.Serializable;

/**
 * @author Sudhakar
 *
 */
public class CustomerDocumetResults implements Serializable {
	
	/**
	 * long variable holds serialVersionUID
	 */
	private static final long serialVersionUID = -8493215817792808585L;
	/**
	 * String variable holds fileName.
	 */
	private String fileName;
	/**
	 * String variable holds filePath.
	 */
	private String filePath;
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
