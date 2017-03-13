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
import com.gsmart.model.RolePermission;
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
	public ResponseEntity<Map<String, Object>> getGradesList(@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
	    str.length(); 
	    
	    List<Grades> list = null;
	    Token tokenObj= tokenDaoImpl.getToken(tokenNumber);
	    RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
	    
		Map<String, Object> resultMap = new HashMap<>();
		
		resultMap.put("modulePermission",modulePermission);
		
		
			list = gradesService.getGradesList(tokenObj.getHierarchy().getHid());
					
			resultMap.put("data", list);
			resultMap.put("status", 200);
			resultMap.put("message", "success");
		
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addGrades(@RequestBody Grades grades,@RequestHeader HttpHeaders token) throws GSmartBaseException {
		Loggers.loggerStart(grades);
		boolean flag=false;
		String tokenNumber = token.get("Authorization").get(0);
		
		Token tokenObj= tokenDaoImpl.getToken(tokenNumber);
		String smartid=tokenObj.getSmartId();
		 grades.setHierarchy(tokenObj.getHierarchy());
		
		 Profile profileinfo = profileDaoImp.getProfileDetails(smartid);
		 
		grades.setInstitution(profileinfo.getInstitution());
		grades.setSchool(profileinfo.getSchool());
		
		Map<String, Object> respMap=new HashMap<>();
		flag=gradesService.addGrades(grades);
		if (flag) {
			respMap.put("status", 200);
        	respMap.put("message", "Saved Successfully");
		} else {
			respMap.put("status", 400);
        	respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
		}
		Loggers.loggerEnd();

		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}
	
/*Delete method*/
	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> deleteGrades(@RequestBody Grades grades, @PathVariable("task") String task,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(grades);
		boolean cb=false;
		Map<String, Object> respMap=new HashMap<>();
		
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if(getAuthorization.authorizationForPut(tokenNumber,task, httpSession)){
		    if(task.equals("edit")){
		    	System.out.println("in side edit method>>>>>>>>>>>>>>>>>");
		    	cb=gradesService.updateGrades(grades);
		    	if(cb){
		    		respMap.put("status", 200);
	        	respMap.put("message", "Updated Successfully");
		    	}else{
		    		respMap.put("status", 400);
	        	respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
		    	}
		    }
		    else if (task.equals("delete")){
		    	System.out.println("in side delete method##########################");
		    	gradesService.deleteGrades(grades);
		    	respMap.put("status", 200);
	        	respMap.put("message", "Deleted Successfully");
		    }
		    
		}
		else {
			 respMap.put("status", 403);
	        	respMap.put("message", "Permission Denied");;
		     }
			Loggers.loggerEnd();

			return new ResponseEntity<Map<String,Object>>(respMap, HttpStatus.OK);
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