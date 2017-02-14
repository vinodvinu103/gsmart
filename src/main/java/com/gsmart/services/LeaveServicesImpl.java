package com.gsmart.services;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.HolidayDaoImpl;
import com.gsmart.dao.LeaveDao;
import com.gsmart.model.CompoundLeave;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Holiday;
import com.gsmart.model.Leave;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class LeaveServicesImpl implements LeaveServices {
	
	@Autowired
	private LeaveDao leaveDao;
	
	@Autowired
	HolidayDaoImpl getholidaylist;
	
	
	@Override
	public Map<String, Object> getLeaveList(String role,Hierarchy hierarchy, int min, int max) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			return leaveDao.getLeaveList(role, hierarchy, min, max);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
			throw new GSmartServiceException(e.getMessage());

		}
	}

	public void editLeave(Leave leave) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			leaveDao.editLeave(leave);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
			throw new GSmartServiceException(e.getMessage());

		}
	}

	@Override
	public CompoundLeave addLeave(Leave leave, Integer noOfdays, String role, Hierarchy hierarchy, int min, int max)
			throws GSmartServiceException {
		Loggers.loggerStart();
		CompoundLeave cl = null;
		Map<String, Object> list = getholidaylist.getHolidayList(role, hierarchy, 1, 1);
		try {
			Calendar startCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();

			Calendar holidayDate = Calendar.getInstance();

			startCal.setTime(leave.getStartDate());
			endCal.setTime(leave.getEndDate());
			int work = getWorkingDaysBetweenTwoDates(startCal, endCal);
			List<Object> holidayList = (List<Object>) list.get("holidayList");
			for (Object holidayObj : holidayList) {
				Holiday holiday = (Holiday) holidayObj;
				holidayDate.setTime(holiday.getHolidayDate());
				if (startCal.getTimeInMillis() <= holidayDate.getTimeInMillis()
						&& holidayDate.getTimeInMillis() <= endCal.getTimeInMillis()) {
					if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
							&& holidayDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
							|| holidayDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
						continue;
					} else if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
							|| holidayDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {

						work--;

					} else {

						work--;
					}

				}

			}
			cl = leaveDao.addLeave(leave, work);

		} catch (GSmartDatabaseException exception) {
			exception.printStackTrace();
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
			// Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cl;
		// return null;
	}

public  int getWorkingDaysBetweenTwoDates(Calendar startCal, Calendar endCal) throws ParseException, GSmartDatabaseException {
	Loggers.loggerStart();
    
   
    

    
	int workDays = 0;

    //Return 0 if start and end are the same
    if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
        return 0;
    }

    if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
    	Calendar temp = Calendar.getInstance();
        temp = startCal;
        startCal = endCal;
        endCal = temp;
    }
   
    while (startCal.getTimeInMillis() < endCal.getTimeInMillis()){
    
       //excluding start date
        
        if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            ++workDays;
            
            	}
        startCal.add(Calendar.DAY_OF_MONTH, 1);
    }
    if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) 
        ++workDays;
    
    Loggers.loggerEnd();
    return workDays;
    }
@Override
public void deleteLeave(Leave leave) throws GSmartServiceException {
	Loggers.loggerStart();
	try {
		
		leaveDao.deleteLeave(leave);
	} catch (GSmartDatabaseException exception) {
		throw (GSmartServiceException) exception;
	} catch (Exception e) {
		Loggers.loggerException(e.getMessage());
		throw new GSmartServiceException(e.getMessage());
		
	}
	Loggers.loggerEnd();
	
}

    
}   





