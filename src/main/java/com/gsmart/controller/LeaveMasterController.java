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

import com.gsmart.model.CompoundLeaveMaster;
import com.gsmart.model.LeaveMaster;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.LeaveMasterService;
import com.gsmart.services.TokenService;
import com.gsmart.util.*;

@Controller
@RequestMapping(Constants.LEAVEMASTER)
public class LeaveMasterController {

	@Autowired
	LeaveMasterService leaveMasterService;

	@Autowired
	GetAuthorization getAuthorization;

	@Autowired
	TokenService tokenService;

	@RequestMapping(value="/{min}/{max}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getleavemaster(@PathVariable("min") Integer min, @PathVariable("max") Integer max, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		Map<String, Object> leaveMasterList = null;

		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		Token tokenObj = (Token) httpSession.getAttribute("hierarchy");
		System.out.println("hierarchy" + tokenObj.getHierarchy());
		Map<String, Object> permissions = new HashMap<>();

		permissions.put("modulePermission", modulePermission);

		/* if (modulePermission != null) { */
		leaveMasterList = leaveMasterService.getLeaveMasterList(tokenObj.getRole(), tokenObj.getHierarchy(), min, max);
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
		/*
		 * } else { return new ResponseEntity<Map<String, Object>>(leavemaster,
		 * HttpStatus.OK); }
		 */

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addLeaveMaster(@RequestBody LeaveMaster leaveMaster,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		Map<String, Object> respMap = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {

			Token tokenObj = (Token) httpSession.getAttribute("hierarchy");

			leaveMaster.setHierarchy(tokenObj.getHierarchy());
			CompoundLeaveMaster cb = leaveMasterService.addLeaveMaster(leaveMaster);

			if (cb != null) {
				respMap.put("status", 200);
				respMap.put("message", "Saved Successfully");
			}

			else {
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

		if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
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

		}

		else {
			respMap.put("status", 200);
			respMap.put("message", "Permission Denied");

		}

		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}
}
