package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.InventoryAssignmentsCompoundKey;
import com.gsmart.util.GSmartDatabaseException;

public interface InventoryAssignmentsDao {

	public Map<String, Object> getInventoryAssignList(String role,Hierarchy hierarchy, Integer min, Integer max) throws GSmartDatabaseException;

	public InventoryAssignmentsCompoundKey addInventoryDetails(InventoryAssignments inventoryAssignments) throws GSmartDatabaseException;

	public InventoryAssignments editInventoryDetails(InventoryAssignments inventoryAssignments) throws GSmartDatabaseException;
	
	public void deleteInventoryDetails(InventoryAssignments inventoryAssignments)throws GSmartDatabaseException;
	
	public List<InventoryAssignments> getInventoryDashboardData(ArrayList<String> smartIdList, Hierarchy hierarchy)
			throws GSmartDatabaseException;
	
}
