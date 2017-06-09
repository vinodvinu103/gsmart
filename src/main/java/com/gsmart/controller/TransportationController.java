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

import com.gsmart.model.Token;
import com.gsmart.model.Transportation;
import com.gsmart.services.TransportationService;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
//import com.gsmart.util.Loggers;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping("/transportation")
public class TransportationController {

	@Autowired
	private GetAuthorization getAuthorization;
	@Autowired
	private TransportationService transportationService;

	@RequestMapping(value = "/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getTransportationfee(@PathVariable("hierarchy") Long hierarchy,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		Loggers.loggerStart();
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Token tokenObj = (Token) httpSession.getAttribute("token");

		Long hid = null;
		if (tokenObj.getHierarchy() == null) {
			hid = hierarchy;
		} else {
			hid = tokenObj.getHierarchy().getHid();
		}

		Loggers.loggerStart(tokenObj.getHierarchy());

		List<Transportation> TransportationfeeList = null;

		Map<String, Object> permissions = new HashMap<>();

		TransportationfeeList = transportationService.getTransportationFeeList(hid);

		permissions.put("TransportationfeeList", TransportationfeeList);

		Loggers.loggerEnd("TransportationfeeList:" + TransportationfeeList);

		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addTransportationFee(@RequestBody Transportation transportation,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart("entering into addTransportationFee method");
		// Transportation oldTransportationfee = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Loggers.loggerStart(transportation);

		Token tokenObj = (Token) httpSession.getAttribute("token");
		transportation.setHierarchy(tokenObj.getHierarchy());
		Loggers.loggerStart(transportation);
		Map<String, Object> permissions = new HashMap<>();

		Transportation TransportationfeeList = null;
		TransportationfeeList = transportationService.addDetails(transportation);
		Loggers.loggerStart("transportationService is called");

		if (TransportationfeeList != null) {
			permissions.put("status", 200);
			permissions.put("message", "success");
			permissions.put("TransportationfeeList", TransportationfeeList);

		} else {
			permissions.put("status", 404);
			permissions.put("message", "No Data Is Present");

		}
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> editDeleteBand(@RequestBody Transportation transportation,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart(transportation);
		Transportation cb = null;
		Map<String, Object> respMap = new HashMap<>();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if ("edit".equals(task)) {
			cb = transportationService.editTransportationFee(transportation);
			if (cb != null) {
				respMap.put("status", 200);
				respMap.put("message", "Updated Successfully");
			} else {
				respMap.put("status", 400);
				respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
			}
		}
		/*
		 * else if (task.equals("delete")){
		 * transportationService.deleteTransportationFee(transportation);
		 * respMap.put("status", 200); respMap.put("message", "Deleted Successfully"); }
		 */

		Loggers.loggerEnd();

		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}

}
