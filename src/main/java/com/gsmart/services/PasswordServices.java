package com.gsmart.services;


import com.gsmart.model.Login;
import com.gsmart.util.GSmartServiceException;

public interface PasswordServices {

	public void setPassword(Login login) throws GSmartServiceException;
	
	public boolean changePassword(Login login, String smartId) throws GSmartServiceException;

}
