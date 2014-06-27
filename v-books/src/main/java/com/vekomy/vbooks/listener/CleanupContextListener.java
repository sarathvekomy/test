/**
 * com.vekomy.vbooks.listener.CleanupContextListener.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Nov 18, 2013
 */
package com.vekomy.vbooks.listener;

import java.lang.reflect.Field;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.ConnectionImpl;

/**
 * @author Sudhakar
 *
 */
public class CleanupContextListener implements ServletContextListener{
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(CleanupContextListener.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// Canceling all the timer tasks.
		if (ConnectionImpl.class.getClassLoader() == getClass().getClassLoader()) {
		    Field field;
			try {
				field = ConnectionImpl.class.getDeclaredField("cancelTimer");
				 field.setAccessible(Boolean.TRUE);
				 Timer timer = (Timer) field.get(null);
				 timer.cancel();
			} catch (NoSuchFieldException exception) {
			} catch (SecurityException exception) {
			} catch (IllegalArgumentException exception) {
			} catch (IllegalAccessException exception) {
			}
		}
		
		// Un-registering the database driver.
		Enumeration<Driver> drivers = DriverManager.getDrivers();
	    while (drivers.hasMoreElements()) {
	        Driver driver = drivers.nextElement();
	        ClassLoader driverclassLoader = driver.getClass().getClassLoader();
	        ClassLoader thisClassLoader = this.getClass().getClassLoader();
	        if (driverclassLoader != null && thisClassLoader != null &&  driverclassLoader.equals(thisClassLoader)) {
	            try {
	                _logger.warn("Deregistering: " + driver);
	                DriverManager.deregisterDriver(driver);
	            } catch (SQLException exception) {
	            }
	        }
	    }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
	 * .ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		
	}

}
