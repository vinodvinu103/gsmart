package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.InventoryAssignmentsCompoundKey;
import com.gsmart.util.GSmartDatabaseException;

public interface InventoryAssignmentsDao {

	public List<InventoryAssignments> getInventoryList() throws GSmartDatabaseException;

	public InventoryAssignmentsCompoundKey addInventoryDetails(InventoryAssignments inventoryAssignments) throws GSmartDatabaseException;

	public InventoryAssignments editInventoryDetails(InventoryAssignments inventoryAssignments) throws GSmartDatabaseException;
	
	public void deleteInventoryDetails(InventoryAssignments inventoryAssignments)throws GSmartDatabaseException;

}