package com.gsmart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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

import com.gsmart.model.CompoundHoliday;
import com.gsmart.model.Holiday;
import com.gsmart.model.RolePermission;
import com.gsmart.services.HolidayServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;
/**
* The HolidayController class implements an application that
* displays list of holiday entities, add new holiday entity,
* edit available holiday entity and delete available holiday entity.
* these functionalities are provided in {@link HolidayServices}
*
* @author :Nirmal Raj J
* @version 1.0
* @since 2016-08-01 
*/
@Controller
@RequestMapping(Constants.HOLIDAY)
public class HolidayController {

	@Autowired
	HolidayServices holidayServices;

	@Autowired
	GetAuthorization getAuthorization;
	
	@Autowired
	TokenService tokenService;
	

	/**
	 * to view {@link Holiday} details.
	 * @param no parameters
	 * @return returns list of holiday entities present in the Holiday table 
	 * @see List
	 * @throws GSmartBaseException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getHoliday(@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		
		String tokenNumber = token.get("Authorization").get(0);
		
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		List<Holiday> holidayList = null;
		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		
		Map<String, Object> permission = new HashMap<>();
		permission.put("modulePermission", modulePermission);

		if (modulePermission!= null) {
			holidayList = holidayServices.getHolidayList();

			permission.put("holidayList", holidayList);
			Loggers.loggerEnd(holidayList);

			Loggers.loggerEnd(holidayList);
			return new ResponseEntity<Map<String,Object>>(permission, HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String,Object>>(permission, HttpStatus.OK);
		}

	}

	/**
	 * provides the access to persist a new holiday entity 
	 * Sets the {@code timeStamp} using {@link CalendarCalculator}
	 * @param holiday is instance of {@link Holiday}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addHoliday(@RequestBody Holiday holiday, @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		
		Loggers.loggerStart(holiday);
		IAMResponse resp = new IAMResponse();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
		CompoundHoliday ch = holidayServices.addHoliday(holiday);
		
		if(ch!=null)
			resp.setMessage("success");
		else
			resp.setMessage("Already exists");
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
		} else {
			resp.setMessage("Permission Denied");
			return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
		}
		
		
		
	}

	/**
	 * provide the access to update holiday entity 
	 * @param holiday instance of {@link Holiday}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	@RequestMapping(value="/{task}", method = RequestMethod.PUT)
	public  ResponseEntity<IAMResponse> editHoliday(@RequestBody Holiday holiday, @PathVariable("task") String task,
			@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		
		Loggers.loggerStart(holiday);
		IAMResponse myResponse;
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			if(task.equals("edit"))
				holidayServices.editHoliday(holiday);
			else if(task.equals("delete"))
				holidayServices.deleteHoliday(holiday);
		
		myResponse = new IAMResponse("success");
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		} else {
			myResponse = new IAMResponse("Permission Denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
	}
	/**
	 * provide the access to delete holiday entity 
	 * @param holiday instance of {@link Holiday}
	 * @return deletion status (success/error) in JSON format
	 * @see IAMResponse
	 */
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<IAMResponse> deleteBand(@RequestBody Holiday holiday)
			throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse myResponse;
		holidayServices.deleteHoliday(holiday);
		myResponse = new IAMResponse("success");
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
	}

}
