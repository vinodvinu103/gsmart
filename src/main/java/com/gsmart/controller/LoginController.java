package com.gsmart.controller;

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
import com.gsmart.services.LoginServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping("/login")
public class LoginController {

	
	@Autowired
	LoginServices loginServices;

	@Autowired
	GetAuthorization getAuthorization;
	
	@Autowired
	TokenService tokenService;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> authenticate(@RequestBody Login login, @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		
		String tokenNumber = null;
		
		if(token.get("Authorization")!=null)
			tokenNumber = token.get("Authorization").get(0);
		
		Loggers.loggerStart(login);
		Loggers.loggerStart(tokenNumber);
		Map<String, Object> jsonMap = loginServices.authenticate(login, tokenNumber);
		Loggers.loggerEnd(jsonMap);
		httpSession.setAttribute("tokenNumber", jsonMap.get("token"));
		Loggers.loggerValue("Token Number", jsonMap.get("token"));
		Loggers.loggerValue("Session Attribute: ", httpSession.getAttribute("tokenNumber"));
		return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, String>> logout(@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException{
		
		String tokenNumber = token.get("Authorization").get(0);
		Loggers.loggerStart(tokenNumber);
		tokenService.deleteToken(tokenNumber);
		Map<String, String> jsonMap = new HashMap<>();
		jsonMap.put("message", "Logged Out");
		httpSession.removeAttribute("permissions");
		httpSession.removeAttribute("tokenNumber");
		return new ResponseEntity<Map<String, String>>(jsonMap, HttpStatus.OK);
	}
		
}