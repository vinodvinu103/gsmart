package com.gsmart.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


import com.gsmart.model.CompoundPerformanceAppraisal;

import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.model.PerformanceRecord;
import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.PerformanceAppraisalService;
import com.gsmart.services.PerformanceRecordService;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.SearchService;
import com.gsmart.services.TokenService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;


@Controller
@RequestMapping(Constants.PERFORMANCE)
public class PerformanceController {

	@Autowired
	PerformanceAppraisalService appraisalservice;
	@Autowired
	GetAuthorization getauthorization;
	@Autowired
	TokenService tokenService;
	@Autowired
	PerformanceRecordService performancerecord;

	@Autowired
	SearchService searchService;

	@Autowired
	ProfileServices profileServices;

	@RequestMapping(value = "/{year}/{smartId}/", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> performance(@PathVariable("year") String year,
			@PathVariable("smartId") String smartId, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		Loggers.loggerStart(year);
		Loggers.loggerStart(smartId);

		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Map<String, Object> jsonMap = new HashMap<>();
		List<PerformanceAppraisal> appraisalList = null;
		Map<String, Object> performancerecordList = null;
		RolePermission modulePermission = getauthorization.authorizationForGet(tokenNumber, httpSession);
		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
		
		Map<String, Object> permissions = new HashMap<>();
		permissions.put("modulePermission", modulePermission);
		IAMResponse rsp = new IAMResponse();
		
		if (modulePermission != null) {

			appraisalList = appraisalservice.getAppraisalList(tokenObj.getReportingManagerId(), year,tokenObj.getRole(),tokenObj.getHierarchy());
			performancerecordList = performancerecord.getPerformanceRecord(smartId, year,tokenObj.getRole(),tokenObj.getHierarchy(),tokenObj.getReportingManagerId());
			jsonMap.put("appraisalList", appraisalList);
			jsonMap.put("performancerecord", performancerecordList);
			Loggers.loggerEnd(jsonMap);
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		}
	}
	@RequestMapping(value = "/om",method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addAppraisal(@RequestBody PerformanceAppraisal  performanceAppraisal,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

	    Loggers.loggerStart( performanceAppraisal);
		IAMResponse myResponse;
		

		IAMResponse resp = new IAMResponse();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		Token token1 = tokenService.getToken(tokenNumber);
		String smartId = token1.getSmartId();
		 performanceAppraisal.setReportingManagerID(smartId);
		
		if (getauthorization.authorizationForPost(tokenNumber, httpSession)) {
			Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
			performanceAppraisal.setHierarchy(tokenObj.getHierarchy());
			appraisalservice.addAppraisal( performanceAppraisal);
			resp.setMessage("success");
		return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
		} else {
			resp.setMessage("Permission Denied");
			return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
		}
	
	}
	

	@RequestMapping(value = "/om/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editPerformance(@RequestBody PerformanceAppraisal appraisal,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse myResponse;
		String tokenNumber = token.get("Authorization").get(0);

		String str = getauthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if (getauthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			if (task.equals("edit"))

				appraisalservice.editAppraisal(appraisal);

			else if (task.equals("delete"))

				appraisalservice.deleteAppraisal(appraisal);

			myResponse = new IAMResponse("success");
			Loggers.loggerEnd();
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		} else {
			myResponse = new IAMResponse("Permission Denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/record/{year}/{smartId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getReportingRecord(@PathVariable("year") String year,
			@PathVariable("smartId") String smartId, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Map<String, Object> permissions = new HashMap<>();
		Map<String, Object> jsonMap = new HashMap<>();
		List<Profile> teamrecordList = null;
		List<PerformanceAppraisal> teamappraisalList = null;
		RolePermission modulePermission = getauthorization.authorizationForGet(tokenNumber, httpSession);
		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
		permissions.put("modulePermission", modulePermission);
		IAMResponse rsp = new IAMResponse();

		if (modulePermission != null) {

			Profile profile = profileServices.getProfileDetails(smartId);
			Map<String, Profile> profiles = searchService.getAllProfiles("2017-2018",tokenObj.getHierarchy().getHid());

			ArrayList<Profile> childList = searchService.searchEmployeeInfo(smartId, profiles);
			teamappraisalList  = appraisalservice.getTeamAppraisalList(smartId, year,tokenObj.getRole(),tokenObj.getHierarchy());
			jsonMap.put("performancerecord", childList);
			jsonMap.put("teamAppraisalLiat", teamappraisalList);
			Loggers.loggerEnd(permissions);
			return new ResponseEntity<Map<String, Object>>(	jsonMap, HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addAppraisalRecord(@RequestBody PerformanceRecord appraisal,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(appraisal);

		Map<String, Object> resp=new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Token token1 = tokenService.getToken(tokenNumber);
		String smartId = token1.getSmartId();
	//	String reportingId = token1.getReportingManagerId();
		appraisal.setSmartId(smartId);
	//	appraisal.setReportingManagerID(reportingId);

		if (getauthorization.authorizationForPost(tokenNumber, httpSession)) {
			Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
			Loggers.loggerStart(appraisal);
			Loggers.loggerStart(smartId);
			appraisal.setHierarchy(tokenObj.getHierarchy());
			performancerecord.addAppraisalRecord(appraisal);

			resp.put("status", 200);
			resp.put("message", "success");
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String,Object>>(resp, HttpStatus.OK);
		} else {
			resp.put("status", 403);
			resp.put("message", "permission denied");
			return new ResponseEntity<Map<String,Object>>(resp, HttpStatus.OK);
		}

	}
	
/*
	@RequestMapping(value = "/record/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editPerformancerecord(@RequestBody PerformanceAppraisal appraisal,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse myResponse;
		String tokenNumber = token.get("Authorization").get(0);

		String str = getauthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if (getauthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			if (task.equals("edit"))

				performancerecord.editAppraisalrecord(appraisal);

			else if (task.equals("delete"))

				performancerecord.deleteAppraisalrecord(appraisal);

			myResponse = new IAMResponse("success");
			Loggers.loggerEnd();
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		} else {
			myResponse = new IAMResponse("Permission Denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
	}*/
	@RequestMapping(value = "/manager/{year}/{smartId}/{reportingManagerId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> performanceManager(@PathVariable("year") String year,
			@PathVariable("smartId") String smartId,@PathVariable("reportingManagerId") String reportingManagerId, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		Loggers.loggerStart(year);
		Loggers.loggerStart(smartId);

		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Map<String, Object> jsonMap = new HashMap<>();
		
		Map<String, Object> performancerecordList1 = null;
		RolePermission modulePermission = getauthorization.authorizationForGet(tokenNumber, httpSession);
		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
		
		Map<String, Object> permissions = new HashMap<>();
		permissions.put("modulePermission", modulePermission);
		IAMResponse rsp = new IAMResponse();
		
		if (modulePermission != null) {

			
			performancerecordList1 = performancerecord.getPerformanceRecordManager(reportingManagerId,smartId, year,tokenObj.getRole(),tokenObj.getHierarchy());
			
			jsonMap.put("performancerecord", performancerecordList1);
			Loggers.loggerEnd(jsonMap);
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/addManagerComment", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addAppraisalRecordManager(@RequestBody PerformanceRecord appraisal,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(appraisal);

		Map<String, Object> resp=new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
	/*	Token token1 = tokenService.getToken(tokenNumber);
		String smartId = token1.getSmartId();
	String reportingId = token1.getReportingManagerId();
		appraisal.setSmartId(smartId);
	appraisal.setReportingManagerID(reportingId);*/

		if (getauthorization.authorizationForPost(tokenNumber, httpSession)) {
			Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
			Loggers.loggerStart(appraisal);
			
			appraisal.setHierarchy(tokenObj.getHierarchy());
			performancerecord.addAppraisalRecordManager(appraisal);

			resp.put("status", 200);
			resp.put("message", "success");
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String,Object>>(resp, HttpStatus.OK);
		} else {
			resp.put("status", 403);
			resp.put("message", "permission denied");
			return new ResponseEntity<Map<String,Object>>(resp, HttpStatus.OK);
		}

	}
	
	

}