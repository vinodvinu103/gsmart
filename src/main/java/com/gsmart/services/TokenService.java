package com.gsmart.services;

import com.gsmart.model.Token;
import com.gsmart.util.GSmartServiceException;

public interface TokenService {

	public void saveToken(Token token) throws GSmartServiceException;
	
	public Token getToken(String tokenNumber) throws GSmartServiceException;
	
	public void deleteToken(String tokenNumber) throws GSmartServiceException;
}
