package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.InventoryAssignmentsDao;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Inventory;
import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.InventoryAssignmentsCompoundKey;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
@Transactional
public class InventoryAssignmentsServicesImpl implements InventoryAssignmentsServices {

	@Autowired
	private InventoryAssignmentsDao inventoryAssignmentsDao;

	

	@Override
	public Map<String, Object> getInventoryAssignList(String role, String smartid, Long hid, Integer min, Integer max) throws GSmartServiceException {
		try {
			return inventoryAssignmentsDao.getInventoryAssignList(role,smartid,hid, min, max);
		} catch (GSmartDatabaseException Exception) {
			throw (GSmartServiceException) Exception;

		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
	}

	@Override
	public InventoryAssignmentsCompoundKey addInventoryDetails(InventoryAssignments inventoryAssignments,InventoryAssignments oldInventory,Long hid) {
		InventoryAssignmentsCompoundKey compoundKey=null;
		try {
			Loggers.loggerStart(inventoryAssignments);
			compoundKey=inventoryAssignmentsDao.addInventoryDetails(inventoryAssignments,oldInventory,hid);
			Loggers.loggerEnd(compoundKey);
		} catch (GSmartDatabaseException e) {
			e.printStackTrace();
		}
		return compoundKey;
	}

	@Override
	public InventoryAssignments editInventoryDetails(InventoryAssignments inventoryAssignments) {
		InventoryAssignments ab = null;
		try {
			ab = inventoryAssignmentsDao.editInventoryDetails(inventoryAssignments);
		} catch (GSmartDatabaseException e) {
			e.printStackTrace();
		}
		return ab;
	}

	@Override
	public void deleteInventoryDetails(InventoryAssignments inventoryAssignments) {
		try {
			Loggers.loggerStart();
			Logger.getLogger(InventoryAssignmentsServicesImpl.class)
					.info("navigating to dao impl to delete the record with entry time : "
							+ inventoryAssignments.getEntryTime());
			inventoryAssignmentsDao.deleteInventoryDetails(inventoryAssignments);
		} catch (GSmartDatabaseException e) {
			Logger.getLogger(InventoryAssignmentsServicesImpl.class)
					.info("tried navigating the record with entry time : " + inventoryAssignments.getEntryTime()
							+ " but ended with exception");
			e.printStackTrace();
		}
	}

	@Override
	public List<InventoryAssignments> getInventoryDashboardData(ArrayList<String> smartIdList, Hierarchy hierarchy)
			throws GSmartServiceException {
		return inventoryAssignmentsDao.getInventoryDashboardData(smartIdList, hierarchy);
	}

	@Override
	public List<InventoryAssignments> groupCategoryAndItem(List<InventoryAssignments> inventoryAssignmentList,
			List<Inventory> inventory) throws GSmartServiceException {
		Loggers.loggerStart();
		List<InventoryAssignments> responseList = new ArrayList<>();
		for (int i = 0; i < inventory.size(); i++) {
			Loggers.loggerStart("inventory loop : " + i);
			InventoryAssignments inAssignments = new InventoryAssignments();
			int totalQuantity = 0;
			for (int j = 0; j < inventoryAssignmentList.size(); j++) {
				Loggers.loggerStart("inventoryAssignmentList loop : " + j);
				if (inventory.get(i).getCategory().equalsIgnoreCase(inventoryAssignmentList.get(j).getCategory())) {
					if (inventory.get(i).getItemType().equalsIgnoreCase(inventoryAssignmentList.get(j).getItemType())) {
						Loggers.loggerStart("inAssignments adding inventory : " + inventory.get(i).getItemType());
						inAssignments.setCategory(inventory.get(i).getCategory());
						inAssignments.setItemType(inventory.get(i).getItemType());
						totalQuantity += inventoryAssignmentList.get(j).getQuantity();
						inAssignments.setQuantity(totalQuantity);
						inAssignments.setTotalQuantity(inventory.get(i).getQuantity());
						System.out.println("inAssignments"+inAssignments);
					}
				}
			}
			if (inAssignments.getCategory() != null) {
				responseList.add(inAssignments);
			}
		}
		Loggers.loggerEnd(responseList);
		return responseList;
	}

	@Override
	public Map<String, Object> getInventoryList(String role, Long hid, Integer min, Integer max)
			throws GSmartServiceException {
		// TODO Auto-generated method stub
		return null;
	}
}
