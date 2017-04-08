package com.gsmart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class InventoryDaoImpl implements InventoryDao {

	@Autowired
	private SessionFactory sessionFactory;

	Query query;

	/**
	 * to view the list of records available in {@link Inventory} table
	 * 
	 * @return list of inventory entities available in Inventory
	 */

	
	@SuppressWarnings("unchecked")
	@Override

	public Map<String, Object> getInventoryList(Long hid, int min, int max) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Map<String, Object> inventoryMap = new HashMap<String, Object>();
		List<Inventory> inventoryList;
		Criteria criteria = null;
		try {

			criteria = sessionFactory.getCurrentSession().createCriteria(Inventory.class);
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("hierarchy.hid", hid));
			criteria.setFirstResult(min);
			criteria.setMaxResults(max);
			// criteria.setProjection(Projections.id());
			inventoryList = criteria.list();
			Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(Inventory.class);
			criteriaCount.add(Restrictions.eq("isActive", "Y"));
			criteriaCount.add(Restrictions.eq("hierarchy.hid", hid));
			criteriaCount.setProjection(Projections.rowCount());
			Long count = (Long) criteriaCount.uniqueResult();
			inventoryMap.put("totalinventory", count);

		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());

		} 
		Loggers.loggerEnd(inventoryList);
		inventoryMap.put("inventoryList", inventoryList);
		return inventoryMap;
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

		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();

		CompoundInventory cb = null;

		try {
			Inventory inventory2=fetch(inventory);
			if (inventory2 ==null) {
				inventory.setEntryTime((CalendarCalculator.getTimeStamp()));
				inventory.setIsActive("Y");
				cb = (CompoundInventory) session.save(inventory);
			}

		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd(inventory);
		return cb;
	}

	public Inventory fetch(Inventory inventory) {
		Inventory inventory2 = null;
		Inventory inve =null;
		try {
			System.out.println("i am going "+ inventory);
			Hierarchy hierarchy = inventory.getHierarchy();
			query = sessionFactory.getCurrentSession().createQuery(
					"from Inventory WHERE category=:category and itemType=:itemType and isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("category", inventory.getCategory());
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameter("itemType", inventory.getItemType());
			query.setParameter("isActive", "Y");
			inventory2 = (Inventory) query.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return inventory2;
	}

	/**
	 * persists the updated inventory instance
	 * 
	 * @param inventory
	 *            instance of {@link Inventory}
	 * @return Nothing
	 */
	@Override
	public Inventory editInventory(Inventory inventory) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Inventory ch = null;
		int diffQuantity =0;
		try {
			Inventory oldInvertory = getInventory(inventory.getEntryTime(),inventory.getHierarchy());
			if(inventory.getQuantity()<oldInvertory.getQuantity())
			{
				diffQuantity =oldInvertory.getQuantity()-inventory.getQuantity();
				System.out.println("Difference........"+diffQuantity);
				inventory.setLeftQuantity(inventory.getLeftQuantity()-diffQuantity);
			}else{
				
				diffQuantity=inventory.getQuantity()- oldInvertory.getQuantity();
				System.out.println("get difference between them >>>>>>"+diffQuantity);
				inventory.setLeftQuantity(inventory.getLeftQuantity()+diffQuantity);
				
			}
			ch=updateInventory(oldInvertory, inventory);
			
			addInventory(inventory);
	

		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());

		}
		
		Loggers.loggerEnd();
		return ch;
	}
	
	private Inventory updateInventory(Inventory oldInventory, Inventory inventory) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		Inventory ch = null;

			try {
				if(oldInventory.getCategory().equals(inventory.getCategory()) && oldInventory.getItemType().equals(inventory.getItemType())){
					oldInventory.setUpdateTime(CalendarCalculator.getTimeStamp());
					oldInventory.setIsActive("N");
					session.update(oldInventory);
					return oldInventory;
				}else{
					Inventory inventory2=fetch(inventory);
					if(inventory2==null){
						oldInventory.setUpdateTime(CalendarCalculator.getTimeStamp());
						oldInventory.setIsActive("N");
						session.update(oldInventory);
						return oldInventory;
						}
					}
				
			} catch (ConstraintViolationException e) {
				throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
			} catch (Throwable e) {
				throw new GSmartDatabaseException(e.getMessage());
			}
			Loggers.loggerEnd();
			return ch;

		}



	/**/
	/**
	 * removes the inventory entity from the database.
	 * 
	 * @param inventory
	 *            instanceOf {@link Inventory}
	 * @return Nothing
	 */

	public Inventory getInventory(String entryTime, Hierarchy hierarchy) {
		try {

			query = sessionFactory.getCurrentSession().createQuery("from Inventory where isactive='Y' and entryTime=:entryTime and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameter("entryTime", entryTime);
		query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameter("entryTime", entryTime);
			Inventory inventry = (Inventory) query.uniqueResult();

			return inventry;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public void deleteInventory(Inventory inventory) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();

		try {

			inventory.setExitTime(CalendarCalculator.getTimeStamp());
			inventory.setIsActive("D");
			session.update(inventory);

		} catch (Exception e) {
			e.printStackTrace();
		} 
		Loggers.loggerEnd();
	}

	/**
	 * create instance for session and begins transaction
	 */

	/*private void getconnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();

	}*/

	@SuppressWarnings("unused")
	private String InventoryCount() {
		Loggers.loggerStart();
		try {
			query = sessionFactory.getCurrentSession().createQuery("from Inventory where isactive='Y' and quantity=quantity");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override

	public List<Inventory> getInventoryList(String role,Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart();

		List<Inventory> inventoryList=null;
		try {
			query = sessionFactory.getCurrentSession().createQuery("from Inventory where isActive='Y' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			inventoryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd(inventoryList);
		return inventoryList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Inventory> getInventory(Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Inventory> inventory = null;
		try {
			if(hid != null){
			query = sessionFactory.getCurrentSession().createQuery("from Inventory where isActive='Y' and hid=:hierarchy");
			query.setParameter("hierarchy",hid);
			}
			else
			{
				query = sessionFactory.getCurrentSession().createQuery("from Inventory where isActive='Y' ");
			}
			
			inventory = query.list();
		} catch (Exception e) {
			e.printStackTrace();

		}
		Loggers.loggerEnd("inveList:"+inventory);
		return inventory;
	}
}
