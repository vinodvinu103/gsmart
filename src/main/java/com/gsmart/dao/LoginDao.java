package com.gsmart.dao;

import java.util.List;
import java.util.Map;

import com.gsmart.model.Login;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartDatabaseException;

public interface LoginDao {

	public Map<String, Object> authenticate(Login login) throws GSmartDatabaseException;
	
	public List<Profile> getAllRecord();

	public void unlockAccounts();
}
