package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Leave;
import com.gsmart.model.LeaveDetails;
import com.gsmart.model.LeaveMaster;
import com.gsmart.model.Profile;
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

	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}

	@SuppressWarnings("unchecked")
	@Override

	public List<Leave> getLeavelist(Profile profileInfo,Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Leave> leavelist = null;
		getConnection();
		try {
			
				query = session.createQuery(
						"FROM Leave WHERE reportingManagerId=:smartId and lower(leaveStatus)!='rejected*' and isActive='Y' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hid);
				query.setParameter("smartId", profileInfo.getSmartId());
			
			leavelist = query.list();

		} catch (Exception e) {
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
//			Hierarchy hierarchy = leave.getHierarchy();
			query = session.createQuery("from Leave where entryTime=:entryTime");
			leave.setLeaveStatus("Rejected*");
			session.update(leave);
			transaction.commit();
			session.close();
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
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();

		try {
			Loggers.loggerStart();
			Leave applyLeave = getLeave(leave);
//			LeaveDetails oldLeaveDetail = getLeaveDetails(leave.getSmartId(), leave.getLeaveType());
			updateLeave(applyLeave);
		} catch (org.hibernate.exception.ConstraintViolationException e) {
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}finally {
			session.close();
		}
	}

	private void updateLeave(Leave applyLeave) throws GSmartDatabaseException {
		Loggers.loggerStart(applyLeave);
		System.out.println("in side update");

		
		applyLeave.setUpdatedTime(CalendarCalculator.getTimeStamp());
		applyLeave.setLeaveStatus("Sanctioned");

		
		session.update(applyLeave);
		transaction.commit();
	}

	public Leave getLeave(Leave leave) {
		try {
			Loggers.loggerStart();
			
			query = session.createQuery("from Leave where entryTime=:entryTime and isActive='Y'");
			query.setParameter("entryTime", leave.getEntryTime());
			Leave leaveList = (Leave) query.uniqueResult();
			transaction.commit();
			Loggers.loggerEnd(leaveList);
			return leaveList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public LeaveDetails getLeaveDetails(String smartId, String leaveType) {
		try {
			Loggers.loggerStart();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createQuery(
					"from LeaveDetails where leaveType='" + leaveType + "' and  smartId='" + smartId + "'");
			LeaveDetails leaveDetailList = (LeaveDetails) query.uniqueResult();
			transaction.commit();
			session.close();
			System.out.println(leaveDetailList);
			Loggers.loggerEnd(leaveDetailList);
			return leaveDetailList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public LeaveMaster getLeaveMaster(String leaveType) {
		try {
			Loggers.loggerStart();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createQuery("from LeaveMaster where leaveType='" + leaveType + "'");
			@SuppressWarnings("unchecked")
			ArrayList<LeaveMaster> leaveMasterList = (ArrayList<LeaveMaster>) query.list();
			transaction.commit();
			session.close();
			Loggers.loggerEnd(leaveMasterList);
			return leaveMasterList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void cancelSanctionLeave(Leave leave) throws GSmartDatabaseException {

		Loggers.loggerStart(leave);

		try {
			Loggers.loggerStart();
			// getting data from Apply_Leave & Leave_Details table
			Leave applyLeave = getLeave(leave);
			LeaveDetails oldLeaveDetail = getLeaveDetails(leave.getSmartId(), leave.getLeaveType());

			// Setting the data to updateSanctionLeave method....
			updateSanctionLeave(applyLeave, oldLeaveDetail);
		} catch (org.hibernate.exception.ConstraintViolationException e) {
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}

	}

	private void updateSanctionLeave(Leave applyLeave, LeaveDetails leaveDetail) throws GSmartDatabaseException {
		try {
			Loggers.loggerStart(applyLeave);
			Loggers.loggerStart(leaveDetail);
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();


			applyLeave.setUpdatedTime(CalendarCalculator.getTimeStamp());
			applyLeave.setLeaveStatus("Rejected*");

			
			session.update(applyLeave);
			transaction.commit();

		} catch (org.hibernate.exception.ConstraintViolationException e) {
		} catch (Throwable e) {
			transaction.rollback();
			throw new GSmartDatabaseException(e.getMessage());
		}

		finally {
			session.close();
		}

	}
}
