
package com.gsmart.controller;

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

import com.gsmart.dao.HierarchyDao;
import com.gsmart.dao.InventoryDao;
import com.gsmart.model.CompoundInventory;
import com.gsmart.model.Inventory;
import com.gsmart.model.Token;
import com.gsmart.services.InventoryServices;
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
	private InventoryServices inventoryServices;
	@Autowired
	private GetAuthorization getauthorization;
	
	@Autowired
	private HierarchyDao hierarchyDao;
	
	@Autowired
	private InventoryDao inventorydao;
	

	/**
	 * to view {@link Inventory} details.
	 * 
	 * @param no
	 *            parameters
	 * @return returns list of inventory entities present in the Inventory table
	 * @see List
	 * @throws GSmartBaseException
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchinventory(@RequestBody Inventory inventory, @RequestHeader HttpHeaders token, HttpSession httpSession)throws GSmartBaseException{
		Loggers.loggerStart();
		
		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Long hid = null;
		Token tokenob = (Token) httpSession.getAttribute("token");
		if(tokenob.getHierarchy()==null){
			hid = inventory.getHierarchy().getHid();
		}else {
			hid = tokenob.getHierarchy().getHid();
		}
		List<Inventory> searchlist = null;
		searchlist = inventoryServices.searchinventory(inventory, hid);
		Map<String, Object> si = new HashMap<>();
		si.put("searchinventory", searchlist);
		return new ResponseEntity<>(si, HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/List" , method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> inventoryList(@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Token tokenObj = (Token) httpSession.getAttribute("token");
		List<Inventory> inveList= null;
		Map<String, Object> permissions = new HashMap<>();
		
		inveList=inventorydao.getInventory(tokenObj.getHierarchy().getHid());
		permissions.put("inventoryList", inveList); 
		
		Loggers.loggerEnd("InventoryList:" + inveList);
		
		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		
		}
	
	@RequestMapping(value="/{min}/{max}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getInventory(@PathVariable ("min") int min, @PathVariable("hierarchy") Long hierarchy,@PathVariable ("max") int max, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> inventoryList = null;
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Map<String, Object> permissions = new HashMap<>();

		Long hid=null;
		if(tokenObj.getHierarchy()==null){
			hid=hierarchy;
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}
          inventoryList = inventoryServices.getInventoryList(hid, min, max);
          if(inventoryList!=null)
	        {
        	  permissions.put("status", 200);
        	  permissions.put("inventoryList", inventoryList);
        	  permissions.put("message", "Success");
	        }
	        	  
		    else{
		    	permissions.put("status", 400);
		    	permissions.put("message", "No Data Found");
		    	
		    }
			
			Loggers.loggerEnd(inventoryList);
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
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

	@RequestMapping(value="/hierarchy/{hierarchy}",method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> addInventory(@RequestBody Inventory inventory,@PathVariable("hierarchy") Long hierarchy, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(inventory);

		String tokenNumber = token.get("Authorization").get(0);
		String str = getauthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Map<String, Object> respMap=new HashMap<>();
			Token tokenObj = (Token) httpSession.getAttribute("token");
			if(tokenObj.getHierarchy()==null){
				inventory.setHierarchy(hierarchyDao.getHierarchyByHid(hierarchy));
			}else{
				inventory.setHierarchy(tokenObj.getHierarchy());
				
			}

			CompoundInventory cb = inventoryServices.addInventory(inventory);

			if(cb!=null)
	        {
	        	respMap.put("status", 200);
	        	respMap.put("message", "Saved Successfully");
	        }
	        	  
		    else{
		    	respMap.put("status", 400);
	        	respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
		    	
		    }
		    
        Loggers.loggerEnd();
    	return new ResponseEntity<Map<String,Object>>(respMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> editInventory(@RequestBody Inventory inventory,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart(inventory);
		Inventory ch=null;
		String tokenNumber = token.get("Authorization").get(0);

		String str = getauthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		Map<String, Object> respMap=new HashMap<>();

			
			if (task.equals("edit")) {
				ch = inventoryServices.editInventory(inventory);
				if (ch != null) {
					respMap.put("status", 200);
		        	respMap.put("message", "Upadted Successfully");

				} else {
					respMap.put("status", 400);
		        	respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
				}
			} else if (task.equals("delete")) {
				inventoryServices.deleteInventory(inventory);
				respMap.put("status", 200);
	        	respMap.put("message", "Deleted Successfully");
			}

		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}

}

