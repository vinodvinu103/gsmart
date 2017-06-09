package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class PerformanceAppraisalDaoImpl implements PerformanceAppraisalDao {

	@Autowired
	private SessionFactory sessionFactory;

	private Query query;

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformanceAppraisal> getAppraisalList(String reportingId, String year, Long hid)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<PerformanceAppraisal> appraisalList = null;
		try {

			System.out.println();
			Loggers.loggerStart();
			query = sessionFactory.getCurrentSession().createQuery(
					"from PerformanceAppraisal where isActive=:isActive AND reportingManagerID=:reportingManagerID AND year=:year and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);

			query.setParameter("reportingManagerID", reportingId);
			query.setParameter("year", year);
			query.setParameter("isActive", "Y");
			appraisalList = (List<PerformanceAppraisal>) query.list();
			Loggers.loggerEnd(appraisalList);

		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
		return appraisalList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformanceAppraisal> getTeamAppraisalList(String smartId, String year, Long hid)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<PerformanceAppraisal> teamappraisalList = null;
		try {

			Loggers.loggerStart(smartId);
			Loggers.loggerStart(year);

			query = sessionFactory.getCurrentSession().createQuery(
					"from PerformanceAppraisal where isActive=:isActive AND reportingManagerID=:smartId AND year=:year and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);
			query.setParameter("smartId", smartId);
			query.setParameter("year", year);
			query.setParameter("isActive", "Y");
			teamappraisalList = (List<PerformanceAppraisal>) query.list();
			Loggers.loggerEnd(teamappraisalList);

		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		}
		Loggers.loggerEnd();
		return teamappraisalList;
	}

	/*public CompoundPerformanceAppraisal addAppraisal(PerformanceAppraisal appraisal) throws GSmartDatabaseException {
				CompoundPerformanceAppraisal ca = null;
				Session session=this.sessionFactory.getCurrentSession();
				try {
					query = sessionFactory.getCurrentSession().createQuery("FROM PerformanceAppraisal WHERE smartId=:smartId AND year=:year");
					query.setParameter("smartId", appraisal.getSmartId());
					query.setParameter("year", appraisal.getYear());

					PerformanceAppraisal oldAppraisal = (PerformanceAppraisal) query.uniqueResult();
					Loggers.loggerStart(oldAppraisal);
					if (oldAppraisal == null) {
						appraisal.setEntryTime(CalendarCalculator.currentEpoch);*/

	@Override
	public void addAppraisal(PerformanceAppraisal performanceAppraisal) throws GSmartDatabaseException {
		try {
			performanceAppraisal.setEntryTime(CalendarCalculator.getCurrentEpochTime());
			performanceAppraisal.setIsActive("Y");
			sessionFactory.getCurrentSession().save(performanceAppraisal);
			Loggers.loggerEnd(performanceAppraisal);
		

		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}

	}

	@Override
	public void editAppraisal(PerformanceAppraisal appraisal) throws GSmartDatabaseException {
		Loggers.loggerStart();

		Session session = this.sessionFactory.getCurrentSession();
		try {
			PerformanceAppraisal oldAppraisal = getAppraisal(appraisal.getEntryTime());
			oldAppraisal.setIsActive("N");
			oldAppraisal.setUpdateTime(CalendarCalculator.getCurrentEpochTime());
			session.update(oldAppraisal);
			appraisal.setEntryTime(CalendarCalculator.getCurrentEpochTime());
			appraisal.setIsActive("Y");
			session.save(appraisal);

		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());

		} 
		Loggers.loggerEnd();
	}

	public PerformanceAppraisal getAppraisal(Long entryTime) {
		Loggers.loggerStart(entryTime);
		try {

			query = sessionFactory.getCurrentSession()
					.createQuery("from PerformanceAppraisal where isActive=:isActive and entryTime=:entryTime");
			query.setParameter("entryTime", entryTime);
			query.setParameter("isActive", "Y");
			PerformanceAppraisal appraisal = (PerformanceAppraisal) query.uniqueResult();

			return appraisal;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void deleteAppraisal(PerformanceAppraisal appraisal) throws GSmartDatabaseException {

		Session session = this.sessionFactory.getCurrentSession();
		Loggers.loggerStart();
		try {

			appraisal.setExitTime(CalendarCalculator.getCurrentEpochTime());
			appraisal.setIsActive("D");
			session.update(appraisal);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
	}

	/*
	 * public void getConnection() { session = sessionFactory.openSession();
	 * transaction = session.beginTransaction(); }
	 */

}