package com.gsmart.services;

import java.util.List;

import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.InventoryAssignmentsCompoundKey;
import com.gsmart.util.GSmartServiceException;

public interface InventoryAssignmentsServices {

	public List<InventoryAssignments> getInventoryList() throws GSmartServiceException;

	public InventoryAssignmentsCompoundKey addInventoryDetails(InventoryAssignments inventoryAssignments);

	public InventoryAssignments editInventoryDetails(InventoryAssignments inventoryAssignments);

	public void deleteInventoryDetails(InventoryAssignments inventoryAssignments);

}