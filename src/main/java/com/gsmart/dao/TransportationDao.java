package com.gsmart.dao;
import java.util.List;
import com.gsmart.model.Transportation;
import com.gsmart.util.GSmartDatabaseException;

public interface TransportationDao{
	
	public List<Transportation> getTransportationfeeList(Long hid) throws GSmartDatabaseException;

	public Transportation addTransportation(Transportation transportation) throws GSmartDatabaseException;

	public Transportation editTransportationFee(Transportation transportation) throws GSmartDatabaseException;


	
}