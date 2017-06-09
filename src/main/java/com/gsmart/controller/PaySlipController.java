package com.gsmart.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gsmart.model.PaySlip;
import com.gsmart.services.PaySlipService;

@Controller
@RequestMapping("/paySlip")
public class PaySlipController {

	@Autowired
	private PaySlipService paySlipService;
	
    public	Boolean flag=false;
    
    
    
	
	@RequestMapping("/download")
	public ResponseEntity<Map<String,String>> download(@RequestBody PaySlip paySlip)
	{
		Map<String,String> jsonMap = new HashMap<String,String>();
		
		try{ 
			 paySlipService.download(paySlip);
			 flag=true;
			 
			jsonMap.put("result", "Success");
		} catch(Exception e){
			e.printStackTrace();
			jsonMap.put("result", "Error");
		}
		return new ResponseEntity<Map<String,String>>(jsonMap, HttpStatus.OK); 
	}
	
	@RequestMapping("/adminDownload")
	public ResponseEntity<Map<String,String>> adminDownload(@RequestBody PaySlip paySlip)
	{
		Map<String,String> jsonMap = new HashMap<String,String>();
		
		try{
			paySlipService.adminDownload(paySlip);
			jsonMap.put("result", "Sucess");
		} catch(Exception e){
			e.printStackTrace();
			jsonMap.put("result", "Error");
		}
		
		return new ResponseEntity<Map<String,String>>(jsonMap, HttpStatus.OK);
	}
	/*send Email by Soumen*/
	@RequestMapping("/sendEmail")
	public ResponseEntity<Map<String,String>> sendEmail(@RequestBody PaySlip paySlip)
	{
		Map<String,String> jsonMap = new HashMap<String,String>();
		try{
			paySlipService.sendEmail(paySlip);
			jsonMap.put("result", "Sucess");
		} catch(Exception e){
			e.printStackTrace();
			jsonMap.put("result", "Error");
		}
		return new ResponseEntity<Map<String,String>>(jsonMap, HttpStatus.OK);
	}
	
}
