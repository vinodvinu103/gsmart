package com.gsmart.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.PayRoll;
import com.gsmart.services.PayRollService;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(value="/payroll")
public class PayRollController {

	/*@Autowired
	HolidayServices holidayServices;
	
	@Autowired
	LeaveMasterService leaveMasterService;
	*/
	
	@Autowired
	private GetAuthorization getAuthorization;
	
	
   @Autowired
   private PayRollService payrollService;
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getPayrollList(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException{
		//Map<String, Object> resp=new HashMap<>();
		Map<String, Object>resp= payrollService.getPayroll();
		if(!resp.isEmpty()){
			resp.put("status", 200);
			resp.put("message", "success");
			
		}else{
			resp.put("status", 404);
			resp.put("message", "No Data Is Present");
			
		}
		Loggers.loggerEnd();
		//resp.put("payrollList", payrollList);
		return new ResponseEntity<Map<String,Object>>(resp, HttpStatus.OK);	
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addPayrollList(@RequestBody PayRoll payroll,@RequestHeader HttpHeaders token, HttpSession httpSession) 
		throws GSmartBaseException{
		Loggers.loggerStart();
		Map<String,Object> resp=new HashMap<>();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
	    str.length();
	    PayRoll payRollObj=null;

        if(getAuthorization.authorizationForPost(tokenNumber, httpSession)){
        	payRollObj=payrollService.addPayroll(payroll);
		
	        if(payRollObj!=null)
	        {
	        	resp.put("status", 200);
	        	resp.put("message", "Saved Successfully");
	        }else{
		    	resp.put("status", 400);
	        	resp.put("message", "Data Already Exist, Please try with SomeOther Data");
		    }    	
        }else{
        	resp.put("status", 403);
        	resp.put("message", "Permission Denied");
               }
		
		Loggers.loggerEnd();
		return new ResponseEntity<Map<String,Object>>(resp,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/put",method=RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> editPayrollList(@RequestBody PayRoll payroll)
	throws GSmartBaseException{
		Map<String, Object> resp=new HashMap<String, Object>();
		PayRoll pay=null;
		
		/*String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		if(getAuthorization.authorizationForPost(tokenNumber, httpSession)){*/
		//Loggers.loggerStart(payroll);
		
	    pay=payrollService.editPayroll(payroll);

		if(pay!=null){
			resp.put("status", 200);
			resp.put("message", "updated successfully");
			
		}else{
			resp.put("status", 400);
			resp.put("message", "Data Already Exist, Please try with some other Data");
		}
		/*}else{
			resp.put("staus",403);
			resp.put("message", "permission Denied");
		}*/
		return new ResponseEntity<Map<String,Object>>(resp,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> deletePayrollList(@RequestBody PayRoll payroll)
			throws GSmartBaseException{
		Map<String , Object> resp= new HashMap<>();
		/*String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();
		
		if(getAuthorization.authorizationForPsut(tokenNumber, httpSession)){*/
		payrollService.deletePayroll(payroll);
		resp.put("status",200);
		resp.put("message", "successfully Deleted");
		/*}*/
	return new ResponseEntity<Map<String,Object>>(resp,HttpStatus.OK);
		
	}
}
