package com.gsmart.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.Leave;
import com.gsmart.model.LeaveDetails;
import com.gsmart.model.LeaveMaster;
import com.gsmart.model.Profile;
import com.gsmart.model.ReportCard;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class MyTeamLeaveDaoImpl implements MyTeamLeaveDao {
	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction transaction = null;
	Query query;
	Criteria criteria=null;

	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}

	@SuppressWarnings("unchecked")
	@Override


	public Map<String, Object> getLeavelist(Profile profileInfo, Long hierarchy,Integer min,Integer max) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Map<String, Object> leavelist =new HashMap<>();
		getConnection();
		try {
			String role = profileInfo.getRole();
			if (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("director")||role.equalsIgnoreCase("hr")) {
				criteria = session.createCriteria(Leave.class);
				criteria.add(Restrictions.eq("isActive", "Y"));
				criteria.addOrder(Order.asc("fullName"));
				criteria.setFirstResult(min);
				criteria.setMaxResults(max);
				leavelist.put("myTeamLeaveList", criteria.list());
				
				criteria = session.createCriteria(ReportCard.class).add(Restrictions.eq("isActive", "Y"))
						.setProjection(Projections.rowCount());
				Long count = (Long) criteria.uniqueResult();
				leavelist.put("totalListCount", count);
			} else {
				criteria = session.createCriteria(Leave.class);
				criteria.add(Restrictions.eq("isActive", "Y"));
				criteria.add(Restrictions.ne("leaveStatus", "Rejected*").ignoreCase());
				criteria.add(Restrictions.eq("reportingManagerId", profileInfo.getSmartId()));
				criteria.add(Restrictions.eq("hierarchy.hid", hierarchy));
				criteria.addOrder(Order.asc("fullName"));
				criteria.setFirstResult(min);
				criteria.setMaxResults(max);
				leavelist.put("myTeamLeaveList", criteria.list());
				
				criteria = session.createCriteria(Leave.class).add(Restrictions.eq("isActive", "Y"))
						.add(Restrictions.eq("reportingManagerId", profileInfo.getSmartId()))
						.add(Restrictions.ne("leaveStatus", "Rejected*").ignoreCase())
						.add(Restrictions.eq("hierarchy.hid", hierarchy))
						.setProjection(Projections.rowCount());
				Long count = (Long) criteria.uniqueResult();
				leavelist.put("totalListCount", count);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
		} finally {

			session.close();
		}
		Loggers.loggerEnd(leavelist);
		return leavelist;
	}

	@Override
	public void rejectleave(Leave leave) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		try {
			leave.setExitTime(CalendarCalculator.getTimeStamp());
			leave.setLeaveStatus("Rejected*");
			session.update(leave);
			transaction.commit();
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}

	}

	@Override
	public void sactionleave(Leave leave) throws GSmartDatabaseException {
		Loggers.loggerStart(leave);
		getConnection();

		try {
			Loggers.loggerStart();
			Leave applyLeave = getLeave(leave);
			updateLeave(applyLeave);
			Loggers.loggerEnd();
		} catch (org.hibernate.exception.ConstraintViolationException e) {
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}finally {
			session.close();
		}
	}

	private void updateLeave(Leave applyLeave) throws GSmartDatabaseException {
		Loggers.loggerStart(applyLeave);

		applyLeave.setUpdatedTime(CalendarCalculator.getTimeStamp());
		applyLeave.setLeaveStatus("Sanctioned");
		session.update(applyLeave);
		transaction.commit();
		
		Loggers.loggerEnd();
	}

	public Leave getLeave(Leave leave) {
		try {
			Loggers.loggerStart();
			
			query = session.createQuery("from Leave where entryTime=:entryTime and isActive='Y'");
			query.setParameter("entryTime", leave.getEntryTime());
			Leave leaveList = (Leave) query.uniqueResult();
			
			Loggers.loggerEnd(leaveList);
			return leaveList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void cancelSanctionLeave(Leave leave) throws GSmartDatabaseException {

		Loggers.loggerStart(leave);

		try {
			getConnection();
			// getting data from Apply_Leave & Leave_Details table
			Leave applyLeave = getLeave(leave);
			
			// Setting the data to updateSanctionLeave method....
			updateSanctionLeave(applyLeave);
		} catch (org.hibernate.exception.ConstraintViolationException e) {
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}finally {
			session.close();
		}

	}

	private void updateSanctionLeave(Leave applyLeave) throws GSmartDatabaseException {
		try {
			Loggers.loggerStart(applyLeave);
			getConnection();
			
			applyLeave.setUpdatedTime(CalendarCalculator.getTimeStamp());
			applyLeave.setLeaveStatus("Rejected*");

			
			session.update(applyLeave);
			transaction.commit();

		} catch (org.hibernate.exception.ConstraintViolationException e) {
		} catch (Throwable e) {
			transaction.rollback();
			throw new GSmartDatabaseException(e.getMessage());
		}
	}
}
