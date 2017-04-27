package com.gsmart.dao;

import com.gsmart.model.Profile;

public interface PushNotificationDao {
	
public void sendNotificationpresent();
	
	public void sendNotificationabsent();

	public void storeDeviceToken(Profile profile);

}
