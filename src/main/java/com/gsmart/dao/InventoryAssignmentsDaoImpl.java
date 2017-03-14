package com.gsmart.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Assign;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Inventory;
import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.InventoryAssignmentsCompoundKey;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class InventoryAssignmentsDaoImpl implements InventoryAssignmentsDao {

	/*
	 * public static Logger logger =
	 * Logger.getLogger(InventoryAssignmentsDaoImpl.class);
	 */
	@Autowired
	SessionFactory sessionFactory;
	Session session = null;
	Query query;
	Transaction tx = null;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getInventoryAssignList(String role,Hierarchy hierarchy, Integer min, Integer max) throws GSmartDatabaseException 
	{
		getConnection();
		Loggers.loggerStart();
		List<InventoryAssignments> inventoryList=null;
		Map<String, Object> inventoryassignMap = new HashMap<String, Object>();
		Criteria criteria = null;
		getConnection();
		try
		{
		if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director"))
		{
		query=session.createQuery("FROM InventoryAssignments WHERE isActive='Y'");
		}else{
			query=session.createQuery("FROM InventoryAssignments WHERE isActive='Y' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
		}
		criteria = session.createCriteria(InventoryAssignments.class);
		criteria.setMaxResults(max);
		criteria.setFirstResult(min);
		criteria.addOrder(Order.asc("standard"));
		inventoryList = criteria.list();
		Criteria criteriaCount = session.createCriteria(InventoryAssignments.class);
		criteriaCount.setProjection(Projections.rowCount());
		Long count = (Long) criteriaCount.uniqueResult();
		inventoryassignMap.put("totalinventoryassign", query.list().size());
		 Loggers.loggerEnd();
		 inventoryassignMap.put("inventoryList", inventoryList);
		
		}
		catch(Throwable e)
		{
			throw new GSmartDatabaseException(e.getMessage());
		} finally {

			session.close();
		}
		return inventoryassignMap;

		/*
		 * Loggers.loggerStart(); List<InventoryAssignments> inventoryList; try
		 * { getConnection(); query =
		 * session.createQuery("from Inventory where isActive='Y' ");
		 * inventoryList = query.list();
		 * 
		 * } catch (Exception e) { e.printStackTrace(); throw new
		 * GSmartDatabaseException(e.getMessage());
		 * 
		 * } finally { session.close(); } Loggers.loggerEnd(inventoryList);
		 * return inventoryList;
		 */
	}

	@Override
	public InventoryAssignmentsCompoundKey addInventoryDetails(InventoryAssignments inventoryAssignments)
			throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();

		InventoryAssignmentsCompoundKey ch = null;
		try {

			Loggers.loggerValue("inside the dao of add inventory details : ", inventoryAssignments.getQuantity());
			inventoryAssignments.setEntryTime(CalendarCalculator.getTimeStamp());
			inventoryAssignments.setIsActive("Y");
			System.out.println("SAVED DATA " + ch);
			String cat = inventoryAssignments.getCategory();
			String item = inventoryAssignments.getItemType();
			int numofQuantity = inventoryAssignments.getQuantity();
			if (updateInventory(cat, item, numofQuantity) == 200) {
				ch = (InventoryAssignmentsCompoundKey) session.save(inventoryAssignments);
				tx.commit();
			}
			System.out.println("data is updated   :  " + ch);
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return ch;
	}

	@Override
	public InventoryAssignments editInventoryDetails(InventoryAssignments inventoryAssignments)
			throws GSmartDatabaseException {
		try {

			InventoryAssignments oldInventory = getInventory(inventoryAssignments.getEntryTime(),
					inventoryAssignments.getHierarchy());
			if (oldInventory != null) {
				oldInventory.setIsActive("N");
				oldInventory.setUpdatedTime(CalendarCalculator.getTimeStamp());
				session.update(oldInventory);

				inventoryAssignments.setIsActive("Y");
				inventoryAssignments.setUpdatedTime(CalendarCalculator.getTimeStamp());
				addInventoryDetails(inventoryAssignments);

			}
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());

		}

		return inventoryAssignments;

	}

	/*
	 * updateInventory(oldInventory,"N");
	 * inventoryAssignments.setUpdatedTime(CalendarCalculator.getTimeStamp());
	 * addInventoryDetails(inventoryAssignments);
	 */

	private InventoryAssignments getInventory(String entryTime, Hierarchy hierarchy) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();

		try {

			query = session.createQuery("from InventoryAssignments where isActive='Y' and ENTRY_TIME='" + entryTime
					+ "' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			InventoryAssignments oldInventory = (InventoryAssignments) query.uniqueResult();
			Loggers.loggerEnd();
			return oldInventory;
		} catch (Throwable e) {

			throw new GSmartDatabaseException(e.getMessage());

		} finally {
			session.close();
		}

	}

	private int updateInventory(String cat, String item, int leftQuantity) {
		Loggers.loggerStart();
		// int leftQuantity=0;
		Inventory inventory = null;
		getConnection();
		query = session.createQuery("from Inventory where category=:category and itemType=:itemType and isActive='Y' ");
		query.setParameter("category", cat);
		query.setParameter("itemType", item);
		inventory = (Inventory) query.uniqueResult();
		int numOfLeftQunt = inventory.getLeftQuantity();
		if (numOfLeftQunt - leftQuantity < 0) {
			return 400;
		} else {
			inventory.setLeftQuantity(numOfLeftQunt - leftQuantity);
			inventory.setUpdateTime(CalendarCalculator.getTimeStamp());
			session.update(inventory);
			tx.commit();
			Loggers.loggerEnd();
			session.close();
			return 200;
		}

	}

	@Override
	public void deleteInventoryDetails(InventoryAssignments inventoryAssignments) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();

		try {

			Logger.getLogger(InventoryAssignmentsDaoImpl.class)
					.info("trying to delete the record with entry time as : " + inventoryAssignments.getEntryTime());
			inventoryAssignments.setIsActive("D");
			inventoryAssignments.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(inventoryAssignments);
			tx.commit();
		} catch (Exception e) {
			Logger.getLogger(InventoryAssignmentsDaoImpl.class).info("trying to delete the record with entry time as : "
					+ inventoryAssignments.getEntryTime() + " and ended with exception : " + e);
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	public void getConnection() {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InventoryAssignments> getInventoryDashboardData(ArrayList<String> smartIdList, Hierarchy hierarchy)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<InventoryAssignments> inventoryAssignmentList = null;
		try {
			getConnection();
			Loggers.loggerStart("smartIdList : " + smartIdList);
			query = session
					.createQuery("From InventoryAssignments where isActive=:isActive and hierarchy.hid=:hierarchy and smartId in (:smartIdList)");
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameterList("smartIdList", smartIdList);
			query.setParameter("isActive", "Y");
			inventoryAssignmentList =  query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		Loggers.loggerEnd(inventoryAssignmentList);
		return inventoryAssignmentList;
	}

}
