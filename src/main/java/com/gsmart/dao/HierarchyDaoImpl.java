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

import java.util.ArrayList;
import java.util.List;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.CompoundHierarchy;
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
public class HierarchyDaoImpl implements HierarchyDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction transaction = null;
	Query query;

	// final Logger logger = Logger.getLogger(HierarchyDaoImpl.class);

	/**
	 * to view the list of records available in {@link Hierarchy} table
	 * 
	 * @return list of hierarchy entities available in Hierarchy
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Hierarchy> getHierarchyList() throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Hierarchy> hierarchyList;
		try {
			getConnection();
			query = session.createQuery("from Hierarchy where isActive='Y'");
			hierarchyList = query.list();

		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {

			session.close();
		}
		Loggers.loggerEnd();
		return hierarchyList;
	}

	/**
	 * Adds new hierarchy entity to {@link Hierarchy} save it in database
	 * 
	 * @param hierarchy
	 *            instance of Hierarchy
	 * @return Nothing
	 */

	@Override
	public CompoundHierarchy addHierarchy(Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart();
		CompoundHierarchy ch = null;
		try {
			getConnection();
			Hierarchy hierarchy1 = fetch(hierarchy);
			if (hierarchy1 != null) {
				return null;
			}
			hierarchy.setEntryTime(CalendarCalculator.getTimeStamp());
			hierarchy.setIsActive("Y");
			System.out.println(hierarchy);
			ch = (CompoundHierarchy) session.save(hierarchy);
			transaction.commit();

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
			Hierarchy oldHierarchy = getHierarchy(hierarchy.getEntryTime());
			ch = updateHierarchy(oldHierarchy, hierarchy);
			addHierarchy(hierarchy);
			return ch;
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}

	}

	private Hierarchy updateHierarchy(Hierarchy oldHierarchy, Hierarchy hierarchy) throws GSmartDatabaseException {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		Hierarchy ch = null;
		try {
			Hierarchy hierarchy1 = fetch(hierarchy);
			if (hierarchy1 == null) {
				oldHierarchy.setUpdateTime(CalendarCalculator.getTimeStamp());
				oldHierarchy.setIsActive("N");
				session.update(oldHierarchy);

				transaction.commit();
				return oldHierarchy;

			}
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}
		return ch;

	}

	@SuppressWarnings("unchecked")
	public Hierarchy getHierarchy(String entryTime) {
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createQuery("from Hierarchy where IS_ACTIVE='Y' and ENTRY_TIME='" + entryTime + "'");
			ArrayList<Hierarchy> hierarchyList = (ArrayList<Hierarchy>) query.list();
			transaction.commit();
			session.close();
			return hierarchyList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void deleteHierarchy(Hierarchy hierarchy) throws GSmartDatabaseException {

		try {
			Loggers.loggerStart();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			hierarchy.setExitTime(CalendarCalculator.getTimeStamp());
			hierarchy.setIsActive("D");
			session.update(hierarchy);
			transaction.commit();
			session.close();
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * create instance for session and begins transaction
	 */
	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}

	public Hierarchy fetch(Hierarchy hierarchy) {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		query = session.createQuery(
				"FROM Hierarchy WHERE institution=:institution AND school=:school AND isActive=:isActive");
		query.setParameter("school", hierarchy.getSchool());
		query.setParameter("isActive", "Y");
		query.setParameter("institution", hierarchy.getInstitution());
		Hierarchy hierarchy1 = (Hierarchy) query.uniqueResult();
		return hierarchy1;

	}
}
