package com.gsmart.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.Login;
import com.gsmart.services.PasswordServices;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Encrypt;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping("/password")
public class PasswordController {
	
	

	@Autowired
	PasswordServices passwordServices;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> setPassword(@RequestBody Login login) throws GSmartBaseException {
		
		Loggers.loggerStart();
		IAMResponse myResponse;
		login.setPassword(Encrypt.md5(String.valueOf(login.getPassword())));
		login.setAttempt(0);
		login.setEntryTime(CalendarCalculator.getTimeStamp());
		passwordServices.setPassword(login);
		System.out.println(login);
		myResponse = new IAMResponse("success");
		
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
	}
	
	

	
}
