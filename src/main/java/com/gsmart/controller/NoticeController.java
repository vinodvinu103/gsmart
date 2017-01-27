package com.gsmart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gsmart.dao.NoticeDao;
import com.gsmart.model.Band;
import com.gsmart.model.Notice;
import com.gsmart.model.Profile;
import com.gsmart.model.RolePermission;
import com.gsmart.services.NoticeService;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GSmartServiceException;
//import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping("/notice")

public class NoticeController {
	
	@Autowired
	NoticeService noticeService;
	
	@Autowired
	GetAuthorization getAuthorization;
	
	final Logger logger = Logger.getLogger(NoticeDao.class);
	
	/*@RequestMapping(value = "/viewNotice", method = RequestMethod.GET)
	public ResponseEntity<Map<String,List<Notice>>> viewNotice(@RequestHeader HttpHeaders token,HttpSession httpSession){
		Map<String,List<Notice>> responseMap = new HashMap<>();
		List<Notice> list=new ArrayList<Notice>();
		
		try 
		{
			 
			list=noticeService.viewNotice();
			responseMap.put("selfNotice", list);
			return new ResponseEntity<Map<String,List<Notice>>>(responseMap, HttpStatus.OK);
		} 
		catch (Exception e) 
		{
 			e.printStackTrace();
 			return null;
		}
	}*/
	@RequestMapping( method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> viewAllNotice(HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		List<Notice> noticeAllList = null;
		
		Map<String, Object> notice = new HashMap<>();
		
		noticeAllList = noticeService.viewAllNotice();
	  		notice.put("noticeAllList", noticeAllList);

	  		Loggers.loggerEnd(noticeAllList);
		
			return new ResponseEntity<Map<String,Object>>(notice, HttpStatus.OK);
		
	}
	/*@RequestMapping(value = "/childNotice/{smartId}",method = RequestMethod.GET)
	public ResponseEntity<Map<String,List<Notice>>> childNotice(@PathVariable("smartId") String smartId, @RequestHeader HttpHeaders token,HttpSession httpSession){
    Map<String,List<Notice>> responseMap = new HashMap<>();
    List<Notice> list=new ArrayList<Notice>();
    Loggers.loggerStart();
    Loggers.loggerStart(smartId);
    try
    {
    	
    	list=noticeService.childNotice(smartId);
    	responseMap.put("childNotice", list);
    	return new ResponseEntity<Map<String,List<Notice>>>(responseMap, HttpStatus.OK);
    }
    catch (Exception e){
	
        e.printStackTrace();
        return null;
    
		// TODO: handle exception
	     }
    	
    
	}*/
	@RequestMapping(value = "/viewSpecificNotice/{smart_id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Notice> viewSpecificNotice(@PathVariable("smart_id") Integer smart_id){
		List<Notice> list=new ArrayList<Notice>();
		
		try 
		{
			 
			list=noticeService.viewSpecificNotice(smart_id);
			return list;
		} 
		catch (Exception e) 
		{
 			e.printStackTrace();
 			return null;
		}
	}

	@RequestMapping(value = "/addNotice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String,String>> addNotice(@RequestBody Notice notice)
	{
		
		Map<String,String> jsonMap=new HashMap<>();
		try 
		{
			noticeService.addNotice(notice);
			jsonMap.put("result","success");
			return new ResponseEntity<Map<String,String>>(jsonMap, HttpStatus.OK);
		} 
		catch (Exception e) 
		{
 			e.printStackTrace();
 			jsonMap.put("result","error");
 			return new ResponseEntity<Map<String,String>>(jsonMap, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/editNotice", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Map<String, String>> editNotice(@RequestBody Notice notice) {
		
		Map<String, String> jsonMap = new HashMap<>();
		
		try {
			//notice.setUpdatedId(smart_id);
			noticeService.editNotice(notice);
			jsonMap.put("result", "success");
			return new ResponseEntity<Map<String, String>>(jsonMap, HttpStatus.OK);
		}
			
		catch (Exception e) {
			jsonMap.put("result", "error");
			return new ResponseEntity<Map<String, String>>(jsonMap, HttpStatus.OK);
		}

	}
	
	@RequestMapping(value = "/deleteNotice", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Map<String, String>> deleteNotice(@RequestBody Notice notice,@RequestHeader HttpHeaders Token, HttpSession httpSession ) throws GSmartServiceException {
		
		
		
		Loggers.loggerStart();
		
		String tokenNumber = Token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		//List<Notice> noticeList = null;
    
	//	RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);

		Map<String, String> jsonMap = new HashMap<>();
		try {
			noticeService.deleteNotice(notice);
			jsonMap.put("result", "success");
			return new ResponseEntity<Map<String, String>>(jsonMap, HttpStatus.OK);
		}
			
		catch (Exception e) {
			jsonMap.put("result", "error");
			return new ResponseEntity<Map<String, String>>(jsonMap, HttpStatus.OK);
		}

	}
	
	
	
	

}
