package com.gsmart.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GSmartExceptionHandler{
	
	final Logger logger = Logger.getLogger(GSmartExceptionHandler.class);
	
	@ExceptionHandler(value=GSmartServiceException.class)
	public ResponseEntity<IAMResponse> handleGSmartServiceException(HttpServletRequest request, Exception ex){
		logger.info("GSmartServiceException Occured:: URL="+request.getRequestURL());
		IAMResponse resp = new IAMResponse();
		if(Constants.CONSTRAINT_VIOLATION.equals(ex.getMessage())){
		resp.setMessage(ex.getMessage());
		}
		else if(Constants.NULL_PONITER.equals(ex.getMessage())){
			resp.setMessage("Session Expired");
		}
		return new ResponseEntity<IAMResponse>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
