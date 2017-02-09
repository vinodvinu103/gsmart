package com.gsmart.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.gsmart.model.MessageDetails;
import com.gsmart.model.RolePermission;
import com.gsmart.services.BeanFactory;
import com.gsmart.services.ContactServices;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.Loggers;
import com.itextpdf.text.pdf.codec.Base64.OutputStream;

@Controller
@RequestMapping("/contact")
public class ContactTeacherController {

	@Autowired
	ContactServices contactServices;

	@Autowired
	BeanFactory employeeBeanFactory;
	
	@Autowired
	GetAuthorization getAuthorization;
	
	@RequestMapping(value="/studentToTeacher", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> studentToTeacher(@RequestBody MessageDetails details,@RequestHeader HttpHeaders token,HttpSession httpSession) throws GSmartServiceException {
		Map<String, String> result=new HashMap<>();
	
		String tokenNumber=token.get("Authorization").get(0);
		Loggers.loggerStart(tokenNumber);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		
		
		getAuthorization.authorizationForGet(tokenNumber, httpSession);
		try 
		{
			if(getAuthorization.authorizationForPost(tokenNumber, httpSession));
			{
			if (contactServices.studentToTeacher(details))
			{
				result.put("result", "MSG Posted");
				return new ResponseEntity<Map<String,String>>(result, HttpStatus.OK);
			}
			}
			return new ResponseEntity<Map<String,String>>(result,HttpStatus.OK);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			result.put("result", "MSG insertion Failed");
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String,String>>(result,HttpStatus.OK);
		}
	}
	
	
	@RequestMapping(value="/msgListForTeacher", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, ArrayList<MessageDetails>>> msgList(@RequestBody MessageDetails details,@RequestHeader HttpHeaders token,HttpSession httpSession) throws GSmartServiceException {
		List<MessageDetails> messages=new ArrayList<>();
		Loggers.loggerStart();
		String tokenNumber=token.get("Authorization").get(0);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, ArrayList<MessageDetails>> jsonMap = new HashMap<>();
		
		getAuthorization.authorizationForGet(tokenNumber, httpSession);
		try 
		{
			if(getAuthorization.authorizationForPost(tokenNumber, httpSession))
			{
			messages=contactServices.msgList(details);
			if (messages.size() != 0) {
				jsonMap.put("result", (ArrayList<MessageDetails>) messages);
				return new ResponseEntity<Map<String, ArrayList<MessageDetails>>>(jsonMap,HttpStatus.OK);
			} 
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return new ResponseEntity<Map<String, ArrayList<MessageDetails>>>(jsonMap,HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	@RequestMapping(value="/teacherView", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, ArrayList<MessageDetails>>> teacherView(@RequestBody MessageDetails details,@RequestHeader HttpHeaders token,HttpSession httpSession) throws GSmartServiceException
	{
		Loggers.loggerStart();
		String tokenNumber=token.get("Authorization").get(0);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		//getAuthorization.authorizationForGet(tokenNumber, httpSession);
		List<MessageDetails> messages=new ArrayList<>();
		Map<String, ArrayList<MessageDetails>> jsonMap = new HashMap<>();
		try 
		{
			if(getAuthorization.authorizationForPost(tokenNumber, httpSession))
			{
			messages=contactServices.teacherView(details);
			if (messages.size()!=0) {
				jsonMap.put("result", (ArrayList<MessageDetails>) messages);
				return new ResponseEntity<Map<String, ArrayList<MessageDetails>>>(jsonMap,HttpStatus.OK);
			}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		Loggers.loggerEnd(messages);
		return new ResponseEntity<Map<String, ArrayList<MessageDetails>>>(jsonMap,HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(value="/teacherToStudent", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> teacherToStudent(@RequestBody MessageDetails details,@RequestHeader HttpHeaders token,HttpSession httpSession) throws GSmartServiceException {
		Map<String, String> result=new HashMap<>();
		Loggers.loggerStart();
		String tokenNumber=token.get("Authorization").get(0);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		getAuthorization.authorizationForGet(tokenNumber, httpSession);
		
		try 
		{
			if(getAuthorization.authorizationForPost(tokenNumber, httpSession))
			{
			if (contactServices.teacherToStudent(details)) {

				result.put("result", "MSG Posted");
				result.put("result", "Image Posted");
				return new ResponseEntity<Map<String,String>>(result, HttpStatus.OK);
			}
			}
			return new ResponseEntity<Map<String,String>>(result,HttpStatus.OK);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			result.put("result", "MSG insertion Failed");
			result.put("result", "Image insertion failed");
			return new ResponseEntity<Map<String,String>>(result,HttpStatus.OK);
		}
	}


	
/*	@RequestMapping(value="/teacherToStudent", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> teacherToStudent1(@RequestBody MessageDetails details,@RequestHeader HttpHeaders token,HttpSession httpSession) throws GSmartServiceException {
		Map<String, String> result=new HashMap<>();
		Loggers.loggerStart();
		String tokenNumber=token.get("Authorization").get(0);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		getAuthorization.authorizationForGet(tokenNumber, httpSession);
		
		try 
		{
			if(getAuthorization.authorizationForPost(tokenNumber, httpSession))
			{
			if (contactServices.teacherToStudent(details)) {

				result.put("result", "image Posted");
				return new ResponseEntity<Map<String,String>>(result, HttpStatus.OK);
			}
			}
			return new ResponseEntity<Map<String,String>>(result,HttpStatus.OK);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			result.put("result", "image insertion Failed");
			return new ResponseEntity<Map<String,String>>(result,HttpStatus.OK);
		}
	}
	*/
	
	
	
	
	@RequestMapping(value="/studentView", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, ArrayList<MessageDetails>>> studentView(@RequestBody MessageDetails details,@RequestHeader HttpHeaders token,HttpSession httpSession) throws GSmartServiceException
	{
		Loggers.loggerStart(details);
		System.out.println("getting token : "+token);
		/*String tokenNumber=token.get("Authorization").get(0);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		getAuthorization.authorizationForGet(tokenNumber, httpSession);*/
		
		List<MessageDetails> messages=new ArrayList<>();
		Map<String, ArrayList<MessageDetails>> jsonMap = new HashMap<>();
		try 
		{
			/*if(getAuthorization.authorizationForPost(tokenNumber, httpSession))*/
			{
			messages=contactServices.studentView(details);
			if (messages.size()!=0) {
				jsonMap.put("result", (ArrayList<MessageDetails>) messages);
				return new ResponseEntity<Map<String, ArrayList<MessageDetails>>>(jsonMap,HttpStatus.OK);
			}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return new ResponseEntity<Map<String, ArrayList<MessageDetails>>>(jsonMap,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/view", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, ArrayList<MessageDetails>>> allMessages(@RequestHeader HttpHeaders token,HttpSession httpSession) throws GSmartServiceException {
		Loggers.loggerStart();
		String tokenNumber=token.get("Authorization").get(0);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		getAuthorization.authorizationForGet(tokenNumber, httpSession);
		Map<String, ArrayList<MessageDetails>> result=new HashMap<>();
		ArrayList<MessageDetails> messages=new ArrayList<>();
		try 
		{
			if(getAuthorization.authorizationForPost(tokenNumber, httpSession))
			{
			messages=(ArrayList<MessageDetails>) contactServices.viewAllMessages();
			result.put("result", messages);
			return new ResponseEntity<Map<String,ArrayList<MessageDetails>>>(result,HttpStatus.OK);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			result.put("result", null);
			return new ResponseEntity<Map<String,ArrayList<MessageDetails>>>(result,HttpStatus.OK);
		}
		return new ResponseEntity<Map<String,ArrayList<MessageDetails>>>(result,HttpStatus.OK);
	}
	
	@RequestMapping(value="/get",method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getDetails(@RequestHeader HttpHeaders token,HttpSession httpSession)throws GSmartBaseException{
		Loggers.loggerStart();
		String tokenNumber=token.get("Authorization").get(0);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		RolePermission modulePermission=getAuthorization.authorizationForGet(tokenNumber, httpSession);
		Map<String, Object> permission=new HashMap<>();
		permission.put("modulePermission", modulePermission);
		List<MessageDetails> list=null;
		try {
			if(modulePermission!=null){
			list=contactServices.getData();
			permission.put("contactList", list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Map<String,Object>>(permission, HttpStatus.OK);
	}
	/*@RequestMapping(value = "/downloadURL/", method = RequestMethod.PUT)
	public void downloadFile(HttpServletResponse response,
	        @RequestBody Map<String,String> spParams
	        ) throws IOException {
	        OutputStream outStream=null;
	outStream = (OutputStream) response.getoutput//is important manage the exceptions here
	ObjectThatWritesOnOutputStream myWriter= new ObjectThatWritesOnOutputStream();// note that this object doesn exist on JAVA,
	ObjectThatWritesOnOutputStream.write(outStream);//you can configure more things here
	outStream.flush();
	return;
	}*/
	

}