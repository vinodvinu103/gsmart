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
	public List<PerformanceRecord> getPerformanceRecord(PerformanceAppraisal appraisal,String smartId) throws GSmartDatabaseException {
		List<PerformanceRecord> performancerecordList=null; 
		Loggers.loggerStart();
		try {
			getConnection();
			System.out.println();
			Loggers.loggerStart();
			query = session.createQuery("from PerformanceRecord where smartId=:smartId AND year=:year AND isActive=:isActive");
			query.setParameter("smartId",smartId);
			query.setParameter("year", appraisal.getYear());
			query.setParameter("isActive", "y");
			performancerecordList=(List<PerformanceRecord>)query.list();
			Loggers.loggerEnd(performancerecordList);
			
			} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		return performancerecordList;
	}
	
	
	
		public void getConnection() {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
		}



		@Override
		public void addAppraisalRecord(PerformanceAppraisal appraisal) throws GSmartDatabaseException {
			Loggers.loggerStart();
			try {
				getConnection();
				appraisal.setEntryTime(CalendarCalculator.currentEpoch);
				 session.save(appraisal);
				
				 Loggers.loggerEnd(appraisal);
				 transaction.commit();
			}
			 catch (ConstraintViolationException e) {
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
			try {
				getConnection();
				PerformanceRecord	 oldAppraisalrecord = getAppraisalRecord(appraisal.getEntryTime());
				oldAppraisalrecord.setIsActive("N");
				oldAppraisalrecord.setUpdateTime(CalendarCalculator.currentEpoch);
				session.update(oldAppraisalrecord);
				appraisal.setEntryTime(CalendarCalculator.currentEpoch);
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
			try {
				getConnection();

				appraisal.setExitTime(CalendarCalculator.currentEpoch);
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
		
		}

