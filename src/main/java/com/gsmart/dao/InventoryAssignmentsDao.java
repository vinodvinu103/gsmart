package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.InventoryAssignmentsCompoundKey;
import com.gsmart.util.GSmartDatabaseException;

public interface InventoryAssignmentsDao {

	public List<InventoryAssignments> getInventoryList(String role,Hierarchy hierarchy) throws GSmartDatabaseException;

	public InventoryAssignmentsCompoundKey addInventoryDetails(InventoryAssignments inventoryAssignments) throws GSmartDatabaseException;

	public InventoryAssignments editInventoryDetails(InventoryAssignments inventoryAssignments) throws GSmartDatabaseException;
	
	public void deleteInventoryDetails(InventoryAssignments inventoryAssignments)throws GSmartDatabaseException;
	
	public List<InventoryAssignments> getInventoryDashboardData(ArrayList<String> smartIdList, Hierarchy hierarchy)
			throws GSmartDatabaseException;
	
}
