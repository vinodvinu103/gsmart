package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.gsmart.model.CompoundModules;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Modules;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
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
	public List<Modules> getModulesList(String role,Hierarchy hierarchy) throws GSmartDatabaseException{
		Loggers.loggerStart();
		 getconnection();
		 List<Modules> moduleslist = null;
		 try { 
			if(role.equalsIgnoreCase("admin"))
			{
			 query=session.createQuery("from Modules");
			}else{
				query=session.createQuery("from Modules and hierarchy:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
			
			 moduleslist = (List<Modules>) query.list();
			
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());

		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		return moduleslist;	
		}
		
	
	
	@Override
	public CompoundModules addModule(Modules modules) throws GSmartDatabaseException {
		CompoundModules cb = null;
		try {
			 getconnection();

			query = session.createQuery(
					"FROM Modules WHERE subModules=:subModules and modules=:modules ");
			query.setParameter("modules", modules.getModules());
			query.setParameter("subModules", modules.getSubModules());
			Modules oldmodules = (Modules) query.uniqueResult();
			Loggers.loggerStart(oldmodules);
			if (oldmodules == null) {
				modules.setEntryTime(CalendarCalculator.getTimeStamp());
				cb = (CompoundModules) session.save(modules);
				Loggers.loggerEnd(oldmodules);
			}

			transaction.commit();
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}
		return cb;
	}
	
	private void getconnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();

	}

}
