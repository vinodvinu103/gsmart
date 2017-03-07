package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gsmart.model.CompoundInventory;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Inventory;
import com.gsmart.util.GSmartServiceException;
/**
 * Provides services for {@link InventoryController}
 * The functionalities are defined in {@link InventoryServicesImpl}  
 * @author :
 * @version 1.0
 * @since 2016-08-01  
 */

public interface InventoryServices {
	
	/**
	 * @return list of inventory entities available in the INVENTORY Table
	 * @throws user define Exception
	 * @see List
	 */
	
	public Map<String, Object> getInventoryList(Long hid, int min, int max) throws GSmartServiceException;
	
	public List<Inventory> getInventoryList(String role,Hierarchy hierarchy) throws GSmartServiceException;
	
	/**
	 * @param inventory instanceOf {@link Inventory}
	 * @return nothing
	 * @throws user define Exception
	 */
	public CompoundInventory addInventory(Inventory inventory) throws GSmartServiceException;
	
	/**
	 * @param inventory instanceOf {@link Inventory}
	 * @return nothing
	 * @throws user define Exception
	 */
	public Inventory editInventory(Inventory inventory)throws GSmartServiceException;
	
	/**
	 * @param timeStamp instanceOf {@link Inventory}
	 * @return nothing
	 * @throws user define Exception
	 */
	public void deleteInventory(Inventory inventory) throws GSmartServiceException;
	
	
	
	
	
	
}
