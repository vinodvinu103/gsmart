package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Grades;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Repository
public class GradesDaoImpl implements GradesDao{
	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction transaction = null;
	Query query;

	// get
	@Override
	public List<Grades> getGradesList() throws GSmartServiceException {
		getConnection();
		Loggers.loggerStart();
		List<Grades> gradesList = null;
		try {

			query = session.createQuery("from Grades where isActive='Y' ");
			gradesList = query.list();
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {

			session.close();
		}
		Loggers.loggerEnd(gradesList);

		return gradesList;

	}
	// add
	@Override
	public void addGrades(Grades grades) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		try {
			grades.setIsActive("Y");
			grades.setEntryTime(CalendarCalculator.getTimeStamp());
			session.save(grades);
			transaction.commit();
			session.close();
		} catch (Exception exception) {
			exception.getMessage();
		} finally {
			session.close();
		}
		Loggers.loggerEnd();

	}
	   

	// update
	   
	   @Override
		public void updateGrades(Grades grades) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		try {
			Grades olddata = getData(grades);
			olddata.setIsActive("N");
			olddata.setUpdateTime(CalendarCalculator.getTimeStamp());
			session.update(olddata);

			addGrades(grades);
			transaction.commit();
			session.close();
		} catch (Exception exception) {

			exception.getMessage();
		} finally {
			session.close();
		}
		Loggers.loggerEnd();

	}

	public Grades getData(Grades grades) {
		getConnection();
		Grades grades1=null;
		try {
			query = session.createQuery("from Grades where entryTime=:entryTime and isActive=:isActive ");
			query.setParameter("entryTime", grades.getEntryTime());
			grades1=(Grades) query.uniqueResult();
		} catch (Exception e) {
			e.getMessage();
		} finally {
			session.close();
		}
		return grades1;
		
	}

	// delete
	@Override
	public void deleteGrades(Grades grades) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart(grades);
		try {
            grades.setExitTime(CalendarCalculator.getTimeStamp());
			grades.setIsActive("D");
			session.update(grades);
			transaction.commit();
			session.close();

		} catch (Exception exception) {

			exception.getMessage();
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
	}

	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}
	/*@Override
	public void updateGrades(Grades grades) throws GSmartDatabaseException {
	 getConnection();
	 Grades olddata=null;
		Loggers.loggerStart();
		try {
			olddata = getData(grades);
			olddata.setIsActive("N");
			olddata.setUpdateTime(CalendarCalculator.getTimeStamp());
			session.update(olddata);

			addGrades(grades);
			transaction.commit();
			session.close();
		} catch (Exception exception) {

			exception.getMessage();
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		
	}*/
}// end of class
