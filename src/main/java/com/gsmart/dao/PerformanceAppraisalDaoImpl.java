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
import com.gsmart.model.Hierarchy;
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
	Session session = null;
	Transaction transaction = null;
	Query query;

	@SuppressWarnings("unchecked")
	@Override
	public List<PerformanceAppraisal> getAppraisalList(String reportingId,String year,String role,Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<PerformanceAppraisal> appraisalList = null;
		getConnection();
		try {
			
		
			Loggers.loggerStart(reportingId);
			Loggers.loggerStart(year);
			if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("director") || role.equalsIgnoreCase("owner"))
			{
			query = session.createQuery(
					
					"from PerformanceAppraisal where isActive=:isActive AND reportingManagerID=:reportingManagerID AND year=:year");
			
			}else{
				query = session.createQuery(
						
						"from PerformanceAppraisal where isActive=:isActive AND reportingManagerID=:reportingManagerID AND year=:year and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
			query.setParameter("reportingManagerID",reportingId);
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
	public List<PerformanceAppraisal> getTeamAppraisalList(String smartId, String year,String role,Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<PerformanceAppraisal> teamappraisalList = null;
		getConnection();
		try {
			
		
			Loggers.loggerStart(smartId);
			Loggers.loggerStart(year);
			if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("director") || role.equalsIgnoreCase("owner"))
			{
				query = session.createQuery(
						"from PerformanceAppraisal where isActive=:isActive AND reportingManagerID=:smartId AND year=:year");
			}else{
			query = session.createQuery(
					"from PerformanceAppraisal where isActive=:isActive AND reportingManagerID=:smartId AND year=:year and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			}
			query.setParameter("smartId",smartId);
			query.setParameter("year", year);
			query.setParameter("isActive", "Y");
			teamappraisalList = (List<PerformanceAppraisal>) query.list();
			Loggers.loggerEnd(teamappraisalList);

		} catch (Exception e) {
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		return teamappraisalList;
	}

	@Override
	public void addAppraisal(PerformanceAppraisal performanceAppraisal) throws GSmartDatabaseException {
		getConnection();
		try {
			performanceAppraisal.setEntryTime(CalendarCalculator.getCurrentEpochTime());
			performanceAppraisal.setIsActive("Y");
			session.save(performanceAppraisal);
			Loggers.loggerEnd(performanceAppraisal);
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
	public void editAppraisal(PerformanceAppraisal appraisal) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		try {
			
			PerformanceAppraisal oldAppraisal = getAppraisal(appraisal.getEntryTime());
			oldAppraisal.setIsActive("N");
			oldAppraisal.setUpdateTime(CalendarCalculator.getCurrentEpochTime());
			session.update(oldAppraisal);
			appraisal.setEntryTime(CalendarCalculator.getCurrentEpochTime());
			appraisal.setIsActive("Y");
			session.save(appraisal);
			transaction.commit();
			//session.close();
      
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());

		}finally {
			session.close();
		}
		 Loggers.loggerEnd();
	}

	public PerformanceAppraisal getAppraisal(Long entryTime) {
		Loggers.loggerStart(entryTime);
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