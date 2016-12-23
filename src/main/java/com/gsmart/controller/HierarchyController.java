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

import com.gsmart.model.CompoundHierarchy;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.RolePermission;
import com.gsmart.services.HierarchyServices;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.*;

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
	HierarchyServices hierarchyServices;

	@Autowired
	GetAuthorization getAuthorization;

	@Autowired
	LoginController controller;

	/**
	 * to view {@link Hierarchy} details.
	 * 
	 * @param no
	 *            parameters
	 * @return returns list of hierarchy entities present in the Hierarchy table
	 * @see List
	 * @throws GSmartBaseException
	 */

	/* String name=Loggers.moduleName(); */

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getHierarchy(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		List<Hierarchy> hierarchyList = null;
		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);

		Map<String, Object> permission = new HashMap<>();
		permission.put("modulePermission", modulePermission);

		if (modulePermission != null) {
			hierarchyList = hierarchyServices.getHierarchyList();
			permission.put("hierarchyList", hierarchyList);
			Loggers.loggerEnd(hierarchyList);
			return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);
		}

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
	public ResponseEntity<IAMResponse> addHierarchy(@RequestBody Hierarchy hierarchy, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(hierarchy);
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		IAMResponse myResponse;
		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
			CompoundHierarchy ch = hierarchyServices.addHierarchy(hierarchy);
			if (ch != null) {
				myResponse = new IAMResponse("success");
			} else {
				myResponse = new IAMResponse("DATA IS ALREADY EXIST");
			}
		} else {
			myResponse = new IAMResponse("Permission Denied...!");
		}
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
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
	public ResponseEntity<IAMResponse> editDeleteHierarchy(@RequestBody Hierarchy hierarchy,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart(hierarchy);
		IAMResponse myResponse = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		Hierarchy ch=null;

		if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			System.out.println("in side edit....");
			if (task.equals("edit")) {
				ch = hierarchyServices.editHierarchy(hierarchy);
				if (ch != null) {
					myResponse = new IAMResponse("success");

				} else {
					myResponse = new IAMResponse("DATA IS ALREADY EXIST");
					System.out.println("data already exist");
				}
			} else if (task.equals("delete")) {
				hierarchyServices.deleteHierarchy(hierarchy);
				myResponse = new IAMResponse("success");
			} 
		}
		else {
			myResponse = new IAMResponse("Permission denied....!");
			System.out.println("Permission denied....");
		}
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
	}

}
