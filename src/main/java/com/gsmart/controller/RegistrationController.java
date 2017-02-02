package com.gsmart.controller;

import java.util.ArrayList;
import java.util.Calendar;
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

	@RequestMapping(value = "/employee", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> viewEmployeeProfiles(@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> jsonMap = new HashMap<>();

		List<Profile> profileList=null;
		RolePermission modulePermisson = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
		jsonMap.put("modulePermisson", modulePermisson);


		if (modulePermisson != null) {
		profileList= profileServices.getProfiles("employee", tokenObj.getSmartId(),tokenObj.getRole(),tokenObj.getHierarchy());
			if(profileList!=null)
			{
				jsonMap.put("status", 200);
				jsonMap.put("result", profileList);
				jsonMap.put("message", "success");
				jsonMap.put("modulePermisson", modulePermisson);
			}else{
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

	@RequestMapping(value = "/student", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> viewStudentProfiles(@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> jsonMap = new HashMap<>();

		RolePermission modulePermisson = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		jsonMap.put("modulePermisson", modulePermisson);

		List<Profile> profileList=null;
		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
		if (modulePermisson != null) {
			profileList= profileServices.getProfiles("student", tokenObj.getSmartId(),tokenObj.getRole(),tokenObj.getHierarchy());
			if(profileList!=null)
			{
				jsonMap.put("status", 200);
				jsonMap.put("result", profileList);
				jsonMap.put("message", "success");
				jsonMap.put("modulePermisson", modulePermisson);
			}else{
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
	public ResponseEntity<Map<String, Object>> addUser(@RequestBody Profile profile, Login login, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		String tokenNumber = token.get("Authorization").get(0);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> jsonMap = new HashMap<>();
		if(getAuthorization.authorizationForPost(tokenNumber, httpSession))
		{

		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
		//String updSmartId = tokenObj.getSmartId();


		Loggers.loggerStart(profile.getFirstName());

		

		String smartId = String.valueOf((Integer.parseInt(profileServices.getmaxSamrtId()) + 1));
		if (profile.getRole().equalsIgnoreCase("student")) {
			profile.setHierarchy(tokenObj.getHierarchy());
			/*
			 * Hierarchy hierarchy =
			 * hierarchyServices.getHierarchyByHid(tokenObj.getHierarchy().
			 * getHid()); Assign assign =
			 * assignService.getStaffByClassAndSection(profile.getStandard(),
			 * profile.getSection(), hierarchy);
			 * profile.setReportingManagerId(assign.getTeacherSmartId());
			 * profile.setCounterSigningManagerId(assign.getHodSmartId());
			 */
		}
		
		profile.setSmartId(smartId);
		profile.setEntryTime(Calendar.getInstance().getTime().toString());

		if (profileServices.insertUserProfileDetails(profile)) {

			login.setReferenceSmartId(Encrypt.md5(smartId));
			login.setSmartId(smartId);
			passwordServices.setPassword(login);
			
			if (profile.getRole().equalsIgnoreCase("student")) {
				FeeMaster feeMaster = feeMasterServices.getFeeStructure(profile.getStandard(),tokenObj.getRole(),tokenObj.getHierarchy());
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
		}else{
				jsonMap.put("status", 500);
				jsonMap.put("message", "EmailID is already Registered");
			}

			
		

		
		}

		Loggers.loggerEnd(jsonMap);
		return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
	}


	@RequestMapping(value = "/updateProfile/{updEmpSmartId}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> updateProfile(@RequestBody Profile profile,
			@PathVariable("updEmpSmartId") String updEmpSmartId,@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart(profile);
		String tokenNumber = token.get("Authorization").get(0);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Loggers.loggerValue("Updated by: ", updEmpSmartId);
		Map<String, String> jsonResult = new HashMap<>();
		if(getAuthorization.authorizationForPost(tokenNumber, httpSession))
		{
			Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
			profile.setHierarchy(tokenObj.getHierarchy());
		String result = profileServices.updateProfile(profile);
		jsonResult.put("result", result);
		Loggers.loggerEnd(jsonResult);
		}
		return new ResponseEntity<Map<String, String>>(jsonResult, HttpStatus.OK);

	}

	@RequestMapping(value = "/searchRep", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchRep(@RequestBody Search search,@RequestHeader HttpHeaders token,
			HttpSession httpSession) {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if(getAuthorization.authorizationForPost(tokenNumber, httpSession)){
			Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
			
		Map<String, Profile> map = searchService.searchRep(search,tokenObj.getRole(),tokenObj.getHierarchy());
		
		ArrayList<Profile> profiless = searchService.getEmployeeInfo(search.getName(), map);
		
		if(profiless!=null)
		{
			jsonMap.put("status", 200);
			jsonMap.put("result", profiless);
			jsonMap.put("message", "success");
		}else{
			jsonMap.put("status", 400);
			jsonMap.put("message", "try again");
			
		}
		Loggers.loggerEnd();
		}
		return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);

	}
}