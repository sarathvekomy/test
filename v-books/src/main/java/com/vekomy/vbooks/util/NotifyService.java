package com.vekomy.vbooks.util;

import java.util.Properties;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class NotifyService {
	private final String SMTP_HOST_NAME="webmail.tsbcorp.com";
	private final String SMTP_PORT="25";
	private final String UESR_NAME="shyamsunder.elakapalli@tsbcorp.com";
	private final String PASSWORD="9912404507";
		public void sendMessage(String toAddress[], String subject,String message, String from) throws MessagingException {
			boolean debug = true;
			Properties props = new Properties();
			props.put("mail.smtp.host", SMTP_HOST_NAME);
			props.put("mail.smtp.auth", "true");
			props.put("mail.debug", "true");
			props.put("mail.smtp.port", SMTP_PORT);
			props.put("mail.smtp.socketFactory.fallback", "false");

			Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(UESR_NAME,PASSWORD);
						}
					});

			session.setDebug(debug);
			Message msg = new MimeMessage(session);
			InternetAddress addressFrom = new InternetAddress(from);
			msg.setFrom(addressFrom);
			InternetAddress[] addressTo = new InternetAddress[toAddress.length];
			for (int i = 0; i < toAddress.length; i++) {
				addressTo[i] = new InternetAddress(toAddress[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, addressTo);
			msg.setSubject(subject);
			msg.setContent(message, "text/plain");
			Transport.send(msg);
			
		}
		
		

}
