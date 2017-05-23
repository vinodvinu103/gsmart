package com.gsmart.controller;

import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.dao.TimeTableDao;
import com.gsmart.model.CompoundTimeTable;
import com.gsmart.model.TimeTable;
import com.gsmart.model.Token;
import com.gsmart.services.TimeTableService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.TIMETABLE)
public class TimeTableController {
	
	@Autowired
	private GetAuthorization getAuthorization;
	
	@Autowired
	TimeTableService timeTableService;
	
	@Autowired
	TimeTableDao tableDao;
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Map<String, String>> cal(@RequestHeader HttpHeaders token,HttpSession httpSession){
		Calendar now = Calendar.getInstance();
	    int month=now.get(Calendar.MONTH)+1;
	    int year=now.get(Calendar.YEAR);
	    String academicYear=null;
	    int incYear=1+year;
	    int decYear=year-1;
	    if(month>=6){
	    	academicYear=year+"-"+incYear;
	    }
	    else{
	    	academicYear=decYear+"-"+year;
	    }
	    Map<String, String> ac=new HashMap<>();
	    ac.put("academicYear", academicYear);
	    System.out.println("academicYear   "+academicYear);
		return new ResponseEntity<Map<String,String>>(ac, HttpStatus.OK);
	}

	@RequestMapping(value="/teacherView/{academicYear}/{day}",method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> teacherView(@RequestHeader HttpHeaders token,
			HttpSession httpSession,@PathVariable("day") String day,@PathVariable("academicYear") String academicYear)throws GSmartBaseException{
		Loggers.loggerStart(day);
		String tokenNumber=token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		List<TimeTable> teacherList = null;
		Token tokenObj=(Token) httpSession.getAttribute("token");

		Map<String, Object> permissions = new HashMap<>();
		
		teacherList=tableDao.teacherView(day, academicYear, tokenObj);
		
		if(teacherList!=null){
			permissions.put("status", 200);
			permissions.put("message", "success");
			permissions.put("teacherList",teacherList);
			
		}else{
			permissions.put("status", 404);
			permissions.put("message", "No Data Is Present");
			
		}
		
		Loggers.loggerEnd();
		return new  ResponseEntity<Map<String,Object>>(permissions, HttpStatus.OK); 
	}
	
	@RequestMapping(value="/studentView/{academicYear}/{day}",method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> studentView(@RequestHeader HttpHeaders token,
			HttpSession httpSession,@PathVariable("day") String day,@PathVariable("academicYear") String academicYear)throws GSmartBaseException{
		Loggers.loggerStart(day);
		String tokenNumber=token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		List<TimeTable> studentList = null;
		Token tokenObj=(Token) httpSession.getAttribute("token");

		Map<String, Object> permissions = new HashMap<>();
		
		studentList=timeTableService.studentView(day, academicYear, tokenObj);
		
		if(studentList!=null){
			permissions.put("status", 200);
			permissions.put("message", "success");
			permissions.put("studentList",studentList);
			
		}else{
			permissions.put("status", 404);
			permissions.put("message", "No Data Is Present");
			
		}
		
		Loggers.loggerEnd();
		return new  ResponseEntity<Map<String,Object>>(permissions, HttpStatus.OK); 
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<IAMResponse> addTTable(@RequestHeader HttpHeaders token,HttpSession httpSession,@RequestBody TimeTable timeTable)throws GSmartBaseException{
		Loggers.loggerStart(timeTable);
		IAMResponse response=null;
		CompoundTimeTable flag=null;
		String tokenNumber=token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Token tokenObj=(Token) httpSession.getAttribute("token");
		flag=tableDao.addTTable(timeTable, tokenObj);
		if(flag != null)
			response = new IAMResponse("success");
		else
			response = new IAMResponse("Oops..! Record Already Exist.. Check once again..");
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse> (response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editDeleteTimeTable(@RequestBody TimeTable timeTable,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart(timeTable);
		IAMResponse response = null;
		TimeTable card2 = null;

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Token tokenObj=(Token) httpSession.getAttribute("token");
				if (task.equals("edit")) {
					card2 =tableDao.editTimeTable(timeTable,tokenObj);
					if (card2 != null)
						response = new IAMResponse("success");
					else
						response = new IAMResponse("Oops...! Record Already Exist");
				} else if (task.equals("delete")) {
					tableDao.deleteTimeTable(timeTable);
				}
			Loggers.loggerEnd(response);
		return new ResponseEntity<IAMResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/hodViewForTeacher/{academicYear}/{smartId}/{day}",method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> hodToTeacher(@RequestHeader HttpHeaders token,
			HttpSession httpSession,@PathVariable("day") String day,@PathVariable("academicYear") String academicYear,@PathVariable("smartId") String smartId)throws GSmartBaseException{
		Loggers.loggerStart(day);
		String tokenNumber=token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		List<TimeTable> hodtoTeacher = null;
		Token tokenObj=(Token) httpSession.getAttribute("token");

		Map<String, Object> permissions = new HashMap<>();
		
		hodtoTeacher=tableDao.hodViewForTeacher(day, academicYear, tokenObj,smartId);
		
		if(hodtoTeacher!=null){
			permissions.put("status", 200);
			permissions.put("message", "success");
			permissions.put("hodtoTeacher",hodtoTeacher);
			
		}else{
			permissions.put("status", 404);
			permissions.put("message", "No Data Is Present");
			
		}
		
		Loggers.loggerEnd();
		return new  ResponseEntity<Map<String,Object>>(permissions, HttpStatus.OK); 
	}
	
	@RequestMapping(value="/hodViewForStudent/{academicYear}/{standard}/{section}/{day}",method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> hodToStudent(@RequestHeader HttpHeaders token,
			HttpSession httpSession,@PathVariable("day") String day,@PathVariable("academicYear") 
			String academicYear,@PathVariable("standard") String standard,@PathVariable("section") 
			String section)throws GSmartBaseException{
		Loggers.loggerStart(day);
		String tokenNumber=token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		List<TimeTable> hodToStudent = null;
		Token tokenObj=(Token) httpSession.getAttribute("token");

		Map<String, Object> permissions = new HashMap<>();
		
		hodToStudent=tableDao.hodViewForStudent(day, academicYear, tokenObj,standard,section);
		
		if(hodToStudent!=null){
			permissions.put("status", 200);
			permissions.put("message", "success");
			permissions.put("hodToStudent",hodToStudent);
			
		}else{
			permissions.put("status", 404);
			permissions.put("message", "No Data Is Present");
			
		}
		
		Loggers.loggerEnd();
		return new  ResponseEntity<Map<String,Object>>(permissions, HttpStatus.OK); 
	}
	
}
