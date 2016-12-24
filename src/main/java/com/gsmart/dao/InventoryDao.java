
package com.gsmart.dao;

import java.util.List;


import com.gsmart.model.CompoundInventory;
import com.gsmart.model.Inventory;
import com.gsmart.util.GSmartDatabaseException;

/**
 * 
 * Defines the behavior of all services provided in {@link InventoryServicesImpl}
 * The functionalities are implemented in {@link InventoryServicesImpl}
 * 
 * @author :
 * @version 1.0
 * @since 2016-08-01  
 */

public interface InventoryDao {
	
	/**
	 * @return list of inventory entities available in the {@link INVENTORY} Table
	 * @throws GSmartDatabaseException
	 */
	
	public List<Inventory>getInventoryList() throws GSmartDatabaseException;
	/**
	 * @param inventory instanceOf {@link Inventory}
	 * @return Nothing
	 * @throws  user define Exception
	 */
	
	
	public CompoundInventory addInventory(Inventory inventory)throws GSmartDatabaseException;

	/**
	 * @param inventory instanceOf {@link Inventory}
	 * @return Nothing
	 * @throws  user define Exception
	 */
	public void editInventory(Inventory inventory)throws GSmartDatabaseException;
	
public	void deleteInventory(Inventory inventory) throws GSmartDatabaseException;
	
	/**
	 * @param timeStamp instanceOf {@link Inventory}
	 * @return Nothing
	 * @throws  user define Exception
	 */
	
	

}
