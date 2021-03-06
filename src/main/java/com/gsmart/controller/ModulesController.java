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

import com.gsmart.model.CompoundModules;
import com.gsmart.model.Modules;
import com.gsmart.model.Token;
import com.gsmart.services.ModulesServices;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.MODULES)
public class ModulesController {
	@Autowired
	private ModulesServices modulesServices;

	@Autowired
	private GetAuthorization getAuthorization;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getmodules(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		List<Modules> modulesList = null;

		Token tokenObj = (Token) httpSession.getAttribute("token");

		Map<String, Object> modules = new HashMap<>();

		modulesList = modulesServices.getModulesList(tokenObj.getRole(), tokenObj.getHierarchy());

		modules.put("modulesList", modulesList);
		Loggers.loggerEnd(modulesList);
		return new ResponseEntity<Map<String, Object>>(modules, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addModules(@RequestBody Modules modules,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart(modules);
		Map<String, Object> respMap = new HashMap<>();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		Token tokenObj = (Token) httpSession.getAttribute("token");
		modules.setHierarchy(tokenObj.getHierarchy());
		CompoundModules cb = modulesServices.addModules(modules);

		if (cb != null) {
			respMap.put("status", 200);
			respMap.put("message", "Upadted Successfully");
		}

		else {
			respMap.put("status", 400);
			respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
		}

		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);

	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> editModule(@RequestBody Modules modules,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		Loggers.loggerStart(modules);
		Modules ch = null;
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();
		Map<String, Object> respMap = new HashMap<>();

		if ("edit".equals(task)) {
			ch = modulesServices.editmodule(modules);
			Loggers.loggerStart(ch);
			if (ch != null) {
				respMap.put("status", 200);
				respMap.put("message", "Upadted Successfully");

			} else {
				respMap.put("status", 400);
				respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
			}
		} else {
			modulesServices.deletemodule(modules);
			respMap.put("status", 200);
			respMap.put("message", "Deleted Successfully");
		}

		Loggers.loggerEnd();
		return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}

}
