package com.gsmart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.model.PerformanceRecord;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class PerformanceRecordDaoImpl implements PerformanceRecordDao {
	@Autowired
	SessionFactory sessionFactory;
	Session session;
	Query query;
	Transaction transaction;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPerformanceRecord(String smartId, String year,String role,Hierarchy hierarchy,String reportingId) throws GSmartDatabaseException {
		
		Map<String, Object> recordobject=new HashMap<>();
		Loggers.loggerStart(smartId);
		getConnection();
		List<PerformanceRecord> performancerecordList = null;
		List<PerformanceRecord> performancerecordList1=getRecord( reportingId,  smartId,  year, role,  hierarchy);
		try {
			
			System.out.println(smartId);
			Loggers.loggerStart();
			if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("director") || role.equalsIgnoreCase("owner"))
			{
				query = session
						.createQuery("from PerformanceRecord where smartId=:smartId AND year=:year AND isActive=:isActive");
			}else{
			query = session
					.createQuery("from PerformanceRecord where smartId=:smartId AND reportingManagerID=null AND  year=:year AND isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			}
			query.setParameter("smartId", smartId);
			query.setParameter("year", year);
			query.setParameter("isActive", "Y");
			performancerecordList = (List<PerformanceRecord>) query.list();
			Loggers.loggerEnd(performancerecordList);

		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		 recordobject.put("owncomments", performancerecordList);
		 recordobject.put("managercomments", performancerecordList1);
		return recordobject;
	}
	
	public List<PerformanceRecord> getRecord(String reportingId, String smartId, String year,
			String role, Hierarchy hierarchy) {
		List<PerformanceRecord> performancerecordList11=null;
		Loggers.loggerStart();
		
		Loggers.loggerValue("reportingId", reportingId);

		try {
			System.out.println("smartId"+smartId);
			Loggers.loggerStart();
			
			if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("director") || role.equalsIgnoreCase("owner"))
			{
				query = session
						.createQuery("from PerformanceRecord where year=:year AND isActive=:isActive");
			}else{
			
		query = session
				.createQuery("from PerformanceRecord where smartId=:smartId AND  reportingManagerID=:reportingManagerID AND year=:year AND isActive=:isActive and hierarchy.hid=:hierarchy");
		query.setParameter("hierarchy", hierarchy.getHid());
			}
		query.setParameter("smartId", smartId);
		query.setParameter("year", year);
		query.setParameter("isActive", "Y");
		query.setParameter("reportingManagerID", reportingId);
		performancerecordList11 = (List<PerformanceRecord>) query.list();
		Loggers.loggerEnd(performancerecordList11);
		
		}
		 catch (Exception e) {
				Loggers.loggerException(e.getMessage());
			} 
			Loggers.loggerEnd();
			
			return performancerecordList11;
	}
	
	
	
	
	
	@Override
	public Map<String, Object> getPerformanceRecordManager(String reportingManagerId, String smartId, String year,
			String role, Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Map<String, Object> recordobject=new HashMap<>();
		getConnection();
		List<PerformanceRecord> performancerecordList = null;
		
		List<PerformanceRecord> performancerecordList11=getManagerRecord( reportingManagerId,  smartId,  year, role,  hierarchy);

	
		
		try {
			
			Loggers.loggerStart();
			if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("director") || role.equalsIgnoreCase("owner"))
			{
				query = session
						.createQuery("from PerformanceRecord where year=:year AND isActive=:isActive");
			}
			else{
				
			query = session
					.createQuery("from PerformanceRecord where smartId=:smartId and reportingManagerID=null  AND year=:year AND isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			
			}
		//	query.setParameter("reportingManagerID", reportingManagerId);
			query.setParameter("smartId", smartId);
			query.setParameter("year", year);
			query.setParameter("isActive", "Y");
			performancerecordList = (List<PerformanceRecord>) query.list();
			Loggers.loggerEnd(performancerecordList);

		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		recordobject.put("teammemberscomments", performancerecordList);
	
		recordobject.put("managercomments",performancerecordList11);
		return recordobject;
		
	}
	
	
	
	

	public List<PerformanceRecord> getManagerRecord(String reportingManagerId, String smartId, String year,
			String role, Hierarchy hierarchy) {
		List<PerformanceRecord> performancerecordList1=null;
		Loggers.loggerStart();
		Loggers.loggerValue("reportingId", reportingManagerId);

		try {
			System.out.println("smartId"+smartId);
			System.out.println();
			Loggers.loggerStart();
			if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("director") || role.equalsIgnoreCase("owner"))
			{
				query = session
						.createQuery("from PerformanceRecord where year=:year AND isActive=:isActive");
			}else{
			
		query = session
				.createQuery("from PerformanceRecord where smartId=:smartId AND  reportingManagerID=:reportingManagerID AND year=:year AND isActive=:isActive and hierarchy.hid=:hierarchy");
		query.setParameter("hierarchy", hierarchy.getHid());
			}
		query.setParameter("smartId", smartId);
		query.setParameter("year", year);
		query.setParameter("isActive", "Y");
		query.setParameter("reportingManagerID", reportingManagerId);
		performancerecordList1 = (List<PerformanceRecord>) query.list();
		Loggers.loggerEnd(performancerecordList1);
		
		}
		 catch (Exception e) {
				Loggers.loggerException(e.getMessage());
			} 
			Loggers.loggerEnd();
			return performancerecordList1;
	}

	@Override
	public void addAppraisalRecord(PerformanceRecord appraisal) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Loggers.loggerStart(appraisal);
		
		getConnection();
		try {
			
		
			appraisal.setEntryTime(CalendarCalculator.getCurrentEpochTime());
			appraisal.setIsActive("Y");
			
			
            session.save(appraisal);
            
			
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
	}
	
	
	@Override
	public void addAppraisalRecordManager(PerformanceRecord appraisal) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Loggers.loggerStart(appraisal);
		
		getConnection();
		try {
			
		
			appraisal.setEntryTime(CalendarCalculator.getCurrentEpochTime());
			appraisal.setIsActive("Y");
			
			
            session.save(appraisal);
            
			
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
		
	}

	
	
	
	

	@Override
	public void editAppraisalRecord(PerformanceAppraisal appraisal) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		try {
			
			PerformanceRecord oldAppraisalrecord = getAppraisalRecord(appraisal.getEntryTime());
			oldAppraisalrecord.setIsActive("N");
			oldAppraisalrecord.setUpdateTime(CalendarCalculator.getCurrentEpochTime());
			session.update(oldAppraisalrecord);
			appraisal.setEntryTime(CalendarCalculator.getCurrentEpochTime());
			appraisal.setIsActive("Y");
			session.save(appraisal);
			transaction.commit();
			session.close();

		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());

		}

	}

	public PerformanceRecord getAppraisalRecord(Long entryTime) {
		try {

			query = session.createQuery("from PerformanceRecord where isActive=:isActive and entryTime=:entryTime");
			query.setParameter("entryTime", entryTime);
			query.setParameter("isActive", "Y");
			PerformanceRecord appraisalrecord = (PerformanceRecord) query.uniqueResult();

			return appraisalrecord;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void deletAppraisalRecord(PerformanceAppraisal appraisal) throws GSmartDatabaseException {

		Loggers.loggerStart();
		getConnection();
		try {
		

			appraisal.setExitTime(CalendarCalculator.getCurrentEpochTime());
			appraisal.setIsActive("D");
			session.update(appraisal);
			transaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
	}

	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}









}
