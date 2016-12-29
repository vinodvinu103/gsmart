package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gsmart.model.Modules;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;
@Repository
public class ModulesDaoImpl implements ModulesDao{
	@Autowired
	SessionFactory sessionFactory;
	

	Session session = null;
	Transaction transaction = null;
	Query query;

	 
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Modules> getModulesList() throws GSmartDatabaseException{
		Loggers.loggerStart();
		 List<Modules> moduleslist = null;
		 try { 
			 getconnection();
			 query=session.createQuery("from Modules");
			 moduleslist = (List<Modules>) query.list();
			
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());

		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		return moduleslist;	
		}
		
	
	private void getconnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();

	}

}
