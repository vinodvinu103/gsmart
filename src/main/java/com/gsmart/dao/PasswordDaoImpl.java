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

	@Override
	public void setPassword(Login login,Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		try {
			
			query=session.createQuery("from Login where (referenceSmartId=:referenceSmartId or referenceSmartId=:SmartId) and hierarchy:hierarchy  ");
			query.setParameter("referenceSmartId", login.getReferenceSmartId());
			query.setParameter("SmartId", login.getSmartId());
			query.setParameter("hierarchy", hierarchy.getHid());
			System.out.println("encrypted smartid"+login.getSmartId());
			Login refId=(Login) query.uniqueResult();
			
			System.out.println(refId);
			if(refId.getReferenceSmartId()==null)
			{
				login.setSmartId(login.getSmartId());
				login.setReferenceSmartId(login.getReferenceSmartId());
			session.save(login);
			}else
			{
				
//				login.setcuPassword(Encrypt.md5(String.valueOf(login.getPassword())));
				refId.setPassword(Encrypt.md5(login.getConfirmPassword()));
				refId.setAttempt(0);
				refId.setEntryTime(CalendarCalculator.getTimeStamp());
				session.saveOrUpdate(refId);
			}
			session.saveOrUpdate(login);
			transaction.commit();
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
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
	public boolean changePassword(Login login, String smartId,Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart(login);
		Login currentPassword = null;
		boolean pwd = false;
		getConnection();
		
		String pass=Encrypt.md5(login.getPassword());
		try {
			
			query = session.createQuery("from Login where smartId=:smartId and password=:currentPassword and hierarchy:hierarchy");
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
			return pwd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pwd;
		}
	

	public Profile forgotPassword(String email,Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		Profile emailId = null;
		try {

			
			
			System.out.println(email);

			query = session.createQuery("from Profile where emailId=:emailId and hierarchy:hierarchy");
			query.setParameter("emailId", email);
			query.setParameter("hierarchy", hierarchy.getHid());
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

