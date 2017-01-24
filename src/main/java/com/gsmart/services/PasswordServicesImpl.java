package com.gsmart.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.PasswordDao;
import com.gsmart.model.Login;
import com.gsmart.util.GSmartServiceException;
@Service
public class PasswordServicesImpl implements PasswordServices {
	
	@Autowired
	PasswordDao passwordDao;

	@Override
	public void setPassword(Login login)throws GSmartServiceException {
		passwordDao.setPassword(login);
		
	}

	@Override
	public boolean changePassword(Login login, String smartId) throws GSmartServiceException {
		
	return passwordDao.changePassword(login, smartId);
		
	}	
	
}
