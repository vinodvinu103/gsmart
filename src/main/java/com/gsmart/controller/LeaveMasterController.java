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
		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");

		Map<String, Object> leavemaster = new HashMap<>();

		leavemaster.put("modulePermission", modulePermission);

		if (modulePermission != null) {
			leaveMasterList = leaveMasterService.getLeaveMasterList(tokenObj.getRole(),tokenObj.getHierarchy(), min, max);

			leavemaster.put("leaveMasterList", leaveMasterList);
			Loggers.loggerEnd(leaveMasterList);
			return new ResponseEntity<Map<String, Object>>(leavemaster, HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(leavemaster, HttpStatus.OK);
		}

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addLeaveMaster(@RequestBody LeaveMaster leaveMaster,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse myResponse = new IAMResponse();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
			
			Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
			
			leaveMaster.setHierarchy(tokenObj.getHierarchy());
			CompoundLeaveMaster cb = leaveMasterService.addLeaveMaster(leaveMaster);

			if (cb != null)
				myResponse.setMessage("success");
			else
				myResponse.setMessage("Already exists");
			Loggers.loggerEnd();
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		} else {
			myResponse.setMessage("Permission Denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editLeaveMaster(@RequestBody LeaveMaster leaveMaster,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse myResponse;
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			if (task.equals("edit"))
				leaveMasterService.editLeaveMaster(leaveMaster);
			else if (task.equals("delete"))
				leaveMasterService.deleteLeaveMaster(leaveMaster);

			myResponse = new IAMResponse("success");
			Loggers.loggerEnd();
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		} else {
			myResponse = new IAMResponse("Permission Denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
	}
}
