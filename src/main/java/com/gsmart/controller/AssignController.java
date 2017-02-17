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

import com.gsmart.model.Assign;
import com.gsmart.model.CompoundAssign;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.AssignService;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.ASSIGN)
public class AssignController {

	@Autowired
	GetAuthorization getAuthorization;

	@Autowired
	AssignService assignService;

	@Autowired
	TokenService tokenServices;

	@Autowired
	ProfileServices profileServices;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAssigningReportee(@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		Map<String, Object> permissions = new HashMap<>();

		List<Assign> assignList = null;

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
	
		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		
		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");

		permissions.put("modulePermission", modulePermission);

		if (modulePermission != null) {

			assignList = assignService.getAssignReportee(tokenObj.getRole(), tokenObj.getHierarchy());

			permissions.put("assignList", assignList);
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		}

		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addAssigningReportee(@RequestBody Assign assign,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		Map<String, Object> response = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
			Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
			assign.setHierarchy(tokenObj.getHierarchy());
			
			CompoundAssign compoundAssign = assignService.addAssigningReportee(assign);
			if (compoundAssign != null) {
				response.put("message", "success");
			} else {
				response.put("message", "Data Already Exists..");

			}
		} else {
			response.put("message", "Permission Denied");

		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editDeleteBand(@RequestBody Assign assign, @PathVariable("task") String task,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse myResponse = null;
		Assign asn = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			if (task.equals("edit")) {
				asn = assignService.editAssigningReportee(assign);
				if (asn != null) {
					myResponse = new IAMResponse("success");
				} else {
					myResponse = new IAMResponse("Data Already exist");
				}
			} else if (task.equals("delete")) {
				assignService.deleteAssigningReportee(assign);
				myResponse = new IAMResponse("success");
			}

			
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
		else {
			myResponse = new IAMResponse("Permission Denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/staff", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getStaffs(@RequestBody Hierarchy hierarchy,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		IAMResponse rsp = null;
		Map<String, Object> response = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();		

		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
			response.put("staffList", profileServices.getProfileByHierarchy(hierarchy));
		} else {
			rsp = new IAMResponse("Permission Denied");
			response.put("message", rsp);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	@RequestMapping(value = "/om/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editTeacher(@RequestBody Assign assign ,Hierarchy hierarchy, @PathVariable("task") String task
			,@RequestHeader HttpHeaders token, HttpSession httpSession	) throws GSmartBaseException {
		Loggers.loggerStart();
	//	Loggers.loggerValue("profilevalue", profile);
		IAMResponse myResponse = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
		if (task.equals("edit")) {
				assignService.editAssigningTeacher(assign,hierarchy);

			} else if (task.equals("delete")) {
				assignService.deleteAssigningReportee(assign);

			}

			myResponse = new IAMResponse("success");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}

		else {
			myResponse = new IAMResponse("Permission Denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}

	}
}
