package com.gsmart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.Profile;
//import com.gsmart.model.Search;
import com.gsmart.services.ProfileServices;
//import com.gsmart.services.SearchService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.REGISTRATION)

public class RegistrationController {

	@Autowired
	ProfileServices profileServices;
	/*@Autowired
	SearchService searchService;*/


	@RequestMapping(value = "/employee" , method = RequestMethod.GET)
	public ResponseEntity<Map<String, ArrayList<Profile>>> viewEmployeeProfiles() throws GSmartBaseException{
		
		Loggers.loggerStart();
		Map<String, ArrayList<Profile>>	jsonMap = new HashMap<>();
		jsonMap.put("result", profileServices.getProfiles("employee"));
		Loggers.loggerEnd(jsonMap);
		return new ResponseEntity<Map<String,ArrayList<Profile>>>(jsonMap, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/student" ,method = RequestMethod.GET)
	public ResponseEntity<Map<String, ArrayList<Profile>>> viewStudentProfiles() throws GSmartBaseException{
		
		Loggers.loggerStart();
		Map<String, ArrayList<Profile>>	jsonMap = new HashMap<>();
		jsonMap.put("result", profileServices.getProfiles("student"));
		Loggers.loggerEnd(jsonMap);
		return new ResponseEntity<Map<String,ArrayList<Profile>>>(jsonMap, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/addProfile/{updSmartId}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> addUser(@RequestBody Profile profile,
			@PathVariable("updSmartId") String updSmartId) throws GSmartBaseException {

		Loggers.loggerStart(profile);
		Loggers.loggerValue("Added by ", updSmartId);
		
		Map<String, String> jsonMap = new HashMap<>();

		String id = profileServices.getmaxSamrtId();
		String[] part = id.split("(?<=\\D)(?=\\d)");
		int newId = Integer.parseInt(part[1]) + 1;
		String smartId = part[0] + newId;

		profile.setSmartId(smartId);
		profile.setUpdSmartId(updSmartId);

		/*if (profileServices.insertUserProfileDetails(profile)) {
			CommonMail commonMail = new CommonMail();
			try {
				commonMail.passwordMail(profile, smartId);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}*/

		jsonMap.put("Id", smartId);
		
		Loggers.loggerEnd(jsonMap);
		return new ResponseEntity<Map<String, String>>(jsonMap, HttpStatus.OK);

	}

	@RequestMapping(value = "/updateProfile/{updEmpSmartId}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> updateProfile(@RequestBody Profile profile,
			@PathVariable("updEmpSmartId") String updEmpSmartId) throws GSmartBaseException {

		Loggers.loggerStart(profile);
		Loggers.loggerValue("Updated by: ", updEmpSmartId);
		Map<String, String> jsonResult = new HashMap<>();
		String result = profileServices.updateProfile(profile);
		jsonResult.put("result", result);
		Loggers.loggerEnd(jsonResult);
		return new ResponseEntity<Map<String, String>>(jsonResult, HttpStatus.OK);

	}


	/*@RequestMapping(value = "/searchRep", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<Profile>>> searchRep(@RequestBody Search search) {
		
			Map<String, ArrayList<Profile>> jsonMap = new HashMap<String, ArrayList<Profile>>();
				Map<String, Profile> map = searchService.searchRep(search);
				ArrayList<Profile> profiless = searchService.getEmployeeInfo(search.getName(), map);
				jsonMap.put("result", profiless);
				return new ResponseEntity<Map<String, ArrayList<Profile>>>(jsonMap, HttpStatus.OK);
			
	}	*/
}