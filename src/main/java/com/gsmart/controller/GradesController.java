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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.dao.ProfileDaoImp;
import com.gsmart.dao.TokenDaoImpl;
import com.gsmart.model.Grades;
import com.gsmart.model.Profile;

import com.gsmart.model.Token;

import com.gsmart.services.GradesService;

import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

/*
 * 
 * 
 * */
    @Controller
    @RequestMapping(Constants.GRADES)
    public class GradesController {
	
	@Autowired
	GetAuthorization getAuthorization;
	
	@Autowired
	GradesService gradesService;
	
	@Autowired
	ProfileDaoImp profileDaoImp;
	
	@Autowired
	TokenDaoImpl tokenDaoImpl;
	
	
	
	//get method
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getGradesList() throws GSmartBaseException {
		Loggers.loggerStart();
		Map<String, Object> resultMap = new HashMap<>();
		List<Grades> list = null;
		try {
			list = gradesService.getGradesList();
					
			resultMap.put("data", list);
			resultMap.put("status", 200);
			resultMap.put("message", "success");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
// add method
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addGrades(@RequestBody Grades grades,@RequestHeader HttpHeaders token) throws GSmartBaseException {
		Loggers.loggerStart(grades);
		String tokenNumber = token.get("Authorization").get(0);
		
		Token tokenObj= tokenDaoImpl.getToken(tokenNumber);
		String smartid=tokenObj.getSmartId();
		 
		 Profile profileinfo = profileDaoImp.getProfileDetails(smartid);
		 
		grades.setInstitution(profileinfo.getInstitution());
		grades.setSchool(profileinfo.getSchool());
		
		
		IAMResponse rsp = null;
		gradesService.addGrades(grades);
		rsp = new IAMResponse("success");

		Loggers.loggerEnd();

		return new ResponseEntity<IAMResponse>(rsp, HttpStatus.OK);
	}
	
/*Delete method*/
	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> deleteGrades(@RequestBody Grades grades, @PathVariable("task") String task,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(grades);
		
		IAMResponse myResponse = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			if (task.equals("delete")) {
				gradesService.deleteGrades(grades);
			
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


/*update */

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> updateGrades(@RequestBody Grades grades) throws GSmartBaseException {

		Loggers.loggerStart(grades);
		IAMResponse rsp = null;
		gradesService.updateGrades(grades);
		rsp = new IAMResponse("success");

		Loggers.loggerEnd();

		return new ResponseEntity<IAMResponse>(rsp, HttpStatus.OK);
	}
}//end of class