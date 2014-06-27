/**
 * com.vekomy.vbooks.listener.ScheduleJobListener.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Oct 28, 2013
 */

package com.vekomy.vbooks.listener;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.command.TrendingAlertResult;
import com.vekomy.vbooks.alerts.dao.AlertsDao;
import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbTrending;
import com.vekomy.vbooks.hibernate.model.VbUserDefinedAlerts;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This listener class is responsible to schedule a job regarding {@link VbTrending} under
 * {@link VbUserDefinedAlerts}.
 * 
 * @author Sudhakar
 * 
 */
public class ScheduleJobListener implements ServletContextListener {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ScheduleJobListener.class);
	/**
	 * ScheduledExecutorService variable holds scheduler.
	 */
	private final static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		scheduler.shutdownNow();
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
		try {
			AlertManager alertManager = (AlertManager) AlertManager.getInstance();
			AlertsDao alertsDao = alertManager.getAlertsDao();
			List<VbOrganization> organizations = alertsDao.getAllActiveOrganizations();
			if(!organizations.isEmpty()) {
				for (final VbOrganization organization : organizations) {
					scheduler.schedule(new Callable<Object>() {
						@Override
						public Object call() throws Exception {
							AlertManager alertManager = (AlertManager) AlertManager.getInstance();
							AlertsDao alertsDao = alertManager.getAlertsDao();
							String alertType = OrganizationUtils.ALERT_TYPE_TRENDING;
							try {
								VbTrending trending = alertsDao.getVbTrending(alertType, organization);
								if (trending != null) {
										Long productPercentage = trending.getProductPercentage();
										Long amountPercentage = trending.getAmountPersentage();
										TrendingAlertResult result = null;
										if (productPercentage != null) {
											result = alertsDao.getProductTrendingAlertResult(organization);
											Integer yesterdaysSoldQty = result.getYesterdaySoldQty();
											Integer todaysSoldQty = result.getTodaySoldQty();
											if (yesterdaysSoldQty > todaysSoldQty) {
												Integer difference = yesterdaysSoldQty - todaysSoldQty;
												Long decreasingPercentage = (long) ((difference / yesterdaysSoldQty) * 100);
												if (decreasingPercentage > productPercentage) {
													AlertManager.getInstance().fireUserDefinedAlert(organization, organization.getSuperUserName(),
																	organization.getSuperUserName(), alertType, null, null,
																	Msg.get(MsgEnum.ALERT_TYPE_TRENDING_PRODUCT_PERCENTAGE_LESS_MESSAGE));

													_logger.info("Firing user defined alert for product alert type: {}", alertType);
												}
											} else {
												Integer difference = todaysSoldQty - yesterdaysSoldQty;
												Long increasingPercentage = (long) ((difference / todaysSoldQty) * 100);
												if(increasingPercentage > productPercentage) {
													AlertManager.getInstance().fireUserDefinedAlert(organization, organization.getSuperUserName(),
															organization.getSuperUserName(), alertType, null, null,
															Msg.get(MsgEnum.ALERT_TYPE_TRENDING_PRODUCT_PERCENTAGE_HIGH_MESSAGE));

													_logger.info("Firing user defined alert for product alert type: {}", alertType);
												}
											}
										} else if (amountPercentage != null) {
											result = alertsDao.getAmountTrendingAlertResult(organization);
											Float yesterdaysAmount = result.getYesterdaysAmount();
											Float todaysAmount = result.getTodaysAmount();
											if (yesterdaysAmount > todaysAmount) {
												Float difference = yesterdaysAmount - todaysAmount;
												Long decreasingPercentage = (long) ((difference / yesterdaysAmount) * 100);
												if (decreasingPercentage > amountPercentage) {
													AlertManager.getInstance().fireUserDefinedAlert(organization, organization.getSuperUserName(),
																	organization.getSuperUserName(), alertType, null, null,
																	Msg.get(MsgEnum.ALERT_TYPE_TRENDING_AMOUNT_PERCENTAGE_LESS_MESSAGE));

													_logger.info("Firing user defined alert for product alert type: {}", alertType);
												} 
											} else {
												Float difference = todaysAmount - yesterdaysAmount;
												Long increasingPercentage = (long) ((difference / todaysAmount) * 100);
												if (increasingPercentage > amountPercentage) {
													AlertManager.getInstance().fireUserDefinedAlert(organization, organization.getSuperUserName(),
															organization.getSuperUserName(), alertType, null, null,
															Msg.get(MsgEnum.ALERT_TYPE_TRENDING_AMOUNT_PERCENTAGE_HIGH_MESSAGE));

													_logger.info("Firing user defined alert for product alert type: {}", alertType);
												}
											}
										}
								}
							} catch(DataAccessException exception) {
								if(_logger.isErrorEnabled()) {
									_logger.error("Cause: {}, Exception: {}", exception.getMessage(), exception);
								}
							}
							return null;
						}
					}, 24 * 60 * 60 * 1000, TimeUnit.SECONDS);
					_logger.info("Scheduling a task for Trending alert.");
				}
			} else {
				_logger.info("Organizations not configured at this point.");
			}
		} catch (DataAccessException exception) {
			if(_logger.isErrorEnabled()) {
				_logger.error(exception.getMessage());
			}
		}
	}
}
