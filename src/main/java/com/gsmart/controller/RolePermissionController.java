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
import com.gsmart.model.RolePermission;
import com.gsmart.model.RolePermissionCompound;
import com.gsmart.model.Token;
import com.gsmart.services.RolePermissionServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

/**
 * The PermissionController class implements an application that displays list
 * of permission entities, add new permission entity, edit available permission
 * entity and delete available permission entity. these functionalities are
 * provided in {@link PermissionServices}
 *
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */
@Controller
@RequestMapping(Constants.ROLEPERMISSION)
public class RolePermissionController {

	@Autowired
	RolePermissionServices rolePermissionServices;

	@Autowired
	GetAuthorization getAuthorization;

	@Autowired
	TokenService tokenService;

	/**
	 * to view {@link Permission} details.
	 * 
	 * @param no
	 *            parameters
	 * @return returns list of permission entities present in the RolePermission
	 *         table
	 * @see List
	 * @throws GSmartBaseException
	 */
	@RequestMapping(value="/{min}/{max}", method = RequestMethod.GET)

	public ResponseEntity<Map<String, Object>> getPermissionList(@PathVariable ("min") Integer min, @PathVariable ("max") Integer max, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		Map<String, Object> rolePermissionList = null;
		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);

		Map<String, Object> permissions = new HashMap<>();

		permissions.put("modulePermission", modulePermission);
		Token tokenObj = (Token) httpSession.getAttribute("hierarchy");

		if (modulePermission != null) {
			rolePermissionList = rolePermissionServices.getPermissionList(tokenObj.getRole(), tokenObj.getHierarchy(), min, max);

			permissions.put("rolePermissionList", rolePermissionList);
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/subModules", method = RequestMethod.GET)
	public ResponseEntity<List<RolePermission>> getSubModels(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart(httpSession);
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		getAuthorization.authorizationForGet(tokenNumber, httpSession);
		System.out.println("<<<<<<role permission>>>>>>>>");

		str.length();

		List<RolePermission> subModules = null;

		Token tokenObj = (Token) httpSession.getAttribute("hierarchy");
		System.out.println("Token object" + tokenObj);
		subModules = rolePermissionServices.getSubModuleNames(tokenObj.getRole(), tokenObj.getHierarchy());
		System.out.println("submodule ::" + subModules);

		return new ResponseEntity<List<RolePermission>>(subModules, HttpStatus.OK);
	}

	/**
	 * provides the access to persist a new permission entity Sets the
	 * {@code timeStamp} using {@link CalendarCalculator}
	 * 
	 * @param permission
	 *            is instance of {@link RolePermission}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addPermission(@RequestBody RolePermission permission,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart(permission);
		IAMResponse resp = new IAMResponse();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
			Token tokenObj = (Token) httpSession.getAttribute("hierarchy");
			permission.setHierarchy(tokenObj.getHierarchy());
			RolePermissionCompound cb = rolePermissionServices.addPermission(permission);

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

	/**
	 * provide the access to update permission entity
	 * 
	 * @param permission
	 *            instance of {@link RolePermission}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editPermission(@RequestBody RolePermission permission,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse resp = new IAMResponse();
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			if (task.equals("edit")) {
				RolePermission cb = rolePermissionServices.editPermission(permission);

				if (cb != null)
					resp.setMessage("success");
				else
					resp.setMessage("Already exists");
				Loggers.loggerEnd();

			}

			else if (task.equals("delete")) {
				rolePermissionServices.deletePermission(permission);
				resp.setMessage("success");

			}

		}

		else {
			resp.setMessage("Permission Denied");

		}

		return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
	}

	public void getPermission(String role) throws GSmartServiceException {

		rolePermissionServices.getPermission(role);
	}

}