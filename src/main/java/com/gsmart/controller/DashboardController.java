package com.gsmart.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.Inventory;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.AttendanceService;
import com.gsmart.services.HierarchyServices;
import com.gsmart.services.InventoryServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.DASHBOARD)
public class DashboardController {
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

	@RequestMapping(value = "/inventory", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getInventory(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		Token tokenObj = (Token) httpSession.getAttribute("hierarchy");
		str.length();
		List<Map<String, Object>> inventoryByHierarchy = new ArrayList<>();
		Map<String, Object> finalResponse = new HashMap<>();
		Map<String, Object> responseMap = new HashMap<>();
		Map<String, Object> dataMap = new HashMap<>();
		List<Inventory> inventoryList = null;
		// RolePermission modulePermission =
		// getAuthorization.authorizationForGet(tokenNumber, httpSession);
		if (tokenObj.getHierarchy() == null) {
			System.out.println("hierarchy is null");
			List<Hierarchy> hierarchyList = hierarchyServices.getAllHierarchy();
			for (Hierarchy hierarchy : hierarchyList) {
				inventoryList = inventoryServices.getInventoryList(tokenObj.getRole(), hierarchy);

				dataMap.put("inventoryList", inventoryList);
				dataMap.put("hierarchy", hierarchy);
				// dataMap.put("totalFees", totalFees);
				inventoryByHierarchy.add(dataMap);

			}
			finalResponse.put("inventoryList", inventoryByHierarchy);
			finalResponse.put("modulePermissions", modulePermission);
			responseMap.put("data", finalResponse);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		} else if (tokenObj.getHierarchy() != null) {
			inventoryList = inventoryServices.getInventoryList(tokenObj.getRole(), tokenObj.getHierarchy());

			dataMap.put("inventoryList", inventoryList);
			dataMap.put("hierarchy", tokenObj.getHierarchy());

			inventoryByHierarchy.add(dataMap);
			finalResponse.put("inventoryList", inventoryByHierarchy);
			finalResponse.put("modulePermissions", modulePermission);
			responseMap.put("data", finalResponse);
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

	@RequestMapping(value = "attendance/{date}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAttendance(@PathVariable("date") Long date,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		Loggers.loggerStart("Given date is : " + date);
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		Token tokenObj = (Token) httpSession.getAttribute("hierarchy");
		List<Hierarchy> hierarchyList = new ArrayList<>();
		Map<String, Object> responseMap = new HashMap<>();
		if (tokenObj.getHierarchy() == null) {
			hierarchyList = hierarchyServices.getAllHierarchy();
		} else {
			hierarchyList.add(tokenObj.getHierarchy());
		}
		responseMap.put("message", "success");
		responseMap.put("status", 200);
		responseMap.put("data", attendanceService.getAttendanceByhierarchy(date, hierarchyList));
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
	}

}
