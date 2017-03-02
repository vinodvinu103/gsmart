package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.loader.plan.build.spi.QuerySpaceTreePrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gsmart.model.CompoundInventory;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Inventory;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

/**
 * provides the implementation for the methods available in {@link InventoryDao}
 * interface
 * 
 * @author :
 * @version 1.0
 * @since 2016-08-01
 */
@Repository
public class InventoryDaoImpl implements InventoryDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction transaction = null;
	Query query;

	/**
	 * to view the list of records available in {@link Inventory} table
	 * 
	 * @return list of inventory entities available in Inventory
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<Inventory> getInventoryList(String role, Hierarchy hierarchy) throws GSmartDatabaseException {
		getconnection();
		Loggers.loggerStart();

		List<Inventory> inventoryList;
		try {
			if (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director")) {
				query = session.createQuery("from Inventory where isActive='Y' ");
			} else {
				query = session.createQuery("from Inventory where isActive='Y' and hierarchy.hid=:hierarchy ");
				query.setParameter("hierarchy", hierarchy.getHid());
			}

			inventoryList = query.list();

		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());

		} finally {
			session.close();
		}
		Loggers.loggerEnd(inventoryList);
		return inventoryList;
	}

	/**
	 * Adds new inventory entity to {@link Inventory} save it in database
	 * 
	 * @param inventory
	 *            instance of Inventory
	 * @return Nothing
	 */

	@Override
	public CompoundInventory addInventory(Inventory inventory) throws GSmartDatabaseException {

		getconnection();
		Loggers.loggerStart();

		CompoundInventory cb = null;

		try {
			getconnection();
			query = session.createQuery(
					"FROM Inventory WHERE category=:category AND quantity=:quantity AND itemType=:itemType AND isActive=:isActive");
			Hierarchy hierarchy = inventory.getHierarchy();
			query = session.createQuery(
					"FROM Inventory WHERE category=:category AND itemType=:itemType AND isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("category", inventory.getCategory());
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameter("itemType", inventory.getItemType());
			query.setParameter("quantity", inventory.getQuantity());
			query.setParameter("isActive", "Y");
			Inventory inventory2 = (Inventory) query.uniqueResult();
			if (inventory2 == null) {
				inventory.setEntryTime((CalendarCalculator.getTimeStamp()));
				inventory.setIsActive("Y");
				cb = (CompoundInventory) session.save(inventory);
			}

			transaction.commit();
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		return cb;
	}

	/**
	 * persists the updated inventory instance
	 * 
	 * @param inventory
	 *            instance of {@link Inventory}
	 * @return Nothing
	 */
	@Override
	public void editInventory(Inventory inventory) throws GSmartDatabaseException {
		getconnection();
		Loggers.loggerStart();
		try {

			Inventory oldInvertory = getInventory(inventory.getEntryTime(), inventory.getHierarchy());
			oldInvertory.setIsActive("N");
			oldInvertory.setUpdateTime(CalendarCalculator.getTimeStamp());
			session.update(oldInvertory);
			inventory.setEntryTime(CalendarCalculator.getTimeStamp());
			inventory.setIsActive("Y");
			session.save(inventory);
			transaction.commit();

		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());

		} finally {
			session.close();
		}
	}

	/**
	 * removes the inventory entity from the database.
	 * 
	 * @param inventory
	 *            instanceOf {@link Inventory}
	 * @return Nothing
	 */

	public Inventory getInventory(String entryTime, Hierarchy hierarchy) {
		try {

			query = session.createQuery(
					"from Inventory where isactive='Y' and entryTime=:entryTime and hierarchy.hid=:hierarchy");
			query.setParameter("entryTime", entryTime);
			query.setParameter("hierarchy", hierarchy.getHid());
			Inventory inventry = (Inventory) query.uniqueResult();

			return inventry;

			/*
			 * query=session.
			 * createQuery("from Holiday where isactive='Y' and entryTime='"
			 * +entryTime+"'"); ArrayList<Holiday> holi=(ArrayList<Holiday>)
			 * query.list(); return holi.get(0);
			 */

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * @Override query=session.
	 * createQuery("from Holiday where isactive='Y' and entryTime='"+entryTime+
	 * "'"); ArrayList<Holiday> holi=(ArrayList<Holiday>) query.list(); return
	 * holi.get(0);
	 */
	@Override
	public void deleteInventory(Inventory inventory) throws GSmartDatabaseException {
		getconnection();
		Loggers.loggerStart();

		try {

			inventory.setExitTime(CalendarCalculator.getTimeStamp());
			inventory.setIsActive("D");
			session.update(inventory);
			transaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
	}

	/**
	 * create instance for session and begins transaction
	 */

	private void getconnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();

	}

	@SuppressWarnings("unused")
	private String InventoryCount() {
		Loggers.loggerStart();
		try {
			query = session.createQuery("from Inventory where isactive='Y' and quantity=quantity");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return null;
	}


}
