package com.gsmart.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.WeekDaysDao;
import com.gsmart.model.WeekDays;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;
@Service
public class WeekDaysServiceImpl implements WeekDaysService{
	
	@Autowired
	WeekDaysDao weekday;
	
	@Override
	public List<WeekDays> getWeekDaysList() throws GSmartServiceException {
		Loggers.loggerStart();
		List<WeekDays> list=null;
		try {
     		
			 list= weekday.getWeekList();
			
			 return list;
		}  catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
	}	

	@Override
	public void addWeekDaysList(WeekDays weekdays) throws GSmartServiceException {
		Loggers.loggerStart();
		String day=weekdays.getWeekDay();
		String wd=day.toUpperCase();
		switch(wd) {
		case "SUNDAY":
			day="1";
			break;

		case "MONDAY":
			day="2";
			break;
			
		case "TUESDAY":
			day="3";
			break;
		case "WEDNESDAY":
			day="4";
			break;
		case "THURSDAY":
			day="5";
			break;
		case "FRIDAY":
			day="6";
			break;
		case "SATURDAY":
			day="7";
			break;
		}
		weekdays.setWeekDay(day);
		weekday.addWeekDays(weekdays);	
		Loggers.loggerEnd();
	
	}

	@Override
	public void editWeekdaysList(WeekDays weekdays) throws GSmartServiceException {
		Loggers.loggerStart();
		try{
	weekday.editweekdays(weekdays);
		}catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		
	}

	@Override
	public void deleteWeekdaysList(WeekDays weekdays) throws GSmartServiceException {
		Loggers.loggerStart();
		try{
	weekday.deleteweekdays(weekdays);
		}catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		
	}
	
	
}
