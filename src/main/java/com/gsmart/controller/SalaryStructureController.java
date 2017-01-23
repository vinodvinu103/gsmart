package com.gsmart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gsmart.model.SalaryStructure;
import com.gsmart.services.SalaryStructureService;

@Controller
@RequestMapping("/salaryStructure")
public class SalaryStructureController {
Logger logger = Logger.getLogger(SalaryStructureController.class);
	@Autowired
	private SalaryStructureService salaryStructureService;
	
	@RequestMapping("/viewSalaryStructure")
	public @ResponseBody List<SalaryStructure> view()
	{
		try {
			List<SalaryStructure> list = salaryStructureService.view();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
			return null;
		}
	}
	
	@RequestMapping("/addSalaryStructure/{updSmartId}")
	public ResponseEntity<Map<String,String>> add(@RequestBody SalaryStructure salaryStructure, @PathVariable("updSmartId") String updSmartId)
	{
		Map<String, String> jsonMap = new HashMap<String, String>();
		
		try{
			salaryStructure.setUpdSmartId(updSmartId);
			salaryStructureService.add(salaryStructure);
			jsonMap.put("result", "Successfully Added");
			return new ResponseEntity<Map<String,String>>(jsonMap, HttpStatus.OK);
		} catch(Exception e){
			e.printStackTrace();
			jsonMap.put("result", "Exception");
			return new ResponseEntity<Map<String,String>>(jsonMap, HttpStatus.OK);
		}
	}
	
	@RequestMapping("/editSalaryStructure/{updSmartId}")
	public ResponseEntity<Map<String,String>> edit(@RequestBody SalaryStructure salaryStructure, @PathVariable("updSmartId") String updSmartId)
	{
		Map<String, String> jsonMap = new HashMap<String, String>();
		
		try{
			salaryStructure.setUpdSmartId(updSmartId);
			salaryStructureService.edit(salaryStructure);
			jsonMap.put("result", "Successfully Edited");
			return new ResponseEntity<Map<String,String>>(jsonMap, HttpStatus.OK);
		} catch(Exception e){
			e.printStackTrace();
			jsonMap.put("result", "Exception");
			return new ResponseEntity<Map<String,String>>(jsonMap, HttpStatus.OK);
		}
	}
	
	@RequestMapping("/deleteSalaryStructure/{updSmartId}")
	public ResponseEntity<Map<String,String>> delete(@RequestBody SalaryStructure salaryStructure, @PathVariable("updSmartId") String updSmartId)
	{
		Map<String, String> jsonMap = new HashMap<String, String>();
		
		try{
			salaryStructureService.delete(salaryStructure);
			jsonMap.put("result", "Successfully Deleted");
			return new ResponseEntity<Map<String,String>>(jsonMap, HttpStatus.OK);
		} catch(Exception e){
			e.printStackTrace();
			jsonMap.put("result", "Exception");
			return new ResponseEntity<Map<String,String>>(jsonMap, HttpStatus.OK);
		}
	}
}
