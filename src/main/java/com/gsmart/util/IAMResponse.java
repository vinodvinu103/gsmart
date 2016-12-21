package com.gsmart.util;

import org.apache.log4j.Logger;

public class IAMResponse {

	Logger logger = Logger.getLogger(IAMResponse.class);

	public IAMResponse() {

	}

	public IAMResponse(String msg) {
		this.message = msg;
	}

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
		
	}
