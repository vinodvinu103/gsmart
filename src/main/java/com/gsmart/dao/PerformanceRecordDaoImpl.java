package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
	public List<PerformanceRecord> getPerformanceRecord(String smartId, String year) throws GSmartDatabaseException {
		List<PerformanceRecord> performancerecordList = null;
		Loggers.loggerStart();
		getConnection();
		try {
			
			System.out.println();
			Loggers.loggerStart();
			query = session
					.createQuery("from PerformanceRecord where smartId=:smartId AND year=:year AND isActive=:isActive");
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
		return performancerecordList;
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
