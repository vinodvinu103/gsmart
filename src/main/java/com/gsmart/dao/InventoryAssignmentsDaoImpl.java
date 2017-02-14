package com.gsmart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class InventoryAssignmentsDaoImpl implements InventoryAssignmentsDao
{

	/*public static Logger logger = Logger.getLogger(InventoryAssignmentsDaoImpl.class);*/
	@Autowired
	SessionFactory sessionFactory;
	Session session=null;
	Query query;
	Transaction tx=null;
	

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getInventoryAssignList(String role,Hierarchy hierarchy, Integer min, Integer max) throws GSmartDatabaseException 
	{
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
		inventoryList = criteria.list();
		Criteria criteriaCount = session.createCriteria(InventoryAssignments.class);
		criteriaCount.setProjection(Projections.rowCount());
		Long count = (Long) criteriaCount.uniqueResult();
		inventoryassignMap.put("totalinventoryassign", query.list().size());
		 Loggers.loggerEnd();
		 inventoryassignMap.put("inventoryList", inventoryList);
		return inventoryassignMap;
		}
		catch(Throwable e)
		{
			throw new GSmartDatabaseException(e.getMessage());
		} finally {

			session.close();
		}
		
		
	}

	@Override
	public InventoryAssignmentsCompoundKey addInventoryDetails(InventoryAssignments inventoryAssignments)throws GSmartDatabaseException
	{
		Loggers.loggerStart();
		getConnection();
		InventoryAssignmentsCompoundKey ch = null;
		try
		{
		
		Loggers.loggerValue("inside the dao of add inventory details : ", inventoryAssignments.getQuantity());
		inventoryAssignments.setEntryTime(CalendarCalculator.getTimeStamp());
		inventoryAssignments.setIsActive("Y");
		ch=(InventoryAssignmentsCompoundKey) session.save(inventoryAssignments);
		tx.commit();
		
	    } catch (ConstraintViolationException e) {
		throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		return ch;
	}


	@Override
	public InventoryAssignments editInventoryDetails(InventoryAssignments inventoryAssignments)throws GSmartDatabaseException
	{
		try{
	
		InventoryAssignments oldInventory = getInventory(inventoryAssignments.getEntryTime(),inventoryAssignments.getHierarchy());
		if(oldInventory!=null){
	        oldInventory.setIsActive("N");
			oldInventory.setUpdatedTime(CalendarCalculator.getTimeStamp());
		    session.update(oldInventory);
		    
		    inventoryAssignments.setIsActive("Y");
		    inventoryAssignments.setUpdatedTime(CalendarCalculator.getTimeStamp());
		    addInventoryDetails(inventoryAssignments);
		    
		}
		}
		catch(Throwable e) {
					throw new GSmartDatabaseException(e.getMessage());
			
		}
		    
		return inventoryAssignments;
			
}
		   
		
		
		
		
		
		/*updateInventory(oldInventory,"N");
		inventoryAssignments.setUpdatedTime(CalendarCalculator.getTimeStamp());
		addInventoryDetails(inventoryAssignments);
*/	
	
	
	private InventoryAssignments getInventory(String entryTime,Hierarchy hierarchy)throws GSmartDatabaseException
	{
		Loggers.loggerStart();
		getConnection();
		try
		{
			
			query=session.createQuery("from InventoryAssignments where isActive='Y' and ENTRY_TIME='"+entryTime+"' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			InventoryAssignments oldInventory=(InventoryAssignments) query.uniqueResult();
			Loggers.loggerEnd();
			return oldInventory;
		}
		catch(Throwable e)
		{
	
			throw new GSmartDatabaseException(e.getMessage());
			
		}
	
	}


	/*private void updateInventory(InventoryAssignments oldInventory, String isActive) 
	{
		try
		{
			getConnection();
			oldInventory.setIsActive(isActive);
			oldInventory.setUpdatedTime(CalendarCalculator.getTimeStamp());
			session.update(oldInventory);
			tx.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			session.close();
		}
	}*/

	@Override
	public void deleteInventoryDetails(InventoryAssignments inventoryAssignments)throws GSmartDatabaseException  
	{
		Loggers.loggerStart();
		getConnection();
		
		try
		{
			
			Logger.getLogger(InventoryAssignmentsDaoImpl.class).info("trying to delete the record with entry time as : " + inventoryAssignments.getEntryTime());
			inventoryAssignments.setIsActive("D");
			inventoryAssignments.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(inventoryAssignments);
			tx.commit();
		}
		catch(Exception e)
		{	
			Logger.getLogger(InventoryAssignmentsDaoImpl.class).info("trying to delete the record with entry time as : " + inventoryAssignments.getEntryTime() + " and ended with exception : " + e);
			e.printStackTrace();
		}
		finally 
		{
			session.close();
		}

	}
	
	public void getConnection() {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
	}


}
