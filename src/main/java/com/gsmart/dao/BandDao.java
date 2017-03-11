package com.gsmart.dao;

import java.util.List;
import java.util.Map;

import com.gsmart.model.Band;
import com.gsmart.model.CompoundBand;
import com.gsmart.util.GSmartDatabaseException;
/**
 * 
 * Defines the behavior of all services provided in {@link BandServicesImpl}
 * The functionalities are implemented in {@link BandDaoImpl}
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-02-23  
 */
public interface BandDao {
	
	/**
	 * @return list of Band entities available in the {@link Band} Table
	 * @throws GSmartDatabaseException
	 */
	public Map<String, Object> getBandList(int min, int max) throws GSmartDatabaseException;
	
	/**
	 * @param band instanceOf {@link Band}
	 * @return Nothing
	 * @throws GSmartDatabaseException
	 */

	public CompoundBand addBand(Band band) throws GSmartDatabaseException;
	
	/**
	 * @param band instanceOf {@link Band}
	 * @return Nothing
	 * @throws GSmartDatabaseException
	 * @throws Exception 
	 */

	public Band editBand(Band band) throws GSmartDatabaseException, Exception;

	/**
	 * @param band instanceOf {@link Band}
	 * @return Nothing
	 * @throws GSmartDatabaseException
	 */

	public void deleteBand(Band band)throws GSmartDatabaseException;
	
	public Band getMaxband() throws GSmartDatabaseException;

	public List<Band> getBandList1() throws GSmartDatabaseException;

}
