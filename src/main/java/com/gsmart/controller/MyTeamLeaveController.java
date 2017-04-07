
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

import com.gsmart.dao.ProfileDao;
import com.gsmart.model.Leave;
import com.gsmart.model.Profile;
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
	
	@Autowired
	ProfileDao profileDao;

	@RequestMapping(value="/{min}/{max}/{hierarchy}",method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getLeave(@PathVariable ("hierarchy") Long hierarchy,@PathVariable ("min") int min, @PathVariable ("max") int max, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		Map<String, Object> myTeamList = null;

		Token tokenObj=(Token) httpSession.getAttribute("token");
		Map<String, Object> myteam = new HashMap<>();
		String smartId=tokenObj.getSmartId();
		Profile profileInfo=profileDao.getProfileDetails(smartId);
		
		Long hid=null;
		if(tokenObj.getHierarchy()==null){
			hid=hierarchy;
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}
		
			myTeamList = myteamleaveServices.getLeavelist(profileInfo,hid,min,max);

			myteam.put("myTeamList", myTeamList);
			Loggers.loggerEnd(myTeamList);
			return new ResponseEntity<Map<String, Object>>(myteam, HttpStatus.OK);
	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editDeleteMyTeamLeave(@RequestHeader HttpHeaders token, HttpSession httpSession,
			@RequestBody Leave leave, @PathVariable("task") String task) throws GSmartBaseException {
		Loggers.loggerStart(leave);
		IAMResponse myResponse;

		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
			if (task.equals("sanction"))
				myteamleaveServices.sactionleave(leave);
			else if (task.equals("reject"))
				myteamleaveServices.rejectleave(leave);
			else if(task.equals("cancelSanction"))
				myteamleaveServices.cancelSanctionLeave(leave);

			myResponse = new IAMResponse("success");
			Loggers.loggerEnd();
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);

	}

}
