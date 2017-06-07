package com.gsmart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.model.PerformanceRecord;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class PerformanceRecordDaoImpl implements PerformanceRecordDao {
	@Autowired
	SessionFactory sessionFactory;
	Query query;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPerformanceRecord(String smartId, String year, Long hid, String reportingId)
			throws GSmartDatabaseException {

		Map<String, Object> recordobject = new HashMap<>();
		Loggers.loggerStart(smartId);
		Loggers.loggerStart(reportingId);
		List<PerformanceRecord> performancerecordList = null;
		List<PerformanceRecord> performancerecordList1 = getRecord(reportingId, smartId, year, hid);
		try {

			System.out.println();
			Loggers.loggerStart();
			/*query = sessionFactory.getCurrentSession()
					.createQuery("from PerformanceRecord where smartId=:smartId AND year=:year AND isActive=:isActive");
			query.setParameter("smartId", smartId);
			query.setParameter("year", year);
			query.setParameter("isActive", "y");
			performancerecordList = (List<PerformanceRecord>) query.list();
			Loggers.loggerEnd(performancerecordList);*/

			query = sessionFactory.getCurrentSession().createQuery(
					"from PerformanceRecord where smartId=:smartId AND reportingManagerID=null AND  year=:year AND isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);

			query.setParameter("smartId", smartId);
			query.setParameter("year", year);
			query.setParameter("isActive", "Y");
			performancerecordList = (List<PerformanceRecord>) query.list();
			Loggers.loggerEnd(performancerecordList);

		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
		recordobject.put("owncomments", performancerecordList);
		recordobject.put("managercomments", performancerecordList1);
		return recordobject;
	}

	@SuppressWarnings("unchecked")
	public List<PerformanceRecord> getRecord(String reportingId, String smartId, String year, Long hid) {
		List<PerformanceRecord> performancerecordList11 = null;
		Loggers.loggerStart();

		Loggers.loggerValue("reportingId", reportingId);

		try {
			System.out.println("smartId" + smartId);
			Loggers.loggerStart();

			query = sessionFactory.getCurrentSession().createQuery(
					"from PerformanceRecord where smartId=:smartId AND  reportingManagerID=:reportingManagerID AND year=:year AND isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);

			query.setParameter("smartId", smartId);
			query.setParameter("year", year);
			query.setParameter("isActive", "Y");
			query.setParameter("reportingManagerID", reportingId);
			performancerecordList11 = (List<PerformanceRecord>) query.list();
			Loggers.loggerEnd(performancerecordList11);

		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();

		return performancerecordList11;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPerformanceRecordManager(String reportingManagerId, String smartId, String year,
			Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Map<String, Object> recordobject = new HashMap<>();
		List<PerformanceRecord> performancerecordList = null;

		List<PerformanceRecord> performancerecordList11 = getManagerRecord(reportingManagerId, smartId, year, hid);

		try {

			Loggers.loggerStart();

			query = sessionFactory.getCurrentSession().createQuery(
					"from PerformanceRecord where smartId=:smartId and reportingManagerID=null  AND year=:year AND isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);

			// query.setParameter("reportingManagerID", reportingManagerId);
			query.setParameter("smartId", smartId);
			query.setParameter("year", year);
			query.setParameter("isActive", "Y");
			performancerecordList = (List<PerformanceRecord>) query.list();
			Loggers.loggerEnd(performancerecordList);

		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
		recordobject.put("teammemberscomments", performancerecordList);

		recordobject.put("managercomments", performancerecordList11);
		return recordobject;

	}

	@SuppressWarnings("unchecked")
	public List<PerformanceRecord> getManagerRecord(String reportingManagerId, String smartId, String year, Long hid) {
		List<PerformanceRecord> performancerecordList1 = null;
		Loggers.loggerStart();
		Loggers.loggerValue("reportingId", reportingManagerId);

		try {
			System.out.println("smartId" + smartId);
			Loggers.loggerStart();

			query = sessionFactory.getCurrentSession().createQuery(
					"from PerformanceRecord where smartId=:smartId AND  reportingManagerID=:reportingManagerID AND year=:year AND isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);

			query.setParameter("smartId", smartId);
			query.setParameter("year", year);
			query.setParameter("isActive", "Y");
			query.setParameter("reportingManagerID", reportingManagerId);
			performancerecordList1 = (List<PerformanceRecord>) query.list();
			Loggers.loggerEnd(performancerecordList1);

		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
		return performancerecordList1;
	}

	@Override
	public void addAppraisalRecord(PerformanceRecord appraisal) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Loggers.loggerStart(appraisal);

		try {

			appraisal.setEntryTime(CalendarCalculator.getCurrentEpochTime());
			appraisal.setIsActive("Y");

			sessionFactory.getCurrentSession().save(appraisal);

		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
	}

	@Override
	public void addAppraisalRecordManager(PerformanceRecord appraisal) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Loggers.loggerStart(appraisal);

		try {

			appraisal.setEntryTime(CalendarCalculator.getCurrentEpochTime());
			appraisal.setIsActive("Y");

			sessionFactory.getCurrentSession().save(appraisal);

		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}

	}

	@Override
	public void editAppraisalRecord(PerformanceAppraisal appraisal) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {

			PerformanceRecord oldAppraisalrecord = getAppraisalRecord(appraisal.getEntryTime());
			oldAppraisalrecord.setIsActive("N");
			oldAppraisalrecord.setUpdateTime(CalendarCalculator.getCurrentEpochTime());
			sessionFactory.getCurrentSession().update(oldAppraisalrecord);
			appraisal.setEntryTime(CalendarCalculator.getCurrentEpochTime());
			appraisal.setIsActive("Y");
			sessionFactory.getCurrentSession().save(appraisal);

		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());

		}

	}

	public PerformanceRecord getAppraisalRecord(Long entryTime) {
		try {

			query = sessionFactory.getCurrentSession()
					.createQuery("from PerformanceRecord where isActive=:isActive and entryTime=:entryTime");
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

			appraisal.setExitTime(CalendarCalculator.getCurrentEpochTime());
			appraisal.setIsActive("D");
			sessionFactory.getCurrentSession().update(appraisal);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getrating(String year, Long hid) throws GSmartDatabaseException {
		Map<String, Object> rating= new HashMap<>();
		List<PerformanceRecord> performancerecordratingList = null;
		try {

			System.out.println();
			Loggers.loggerStart();
		    query = sessionFactory.getCurrentSession().createQuery(
					"from PerformanceRecord where ratings!=null AND reportingManagerID!=null AND  year=:year AND isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);

			
			query.setParameter("year", year);
			query.setParameter("isActive", "Y");
			performancerecordratingList = (List<PerformanceRecord>) query.list();
			Loggers.loggerEnd(performancerecordratingList);

		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
		rating.put("ratings", performancerecordratingList);
		
		return rating;
		
	}

	

}
