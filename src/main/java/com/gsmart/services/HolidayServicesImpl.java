package com.gsmart.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.HolidayDao;
import com.gsmart.model.CompoundHoliday;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Holiday;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;
/**
 * Provides implementation for services declared in {@link HolidayServices}
 * interface. it will go to {@link HolidayDao}
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */
@Service
public class HolidayServicesImpl implements HolidayServices {

	
final Logger logger = Logger.getLogger(HolidayServicesImpl.class);
	
	@Autowired
	private HolidayDao holidayDao;
		
	/**
	 * @return calls {@link HolidayDao}'s <code>getHolidayList()</code> method
	 */
	@Override
	public List<Holiday> getHolidayList(String role,Hierarchy hierarchy) throws GSmartServiceException {
	
		Loggers.loggerStart();
	try {
		return holidayDao.getHolidayList(role,hierarchy);
	} catch (GSmartDatabaseException exception) {
		throw (GSmartServiceException) exception;
	} catch (Exception e) {
		throw new GSmartServiceException(e.getMessage());
	}

	}
	/**
	 * calls {@link HolidayDao}'s <code>addHoliday(...)</code> method
	 * 
	 * @param holiday
	 *            an instance of {@link Holiday} class
	 * @throws GSmartServiceException
	 */
	@Override
	public CompoundHoliday addHoliday(Holiday holiday) throws GSmartServiceException {
		Loggers.loggerStart();
		
		CompoundHoliday ch = null;
		
		try {
			ch = holidayDao.addHoliday(holiday);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return ch;
	}
	/**
	 * calls {@link HolidayDao}'s <code>editHoliday(...)</code> method
	 * 
	 * @param holiday
	 *            an instance of {@link Holiday} class
	 * @throws GSmartServiceException
	 */
	@Override
	public void editHoliday(Holiday holiday) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			holidayDao.editHoliday(holiday);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
	}
	/**
	 * calls {@link HolidayDao}'s <code>deleteHoliday(...)</code> method
	 * 
	 * @param holiday
	 *            an instance of {@link Holiday} class
	 * @throws GSmartServiceException
	 */
	@Override
	public void deleteHoliday(Holiday holiday) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			holidayDao.deleteHoliday(holiday);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();

	}

	
}
