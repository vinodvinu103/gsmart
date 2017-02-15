package com.gsmart.dao;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Hierarchy;
import com.gsmart.model.Login;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.model.Profile;
import com.gsmart.util.Constants;
import com.gsmart.util.Encrypt;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class PasswordDaoImpl implements PasswordDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction transaction = null;
	Query query;

	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}
	
	@Override
	public void setPassword(Login login,Hierarchy hierarchy) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		
		try {
			
			query=session.createQuery("from Login where (referenceSmartId=:referenceSmartId or referenceSmartId=:SmartId)  ");
			query.setParameter("referenceSmartId", login.getReferenceSmartId());
			query.setParameter("SmartId", login.getSmartId());
			System.out.println("encrypted smartid"+login.getSmartId());
			Login refId=(Login) query.uniqueResult();
			
			if(refId!=null)
			{
				refId.setPassword(Encrypt.md5(login.getConfirmPassword()));
				refId.setAttempt(0);
				refId.setEntryTime(CalendarCalculator.getTimeStamp());
				session.saveOrUpdate(refId);
				
			}else
			{
				
				System.out.println("refild is null");
				login.setSmartId(login.getSmartId());
				login.setHierarchy(hierarchy);
				login.setReferenceSmartId(login.getReferenceSmartId());
			    session.save(login);
				
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
	}

	@Override
	public boolean changePassword(Login login, String smartId,Hierarchy hierarchy) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart(login);
		Login currentPassword = null;
		boolean pwd = false;
				
		String pass=Encrypt.md5(login.getPassword());
		try {
			
			query = session.createQuery("from Login where smartId=:smartId and password=:currentPassword and hierarchy.hid=:hierarchy");
			query.setParameter("currentPassword", pass);
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameter("smartId", smartId);
			currentPassword = (Login) query.uniqueResult();

			if (currentPassword != null) {
				currentPassword.setPassword(Encrypt.md5(login.getConfirmPassword()));
				session.update(currentPassword);
				transaction.commit();
				session.close();
				pwd = true;
			}
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
		return pwd;
		}
	
	@Override
	public Profile emailLink(String email) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		
		Profile emailId = null;
		try {

		    System.out.println(email);
			query = session.createQuery("from Profile where emailId=:emailId ");
			query.setParameter("emailId", email);
			emailId = (Profile) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		finally {
			session.close();
		}

		Loggers.loggerEnd(emailId);
		return emailId;
	}
}

