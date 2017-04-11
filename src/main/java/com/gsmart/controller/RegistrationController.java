package com.gsmart.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
import com.gsmart.dao.HierarchyDao;
import com.gsmart.dao.ProfileDao;
import com.gsmart.model.Fee;
import com.gsmart.model.FeeMaster;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.model.Search;
import com.gsmart.model.Token;

import com.gsmart.services.PasswordServices;

import com.gsmart.services.FeeMasterServices;
import com.gsmart.services.FeeServices;

//import com.gsmart.model.Search;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.SearchService;
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
	private ProfileServices profileServices;

	@Autowired
	private SearchService searchService;

	@Autowired
	private GetAuthorization getAuthorization;


	@Autowired
	private PasswordServices passwordServices;


	@Autowired
	private FeeMasterServices feeMasterServices;

	@Autowired
	private FeeServices feeService;
	@Autowired
	private HierarchyDao hierarchyDao;
	@Autowired
	private ProfileDao profileDao;

	int i = 0;

	@RequestMapping(value = "/employee/{min}/{max}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> viewEmployeeProfiles(@PathVariable("min") Integer min,
			@PathVariable("max") Integer max, @PathVariable("hierarchy") Long hierarchy,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> jsonMap = new HashMap<>();

		Map<String, Object> profileList = null;
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Long hid = null;
		if (tokenObj.getHierarchy() == null) {
			hid = hierarchy;

		} else {
			hid = tokenObj.getHierarchy().getHid();
		}

			profileList = profileServices.getProfiles("employee", tokenObj.getSmartId(), hid, min, max);
			if (profileList != null) {
				jsonMap.put("status", 200);
				jsonMap.put("result", profileList);
				jsonMap.put("message", "success");
			} else {
				jsonMap.put("status", 400);
				jsonMap.put("message", "try again");

			}

			Loggers.loggerEnd(jsonMap);
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);

	}

	@RequestMapping(value = "/student/{min}/{max}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> viewStudentProfiles(@PathVariable("min") Integer min,
			@PathVariable("max") Integer max, @RequestHeader HttpHeaders token,
			@PathVariable("hierarchy") Long hierarchy, HttpSession httpSession) throws GSmartBaseException {

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> jsonMap = new HashMap<>();


		Map<String, Object> profileMap1 = null;
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Long hid = null;
		if (tokenObj.getHierarchy() == null) {
			hid = hierarchy;

		} else {
			hid = tokenObj.getHierarchy().getHid();
		}

			profileMap1 = profileServices.getProfiles("student", tokenObj.getSmartId(), hid, min, max);
			if (profileMap1 != null) {
				jsonMap.put("status", 200);
				jsonMap.put("result", profileMap1);
				jsonMap.put("message", "success");
			} else {
				jsonMap.put("status", 400);
				jsonMap.put("message", "try again");

			}
			Loggers.loggerEnd(jsonMap);
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/addProfile/{hierarchy}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addUser(@RequestBody Profile profile,
			@PathVariable("hierarchy") Long hierarchy, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> jsonMap = new HashMap<>();

			Token tokenObj = (Token) httpSession.getAttribute("token");
			// String updSmartId = tokenObj.getSmartId();

			Loggers.loggerStart(profile.getFirstName());
			System.out.println("hid"+hierarchy);

			String smartId = String.valueOf((Integer.parseInt(profileServices.getmaxSamrtId()) + 1));
			if (profile.getRole().equalsIgnoreCase("student")) {
				if (tokenObj.getHierarchy() == null) {
					Hierarchy hierarchy2=hierarchyDao.getHierarchyByHid(hierarchy);
					profile.setHierarchy(hierarchy2);
					profile.setSchool(hierarchy2.getSchool());
					profile.setInstitution(hierarchy2.getInstitution());

				} else {
					profile.setHierarchy(tokenObj.getHierarchy());
					profile.setSchool(tokenObj.getHierarchy().getSchool());
					profile.setInstitution(tokenObj.getHierarchy().getInstitution());

				}

				 
			}

			profile.setSmartId(smartId);
			profile.setEntryTime(Calendar.getInstance().getTime().toString());

			if (profileServices.insertUserProfileDetails(profile)) {

				Login login = new Login();
				login.setReferenceSmartId(Encrypt.md5(smartId));
				login.setSmartId(smartId);
				passwordServices.setPassword(login, profile.getHierarchy());

				if (profile.getRole().equalsIgnoreCase("student")) {
					FeeMaster feeMaster = feeMasterServices.getFeeStructure(profile.getStandard(), profile.getHierarchy().getHid());
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
				if (!sendMail(profile)) {
					if (profileDao.deleteProfileIfMailFailed(profile.getSmartId())) {
						jsonMap.put("status", 404);
						jsonMap.put("message", "please try again.. server is busy");
					} else {
						jsonMap.put("status", 500);
						jsonMap.put("message", "profile not recorded ..please contact admin");
					}

				} else {
					jsonMap.put("status", 200);
					jsonMap.put("message", "success");
					jsonMap.put("Id", smartId);

				}

			} else {
				jsonMap.put("status", 500);
				jsonMap.put("message", "EmailID is already Registered");
			}

		

		Loggers.loggerEnd(jsonMap);
		return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
	}

	private boolean sendMail(Profile profile) {
		CommonMail commonMail = new CommonMail();
		try {
			i++;
			commonMail.passwordMail(profile, profile.getSmartId());
			return true;
		} catch (Exception e) {
			if (i < 3) {
				sendMail(profile);
			}
			e.printStackTrace();
			return false;

		}

	}

	@RequestMapping(value = "/updateProfile/{updEmpSmartId}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody Profile profile,
			@PathVariable("updEmpSmartId") String updEmpSmartId, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart(profile);
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Loggers.loggerValue("Updated by: ", updEmpSmartId);
		Map<String, Object> jsonResult = new HashMap<>();
			Token tokenObj = (Token) httpSession.getAttribute("token");
			profile.setHierarchy(tokenObj.getHierarchy());
			String result = profileServices.updateProfile(profile);
			jsonResult.put("status", 200);
			jsonResult.put("result", result);
		Loggers.loggerEnd(jsonResult);
		return new ResponseEntity<Map<String, Object>>(jsonResult, HttpStatus.OK);

	}
	
	@RequestMapping(value="/changeprofileimage",method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> changeprofileimage(@RequestBody Profile profile,@RequestHeader HttpHeaders token, HttpSession httpSession)
	throws GSmartBaseException{
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String,Object> jsonResult = new HashMap<String, Object>();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		profile.setSmartId(tokenObj.getSmartId());
		String result = profileServices.changeprofileimage(profile);
		jsonResult.put("status", 200);
		jsonResult.put("result", result);
		Loggers.loggerEnd(jsonResult);
		return new ResponseEntity<Map<String, Object>>(jsonResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete/{task}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> deleteProfile(@RequestBody Profile profile,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		Map<String, Object> jsonResult = new HashMap<String, Object>();
			if (task.equals("delete")) {
				String result = profileServices.deleteprofile(profile);
				jsonResult.put("result", result);
				Loggers.loggerEnd(jsonResult);

			}

		
		return new ResponseEntity<Map<String, Object>>(jsonResult, HttpStatus.OK);
	}
    
	
	@RequestMapping(value = "/searchRep", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchRep(@RequestBody Search search, @RequestHeader HttpHeaders token,
			HttpSession httpSession) {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
			Token tokenObj = (Token) httpSession.getAttribute("token");
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
		
		return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);

	}

}
