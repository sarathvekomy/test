/**
 * com.vekomy.vbooks.alerts.manager.AlertManager.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 10, 2013
 */
package com.vekomy.vbooks.alerts.manager;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vekomy.vbooks.alerts.command.AlertsResult;
import com.vekomy.vbooks.alerts.dao.AlertsDao;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * This singleton class is responsible for firing alert, sending E-mail and SMS.
 * 
 * @author Sudhakar
 * 
 */
public class AlertManager implements IAlertManager {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(AlertManager.class);

	/**
	 * AlertManager variable holds singleton instance of alertManager.
	 */
	private static AlertManager alertManager = new AlertManager();

	/**
	 * private constructor for singleton instance.
	 */
	private AlertManager() {

	}

	/**
	 * This method is responsible to get the singleton instance of
	 * {@link AlertManager}.
	 * 
	 * @return alertManager - {@link IAlertManager}
	 * 
	 */
	public static IAlertManager getInstance() {
		if (alertManager == null) {
			alertManager = new AlertManager();
		}
		
		return alertManager;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vekomy.vbooks.alerts.manager.IAlertManager#fireSystemAlert(com.vekomy
	 * .vbooks.hibernate.model.VbOrganization, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void fireSystemAlert(VbOrganization organization, String userName,
			String toRecipient, String alertType, String description) {
		AlertsDao alertsDao = getAlertsDao();
		
		// Persisting system alerts history.
		alertsDao.saveSystemAlertsHistory(alertType, userName, organization);

		// Sending E-mails
		sendEmailForAlert(alertsDao, toRecipient, organization, alertType, description, OrganizationUtils.SYSTEM_ALERT);

		if (_logger.isDebugEnabled()) {
			_logger.debug("Alert type: {} have been fired for system alerts.", alertType);
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vekomy.vbooks.alerts.manager.IAlertManager#fireUserDefinedAlert(com
	 * .vekomy.vbooks.hibernate.model.VbOrganization, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void fireUserDefinedAlert(VbOrganization organization,
			String userName, String toRecipient, String alertType,
			String alertMysales, String alertMysalesPage, String description) {
		AlertsDao alertsDao = getAlertsDao();
		
		// Persisting user defined alerts history.
		alertsDao.saveUserDefinedAlertsHistory(alertType, alertMysales, alertMysalesPage, userName, organization);
		
		// Sending E-mails.
		sendEmailForAlert(alertsDao, toRecipient, organization, alertType, description, OrganizationUtils.USER_DEFINED_ALERT);

		if (_logger.isDebugEnabled()) {
			_logger.debug("Alert type: {} have been fired for user defined alerts.", alertType);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vekomy.vbooks.alerts.manager.IAlertManager#sendMail(java.lang.String,
	 * java.lang.StringBuffer, java.lang.StringBuffer, java.lang.StringBuffer,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMail(String from, StringBuffer to, StringBuffer cc, StringBuffer bcc,
			String subject, String content) {
		try {
			// Invoking Linux send mail server as a Process.
			Process process = Runtime.getRuntime().exec(new String[] { "/usr/sbin/sendmail", "-t" });
			OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream(), "8859_1");
			
			// Adding To recipients
			if (to != null) {
				writer.write("to: ");
				writer.write(to.toString());
			}
			
			// Adding CC recipients
			if (cc != null) {
				writer.write("\n");
				writer.write("cc: ");
				writer.write(cc.toString());
			}
			
			// Adding BCC recipients.
			if (bcc != null) {
				writer.write("\n");
				writer.write("bcc: ");
				writer.write(bcc.toString());
			}
			
			// Adding subject.
			writer.write("\n");
			writer.write("Subject:" + subject + "\n");
			
			// Adding body of the mail.
			writer.write("\n");
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Exception causes: {}", e.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vekomy.vbooks.alerts.manager.IAlertManager#sendSms(java.lang.StringBuffer
	 * )
	 */
	@Override
	public void sendSms(StringBuffer to) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * This method is responsible to get all the mobile nos.
	 * 
	 * @param mobileNoList - {@link List}
	 * @return mobileNos - {@link StringBuffer}
	 * 
	 */
	private StringBuffer getMobileNos(List<String> mobileNoList) {
		StringBuffer mobileNos = null;
		for (String mobileNo : mobileNoList) {
			if(mobileNos == null){
				mobileNos = new StringBuffer(mobileNo);
			} else {
				mobileNos = mobileNos.append(",").append(mobileNo);
			}
		}
		
		return mobileNos;
	}
	/**
	 * This method is used to get the {@link AlertsDao} instance.
	 * 
	 * @return alertsDao - {@link AlertsDao}
	 * 
	 */
	public AlertsDao getAlertsDao() {
		ApplicationContext context = new ClassPathXmlApplicationContext("vbooks-hibernate.xml"); 
		AlertsDao alertsDao = (AlertsDao) context.getBean("alertsDao");
		return alertsDao;
	}
	
	/**
	 * This method is responsible to send E-mails for raised alert.
	 * 
	 * @param alertsDao - {@link AlertsDao}
	 * @param toRecipient - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param alertType - {@link String}
	 * @param description - {@link String}
	 * 
	 */
	private void sendEmailForAlert(AlertsDao alertsDao, String toRecipient,
			VbOrganization organization, String alertType, String description,
			String alertcategory) {
		String defaultToRecipient = alertsDao.getEmailIDByName(toRecipient,	organization);
		List<AlertsResult> alertEmailResults = null;
		if (OrganizationUtils.SYSTEM_ALERT.equalsIgnoreCase(alertcategory)) {
			alertEmailResults = alertsDao.getEmailIDByAlertTypeForSystemAlerts(alertType, organization);
		} else if (OrganizationUtils.USER_DEFINED_ALERT.equalsIgnoreCase(alertcategory)) {
			alertEmailResults = alertsDao.getEmailIDByAlertTypeForUserDefinedAlerts(alertType, organization);
		}

		// Preparing default ToRecipient.
		StringBuffer toRecipients = new StringBuffer(defaultToRecipient);
		StringBuffer ccRecipients = null;
		StringBuffer bccRecipients = null;
		String mailRecipientType = null;
		String emailId = null;
		for (AlertsResult alertsResult : alertEmailResults) {
			mailRecipientType = alertsResult.getMailRecipientType();
			emailId = alertsResult.getEmailId();
			if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(mailRecipientType)) {
				if (toRecipients == null) {
					toRecipients = new StringBuffer(emailId);
				} else {
					toRecipients = toRecipients.append(",").append(emailId);
				}
			} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(mailRecipientType)) {
				if (ccRecipients == null) {
					ccRecipients = new StringBuffer(emailId);
				} else {
					ccRecipients = ccRecipients.append(",").append(emailId);
				}
			} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(mailRecipientType)) {
				if (bccRecipients == null) {
					bccRecipients = new StringBuffer(emailId);
				} else {
					bccRecipients = bccRecipients.append(",").append(emailId);
				}
			}
		}
		sendMail(null, toRecipients, ccRecipients, bccRecipients, alertType, description);
	}
}