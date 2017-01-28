package com.gsmart.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.CompoundHierarchy;
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
	public List<InventoryAssignments> getInventoryList() throws GSmartDatabaseException 
	{
		Loggers.loggerStart();
		List<InventoryAssignments> inventoryList=null;
		try
		{
		getConnection();
		query=session.createQuery("FROM InventoryAssignments WHERE isActive='Y'");
		inventoryList=(List<InventoryAssignments>)query.list();
		 
		 Loggers.loggerEnd();
		return inventoryList;
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
		InventoryAssignmentsCompoundKey ch = null;
		try
		{
		getConnection();
		Loggers.loggerValue("inside the dao of add inventory details : ", inventoryAssignments.getQuantity());
		inventoryAssignments.setEntryTime(CalendarCalculator.getTimeStamp());
		inventoryAssignments.setIsActive("Y");
		ch=(InventoryAssignmentsCompoundKey) session.save(inventoryAssignments);
		tx.commit();
		System.out.println("SAVED DATA "+ch);
		String cat=inventoryAssignments.getCategory();
		String item=inventoryAssignments.getItemType();
		int numofQuantity=inventoryAssignments.getQuantity();
		updateInventory(cat, item, numofQuantity);
		System.out.println("data is updated   :  "  +ch);
		session.close();
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		Loggers.loggerEnd();
		return ch;
	}


	@Override
	public InventoryAssignments editInventoryDetails(InventoryAssignments inventoryAssignments)throws GSmartDatabaseException
	{
		try{
	
		InventoryAssignments oldInventory = getInventory(inventoryAssignments.getEntryTime());
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
	
	
	private InventoryAssignments getInventory(String entryTime)throws GSmartDatabaseException
	{
		Loggers.loggerStart();
		try
		{
			getConnection();
			query=session.createQuery("from InventoryAssignments where isActive='Y' and ENTRY_TIME='"+entryTime+"'");
			InventoryAssignments oldInventory=(InventoryAssignments) query.uniqueResult();
			Loggers.loggerEnd();
			return oldInventory;
		}
		catch(Throwable e)
		{
	
			throw new GSmartDatabaseException(e.getMessage());
			
		}
	
	}


	private void updateInventory(String cat, String item,int leftQuantity) 
	{	
		Loggers.loggerStart();
		//int leftQuantity=0;
		Inventory inventory=null;
		try
		{   
			getConnection();
			query=session.createQuery("from Inventory where category=:category and itemType=:itemType and isActive='Y' ");
			query.setParameter("category", cat);
			query.setParameter("itemType", item);
			inventory=(Inventory) query.uniqueResult();
			int numOfLeftQunt=inventory.getLeftQuantity();
			inventory.setLeftQuantity(numOfLeftQunt-leftQuantity);
			inventory.setUpdateTime(CalendarCalculator.getTimeStamp());
			session.update(inventory);
			tx.commit();
			Loggers.loggerEnd();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			session.close();
		}
	}

	@Override
	public void deleteInventoryDetails(InventoryAssignments inventoryAssignments)throws GSmartDatabaseException  
	{
		
		try
		{
			getConnection();
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
