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

import com.gsmart.model.CompoundLeaveMaster;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.LeaveMaster;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class LeaveMasterDaoImpl implements LeaveMasterDao {

	@Autowired
	private SessionFactory sessionFactory;

	
	Query query;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getLeaveMasterList(Long hid, Integer min, Integer max)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		Criteria criteria = null;
		List<LeaveMaster> leavemasterlist = null;
		Map<String, Object> leavemasterMap = new HashMap<>();
		try {
			criteria = sessionFactory.getCurrentSession().createCriteria(LeaveMaster.class);
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			criteria.addOrder(Order.desc("daysAllow"));
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("hierarchy.hid", hid));
//			criteria.setProjection(Projections.id());

			leavemasterlist = criteria.list();
			Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(LeaveMaster.class);
			criteriaCount.add(Restrictions.eq("isActive", "Y"));
			criteriaCount.add(Restrictions.eq("hierarchy.hid", hid));
			
			criteriaCount.setProjection(Projections.rowCount());
			Long count = (Long) criteriaCount.uniqueResult();
			leavemasterMap.put("totallist", count);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());

		} 
		Loggers.loggerEnd(leavemasterlist);
		leavemasterMap.put("leavemasterlist", leavemasterlist);
		return leavemasterMap;
	}
	@Override
	public CompoundLeaveMaster addLeaveMaster(LeaveMaster leaveMaster) throws GSmartDatabaseException {
		Loggers.loggerStart();
		CompoundLeaveMaster cb = null;
		Session session=this.sessionFactory.getCurrentSession();
		
		try {
			LeaveMaster leaveMaster2=fetch2(leaveMaster);
			if (leaveMaster2 == null) {
				leaveMaster.setEntryTime((CalendarCalculator.getTimeStamp()));
				leaveMaster.setIsActive("Y");
				cb = (CompoundLeaveMaster) session.save(leaveMaster);
			}

		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd();
		return cb;
	}

	public void deleteLeaveMaster(LeaveMaster leaveMaster) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		try {
			

			leaveMaster.setExitTime(CalendarCalculator.getTimeStamp());
			leaveMaster.setIsActive("D");
			session.update(leaveMaster);

		} catch (Exception e) {
			e.printStackTrace();
		} 
		Loggers.loggerEnd();
	}

	@Override
	public LeaveMaster editLeaveMaster(LeaveMaster leaveMaster) throws GSmartDatabaseException {
		Loggers.loggerStart();
		LeaveMaster ch=null;
		try {
			
			LeaveMaster oldleaveMaster= getLeaveMaster(leaveMaster.getEntryTime(),leaveMaster.getHierarchy());
			ch = updateLeaveMaster(oldleaveMaster, leaveMaster);
			addLeaveMaster(leaveMaster);

		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());

		}
		return ch;
	}
	
	private LeaveMaster updateLeaveMaster(LeaveMaster oldLeaveMaster, LeaveMaster leaveMaster) throws GSmartDatabaseException {

		Session session=this.sessionFactory.getCurrentSession();
		LeaveMaster ch = null;
		try {
			if(oldLeaveMaster.getLeaveType().equals(leaveMaster.getLeaveType())){
				
					oldLeaveMaster.setUpdateTime(CalendarCalculator.getTimeStamp());
					oldLeaveMaster.setIsActive("N");
					session.update(oldLeaveMaster);

					return oldLeaveMaster;

				
			}else{
				LeaveMaster leaveMaster1 = fetch2(leaveMaster);
				if (leaveMaster1 == null) {
					oldLeaveMaster.setUpdateTime(CalendarCalculator.getTimeStamp());
					oldLeaveMaster.setIsActive("N");
					session.update(oldLeaveMaster);

					return oldLeaveMaster;
				
			}
			}
			
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		return ch;
	
	}
	
	
	
	private LeaveMaster fetch2(LeaveMaster leaveMaster) {
		Loggers.loggerStart();
	
		query = sessionFactory.getCurrentSession().createQuery(
				"FROM LeaveMaster WHERE leaveType=:leaveType  AND isActive=:isActive and hierarchy.hid=:hierarchy");
	
	query.setParameter("isActive", "Y");
	query.setParameter("hierarchy", leaveMaster.getHierarchy().getHid());
	query.setParameter("leaveType", leaveMaster.getLeaveType());
	LeaveMaster leaveMaster2 = (LeaveMaster) query.uniqueResult();
	Loggers.loggerEnd();
		return leaveMaster2;
	}

	public LeaveMaster getLeaveMaster(String entryTime,Hierarchy hierarchy) {
		try {


				query = sessionFactory.getCurrentSession().createQuery("from LeaveMaster where isActive=:isActive and entryTime=:entryTime and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			
			query.setParameter("entryTime",entryTime);
		     
		     query.setParameter("isActive","Y");
			 LeaveMaster leaveMaster = ( LeaveMaster) query.uniqueResult();
			
			
			return leaveMaster;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*private void getconnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();

	}*/
	
	public LeaveMaster getLeaveMasterByType(String role,Hierarchy hierarchy,String leaveType){
		Loggers.loggerStart();
		LeaveMaster leaveMaster=null;
		try {
				query=sessionFactory.getCurrentSession().createQuery("from LeaveMaster where leaveType=:leaveType and isActive='Y' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			
			query.setParameter("leaveType", leaveType);
			
			leaveMaster=(LeaveMaster) query.uniqueResult();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return leaveMaster;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LeaveMaster> getLeaveMasterListForApplyLeave(Long hid)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		Criteria criteria=null;
		List<LeaveMaster> masterList=null;
		try {
			criteria = sessionFactory.getCurrentSession().createCriteria(LeaveMaster.class);
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("hierarchy.hid", hid));
			masterList=criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(masterList);
		return masterList;
	}
	
}
