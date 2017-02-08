package com.gsmart.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends Authenticator {

	private static String username;
	private static String password;
	//private static final Logger logger = Logger.getLogger(SMTPAuthenticator.class);

	public PasswordAuthentication getPasswordAuthentication() {

		username = "vinod@gowdanar.com";

		username = "vinodkumarreddy@gowdanar.com";

		password = "vinodvinu@103";
		return new PasswordAuthentication(username, password);
	}

}
