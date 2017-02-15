package com.gsmart.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.PasswordDao;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Login;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;
@Service
public class PasswordServicesImpl implements PasswordServices {
	
	@Autowired
	PasswordDao passwordDao;

	@Override
	public void setPassword(Login login,Hierarchy hierarchy)throws GSmartServiceException {
		
		passwordDao.setPassword(login,hierarchy);
		
	}
	
	@Override
	public boolean changePassword(Login login, String smartId,Hierarchy hierarchy) throws GSmartServiceException {
		
	return passwordDao.changePassword(login, smartId,hierarchy);
		
	}	
	
	@Override
	public Profile emailLink(String email) throws GSmartServiceException {
		Loggers.loggerStart();
		return passwordDao.emailLink(email);
	}
	
}
