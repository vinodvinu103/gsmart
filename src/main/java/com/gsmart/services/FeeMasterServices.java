package com.gsmart.services;

import java.util.List;
import java.util.Map;

import com.gsmart.model.CompoundFeeMaster;
import com.gsmart.model.FeeMaster;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartServiceException;
/**
 * Provides services for {@link FeeMasterController}.
 * The functionalities are defined in {@link FeeMasterServicesImpl}
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */

public interface FeeMasterServices 
{
	/**
	 * @return list of feeMaster entities available in the FeeMaster Table
	 * @throws GSmartServiceException
	 */

	public Map<String, Object> getFeeList(String role,Hierarchy hierarchy, int min, int max) throws GSmartServiceException;
	/**
	 * @param feeMaster instanceOf {@link FeeMaster}
	 * @return nothing
	 * @throws GSmartServiceException
	 */

	public CompoundFeeMaster addFee(FeeMaster feeMaster) throws GSmartServiceException;
	/**
	 * @param feeMaster instanceOf {@link FeeMaster}
	 * @return nothing
	 * @throws GSmartServiceException
	 */

	public FeeMaster editFee(FeeMaster feeMaster) throws GSmartServiceException;
	/**
	 * @param feeMaster instanceOf {@link FeeMaster}
	 * @return nothing
	 * @throws GSmartServiceException
	 */
	public void deleteFee(FeeMaster feeMaster)throws GSmartServiceException;
	
	
	
	
	public FeeMaster getFeeStructure(String standard,String role,Hierarchy hierarchy) throws GSmartServiceException;
	
	/*public void fileUpload(FileUpload fileUpload)throws GSmartServiceException;*/
	
	/*public List getFile(String fileName) throws GSmartServiceException;*/
	
}