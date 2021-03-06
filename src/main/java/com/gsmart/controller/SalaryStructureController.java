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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.CompoundSalaryStructure;
import com.gsmart.model.SalaryStructure;
import com.gsmart.model.Token;
import com.gsmart.services.SalaryStructureService;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping("/salaryStructure")
public class SalaryStructureController {

	@Autowired
	private SalaryStructureService salaryStructureService;

	@Autowired
	private GetAuthorization getAuthorization;


	@RequestMapping(value = "/min/max/hierarchy", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getSalaryStructure(@PathVariable("min") Integer min,
			@PathVariable("max") Integer max, @PathVariable("hierarchy") Long hierarchy,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartServiceException {
		Loggers.loggerStart();
		Map<String, Object> permissions = new HashMap<>();
		Map<String, Object> salaryStructureList = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
//		Token tokenObj = (Token) httpSession.getAttribute("token");
		Long hid = null;
			salaryStructureList = salaryStructureService.getSalaryStructure(hid, min, max);
			if (salaryStructureList != null) {
				permissions.put("status", 200);
				permissions.put("message", "success");
				permissions.put("salaryStructureList", salaryStructureList);
			} else {
				permissions.put("status", "404");
				permissions.put("message", "success");
				permissions.put("salaryStructureList", salaryStructureList);
			}
		
		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);

	}

	@RequestMapping(value="/salary",method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addSalaryStructure(@RequestBody SalaryStructure salaryStructure,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		Map<String, Object> respMap = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

			Token tokenObj = (Token) httpSession.getAttribute("token");
			salaryStructure.setHierarchy(tokenObj.getHierarchy());

			CompoundSalaryStructure css = salaryStructureService.addSalaryStructure(salaryStructure);
			if (css != null) {
				respMap.put("status", 200);
				respMap.put("message", "Saved Successfully");
			}

			else {
				respMap.put("status", 400);
				respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
			}

		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}

}
