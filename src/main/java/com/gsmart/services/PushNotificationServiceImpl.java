package com.gsmart.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.PushNotificationDao;
import com.gsmart.model.Profile;

@Service
@Transactional
public class PushNotificationServiceImpl implements PushNotificationService {

	@Autowired
	private PushNotificationDao pushDao;
	@Override
	public void storeDeviceToken(Profile profile) {
		pushDao.storeDeviceToken(profile);		
	}
	
	

}
