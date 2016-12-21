package com.gsmart.dao;

import com.gsmart.model.Login;
import com.gsmart.util.GSmartDatabaseException;

public interface LoginDao {

	public int authenticate(Login login) throws GSmartDatabaseException;

}
