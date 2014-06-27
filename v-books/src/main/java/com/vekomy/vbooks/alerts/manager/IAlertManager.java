/**
 * com.vekomy.vbooks.alerts.manager.IAlertManager.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 10, 2013
 */
package com.vekomy.vbooks.alerts.manager;


import com.vekomy.vbooks.exception.ProcessingException;
import com.vekomy.vbooks.hibernate.model.VbOrganization;

/**
 * This interface is responsible for firing alert, sending E-mail and SMS.
 * 
 * @author Sudhakar
 * 
 */
public interface IAlertManager {
	/**
	 * This method is responsible to fire system alert.
	 * 
	 * @param userGroup - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @param toRecipient - {@link String}
	 * @param alertType - {@link String}
	 * @param description - {@link String}
	 * @throws ProcessingException - {@link ProcessingException}
	 * 
	 */
	public abstract void fireSystemAlert(VbOrganization organization,
			String userName, String toRecipient, String alertType,
			String description) throws ProcessingException;
	
	/**
	 * This method is responsible to fire user defined alert.
	 * 
	 * @param userGroup - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @param alertType - {@link String}
	 * @param alertMysales - {@link String}
	 * @param alertMysalesPage - {@link String}
	 * @param description - {@link String}
	 * @throws ProcessingException - {@link ProcessingException}
	 * 
	 */
	public abstract void fireUserDefinedAlert(VbOrganization organization,
			String userName, String toRecipient, String alertType,
			String alertMysales, String alertMysalesPage, String description)
			throws ProcessingException;

	/**
	 * This method is responsible to send mail, when the alert is fire.
	 * 
	 * @param from - {@link String}
	 * @param to - {@link StringBuffer}
	 * @param cc - {@link StringBuffer}
	 * @param bcc - {@link StringBuffer}
	 * @param subject - {@link String}
	 * @param content - {@link String}
	 * 
	 */
	public abstract void sendMail(String from, StringBuffer to,
			StringBuffer cc, StringBuffer bcc, String subject, String content);

	/**
	 * This method is responsible to send SMS, when the alert is fire.
	 * 
	 * @param to - {@link StringBuffer}
	 * 
	 */
	public abstract void sendSms(StringBuffer to);
}
