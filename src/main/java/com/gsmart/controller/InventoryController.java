package com.gsmart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

//import org.hibernate.annotations.common.util.impl.Log_.logger;
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

import com.gsmart.model.CompoundInventory;
import com.gsmart.model.Inventory;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.InventoryServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

/**
 * The InventoryController class implements an application that displays list of
 * inventory entities, add new inventory entity, edit available inventory entity
 * and delete available inventory entity. these functionalities are provided in
 * {@link InventoryServices}
 *
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */
@Controller
@RequestMapping(Constants.INVENTORY)
public class InventoryController {

	@Autowired
	InventoryServices inventoryServices;
	@Autowired
	GetAuthorization  getauthorization;
	@Autowired
	TokenService tokenService;
	
	/**
	 * to view {@link Inventory} details.
	 * 
	 * @param no
	 *            parameters
	 * @return returns list of inventory entities present in the Inventory table
	 * @see List
	 * @throws GSmartBaseException
	 */
	
	@RequestMapping(value="/{min}/{max}", method = RequestMethod.GET )
	public ResponseEntity<Map<String, Object>> getInventory(@PathVariable ("min") int min, @PathVariable ("max") int max, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		 Map<String, Object> inventoryList = null;
     RolePermission	modulePermission=getauthorization.authorizationForGet(tokenNumber, httpSession);
     Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
          Map<String, Object> permissions = new HashMap<>();
		
		permissions.put("modulePermission",modulePermission);
       
		if (modulePermission!= null) {
			 inventoryList = inventoryServices.getInventoryList(tokenObj.getRole(),tokenObj.getHierarchy(), min, max);
			 permissions.put("inventoryList", inventoryList);
				Loggers.loggerEnd(inventoryList);
				return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		    }
		 else {
				return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
			}
		}

	/**
	 * provides the access to persist a new inventory entity Sets the
	 * {@code timeStamp} using {@link CalendarCalculator}
	 * 
	 * @param inventory
	 *            is instance of {@link Inventory}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addInventory(@RequestBody Inventory inventory,@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(inventory);

		IAMResponse resp = new IAMResponse();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		
		if (getauthorization.authorizationForPost(tokenNumber, httpSession)) {
			Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
			
			inventory.setHierarchy(tokenObj.getHierarchy());
		CompoundInventory cb= inventoryServices.addInventory(inventory);
		
		
		if (cb != null)
			resp.setMessage("success");
		else
			resp.setMessage("Already exists");
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
	} else {
		resp.setMessage("Permission Denied");
		return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
	}
	}
	
	
	
	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editInventory(@RequestBody Inventory inventory,@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse myResponse;
		String tokenNumber = token.get("Authorization").get(0);

		String str = getauthorization.getAuthentication(tokenNumber, httpSession);

		str.length();


		if (getauthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			if (task.equals("edit"))
	
			inventoryServices.editInventory(inventory);
			
			else if (task.equals("delete"))
				
		    inventoryServices.deleteInventory(inventory);

			myResponse = new IAMResponse("success");
			Loggers.loggerEnd();
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		} else {
			myResponse = new IAMResponse("Permission Denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
	}

	
	
/*	 
	@RequestMapping( method = RequestMethod.DELETE)
	public ResponseEntity<IAMResponse> deleteInventory(@RequestBody Inventory inventory)
			throws GSmartBaseException {
		System.out.println(inventory);
		Loggers.loggerStart();
		IAMResponse myResponse;
		inventoryServices.deleteInventory(inventory);
		myResponse = new IAMResponse("success");
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
	}*/
/*	public void getPermission(String role) throws GSmartServiceException {

		rolePermissionServices.getPermission(role);
	}*/


}
