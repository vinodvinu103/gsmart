package com.gsmart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.Attenance;
import com.gsmart.model.Band;
import com.gsmart.model.CompoundAttenance;
import com.gsmart.model.CompoundInventory;
import com.gsmart.model.Inventory;
import com.gsmart.services.AttenanceServices;
import com.gsmart.services.BandServices;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping()
public class AttenanceControler {
	
	@Autowired
	AttenanceServices attenanceServices;
	
	@RequestMapping(method = RequestMethod.GET )
	public ResponseEntity<List<Attenance>> getAttenance() throws GSmartBaseException {
		Loggers.loggerStart();
		List<Attenance> List = null;
		
		try {
			List = attenanceServices.getAttenanceList();

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd( List);
		return new ResponseEntity<List<Attenance>>( List, HttpStatus.OK);

	}
	
	@RequestMapping(method = RequestMethod.POST )
	
	public ResponseEntity<IAMResponse> addAttenance(@RequestBody Attenance attenance) throws GSmartBaseException {
		Loggers.loggerStart(attenance);
		IAMResponse myResponse;
		
		CompoundAttenance cb= attenanceServices.addAttenace(attenance);
	
		myResponse = new IAMResponse();
		if (cb !=null) {
			myResponse= new IAMResponse("success");
			
		}
		else{
			myResponse=new IAMResponse("Record Already Exists");
		}
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);

	}

	
	
	@RequestMapping (value="/{task}",method=RequestMethod.PUT)
    public ResponseEntity<IAMResponse> editDeleteAttenance(@RequestBody Attenance attenance ,@ PathVariable("task") String task) throws GSmartBaseException{
		Loggers.loggerStart( attenance);
		IAMResponse myResponse;
		 
		if(task.equals("edit"))
			attenanceServices.editAttenance( attenance);
		else if(task.equals("delete"))
			attenanceServices.deleteAttenance( attenance);
		
		myResponse = new IAMResponse("success");
		Loggers.loggerEnd();
			return new ResponseEntity<IAMResponse>(myResponse,HttpStatus.OK);
		
    }

	
	
	
}
