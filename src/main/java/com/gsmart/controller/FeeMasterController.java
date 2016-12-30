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

import com.gsmart.model.CompoundFeeMaster;
import com.gsmart.model.FeeMaster;
import com.gsmart.model.RolePermission;
import com.gsmart.services.FeeMasterServices;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

/**
 * The FeeMasterController class implements an application that displays list of
 * feeMaster entities, add new feeMaster entity, edit available feeMaster entity
 * and delete available feeMaster entity. these functionalities are provided in
 * {@link FeeMasterServices}
 *
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */
@Controller
@RequestMapping(Constants.FEE_MASTER)
public class FeeMasterController {
	@Autowired
	FeeMasterServices feeMasterServices;
	
	@Autowired
	GetAuthorization getAuthorization;

	


	/**
	 * to view {@link FeeMaster} details.
	 * 
	 * @param no
	 *            parameters
	 * @return returns list of feeMaster entities present in the FeeMaster table
	 * @see List
	 * @throws GSmartBaseException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getFee(@RequestHeader HttpHeaders token, HttpSession httpSession ) throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber=token.get("Authorization").get(0);
		
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		List<FeeMaster> feeList = null;
		
		RolePermission modulePermission=getAuthorization.authorizationForGet(tokenNumber, httpSession);
		Map<String, Object> permissions=new HashMap<>();
		permissions.put("modulePermissions", modulePermission);
		
		if(modulePermission!=null)
		{
			feeList = feeMasterServices.getFeeList();
			Loggers.loggerValue("feeList", feeList);
			permissions.put("feeList", feeList);
			Loggers.loggerValue("permissions List", permissions);
			return new ResponseEntity<Map<String,Object>>(permissions, HttpStatus.OK);
			
		}else
		{
		return new ResponseEntity<Map<String,Object>>(permissions, HttpStatus.OK);
		}
	}

	/**
	 * provides the access to persist a new feeMaster entity Sets the
	 * {@code timeStamp} using {@link CalendarCalculator}
	 * 
	 * @param feeMaster
	 *            is instance of {@link FeeMaster}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addFee(@RequestBody FeeMaster feeMaster, @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(feeMaster);
		IAMResponse resp =new IAMResponse();
		
		String tokenNumber=token.get("Authorization").get(0);
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		if(getAuthorization.authorizationForPost(tokenNumber, httpSession)){
		CompoundFeeMaster cf = feeMasterServices.addFee(feeMaster);
		
		if (cf!= null) 
			resp.setMessage("success");
			else 
				resp.setMessage("data already exist");
	return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
		}
		else
		{
			resp.setMessage("permission denied");
			return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
		}
	}	

		

	

	/**
	 * provide the access to update feeMaster entity
	 * 
	 * @param feeMaster
	 *            instance of {@link FeeMaster}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	@RequestMapping(value="/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editFee(@RequestBody FeeMaster feeMaster, @PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		
		IAMResponse myResponse;
		Loggers.loggerStart(feeMaster);
		
		String tokenNumber=token.get("Authorization").get(0);
		
		String str=getAuthorization.getAuthentication(tokenNumber, httpSession);
		
		str.length();
		
		if(getAuthorization.authorizationForPut(tokenNumber, task, httpSession))
		{
		
		if(task.equals("edit"))
			feeMasterServices.editFee(feeMaster);
		else if(task.equals("delete"))
			feeMasterServices.deleteFee(feeMaster);
		
		myResponse = new IAMResponse("success");
		Loggers.loggerEnd();
		
		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);

		}
		else
		{
			myResponse=new IAMResponse("Permissions denied");
			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
	}

	/**
	 * provide the access to delete feeMaster entity                                                                                          
	 * 
	 * @param timeStamp
	 * @return deletion status (success/error) in JSON format
	 * @see IAMResponse
	 */
	/*@RequestMapping( method = RequestMethod.DELETE)
	public ResponseEntity<IAMResponse> deleteFee(@RequestBody FeeMaster feeMaster)
			throws GSmartBaseException {
		Loggers.loggerStart();
		IAMResponse myResponse;
		feeMaster.setIsActive("D");
		feeMaster.setExittime(CalendarCalculator.getTimeStamp());
		feeMasterServices.deleteFee(feeMaster);
		myResponse = new IAMResponse("success");
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
	}*/

	/*@RequestMapping(value = "/upload/{updSmartId}", method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> upload(@PathVariable("updSmartId") String updSmartId,
			@RequestParam(value = "file") MultipartFile file) throws GSmartBaseException, IOException {
		Loggers.loggerStart();

		FileUpload fileUpload = new FileUpload();
		fileUpload.setFeeDetails("feeDetails");

		byte[] byteArr = file.getBytes();
		fileUpload.setFile(byteArr);
		feeMasterServices.fileUpload(fileUpload);

		IAMResponse myResponse;
		myResponse = new IAMResponse("success");
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/download/{fileName}", method = RequestMethod.GET)
	public ResponseEntity<Object > download(@PathVariable("fileName") String fileName) throws GSmartBaseException {
		
		Loggers.loggerStart();
		
		
		List<FileUpload> src = feeMasterServices.getFile(fileName);
		Loggers.loggerEnd();
		Loggers.loggerValue(src.get(0));
		
		return new ResponseEntity<Object>(src.get(0), HttpStatus.OK);
	}*/

}