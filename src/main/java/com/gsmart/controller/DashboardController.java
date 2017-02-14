package com.gsmart.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.Token;
import com.gsmart.services.DashboardService;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
	DashboardService dashBoardService;

	@Autowired
	GetAuthorization getAuthorization;

	@RequestMapping(value = "dash/{date}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAttendanceData(@RequestHeader HttpHeaders token,
			HttpSession httpSession, @PathVariable("date") Long date) throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		Token tokenObj = (Token) httpSession.getAttribute("hierarchy");
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Map<String, Object> dashMap = new HashMap<>();
		Map<String, Object> responseMap = new HashMap<>();
		
		if(tokenObj.getHierarchy() == null) {
			
		}
			
		dashMap.put("AttendanceCount", dashBoardService.getAttendance());
		/*
		 * dashMap.put("InventoryCount", dashService.getInventory());
		 * dashMap.put("totalfee", dashService.getTotalfee());
		 */

		return new ResponseEntity<Map<String, Object>>(dashMap, HttpStatus.OK);

	}

}
