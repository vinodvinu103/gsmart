package com.gsmart.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.Login;
import com.gsmart.model.Profile;

import com.gsmart.util.Encrypt;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class LoginDaoImpl implements LoginDao {

	@Autowired
	private SessionFactory sessionFactory;
	Login login;
	Query query;

	@Override
	public Map<String, Object> authenticate(Login loginDetails) throws GSmartDatabaseException {
		Map<String, Object> authMap = new HashMap<>();
		Loggers.loggerStart();
		
		try {
			
			int attempt = 0;
			Query query = sessionFactory.getCurrentSession().createQuery("from Login where smartId = :smartId");
			query.setParameter("smartId", loginDetails.getSmartId());
			@SuppressWarnings("unchecked")
			ArrayList<Login> loginarray = (ArrayList<Login>) query.list();

			if (!loginarray.isEmpty()) {

				Login login = loginarray.get(0);
			
				authMap.put("login", login);
				if ((Encrypt.md5(loginDetails.getPassword()).equals(login.getPassword())) && login.getAttempt() >= 4)
				{
					Loggers.loggerEnd(2);
					authMap.put("status", 2);
					
				}else if (!(Encrypt.md5(loginDetails.getPassword()).equals(login.getPassword()))
						&& login.getAttempt() >= 4) {
					Loggers.loggerEnd(2);
					authMap.put("status", 2);
					
				} 
				
				else if ((Encrypt.md5(loginDetails.getPassword()).equals(login.getPassword()))
						&& login.getAttempt() <= 4) {
					
					if (login.getAttempt() > 0)
						resetAttempt(login);
					Loggers.loggerEnd(0);
					authMap.put("status", 0);
				} else {
					attempt = login.getAttempt();
					++attempt;
					login.setAttempt(attempt);
					update(login);
					Loggers.loggerEnd(3);
					authMap.put("status", 3);
				}
			} else {
				Loggers.loggerEnd(1);
				authMap.put("status", 1);
			}
			
			return authMap;
		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
			authMap.put("status", 1);
			return authMap;
		}
		
	}

	public void resetAttempt(Login login) {
		Session session=this.sessionFactory.getCurrentSession();
		try {
			login.setAttempt(0);
			session.update(login);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void update(Login login) {
		Session session=this.sessionFactory.getCurrentSession();
		try {
			session.update(login);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public void getConnection() {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
	}*/
	
	@Override
	public List<Profile> getAllRecord(){
		List<Profile> list=null;
		try {
			query=sessionFactory.getCurrentSession().createQuery("from Profile where isActive='Y'");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
