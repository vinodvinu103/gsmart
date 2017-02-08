
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

import com.gsmart.model.Leave;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.MyTeamLeaveServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.MYTEAMLEAVE)
public class MyTeamLeaveController {
	@Autowired
	MyTeamLeaveServices myteamleaveServices;

	@Autowired
	GetAuthorization getAuthorization;

	@Autowired
	TokenService tokenService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getLeave(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		List<Leave> myTeamList = null;
		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);

		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
		Map<String, Object> myteam = new HashMap<>();
		myteam.put("modulePermission", modulePermission);

		if (modulePermission != null) {
			myTeamList = myteamleaveServices.getLeavelist(tokenObj.getRole(),tokenObj.getHierarchy());

			myteam.put("myTeamList", myTeamList);
			Loggers.loggerEnd(myTeamList);
			return new ResponseEntity<Map<String, Object>>(myteam, HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(myteam, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editDeleteBand(@RequestHeader HttpHeaders token, HttpSession httpSession,
			@RequestBody Leave leave, @PathVariable("task") String task) throws GSmartBaseException {
		Loggers.loggerStart(leave);
		IAMResponse myResponse;

		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
			if (task.equals("sanction"))
				myteamleaveServices.sactionleave(leave);
			else if (task.equals("reject"))
				myteamleaveServices.rejectleave(leave);
			else if(task.equals("cancelSanction"))
				myteamleaveServices.cancelSanctionLeave(leave);

			myResponse = new IAMResponse("success");
			Loggers.loggerEnd();
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		} else {
			myResponse = new IAMResponse("Permission Denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}

	}

}
