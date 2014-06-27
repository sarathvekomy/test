/**
 * com.vekomy.vbooks.listener.JmsListerner.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 19, 2013
 */
package com.vekomy.vbooks.listener;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This listener class is responsible to check whether message server is in
 * active state or not.
 * 
 * @author Sudhakar
 * 
 */
public class CustomExceptionListener implements ExceptionListener {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(CustomExceptionListener.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.ExceptionListener#onException(javax.jms.JMSException)
	 */
	@Override
	public void onException(JMSException exception) {
		if (_logger.isErrorEnabled()) {
			_logger.error(Msg.get(MsgEnum.ACTIVEMQ_INACTIVE_MESSAGE));
		}
	}
}
