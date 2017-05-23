package com.gsmart.services;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class HolidayServicesImpl implements HolidayServices {

	
final Logger logger = Logger.getLogger(HolidayServicesImpl.class);

@Autowired
private HolidayDao holidayDao;
	
/*public void setHolidayDaoImpl(HolidayDao holidayDao) {
	this.holidayDao = holidayDao;
}*/
		
	/**
	 * @return calls {@link HolidayDao}'s <code>getHolidayList()</code> method
	 */
	@Override
	public Map<String, Object> getHolidayList(Long hid, Integer min, Integer max) throws GSmartServiceException {
	
		Loggers.loggerStart();
	try {
		return holidayDao.getHolidayList(hid,min,max);

	} catch (GSmartDatabaseException exception) {
		exception.printStackTrace();
		throw (GSmartServiceException) exception;
	} catch (Exception e) {
		e.printStackTrace();
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
	public Holiday editHoliday(Holiday holiday) throws GSmartServiceException {
		Loggers.loggerStart();
		Holiday ch=null;
		try {
			ch=holidayDao.editHoliday(holiday);
		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
		return ch;
		
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
	@Override
	public List<Holiday> searchHoliday(Holiday holiday, Long hid) throws GSmartServiceException {
			return holidayDao.searchHoliday(holiday, hid);	
	}

	
}
