package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.Login;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartDatabaseException;

public interface LoginDao {

	public int authenticate(Login login) throws GSmartDatabaseException;
	
	public List<Profile> getAllRecord();
}
