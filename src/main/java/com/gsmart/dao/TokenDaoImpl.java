package com.gsmart.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.Login;
import com.gsmart.model.Token;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class TokenDaoImpl implements TokenDao{

	@Autowired
	private SessionFactory sessionFactory;

	Query query;

	public void saveToken(Token token, Login loginObj) throws GSmartDatabaseException {
		Loggers.loggerStart(token);
		Session session=this.sessionFactory.getCurrentSession();
		try {			
//			query = session.createQuery("from Login where smartId=:smartId");
//			query.setParameter("smartId", loginObj.getSmartId());
//			Login login = (Login)query.uniqueResult();
			
			session.save(token);
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd();
	}

	/*public void getConnection() {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
	}*/

	public Token getToken(String tokenNumber) throws GSmartDatabaseException  {

		Token token = null;

		try {
			query = sessionFactory.getCurrentSession().createQuery("from Token where tokenNumber=:tokenNumber");
			query.setParameter("tokenNumber", tokenNumber);
			token = (Token) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		

		return token;
	}

	public void deleteToken(String tokenNumber) throws GSmartDatabaseException {

		Loggers.loggerStart(tokenNumber);
		Session session=this.sessionFactory.getCurrentSession();
		
		Token token = new Token();
		token.setTokenNumber(tokenNumber);

		try {
			Loggers.loggerValue("Token: ", token);
			session.delete(token);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
	}

}
