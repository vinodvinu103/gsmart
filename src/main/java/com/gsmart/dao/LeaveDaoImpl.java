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

import com.gsmart.model.CompoundLeave;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Leave;
import com.gsmart.model.LeaveMaster;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class LeaveDaoImpl implements LeaveDao {
	
	
	@Autowired
	SessionFactory sessionFactory;
	Session session;
	Query query;
	Transaction transaction;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getLeaveList(String role, Hierarchy hierarchy, int min, int max)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		List<Leave> leave = null;
		Criteria criteria = null;
		Map<String, Object> leaveMap = new HashMap<>();
		try {
			if (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director")) {
				query = session.createQuery("FROM Leave WHERE isActive='Y'");
			} else {
				query = session.createQuery("FROM Leave WHERE isActive='Y' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
			criteria = session.createCriteria(Leave.class);
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			criteria.setProjection(Projections.id());
			leave = criteria.list();
			Criteria criteriaCount = session.createCriteria(Leave.class);
			criteriaCount.setProjection(Projections.rowCount());
			Long count = (Long) criteriaCount.uniqueResult();
			leaveMap.put("totalleavelist", query.list().size());

		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		leaveMap.put("leave", leave);
		return leaveMap;
	}

	public CompoundLeave addLeave(Leave leave,Integer noOfdays) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		CompoundLeave cl = null;
		try {
			Hierarchy hierarchy=leave.getHierarchy();
			query=session.createQuery("FROM Leave WHERE smartId=:smartId AND isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("smartId", leave.getSmartId());
			query.setParameter("hierarchy", hierarchy.getHid());
			//query.setParameter("reportingManagerId", leave.getReportingManagerId());
			query.setParameter("isActive", "Y");
			Leave leave1=(Leave)query.uniqueResult();
			if(leave1==null)
			{
				leave.setEntryTime(CalendarCalculator.getTimeStamp());
				leave.setIsActive("Y");
				leave.setNumberOfDays(noOfdays);
				leave.setLeaveStatus("Pending");
				
				cl=(CompoundLeave)session.save(leave);
				
				/*session.save(details);*/
			}
				transaction.commit();
		} 
		catch (ConstraintViolationException e) 
		{		Loggers.loggerEnd();

			e.printStackTrace();
			//throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
			Loggers.loggerException(Constants.CONSTRAINT_VIOLATION);
		} 
		
	catch (Exception e) {
		e.printStackTrace();
			//throw new GSmartDatabaseException(e.getMessage());
		Loggers.loggerException(e.getMessage());
		} 
		finally {
			session.close();
		}
		//logger.debug("End :: PermissionDaoImp.addPermission()");
		//Loggers.loggerEnd();
		return cl;
	
	}
	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}

	@Override
	public void editLeave(Leave leave) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			getConnection();
			Leave oldLeave = getLeave(leave.getEntryTime(),leave.getHierarchy());
			oldLeave.setIsActive("N");
			oldLeave.setUpdatedTime(CalendarCalculator.getTimeStamp());
			session.update(oldLeave);
			leave.setIsActive("Y");
			leave.setEntryTime(CalendarCalculator.getTimeStamp());
			session.save(leave);
			session.getTransaction().commit();
			session.close();
		}
		catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
			//Loggers.loggerException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			//throw new GSmartDatabaseException(e.getMessage());
			Loggers.loggerException(e.getMessage());
		}
	}

	
	public Leave getLeave(String entryTime,Hierarchy hierarchy){
		Loggers.loggerStart();
		try{
			query=session.createQuery("from Leave where isActive='Y' and entryTime='"+entryTime+"' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			Leave leave = (Leave) query.uniqueResult();
			return leave;
			
			} 
		catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerEnd();
			return null;
					}
	
	}
	@Override
	public void deleteLeave( Leave leave ) throws GSmartDatabaseException {
		Loggers.loggerStart(leave);
		try {
			getConnection();
			leave.setIsActive("D");
			leave.setLeaveStatus("Cancelled");
			leave.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(leave);
			transaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
	}


}
