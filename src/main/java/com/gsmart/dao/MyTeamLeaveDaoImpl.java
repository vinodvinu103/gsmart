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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.Leave;
import com.gsmart.model.Profile;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class MyTeamLeaveDaoImpl implements MyTeamLeaveDao {
	@Autowired
	private SessionFactory sessionFactory;

	private Query query;
	private Criteria criteria=null;

	/*public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}*/

	@Override
	public Map<String, Object> getLeavelist(Profile profileInfo, Long hierarchy, Integer min, Integer max)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		Map<String, Object> leavelist = new HashMap<>();
		Criteria criteriaCount = null;
		try {
			String role = profileInfo.getRole();
			if (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("director") || role.equalsIgnoreCase("hr")) {
				criteria = sessionFactory.getCurrentSession().createCriteria(Leave.class);
				criteria.add(Restrictions.eq("isActive", "Y"));
				criteria.addOrder(Order.asc("fullName"));
				criteria.add(Restrictions.eq("hierarchy.hid", hierarchy));
				criteria.setFirstResult(min);
				criteria.setMaxResults(max);
				criteria.addOrder(Order.desc("entryTime"));
				leavelist.put("myTeamLeaveList", criteria.list());

				criteriaCount = sessionFactory.getCurrentSession().createCriteria(Leave.class)
						.add(Restrictions.eq("isActive", "Y")).add(Restrictions.eq("hierarchy.hid", hierarchy))
						.setProjection(Projections.rowCount());
				Long count = (Long) criteriaCount.uniqueResult();
				leavelist.put("totalListCount", count);
			} else {
				criteria = sessionFactory.getCurrentSession().createCriteria(Leave.class);
				criteria.add(Restrictions.eq("isActive", "Y"));
				criteria.add(Restrictions.ne("leaveStatus", "Rejected*").ignoreCase());
				criteria.add(Restrictions.eq("reportingManagerId", profileInfo.getSmartId()));
				criteria.add(Restrictions.eq("hierarchy.hid", hierarchy));
				criteria.addOrder(Order.asc("fullName"));
				criteria.setFirstResult(min);
				criteria.setMaxResults(max);
				leavelist.put("myTeamLeaveList", criteria.list());

				criteriaCount = sessionFactory.getCurrentSession().createCriteria(Leave.class)
						.add(Restrictions.eq("isActive", "Y"))
						.add(Restrictions.eq("reportingManagerId", profileInfo.getSmartId()))
						.add(Restrictions.ne("leaveStatus", "Rejected*").ignoreCase())
						.add(Restrictions.eq("hierarchy.hid", hierarchy)).setProjection(Projections.rowCount());
				Long count = (Long) criteria.uniqueResult();
				leavelist.put("totalListCount", count);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd(leavelist);
		return leavelist;
	}

	@Override
	public void rejectleave(Leave leave) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		try {
			leave.setExitTime(CalendarCalculator.getTimeStamp());
			leave.setLeaveStatus("Rejected*");
			session.update(leave);
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
			Leave applyLeave = getLeave(leave);
			updateLeave(applyLeave);
			Loggers.loggerEnd();
		} catch (org.hibernate.exception.ConstraintViolationException e) {
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
	}

	private void updateLeave(Leave applyLeave) throws GSmartDatabaseException {
		Loggers.loggerStart(applyLeave);
		Session session=this.sessionFactory.getCurrentSession();

		applyLeave.setUpdatedTime(CalendarCalculator.getTimeStamp());
		applyLeave.setLeaveStatus("Sanctioned");
		session.update(applyLeave);
		
		Loggers.loggerEnd();
	}

	public Leave getLeave(Leave leave) {
		try {
			Loggers.loggerStart();
			
			query = sessionFactory.getCurrentSession().createQuery("from Leave where entryTime=:entryTime and isActive='Y'");
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
			// getting data from Apply_Leave & Leave_Details table
			Leave applyLeave = getLeave(leave);
			
			// Setting the data to updateSanctionLeave method....
			updateSanctionLeave(applyLeave);
		} catch (org.hibernate.exception.ConstraintViolationException e) {
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}

	}

	private void updateSanctionLeave(Leave applyLeave) throws GSmartDatabaseException {
		try {
			Session session=this.sessionFactory.getCurrentSession();
			Loggers.loggerStart(applyLeave);
			
			applyLeave.setUpdatedTime(CalendarCalculator.getTimeStamp());
			applyLeave.setLeaveStatus("Rejected*");

			
			session.update(applyLeave);

		} catch (org.hibernate.exception.ConstraintViolationException e) {
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Leave> searchmyteamleave(Leave leave, Long hid) throws GSmartDatabaseException {
		List<Leave> leavelist = null;
		try{
			if(hid!=null){
			query = sessionFactory.getCurrentSession().createQuery("from Leave where isActive = 'Y' and fullName like '%"+leave.getFullName()+"%' and hierarchy.hid = :hierarchy ");
			query.setParameter("hierarchy", hid);
			}else{
				query = sessionFactory.getCurrentSession().createQuery("from Leave where isActive = 'Y' and fullName like '%"+leave.getFullName()+"%'");
			}
			leavelist = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return leavelist;
	}
}
