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

	/*
	 * public static Logger logger =
	 * Logger.getLogger(InventoryAssignmentsController.class);
	 */

	/*
	 * @RequestMapping(value="/view", method= RequestMethod.POST,consumes =
	 * MediaType.APPLICATION_JSON_VALUE)
	 */

	/*
	 * @RequestMapping(method = RequestMethod.GET) public ResponseEntity
	 * <Map<String, Object>> getInventory(@RequestHeader HttpHeaders token,
	 * HttpSession httpSession) throws GSmartBaseException {
	 * Loggers.loggerStart(); String tokenNumber =
	 * token.get("Authorization").get(0); String str =
	 * getAuthorization.getAuthentication(tokenNumber, httpSession);
	 * str.length();
	 * 
	 * List<InventoryAssignments> inventoryList = null;
	 * 
	 * RolePermission modulePermission =
	 * getAuthorization.authorizationForGet(tokenNumber, httpSession);
	 * 
	 * Map<String, Object> permission = new HashMap<>();
	 * permission.put("modulePermission", modulePermission); if
	 * (modulePermission != null) { inventoryList =
	 * inventoryAssignmentsServices.getInventoryList();
	 * permission.put("inventoryList", inventoryList);
	 * 
	 * return new ResponseEntity<Map<String, Object>>(permission,
	 * HttpStatus.OK); } else { Loggers.loggerEnd(); return new
	 * ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK); }
	 * 
	 * }
	 */
	@RequestMapping(value="/assign/{min}/{max}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getInventoryAssign(@PathVariable ("min") Integer min, @PathVariable ("max") Integer max, @RequestHeader HttpHeaders token, HttpSession httpSession)
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
/*		List<InventoryAssignments> inventoryList = null;
*/		Map<String, Object> inventoryList = null;
		// RolePermission modulePermission =
		// getAuthorization.authorizationForGet(tokenNumber, httpSession);
		if (tokenObj.getHierarchy() == null && modulePermission != null) {
			System.out.println("hierarchy is null");
			List<Hierarchy> hierarchyList = hierarchyServices.getAllHierarchy();
			for (Hierarchy hierarchy : hierarchyList) {
				inventoryList = inventoryAssignmentsServices.getInventoryList(tokenObj.getRole(), hierarchy, min, max);

				dataMap.put("inventoryList", inventoryList);
				dataMap.put("hierarchy", hierarchy);
				inventoryByHierarchy.add(dataMap);

			}
			finalResponse.put("inventoryList", inventoryByHierarchy);
			finalResponse.put("modulePermissions", modulePermission);
			responseMap.put("data", finalResponse);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
		} else if (tokenObj.getHierarchy() != null && modulePermission != null) {
			inventoryList = inventoryAssignmentsServices.getInventoryList(tokenObj.getRole(), tokenObj.getHierarchy(), min, max);

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

	
	@RequestMapping(method = RequestMethod.POST)
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
	

	

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
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


