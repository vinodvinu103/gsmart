package com.gsmart.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
				String decrtptsmartId=Decrypt.md5(encrptSmartid);
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
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}

	/*
	 * @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	 * public ResponseEntity<IAMResponse> changePassword(@RequestBody Login
	 * login, @RequestHeader HttpHeaders token, HttpSession httpSession) {
	 * Loggers.loggerStart(login); String tokenNumber = null;
	 * if(token.get("Authorization")!=null){ tokenNumber =
	 * token.get("Authorization").get(0); Token tokenList = null; try {
	 * tokenList = tokenService.getToken(tokenNumber); String smartId =
	 * tokenList.getSmartId(); passwordServices.changePassword(login, smartId);
	 * 
	 * } catch (GSmartServiceException e) { e.printStackTrace(); } } return
	 * null; }
	 */

}
