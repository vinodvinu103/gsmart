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

import com.gsmart.dao.ProfileDao;
import com.gsmart.model.CompoundLeave;
import com.gsmart.model.Leave;
import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.LeaveServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.LEAVE)
public class LeaveController {
	@Autowired
	LeaveServices leaveServices;
	
	@Autowired
	GetAuthorization getAuthorization;
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	ProfileDao profileDao;
	
	
	@RequestMapping( method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getLeave(@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		
		String tokenNumber = token.get("Authorization").get(0);
		
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		List<Leave> leaveList = null;
		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);


		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");

		Map<String, Object> leave = new HashMap<>();
		leave.put("modulePermission", modulePermission);
		
		//	CronJob.cronJob();	
			
		if (modulePermission!= null) {
			leaveList = leaveServices.getLeaveList(tokenObj,tokenObj.getHierarchy());
			
			leave.put("leaveList", leaveList);
			Loggers.loggerEnd(leaveList);
			return new ResponseEntity<Map<String,Object>>(leave, HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String,Object>>(leave, HttpStatus.OK);
		}	

	}
		
		
	@RequestMapping( method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addLeave(@RequestBody Leave leave, Integer noOfdays, @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		
		IAMResponse resp=new IAMResponse();
		
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
	
			Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
			Profile profileInfo=profileDao.getProfileDetails(tokenObj.getSmartId());
			leave.setSmartId(profileInfo.getSmartId());
			leave.setReportingManagerId(profileInfo.getReportingManagerId());
			leave.setFullName(profileInfo.getFirstName()+" "+profileInfo.getLastName());
			leave.setHierarchy(tokenObj.getHierarchy());
			System.out.println("leave details>>>>>>>>>>>>>."+leave);
			CompoundLeave cl1=leaveServices.addLeave(leave,noOfdays,tokenObj.getSmartId(),tokenObj.getRole(),tokenObj.getHierarchy());
			if(cl1!=null)
			resp.setMessage("success");
		else
			resp.setMessage("Already exists");
		
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse> (resp, HttpStatus.OK);
		} else {
			resp.setMessage("Permission Denied");
			return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
		}
	}
	@RequestMapping(value = "/{task}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public  ResponseEntity<IAMResponse> editLeave(@RequestBody Leave leave, @PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse myResponse;
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		
		if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			if (task.equals("edit"))
				leaveServices.editLeave(leave);
			else if (task.equals("delete"))
				leaveServices.deleteLeave(leave);

			myResponse = new IAMResponse("success");
			Loggers.loggerEnd();
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		} else {
			myResponse = new IAMResponse("Permission Denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
	}
	@RequestMapping(value="/leftleaves/{smartId}/{leaveType}",method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getLeftLeaves(@PathVariable ("smartId") String smartId,@PathVariable("leaveType") String leaveType){
		Loggers.loggerStart();
		Map<String, Object>leftLeaves=leaveServices.getLeftLeaves(smartId, leaveType);
		
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String,Object>>(leftLeaves, HttpStatus.OK);
		
	}
	
	}
	
	
	
	