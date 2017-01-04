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
import com.gsmart.model.Band;
import com.gsmart.model.CompoundBand;
import com.gsmart.model.RolePermission;
import com.gsmart.services.BandServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

/**
 * The BandController class implements an application that displays list of band
 * entities, add new band entity, edit available band entity and delete
 * available band entity. these functionalities are provided in
 * {@link BandServices}
 *
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-02-23
 */

@Controller
@RequestMapping(Constants.BAND)
public class BandController {

	@Autowired
	GetAuthorization getAuthorization;

	@Autowired
	BandServices bandServices;

	@Autowired
	TokenService tokenService;

	/**
	 * to view {@link Band} details.
	 * 
	 * @param no
	 *            parameters
	 * @return returns list of band entities present in the Band table
	 * @see List
	 * @throws GSmartBaseException
	 */
	// String module=getAuthorization.getModuleName();

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getBand(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		List<Band> bandList = null;
       
		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);

		Map<String, Object> permissions = new HashMap<>();
        
		permissions.put("modulePermission",modulePermission);
		if (modulePermission != null) {
			System.out.println("success");
			bandList = bandServices.getBandList();
			permissions.put("bandList",bandList);
			Loggers.loggerEnd(bandList);
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		}
		
		}

	/**
	 * provides the access to persist a new band entity 
	 * Sets the {@code timeStamp} using {@link CalendarCalculator}
	 * @param band is instance of {@link Band}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	
	@RequestMapping( method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addBand(@RequestBody Band band,@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
	    
		Loggers.loggerStart(band);
		IAMResponse rsp=null;
		
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

        if(getAuthorization.authorizationForPost(tokenNumber, httpSession)){
		CompoundBand cb=bandServices.addBand(band);
		
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

	/**
	 * provide the access to update band entity 
	 * @param band instance of {@link Band}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	
	@RequestMapping (value="/{task}",method=RequestMethod.PUT)
    public ResponseEntity<IAMResponse> editDeleteBand(@RequestBody Band band ,@PathVariable("task") String task,
    @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException{
		Loggers.loggerStart(band);
		IAMResponse myResponse=null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		 if(getAuthorization.authorizationForPut(tokenNumber,task, httpSession)){
		    if(task.equals("edit")){
		    	bandServices.editBand(band);
		    	if(band!=null)
		    		myResponse =new IAMResponse("SUCCESS");
		    	else
		    		myResponse = new IAMResponse("DATA IS ALREADY EXIST.");
		    }
		    else if (task.equals("delete")){
		    	bandServices.deleteBand(band);
		    	myResponse = new IAMResponse("DATA IS ALREADY EXIST.");
		    }
		    Loggers.loggerEnd();
		        
		     return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		}
		 
		 else {
		    myResponse = new IAMResponse("Permission Denied");
		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
		     }
          

}}
