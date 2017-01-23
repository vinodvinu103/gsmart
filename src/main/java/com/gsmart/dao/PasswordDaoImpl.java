package com.gsmart.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Login;
import com.gsmart.model.Profile;
import com.gsmart.util.Constants;
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
	public void setPassword(Login login) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			getConnection();
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
	public Profile forgotPassword(String email) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Profile emailId = null;
		try {

			getConnection();
			
			System.out.println(email);

			query = session.createQuery("from Profile where emailId=:emailId");
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
