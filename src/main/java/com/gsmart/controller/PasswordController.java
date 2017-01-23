package com.gsmart.controller;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.Login;
import com.gsmart.model.Profile;
import com.gsmart.services.PasswordServices;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.CommonMail;
import com.gsmart.util.Decrypt;
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
		IAMResponse myResponse=null;
		try {
			login.setSmartId(Decrypt.MD5(login.getSmartId()));
			
			login.setPassword(Encrypt.md5(String.valueOf(login.getPassword())));
			login.setAttempt(0);
			login.setEntryTime(CalendarCalculator.getTimeStamp());
			passwordServices.setPassword(login);
			System.out.println(login);
			myResponse = new IAMResponse("success");
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value="/email",method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody String email) throws GSmartBaseException {
		Loggers.loggerStart();
		Profile profile=null;
		Map<String, Object> responseMap = new HashMap<>();
		profile=passwordServices.forgotPassword(email);
		Loggers.loggerValue("emaild id matched details", profile);
		if(profile!=null && profile.getEmailId().equals(email))
		{	
			responseMap.put("status", 200);
			responseMap.put("message", "valid user");
			CommonMail commonMail = new CommonMail();
	
			String smartId=Encrypt.md5(profile.getSmartId());
				try {
					commonMail.passwordMail(profile,smartId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}else
		{	
			responseMap.put("status", 404);
			responseMap.put("message", "Enter registered EmailId");
		}
		
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	
    }
	
}


  
