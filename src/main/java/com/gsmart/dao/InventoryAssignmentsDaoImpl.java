package com.gsmart.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Index;

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
import com.gsmart.model.Token;
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
	public Map<String, Object> getInventoryAssignStudentList(String role, Long hid, Integer min, Integer max)
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
				criteria.addOrder(Order.desc("entryTime"));
			}else{
				criteria.add(Restrictions.eq("isActive", "Y"));
				criteria.add(Restrictions.eq("hierarchy.hid", hid));
				criteria.addOrder(Order.asc("entryTime"));
				criteriaCount.add(Restrictions.eq("isActive", "Y"));
				criteriaCount.add(Restrictions.eq("hierarchy.hid", hid));
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
	public Map<String, Object> getInventoryAssignList(Token tokenObj, Long hid, Integer min, Integer max) throws GSmartDatabaseException 
	{
		Loggers.loggerStart(tokenObj);
		
		List<InventoryAssignments> inventoryList=null;
		Map<String, Object> inventoryassignMap = new HashMap<String, Object>();
		Criteria criteria = null;
		criteria = sessionFactory.getCurrentSession().createCriteria(InventoryAssignments.class);
		Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(InventoryAssignments.class);
		try
		{
		if(tokenObj.getRole().equalsIgnoreCase("admin") || tokenObj.getRole().equalsIgnoreCase("finance"))
		{
		criteria.add(Restrictions.eq("isActive", "Y"));
		}else if(tokenObj.getRole().equalsIgnoreCase("teacher")){
			criteria.add(Restrictions.eq("smartId",tokenObj.getSmartId()));
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("hierarchy.hid", tokenObj.getHierarchy().getHid()));
			criteriaCount.add(Restrictions.eq("smartId",tokenObj.getSmartId()));
			criteriaCount.add(Restrictions.eq("isActive", "Y"));
			criteriaCount.add(Restrictions.eq("hierarchy.hid", tokenObj.getHierarchy().getHid()));
			
		}
		
		criteria.setMaxResults(max);
		criteria.setFirstResult(min);
		criteria.addOrder(Order.asc("standard"));
		inventoryList = criteria.list();
		
		criteriaCount.setProjection(Projections.rowCount());
		inventoryassignMap.put("totalinventoryassign", criteriaCount.uniqueResult());
	    inventoryassignMap.put("inventoryList", inventoryList);
		}
		catch(Throwable e)
		{
			throw new GSmartDatabaseException(e.getMessage());
		} 
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Loggers.loggerEnd(inventoryList);
		
		return inventoryassignMap;

		
	}

	@Override
	public InventoryAssignmentsCompoundKey addInventoryDetails(InventoryAssignments inventoryAssignments,InventoryAssignments oldInventory,Long hid)
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
			 
			if (updateInventory(cat, item, numofQuantity, numofQuantity, oldInventory,hid) == 200) {
				inventoryAssignments.setInvreturn(0);
		    	ch=	(InventoryAssignmentsCompoundKey) session.save(inventoryAssignments);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(inventoryAssignments);
		return ch;
	}
 
	private InventoryAssignmentsCompoundKey returnDetails(InventoryAssignments inventoryAssignments,InventoryAssignments oldInventory,Long hid)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		InventoryAssignmentsCompoundKey ch1 = null;
		try {
			Loggers.loggerValue("inside the dao of add inventory details : ", inventoryAssignments.getInvreturn());
			Loggers.loggerValue("what no of quntity", inventoryAssignments.getQuantity());
			
			inventoryAssignments.setEntryTime(CalendarCalculator.getTimeStamp());
			inventoryAssignments.setIsActive("Y");
			System.out.println("SAVED DATA " + ch1);
			String cat = inventoryAssignments.getCategory();
			String item = inventoryAssignments.getItemType();
			int numofQuantity = inventoryAssignments.getInvreturn();
			System.out.println(numofQuantity+" no of quantity ");
			if (updateInventory(cat, item, numofQuantity, numofQuantity, oldInventory,hid) == 200) {
				
		    	ch1=	(InventoryAssignmentsCompoundKey) session.save(inventoryAssignments);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(inventoryAssignments);
		return ch1;
	}
	
	
	@Override
	public CompoundInventoryAssignmentsStudent addInventoryStudent(InventoryAssignmentsStudent inventoryAssignmentsStudent,
			InventoryAssignmentsStudent oldInventoryAssignment,String reportingManagerId,Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		CompoundInventoryAssignmentsStudent chstudent=null;
		try{
			Loggers.loggerValue("inside addinventorystudent details : ",inventoryAssignmentsStudent.getQuantity());

			inventoryAssignmentsStudent.setIsActive("Y");
			inventoryAssignmentsStudent.setEntryTime(CalendarCalculator.getTimeStamp());
			System.out.println("SAVED DATA " + chstudent);
			String cat = inventoryAssignmentsStudent.getCategory();
			String item = inventoryAssignmentsStudent.getItemType();
			int  numQuntity = inventoryAssignmentsStudent.getQuantity();
			
			if (updeteInv(cat,item,numQuntity,numQuntity, profileDao.getProfileDetails(reportingManagerId).getSmartId(),oldInventoryAssignment,hid) == 200){
				inventoryAssignmentsStudent.setReturnstu(0);
				chstudent= (CompoundInventoryAssignmentsStudent) session.save(inventoryAssignmentsStudent);
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
			Loggers.loggerEnd(inventoryAssignmentsStudent);
		return chstudent;
	}
	
	private CompoundInventoryAssignmentsStudent returnStudentDetails(InventoryAssignmentsStudent inventoryAssignmentsStudent,
			InventoryAssignmentsStudent oldInventoryAssignment,String reportingManagerId,Long hid)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		CompoundInventoryAssignmentsStudent chstudent1=null;
	   
		try {
			Loggers.loggerValue("inside the dao of add inventory details : ", inventoryAssignmentsStudent.getReturnstu());
			Loggers.loggerValue("what no of quntity", inventoryAssignmentsStudent.getQuantity());
			
			inventoryAssignmentsStudent.setEntryTime(CalendarCalculator.getTimeStamp());
			System.out.println("data setting as Y");
			inventoryAssignmentsStudent.setIsActive("Y");
			System.out.println("SAVED DATA " + chstudent1);
			String cat = inventoryAssignmentsStudent.getCategory();
			String item = inventoryAssignmentsStudent.getItemType();
			int numQuntity  = inventoryAssignmentsStudent.getQuantity();
			int numQuntity1 = inventoryAssignmentsStudent.getReturnstu();
			System.out.println(numQuntity1+"num of quntiry update ");
			
			if (updeteInv(cat,item,numQuntity,numQuntity1,profileDao.getProfileDetails(reportingManagerId).getReportingManagerId(),oldInventoryAssignment,hid) == 200) {
				
					chstudent1=	(CompoundInventoryAssignmentsStudent) session.save(inventoryAssignmentsStudent);
		    	System.out.println(chstudent1+" inside if condition ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(inventoryAssignmentsStudent);
		return chstudent1;
	}

         @Override
		public InventoryAssignmentsStudent editInventoryStudentDetails(
				InventoryAssignmentsStudent inventoryAssignmentsStudent) throws GSmartDatabaseException {
        	 Session session=this.sessionFactory.getCurrentSession();
			try{
				Loggers.loggerStart(inventoryAssignmentsStudent);
				InventoryAssignmentsStudent oldInventoryAssignment = getInventoryAssignment(inventoryAssignmentsStudent.getEntryTime(),inventoryAssignmentsStudent.getHierarchy().getHid());
				System.out.println("old  inventory"+oldInventoryAssignment);
				if(oldInventoryAssignment != null){
					oldInventoryAssignment.setIsActive("N");
					oldInventoryAssignment.setUpdatedTime(CalendarCalculator.getTimeStamp());
					session.update(oldInventoryAssignment);
					
					returnStudentDetails(inventoryAssignmentsStudent, oldInventoryAssignment,oldInventoryAssignment.getSmartId(),inventoryAssignmentsStudent.getHierarchy().getHid());
				
				}
			}catch (Throwable e) {
				System.out.println(" exceptin in editInvStudent");
				e.printStackTrace();
				throw new GSmartDatabaseException(e.getMessage());
	      }
			Loggers.loggerEnd(inventoryAssignmentsStudent);
		   return inventoryAssignmentsStudent;
		}
		

		

	@Override
	public InventoryAssignments editInventoryDetails(InventoryAssignments inventoryAssignments)
			throws GSmartDatabaseException {
		Session session=this.sessionFactory.getCurrentSession();
		try {

			Loggers.loggerStart(inventoryAssignments);
			InventoryAssignments oldInventory = getInventory(inventoryAssignments.getEntryTime(),inventoryAssignments.getHierarchy().getHid());
			if (oldInventory != null) {
				oldInventory.setIsActive("N");
				oldInventory.setUpdatedTime(CalendarCalculator.getTimeStamp());
				session.update(oldInventory);
                
				returnDetails(inventoryAssignments,oldInventory,inventoryAssignments.getHierarchy().getHid());
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
      }
		return inventoryAssignments;
      }
 
	private InventoryAssignmentsStudent getInventoryAssignment(String entryTime, Long hid) throws GSmartDatabaseException{
		Loggers.loggerStart();
		try{
			query = sessionFactory.getCurrentSession().createQuery("from InventoryAssignmentsStudent  where isActive='Y' and ENTRY_TIME='" + entryTime
					+ "' and hierarchy.hid=:hierarchy");
					query.setParameter("hierarchy", hid);
					InventoryAssignmentsStudent oldInventoryAssignment = (InventoryAssignmentsStudent) query.uniqueResult();
					Loggers.loggerEnd();
					return oldInventoryAssignment;
					
					
		}catch (Throwable e) {
			e.printStackTrace();
	      	throw new GSmartDatabaseException(e.getMessage());
		}
	}
		
	private InventoryAssignments getInventory(String entryTime, Long hid) throws GSmartDatabaseException {
		
		Loggers.loggerStart();

		try {
             query = sessionFactory.getCurrentSession().createQuery("from InventoryAssignments where isActive='Y' and ENTRY_TIME='" + entryTime
					+ "' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);
			InventoryAssignments oldInventory = (InventoryAssignments) query.uniqueResult();
			Loggers.loggerEnd();
			return oldInventory;
		} catch (Throwable e) {
		e.printStackTrace();
      	throw new GSmartDatabaseException(e.getMessage());
        } 

	}
 
	private int updeteInv(String cat, String item, int numQuntity, int numQuntity1, String reportingManagerId, InventoryAssignmentsStudent oldInventoryAssignment,Long hid){
		Loggers.loggerStart("what will num of quntity");
		System.out.println("repoertingId"+reportingManagerId);
		Session session=this.sessionFactory.getCurrentSession();
		InventoryAssignments inv =null;
		System.out.println("num of quntity for return from student"+numQuntity1);
		query = sessionFactory.getCurrentSession().createQuery("from InventoryAssignments where category=:category and"
				+ " smartId=:reportingManagerId  and itemType=:itemType and isActive='Y' and hierarchy.hid=:hierarchy ");
		query.setParameter("reportingManagerId", reportingManagerId);
		query.setParameter("hierarchy", hid);
		query.setParameter("category", cat);
		query.setParameter("itemType", item);
		System.out.println("welcome");
		inv = (InventoryAssignments) query.uniqueResult();
		if(inv!=null){
			System.out.println("inside student update method");
			int numOfLeftQuntity = inv.getLeftQuantity();
			if(numOfLeftQuntity - numQuntity < 0){
				System.out.println("condition satisfied");
			}else {
				if(oldInventoryAssignment != null){
					System.out.println("inside if condition");
					int oldQuntity= oldInventoryAssignment.getQuantity();
					if(oldQuntity < numQuntity){
						System.out.println("old quntity"+ oldQuntity);
						int updateQuntity=numQuntity-oldQuntity;
						inv.setLeftQuantity(numOfLeftQuntity + updateQuntity);
					}
					else if (oldQuntity > numQuntity);{
					int updateQuntity = oldQuntity-numQuntity;
					inv.setLeftQuantity(numOfLeftQuntity + numQuntity1);
					System.out.println("updateQutity"+ updateQuntity);
					System.out.println("update left quntity"+numQuntity1);
				}
			}
			else{
				inv.setLeftQuantity(numOfLeftQuntity-numQuntity1);
				inv.setUpdatedTime(CalendarCalculator.getTimeStamp());
			}
				session.update(inv);
			Loggers.loggerEnd();
			return 200;
		}
			
		}
			return 400;
			
		
		}
		
	private int updateInventory(String cat, String item, int requestQuantity, int numofQuantity, InventoryAssignments oldInventory,Long hid) {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		Inventory inventory = null;
	
		query = sessionFactory.getCurrentSession().createQuery("from Inventory where category=:category and itemType=:itemType and isActive='Y' and hierarchy.hid=:hierarchy ");
		query.setParameter("category", cat);
		query.setParameter("itemType", item);
		query.setParameter("hierarchy", hid);
		inventory = (Inventory) query.uniqueResult();
		int numOfLeftQunt = inventory.getLeftQuantity();
		if (numOfLeftQunt - requestQuantity < 0) {
			return 400;
		} else {
			System.out.println("odlinventory rajakumar");
		    if(oldInventory != null) {

		    	int oldQuntity=oldInventory.getQuantity();
				if(oldQuntity < requestQuantity){
					System.out.println("old quntity"+ oldQuntity);
					int updateQuntity=requestQuantity-oldQuntity;
					inventory.setLeftQuantity(numOfLeftQunt - updateQuntity);
				}else if (oldQuntity > requestQuantity){
					System.out.println("i here come this side");
					int updateQuntity=oldQuntity-requestQuantity;
					inventory.setLeftQuantity(numOfLeftQunt + numofQuantity);
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
		} 
		}
	
	@Override
	public void deleteInventoryStudentDetails(InventoryAssignmentsStudent inventoryAssignmentsStudent)
			throws GSmartDatabaseException {
			Session session=this.sessionFactory.getCurrentSession();
		Loggers.loggerStart();
		try{
			inventoryAssignmentsStudent.setIsActive("D");
			System.out.println("we are setting isActive as D....................");
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
		
	public List<InventoryAssignments> getInventoryStudentList(Token tokenObj) throws GSmartDatabaseException {
		List<InventoryAssignments> inventoryStudentList = null;
		try {
			if(tokenObj != null){
			query = sessionFactory.getCurrentSession().createQuery("from InventoryAssignments where isActive='Y' and hid=:hierarchy and smartId=:smartId ");
			query.setParameter("hierarchy",tokenObj.getHierarchy().getHid());
			query.setParameter("smartId", tokenObj.getSmartId());
			
			}
			inventoryStudentList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd("inventoryStuidentList:"+inventoryStudentList);
		return inventoryStudentList;
	}

	

}
