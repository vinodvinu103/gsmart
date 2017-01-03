package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.CompoundHoliday;
import com.gsmart.model.Holiday;
import com.gsmart.util.GSmartDatabaseException;
/**
 * 
 * Defines the behavior of all services provided in {@link HolidayServicesImpl}
 * The functionalities are implemented in {@link HolidayDaoImpl}
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01 
 */

public interface HolidayDao {
	/**
	 * @return list of Holiday entities available in the {@link Holiday} Table
	 * @throws GSmartDatabaseException
	 */

	public List<Holiday> getHolidayList() throws GSmartDatabaseException;


	/**
	 * @param holiday instanceOf {@link Holiday}
	 * @return Nothing
	 * @throws GSmartDatabaseException
	 */

	public CompoundHoliday addHoliday(Holiday holiday) throws GSmartDatabaseException;
	/**
	 * @param holiday instanceOf {@link Holiday}
	 * @return Nothing
	 * @throws GSmartDatabaseException
	 */

	public void editHoliday(Holiday holiday) throws GSmartDatabaseException;
	/**
	 * @param holiday instanceOf {@link Holiday}
	 * @return Nothing
	 * @throws GSmartDatabaseException
	 */

	public void deleteHoliday(Holiday holiday)throws GSmartDatabaseException;

}