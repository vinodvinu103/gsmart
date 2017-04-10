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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.CompoundInventoryAssignmentsStudent;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Inventory;
import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.InventoryAssignmentsCompoundKey;
import com.gsmart.model.InventoryAssignmentsStudent;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class InventoryAssignmentsDaoImpl implements InventoryAssignmentsDao {

	@Autowired
	private SessionFactory sessionFactory;
	Query query;
	
	@Autowired
	private ProfileDao profileDao;
	

	@SuppressWarnings("unchecked")
	
	
	@Override
	public Map<String, Object> getInventoryAssignStudentList(String role, Hierarchy hierarchy, Integer min, Integer max)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		
		List<InventoryAssignmentsStudent> inventoryStudentList= null;
		Map<String, Object> inventoryassignStudentMap = new HashMap<String, Object>();
		Criteria criteria = null;
		criteria = sessionFactory.getCurrentSession().createCriteria(InventoryAssignmentsStudent.class);
		Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(InventoryAssignmentsStudent.class);
		try{
			if(role.equalsIgnoreCase("teacher") || role.equalsIgnoreCase("student"))
			{
				criteria.add(Restrictions.eq("isActive", "Y"));
				criteriaCount.add(Restrictions.eq("isActive", "Y"));
			}else{
				criteria.add(Restrictions.eq("isActive", "Y"));
				criteria.add(Restrictions.eq("hierarchy.hid", hierarchy.getHid()));
				criteriaCount.add(Restrictions.eq("isActive", "Y"));
				criteriaCount.add(Restrictions.eq("hierarchy.hid", hierarchy.getHid()));
			}
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			criteria.addOrder(Order.asc("standard"));
			inventoryStudentList = criteria.list();
			Loggers.loggerEnd("data base data >>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+ criteria);
			criteriaCount.setProjection(Projections.rowCount());
			inventoryassignStudentMap.put("totalinventoryassignstudent", criteriaCount.uniqueResult());
			inventoryassignStudentMap.put("inventoryStudentList", inventoryStudentList);
			Loggers.loggerEnd(inventoryStudentList);
		}catch(Throwable e)
		{
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd();
		return inventoryassignStudentMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getInventoryAssignList(String role, String smartId, Hierarchy hierarchy, Integer min, Integer max) throws GSmartDatabaseException 
	{
		Loggers.loggerStart();

		List<InventoryAssignments> inventoryList=null;
		Map<String, Object> inventoryassignMap = new HashMap<String, Object>();
		Criteria criteria = null;
		criteria = sessionFactory.getCurrentSession().createCriteria(InventoryAssignments.class);
		Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(InventoryAssignments.class);
		try
		{
		if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director"))
		{
		criteria.add(Restrictions.eq("isActive", "Y"));
		}else{
			criteriaCount.add(Restrictions.eq("isActive", "Y"));
			criteriaCount.add(Restrictions.eq("hierarchy.hid", hierarchy.getHid()));
			criteriaCount.add(Restrictions.eq("smartId",smartId));
		}
		
		criteria.setMaxResults(max);
		criteria.setFirstResult(min);
		criteria.addOrder(Order.asc("standard"));
		inventoryList = criteria.list();
		
		criteriaCount.setProjection(Projections.rowCount());
		inventoryassignMap.put("totalinventoryassign", criteriaCount.uniqueResult());
	    inventoryassignMap.put("inventoryList", inventoryList);
		 Loggers.loggerEnd();
		}
		catch(Throwable e)
		{
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd();
		
		return inventoryassignMap;

		
	}

	@Override
	public InventoryAssignmentsCompoundKey addInventoryDetails(InventoryAssignments inventoryAssignments,InventoryAssignments oldInventory)
			throws GSmartDatabaseException {
		
		Loggers.loggerStart();

		Session session=this.sessionFactory.getCurrentSession();
		
		InventoryAssignmentsCompoundKey ch = null;
		try {
		
			Loggers.loggerValue("inside the dao of add inventory details : ", inventoryAssignments.getQuantity());
			inventoryAssignments.setEntryTime(CalendarCalculator.getTimeStamp());
			inventoryAssignments.setIsActive("Y");
			System.out.println("SAVED DATA " + ch);
			String cat = inventoryAssignments.getCategory();
			String item = inventoryAssignments.getItemType();
			int numofQuantity = inventoryAssignments.getQuantity();
			
			if (updateInventory(cat, item, numofQuantity, oldInventory) == 200) {
		    	ch=	(InventoryAssignmentsCompoundKey) session.save(inventoryAssignments);

			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(inventoryAssignments);
		return ch;
	}

	@Override
	public CompoundInventoryAssignmentsStudent addInventoryStudent(InventoryAssignmentsStudent inventoryAssignmentsStudent,
			InventoryAssignmentsStudent oldInventoryAssignment,String reportingManagerId) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		CompoundInventoryAssignmentsStudent ch1=null;
		try{
			Loggers.loggerValue("inside addinventorystudent details : ",inventoryAssignmentsStudent.getQuantity());

			inventoryAssignmentsStudent.setIsActive("Y");
			inventoryAssignmentsStudent.setEntryTime(CalendarCalculator.getTimeStamp());
			System.out.println("SAVED DATA " + ch1);
			String cat = inventoryAssignmentsStudent.getCategory();
			String item = inventoryAssignmentsStudent.getItemType();
			int  numQuntity = inventoryAssignmentsStudent.getQuantity();
			
			
			
			if (updeteInv(cat,item,numQuntity,profileDao.getProfileDetails(reportingManagerId).getTeacherId(),oldInventoryAssignment) == 200){
				ch1= (CompoundInventoryAssignmentsStudent) session.save(inventoryAssignmentsStudent);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
			Loggers.loggerEnd(inventoryAssignmentsStudent);
		return ch1;
	}
	
		@Override
		public InventoryAssignmentsStudent editInventoryStudentDetails(
				InventoryAssignmentsStudent inventoryAssignmentsStudent) throws GSmartDatabaseException {
			try{
				Loggers.loggerStart(inventoryAssignmentsStudent);
				InventoryAssignmentsStudent oldInventoryAssignment = getInventoryAssignment(inventoryAssignmentsStudent.getEntryTime(),inventoryAssignmentsStudent.getHierarchy());
				if(oldInventoryAssignment != null){
					oldInventoryAssignment.setIsActive("N");
					oldInventoryAssignment.setUpdatedTime(CalendarCalculator.getTimeStamp());
					sessionFactory.getCurrentSession().update(oldInventoryAssignment);
					
					addInventoryStudent(inventoryAssignmentsStudent, oldInventoryAssignment,oldInventoryAssignment.getSmartId());
				}
			}catch (Throwable e) {
				e.printStackTrace();
				throw new GSmartDatabaseException(e.getMessage());
	      }
		   return inventoryAssignmentsStudent;
		}
		

		

	@Override
	public InventoryAssignments editInventoryDetails(InventoryAssignments inventoryAssignments)
			throws GSmartDatabaseException {
		Session session=this.sessionFactory.getCurrentSession();
		try {
			Loggers.loggerStart(inventoryAssignments);
			InventoryAssignments oldInventory = getInventory(inventoryAssignments.getEntryTime(),inventoryAssignments.getHierarchy());
			if (oldInventory != null) {
				oldInventory.setIsActive("N");
				oldInventory.setUpdatedTime(CalendarCalculator.getTimeStamp());
				session.update(oldInventory);
                
				addInventoryDetails(inventoryAssignments,oldInventory);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
      }
		return inventoryAssignments;
      }
 
	private InventoryAssignmentsStudent getInventoryAssignment(String entryTime, Hierarchy hierarchy) throws GSmartDatabaseException{
		Loggers.loggerStart();
		try{
			query = sessionFactory.getCurrentSession().createQuery("from InventoryAssignmentsStudent  where isActive='Y' and ENTRY_TIME='" + entryTime
					+ "' and hierarchy.hid=:hierarchy");
					query.setParameter("hierarchy", hierarchy.getHid());
					InventoryAssignmentsStudent oldInventoryAssignment = (InventoryAssignmentsStudent) query.uniqueResult();
					Loggers.loggerEnd();
					return oldInventoryAssignment;
					
					
		}catch (Throwable e) {
			e.printStackTrace();
	      	throw new GSmartDatabaseException(e.getMessage());
		}
	}
		
	private InventoryAssignments getInventory(String entryTime, Hierarchy hierarchy) throws GSmartDatabaseException {
		
		Loggers.loggerStart();

		try {
             query = sessionFactory.getCurrentSession().createQuery("from InventoryAssignments where isActive='Y' and ENTRY_TIME='" + entryTime
					+ "' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			InventoryAssignments oldInventory = (InventoryAssignments) query.uniqueResult();
			Loggers.loggerEnd();
			return oldInventory;
		} catch (Throwable e) {
		e.printStackTrace();
      	throw new GSmartDatabaseException(e.getMessage());
        } 

	}
 
	private int updeteInv(String cat, String item,int reqQunity,String reportingManagerId, InventoryAssignmentsStudent oldInventoryAssignment){
		Loggers.loggerStart();
		System.out.println("repoertingId"+reportingManagerId);
		Session session=this.sessionFactory.getCurrentSession();
		InventoryAssignments inv =null;
		query = sessionFactory.getCurrentSession().createQuery("from InventoryAssignments where category=:category and smartId=:reportingManagerId  and itemType=:itemType and isActive='Y' ");
		query.setParameter("reportingManagerId", reportingManagerId);
		query.setParameter("category", cat);
		query.setParameter("itemType", item);
		
		inv = (InventoryAssignments) query.uniqueResult();
		if(inv!=null){
			int numOfLeftQuntity = inv.getLeftQuantity();
			if(numOfLeftQuntity - reqQunity < 0){
				
			}else {
				if(oldInventoryAssignment != null){
					int oldQuntity= oldInventoryAssignment.getQuantity();
					if(oldQuntity < reqQunity){
						System.out.println("old quntity"+ oldQuntity);
						int updateQuntity=reqQunity-oldQuntity;
						inv.setLeftQuantity(numOfLeftQuntity + updateQuntity);
					}else if (oldQuntity > reqQunity);{
					int updateQuntity = oldQuntity-reqQunity;
					inv.setLeftQuantity(numOfLeftQuntity + updateQuntity);
					System.out.println("updateQutity"+ updateQuntity);
				}
			}

			else{
				inv.setLeftQuantity(numOfLeftQuntity-reqQunity);
				inv.setUpdatedTime(CalendarCalculator.getTimeStamp());
			}
				session.update(inv);
			Loggers.loggerEnd();
			return 200;
		}
			
		}
			return 400;
			
		
		}
		
	private int updateInventory(String cat, String item, int requestQuantity, InventoryAssignments oldInventory) {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		Inventory inventory = null;

		query = sessionFactory.getCurrentSession().createQuery("from Inventory where category=:category and itemType=:itemType and isActive='Y' ");
		query.setParameter("category", cat);
		query.setParameter("itemType", item);
		inventory = (Inventory) query.uniqueResult();
		int numOfLeftQunt = inventory.getLeftQuantity();
		if (numOfLeftQunt - requestQuantity < 0) {
			return 400;
		} else {
		    if(oldInventory != null) {
		    	int oldQuntity=oldInventory.getQuantity();
				if(oldQuntity < requestQuantity){
					System.out.println("old quntity"+ oldQuntity);
					int updateQuntity=requestQuantity-oldQuntity;
					inventory.setLeftQuantity(numOfLeftQunt - updateQuntity);
				}else if (oldQuntity > requestQuantity){
					int updateQuntity=oldQuntity-requestQuantity;
					inventory.setLeftQuantity(numOfLeftQunt + updateQuntity);
					System.out.println("upddateQuntity"+ updateQuntity);
				}
		    }
		    else {
				inventory.setLeftQuantity(numOfLeftQunt-requestQuantity);
		        inventory.setUpdateTime(CalendarCalculator.getTimeStamp());
			}
			
			session.update(inventory);
			
			Loggers.loggerEnd();
			return 200;
		}
     }

	@Override
	public void deleteInventoryDetails(InventoryAssignments inventoryAssignments) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		try {
             Logger.getLogger(InventoryAssignmentsDaoImpl.class)
					.info("trying to delete the record with entry time as : " + inventoryAssignments.getEntryTime());
			inventoryAssignments.setIsActive("D");
			inventoryAssignments.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(inventoryAssignments);
		} catch (Exception e) {
			Logger.getLogger(InventoryAssignmentsDaoImpl.class).info("trying to delete the record with entry time as : "
					+ inventoryAssignments.getEntryTime() + " and ended with exception : " + e);
			e.printStackTrace();
		} finally {
			session.close();
		}
		}
	
	@Override
	public void deleteInventoryStudentDetails(InventoryAssignmentsStudent inventoryAssignmentsStudent)
			throws GSmartDatabaseException {
			Session session=this.sessionFactory.getCurrentSession();
		Loggers.loggerStart();
		try{
			inventoryAssignmentsStudent.setIsActive("D");
			inventoryAssignmentsStudent.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(inventoryAssignmentsStudent);
	    Loggers.loggerEnd();
		}catch (Exception e) {
         e.printStackTrace();
		}
		} 

	

	/*public void getConnection() {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public List<InventoryAssignments> getInventoryDashboardData(ArrayList<String> smartIdList, Hierarchy hierarchy)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<InventoryAssignments> inventoryAssignmentList = null;
		try {
			Loggers.loggerStart("smartIdList : " + smartIdList);
			query = sessionFactory.getCurrentSession()
					.createQuery("From InventoryAssignments where isActive=:isActive and hierarchy.hid=:hierarchy and smartId in (:smartIdList)");
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameterList("smartIdList", smartIdList);
			query.setParameter("isActive", "Y");
			inventoryAssignmentList =  query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		Loggers.loggerEnd(inventoryAssignmentList);
		return inventoryAssignmentList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InventoryAssignments> getInventoryStudentList(Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<InventoryAssignments> inventoryStudentList = null;
		try {
			if(hid != null){
			query = sessionFactory.getCurrentSession().createQuery("from InventoryAssignments where isActive='Y' and hid=:hierarchy");
			query.setParameter("hierarchy",hid);
			
			}
			inventoryStudentList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}/*finally {
			session.close();
		}*/
		Loggers.loggerEnd("inventoryStuidentList:"+inventoryStudentList);
		return inventoryStudentList;
	}

	

}
