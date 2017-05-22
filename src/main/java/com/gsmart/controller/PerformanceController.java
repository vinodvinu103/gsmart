package com.gsmart.controller;


import java.util.ArrayList;
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

import com.gsmart.dao.HierarchyDao;
import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.model.PerformanceRecord;
import com.gsmart.model.Profile;
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
	private PerformanceAppraisalService appraisalservice;
	@Autowired
	private GetAuthorization getauthorization;
	@Autowired
	TokenService tokenService;
	@Autowired
	PerformanceRecordService performancerecord;

	@Autowired
	SearchService searchService;
	@Autowired
	private HierarchyDao hierarchyDao;

	@Autowired
	ProfileServices profileServices;

	@RequestMapping(value = "/{year}/{smartId}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> performance(@PathVariable("year") String year,
			@PathVariable("smartId") String smartId, @PathVariable("hierarchy") Long hierarchy, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart(hierarchy);
		Loggers.loggerStart(year);
		Loggers.loggerStart(smartId);
		

		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Map<String, Object> jsonMap = new HashMap<>();
		List<PerformanceAppraisal> appraisalList = null;
		Map<String, Object> performancerecordList = null;
		Token tokenObj=(Token) httpSession.getAttribute("token");
		
		Long hid=null;
		if(tokenObj.getHierarchy()==null){
			hid=hierarchy;
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}


			appraisalList = appraisalservice.getAppraisalList(tokenObj.getReportingManagerId(), year,hid);
			performancerecordList = performancerecord.getPerformanceRecord(smartId, year,hid,tokenObj.getReportingManagerId());
			jsonMap.put("appraisalList", appraisalList);
			jsonMap.put("performancerecord", performancerecordList);
			Loggers.loggerEnd(jsonMap);
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		
	}
	@RequestMapping(value = "/om",method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addAppraisal(@RequestBody PerformanceAppraisal  performanceAppraisal,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

	    Loggers.loggerStart( performanceAppraisal);
		

		IAMResponse resp = new IAMResponse();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
	
		
	
			Token tokenObj=(Token) httpSession.getAttribute("token");
			String smartId = tokenObj.getSmartId();
			 performanceAppraisal.setReportingManagerID(smartId);
			performanceAppraisal.setHierarchy(tokenObj.getHierarchy());
			appraisalservice.addAppraisal( performanceAppraisal);
			resp.setMessage("success");
		return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
		
	
	}
	

	@RequestMapping(value = "/omkar/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editPerformance(@RequestBody PerformanceAppraisal appraisal,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse myResponse;
		String tokenNumber = token.get("Authorization").get(0);

		String str = getauthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		
			if (task.equals("edit"))

				appraisalservice.editAppraisal(appraisal);

			else if (task.equals("delete"))

				appraisalservice.deleteAppraisal(appraisal);

			myResponse = new IAMResponse("success");
			Loggers.loggerEnd();
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		
	}

	@RequestMapping(value = "/record/{year}/{smartId}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getReportingRecord(@PathVariable("year") String year,
			@PathVariable("smartId") String smartId,@PathVariable("hierarchy") Long hierarchy, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Map<String, Object> permissions = new HashMap<>();
		Map<String, Object> jsonMap = new HashMap<>();
		List<PerformanceAppraisal> teamappraisalList = null;
		Token tokenObj=(Token) httpSession.getAttribute("token");
		Long hid=null;
		if(tokenObj.getHierarchy()==null){
			hid=hierarchy;
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}


			Profile profile = profileServices.getProfileDetails(smartId);
			Map<String, Profile> profiles = searchService.getAllProfiles("2017-2018",hid);

			ArrayList<Profile> childList = searchService.searchEmployeeInfo(smartId, profiles);
			teamappraisalList  = appraisalservice.getTeamAppraisalList(smartId, year, hid);
			jsonMap.put("performancerecord", childList);
			jsonMap.put("teamAppraisalLiat", teamappraisalList);
			Loggers.loggerEnd(permissions);
			return new ResponseEntity<Map<String, Object>>(	jsonMap, HttpStatus.OK);
		
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addAppraisalRecord(@RequestBody PerformanceRecord appraisal,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(appraisal);

		Map<String, Object> resp=new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		

			Token tokenObj=(Token) httpSession.getAttribute("token");
			String smartId = tokenObj.getSmartId();
			
			//	String reportingId = token1.getReportingManagerId();
				appraisal.setSmartId(smartId);
			//	appraisal.setReportingManagerID(reportingId);
			Loggers.loggerStart(appraisal);
			Loggers.loggerStart(smartId);
			appraisal.setHierarchy(tokenObj.getHierarchy());
			performancerecord.addAppraisalRecord(appraisal);

			resp.put("status", 200);
			resp.put("message", "success");
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String,Object>>(resp, HttpStatus.OK);
		

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
	@RequestMapping(value = "/manager/{year}/{smartId}/{reportingManagerId}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> performanceManager(@PathVariable("year") String year,
			@PathVariable("smartId") String smartId,@PathVariable("reportingManagerId") String reportingManagerId,@PathVariable("hierarchy") Long hierarchy, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		Loggers.loggerStart(year);
		Loggers.loggerStart(smartId);

		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
	Map<String, Object> jsonMap = new HashMap<>();
		
		Map<String, Object> performancerecordList1 = null;
		Token tokenObj=(Token) httpSession.getAttribute("token");
		
		Long hid=null;
		if(tokenObj.getHierarchy()==null){
			hid=hierarchy;
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}
		

			
			performancerecordList1 = performancerecord.getPerformanceRecordManager(reportingManagerId,smartId, year,hid);
			
			jsonMap.put("performancerecord", performancerecordList1);
			Loggers.loggerEnd(jsonMap);
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/addManagerComment/hierarchy/{hierarchy}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addAppraisalRecordManager(@PathVariable("hierarchy") Long hierarchy,@RequestBody PerformanceRecord appraisal,
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

		
		
		
			Token tokenObj=(Token) httpSession.getAttribute("token");
			Loggers.loggerStart(appraisal);
			Long hid=null;
			if(tokenObj.getHierarchy()==null){
				appraisal.setHierarchy(hierarchyDao.getHierarchyByHid(hierarchy));
			}else{
				appraisal.setHierarchy(tokenObj.getHierarchy());
			}
			
			
			performancerecord.addAppraisalRecordManager(appraisal);

			resp.put("status", 200);
			resp.put("message", "success");
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String,Object>>(resp, HttpStatus.OK);
		

	}
	
	
	
	@RequestMapping(value = "/rating/{year}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> ratingOfEmployee(@PathVariable("year") String year,
			 @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
	
		Loggers.loggerStart(year);
	

		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Map<String, Object> jsonMap = new HashMap<>();
		
		Map<String, Object> performancerecordratingList = null;
		Token tokenObj=(Token) httpSession.getAttribute("token");
		
	
		
		Long hid=null;
		hid=tokenObj.getHierarchy().getHid();
		Loggers.loggerStart(hid);
		
		

			
		performancerecordratingList = performancerecord.getrating(year,hid);
			
			jsonMap.put("performancerecord", performancerecordratingList);
			Loggers.loggerEnd(jsonMap);
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		
	}

}
