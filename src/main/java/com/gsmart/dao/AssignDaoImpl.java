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
import com.gsmart.model.Hierarchy;
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
	public List<Assign> getAssignReportee(String role, Hierarchy hierarchy) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		List<Assign> assignList = null;
		try {
			
			if(role.equalsIgnoreCase("admin")|| role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director"))
				query = session.createQuery("from Assign where isActive=:isActive");
			else {
				query = session.createQuery("from Assign where isActive=:isActive and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
			query.setParameter("isActive", "Y");
			assignList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}

		Loggers.loggerEnd();
		return assignList;
	}

	@Override
	public CompoundAssign addAssigningReportee(Assign assign) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		
		CompoundAssign ch = null;
		try {
			query = session.createQuery(
					"from Assign where standard=:standard and section=:section and isActive=:isActive");
			query.setParameter("standard", assign.getStandard());
			query.setParameter("section", assign.getSection());
			query.setParameter("isActive", "Y");

			Assign assign1 =  (Assign) query.uniqueResult();

			if (assign1 == null) {
				assign.setEntryTime(CalendarCalculator.getTimeStamp());
				assign.setIsActive("Y");
				ch = (CompoundAssign) session.save(assign);
				transaction.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			session.close();
		}
		return ch;
	}

	@Override
	public void editAssigningReportee(Assign assign) throws GSmartDatabaseException {

		getConnection();
		Loggers.loggerStart();
		try {			
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
		getConnection();
		Loggers.loggerStart();
		try {			
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

	@Override
	public Assign getStaffByClassAndSection(String standard, String section, Hierarchy hierarchy) {
		getConnection();
		try {
			query = session.createQuery(
					"from Assign where hierarchy.hid=:hierarchy and standard=:standard and section=:section");
			query.setParameter("standard", standard);
			query.setParameter("section", section);
			query.setParameter("hierarchy", hierarchy.getHid());
			return (Assign) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			session.close();
		}
	}

}
