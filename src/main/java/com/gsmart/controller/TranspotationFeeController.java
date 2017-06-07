package com.gsmart.controller;

import java.util.ArrayList;
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

import com.gsmart.model.Token;
import com.gsmart.model.TransportationFee;
import com.gsmart.services.TranspotationFeeservice;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.TRANSPOTATIONFEE)
public class TranspotationFeeController {
	
	@Autowired
	private TranspotationFeeservice transpotationFeeservice;
	
	@Autowired
	private GetAuthorization getAuthorization;
	
	@RequestMapping(value="/addfee",method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addFee(@RequestBody TransportationFee taranspotationfee, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		Map<String, Object> respMap=new HashMap<>();
			
			try {
				Token tokenObj = (Token) httpSession.getAttribute("token");
				taranspotationfee.setHierarchy(tokenObj.getHierarchy());
				TransportationFee	cb	=transpotationFeeservice.addTranspotationFee(taranspotationfee);
				if(cb!=null)
				{
					respMap.put("status", 200);
					respMap.put("message", "Upadted Successfully");
				}

				else {
					respMap.put("status", 400);
					respMap.put("message", "Data Already Exist, Please try with SomeOther Data");
				}
				
		
				
			} catch (Exception e) {
				throw new GSmartBaseException(e.getMessage());
			}
			Loggers.loggerEnd();
			
			return new ResponseEntity<Map<String, Object>>(respMap, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/paying/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editFee(@RequestBody TransportationFee transpotationfee, @PathVariable("task") String task,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		IAMResponse myResponse;
		Loggers.loggerStart(transpotationfee);
		Map<String, Object> responseMap = new HashMap<>();
		Map<String, Object> response = new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

	if (task.equals("edit"))
		transpotationFeeservice.editTranspotationFee(transpotationfee);
			else if (task.equals("delete"))
			transpotationFeeservice.deleteFee(transpotationfee);

			myResponse = new IAMResponse("success");
			response.put("message", myResponse);
			responseMap.put("data", response);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
			Loggers.loggerEnd();

			return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/paidtranspotationfee/{min}/{max}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getPaidStudentsList(@PathVariable ("hierarchy") Long hierarchy,@PathVariable ("min") Integer min, @PathVariable ("max") Integer max, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart(min);

		Loggers.loggerStart(max);
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Map<String, Object> PaidStudentsList = null;
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Long hid=null;
		if(tokenObj.getHierarchy()==null){
			hid=hierarchy;
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}
		Map<String, Object> permission = new HashMap<>();
		Map<String, Object> responseMap = new HashMap<>();
			PaidStudentsList = transpotationFeeservice.getPaidStudentsList(hid, min, max);
			if(PaidStudentsList!=null){
			permission.put("PaidStudentsList", PaidStudentsList);
			responseMap.put("data", permission);
			responseMap.put("status", 200);
			responseMap.put("message", "success");

		} else {
			responseMap.put("data", permission);
			responseMap.put("status", 404);
			responseMap.put("message", "data not found");
			Loggers.loggerEnd();
		
		}
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/unpaidtranspotationfee/{min}/{max}/{hierarchy}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getUnPaidStudentsList(@PathVariable ("hierarchy") Long hierarchy,@PathVariable ("min") Integer min, @PathVariable ("max") Integer max, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		Map<String, Object> unPaidStudentsList = null;
		Token tokenObj = (Token) httpSession.getAttribute("token");
		Long hid=null;
		if(tokenObj.getHierarchy()==null){
			hid=hierarchy;
		}else{
			hid=tokenObj.getHierarchy().getHid();
		}
		Map<String, Object> responseMap = new HashMap<>();
		
			unPaidStudentsList= transpotationFeeservice.getUnpaidStudentsList(hid, min, max);
			Loggers.loggerEnd(unPaidStudentsList);
			
			List<TransportationFee>t=(List<TransportationFee>) unPaidStudentsList.get("unpaidStudentsList");
			System.out.println("paid fee "+t.get(0).getPaidFee());
			if (!unPaidStudentsList.isEmpty()) {
			responseMap.put("unpaidList", unPaidStudentsList);
			responseMap.put("status", 200);
			responseMap.put("message", "success");
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
		
		} else {
			responseMap.put("status", 404);
			responseMap.put("message", "data not found");
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
		}
		

	}
	
	
	
	@RequestMapping(value = "/studentpaidfee/{smartId}/{academicYear}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, ArrayList<TransportationFee>>> getTransportationFeeList(@PathVariable("smartId") String smartId,@PathVariable("academicYear") String academicYear,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		Token tokenObj = (Token) httpSession.getAttribute("token");

		str.length();
		Map<String, ArrayList<TransportationFee>> jsonMap = new HashMap<String, ArrayList<TransportationFee>>();
		
		TransportationFee transportationFee =new TransportationFee();
		transportationFee.setAcademicYear(academicYear);
		transportationFee.setSmartId(smartId);
		Map<String, Object> responseMap = new HashMap<>();

		

			ArrayList<TransportationFee> feetransportationList = (ArrayList<TransportationFee>) transpotationFeeservice.getTransportationFeeList(transportationFee,tokenObj.getHierarchy().getHid());

			if (feetransportationList.size() != 0) {
				jsonMap.put("result", feetransportationList);
				responseMap.put("data", jsonMap);
				responseMap.put("status", 200);
				responseMap.put("message", "success");
				Loggers.loggerEnd();
			} else {
				jsonMap.put("result", null);
				
				Loggers.loggerEnd();
			}

		
		return new ResponseEntity<Map<String, ArrayList<TransportationFee>>>(jsonMap, HttpStatus.OK);

	}
	@RequestMapping(value = "/transstudentunpaidfee/{smartId}/{academicYear}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, ArrayList<TransportationFee>>> getStudentUnpaid(@PathVariable("smartId") String smartId,@PathVariable("academicYear") String academicYear,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		Token tokenObj = (Token) httpSession.getAttribute("token");

		str.length();
		Map<String, ArrayList<TransportationFee>> jsonMap1 = new HashMap<String, ArrayList<TransportationFee>>();
		
		TransportationFee transportationFee =new TransportationFee();
		transportationFee.setAcademicYear(academicYear);
		transportationFee.setSmartId(smartId);
		Map<String, Object> responseMap = new HashMap<>();

		

			ArrayList<TransportationFee> feeList = (ArrayList<TransportationFee>) transpotationFeeservice.getStudentUnpaidFeeList(transportationFee,tokenObj.getHierarchy().getHid());

			if (feeList.size() != 0) {
				jsonMap1.put("result", feeList);
				responseMap.put("data", jsonMap1);
				responseMap.put("status", 200);
				responseMap.put("message", "success");
				Loggers.loggerEnd();
			} else {
				jsonMap1.put("result", null);
				Loggers.loggerEnd();
			}

		
		return new ResponseEntity<Map<String, ArrayList<TransportationFee>>>(jsonMap1, HttpStatus.OK);

	}


	
	
	
}
