package com.gsmart.services;

import com.gsmart.model.Login;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartServiceException;

public interface PasswordServices {

	public void setPassword(Login login) throws GSmartServiceException;
	
	

	public Profile forgotPassword(String email) throws GSmartServiceException;
}
