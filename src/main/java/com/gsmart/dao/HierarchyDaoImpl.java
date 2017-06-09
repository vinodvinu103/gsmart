/*
 * class name: HierarchyDaoImpl.java

 *
 * Copyright (c) 2008-2009 Gowdanar Technologies Pvt. Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Gowdanar
 * Technologies Pvt. Ltd.("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Gowdanar Technologie.
 *
 * GOWDANAR TECHNOLOGIES MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. GOWDANAR TECHNOLOGIES SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING
 */

package com.gsmart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.Hierarchy;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

/**
 * provides the implementation for the methods available in {@link HierarchyDao}
 * interface
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */
@Repository
@Transactional
public class HierarchyDaoImpl implements HierarchyDao {

	@Autowired
	private SessionFactory sessionFactory;

	
	private Query query;

	// final Logger logger = Logger.getLogger(HierarchyDaoImpl.class);

	/**
	 * to view the list of records available in {@link Hierarchy} table
	 * 
	 * @return list of hierarchy entities available in Hierarchy
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getHierarchyList(String role,Hierarchy hierarchy, Integer min, Integer max) throws GSmartDatabaseException {
		Loggers.loggerStart();

		List<Hierarchy> hierarchyList;
		Map<String, Object> hierarchyMap = new HashMap<String, Object>();
		Criteria criteria = null;
		try {
			
			criteria = sessionFactory.getCurrentSession().createCriteria(Hierarchy.class);
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.addOrder(Order.asc("school"));
			hierarchyList = criteria.list();
			Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(Hierarchy.class);
			criteriaCount.setProjection(Projections.rowCount());
			Long count = (Long) criteriaCount.uniqueResult();
			hierarchyMap.put("totalhierarchy", count);

		} catch (Throwable e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd(hierarchyList);
		hierarchyMap.put("hierarchyList", hierarchyList);
		return hierarchyMap;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Hierarchy> getHierarchyList1(String role,Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart();

		List<Hierarchy> hierarchyList1;
		Criteria criteria = null;
		try {
			if (hierarchy==null) {
				query = sessionFactory.getCurrentSession().createQuery("from Hierarchy where isActive='Y'");
			} else {
				query = sessionFactory.getCurrentSession().createQuery("from Hierarchy where isActive='Y' and hid=:hid");
				query.setParameter("hid", hierarchy.getHid());
			}
			criteria = sessionFactory.getCurrentSession().createCriteria(Hierarchy.class);
			hierarchyList1 = criteria.list();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd(hierarchyList1);
		return hierarchyList1;
	}

	/**
	 * Adds new hierarchy entity to {@link Hierarchy} save it in database
	 * 
	 * @param hierarchy
	 *            instance of Hierarchy
	 * @return Nothing
	 */

	@Override
	public boolean addHierarchy(Hierarchy hierarchy) throws GSmartDatabaseException {

		Session session=this.sessionFactory.getCurrentSession();
		Loggers.loggerStart(hierarchy);
		System.out.println("hiearrachy:::::::::::" + hierarchy.getEntryTime() + hierarchy.getInstitution()
				+ hierarchy.getSchool());
		boolean status;
		try {
			Hierarchy hierarchy1 = fetch(hierarchy);
			if (hierarchy1 != null) {
				return false;
			}
			hierarchy.setEntryTime(CalendarCalculator.getTimeStamp());
			hierarchy.setIsActive("Y");
			System.out.println(hierarchy);
			session.save(hierarchy);
			status = true;
		} catch (ConstraintViolationException e) {

			status = false;
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {

			status = false;
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd();
		return status;
	}

	/**
	 * persists the updated hierarchy instance
	 * 
	 * @param hierarchy
	 *            instance of {@link Hierarchy}
	 * @return Nothing
	 */

	@Override
	public Hierarchy editHierarchy(Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart(hierarchy);
		Hierarchy ch = null;
		try {
//			Hierarchy oldHierarchy = getHierarchy(hierarchy.getEntryTime());
			ch = updateHierarchy( hierarchy);
			

			return ch;
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 

	}

	private Hierarchy updateHierarchy( Hierarchy hierarchy) throws GSmartDatabaseException {
		
		try {


				hierarchy.setUpdateTime(CalendarCalculator.getTimeStamp());
				hierarchy.setIsActive("Y");
				sessionFactory.getCurrentSession().update(hierarchy);
				
				query=sessionFactory.getCurrentSession().createQuery("update Profile set school=:school,institution=:institution where hierarchy.hid=:hierarchy");
				query.setParameter("school", hierarchy.getSchool());
				query.setParameter("institution", hierarchy.getInstitution());
				query.setParameter("hierarchy", hierarchy.getHid());
				query.executeUpdate();

				return hierarchy;

		/*	}*/
		} catch (ConstraintViolationException e) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			e.printStackTrace();
			
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
			
		} catch (Throwable e) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}

	}

	public Hierarchy getHierarchy(String entryTime) {
		try {

			query = sessionFactory.getCurrentSession().createQuery("from Hierarchy where IS_ACTIVE='Y' and ENTRY_TIME='" + entryTime + "'");
			 Hierarchy hierarchyList =  (Hierarchy) query.uniqueResult();

			return hierarchyList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void deleteHierarchy(Hierarchy hierarchy) throws GSmartDatabaseException {

		Session session=this.sessionFactory.getCurrentSession();
		try {
			Loggers.loggerStart();

			hierarchy.setExitTime(CalendarCalculator.getTimeStamp());
			/*hierarchy.setIsActive("D");
			session.update(hierarchy);*/
			session.delete(hierarchy);

			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}

	/**
	 * create instance for session and begins transaction
	 */
	/*public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}*/

	public Hierarchy fetch(Hierarchy hierarchy) {

		Loggers.loggerStart("entering into fetch");
		Hierarchy hierarchyList = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"FROM Hierarchy WHERE institution=:institution AND school=:school AND isActive=:isActive");
			query.setParameter("school", hierarchy.getSchool());
			query.setParameter("isActive", "Y");
			query.setParameter("institution", hierarchy.getInstitution());
			hierarchyList = (Hierarchy) query.uniqueResult();
			//Loggers.loggerStart("hierarchylist is:", hierarchyList);
			Loggers.loggerEnd(hierarchyList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hierarchyList;

	}

	@Override
	public Hierarchy getHierarchyByHid(Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart(hid);
		Hierarchy hierarchyList = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery("FROM Hierarchy WHERE hid=:hid");
			query.setParameter("hid", hid);
			hierarchyList = (Hierarchy) query.uniqueResult();
		} catch (Exception e) {

			e.printStackTrace();

		}
		Loggers.loggerEnd();
		return hierarchyList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Hierarchy> getAllHierarchy() {
		query = sessionFactory.getCurrentSession().createQuery("from Hierarchy where isActive='Y'");
		return query.list();

	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Hierarchy> searchhierarchy(Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Hierarchy> searchlist = null;
		try{
		query = sessionFactory.getCurrentSession().createQuery("from Hierarchy where school like '%"+hierarchy.getSchool()+"%' and isActive = 'Y'");
		searchlist = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return searchlist;
	}

	/*@Override
	public List<Hierarchy> getAllHierarchy() {
		getConnection();
		query = session.createQuery("from Hierarchy where isActive='Y'");
		
		
		return query.list();
	}*/
}
