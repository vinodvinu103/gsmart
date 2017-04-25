package com.gsmart.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.Profile;
import com.gsmart.services.PushNotificationService;
import com.gsmart.util.Loggers;


@Controller
@RequestMapping("/pushnotification")
public class PushNotificationController {
	
	@Autowired
	private PushNotificationService pushService;
	
	@RequestMapping(value="/storetoken",method=RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> deviceToken(@RequestBody Profile profile){
		
		Loggers.loggerStart();
		
		pushService.storeDeviceToken(profile);
		
		Map<String, Object> respMap=new HashMap<>();
		
		respMap.put("message", "success");
		
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String,Object>>(respMap, HttpStatus.OK);
		
	}

}
