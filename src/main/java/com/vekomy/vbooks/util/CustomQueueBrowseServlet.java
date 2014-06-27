/**
 * com.vekomy.vbooks.util.CustomQueueBrowseServlet.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created: Aug 30, 2012
 */
package com.vekomy.vbooks.util;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.web.QueueBrowseServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This servlet is responsible to delete queues from message server.
 * 
 * @author Swarupa
 *
 */
public class CustomQueueBrowseServlet extends QueueBrowseServlet {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(CustomQueueBrowseServlet.class);
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -3662965281902833709L;
	/**
	 * String variable holds BROKER_URL.
	 */
	private static final String BROKER_URL = Msg.get(MsgEnum.ACTIVEMQ_BROKER_URL);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.activemq.web.QueueBrowseServlet#doGet(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Connection connection = null;
		Session session = null;
		try {
			String queueName = request.getParameter("queueName");
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
	        connection = connectionFactory.createConnection();
	        connection.start();
	        session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
	        Destination destinationQueue = session.createQueue(queueName);
			((ActiveMQConnection) connection).destroyDestination((ActiveMQDestination) destinationQueue);
		} catch (JMSException exception) {
			// TODO:
		} finally {
			try {
				if(session != null && connection != null) {
					session.close();
					connection.close();
				}
			} catch (JMSException e) {
				if(_logger.isErrorEnabled()) {
					_logger.error("Error While closing the connection.");
				}
			}
		}
	}
}
