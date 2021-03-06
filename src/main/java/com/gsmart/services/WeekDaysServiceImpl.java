package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.WeekDaysDao;
import com.gsmart.model.WeekDays;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;
@Service
@Transactional
public class WeekDaysServiceImpl implements WeekDaysService{
	
	@Autowired
	private WeekDaysDao weekday;
	
	@Override
	public List<WeekDays> getWeekDaysList(long hid) throws GSmartServiceException {
		Loggers.loggerStart();
		List<WeekDays> list=new ArrayList<>();
		try {
     		
			 list= weekday.getWeekList(hid);
			
			 /*for (WeekDays weekDays : list) {
					String wk=weekDays.getWeekDay();
					String day=null;
						switch(wk) {
						case "1":
							day="SUNDAY";
							break;

						case "2":
							day="MONDAY";
							break;
							
						case "3":
							day="TUESDAY";
							break;
						case "4":
							day="WEDNESDAY";
							break;
						case "5":
							day="THURSDAY";
							break;
						case "6":
							day="FRIDAY";
							break;
						case "7":
							day="SATURDAY";
							break;
						}
					
					weekDays.setWeekDay(day);
					System.out.println("weekdays +++++"+weekDays.getWeekDay());
					}*/
			Loggers.loggerEnd(list);
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
	public boolean addWeekDaysList(WeekDays weekdays) throws GSmartServiceException {
		Loggers.loggerStart();
		
		boolean status=false;
		
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
		default:
			break;
		}
		weekdays.setWeekDay(day);
		status = weekday.addWeekDays(weekdays);	
		Loggers.loggerEnd();
		
	    return status;
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
		default:
			break;
		}
		weekdays.setWeekDay(day);

	

		Loggers.loggerEnd();
	
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
