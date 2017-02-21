package com.gsmart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.dao.ProfileDaoImp;
import com.gsmart.dao.TokenDaoImpl;
import com.gsmart.model.Band;
import com.gsmart.model.Profile;
import com.gsmart.model.Token;
import com.gsmart.model.WeekDays;
import com.gsmart.services.WeekDaysService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.WEEKDAYS)
public class WeekDayController {

	
	@Autowired
	WeekDaysService weekDaysService;

	@Autowired
	TokenDaoImpl tokenDaoImpl;
	
	@Autowired
	ProfileDaoImp profileDaoImp;
	
	@Autowired
	GetAuthorization getAuthorization;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getWeekDaysList() throws GSmartBaseException {
		Loggers.loggerStart();
		Map<String, Object> resultMap = new HashMap<>();
		List<WeekDays> list = null;
		try {
			list = weekDaysService.getWeekDaysList();
			resultMap.put("data", list);
			resultMap.put("status", 200);
			resultMap.put("message", "success");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	// add
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IAMResponse> addWeekDays(@RequestBody WeekDays weekDays,@RequestHeader HttpHeaders token) throws GSmartBaseException {
		Loggers.loggerStart(weekDays);
		String tokenNumber = token.get("Authorization").get(0);
		
		Token tokenObj= tokenDaoImpl.getToken(tokenNumber);
		 String smartid=tokenObj.getSmartId();
		 
		 Profile profileinfo = profileDaoImp.getProfileDetails(smartid);
		 
		 weekDays.setInstitution(profileinfo.getInstitution());
		 weekDays.setSchool(profileinfo.getSchool());
		 
		System.out.println("weekdays info display>>>>>>"+weekDays);
		
		
		IAMResponse rsp = null;
		weekDaysService.addWeekDaysList(weekDays);
		rsp = new IAMResponse("success");

		Loggers.loggerEnd();

		return new ResponseEntity<IAMResponse>(rsp, HttpStatus.OK);
	}

	// delete

	/*@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<IAMResponse> deleteWeekDays(@RequestBody WeekDays weekDays) throws GSmartBaseException {

		Loggers.loggerStart(weekDays);
		IAMResponse rsp = null;
		weekDaysService.deleteWeekdaysList(weekDays);
		rsp = new IAMResponse("success");

		Loggers.loggerEnd();

		return new ResponseEntity<IAMResponse>(rsp, HttpStatus.OK);
	}*/
	
	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> deleteWeekDays(@RequestBody WeekDays weekDays, @PathVariable("task") String task,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(weekDays);
		
		IAMResponse myResponse = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			if (task.equals("delete")) {
				weekDaysService.deleteWeekdaysList(weekDays);
			
					myResponse = new IAMResponse("DATA IS ALREADY EXIST.");
			}
			Loggers.loggerEnd();

			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}

		else {
			myResponse = new IAMResponse("Permission Denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}

	}

	// update
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> updateWeekDays(@RequestBody WeekDays weekDays) throws GSmartBaseException {

		Loggers.loggerStart(weekDays);
		IAMResponse rsp = null;
		weekDaysService.editWeekdaysList(weekDays);
		rsp = new IAMResponse("success");

		Loggers.loggerEnd();

		return new ResponseEntity<IAMResponse>(rsp, HttpStatus.OK);
	}

}