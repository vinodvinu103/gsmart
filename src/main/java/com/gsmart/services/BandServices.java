package com.gsmart.services;

import java.util.List;

import com.gsmart.model.Band;
import com.gsmart.model.CompoundBand;
import com.gsmart.util.GSmartServiceException;

/**
 * Provides services for {@link BandController}.
 * The functionalities are defined in {@link BandServicesImpl}
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-02-23
 */
public interface BandServices {
	
	/**
	 * @return list of Band entities available in the Band Table
	 * @throws GSmartServiceException
	 */
	public List<Band> getBandList() throws GSmartServiceException;
	
	/**
	 * @param band instanceOf {@link Band}
	 * @return nothing
	 * @throws GSmartServiceException
	 */
	public CompoundBand addBand(Band band) throws GSmartServiceException;
	
	/**
	 * @param band instanceOf {@link Band}
	 * @return nothing 
	 * @throws GSmartServiceException
	 */
	public Band editBand(Band band) throws GSmartServiceException;
	
	/**
	 * @param band instanceOf {@link Band}
	 * @return nothing
	 * @throws GSmartServiceException
	 */
	public void deleteBand(Band band)throws GSmartServiceException;

}
