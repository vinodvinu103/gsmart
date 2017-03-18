package com.gsmart.dao;


import java.util.List;

import com.gsmart.model.WeekDays;
import com.gsmart.util.GSmartDatabaseException;

public interface WeekDaysDao {
	

	public List<WeekDays> getWeekList()throws GSmartDatabaseException ;
	
	public void addWeekDays(WeekDays weekdays) throws GSmartDatabaseException;
    
 
    public void editweekdays(WeekDays weekdays) throws GSmartDatabaseException;
    
    public void deleteweekdays(WeekDays weekdays) throws GSmartDatabaseException;
    
    public List<WeekDays> getWeekdaysForHoliday(String school, String institution) throws GSmartDatabaseException;
 
}