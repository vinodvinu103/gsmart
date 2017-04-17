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

import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.model.Token;
import com.gsmart.services.FeeServices;
import com.gsmart.services.HierarchyServices;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.SearchService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.FEE)
public class FeeController {

	@Autowired
	private FeeServices feeServices;

	@Autowired
	private ProfileServices profileSevices;

	@Autowired
	private SearchService searchService;

	@Autowired
	private GetAuthorization getAuthorization;


	@Autowired
	private HierarchyServices hierarchyServices;

	@RequestMapping(value = "/viewFee/{smartId}/{academicYear}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, ArrayList<Fee>>> getFeeList(@PathVariable("smartId") String smartId,@PathVariable("academicYear") String academicYear,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		Token tokenObj = (Token) httpSession.getAttribute("token");

		str.length();
		Map<String, ArrayList<Fee>> jsonMap = new HashMap<String, ArrayList<Fee>>();
		
		Fee fee =new Fee();
		fee.setAcademicYear(academicYear);
		fee.setSmartId(smartId);
		Map<String, Object> responseMap = new HashMap<>();

		

			ArrayList<Fee> feeList = (ArrayList<Fee>) feeServices.getFeeList(fee,tokenObj.getHierarchy().getHid());

			if (feeList.size() != 0) {
				jsonMap.put("result", feeList);
				responseMap.put("data", jsonMap);
				responseMap.put("status", 200);
				responseMap.put("message", "success");
				Loggers.loggerEnd();
			} else {
				jsonMap.put("result", null);
				Loggers.loggerEnd();
			}

		
		return new ResponseEntity<Map<String, ArrayList<Fee>>>(jsonMap, HttpStatus.OK);

	}
	@RequestMapping(value = "/studentunpaidfee/{smartId}/{academicYear}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, ArrayList<Fee>>> getStudentUnpaid(@PathVariable("smartId") String smartId,@PathVariable("academicYear") String academicYear,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		Token tokenObj = (Token) httpSession.getAttribute("token");

		str.length();
		Map<String, ArrayList<Fee>> jsonMap1 = new HashMap<String, ArrayList<Fee>>();
		
		Fee fee =new Fee();
		fee.setAcademicYear(academicYear);
		fee.setSmartId(smartId);
		Map<String, Object> responseMap = new HashMap<>();

		

			ArrayList<Fee> feeList = (ArrayList<Fee>) feeServices.getStudentUnpaidFeeList(fee,tokenObj.getHierarchy().getHid());

			if (feeList.size() != 0) {
				jsonMap1.put("result", feeList);
				responseMap.put("data", jsonMap1);
				responseMap.put("status", 200);
				responseMap.put("message", "success");
				Loggers.loggerEnd();
			} else {
				jsonMap1.put("result", null);
				Loggers.loggerEnd();
			}

		
		return new ResponseEntity<Map<String, ArrayList<Fee>>>(jsonMap1, HttpStatus.OK);

	}

	
	

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addFee(@RequestBody Fee fee, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		IAMResponse myResponse;
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> responseMap = new HashMap<>();
			
			try {
				Token tokenObj = (Token) httpSession.getAttribute("token");
				fee.setHierarchy(tokenObj.getHierarchy());
				feeServices.addFee(fee);
				myResponse = new IAMResponse("Success");
				response.put("message", myResponse);
				responseMap.put("data", response);
				responseMap.put("status", 200);
				responseMap.put("message", "success");
			} catch (Exception e) {
				throw new GSmartBaseException(e.getMessage());
			}
			Loggers.loggerEnd();
			responseMap.put("data", null);
			responseMap.put("status", 404);
			responseMap.put("message", "data not found");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/feeOrg/{smartId}/{academicYear}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> feeStructureController(@PathVariable("smartId") String smartId,
			@PathVariable("academicYear") String academicYear,@PathVariable("hierarchy") Long hierarchy, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart(smartId);
		Profile profile=null;
		Map<String, Profile> profileMap = new HashMap<String, Profile>();
		Map<String, Object> permissions = new HashMap<String, Object>();
		String tokenNumber = token.get("Authorization").get(0);
		Loggers.loggerValue("tokenNumber", tokenNumber);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Token tokenObj = (Token) httpSession.getAttribute("token");

		Long hid=null;
		

		ArrayList<Profile> fees = new ArrayList<Profile>();

		ArrayList<Profile> childs = new ArrayList<Profile>();

		ArrayList<Profile> self = new ArrayList<Profile>();
		Profile selfProfile = new Profile();
		
		if(tokenObj.getHierarchy()==null){
			hid=hierarchy;
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}


			Map<String, Profile> profiles = (Map<String, Profile>) searchService.getAllProfiles(academicYear,
					hid);

			ArrayList<Profile> childList = searchService.searchEmployeeInfo(smartId, profiles);

			fees = searchService.sumUpFee(childList, profiles, academicYear,hid);

			profileMap.put(smartId, profiles.get(smartId));
			if(tokenObj.getHierarchy()==null){
				 profile=profileSevices.getProfileDetails(smartId);
			}else{
				profile = profiles.get(smartId);
			}

			
			
			System.out.println("self profile" + profile);

			selfProfile = searchService.totalFessToAdmin(profile, fees);

			self.add(selfProfile);

			Set<String> key = profiles.keySet();
			for (int i = 0; i < fees.size(); i++) {

				for (String j : key) {

					Profile p = (Profile) profiles.get(j);
					if (p.getReportingManagerId().equals(fees.get(i).getSmartId())) {

						if (!(p.getSmartId().equals(fees.get(i).getSmartId()))) {
							fees.get(i).setChildFlag(true);

						}
					}
				}
				if (fees.get(i).getReportingManagerId().equals(smartId)) {
					childs.add(fees.get(i));

				}
			}

			permissions.put("selfProfile", self);
			permissions.put("childProfile", childs);

			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
	}

	@RequestMapping(value = "/totalfee", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> gettotalfee(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Token tokenObj = (Token) httpSession.getAttribute("token");
//		Long hid=null;
		/*if(tokenObj.getHierarchy()==null){
			hid=hierarchy;
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}*/
		Loggers.loggerStart();
		Map<String, Object> responseMap = new HashMap<>();
		int fees;
			fees = feeServices.gettotalfee(tokenObj.getRole(),tokenObj.getHierarchy());
			responseMap.put("data", fees);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/totalpaidfees", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> gettotalpaidfee(@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		Token tokenObj = (Token) httpSession.getAttribute("token");
		str.length();
		int totalPaidFees;
		int totalFees;
		List<Map<String, Object>> responseList = new ArrayList<>();
		Map<String, Object> responseMap = new HashMap<>();
		Map<String, Object> dataMap = new HashMap<>();
		if (tokenObj.getHierarchy() == null ) {
			List<Hierarchy> hierarchyList = hierarchyServices.getAllHierarchy();
			for (Hierarchy hierarchy : hierarchyList) {
				totalPaidFees = feeServices.gettotalpaidfee(tokenObj.getRole(), hierarchy);
				totalFees = feeServices.gettotalfee(tokenObj.getRole(), hierarchy);
				dataMap.put("totalPaidFees", totalPaidFees);
				dataMap.put("hierarchy", hierarchy);
				dataMap.put("totalFees", totalFees);
				responseList.add(dataMap);
			}
			responseMap.put("data", responseList);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		} else if (tokenObj.getHierarchy() != null) {
			totalPaidFees = feeServices.gettotalpaidfee(tokenObj.getRole(), tokenObj.getHierarchy());
			totalFees = feeServices.gettotalfee(tokenObj.getRole(), tokenObj.getHierarchy());
			dataMap.put("totalPaidFees", totalPaidFees);
			dataMap.put("hierarchy", tokenObj.getHierarchy());
			dataMap.put("totalFees", totalFees);
			responseList.add(dataMap);
			responseMap.put("data", responseList);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		} else {
			responseMap.put("data", null);
			responseMap.put("status", 404);
			responseMap.put("message", "Data not found");
		}

		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}


	@RequestMapping(value = "/paidfee/{min}/{max}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getPaidStudentsList(@PathVariable ("hierarchy") Long hierarchy,@PathVariable ("min") Integer min, @PathVariable ("max") Integer max, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart(min);

		Loggers.loggerStart(max);
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Map<String, Object> PaidStudentsList = null;
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Long hid=null;
		if(tokenObj.getHierarchy()==null){
			hid=hierarchy;
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}
		Map<String, Object> permission = new HashMap<>();
		Map<String, Object> responseMap = new HashMap<>();
			PaidStudentsList = feeServices.getPaidStudentsList(hid, min, max);
			if(PaidStudentsList!=null){
			permission.put("PaidStudentsList", PaidStudentsList);
			responseMap.put("data", permission);
			responseMap.put("status", 200);
			responseMap.put("message", "success");

		} else {
			responseMap.put("data", permission);
			responseMap.put("status", 404);
			responseMap.put("message", "data not found");
			Loggers.loggerEnd();
		
		}
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);

	}
	/*
	 * public ResponseEntity<Map<String, ArrayList<Fee>>>
	 * getPaidStudentsList(@RequestHeader HttpHeaders token, HttpSession
	 * httpSession) throws GSmartBaseException {
	 * 
	 * Loggers.loggerStart(); Loggers.loggerValue("token", token); Map<String,
	 * Profile> profileMap = new HashMap<String, Profile>(); Map<String, Object>
	 * permissions = new HashMap<String, Object>(); String tokenNumber =
	 * token.get("Authorization").get(0); Loggers.loggerValue("tokenNumber",
	 * tokenNumber); String str =
	 * getAuthorization.getAuthentication(tokenNumber, httpSession);
	 * str.length();
	 * 
	 * 
	 * 
	 * 
	 * 
	 * Map<String, ArrayList<Fee>> jsonMap = new HashMap<String,
	 * ArrayList<Fee>>();
	 * 
	 * RolePermission modulePermission =
	 * getAuthorization.authorizationForGet(tokenNumber, httpSession);
	 * 
	 * Map<String, Object> permission = new HashMap<>();
	 * permission.put("modulePermission", modulePermission); if
	 * (modulePermission != null) { PaidStudentsList =
	 * inventoryAssignmentsServices.getPaidStudentsList();
	 * permission.put("inventoryList", inventoryList);
	 * 
	 * return new ResponseEntity<Map<String, Object>>(permission,
	 * HttpStatus.OK); } else { Loggers.loggerEnd(); return new
	 * ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK); }
	 * 
	 * 
	 * ArrayList<Fee> PaidStudentsList = (ArrayList<Fee>)
	 * feeServices.getPaidStudentsList(); <<<<<<< HEAD
	 * 
	 * if (PaidStudentsList.size() != 0) { jsonMap.put("result",
	 * PaidStudentsList); Loggers.loggerEnd(); return new
	 * ResponseEntity<Map<String, ArrayList<Fee>>>(jsonMap, HttpStatus.OK); }
	 * else { jsonMap.put("result", null); Loggers.loggerEnd(); return new
	 * ResponseEntity<Map<String, ArrayList<Fee>>>(jsonMap, HttpStatus.OK); }
	 * 
	 * }
	 * 
	 */

	@RequestMapping(value = "/unpaidfee/{min}/{max}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getUnPaidStudentsList(@PathVariable ("hierarchy") Long hierarchy,@PathVariable ("min") Integer min, @PathVariable ("max") Integer max, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Map<String, Object> unPaidStudentsList = null;
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Long hid=null;
		if(tokenObj.getHierarchy()==null){
			hid=hierarchy;
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}
		Map<String, Object> responseMap = new HashMap<>();
		
			unPaidStudentsList = feeServices.getUnpaidStudentsList(hid, min, max);
			if (unPaidStudentsList != null) {
			responseMap.put("unpaidList", unPaidStudentsList);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
		
		} else {
			responseMap.put("status", 404);
			responseMap.put("message", "data not found");
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
		}
		

	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editFee(@RequestBody Fee fee, @PathVariable("task") String task,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		IAMResponse myResponse;
		Loggers.loggerStart(fee);
		Map<String, Object> responseMap = new HashMap<>();
		Map<String, Object> response = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

	if (task.equals("edit"))
				feeServices.editFee(fee);
			else if (task.equals("delete"))
				feeServices.deleteFee(fee);

			myResponse = new IAMResponse("success");
			response.put("message", myResponse);
			responseMap.put("data", response);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
			Loggers.loggerEnd();

			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);

	}
}
