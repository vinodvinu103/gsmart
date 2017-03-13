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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.InventoryAssignmentsCompoundKey;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.HierarchyServices;
import com.gsmart.services.InventoryAssignmentsServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.INVENTORYASSIGN)
public class InventoryAssignmentsController {
	@Autowired
	InventoryAssignmentsServices inventoryAssignmentsServices;
	@Autowired
	GetAuthorization getAuthorization;
	@Autowired
	TokenService tokenService;
	@Autowired
	HierarchyServices hierarchyServices;

	
	@RequestMapping(value="/assign/{min}/{max}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getInventoryAssign(@PathVariable ("min") Integer min, @PathVariable ("max") Integer max, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
		Token tokenObj = (Token) httpSession.getAttribute("hierarchy");
		str.length();
        System.out.println("coming");
		List<Map<String, Object>> inventoryByHierarchy = new ArrayList<>();
		Map<String, Object> finalResponse = new HashMap<>();
		Map<String, Object> responseMap = new HashMap<>();
		Map<String, Object> dataMap = new HashMap<>();
		Map<String, Object> inventoryList = null;
        System.out.println("inside the if condition");
		if (tokenObj.getHierarchy() == null && modulePermission != null) {
			System.out.println("hierarchy is null");
			List<Hierarchy> hierarchyList = hierarchyServices.getAllHierarchy();
			System.out.println("going inside for loop");
			for (Hierarchy hierarchy : hierarchyList) {
				inventoryList = inventoryAssignmentsServices.getInventoryAssignList(tokenObj.getRole(), hierarchy, min, max);
                System.out.println("going to the map");
				dataMap.put("inventoryList", inventoryList);
				dataMap.put("hierarchy", hierarchy);
				dataMap.put("modulePermissions", modulePermission);
                Loggers.loggerEnd("Inventory List:" + inventoryList);
				
				}
			responseMap.put("data", dataMap);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		} else if (tokenObj.getHierarchy() != null && modulePermission != null) {
			inventoryList = inventoryAssignmentsServices.getInventoryAssignList(tokenObj.getRole(), tokenObj.getHierarchy(), min, max);

			dataMap.put("inventoryList", inventoryList);
			dataMap.put("hierarchy", tokenObj.getHierarchy());
            dataMap.put("modulePermissions", modulePermission);
			responseMap.put("data", dataMap);
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

	
	@RequestMapping(value="/assign",method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addInventory(@RequestBody InventoryAssignments inventoryAssignments,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart(inventoryAssignments);
		InventoryAssignments old=null;
		Loggers.loggerValue("InventoryAssignments quantity", inventoryAssignments.getQuantity());

		IAMResponse resp = new IAMResponse();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)){
			Token tokenObj = (Token) httpSession.getAttribute("hierarchy");
			inventoryAssignments.setHierarchy(tokenObj.getHierarchy());

			InventoryAssignmentsCompoundKey ch = inventoryAssignmentsServices.addInventoryDetails(inventoryAssignments,old);

			if (ch != null) {
				resp.setMessage("success");
			} else {
				resp.setMessage("Already exists");
			}
			return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
		} else {
			resp.setMessage("Permission Denied");

		}
		Loggers.loggerEnd();
		
		return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
	}
	
	@RequestMapping(value = "{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editdeleteInventory(@RequestBody InventoryAssignments inventoryAssignments,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		IAMResponse myResponse = null;

		try {
			String tokenNumber = token.get("Authorization").get(0);

			String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

			str.length();
			InventoryAssignments ch = null;

			if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {

				if (task.equals("edit")) {
					ch = inventoryAssignmentsServices.editInventoryDetails(inventoryAssignments);
					if (ch != null) {
						myResponse = new IAMResponse("success");
					} else {
						myResponse = new IAMResponse("DATA IS ALREADY EXIST");

					}
				} else if (task.equals("delete")) {
					inventoryAssignmentsServices.deleteInventoryDetails(inventoryAssignments);
					myResponse = new IAMResponse("success");
					return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
				}
			} else {
				myResponse = new IAMResponse("Permission Denied");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
	}

}


