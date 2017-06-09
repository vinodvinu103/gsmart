package com.gsmart.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends Authenticator {

	
	//private static final Logger logger = Logger.getLogger(SMTPAuthenticator.class);

	public PasswordAuthentication getPasswordAuthentication() {
		 String username;
		 String password;

//		username = "vinod@gowdanar.com";

		username = "vinukv42@gmail.com";

		password = "8123169295";
		return new PasswordAuthentication(username, password);
	}

}
