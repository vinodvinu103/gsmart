package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Grades;
import com.gsmart.model.Profile;
import com.gsmart.model.WeekDays;
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
		Loggers.loggerStart(grades);
		try {
			grades.setIsActive("Y");
			grades.setEntryTime(CalendarCalculator.getTimeStamp());
			session.save(grades);
			transaction.commit();
			session.close();
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd();

	}
	   

	// update
	/* EDIT GRADES FROM THE DATABASE */
	
	   @Override
		public void updateGrades(Grades grades) throws GSmartDatabaseException {
		getConnection();
		//Grades ch = null;
		Loggers.loggerStart();
		try {
			
			System.out.println("data is updating............");
			
			Grades olddata = getData(grades);
			
			olddata.setIsActive("N");
			olddata.setUpdateTime(CalendarCalculator.getTimeStamp());
			session.update(olddata);
			transaction.commit();
			
			addGrades(grades);
			
			
		} catch (Exception exception) {

			exception.getMessage();
			exception.printStackTrace();
		} 
		Loggers.loggerEnd();
		return;
	}

	public Grades getData(Grades grades) {
		getConnection();
		Grades grades1=null;
		try {
			query = session.createQuery("from Grades where entryTime=:entryTime and isActive=:isActive ");
			query.setParameter("entryTime", grades.getEntryTime());
			query.setParameter("isActive", "Y");
			grades1=(Grades) query.uniqueResult();
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
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
	
}// end of class
