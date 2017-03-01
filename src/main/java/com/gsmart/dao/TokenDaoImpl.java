package com.gsmart.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Login;
import com.gsmart.model.Token;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class TokenDaoImpl implements TokenDao{

	@Autowired
	SessionFactory sessionFactory;

	Session session;
	Transaction tx;
	Query query;

	public void saveToken(Token token, Login loginObj) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart(token);
		try {			
			query = session.createQuery("from Login where smartId=:smartId");
			query.setParameter("smartId", loginObj.getSmartId());
			Login login = (Login)query.uniqueResult();
			token.setHierarchy(login.getHierarchy());
			session.save(token);
			tx.commit();
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}finally {
			session.close();
		}
		Loggers.loggerEnd();
	}

	public void getConnection() {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
	}

	public Token getToken(String tokenNumber) throws GSmartDatabaseException  {

		Token token = null;
		getConnection();

		try {
			query = session.createQuery("from Token where tokenNumber=:tokenNumber");
			query.setParameter("tokenNumber", tokenNumber);
			token = (Token) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		/*finally {
			session.close();
		}*/
		return token;
	}

	public void deleteToken(String tokenNumber) throws GSmartDatabaseException {

		getConnection();
		Loggers.loggerStart(tokenNumber);
		
		Token token = new Token();
		token.setTokenNumber(tokenNumber);

		try {
			Loggers.loggerValue("Token: ", token);
			session.delete(token);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}finally {
			session.close();
		}
	}

}
