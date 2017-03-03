package com.gsmart.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.InventoryAssignmentsDao;
import com.gsmart.dao.InventoryDaoImpl;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Inventory;
//import com.gsmart.model.Hierarchy;
import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.InventoryAssignmentsCompoundKey;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class InventoryAssignmentsServicesImpl implements InventoryAssignmentsServices {

	@Autowired
	InventoryAssignmentsDao inventoryAssignmentsDao;
	
	@Autowired
	InventoryDaoImpl inventoryDao;

	@Override
	public List<InventoryAssignments> getInventoryList(String role,Hierarchy hierarchy) throws GSmartServiceException {
		try {
			return inventoryAssignmentsDao.getInventoryList(role,hierarchy);
		} catch (GSmartDatabaseException Exception) {
			throw (GSmartServiceException) Exception;

		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
	}

	@Override
	public InventoryAssignmentsCompoundKey addInventoryDetails(InventoryAssignments inventoryAssignments,InventoryAssignments oldInventory) {
		InventoryAssignmentsCompoundKey compoundKey=null;
		try {
			Loggers.loggerStart(inventoryAssignments);
			compoundKey=inventoryAssignmentsDao.addInventoryDetails(inventoryAssignments,oldInventory);
			Loggers.loggerEnd(compoundKey);
		} catch (GSmartDatabaseException e) {
			e.printStackTrace();
		}
		return compoundKey;
	}

	@Override
	public InventoryAssignments editInventoryDetails(InventoryAssignments inventoryAssignments) {
		InventoryAssignments ab=null;
		try {
			ab=inventoryAssignmentsDao.editInventoryDetails(inventoryAssignments);
		} catch (GSmartDatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ab;
	}

	@Override
	public void deleteInventoryDetails(InventoryAssignments inventoryAssignments) {
		try {
			Loggers.loggerStart();
			Logger.getLogger(InventoryAssignmentsServicesImpl.class).info("navigating to dao impl to delete the record with entry time : " + inventoryAssignments.getEntryTime());
			inventoryAssignmentsDao.deleteInventoryDetails(inventoryAssignments);
		} catch (GSmartDatabaseException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(InventoryAssignmentsServicesImpl.class).info("tried navigating the record with entry time : " + inventoryAssignments.getEntryTime() + " but ended with exception");
			e.printStackTrace();
		}
	}

	
}
