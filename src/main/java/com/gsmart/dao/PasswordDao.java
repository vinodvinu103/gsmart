package com.gsmart.dao;

import com.gsmart.model.Login;
import com.gsmart.util.GSmartDatabaseException;

public interface PasswordDao {

	public void setPassword(Login login) throws GSmartDatabaseException;
	
	

}
