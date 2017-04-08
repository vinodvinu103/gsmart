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
import com.gsmart.services.BandServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.CalendarCalculator;
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
@RequestMapping("/band")
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



	@RequestMapping(value="/{min}/{max}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getBand(@PathVariable("min") Integer min, @PathVariable("hierarchy") Integer hierarchy, @PathVariable("max") Integer max, @RequestHeader HttpHeaders token, HttpSession httpSession)
	throws GSmartBaseException {
		Loggers.loggerStart(hierarchy);
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
	    str.length();
	    Map<String, Object> bandList = null;
       

		Map<String, Object> permissions = new HashMap<>();
        
			bandList = bandServices.getBandList(min, max);
			if(bandList!=null){
				permissions.put("status", 200);
				permissions.put("message", "success");
				permissions.put("bandList",bandList);
				
			}else{
				permissions.put("status", 404);
				permissions.put("message", "No Data Is Present");
				
			}
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		
		}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getBand(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {
		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
	    str.length();

	    List<Band>bandList=null;

		Map<String, Object> permissions = new HashMap<>();
        
		bandList = bandServices.getBandList1();
			if(bandList!=null){
				permissions.put("status", 200);
				permissions.put("message", "success");
				permissions.put("bandList",bandList);
				
			}else{
				permissions.put("status", 404);
				permissions.put("message", "No Data Is Present");
				
			}
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		
		}

	/**
	 * provides the access to persist a new band entity 
	 * Sets the {@code timeStamp} using {@link CalendarCalculator}
	 * @param band is instance of {@link Band}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	
	@RequestMapping( method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addBand(@RequestBody Band band,@RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {
	    
		Loggers.loggerStart(band);

		Map<String, Object> respMap=new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		CompoundBand cb=bandServices.addBand(band);
		
	        if(cb!=null)
	        {
	        	respMap.put("status", 200);
	        	respMap.put("message", "Saved Successfully");
	        }
	        	  
		    else{
		    	respMap.put("status", 400);
	        	respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
		    	
		    }
		    
	    	
        Loggers.loggerEnd();
        return new ResponseEntity<Map<String,Object>>(respMap, HttpStatus.OK);
	     
	}

	/**
	 * provide the access to update band entity 
	 * @param band instance of {@link Band}
	 * @return persistence status (success/error) in JSON format
	 * @see IAMResponse
	 */
	
	@RequestMapping (value="/{task}",method=RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> editDeleteBand(@RequestBody Band band ,@PathVariable("task") String task,
    @RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException{
		Loggers.loggerStart(band);
		Band cb=null;
		Map<String, Object> respMap=new HashMap<>();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		    if(task.equals("edit")){
		    	cb=bandServices.editBand(band);
		    	if(cb!=null){
		    		respMap.put("status", 200);
	        	respMap.put("message", "Updated Successfully");
		    	}else{
		    		respMap.put("status", 400);
	        	respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
		    	}
		    }
		    else if (task.equals("delete")){
		    	bandServices.deleteBand(band);
		    	respMap.put("status", 200);
	        	respMap.put("message", "Deleted Successfully");
		    }
		    
		 Loggers.loggerEnd();
	        
	     return new ResponseEntity<Map<String,Object>>(respMap, HttpStatus.OK);

}
	}

