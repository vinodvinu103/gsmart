package com.gsmart.dao;


import com.gsmart.model.Hierarchy;
import com.gsmart.model.Login;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartDatabaseException;

public interface PasswordDao {

	public void setPassword(Login login) throws GSmartDatabaseException;

	
	public boolean changePassword(Login login, String smartId,Hierarchy hierarchy) throws GSmartDatabaseException;


	public Profile emailLink(String email) throws GSmartDatabaseException;

}
