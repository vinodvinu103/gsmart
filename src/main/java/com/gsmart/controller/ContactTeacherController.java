package com.gsmart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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

import com.gsmart.dao.ContactDao;
import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.MessageDetails;
import com.gsmart.model.Notice;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.BeanFactory;
import com.gsmart.services.ContactServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;


@Controller
@RequestMapping("/contact")
public class ContactTeacherController {

	@Autowired
	private ContactServices contactServices;

	@Autowired
	BeanFactory employeeBeanFactory;

	@Autowired
    	GetAuthorization getAuthorization;

	@Autowired
	TokenService tokenService;
	
	@Autowired
	ContactDao contactDao;

	@RequestMapping(value = "/studentToTeacher", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> studentToTeacher(@RequestBody MessageDetails details,
			@RequestHeader HttpHeaders token, HttpSession httpSession,String role) throws GSmartServiceException {
		Map<String, String> result = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);

		Loggers.loggerStart(tokenNumber);
		System.out.println(""+details.getMessage());
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		try {
				Token tk = (Token) httpSession.getAttribute("token");

				if (contactServices.studentToTeacher(details,tk.getRole())) {
					result.put("result", "MSG Posted");
					return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
				}
			return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", "MSG insertion Failed");
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/teacherToStudent", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> teacherToStudent(@RequestBody MessageDetails details,
			@RequestHeader HttpHeaders token, HttpSession httpSession,String role) throws GSmartServiceException {
		Map<String, Object> result = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		Loggers.loggerStart(tokenNumber);
		System.out.println(""+details.getMessage());
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		try {
				Token tk1 = (Token) httpSession.getAttribute("token");
			MessageDetails msgDetails=	contactServices.teacherToStudent(details,tk1.getRole());
					System.out.println(details);
					result.put("result", "MSG Posted");
					result.put("details", msgDetails);
					return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
				
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", "MSG insertion Failed");
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		}
//		return details;
	}
	
	@RequestMapping(value = "/msgListForTeacher", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, ArrayList<MessageDetails>>> msgList(@RequestBody MessageDetails details,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartServiceException {
		List<MessageDetails> messages = new ArrayList<>();
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, ArrayList<MessageDetails>> jsonMap = new HashMap<>();

		try {
				messages = contactServices.msgList(details);
				if (messages.size() != 0) {
					jsonMap.put("result", (ArrayList<MessageDetails>) messages);
					return new ResponseEntity<Map<String, ArrayList<MessageDetails>>>(jsonMap, HttpStatus.OK);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Map<String, ArrayList<MessageDetails>>>(jsonMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/teacherView/{min}/{max}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> teacherView(@PathVariable("min") Integer min,
			@PathVariable("max") Integer max, @RequestBody MessageDetails details, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartServiceException {
		Loggers.loggerStart(details);
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		Token tk1 = (Token) httpSession.getAttribute("token");
		// List<MessageDetails> messages=new ArrayList<>();
		Map<String, Object> messages = null;
		Map<String, Object> jsonMap = new HashMap<>();
		try {
			String role=tk1.getRole();
			details.setPostedBy(role);
				/*messages = contactServices.studentView(details, Integer.parseInt(min), Integer.parseInt(max));*/
				messages = contactServices.teacherView(tk1, min, max);
				if (messages.size() != 0) {
					jsonMap.put("result", messages);
					return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(messages);
		return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/teacherChat", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> teacherView(@RequestBody MessageDetails details, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartServiceException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		// List<MessageDetails> messages=new ArrayList<>();
		Map<String, Object> messages = null;
		Map<String, Object> jsonMap = new HashMap<>();
		try {
				messages = contactServices.teacherChat(details);
				if (messages.size() != 0) {
					jsonMap.put("result", messages);
					return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(messages);
		System.out.println("fetched message :"+details.getMessage());
		return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/studentChat", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> studentView(@RequestBody MessageDetails details, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartServiceException {
		Loggers.loggerStart(details);
		System.out.println("getting token : " + token);

		Map<String, Object> messages = null;
		Map<String, Object> jsonMap = new HashMap<>();
		try {
				messages = contactServices.studentChat(details);
				if (messages.size() != 0) {
					jsonMap.put("result", messages);
					return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/studentView/{min}/{max}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> studentView(@PathVariable("min") Integer min,
			@PathVariable("max") Integer max, @RequestBody MessageDetails details, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartServiceException {
		Loggers.loggerStart(details);
		System.out.println("getting token : " + token);
		
			String tokenNumber=token.get("Authorization").get(0); 
			String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		    str.length();
		    Token tk1 = (Token) httpSession.getAttribute("token");
		// List<MessageDetails> messages=new ArrayList<>();
		Map<String, Object> messages = null;
		Map<String, Object> jsonMap = new HashMap<>();
		try {
			String role=tk1.getRole();
			details.setPostedBy(role);
				/*messages = contactServices.studentView(details, Integer.parseInt(min), Integer.parseInt(max));*/
				  messages = contactServices.studentView(tk1, min, max);
				if (messages.size() != 0) {
					jsonMap.put("result", messages);
					return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/updateStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Map<String, Object>> updateStatus(@RequestHeader HttpHeaders Token, HttpSession httpSession) throws GSmartServiceException {

		Loggers.loggerStart();

		String tokenNumber = Token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Map<String, Object> jsonMap = new HashMap<>();
		try {
			Token tk1 = (Token) httpSession.getAttribute("token");
			contactServices.updateStatus(tk1, tk1.getHierarchy().getHid(), tk1.getSmartId());
			jsonMap.put("status", 200);
			jsonMap.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		}
		catch (Exception e) {
			jsonMap.put("result", "error");
			jsonMap.put("status", 400);
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		}

	}
	
}