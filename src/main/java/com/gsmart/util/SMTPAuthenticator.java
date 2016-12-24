package com.gsmart.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends Authenticator {

	private static String username;
	private static String password;
	//private static final Logger logger = Logger.getLogger(SMTPAuthenticator.class);

	public PasswordAuthentication getPasswordAuthentication() {
		username = "kingsley.rakesh@gowdanar.com";
		password = "gowdanar@001";
		return new PasswordAuthentication(username, password);
	}

}
