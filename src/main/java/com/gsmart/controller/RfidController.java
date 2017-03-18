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
	
	@RequestMapping(value = "/{min}/{max}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getProfilesWithoutRfid(@PathVariable("min") Integer min,
			@PathVariable("max") Integer max, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		Map<String, Object> profileListWithoutRfid = null;
		Map<String, Object> profileListWithRfid = null;

		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);

		Map<String, Object> profile = new HashMap<>();
		profile.put("modulePermission", modulePermission);

		if (modulePermission != null) {
			profileListWithoutRfid = profileServices.getProfilesWithoutRfid(min, max);
			profile.put("profileListWithoutRfid", profileListWithoutRfid);

			profileListWithRfid = profileServices.getProfilesWithRfid(min, max);
			profile.put("profileListWithRfid", profileListWithRfid);

			// profile.put("profileList", profileListWithoutRfid);
			Loggers.loggerEnd(profileListWithoutRfid);
			Loggers.loggerEnd(profileListWithRfid);

			return new ResponseEntity<Map<String, Object>>(profile, HttpStatus.OK);
			// return new ResponseEntity<Map<String,Object>>(profile,
			// HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(profile, HttpStatus.OK);
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
			List<Profile> pl=profileServices.addRfid(rfid);
			List<Profile> pl1=profileServices.editRfid(rfid);
			if(pl!=null || pl1!=null)
			resp.setMessage("success");
		else
			resp.setMessage("Already exists");
		
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse> (resp, HttpStatus.OK);
		} else {
			resp.setMessage("Permission Denied");
			return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
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
//	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
//	public  ResponseEntity<IAMResponse> editLeave(@RequestBody Leave leave, @PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
//		Loggers.loggerStart();
//		IAMResponse myResponse;
//		String tokenNumber = token.get("Authorization").get(0);
//
//		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
//
//		str.length();
//		
//		if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
//			if (task.equals("edit"))
//				leaveServices.editLeave(leave);
//			else if (task.equals("delete"))
//				leaveServices.deleteLeave(leave);
//
//			myResponse = new IAMResponse("success");
//			Loggers.loggerEnd();
//			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
//		} else {
//			myResponse = new IAMResponse("Permission Denied");
//			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
//		}
//	}

}
