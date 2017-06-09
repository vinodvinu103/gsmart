package com.gsmart.util;


public class IAMResponse {

	
	private String message;
	private int statusCode;

	public IAMResponse() {

	}

	public IAMResponse(String msg) {
		this.message = msg;
	}
	
	public IAMResponse(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
		
	}
