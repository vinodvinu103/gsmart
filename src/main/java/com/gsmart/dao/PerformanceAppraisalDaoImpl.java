package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.CompoundPerformanceAppraisal;
import com.gsmart.model.Inventory;
import com.gsmart.model.PerformanceAppraisal;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class PerformanceAppraisalDaoImpl implements PerformanceAppraisalDao {

	@Autowired
	SessionFactory sessionFactory;
	Session session;
	Query query;
	Transaction transaction;

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformanceAppraisal> getAppraisalList(String smartId, String year) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<PerformanceAppraisal> appraisalList = null;
		try {
			getConnection();
			System.out.println();
			Loggers.loggerStart();
			query = session.createQuery(
					"from PerformanceAppraisal where isActive=:isActive AND smartId=:smartId AND year=:year");
			query.setParameter("smartId", smartId);
			query.setParameter("year", year);
			query.setParameter("isActive", "Y");
			appraisalList = (List<PerformanceAppraisal>) query.list();
			Loggers.loggerEnd(appraisalList);
           
		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		return appraisalList;
	}

	@Override
	public CompoundPerformanceAppraisal addAppraisal(PerformanceAppraisal appraisal) throws GSmartDatabaseException {
		CompoundPerformanceAppraisal ca = null;
		try {
			getConnection();
			query = session.createQuery("FROM PerformanceAppraisal WHERE smartId=:smartId AND year=:year");
			query.setParameter("smartId", appraisal.getSmartId());
			query.setParameter("year", appraisal.getYear());

			PerformanceAppraisal oldAppraisal = (PerformanceAppraisal) query.uniqueResult();
			Loggers.loggerStart(oldAppraisal);
			if (oldAppraisal == null) {
				appraisal.setEntryTime(CalendarCalculator.currentEpoch);

				ca = (CompoundPerformanceAppraisal) session.save(appraisal);
				Loggers.loggerEnd(appraisal);
				Loggers.loggerEnd(ca);
			}

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
		return ca;
	}

	@Override
	public void editAppraisal(PerformanceAppraisal appraisal) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			getConnection();
			PerformanceAppraisal oldAppraisal = getAppraisal(appraisal.getEntryTime());
			oldAppraisal.setIsActive("N");
			oldAppraisal.setUpdateTime(CalendarCalculator.currentEpoch);
			session.update(oldAppraisal);
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

	public PerformanceAppraisal getAppraisal(Long entryTime) {
		try {

			query = session.createQuery("from PerformanceAppraisal where isActive=:isActive and entryTime=:entryTime");
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

	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}

}