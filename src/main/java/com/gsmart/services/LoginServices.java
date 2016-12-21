package com.gsmart.services;

import java.util.Map;

import com.gsmart.model.Login;
import com.gsmart.util.GSmartServiceException;

public interface LoginServices {

	public Map<String, Object> authenticate(Login login, String tokenNumber) throws GSmartServiceException;

	
}
