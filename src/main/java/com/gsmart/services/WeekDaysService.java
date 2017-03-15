package com.gsmart.services;

import java.util.List;

import com.gsmart.model.WeekDays;
import com.gsmart.util.GSmartServiceException;

public interface WeekDaysService {
	
	public List<WeekDays> getWeekDaysList(long hid)throws GSmartServiceException;
	
	public boolean addWeekDaysList(WeekDays weekdays) throws GSmartServiceException;
	
	public void deleteWeekdaysList(WeekDays weekdays) throws GSmartServiceException;

	public void editWeekdaysList(WeekDays weekdays) throws GSmartServiceException;

}
