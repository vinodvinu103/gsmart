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

import com.gsmart.model.Hierarchy;
import com.gsmart.model.Token;
import com.gsmart.services.HierarchyServices;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

/**
 * The HierarchyController class implements an application that displays list of
 * hierarchy entities, add new hierarchy entity, edit available hierarchy entity
 * and delete available hierarchy entity. these functionalities are provided in
 * {@link HierarchyServices}
 *
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */

@Controller
@RequestMapping(Constants.HIERARCHY)
public class HierarchyController {

	@Autowired
	private HierarchyServices hierarchyServices;

	@Autowired
	private GetAuthorization getAuthorization;

	

	/**
	 * to view {@link Hierarchy} details.
	 * 
	 * @param no
	 *            parameters
	 * @return returns list of hierarchy entities present in the Hierarchy table
	 * @see List
	 * @throws GSmartBaseException
	 */
	
	@RequestMapping(value="/search", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> searchhierarchy(@RequestBody Hierarchy hierarchy, @RequestHeader HttpHeaders token, HttpSession httpSession)throws GSmartBaseException{
		Loggers.loggerStart();
		Map<String, Object> searchhierar = new HashMap<>();
		List<Hierarchy> searchHier = null;
		searchHier = hierarchyServices.searchhierarchy(hierarchy);
		searchhierar.put("searchlist", searchHier);
		return new ResponseEntity<>(searchhierar, HttpStatus.OK);
		
	}


	@RequestMapping(value="/{min}/{max}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getHierarchy(@PathVariable ("min") Integer min, @PathVariable ("max") Integer max, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> hierarchyList = null;
		Token tokenObj=(Token) httpSession.getAttribute("token");

		Map<String, Object> permissions = new HashMap<>();

			hierarchyList = hierarchyServices.getHierarchyList(tokenObj.getRole(),tokenObj.getHierarchy(), min, max);
			if(hierarchyList!=null){
				permissions.put("status", 200);
				permissions.put("message", "success");
				permissions.put("hierarchyList",hierarchyList);
				
			}else{
				permissions.put("status", 404);
				permissions.put("message", "No Data Is Present");
				
			}
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);

	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAllHierarchys(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		List<Hierarchy> hierarchyList = null;
		Token tokenObj=(Token) httpSession.getAttribute("token");

		Map<String, Object> permissions = new HashMap<>();

			hierarchyList = hierarchyServices.getHierarchyList1(tokenObj.getRole(),tokenObj.getHierarchy());
			if(hierarchyList!=null){
				permissions.put("status", 200);
				permissions.put("message", "success");
				permissions.put("hierarchyList",hierarchyList);
				
			}else{
				permissions.put("status", 404);
				permissions.put("message", "No Data Is Present");
				
			}
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);

	}

	/**
	 * provides the access to persist a new hierarchy entity Sets the
	 * {@code timeStamp} using {@link CalendarCalculator}
	 * 
	 * @param hierarchy
	 *            is instance of {@link Hierarchy}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addHierarchy(@RequestBody Hierarchy hierarchy, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(hierarchy);
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		Map<String, Object> respMap=new HashMap<>();
			boolean status = hierarchyServices.addHierarchy(hierarchy);
			if (status) {
				respMap.put("status", 200);
	        	respMap.put("message", "Saved Successfully");
			} else {
				respMap.put("status", 400);
	        	respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
			}
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}

	/**
	 * provide the access to update hierarchy entity
	 * 
	 * @param hierarchy
	 *            instance of {@link Hierarchy}
	 * @return pearsistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> editDeleteHierarchy(@RequestBody Hierarchy hierarchy,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart(hierarchy);
		Map<String, Object> respMap=new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		Hierarchy ch=null;

			if (task.equals("edit")) {
				ch = hierarchyServices.editHierarchy(hierarchy);
				if (ch != null) {
					respMap.put("status", 200);
		        	respMap.put("message", "Saved Succesfully");
				} else {
					respMap.put("status", 400);
		        	respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
				}
			} else if (task.equals("delete")) {
				hierarchyServices.deleteHierarchy(hierarchy);
				respMap.put("status", 200);
	        	respMap.put("message", "Deleted Successfully");
			} 
		
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}

}
