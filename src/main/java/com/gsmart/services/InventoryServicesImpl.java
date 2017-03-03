package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.InventoryDao;
import com.gsmart.model.CompoundInventory;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Inventory;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

/**
 * Provides implementation for services declared in {@link InventoryServices}
 * interface. it will go to {@link inventoryDao}
 * 
 * @author :
 * @version 1.0
 * @since 2016-08-01
 */
@Service
public class InventoryServicesImpl implements InventoryServices {

	@Autowired
	private InventoryDao inventoryDao;

	/**
	 * @return calls {@link InventoryDao}'s <code>getInventoryList()</code>
	 *         method
	 * @see List
	 */

	@Override
	public Map<String, Object> getInventoryList(String role, Hierarchy hierarchy, int min, int max)
			throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			Loggers.loggerEnd();
			return inventoryDao.getInventoryList(role, hierarchy, min, max);
		} catch (GSmartDatabaseException Exception) {
			throw (GSmartServiceException) Exception;

		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}

	}

	/**
	 * calls {@link InventoryDao}'s <code>addInventory(...)</code> method
	 * 
	 * @param inventory
	 *            an instance of {@link Inventory} class
	 * @throws user
	 *             define Exception
	 */
	@Override
	public CompoundInventory addInventory(Inventory inventory) throws GSmartServiceException {
		Loggers.loggerStart();
		CompoundInventory cb;
		try {
			cb = inventoryDao.addInventory(inventory);

		} catch (GSmartDatabaseException exception) {

			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cb;
	}

	/**
	 * calls {@link InventoryDao}'s <code>editInventory(...)</code> method
	 * 
	 * @param inventory
	 *            an instance of {@link Inventory} class
	 * @throws user
	 *             define Exception
	 */

	@Override
	public void editInventory(Inventory inventory) throws GSmartServiceException {
		Loggers.loggerStart();
		try {
			inventoryDao.editInventory(inventory);

		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {

			throw new GSmartServiceException(e.getMessage());

		}

		Loggers.loggerEnd();
	}

	/**
	 * calls {@link InventoryDao}'s <code>deleteInventory(...)</code> method
	 * 
	 * @param inventory
	 *            an instance of {@link Inventory} class
	 * @throws user
	 *             define Exception
	 */

	public void deleteInventory(Inventory inventory) throws GSmartServiceException {

		Loggers.loggerStart();
		try {
			inventoryDao.deleteInventory(inventory);

		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}

		Loggers.loggerEnd();

	}

	@Override
	public List<Inventory> getInventoryList(String role, Hierarchy hierarchy) throws GSmartServiceException {
		return inventoryDao.getInventoryList(role, hierarchy);
	}

}
