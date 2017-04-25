package com.gsmart.services;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Transportation;
import com.gsmart.util.GSmartServiceException;
 
 public interface TransportationService {
		/**
		 * @return list of Hierarchy entities available in the Hierarchy Table
		 * @throws GSmartServiceException
		 */	
		//public Map<String, Object> getTransportationList1() throws GSmartServiceException;

		//public List<Transportation> getTransportationFeeList() throws GSmartServiceException;
		
		public List<Transportation> getTransportationFeeList(Long hid) throws GSmartServiceException;

		public Transportation addDetails(Transportation transportation) throws GSmartServiceException ;

		public Transportation editTransportationFee(Transportation transportation) throws GSmartServiceException ;

	//	public void deleteTransportationFee(Transportation transportation);
		
		/*public List<Hierarchy> getHierarchyList1(String role,Hierarchy hierarchy) throws GSmartServiceException;
		*//**
		 * @param hierarchy
		 *            instanceOf {@link Hierarchy}
		 * @return nothing
		 * @throws GSmartServiceException
		 *//*
		public boolean addHierarchy(Hierarchy hierarchy) throws GSmartServiceException;

		*//**
		 * @param hierarchy
		 *            instanceOf {@link Hierarchy}
		 * @return nothing
		 * @throws GSmartServiceException
		 *//*
		public Hierarchy editHierarchy(Hierarchy hierarchy) throws GSmartServiceException;

		*//**
		 * @param hierarchy
		 *            instanceOf {@link Hierarchy}
		 * @return nothing
		 * @throws GSmartServiceException
		 *//*
		public void deleteHierarchy(Hierarchy hierarchy)throws GSmartServiceException;
		
		public Hierarchy getHierarchyByHid(Long hid)throws GSmartServiceException;
		public List<Hierarchy> getAllHierarchy();
		
		*/
		
	}  
 