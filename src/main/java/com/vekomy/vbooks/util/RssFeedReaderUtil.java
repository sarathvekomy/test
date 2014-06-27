/**
 * com.vekomy.vbooks.util.RssFeedReader.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 29, 2013
 */
package com.vekomy.vbooks.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This utility class is responsible to read messages from message server.
 * 
 * @author Sudhakar
 * 
 */
public class RssFeedReaderUtil {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(RssFeedReaderUtil.class);
	/**
	 * String variable holds SERVER_IP.
	 */
	private static final String SERVER_IP = Msg.get(MsgEnum.ACTIVEMQ_IP);
	/**
	 * String variable holds PORT.
	 */
	private static final String PORT = Msg.get(MsgEnum.ACTIVEMQ_PORT);

	/**
	 * This method is responsible to read messages from message server.
	 * 
	 * @param queueName - {@link String}
	 * @return feedsList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public static List<RSSFeedReader> getFeedMessages(String queueName)
			throws DataAccessException {
		XmlReader reader = null;
		List<RSSFeedReader> feedsList = new ArrayList<RSSFeedReader>();
		String MESSAGE_SERVER_URL = new StringBuffer("http://")
				.append(SERVER_IP).append(":").append(PORT)
				.append("/admin/queueBrowse/").append(queueName)
				.append("?view=rss&feedType=rss_2.0").toString();
		try {
			URL url = new URL(MESSAGE_SERVER_URL);
			reader = new XmlReader(url);
			SyndFeed feedMsg = new SyndFeedInput().build(reader);
			List<SyndEntry> feedEntries = feedMsg.getEntries();
			if (feedEntries != null && !feedEntries.isEmpty()) {
				RSSFeedReader feedReader = null;
				for (SyndEntry entry : feedEntries) {
					feedReader = new RSSFeedReader();
					feedReader.setMessage(entry.getDescription().getValue());
					feedReader.setDate(DateUtils.formatDateWithTimestamp(entry.getPublishedDate()));

					feedsList.add(feedReader);
				}
			}
		} catch (MalformedURLException exception) {
		} catch (IOException exception) {
		} catch (IllegalArgumentException exception) {
		} catch (FeedException exception) {
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException exception) {
				if (_logger.isErrorEnabled()) {
					_logger.error(exception.getMessage());
				}
			}
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("{} notifications are available for {}", feedsList.size(), queueName);
		}
		return feedsList;
	}
}
