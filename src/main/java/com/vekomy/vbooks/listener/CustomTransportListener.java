/**
 * com.vekomy.vbooks.listener.TransportJmsListener.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 19, 2013
 */
package com.vekomy.vbooks.listener;

import java.io.IOException;

import org.apache.activemq.transport.TransportListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This listener class is responsible to check message server status.
 * 
 * @author Sudhakar
 * 
 */
public class CustomTransportListener implements TransportListener {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(CustomTransportListener.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.activemq.transport.TransportListener#onCommand(java.lang.Object
	 * )
	 */
	@Override
	public void onCommand(Object object) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.activemq.transport.TransportListener#onException(java.io.
	 * IOException)
	 */
	@Override
	public void onException(IOException exception) {
		if(_logger.isErrorEnabled()) {
			_logger.error(Msg.get(MsgEnum.ACTIVEMQ_INACTIVE_MESSAGE));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.activemq.transport.TransportListener#transportInterupted()
	 */
	@Override
	public void transportInterupted() {
		if(_logger.isWarnEnabled()) {
			_logger.warn(Msg.get(MsgEnum.ACTIVEMQ_TRANSPORT_INTERRUPTED_MESSAGE));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.activemq.transport.TransportListener#transportResumed()
	 */
	@Override
	public void transportResumed() {
		if(_logger.isInfoEnabled()) {
			_logger.info(Msg.get(MsgEnum.ACTIVEMQ_TRANSPORT_RESUMED_MESSAGE));
		}
	}
}
