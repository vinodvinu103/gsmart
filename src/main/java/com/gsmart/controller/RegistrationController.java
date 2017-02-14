package com.gsmart.controller;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.gsmart.model.Login;

import com.gsmart.model.Fee;
import com.gsmart.model.FeeMaster;

import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Search;
import com.gsmart.model.Token;

import com.gsmart.services.PasswordServices;

import com.gsmart.services.AssignService;
import com.gsmart.services.FeeMasterServices;
import com.gsmart.services.FeeServices;
import com.gsmart.services.HierarchyServices;

//import com.gsmart.model.Search;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.SearchService;
import com.gsmart.services.TokenService;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.CommonMail;
//import com.gsmart.services.SearchService;
import com.gsmart.util.Constants;
import com.gsmart.util.Encrypt;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.REGISTRATION)

public class RegistrationController {

	@Autowired
	ProfileServices profileServices;

	@Autowired
	SearchService searchService;

	@Autowired
	GetAuthorization getAuthorization;

	@Autowired
	TokenService tokenService;

	@Autowired

	PasswordServices passwordServices;

	AssignService assignService;

	@Autowired
	HierarchyServices hierarchyServices;

	@Autowired
	FeeMasterServices feeMasterServices;

	@Autowired
	FeeServices feeService;

	@RequestMapping(value = "/employee/{min}/{max}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> viewEmployeeProfiles(@PathVariable("min") Integer min,
			@PathVariable("max") Integer max, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> jsonMap = new HashMap<>();

		Map<String, Object> profileList = null;
		RolePermission modulePermisson = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		Token tokenObj = (Token) httpSession.getAttribute("hierarchy");
		jsonMap.put("modulePermisson", modulePermisson);

		if (modulePermisson != null) {
			profileList = profileServices.getProfiles("employee", tokenObj.getSmartId(), tokenObj.getRole(),
					tokenObj.getHierarchy(), min, max);
			if (profileList != null) {
				jsonMap.put("status", 200);
				jsonMap.put("result", profileList);
				jsonMap.put("message", "success");
				jsonMap.put("modulePermisson", modulePermisson);
			} else {
				jsonMap.put("status", 400);
				jsonMap.put("message", "try again");

			}

			Loggers.loggerEnd(jsonMap);
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		} else {
			jsonMap.put("status", 403);
			jsonMap.put("message", "Permission Denied");
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/student/{min}/{max}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> viewStudentProfiles(@PathVariable("min") Integer min, @PathVariable("max") Integer max, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> jsonMap = new HashMap<>();

		RolePermission modulePermisson = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		jsonMap.put("modulePermisson", modulePermisson);

		Map<String, Object> profileMap = null;
		Token tokenObj = (Token) httpSession.getAttribute("hierarchy");
		if (modulePermisson != null) {
			profileMap = profileServices.getProfiles("student", tokenObj.getSmartId(), tokenObj.getRole(),
					tokenObj.getHierarchy(), min, max);
			if (profileMap != null) {
				jsonMap.put("status", 200);
				jsonMap.put("result", profileMap);
				jsonMap.put("message", "success");
				jsonMap.put("modulePermisson", modulePermisson);
			} else {
				jsonMap.put("status", 400);
				jsonMap.put("message", "try again");

			}
			Loggers.loggerEnd(jsonMap);
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		} else {
			jsonMap.put("status", 403);
			jsonMap.put("message", "Permission Denied");
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/addProfile", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addUser(@RequestBody Profile profile, Login login,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> jsonMap = new HashMap<>();
		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {

			Token tokenObj = (Token) httpSession.getAttribute("hierarchy");
			// String updSmartId = tokenObj.getSmartId();

			Loggers.loggerStart(profile.getFirstName());

			String smartId = String.valueOf((Integer.parseInt(profileServices.getmaxSamrtId()) + 1));
			if (profile.getRole().equalsIgnoreCase("student")) {
				profile.setHierarchy(tokenObj.getHierarchy());
				Loggers.loggerStart("institution by hierarchy is : " + tokenObj.getHierarchy().getInstitution());
				Loggers.loggerStart("school by hierarchy is : " + tokenObj.getHierarchy().getSchool());
				profile.setSchool(tokenObj.getHierarchy().getSchool());
				profile.setInstitution(tokenObj.getHierarchy().getInstitution());
				/*
				 * Hierarchy hierarchy =
				 * hierarchyServices.getHierarchyByHid(tokenObj.getHierarchy().
				 * getHid()); Assign assign =
				 * assignService.getStaffByClassAndSection(profile.getStandard()
				 * , profile.getSection(), hierarchy);
				 * profile.setReportingManagerId(assign.getTeacherSmartId());
				 * profile.setCounterSigningManagerId(assign.getHodSmartId());
				 */
			}

			profile.setSmartId(smartId);
			profile.setEntryTime(Calendar.getInstance().getTime().toString());

			if (profileServices.insertUserProfileDetails(profile)) {

				login.setReferenceSmartId(Encrypt.md5(smartId));
				login.setSmartId(smartId);
				passwordServices.setPassword(login, profile.getHierarchy());

				if (profile.getRole().equalsIgnoreCase("student")) {
					FeeMaster feeMaster = feeMasterServices.getFeeStructure(profile.getStandard(), tokenObj.getRole(),
							tokenObj.getHierarchy());
					Fee fee = new Fee();
					fee.setSmartId(profile.getSmartId());
					fee.setName(profile.getFirstName());
					fee.setParentName(profile.getFatherName());
					fee.setEntryTime(CalendarCalculator.getTimeStamp());
					fee.setDate(CalendarCalculator.getTimeStamp());
					fee.setFeeStatus("unpaid");
					fee.setHierarchy(profile.getHierarchy());
					fee.setTotalFee(feeMaster.getTotalFee());
					fee.setIdCardFee(feeMaster.getIdCardFee());
					fee.setMiscellaneousFee(feeMaster.getMiscellaneousFee());
					fee.setSportsFee(feeMaster.getSportsFee());
					fee.setStandard(feeMaster.getStandard());
					fee.setTuitionFee(feeMaster.getTuitionFee());
					fee.setTransportationFee(feeMaster.getTransportationFee());
					fee.setReportingManagerId(profile.getReportingManagerId());
					fee.setModeOfPayment("cash");
					fee.setAcademicYear(profile.getAcademicYear());
					fee.setPaidFee(0);
					fee.setBalanceFee(feeMaster.getTotalFee());
					feeService.addFee(fee);
				}
				CommonMail commonMail = new CommonMail();
				try {
					commonMail.passwordMail(profile, Encrypt.md5(smartId));
				} catch (Exception e) {
					e.printStackTrace();
				}
				jsonMap.put("status", 200);
				jsonMap.put("message", "success");
				jsonMap.put("Id", smartId);
			} else {
				jsonMap.put("status", 500);
				jsonMap.put("message", "EmailID is already Registered");
			}

		}

		Loggers.loggerEnd(jsonMap);
		return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/updateProfile/{updEmpSmartId}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> updateProfile(@RequestBody Profile profile,
			@PathVariable("updEmpSmartId") String updEmpSmartId, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart(profile);
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Loggers.loggerValue("Updated by: ", updEmpSmartId);
		Map<String, String> jsonResult = new HashMap<>();
		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
			Token tokenObj = (Token) httpSession.getAttribute("hierarchy");
			profile.setHierarchy(tokenObj.getHierarchy());
			String result = profileServices.updateProfile(profile);
			jsonResult.put("result", result);
			Loggers.loggerEnd(jsonResult);
		}
		return new ResponseEntity<Map<String, String>>(jsonResult, HttpStatus.OK);

	}

	@RequestMapping(value = "/searchRep", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchRep(@RequestBody Search search, @RequestHeader HttpHeaders token,
			HttpSession httpSession) {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
			Token tokenObj = (Token) httpSession.getAttribute("hierarchy");
			Loggers.loggerStart("search.getName() before : " + search.getName());
			Map<String, Profile> map = searchService.searchRep(search, tokenObj.getRole(), tokenObj.getHierarchy());
			Loggers.loggerStart("search.getName() after : " + search.getName());
			ArrayList<Profile> profiless = null;
			if (search.getName() != null && map != null) {
				Loggers.loggerStart("search.getName() is not null before : " + search.getName());
				profiless = searchService.getEmployeeInfo(search.getName(), map);
				Loggers.loggerStart("search.getName() is not null after : " + search.getName());
				if (profiless != null) {
					Loggers.loggerStart("profiless is not null");
					jsonMap.put("status", 200);
					jsonMap.put("result", profiless);
					jsonMap.put("message", "success");
				} else {
					Loggers.loggerStart("profiless is null");
					jsonMap.put("status", 400);
					jsonMap.put("message", "try again");

				}
			} else if (map != null) {
				Loggers.loggerStart("search.getName() is null before : " + search.getName());
				/*
				 * Set<String> keys = map.keySet();
				 * Loggers.loggerStart("keys are extracted"); for(String key :
				 * keys){ profiless.add(map.get(key)); }
				 */
				jsonMap.put("status", 200);
				jsonMap.put("message", "success");
				jsonMap.put("result", map.values());
				Loggers.loggerStart("search.getName() is null after : " + search.getName());
			} else {
				Loggers.loggerStart("map is null");
				Loggers.loggerStart("profiless is null");
				jsonMap.put("status", 400);
				jsonMap.put("message", "try again");
			}
			Loggers.loggerEnd();
		}
		return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);

	}
}