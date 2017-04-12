package com.gsmart.controller;

import java.time.DayOfWeek;
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
	private WeekDaysService weekDaysService;

	
	@Autowired
	private GetAuthorization getAuthorization;
	
	@Autowired
	private HierarchyDao hierarchyDao;
	
	@RequestMapping(value="/{hierarchy}",method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getWeekDaysList(@PathVariable("hierarchy") Long hierarchy,@RequestHeader HttpHeaders token,HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		List<WeekDays> weekofflist=null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		
		
		Token tokenObj=(Token) httpSession.getAttribute("token");
		
		Map<String, Object> resultMap = new HashMap<>();
	
		 Long hid=null;
			if(tokenObj.getHierarchy()==null){
				System.out.println("entry into if tokenobj................");
				hid=hierarchy;
			}else{
				hid=tokenObj.getHierarchy().getHid();
			}
			
			weekofflist = weekDaysService.getWeekDaysList(hid);
			for (WeekDays weekDays : weekofflist) {
			String wk=weekDays.getWeekDay();
			String day=null;
				switch(wk) {
				case "1":
					day="SUNDAY";
					break;

				case "2":
					day="MONDAY";
					break;
					
				case "3":
					day="TUESDAY";
					break;
				case "4":
					day="WEDNESDAY";
					break;
				case "5":
					day="THURSDAY";
					break;
				case "6":
					day="FRIDAY";
					break;
				case "7":
					day="SATURDAY";
					break;
				}
			
			weekDays.setWeekDay(day);
			System.out.println("weekdays +++++"+weekDays.getWeekDay());
			}
				/* for (WeekDays weekDays : weekofflist) {
					 int num=Integer.parseInt(weekDays.getWeekDay());
					 String day=DayOfWeek.of(num).toString();
					 System.out.println("-----days----- "+DayOfWeek.of(num).toString());
					 weekDays.setWeekDay(day);
				}*/
				resultMap.put("data", weekofflist);
				resultMap.put("status", 200);
				resultMap.put("message", "success");
			
         Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	// add
	@RequestMapping(value="{hierarchy}",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> addWeekDays(@PathVariable("hierarchy") Long hierarchy,@RequestBody WeekDays weekDays,@RequestHeader HttpHeaders token,HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(weekDays);
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		
		Token tokenObj=(Token) httpSession.getAttribute("token");
		 if(tokenObj.getHierarchy()==null){
			 weekDays.setHierarchy(hierarchyDao.getHierarchyByHid(hierarchy));
			}else{
				weekDays.setHierarchy(tokenObj.getHierarchy());
			}
		
		
		
		Map<String, Object> respMap=new HashMap<>();
			boolean status =weekDaysService.addWeekDaysList(weekDays);
			  
			if (status) {
				respMap.put("status", 200);
	        	respMap.put("message", "Saved Successfully");
			} else {
				respMap.put("status", 400);
	        	respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
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

			if (task.equals("delete")) {
				weekDaysService.deleteWeekdaysList(weekDays);
			
					myResponse = new IAMResponse("DATA IS ALREADY EXIST.");
			}
			Loggers.loggerEnd();

			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		


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