package com.gsmart.dao;

import java.util.Calendar;
import java.util.Date;
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

import com.gsmart.model.CompoundLeave;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Leave;
import com.gsmart.model.Token;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class LeaveDaoImpl implements LeaveDao {

	@Autowired
	private SessionFactory sessionFactory;

	Query query;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getLeaveList(Token tokenObj, Hierarchy hierarchy, int min, int max)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Leave> leave = null;
		Criteria criteria = null;
		Map<String, Object> leaveMap = new HashMap<>();
		try {
			criteria = sessionFactory.getCurrentSession().createCriteria(Leave.class);
			criteria.add(Restrictions.eq("smartId",tokenObj.getSmartId()));
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("hierarchy.hid",  hierarchy.getHid()));
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			criteria.addOrder(Order.desc("entryTime"));
			leave = criteria.list();

			Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(Leave.class);
			criteriaCount.add(Restrictions.eq("isActive", "Y")).setProjection(Projections.rowCount());
			criteriaCount.add(Restrictions.eq("hierarchy.hid",hierarchy.getHid()));
			criteriaCount.add(Restrictions.eq("smartId",tokenObj.getSmartId() ));
			criteriaCount.setProjection(Projections.rowCount());
			leaveMap.put("totalleavelist",criteriaCount.uniqueResult());
		}catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		} 
		Loggers.loggerEnd(leave);
		leaveMap.put("leave", leave);
		return leaveMap;
	}

	public CompoundLeave addLeave(Leave leave, Integer noOfdays) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		CompoundLeave cl = null;
		try {
			
			Date startDate=getTimeWithOutMills(leave.getStartDate());
			Date endDate = getTimeWithOutMills(leave.getEndDate());
			 
				 leave.setStartDate(startDate);
				 leave.setEndDate(endDate);
				 leave.setEntryTime(CalendarCalculator.getTimeStamp());
				 leave.setIsActive("Y");
				 leave.setNumberOfDays(noOfdays);
				 leave.setLeaveStatus("Pending");
				 cl = (CompoundLeave) session.save(leave);
			
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			 throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd();
		return cl;

	}
	public Date getTimeWithOutMills(Date date){
		Loggers.loggerStart();
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		Date date2=calendar.getTime();
		return date2;
		
	}

	/*public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}*/

	@Override
	public void editLeave(Leave leave) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		try {

			Leave oldLeave = getLeave(leave.getEntryTime(), leave.getHierarchy());
			oldLeave.setIsActive("N");
			oldLeave.setUpdatedTime(CalendarCalculator.getTimeStamp());
			session.update(oldLeave);
			leave.setIsActive("Y");
			leave.setEntryTime(CalendarCalculator.getTimeStamp());
			session.save(leave);
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
			// Loggers.loggerException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			// throw new GSmartDatabaseException(e.getMessage());
			Loggers.loggerException(e.getMessage());
		} 
	}

	public Leave getLeave(String entryTime, Hierarchy hierarchy) {
		Loggers.loggerStart();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Leave where isActive='Y' and entryTime='" + entryTime + "' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			Leave leave = (Leave) query.uniqueResult();
			return leave;

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerEnd();
			return null;
		}

	}

	@Override
	public void deleteLeave(Leave leave) throws GSmartDatabaseException {
		Loggers.loggerStart(leave);
		Session session=this.sessionFactory.getCurrentSession();
		try {

			leave.setIsActive("D");
			leave.setLeaveStatus("Cancelled");
			leave.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(leave);

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
		} 
		Loggers.loggerEnd();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Leave> getLeaves(String role, Hierarchy hierarchy, String smartId, String leaveType) {
		Loggers.loggerStart();
		List<Leave> leaveList = null;
		try {
			
				query = sessionFactory.getCurrentSession().createQuery(
						"from Leave where smartId=:smartId and leaveType=:leaveType and lower(leaveStatus)!='rejected*' and isActive='Y' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			

			query.setParameter("smartId", smartId);
			query.setParameter("leaveType", leaveType);
			leaveList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return leaveList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Leave> leaveForAdding(Leave leave,Long hid) throws GSmartDatabaseException {
		
		Loggers.loggerStart();
		List<Leave> leaveAddList=null;
		try {
			query=sessionFactory.getCurrentSession().createQuery("from Leave where isActive='Y' and smartId=:smartId and leaveStatus!='Rejected*' and hid=:hierarchy");
			query.setParameter("smartId", leave.getSmartId());
			query.setParameter("hierarchy",hid);
			leaveAddList=query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}/*finally {
			session.close();
		}*/
		Loggers.loggerEnd();
		return leaveAddList;
	}

}
