package com.gsmart.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.TokenDao;
import com.gsmart.model.Login;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	TokenDao tokenDAO;

	public void saveToken(Token token, Login loginObj) throws GSmartServiceException {
		try {
			tokenDAO.saveToken(token, loginObj);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
	}

	@Override
	public Token getToken(String tokenNumber) throws GSmartServiceException {
		
		Token token = null;
		try {
			token = tokenDAO.getToken(tokenNumber);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		
		return token;
	}

	@Override
	public void deleteToken(String tokenNumber) throws GSmartServiceException {

		Loggers.loggerStart();
		try {
			tokenDAO.deleteToken(tokenNumber);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
	}

}
