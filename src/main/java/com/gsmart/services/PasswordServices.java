package com.gsmart.services;


import com.gsmart.model.Hierarchy;
import com.gsmart.model.Login;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartServiceException;

public interface PasswordServices {

	public void setPassword(Login login,Hierarchy hierarchy) throws GSmartServiceException;
	
	public boolean changePassword(Login login, String smartId,Hierarchy hierarchy) throws GSmartServiceException;

	public Profile forgotPassword(String email,Hierarchy hierarchy) throws GSmartServiceException;
	
}
