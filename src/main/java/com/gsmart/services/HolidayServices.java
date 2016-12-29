package com.gsmart.services;

import java.util.List;

import com.gsmart.model.CompoundHoliday;
import com.gsmart.model.Holiday;
import com.gsmart.util.GSmartServiceException;
/**
 * Provides services for {@link HolidayController}.
 * The functionalities are defined in {@link HolidayServicesImpl}
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */

public interface HolidayServices {
	/**
	 * @return list of Holiday entities available in the Holiday Table
	 * @throws GSmartServiceException
	 */

	public List<Holiday> getHolidayList() throws GSmartServiceException;
	/**
	 * @param holiday instanceOf {@link Holiday}
	 * @return nothing
	 * @throws GSmartServiceException
	 */
	public CompoundHoliday addHoliday(Holiday holiday) throws GSmartServiceException;
	/**
	 * @param holiday instanceOf {@link Holiday}
	 * @return nothing
	 * @throws GSmartServiceException
	 */
	public void editHoliday(Holiday holiday) throws GSmartServiceException;
	/**
	 * @param holiday instanceOf {@link Holiday}
	 * @return nothing
	 * @throws GSmartServiceException
	 */
	public void deleteHoliday(Holiday holiday)throws GSmartServiceException;

}
