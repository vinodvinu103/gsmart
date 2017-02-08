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

import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;
 
/**
 * 
 * The PrivilegeController class implements an application
 * while searching new profile entities that displays list of profile entities,
 * edit available profile entity.
 * these functionalities are provided in {@link ProfileServices}
 * @author :Rajeshwari M
 * @version 1.0
 * @since 2016-02-23
 *
 */

@Controller
@RequestMapping(Constants.PRIVILEGE)
public class PrivilegeController {

	@Autowired
	ProfileServices profileServices;

	@Autowired
	GetAuthorization getAuthorization;
	
	@Autowired
	TokenService tokenService;
	
	
	/**
	 * To search profile details{@link Profile}.
	 * @param profile
	 * @return returns the list of profile entities present in the Profile.
	 * @throws GSmartBaseException
	 * @see IMResponse
	 */
	
	@RequestMapping( method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> search(@RequestBody Profile profile,@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		List<Profile> profileList = null;
        RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);
        
		
		Map<String, Object> privilege = new HashMap<>();
		privilege.put("modulePermission", modulePermission);
		if (modulePermission!= null) {			
			profileList = profileServices.search(profile);
			privilege.put("profileList", profileList);
			Loggers.loggerEnd(profileList);
		 return new ResponseEntity<Map<String, Object>>(privilege, HttpStatus.OK);
	} else {
		return new ResponseEntity<Map<String, Object>>(privilege, HttpStatus.OK);
	}
	}
	

	/**
	 * Provide the access to update profile entity
	 * @param profile instance of {@link Profile}
	 * @return persistance status (success/error) in JSON format
	 * @throws GSmartBaseException
	 * @see IMResponse
	 */
	@RequestMapping(value = "/{task}",method = RequestMethod.PUT)
	public  ResponseEntity<IAMResponse> editPrivilege(@RequestBody Profile profile ,
			@PathVariable("task") String task,@RequestHeader HttpHeaders token,HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse myResponse;
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			if (task.equals("edit"))
		
		     profileServices.editRole(profile);
		
		myResponse = new IAMResponse("success");
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse> (myResponse, HttpStatus.OK);
	}
		 else {
				myResponse = new IAMResponse("Permission Denied");
				return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
			}


}
}
