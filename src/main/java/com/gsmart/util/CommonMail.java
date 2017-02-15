package com.gsmart.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.gsmart.model.Login;
import com.gsmart.model.Profile;

@SuppressWarnings("unused")

public class CommonMail {
	private static final Logger logger = Logger.getLogger(CommonMail.class);

	public void mail(String toAddress, String subject, String messagebody, String fromAddress, String ccAddress)
			throws Exception {
		Session mailSession = msgGetDefaultInstance();
		Transport transport = mailSession.getTransport();
		MimeMessage message = new MimeMessage(mailSession);
		message.setSubject(subject);
		message.setContent(messagebody, "text/html");
		message.setFrom(new InternetAddress(fromAddress));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));

		sendMessage(transport, message, message.getRecipients(Message.RecipientType.TO));
	}

	private Session msgGetDefaultInstance() {
		// String host="localhost";
		boolean debug = false;
		Properties prop = new Properties();
		prop.put("mail.transport.protocol", "smtp");
		// prop.put("mail.smtp.port",587);
		prop.put("mail.smtp.host", Constants.SMTP_HOST_NAME);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		Authenticator auth = new SMTPAuthenticator();
		Session mailSession = Session.getDefaultInstance(prop, auth);
		mailSession.setDebug(true);
		return mailSession;
	}

	private void sendMessage(Transport transport, MimeMessage message, Address[] recipients) {
		try {
			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (MessagingException e) {
			e.getMessage();
			e.printStackTrace();
		}
	}

	public void passwordMail(Profile userProfile, String id) throws Exception {
		String toAddress = userProfile.getEmailId();
		String messagebody = ("<h1><font color=green>Gowdanar Technologies</font></h1>" + "<br>Hi <font color=blue>"
				+ userProfile.getFirstName() + ","
				+ "</font><br><br>"
				+ "Your G-Smart account has been activated."
				+ "<br><br> Please click on the below link to set password to your account " 
				+ "<br><br>http://139.162.55.63:8080/gsmart-js/#/setPassword/"+id
				+ "<br><br><br><br><font color=#999999>*** Please do not reply to this message.Replies to this message are undeliverable. *** </font>"
				+ "<br><br><br>Gowdanar Technologies Team");
		String subject = "Your Account Details";
		mail(toAddress, subject, messagebody, Constants.SUPPORT_MAIL, null);
	}


}
