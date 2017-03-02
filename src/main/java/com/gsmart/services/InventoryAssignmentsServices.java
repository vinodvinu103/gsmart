package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.Inventory;
import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.InventoryAssignmentsCompoundKey;
import com.gsmart.util.GSmartServiceException;

public interface InventoryAssignmentsServices {

	public List<InventoryAssignments> getInventoryList(String role, Hierarchy hierarchy) throws GSmartServiceException;
	//public List<InventoryAssignments> getInventoryList(String role,Hierarchy hierarchy) throws GSmartServiceException;

	public InventoryAssignmentsCompoundKey addInventoryDetails(InventoryAssignments inventoryAssignments);

	public InventoryAssignments editInventoryDetails(InventoryAssignments inventoryAssignments);

	public void deleteInventoryDetails(InventoryAssignments inventoryAssignments);
	
	public List<InventoryAssignments>getInventoryDashboardData(ArrayList<String> smartIdList,Hierarchy hierarchy) throws GSmartServiceException;
	
	public List<InventoryAssignments>groupCategoryAndItem(List<InventoryAssignments> inventoryAssignments, List<Inventory> inventory) throws GSmartServiceException;
	
}
