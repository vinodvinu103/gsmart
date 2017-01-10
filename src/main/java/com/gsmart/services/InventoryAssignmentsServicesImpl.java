package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.InventoryAssignmentsDao;
import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.InventoryAssignmentsCompoundKey;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;

@Service
public class InventoryAssignmentsServicesImpl implements InventoryAssignmentsServices {

	@Autowired
	InventoryAssignmentsDao inventoryAssignmentsDao;

	@Override
	public List<InventoryAssignments> getInventoryList() throws GSmartServiceException {
		try {
			return inventoryAssignmentsDao.getInventoryList();
		} catch (GSmartDatabaseException Exception) {
			throw (GSmartServiceException) Exception;

		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
	}

	@Override
	public InventoryAssignmentsCompoundKey addInventoryDetails(InventoryAssignments inventoryAssignments) {
		InventoryAssignmentsCompoundKey compoundKey=null;
		try {
			compoundKey=inventoryAssignmentsDao.addInventoryDetails(inventoryAssignments);
		} catch (GSmartDatabaseException e) {
			// TODO Auto-generated catch block
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
			inventoryAssignmentsDao.deleteInventoryDetails(inventoryAssignments);
		} catch (GSmartDatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
