package com.gsmart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Band;
import com.gsmart.model.CompoundLeaveMaster;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.LeaveMaster;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class LeaveMasterDaoImpl implements LeaveMasterDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction transaction = null;
	Query query;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getLeaveMasterList(String role, Hierarchy hierarchy, Integer min, Integer max)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		Criteria criteria = null;
		List<LeaveMaster> leavemasterlist = null;
		Map<String, Object> leavemasterMap = new HashMap<>();
		getconnection();
		try {
			if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director"))
			 {
			 query=session.createQuery("from LeaveMaster where isActive='Y'");
			 }else{
				 query=session.createQuery("from LeaveMaster where isActive='Y' and hierarchy.hid=:hierarchy");
				 query.setParameter("hierarchy", hierarchy.getHid());
			 }
			criteria = session.createCriteria(LeaveMaster.class);
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			criteria.setProjection(Projections.id());
			leavemasterlist = criteria.list();
			Criteria criteriaCount = session.createCriteria(LeaveMaster.class);
			criteriaCount.setProjection(Projections.rowCount());
			Long count = (Long) criteriaCount.uniqueResult();
			leavemasterMap.put("totallist", query.list().size());
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());

		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		leavemasterMap.put("leavemasterlist", leavemasterlist);
		return leavemasterMap;
	}
	@Override
	public CompoundLeaveMaster addLeaveMaster(LeaveMaster leaveMaster) throws GSmartDatabaseException {
		getconnection();
		Loggers.loggerStart();
		CompoundLeaveMaster cb = null;
		
		try {
			Hierarchy hierarchy=leaveMaster.getHierarchy();
			query=session.createQuery("FROM LeaveMaster WHERE leaveType=:leaveType AND  daysAllow=:daysAllow AND isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameter("leaveType", leaveMaster.getLeaveType());
			query.setParameter("daysAllow", leaveMaster.getDaysAllow());
			query.setParameter("isActive", "Y");
			LeaveMaster leaveMaster2 = (LeaveMaster) query.uniqueResult();
			if (leaveMaster2 == null) {
				leaveMaster.setEntryTime((CalendarCalculator.getTimeStamp()));
				leaveMaster.setIsActive("Y");
				cb = (CompoundLeaveMaster) session.save(leaveMaster);
			}

			transaction.commit();
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		return cb;
	}

	public void deleteLeaveMaster(LeaveMaster leaveMaster) throws GSmartDatabaseException {
		getconnection();
		Loggers.loggerStart();
		try {
			

			leaveMaster.setExitTime(CalendarCalculator.getTimeStamp());
			leaveMaster.setIsActive("D");
			session.update(leaveMaster);
			transaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
	}

	@Override
	public void editLeaveMaster(LeaveMaster leaveMaster) throws GSmartDatabaseException {
		getconnection();
		Loggers.loggerStart();
		try {
			
			LeaveMaster oldleaveMaster= getLeaveMaster(leaveMaster.getEntryTime(),leaveMaster.getHierarchy());
			oldleaveMaster.setIsActive("N");
			oldleaveMaster.setUpdateTime(CalendarCalculator.getTimeStamp());
			session.update(oldleaveMaster);
			leaveMaster.setIsActive("Y");
			session.save(leaveMaster);
			transaction.commit();
			session.close();

		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());

		}finally {
			session.close();
		}
	}
	
	
	
	
	
	public LeaveMaster getLeaveMaster(String entryTime,Hierarchy hierarchy) {
		try {


			query = session.createQuery("from LeaveMaster where isActive=:isActive and entryTime=:entryTime and hierarchy.hid=:hierarchy");
		     query.setParameter("entryTime",entryTime);
		     query.setParameter("hierarchy", hierarchy.getHid());
		     query.setParameter("isActive","Y");
			 LeaveMaster leaveMaster = ( LeaveMaster) query.uniqueResult();
			
			
			return leaveMaster;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void getconnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();

	}

}
