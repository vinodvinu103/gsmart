package com.gsmart.dao;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Login;
import com.gsmart.util.Encrypt;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class LoginDaoImpl implements LoginDao {

	@Autowired
	SessionFactory sessionFactory;
	Login login;
	Session session;
	Transaction tx;
	Query query;

	@Override
	public int authenticate(Login loginDetails) throws GSmartDatabaseException {
		
		Loggers.loggerStart();
		try {
			getConnection();
			int attempt = 0;
			Query query = session.createQuery("from Login where smartId = :smartId");
			query.setParameter("smartId", loginDetails.getSmartId());
			@SuppressWarnings("unchecked")
			ArrayList<Login> loginarray = (ArrayList<Login>) query.list();

			if (!loginarray.isEmpty()) {

				Login login = loginarray.get(0);
				if ((Encrypt.md5(loginDetails.getPassword()).equals(login.getPassword())) && login.getAttempt() >= 4)
				{
					Loggers.loggerEnd(2);
					return 2;
				} else if ((Encrypt.md5(loginDetails.getPassword()).equals(login.getPassword()))
						&& login.getAttempt() <= 4) {
					if (login.getAttempt() > 0)
						resetAttempt(login);
					Loggers.loggerEnd(0);
					return 0;
				} else {
					attempt = login.getAttempt();
					++attempt;
					login.setAttempt(attempt);
					update(login);
					Loggers.loggerEnd(3);
					return 3;
				}
			} else {
				Loggers.loggerEnd(1);
				return 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
			return 5;

		}
		finally {
			session.close();
		}
	}

	public void resetAttempt(Login login) {
		try {
			login.setAttempt(0);
			session.update(login);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void update(Login login) {
		try {
			session.update(login);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getConnection() {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
	}

}
