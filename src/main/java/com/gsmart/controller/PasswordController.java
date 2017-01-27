package com.gsmart.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.Login;
import com.gsmart.model.Token;
import com.gsmart.services.PasswordServices;
import com.gsmart.services.TokenService;
import com.gsmart.model.Profile;
import com.gsmart.services.PasswordServices;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.CommonMail;
import com.gsmart.util.Decrypt;
import com.gsmart.util.Encrypt;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping("/password")
public class PasswordController {

	@Autowired
	PasswordServices passwordServices;

	@Autowired
	TokenService tokenService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> setPassword(@RequestBody Login login, @RequestHeader HttpHeaders token,
			HttpSession httpSession)
			throws GSmartBaseException, NoSuchAlgorithmException, UnsupportedEncodingException {

		Loggers.loggerStart(login);
		System.out.println("Confirm password..."+login.getConfirmPassword());
		System.out.println("password..."+login.getPassword());
		System.out.println("smartid..."+login.getSmartId());
		System.out.println("newpassword..."+login.getNewPassword());
		Map<String, Object> responseMap = new HashMap<>();
		String tokenNumber = null;
		if (token.get("Authorization") != null) {
			System.out.println("in side if condition.....");
			tokenNumber = token.get("Authorization").get(0);
			Token tokenList = null;
			try {
				tokenList = tokenService.getToken(tokenNumber);
				String smartId = tokenList.getSmartId();
				String encrptSmartid=Encrypt.md5(smartId);
				System.out.println("encrpyted smart id........"+encrptSmartid);
				String decrtptsmartId=Decrypt.MD5(encrptSmartid);
				System.out.println("Decrypted smart id,,,,,,,,,,,"+decrtptsmartId);
				//Loggers.loggerStart("encrypted smartId is : " + Encrypt.md5(login.getSmartId()));
				boolean pwd = passwordServices.changePassword(login, smartId);
				if (pwd) {
					responseMap.put("status", 200);
					responseMap.put("message", "valid password");
				} else {
					responseMap.put("status", 404);
					responseMap.put("message", "Enter Valid Password");
				}

			} catch (GSmartServiceException e) {
				e.printStackTrace();
			}
		} 
		else {			
			login.setSmartId(login.getSmartId());			
			passwordServices.setPassword(login);
			System.out.println(login);
			responseMap.put("status", 200);
			responseMap.put("message", "sucessfully registered");
		}
		return new ResponseEntity<Map<String,Object>>(responseMap, HttpStatus.OK);
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


  
