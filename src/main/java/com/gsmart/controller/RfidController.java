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
import com.gsmart.model.Token;
import com.gsmart.model.TransportationFee;
import com.gsmart.services.ProfileServices;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.RFID)
public class RfidController {
	@Autowired
	private ProfileServices profileServices;
	
	@Autowired
	private GetAuthorization getAuthorization;
	
	@RequestMapping(value = "/searchwithrfid", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchwithrfid(@RequestBody Profile profile, @RequestHeader HttpHeaders token, HttpSession httpSession)throws GSmartBaseException{
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Long hid=null;
		if(tokenObj.getHierarchy()==null){
			hid=profile.getHierarchy().getHid();
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}
		List<Profile> searchrfid = null;
		
		searchrfid = profileServices.searchwithrfid(profile, hid);
		Map<String, Object> hl= new HashMap<>();
		hl.put("searchrfid", searchrfid);
		
		
		return new ResponseEntity<>(hl, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/searchwithoutrfid", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchwithoutrfid(@RequestBody Profile profile, @RequestHeader HttpHeaders token, HttpSession httpSession)throws GSmartBaseException{
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Long hid=null;
		if(tokenObj.getHierarchy()==null){
			hid=profile.getHierarchy().getHid();
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}
		List<Profile> searchrfid = null;
		
		searchrfid = profileServices.searchwithoutrfid(profile, hid);
		Map<String, Object> hl= new HashMap<>();
		hl.put("searchrfid", searchrfid);
		
		
		return new ResponseEntity<>(hl, HttpStatus.OK);
		
	}

	@RequestMapping(value = "/ProfilesWithRfid/{min}/{max}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getProfilesWithRfid(@PathVariable("min") Integer min,
			@PathVariable("max") Integer max,@PathVariable("hierarchy") Long hierarchy, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		Token tokenObj=(Token) httpSession.getAttribute("token");

		str.length();

		Map<String, Object> profileListWithRfid = null;


		Map<String, Object> profile = new HashMap<>();
		Long hid=null;
		if(tokenObj.getHierarchy()==null){
			hid=hierarchy;
			
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}



			profileListWithRfid = profileServices.getProfilesWithRfid(min, max,hid);
			profile.put("profileListWithRfid", profileListWithRfid);
			Loggers.loggerEnd(profileListWithRfid);

			return new ResponseEntity<Map<String, Object>>(profile, HttpStatus.OK);

	}
	@RequestMapping(value = "/ProfilesWithoutRfid/{min}/{max}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getProfilesWithoutRfid(@PathVariable("min") Integer min,
			@PathVariable("max") Integer max,@PathVariable("hierarchy") Long hierarchy, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		Token tokenObj=(Token) httpSession.getAttribute("token");

		str.length();

		Map<String, Object> profileListWithoutRfid = null;


		Map<String, Object> profile = new HashMap<>();
		Long hid=null;
		if(tokenObj.getHierarchy()==null){
			hid=hierarchy;
			
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}


			profileListWithoutRfid = profileServices.getProfilesWithoutRfid(min, max,hid);
			profile.put("profileListWithoutRfid", profileListWithoutRfid);

			Loggers.loggerEnd(profileListWithoutRfid);

			return new ResponseEntity<Map<String, Object>>(profile, HttpStatus.OK);


	}
		
		
	
		
	@RequestMapping( method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addRfid(@RequestBody Profile rfid, @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		Loggers.loggerStart( rfid);
		IAMResponse resp=new IAMResponse();
		
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		
			profileServices.addRfid(rfid);
			Loggers.loggerEnd();
			return new ResponseEntity<IAMResponse> (resp, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/profilesListWithoutRfid/{profile}",  method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchProfilesWithoutRfid(@PathVariable String profile,@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		List<Profile> profilesListWithoutRfid = null;
        Token tokenObj=(Token) httpSession.getAttribute("token");
		
		Map<String, Object> studentMap = new HashMap<>();
			profilesListWithoutRfid = profileServices.searchProfilesWithoutRfid(profile,tokenObj.getRole(),tokenObj.getHierarchy());
			studentMap.put("profilesListWithoutRfid", profilesListWithoutRfid);
			Loggers.loggerEnd(profilesListWithoutRfid);
		 return new ResponseEntity<Map<String, Object>>(studentMap, HttpStatus.OK);
	}
	

	
	
	@RequestMapping(value="/profilesListWithRfid/{profile}", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> searchProfilesWithRfid(@PathVariable String profile,@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException{
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str =getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		List<Profile> profilesListWithRfid = null;
		 Token tokenObj=(Token) httpSession.getAttribute("token");
		Map<String,Object>employeeMap = new HashMap<>();
			profilesListWithRfid=profileServices.searchProfilesWithRfid(profile,tokenObj.getRole(),tokenObj.getHierarchy());
			employeeMap.put("profilesListWithRfid", profilesListWithRfid);
			Loggers.loggerEnd(profilesListWithRfid);
			return new ResponseEntity<Map<String,Object>>(employeeMap,HttpStatus.OK);
		
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
