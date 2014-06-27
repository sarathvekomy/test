/**
 * com.vekomy.vbooks.alerts.manager.AlertScheduler.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 23, 2013
 */
package com.vekomy.vbooks.alerts.manager;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.command.TrendingAlertResult;
import com.vekomy.vbooks.alerts.dao.AlertsDao;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbTrending;
import com.vekomy.vbooks.hibernate.model.VbUserDefinedAlerts;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This class is responsible to schedule task regarding {@link VbTrending} under
 * {@link VbUserDefinedAlerts}.
 * 
 * @author Sudhakar
 * 
 */
public class AlertScheduler {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(AlertScheduler.class);

	/**
	 * This method is responsible to schedule {@link AlertTask}, and this method
	 * is called from Spring itself.
	 * 
	 */
	public void execute() {
		AlertManager alertManager = (AlertManager) AlertManager.getInstance();
		AlertsDao alertsDao = alertManager.getAlertsDao();
		List<VbOrganization> organizations = alertsDao.getAllOrganizations();
		AlertTask task = null;
		Timer timer = null;
		for (VbOrganization vbOrganization : organizations) {
			task = new AlertTask();
			task.setOrganization(vbOrganization);
			timer = new Timer("Timer-" + vbOrganization.getId());
			timer.schedule(task, DateUtils.getEndTimeStamp(new Date()), 24 * 60 * 60 * 1000);
			
			//TODO: We can remove the below line later.
			//timer.schedule(task, DateUtils.getSchedulerTime(), 60 * 1000);

			_logger.info("Scheduling a task for Trending alert.");
		}
	}

	/**
	 * This inner class is acting like {@link TimerTask}.
	 * 
	 * @author Sudhakar
	 *
	 */
	private class AlertTask extends TimerTask {
		
		/**
		 * VbOrganization variable holds organization.
		 */
		private VbOrganization organization;

		/**
		 * @param organization
		 *            the organization to set
		 */
		public void setOrganization(VbOrganization organization) {
			this.organization = organization;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			AlertManager alertManager = (AlertManager) AlertManager.getInstance();
			AlertsDao alertsDao = alertManager.getAlertsDao();
			String alertType = OrganizationUtils.ALERT_TYPE_TRENDING;
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
											Msg.get(MsgEnum.ALERT_TYPE_TRENDING_PRODUCT_PERCENTAGE_MESSAGE));

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
											Msg.get(MsgEnum.ALERT_TYPE_TRENDING_AMOUNT_PERCENTAGE_MESSAGE));

							_logger.info("Firing user defined alert for product alert type: {}", alertType);
						}
					}
				}
			}
		}
	}

}