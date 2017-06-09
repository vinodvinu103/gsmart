package com.gsmart.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.gsmart.model.Profile;


public class CommonMail {
//	private static final Logger logger = Logger.getLogger(CommonMail.class);

	public void mail(String toAddress, String subject, String messagebody, String fromAddress, String ccAddress)
			throws Exception {
		Session mailSession = msgGetDefaultInstance();
		Transport transport = mailSession.getTransport();
		MimeMessage message = new MimeMessage(mailSession);
		message.setSubject(subject);
		message.setContent(messagebody, "text/html");
		message.setFrom(new InternetAddress(fromAddress));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));

		sendMessage(transport, message);
	}

	private Session msgGetDefaultInstance() {
		// String host="localhost";
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

	private void sendMessage(Transport transport, MimeMessage message) throws Exception  {
			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		
	}

	public void passwordMail(Profile userProfile, String id) throws Exception {
		String toAddress = userProfile.getEmailId();
		String smartId=Encrypt.md5(id);
		String messagebody = ("<h1><font color=green>Gowdanar Technologies</font></h1>" + "<br>Hi <font color=blue>"
				+ userProfile.getFirstName() + ","
				+ "</font><br><br>"
				+ "Your G-Smart account has been activated, Login id is : "+id
				+ "<br><br> Please click on the below link to set password to your account " 
				+ "<br><br>http://172.104.60.94:8080/gsmart-js/#/setPassword/"+smartId
				+ "<br><br><br><br><font color=#999999>*** Please do not reply to this message.Replies to this message are undeliverable. *** </font>"
				+ "<br><br><br>Gowdanar Technologies Team");
		String subject = "Your Account Details";
		mail(toAddress, subject, messagebody, Constants.SUPPORT_MAIL, null);
	}


}
