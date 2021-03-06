package com.gsmart.dao;

import com.gsmart.model.Login;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;

public interface TokenDao {

	public void saveToken(Token token, Login login) throws GSmartDatabaseException;
	
	public Token getToken(String tokenNumber) throws GSmartDatabaseException;
	
	public void deleteToken(String tokenNumber) throws GSmartDatabaseException;
	
}
