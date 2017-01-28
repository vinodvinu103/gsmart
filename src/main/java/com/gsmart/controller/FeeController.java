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
import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.FeeServices;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.SearchService;
import com.gsmart.services.TokenService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.FEE)
public class FeeController {

	@Autowired
	FeeServices feeServices;
	

	@Autowired
	ProfileServices profileSevices;

	@Autowired
	SearchService searchService;

	@Autowired
	GetAuthorization getAuthorization;
	
	@Autowired
	TokenService tokenService;
	
	
	

	@RequestMapping(value = "/viewFee", method = RequestMethod.POST)
	public ResponseEntity<Map<String, ArrayList<Fee>>> getFeeList(@RequestBody Fee fee,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		
		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");

		str.length();
		Map<String, ArrayList<Fee>> jsonMap = new HashMap<String, ArrayList<Fee>>();

		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {

			ArrayList<Fee> feeList = (ArrayList<Fee>) feeServices.getFeeList(fee,tokenObj.getRole(),tokenObj.getHierarchy());

			if (feeList.size() != 0) {
				jsonMap.put("result", feeList);
				Loggers.loggerEnd();
				return new ResponseEntity<Map<String, ArrayList<Fee>>>(jsonMap, HttpStatus.OK);
			} else {
				jsonMap.put("result", null);
				Loggers.loggerEnd();
				return new ResponseEntity<Map<String, ArrayList<Fee>>>(jsonMap, HttpStatus.OK);
			}

		}
		return new ResponseEntity<Map<String, ArrayList<Fee>>>(jsonMap, HttpStatus.OK);

	}	

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addFee(@RequestBody Fee fee, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		
		
		
		
		
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		IAMResponse myResponse;
		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
			try {
				Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
				fee.setHierarchy(tokenObj.getHierarchy());
				feeServices.addFee(fee);
				myResponse = new IAMResponse("Success");
			} catch (Exception e) {
				throw new GSmartBaseException(e.getMessage());
			}
			Loggers.loggerEnd();
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		} else {
			myResponse = new IAMResponse("permission denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{smartId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> feeStructureController(@PathVariable("smartId") String smartId,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		
		Loggers.loggerStart();
		Loggers.loggerValue("token", token);
		Map<String, Profile> profileMap = new HashMap<String, Profile>();
		Map<String, Object> permissions = new HashMap<String, Object>();
		String tokenNumber = token.get("Authorization").get(0);
		Loggers.loggerValue("tokenNumber", tokenNumber);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		RolePermission modulePermissions = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");

		permissions.put("modulePermissions", modulePermissions);

		ArrayList<Profile> fees = new ArrayList<Profile>();

		ArrayList<Profile> self = new ArrayList<Profile>();

		if (modulePermissions != null) {
			Map<String, Profile> profiles = (Map<String, Profile>) searchService.getAllProfiles(tokenObj.getRole(),tokenObj.getHierarchy());
			
			
			ArrayList<Profile> childList = searchService.searchEmployeeInfo(smartId, profiles);
			Loggers.loggerValue("childlist", childList);
			

			fees = searchService.sumUpFee(childList, profiles);

			profileMap.put(smartId, profiles.get(smartId));

			Loggers.loggerStart("calling totalfees");

			self = searchService.totalfees(profileMap, fees);

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
			}

			permissions.put("selfProfile", self);
			permissions.put("childProfile", fees);

			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/totalfee", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> gettotalfee(@RequestHeader HttpHeaders token,HttpSession httpSession) throws GSmartBaseException {
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		RolePermission modulePermission=getAuthorization.authorizationForGet(tokenNumber, httpSession);
		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
		Loggers.loggerStart();
		Map<String, Object> responseMap = new HashMap<>();
		int fees;
		if(modulePermission!=null)
		{
		fees = feeServices.gettotalfee(tokenObj.getRole(),tokenObj.getHierarchy());
		responseMap.put("data", fees);
		}
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String,Object>>(responseMap, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/totalpaidfees", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> gettotalpaidfee(@RequestHeader HttpHeaders token,HttpSession httpSession) throws GSmartBaseException{
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		RolePermission modulePermission=getAuthorization.authorizationForGet(tokenNumber, httpSession);
		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
		str.length();
		Map<String, Object> responseMap = new HashMap<>();
		int paidfees;
		if(modulePermission!=null)
		{
		paidfees=feeServices.gettotalpaidfee(tokenObj.getRole(),tokenObj.getHierarchy());
		responseMap.put("data", paidfees);
		}
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String,Object>>(responseMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/paidfee", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getPaidStudentsList(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		List<Fee> PaidStudentsList = null;
		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
		Map<String, Object> permission = new HashMap<>();
		permission.put("modulePermission", modulePermission);
		if (modulePermission != null) {
			PaidStudentsList = feeServices.getPaidStudentsList(tokenObj.getRole(),tokenObj.getHierarchy());
			permission.put("PaidStudentsList", PaidStudentsList);

			return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);
		} else {
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);
		}

	}
	/*public ResponseEntity<Map<String, ArrayList<Fee>>> getPaidStudentsList(@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

	Loggers.loggerStart();
		Loggers.loggerValue("token", token);
		Map<String, Profile> profileMap = new HashMap<String, Profile>();
		Map<String, Object> permissions = new HashMap<String, Object>();
		String tokenNumber = token.get("Authorization").get(0);
		Loggers.loggerValue("tokenNumber", tokenNumber);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		

		
	
		Map<String, ArrayList<Fee>> jsonMap = new HashMap<String, ArrayList<Fee>>();

	RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);

	Map<String, Object> permission = new HashMap<>();
	permission.put("modulePermission", modulePermission);
	if (modulePermission != null) {
		PaidStudentsList = inventoryAssignmentsServices.getPaidStudentsList();
		permission.put("inventoryList", inventoryList);

		return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);
	} else {
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);
	}


			ArrayList<Fee> PaidStudentsList = (ArrayList<Fee>) feeServices.getPaidStudentsList();

			if (PaidStudentsList.size() != 0) {
				jsonMap.put("result", PaidStudentsList);
				Loggers.loggerEnd();
				return new ResponseEntity<Map<String, ArrayList<Fee>>>(jsonMap, HttpStatus.OK);
			} else {
				jsonMap.put("result", null);
				Loggers.loggerEnd();
				return new ResponseEntity<Map<String, ArrayList<Fee>>>(jsonMap, HttpStatus.OK);
			}

		}
	
	*/
	

		
	
		
	@RequestMapping(value = "/unpaidfee", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getUnPaidStudentsList(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

	Loggers.loggerStart();
	String tokenNumber = token.get("Authorization").get(0);
	String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
	str.length();

	List<Fee> unPaidStudentsList = null;
	RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
	Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
	Map<String, Object> permission = new HashMap<>();
	permission.put("modulePermission", modulePermission);
	if (modulePermission != null) {
		unPaidStudentsList = feeServices.getUnpaidStudentsList(tokenObj.getRole(),tokenObj.getHierarchy());
		permission.put("unPaidStudentsList", unPaidStudentsList);

		return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);
	} else {
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);
	}

    }
	
	@RequestMapping(value="/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editFee(@RequestBody Fee fee, @PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		
		IAMResponse myResponse;
		Loggers.loggerStart(fee);
		
		String tokenNumber=token.get("Authorization").get(0);
		
	    String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		
		str.length();
		
		if(getAuthorization.authorizationForPut(tokenNumber, task, httpSession))
		{
		
		if(task.equals("edit"))
			feeServices.editFee(fee);
		else if(task.equals("delete"))
			feeServices.deleteFee(fee);
		
		myResponse = new IAMResponse("success");
		Loggers.loggerEnd();
		
		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);

		}
		else
		{
			myResponse=new IAMResponse("Permissions denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
	
}
}


