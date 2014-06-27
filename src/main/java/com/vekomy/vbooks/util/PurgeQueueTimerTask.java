/**
 * com.vekomy.vbooks.util.PurgeQueueTimerTask.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jan 10, 2014
 */
package com.vekomy.vbooks.util;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * @author Swarupa
 *
 */
public class PurgeQueueTimerTask {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(PurgeQueueTimerTask.class);
	/**
	 * String variable holds BROKER_URL.
	 */
	private static final String BROKER_URL = Msg.get(MsgEnum.ACTIVEMQ_BROKER_URL);
	/**
	 * String variable holds ACTIVEMQ_PURGE_QUEUE.
	 */
	private static final String ACTIVEMQ_PURGE_QUEUE = "ActiveMQ.DLQ";
	
	/**
	 * This method is responsible to delete the ActiveMQ default consumer queue(ActiveMQ.DLQ).
	 */
	public void execute() {
		Session session = null;
		ActiveMQConnection activeMQConnection = null;
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
			activeMQConnection = (ActiveMQConnection) connectionFactory.createConnection();
			activeMQConnection.start();
			session = activeMQConnection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			Destination destinationQueue = session.createQueue(ACTIVEMQ_PURGE_QUEUE);
			activeMQConnection.destroyDestination((ActiveMQDestination) destinationQueue);
		} catch (JMSException exception) {
			if(_logger.isErrorEnabled()) {
				_logger.error("Cause: {}, Exception: {}", exception.getMessage(), exception);
			}
		} finally {
			try {
				if (session != null && activeMQConnection != null) {
					session.close();
					activeMQConnection.close();
				}
			} catch (JMSException exception2) {
				if(_logger.isErrorEnabled()) {
					_logger.error("Cause: {}, Exception: {}", exception2.getMessage(), exception2);
				}
			}
		}
	}
}
