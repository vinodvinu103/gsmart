package com.gsmart.dao;


import java.util.ArrayList;
import java.util.List;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.Leave;
import com.gsmart.model.LeaveDetails;
import com.gsmart.model.LeaveMaster;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;
@Repository
public class MyTeamLeaveDaoImpl  implements MyTeamLeaveDao{
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
	public List<Leave> getLeavelist(String role,Hierarchy hierarchy) throws GSmartDatabaseException{
		Loggers.loggerStart();
		List<Leave> leavelist=null;
		getConnection();
		try {
			if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director"))
			{

			query = session.createQuery("FROM Leave WHERE isActive='Y'");
			}else{
				query = session.createQuery("FROM Leave WHERE isActive='Y' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
			leavelist = (List<Leave>)query.list();

		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		} finally {

			session.close();
		}
		Loggers.loggerEnd();
		return leavelist;
	}
	@Override
	public void rejectleave(Leave leave)throws GSmartDatabaseException{
		Loggers.loggerStart();
		getConnection();
	try {
	    Hierarchy hierarchy=leave.getHierarchy();
		query = session.createQuery("from Leave where entryTime=:entryTime");
		leave.setLeaveStatus("rejected");
		session.update(leave);
		transaction.commit();
		session.close();
		Loggers.loggerEnd();
	 } catch (Exception e) {
				e.printStackTrace();
			}
	
}
	
	@Override
	public void sactionleave(Leave leave) throws GSmartDatabaseException {
		Loggers.loggerStart(leave);

		try {
			Loggers.loggerStart();

			Leave applyLeave = getLeave(leave.getSmartId(), leave.getLeaveType());

//			LeaveMaster leaveMaster = getLeaveMaster(leave.getLeaveType());

			LeaveDetails oldLeaveDetail = getLeaveDetails(leave.getSmartId(), leave.getLeaveType());
			/*if (oldLeaveDetail == null) {
				updateLeave(applyLeave, addLeaveDetail(leaveMaster, applyLeave, oldLeaveDetail), leaveMaster);
			} else {
				updateLeave(applyLeave, oldLeaveDetail, leaveMaster);
			}*/
			updateLeave(applyLeave, oldLeaveDetail);
			
//			Loggers.loggerValue(leave);
		} catch (org.hibernate.exception.ConstraintViolationException e) {
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
	}

	private void updateLeave(Leave applyLeave, LeaveDetails leaveDetail)
			throws GSmartDatabaseException {
		Loggers.loggerStart(applyLeave);
		Loggers.loggerStart(leaveDetail);
		System.out.println("in side update");
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();

		query = session.createQuery(
				"UPDATE LeaveDetails SET appliedLeaves=:appliedLeaves WHERE smartId=:smartId and leaveType=:leaveType");
		query.setParameter("smartId", applyLeave.getSmartId());
		query.setParameter("appliedLeaves", applyLeave.getNumberOfDays());
		query.setParameter("leaveType", applyLeave.getLeaveType());
//		Loggers.loggerValue(query);
		applyLeave.setUpdatedTime(CalendarCalculator.getTimeStamp());
		applyLeave.setLeaveStatus("Sanction");
		
		int i = (leaveDetail.getLeftLeaves() - applyLeave.getNumberOfDays());
		leaveDetail.setLeftLeaves(i);
		
		session.update(leaveDetail);
		session.update(applyLeave);
		query.executeUpdate();
		transaction.commit();
		session.close();
	}
	
	public Leave getLeave(String smartId, String leaveType) {
		try {
			Loggers.loggerStart();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createQuery(
					"from Leave where leaveType='" + leaveType + "' and isActive='Y' and smartId='" + smartId + "'");
			Leave bandList = (Leave) query.uniqueResult();
			transaction.commit();
			session.close();
			Loggers.loggerEnd(bandList);
			return bandList;
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
			Leave applyLeave = getLeave(leave.getSmartId(), leave.getLeaveType());
			LeaveDetails oldLeaveDetail = getLeaveDetails(leave.getSmartId(), leave.getLeaveType());

			// Setting the data to updateSanctionLeave method....
			updateSanctionLeave(applyLeave, oldLeaveDetail);

//			Loggers.loggerValue(leave);
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

			query = session.createQuery(
					"UPDATE LeaveDetails SET appliedLeaves=:appliedLeaves WHERE smartId=:smartId and leaveType=:leaveType");
			query.setParameter("smartId", applyLeave.getSmartId());
			query.setParameter("appliedLeaves", applyLeave.getNumberOfDays());
			query.setParameter("leaveType", applyLeave.getLeaveType());
			
			applyLeave.setUpdatedTime(CalendarCalculator.getTimeStamp());
			applyLeave.setLeaveStatus("Rejected*");

			int i = (leaveDetail.getLeftLeaves() + applyLeave.getNumberOfDays());
			leaveDetail.setLeftLeaves(i);

			session.update(leaveDetail);
			session.update(applyLeave);
			query.executeUpdate();
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

		

		/*try {
			Loggers.loggerStart();
			Leave applyLeave = getLeave(leave.getSmartId(), leave.getLeaveType());
			LeaveDetails oldLeaveDetail = getLeaveDetails(leave.getSmartId(), leave.getLeaveType());
			applyLeave.setUpdatedTime(CalendarCalculator.getTimeStamp());
			updateLeave(applyLeave,oldLeaveDetail);
			Loggers.loggerValue(leave);
		} catch (org.hibernate.exception.ConstraintViolationException e) {
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
	}

	private void updateLeave(Leave applyLeave, LeaveDetails leaveDetail) {
		System.out.println("in side update");
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		query=session.createQuery("UPDATE LeaveDetails SET appliedLeaves=:appliedLeaves WHERE smartId=:smartId and leaveType=:leaveType");
		query.setParameter("smartId", applyLeave.getSmartId());
		query.setParameter("appliedLeaves",applyLeave.getNumberOfDays());
		query.setParameter("leaveType",applyLeave.getLeaveType());
		Loggers.loggerValue(query);
		applyLeave.setUpdatedTime(CalendarCalculator.getTimeStamp());
		applyLeave.setLeaveStatus("Sanction");;
		
		int i=(leaveDetail.getLeftLeaves()-applyLeave.getNumberOfDays());
		leaveDetail.setLeftLeaves(i);
		
		session.update(leaveDetail);
		session.update(applyLeave);
		query.executeUpdate();
		transaction.commit();
		session.close();
	}

	public Leave getLeave(String smartId, String leaveType) {
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createQuery("from Leave where leaveType='"+leaveType+"' and isActive='Y' and smartId='" + smartId+ "'");
			@SuppressWarnings("unchecked")
			ArrayList<Leave> bandList = (ArrayList<Leave>) query.list();
			transaction.commit();
			session.close();
			return bandList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public LeaveDetails getLeaveDetails(String smartId,  String leaveType) {
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createQuery("from LeaveDetails where leaveType='"+leaveType+"' and  smartId='" + smartId+ "'");
			@SuppressWarnings("unchecked")
			ArrayList<LeaveDetails> leaveDetailList = (ArrayList<LeaveDetails>) query.list();
			transaction.commit();
			session.close();
			return leaveDetailList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
}
*/