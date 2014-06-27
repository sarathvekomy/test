/**
 * com.vekomy.vbooks.util.ReportsUtils.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Oct 21, 2013
 */
package com.vekomy.vbooks.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible to provide utilities for reports.
 * 
 * @author Swarupa
 *
 */
public class ReportsUtils {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ReportsUtils.class);
	
	/**
	 * This method is responsible to get {@link Date} format for reports download.
	 * 
	 * @return dateFormat - {@link String}
	 */
	public String getDownloadDate() {
		String dateFormat = DateUtils.downloadFormat(new Date());
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("dateformat: {}", dateFormat);
		}
		return dateFormat;
	}
}
