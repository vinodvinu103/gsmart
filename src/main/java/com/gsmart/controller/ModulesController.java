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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.CompoundModules;
import com.gsmart.model.Modules;
import com.gsmart.model.RolePermission;
import com.gsmart.services.ModulesServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;
@Controller
@RequestMapping(Constants.MODULES)
public class ModulesController {
	@Autowired
	ModulesServices modulesServices;

	@Autowired
	GetAuthorization getAuthorization;

	@Autowired
	TokenService tokenService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getmodules(@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		List<Modules> modulesList = null;

		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);

		Map<String, Object> modules = new HashMap<>();

		modules.put("modulePermission", modulePermission);

		if (modulePermission != null) {
			modulesList = modulesServices.getModulesList();

			modules.put("modulesList", modulesList);
			Loggers.loggerEnd(modulesList);
			return new ResponseEntity<Map<String, Object>>(modules, HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(modules, HttpStatus.OK);
		}

	}
	@RequestMapping( method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addModules(@RequestBody Modules modules,@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
	    
		Loggers.loggerStart(modules);
		IAMResponse rsp=null;
		
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

        if(getAuthorization.authorizationForPost(tokenNumber, httpSession)){
		CompoundModules cb=modulesServices.addModules(modules);
		
	        if(cb!=null)
	        	  rsp=new IAMResponse("DATA IS SUCCESSFULLY SAVED.");
		    else
			      rsp=new IAMResponse("DATA IS ALREADY EXIST.");
		    
	    	Loggers.loggerEnd();
	    	return new ResponseEntity<IAMResponse>(rsp, HttpStatus.OK);
        }else{
			 rsp=new IAMResponse("Permission Denied"); 
		    return new ResponseEntity<IAMResponse>(rsp, HttpStatus.OK);
               }
	     
	}

}
