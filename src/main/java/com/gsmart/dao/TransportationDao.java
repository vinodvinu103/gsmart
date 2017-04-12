package com.gsmart.dao;
import java.util.List;
import java.util.Map;

import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Transportation;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;

public interface TransportationDao{
	
	public List<Transportation> getTransportationfeeList(Long hid) throws GSmartDatabaseException;

	public Transportation addTransportation(Transportation transportation) throws GSmartDatabaseException;

	public Transportation editTransportationFee(Transportation transportation) throws GSmartDatabaseException;


	
}