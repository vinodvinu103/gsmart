package com.gsmart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Search;
import com.gsmart.model.Token;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.RFID)
public class RfidController {
	@Autowired
	ProfileServices profileServices;
	
	@Autowired
	GetAuthorization getAuthorization;
	
	@Autowired
	TokenService tokenService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getProfilesWithoutRfid(@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		
		String tokenNumber = token.get("Authorization").get(0);
		
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");

		str.length();
		
		List<Profile> profileListWithoutRfid = null;
		List<Profile> profileListWithRfid = null;

		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		
	Map<String, Object> profile = new HashMap<>();
		profile.put("modulePermission", modulePermission);
					
  if (modulePermission!= null) {
			profileListWithoutRfid = profileServices.getProfilesWithoutRfid(tokenObj.getHierarchy());
			profile.put("profileListWithoutRfid", profileListWithoutRfid);
			
			profileListWithRfid = profileServices.getProfilesWithRfid(tokenObj.getHierarchy());
			profile.put("profileListWithRfid", profileListWithRfid);

			Loggers.loggerEnd(profileListWithoutRfid);
			Loggers.loggerEnd(profileListWithRfid);
		
			return new ResponseEntity<Map<String,Object>>(profile, HttpStatus.OK);
//			return new ResponseEntity<Map<String,Object>>(profile, HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String,Object>>(profile, HttpStatus.OK);
		}

	}
		
		
		
	@RequestMapping( method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addRfid(@RequestBody Profile rfid, @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		Loggers.loggerStart( rfid);
		IAMResponse resp=new IAMResponse();
		
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		
		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
			profileServices.addRfid(rfid);
//			List<Profile> pl1=profileServices.editRfid(rfid);
//			if(pl!=null || pl1!=null)
//			resp.setMessage("success");
//		else
//			resp.setMessage("Already exists");
		
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse> (resp, HttpStatus.OK);
		} else {
			resp.setMessage("Permission Denied");
			return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/profilesListWithoutRfid/{profile}",  method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchProfilesWithoutRfid(@PathVariable String profile,@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		List<Profile> profilesListWithoutRfid = null;
        RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
        Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
		
		Map<String, Object> studentMap = new HashMap<>();
		studentMap.put("modulePermission", modulePermission);
		if (modulePermission!= null) {			
			profilesListWithoutRfid = profileServices.searchProfilesWithoutRfid(profile,tokenObj.getRole(),tokenObj.getHierarchy());
			studentMap.put("profilesListWithoutRfid", profilesListWithoutRfid);
			Loggers.loggerEnd(profilesListWithoutRfid);
		 return new ResponseEntity<Map<String, Object>>(studentMap, HttpStatus.OK);
	} else {
		return new ResponseEntity<Map<String, Object>>(studentMap, HttpStatus.OK);
	}
	}
	

	
	
	@RequestMapping(value="/profilesListWithRfid/{profile}", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> searchProfilesWithRfid(@PathVariable String profile,@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException{
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str =getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		List<Profile> profilesListWithRfid = null;
		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber,httpSession);
		 Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
		Map<String,Object>employeeMap = new HashMap<>();
		employeeMap.put("modulePermission", modulePermission);
		if(modulePermission!=null){
			profilesListWithRfid=profileServices.searchProfilesWithRfid(profile,tokenObj.getRole(),tokenObj.getHierarchy());
			employeeMap.put("profilesListWithRfid", profilesListWithRfid);
			Loggers.loggerEnd(profilesListWithRfid);
			return new ResponseEntity<Map<String,Object>>(employeeMap,HttpStatus.OK);
		}else{
			return new ResponseEntity<Map<String,Object>>(employeeMap,HttpStatus.OK);
		}
		
	}
	
/*	@RequestMapping( method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> editRfid(@RequestBody Profile rfid, @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		Loggers.loggerStart( rfid);
		IAMResponse resp=new IAMResponse();
		
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		
		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
			List<Profile> pl=profileServices.editRfid(rfid);
			if(pl!=null)
			resp.setMessage("success");
		else
			resp.setMessage("Already exists");
		
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse> (resp, HttpStatus.OK);
		} else {
			resp.setMessage("Permission Denied");
			return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
		}
	}*/


}
