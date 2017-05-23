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

import com.gsmart.dao.HierarchyDao;
import com.gsmart.dao.LeaveMasterDao;
import com.gsmart.model.CompoundLeaveMaster;
import com.gsmart.model.LeaveMaster;
import com.gsmart.model.Token;
import com.gsmart.services.LeaveMasterService;
import com.gsmart.util.*;

@Controller
@RequestMapping(Constants.LEAVEMASTER)
public class LeaveMasterController {

	@Autowired
	private LeaveMasterService leaveMasterService;

	@Autowired
	private GetAuthorization getAuthorization;

	
	@Autowired
	private HierarchyDao hierarchyDao;
	@Autowired
	private LeaveMasterDao leaveMasterDao;
	
	@RequestMapping(value="/search", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchLeaveMaster(@RequestBody LeaveMaster leavemaster, @RequestHeader HttpHeaders token, HttpSession httpSession)throws GSmartBaseException{
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Long hid = null;
		if(tokenObj.getHierarchy()==null){
			hid=leavemaster.getHierarchy().getHid();
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}
		List<LeaveMaster> searchlist = null;
		searchlist = leaveMasterService.searchLeaveMaster(leavemaster, hid);
		Map<String, Object> ch = new HashMap<>();
		ch.put("searchlist", searchlist);
		return new ResponseEntity<>(ch, HttpStatus.OK);
		
	}

	@RequestMapping(value="/{min}/{max}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getleavemaster(@PathVariable("min") Integer min, @PathVariable("hierarchy") Long hierarchy,@PathVariable("max") Integer max, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		Map<String, Object> leaveMasterList = null;

		Token tokenObj = (Token) httpSession.getAttribute("token");
		System.out.println("hierarchy" + tokenObj.getHierarchy());
		Map<String, Object> permissions = new HashMap<>();

		Long hid=null;
		if(tokenObj.getHierarchy()==null){
			hid=hierarchy;
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}

		leaveMasterList = leaveMasterService.getLeaveMasterList(hid, min, max);
		if (leaveMasterList != null) {
			permissions.put("status", 200);
			permissions.put("message", "success");
			permissions.put("leaveMasterList", leaveMasterList);

		} else {
			permissions.put("status", 404);
			permissions.put("message", "No Data Is Present");
		}
		permissions.put("leaveMasterList", leaveMasterList);
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);

	}

	@RequestMapping(value="/hierarchy/{hierarchy}",method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addLeaveMaster(@PathVariable("hierarchy") Long hierarchy,@RequestBody LeaveMaster leaveMaster,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		Map<String, Object> respMap = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();


			Token tokenObj = (Token) httpSession.getAttribute("token");
			if(tokenObj.getHierarchy()==null){
				leaveMaster.setHierarchy(hierarchyDao.getHierarchyByHid(hierarchy));
			}else{
				leaveMaster.setHierarchy(tokenObj.getHierarchy());
			}

			
			CompoundLeaveMaster cb = leaveMasterService.addLeaveMaster(leaveMaster);

			if (cb != null) {
				respMap.put("status", 200);
				respMap.put("message", "Saved Successfully");
			}

			else {
				respMap.put("status", 400);
				respMap.put("message", "Data Already Exist, Please try with SomeOther Data");

			}


		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);

	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> editLeaveMaster(@RequestBody LeaveMaster leaveMaster,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);

		LeaveMaster ch = null;
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		Map<String, Object> respMap = new HashMap<>();

			if (task.equals("edit")) {
				ch = leaveMasterService.editLeaveMaster(leaveMaster);
				if (ch != null) {
					respMap.put("status", 200);
					respMap.put("message", "Updated Succesfully");

				} else {
					respMap.put("status", 400);
					respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
				}
			} else if (task.equals("delete")){
				leaveMasterService.deleteLeaveMaster(leaveMaster);
			respMap.put("status", 200);
			respMap.put("message", "Deleted Succesfully");
			}

		


		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getleavemasterForApplyLeave(@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		List<LeaveMaster> leaveMasterList =null;

		Token tokenObj = (Token) httpSession.getAttribute("token");
		System.out.println("hierarchy" + tokenObj.getHierarchy());
		Map<String, Object> permissions = new HashMap<>();
		leaveMasterList =leaveMasterDao.getLeaveMasterListForApplyLeave(tokenObj.getHierarchy().getHid());
		if (leaveMasterList != null) {
			permissions.put("status", 200);
			permissions.put("message", "success");
			permissions.put("leaveMasterList", leaveMasterList);

		} else {
			permissions.put("status", 404);
			permissions.put("message", "No Data Is Present");
		}
		permissions.put("leaveMasterList", leaveMasterList);
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
	}
}
