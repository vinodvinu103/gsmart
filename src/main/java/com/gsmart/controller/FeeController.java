package com.gsmart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.Fee;
import com.gsmart.model.Profile;
import com.gsmart.services.FeeServices;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.SearchService;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.FEE)
public class FeeController {

	@Autowired
	FeeServices feeServices;

	@Autowired
	ProfileServices profileSevices;

	@Autowired
	SearchService searchService;

	@Autowired
	GetAuthorization getAuthorization;

	@RequestMapping(value = "/viewFee", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<Fee>>> getFeeList(@RequestBody Fee fee, @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		
		String tokenNumber=token.get("Authorization").get(0);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		
		str.length();
		Map<String, ArrayList<Fee>> jsonMap = new HashMap<String, ArrayList<Fee>>();
		
		if(getAuthorization.authorizationForPost(tokenNumber, httpSession))
		{
		
			ArrayList<Fee> feeList = (ArrayList<Fee>) feeServices.getFeeList(fee);

			if (feeList.size() != 0) {
				jsonMap.put("result", feeList);
				Loggers.loggerEnd();
				return new ResponseEntity<Map<String, ArrayList<Fee>>>(jsonMap, HttpStatus.OK);
			}else {
				jsonMap.put("result", null);
				return new ResponseEntity<Map<String, ArrayList<Fee>>>(jsonMap, HttpStatus.OK);
		    }
			
		}
		return new ResponseEntity<Map<String, ArrayList<Fee>>>(jsonMap, HttpStatus.OK);	
			
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addFee(@RequestBody Fee fee) throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse myResponse;
		try {
			fee.setTimeStamp(CalendarCalculator.getTimeStamp());
			feeServices.addFee(fee);
			myResponse = new IAMResponse("Success");
		} catch (Exception e) {
			throw new GSmartBaseException(e.getMessage());
		}
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/{smartId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> feeStructureController(@PathVariable("smartId") String smartId)
			throws GSmartBaseException {

		Loggers.loggerStart();

		ArrayList<Profile> fees = new ArrayList<Profile>();

		ArrayList<Profile> self = new ArrayList<Profile>();

		Map<String, Profile> profileMap = new HashMap<String, Profile>();

		Map<String, Profile> profiles = (Map<String, Profile>) searchService.getAllProfiles();
		ArrayList<Profile> childList = searchService.searchEmployeeInfo(smartId, profiles);

		fees = searchService.sumUpFee(childList, profiles);

		profileMap.put(smartId, profiles.get(smartId));

		Loggers.loggerStart("calling totalfees");

		self = searchService.totalfees(profileMap, fees);

		Set<String> key = profiles.keySet();
		for (int i = 0; i < fees.size(); i++) {

			for (String j : key) {

				Profile p = (Profile) profiles.get(j);
				if (p.getReportingManagerId().equals(fees.get(i).getSmartId())) {

					if (!(p.getSmartId().equals(fees.get(i).getSmartId()))) {
						fees.get(i).setChildFlag(true);
					}
				}
			}
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("selfProfile", self);
		resultMap.put("childProfile", fees);

		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

}
