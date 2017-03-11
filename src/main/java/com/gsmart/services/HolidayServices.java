package com.gsmart.services;


import java.util.Map;

import com.gsmart.model.CompoundHoliday;
import com.gsmart.model.Hierarchy;
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

	public Map<String, Object> getHolidayList(Long hid, Integer min, Integer max) throws GSmartServiceException;

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
	public Holiday editHoliday(Holiday holiday) throws GSmartServiceException;
	/**
	 * @param holiday instanceOf {@link Holiday}
	 * @return nothing
	 * @throws GSmartServiceException
	 */
	public void deleteHoliday(Holiday holiday)throws GSmartServiceException;
	

}
