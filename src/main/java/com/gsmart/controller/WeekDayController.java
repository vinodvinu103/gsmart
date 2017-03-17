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

import com.gsmart.dao.HierarchyDao;
import com.gsmart.dao.ProfileDaoImp;
import com.gsmart.dao.TokenDaoImpl;
import com.gsmart.model.RolePermission;
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
	
	@Autowired
	HierarchyDao hierarchyDao;
	
	@RequestMapping(value="/{hierarchy}",method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getWeekDaysList(@PathVariable("hierarchy") Long hierarchy,@RequestHeader HttpHeaders token,HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart("loggers start for weekdays +++++++++++++++++++");
		
		List<WeekDays> list = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		
		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		
		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("modulePremission", modulePermission);
	
		 System.out.println("weekdays...map entery.......");
		 Long hid=null;
			if(tokenObj.getHierarchy()==null){
				hid=hierarchy;
			}else{
				hid=tokenObj.getHierarchy().getHid();
			}
		try {
			
			System.out.println("weekdays try block entry...........");
			if (modulePermission.getView()){
				System.out.println("in side if condition for get weekdays>>>>>>>>>>>");
				list = weekDaysService.getWeekDaysList(hid);
				resultMap.put("data", list);
				resultMap.put("status", 200);
				resultMap.put("message", "success");
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
			}
			else{
				System.out.println("in side else >>>>>>>>>>>");
			}
			
		} catch (Exception e) {
			throw new GSmartBaseException(e.getMessage());
			
		}

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	// add
	@RequestMapping(value="{hierarchy}",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> addWeekDays(@PathVariable("hierarchy") Long hierarchy,@RequestBody WeekDays weekDays,@RequestHeader HttpHeaders token,HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(weekDays);
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		
		Token tokenObj= tokenDaoImpl.getToken(tokenNumber);
		 if(tokenObj.getHierarchy()==null){
			 weekDays.setHierarchy(hierarchyDao.getHierarchyByHid(hierarchy));
			}else{
				weekDays.setHierarchy(tokenObj.getHierarchy());
			}
		
		System.out.println("weekdays info display>>>>>>"+weekDays);
		
		
		Map<String, Object> respMap=new HashMap<>();
		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
			boolean status =weekDaysService.addWeekDaysList(weekDays);
			  
			if (status) {
				respMap.put("status", 200);
	        	respMap.put("message", "Saved Successfully");
			} else {
				respMap.put("status", 400);
	        	respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
			}
		} else {
			respMap.put("status", 403);
        	respMap.put("message", "Permission Denied");
		}
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}
		

	// delete
	
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