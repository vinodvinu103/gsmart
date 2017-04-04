package com.gsmart.controller;


import java.util.HashMap;

import java.util.ArrayList;

import java.util.List;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.gsmart.model.Token;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.Inventory;
import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;
import com.gsmart.services.AttendanceService;
import com.gsmart.services.FeeServices;
import com.gsmart.services.HierarchyServices;
import com.gsmart.services.InventoryAssignmentsServices;
import com.gsmart.services.InventoryServices;
import com.gsmart.services.SearchService;
import com.gsmart.services.TokenService;
import com.gsmart.util.Constants;

import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.Loggers;

@Controller

@RequestMapping(Constants.DASHBOARD)
public class DashboardController {
	
	@Autowired
	InventoryAssignmentsServices inventoryAssignmentServices;
	@Autowired
	InventoryServices inventoryServices;
	@Autowired
	GetAuthorization getAuthorization;
	@Autowired
	TokenService tokenService;
	@Autowired
	HierarchyServices hierarchyServices;
	@Autowired
	AttendanceService attendanceService;
	@Autowired
	SearchService searchService;
	@Autowired
	FeeServices feeServices;
	
	@RequestMapping(value = "/inventory/{academicYear}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getInventory1(@PathVariable("academicYear") String academicYear,@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		List<Inventory> inventoryList = new ArrayList<>();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		List<Map<String, Object>> inventoryByHierarchy = new ArrayList<>();
		Map<String, Object> finalResponse = new HashMap<>();
		Map<String, Object> responseMap = new HashMap<>();
			
			if(tokenObj.getHierarchy()==null){
				
				List<Hierarchy> hierarchyList = hierarchyServices.getAllHierarchy();
				Loggers.loggerStart(hierarchyList);
				for (Hierarchy hierarchy : hierarchyList) {
					Map<String, Object> dataMap = new HashMap<>();
					System.out.println("in side for >>>>>>>>>>>>>>>>>>>   >>>>>>>>>>>>>>>>>. "+hierarchy.getSchool());
					inventoryList = inventoryServices.getInventoryList(tokenObj.getRole(), hierarchy);
					dataMap.put("inventoryList", inventoryList);
					dataMap.put("hierarchy", hierarchy);
					inventoryByHierarchy.add(dataMap);
				}
				finalResponse.put("inventoryList", inventoryByHierarchy);
				finalResponse.put("modulePermissions", modulePermission);
				responseMap.put("data", finalResponse);
				responseMap.put("status", 200);
				responseMap.put("message", "success");
			}else{
				Map<String, Object> dataMap = new HashMap<>();
				System.out.println("in side else condition   <><<><><<><><><><  >>>");
				inventoryList = inventoryServices.getInventoryList(tokenObj.getRole(), tokenObj.getHierarchy());
				dataMap.put("inventoryList", inventoryList);
				dataMap.put("hierarchy", tokenObj.getHierarchy());
				inventoryByHierarchy.add(dataMap);
				finalResponse.put("inventoryList", inventoryByHierarchy);
				finalResponse.put("modulePermissions", modulePermission);
				responseMap.put("data", finalResponse);
				responseMap.put("status", 200);
				responseMap.put("message", "success");
			}
			Loggers.loggerEnd(inventoryByHierarchy);
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
		} 
	

	/*@RequestMapping(value = "/inventory1/{academicYear}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getInventory(@PathVariable("academicYear") String academicYear,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Map<String, Object> responseMap = new HashMap<>();
		List<Map<String, Object>> inventoryByHierarchy = new ArrayList<>();
		Map<String, Object> finalResponse = new HashMap<>();
		
		List<InventoryAssignments> inventoryAssignmentList = null;
		List<Inventory> inventoryList = null;
		if (tokenObj.getHierarchy() == null) {
			List<Hierarchy> hierarchyList = hierarchyServices.getAllHierarchy();
			for (Hierarchy hierarchy : hierarchyList) {
				Map<String, Object> dataMap = new HashMap<>();
				Map<String, Profile> allProfiles = searchService.getAllProfiles(academicYear,
						hierarchy.getHid());
				ArrayList<String> childsList = searchService.getAllChildSmartId(tokenObj.getSmartId(), allProfiles);
				childsList.add(tokenObj.getSmartId());
				inventoryAssignmentList = inventoryAssignmentServices.getInventoryDashboardData(childsList, hierarchy);
				inventoryList = inventoryServices.getInventoryList(tokenObj.getRole(), hierarchy);
				System.out.println("Inventory list::::::::::::::"+inventoryList);
				inventoryAssignmentList = inventoryAssignmentServices.groupCategoryAndItem(inventoryAssignmentList, inventoryList);
				dataMap.put("inventoryAssignmentList", inventoryAssignmentList);
				dataMap.put("hierarchy", hierarchy);
				inventoryByHierarchy.add(dataMap);
			}
			finalResponse.put("inventoryList", inventoryByHierarchy);
			responseMap.put("data", finalResponse);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		} else if (tokenObj.getHierarchy() != null) {
			Map<String, Object> dataMap = new HashMap<>();
			Map<String, Profile> allProfiles = searchService.getAllProfiles(academicYear, tokenObj.getHierarchy().getHid());
			ArrayList<String> childsList = searchService.getAllChildSmartId(tokenObj.getSmartId(), allProfiles);
			childsList.add(tokenObj.getSmartId());
			inventoryAssignmentList = inventoryAssignmentServices.getInventoryDashboardData(childsList, tokenObj.getHierarchy());
			inventoryList = inventoryServices.getInventoryList(tokenObj.getRole(), tokenObj.getHierarchy());
			inventoryAssignmentList = inventoryAssignmentServices.groupCategoryAndItem(inventoryAssignmentList, inventoryList);
			dataMap.put("inventoryAssignmentList", inventoryAssignmentList);
			dataMap.put("hierarchy", tokenObj.getHierarchy());
			inventoryByHierarchy.add(dataMap);
			finalResponse.put("inventoryList", inventoryByHierarchy);
			responseMap.put("data", finalResponse);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		}
		Loggers.loggerEnd(responseMap);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}*/

	@RequestMapping(value = "/attendance/{date}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAttendance(@PathVariable("date") Long date,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		Loggers.loggerStart("Given date is : " + date);
		Token tokenObj = (Token) httpSession.getAttribute("token");
		List<Hierarchy> hierarchyList = new ArrayList<>();
		Map<String, Object> responseMap = new HashMap<>();
		List<Map<String,  Object>> attendanceList=null;
		if (tokenObj.getHierarchy() == null) {
			System.out.println("in side if");
			hierarchyList = hierarchyServices.getAllHierarchy();
			System.out.println("hid for admiin <><><>   "+hierarchyList.size());
		} else {
			System.out.println("in side else");
			hierarchyList.add(tokenObj.getHierarchy());
		}
		attendanceList=attendanceService.getAttendanceByhierarchy(tokenObj.getSmartId(), date, hierarchyList);
		responseMap.put("message", "success");
		responseMap.put("status", 200);
		responseMap.put("data", attendanceList);
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fee/{academicYear}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> gettotalpaidfee(@PathVariable("academicYear") String academincYear, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		Token tokenObj = (Token) httpSession.getAttribute("token");
		str.length();

		int totalPaidFees=0;
		int totalFees=0;
		List<Map<String, Object>> responseList = new ArrayList<>();

		Map<String, Object> responseMap = new HashMap<>();
		
		if (tokenObj.getHierarchy() == null) {
			System.out.println("in side if condition for fees");
			List<Hierarchy> hierarchyList = hierarchyServices.getAllHierarchy();
			for (Hierarchy hierarchy : hierarchyList) {

				Map<String, Object> dataMap = new HashMap<>();

				Map<String, Profile> allProfiles = searchService.getAllProfiles(academincYear,
						hierarchy.getHid());

				List<String> childList = searchService.getAllChildSmartId(tokenObj.getSmartId(), allProfiles);
				childList.add(tokenObj.getSmartId());
				totalPaidFees = feeServices.getTotalFeeDashboard(academincYear, hierarchy.getHid(), childList);
				totalFees = feeServices.getPaidFeeDashboard(academincYear, hierarchy.getHid(), childList);
				dataMap.put("totalPaidFees", totalPaidFees);
				dataMap.put("hierarchy", hierarchy);
				dataMap.put("totalFees", totalFees);
				responseList.add(dataMap);
			}
			responseMap.put("data", responseList);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		} else if (tokenObj.getHierarchy() != null) {

			Map<String, Object> dataMap = new HashMap<>();

			Map<String, Profile> allProfiles = searchService.getAllProfiles(academincYear,
					tokenObj.getHierarchy().getHid());

			List<String> childList = searchService.getAllChildSmartId(tokenObj.getSmartId(), allProfiles);
			childList.add(tokenObj.getSmartId());
			totalPaidFees = feeServices.getTotalFeeDashboard(academincYear, tokenObj.getHierarchy().getHid(), childList);
			totalFees = feeServices.getPaidFeeDashboard(academincYear, tokenObj.getHierarchy().getHid(), childList);
			dataMap.put("totalPaidFees", totalPaidFees);
			dataMap.put("hierarchy", tokenObj.getHierarchy());
			dataMap.put("totalFees", totalFees);
			responseList.add(dataMap);
			responseMap.put("data", responseList);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		} else {
			System.out.println("in side else condition for fees");
			responseMap.put("data", null);
			responseMap.put("status", 404);
			responseMap.put("message", "Data not found");
		}
		System.out.println("The paid fees passed here is: "+totalPaidFees);
		System.out.println("The total fees passed here is"+totalFees);
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}

}
