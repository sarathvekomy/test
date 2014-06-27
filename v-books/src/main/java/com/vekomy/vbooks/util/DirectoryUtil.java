/**
 * com.vekomy.vbooks.util.DirectoryUtil.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 9, 2013
 */
package com.vekomy.vbooks.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.customer.command.CustomerDocumetResults;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.exception.ProcessingException;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This singleton class is responsible to create directory structure.
 * 
 * @author Sudhakar
 * 
 */
public class DirectoryUtil {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(DirectoryUtil.class);

	/**
	 * String variable holds USER_HOME.
	 */
	private static final String USER_HOME = "user.home";
	
	/**
	 * String variable holds V_BOOKS_DIR.
	 */
	private static final String V_BOOKS_DIR = "V-Books";
	
	/**
	 * DirectoryUtil variable holds singleton instance of directoryUtil.
	 */
	private static DirectoryUtil directoryUtil = new DirectoryUtil();
	
	/**
	 * private constructor for singleton instance.
	 */
	private DirectoryUtil() {
		
	}
	
	/**
	 * This method is responsible to get the singleton instance of {@link DirectoryUtil}.
	 * 
	 * @return directoryUtil - {@link DirectoryUtil}
	 * 
	 */
	public static DirectoryUtil getInstance() {
		if(directoryUtil == null) {
			directoryUtil = new DirectoryUtil();
		}
		
		return directoryUtil;
	}
	
	/**
	 * This method is re responsible to fetch the user home location from the
	 * environmental variable.
	 * 
	 * @return userHome - {@link String}
	 */
	private String getUserHome() {
		String userHome = System.getProperty(USER_HOME);
		if (userHome == null) {
			_logger.info("Not able to get system property " + USER_HOME + ". Trying to get from system environment.");
			userHome = System.getenv(USER_HOME);
		}
		if (userHome == null) {
			throw new RuntimeException("User Home system/envrionment property is not specified as a JVM arg; use the: ["
							+ USER_HOME + "] to define it. E.g., -Duser.home=C:/Users/V-Books or set as environment variable.");
		}
		return userHome;
	}
	
	/**
	 * This method is responsible to get base directory structure.
	 * 
	 * @return baseDirectory - {@link StringBuffer}
	 */
	private StringBuffer getBaseDirectory() {
		StringBuffer baseDirectory = new StringBuffer(getUserHome()).append(File.separator).append(V_BOOKS_DIR);
		return baseDirectory;
	}
	
	/**
	 * This method is responsible to create directory structure.
	 * 
	 * @param organizationName - {@link String}
	 * @param businessName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void createDirectoryStructure(String organizationName, String businessName) throws DataAccessException {
		File directoryStructure = new File(getBaseDirectory().toString());
		directoryStructure.mkdir();
		if(directoryStructure.exists()) {
			directoryStructure = new File(new StringBuffer(getBaseDirectory()
					.append(File.separator).append(organizationName)
					.append(File.separator).append(businessName)).toString());
			directoryStructure.mkdirs();
			
			if(_logger.isDebugEnabled()) {
				_logger.debug(Msg.get(MsgEnum.DIRECTORY_CREATE_MESSAGE));
			}
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.DIRECTORY_CREATION_FAIL_MESSAGE));
        }
	}
	
	
	/**
	 * This method is responsible to get the destination location of a directory to upload the files.
	 * 
	 * @param organizationName - {@link String}
	 * @param businessName - {@link String}
	 * @return destinationPath - {@link String}
	 */
	public String getDestinationPath(String organizationName, String businessName) {
		String destinationPath = null;
		if(organizationName != null && businessName != null) {
			File file = new File(new StringBuffer(getBaseDirectory())
					.append(File.separator).append(organizationName)
					.append(File.separator).append(businessName).toString());
			destinationPath = file.getAbsolutePath();
		}
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("File Path: {}", destinationPath);
		}
		return destinationPath; 
	}
	
	/**
	 * This method is responsible to get all the files in directory.
	 * 
	 * @param organizationName - {@link String}
	 * @param businessName - {@link String}
	 * @return files - {@link List}
	 * @throws ProcessingException - {@link ProcessingException}
	 */
	public List<CustomerDocumetResults> getAllFiles(String organizationName, String businessName) throws ProcessingException {
		File folder = new File(getDestinationPath(organizationName, businessName));
		if(folder.exists()) {
			File[] filesList = folder.listFiles();
			if(filesList.length >0) {
				List<CustomerDocumetResults> files = new ArrayList<CustomerDocumetResults>();
				CustomerDocumetResults results = null;
				for (File file : filesList) {
					results = new CustomerDocumetResults();
					results.setFilePath(file.getAbsolutePath());
					results.setFileName(file.getName());
					files.add(results);
				}
				
				if(_logger.isDebugEnabled()) {
					_logger.debug("{} files have been found for the business name: {}", files.size(), businessName);
				}
				return files;
			} else {
				throw new ProcessingException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
			}
		} else {
			throw new ProcessingException(Msg.get(MsgEnum.NO_SUCH_DIRECTORY_MESSAGE));
		}
	}
	
	/**
	 *  This method is responsible to delete file from specified directory.
	 *  
	 * @param filePath - {@link String}
	 * @throws ProcessingException - {@link ProcessingException}
	 */
	public void deleteFile(String filePath) throws ProcessingException {
		File file = new File(filePath);
		if(file.exists()) {
			file.delete();
		} else {
			throw new ProcessingException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
}

