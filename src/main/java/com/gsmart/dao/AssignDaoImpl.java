package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Assign;
import com.gsmart.model.CompoundAssign;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class AssignDaoImpl implements AssignDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;;
	Query query;
	Transaction transaction = null;

	@SuppressWarnings("unchecked")
	@Override
	public List<Assign> getAssignReportee() throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Assign> assignList = null;
		try {
			getConnection();
			query = session.createQuery("from Assign where isActive=:isActive");
			query.setParameter("isActive", "Y");
			assignList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Loggers.loggerEnd();
		return assignList;
	}

	@Override
	public CompoundAssign addAssigningReportee(Assign assign) throws GSmartDatabaseException {
		Loggers.loggerStart();

		getConnection();
		CompoundAssign compoundAssign = null;
		try {
			query = session.createQuery(
					"from Assign where (standard=:standard and section=:section and teacherSmartId=:teacherSmartId) or (hodSmartId=:hodSmartId and teacherSmartId=:teacherSmartId) or (hodSmartId=:hodSmartId and principalSmartId=:principalSmartId)");
			query.setParameter("standard", assign.getStandard());
			query.setParameter("section", assign.getSection());
			query.setParameter("teacherSmartId", assign.getTeacherSmartId());
			query.setParameter("hodSmartId", assign.getHodSmartId());
			query.setParameter("principalSmartId", assign.getPrincipalSmartId());

			compoundAssign = (CompoundAssign) query.uniqueResult();

			if (compoundAssign == null) {
				assign.setEntryTime(CalendarCalculator.getTimeStamp());
				assign.setIsActive("Y");
				session.save(assign);
				transaction.commit();

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return compoundAssign;

	}

	@Override
	public void editAssigningReportee(Assign assign) throws GSmartDatabaseException {

		Loggers.loggerStart();
		try {
			getConnection();
			Assign oldAssign = getAssigns(assign.getEntryTime());
			oldAssign.setIsActive("N");
			oldAssign.setUpdatedTime(CalendarCalculator.getTimeStamp());
			session.update(oldAssign);
			
			assign.setEntryTime(CalendarCalculator.getTimeStamp());
			assign.setIsActive("Y");
			session.save(assign);
			session.getTransaction().commit();
			
			

		} catch (Exception e) {
			e.printStackTrace();
			// throw new GSmartDatabaseException(e.getMessage());
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}

	}

	public Assign getAssigns(String entryTime) {
		Loggers.loggerStart();
		try {
			query = session.createQuery("from Assign where isActive='Y' and entryTime='" + entryTime + "'");
			Assign assign = (Assign) query.uniqueResult();
			return assign;

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerEnd();
			return null;
		}

	}

	@Override
	public void deleteAssigningReportee(Assign assign) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			getConnection();
			assign.setIsActive("D");
			assign.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(assign);
			session.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
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
