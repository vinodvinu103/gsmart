package com.gsmart.dao;

import java.util.List;
import java.util.Map;

import com.gsmart.model.CompoundFeeMaster;
import com.gsmart.model.FeeMaster;
import com.gsmart.util.GSmartDatabaseException;

/**
 * 
 * Defines the behavior of all services provided in
 * {@link FeeMasterServicesImpl} The functionalities are implemented in
 * {@link FeeMasterDaoImpl}
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */
public interface FeeMasterDao {

	/**
	 * @return list of entities available in the {@link FeeMaster} Table
	 * @throws GSmartDatabaseException
	 */
	public Map<String, Object> getFeeList(Long hid, int min, int max) throws GSmartDatabaseException;

	/**
	 * @param feeMaster
	 *            instanceOf {@link FeeMaster}
	 * @return Nothing
	 * @throws GSmartDatabaseException
	 */
	public CompoundFeeMaster addFee(FeeMaster feeMaster) throws GSmartDatabaseException;

	/**
	 * @param feeMaster
	 *            instanceOf {@link FeeMaster}
	 * @return Nothing
	 * @throws GSmartDatabaseException
	 */

	public FeeMaster editFee(FeeMaster feeMaster) throws GSmartDatabaseException;

	/**
	 * @param timeStamp
	 * @return Nothing
	 * @throws GSmartDatabaseException
	 */

	public void deleteFee(FeeMaster feeMaster) throws GSmartDatabaseException;

	public FeeMaster getFeeStructure(String standard,Long hid) throws GSmartDatabaseException ;

	public List<FeeMaster> searchfeemaster(FeeMaster feemaster, Long hid)throws GSmartDatabaseException;

	/*public void fileUpload(FileUpload fileUpload) throws GSmartDatabaseException;*/

	/*public List getFile(String fileName)throws GSmartDatabaseException;*/

}