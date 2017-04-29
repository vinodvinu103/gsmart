package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gsmart.model.Assign;
import com.gsmart.model.CompoundInventoryAssignmentsStudent;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Inventory;
import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.InventoryAssignmentsCompoundKey;
import com.gsmart.model.InventoryAssignmentsStudent;
import com.gsmart.util.GSmartDatabaseException;

public interface InventoryAssignmentsDao {


	public Map<String, Object> getInventoryAssignList(String role, String smartId, Long hid, Integer min, Integer max) throws GSmartDatabaseException;

	public Map<String, Object> getInventoryAssignStudentList(String role,Long hid, Integer min, Integer max) throws GSmartDatabaseException; 
	
	public InventoryAssignmentsCompoundKey addInventoryDetails(InventoryAssignments inventoryAssignments,InventoryAssignments oldInventory,Long hid) throws GSmartDatabaseException;

	public CompoundInventoryAssignmentsStudent addInventoryStudent(InventoryAssignmentsStudent inventoryAssignmentsStudent,InventoryAssignmentsStudent oldInventoryAssignment,String reportingManagerId,Long hid) throws GSmartDatabaseException;
	
	public InventoryAssignments editInventoryDetails(InventoryAssignments inventoryAssignments) throws GSmartDatabaseException;
	
	public InventoryAssignmentsStudent editInventoryStudentDetails(InventoryAssignmentsStudent inventoryAssignmentsStudent) throws GSmartDatabaseException;
	
	public void deleteInventoryDetails(InventoryAssignments inventoryAssignments)throws GSmartDatabaseException;
	
	public void deleteInventoryStudentDetails(InventoryAssignmentsStudent inventoryAssignmentsStudent) throws GSmartDatabaseException;
	
	public List<InventoryAssignments> getInventoryDashboardData(ArrayList<String> smartIdList, Hierarchy hierarchy)
			throws GSmartDatabaseException;


	public List<InventoryAssignments> getInventoryStudentList(Long hid)throws GSmartDatabaseException;
}
