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
		getconnection();
		Loggers.loggerStart();
		 
		 List<Modules> moduleslist = null;
		 try { 
			/*if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director"))
			{
			 query=session.createQuery("from Modules");
			}else{
				query=session.createQuery("from Modules and hierarchy:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}*/
			 query=session.createQuery("from Modules WHERE isActive=:isActive");
			query.setParameter("isActive", "Y");
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
		 getconnection();
		CompoundModules cb = null;
		try {
			

		/*	query = session.createQuery(
					"FROM Modules WHERE subModules=:subModules and modules=:modules AND isActive=:isActive");
			query.setParameter("modules", modules.getModules());
			query.setParameter("subModules", modules.getSubModules());
			query.setParameter("isActive", "Y");
			Modules oldmodules = (Modules) query.uniqueResult();
			Loggers.loggerStart(oldmodules);*/
			
			if (modules.getSubModules()==null) {
				 Modules module3=fetch1(modules);
				 if (module3 == null) {
						
						modules.setEntryTime(CalendarCalculator.getTimeStamp());
						modules.setIsActive("Y");
						cb = (CompoundModules) session.save(modules);
						transaction.commit();
						Loggers.loggerEnd(module3);
					}
				 else {
					return cb;
				}
				
			} else {
				 Modules module2=fetch(modules);
				 if(module2==null){
				 modules.setEntryTime(CalendarCalculator.getTimeStamp());
					modules.setIsActive("Y");
					cb = (CompoundModules) session.save(modules);
					transaction.commit();
					Loggers.loggerEnd(module2);
				 }else {
					return null;
				}

			}
			
			

		
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



	@Override
	public Modules editModule(Modules modules) throws GSmartDatabaseException {

		getconnection();
		Modules cb=null;
		try{
			Loggers.loggerStart();
			
			Modules oldModule = getModule(modules.getEntryTime());
			
			cb=updatemodule(oldModule,modules);
		
		
			if (cb!=null) {
				getconnection();
				modules.setEntryTime(CalendarCalculator.getTimeStamp());
				modules.setIsActive("Y");
				session.save(modules);
				transaction.commit();
				session.close();
				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd(cb);
		return cb;
		
	}
	



	private Modules updatemodule(Modules oldModule,Modules modules)throws GSmartDatabaseException {
		
		Modules ch=null;
		
		Loggers.loggerStart(modules.getSubModules());
		
	      try{
	    	  
	    	  if ( modules.getSubModules()==null) {
	    		  Modules module3=fetch1(modules);
	    		  Loggers.loggerStart(module3);
	    		  if (module3==null) {
	    			  oldModule.setIsActive("N");
						oldModule.setUpdateTime(CalendarCalculator.getTimeStamp());
						session.update(oldModule);
						transaction.commit();
						return oldModule;
					
				}
	    		  else {
					return ch;
				}
			} 
	    	  
	    	  
	    	  else {

			 Modules module2=fetch(modules);
	    	  if (module2==null) {
					oldModule.setIsActive("N");
					oldModule.setUpdateTime(CalendarCalculator.getTimeStamp());
					session.update(oldModule);
					transaction.commit();
					return oldModule;
	    	  }
			}
	      }catch (ConstraintViolationException e) {
				throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
			} catch (Throwable e) {
				throw new GSmartDatabaseException(e.getMessage());
			}
			return ch;
	      
	}



	private Modules fetch1(Modules modules) {
		Loggers.loggerStart();
		Modules moduleslist=null;
		try {
		query = session.createQuery(
				"FROM Modules WHERE modules=:modules AND isActive=:isActive");
		query.setParameter("modules", modules.getModules());
		
		query.setParameter("isActive", "Y");
		 moduleslist = (Modules) query.uniqueResult();
	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			
		}
		Loggers.loggerEnd();
		return moduleslist;
	}



	private Modules fetch(Modules modules) {
		Loggers.loggerStart();
		Modules moduleslist=null;
		try {
		query = session.createQuery(
				"FROM Modules WHERE subModules=:subModules and modules=:modules AND isActive=:isActive");
		query.setParameter("modules", modules.getModules());
		query.setParameter("subModules", modules.getSubModules());
		query.setParameter("isActive", "Y");
		 moduleslist = (Modules) query.uniqueResult();
	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			
		}
	    Loggers.loggerEnd();
		return moduleslist;
	}



	public Modules getModule(String entryTime){
		 
      try{
    	  
			query = session.createQuery("FROM Modules WHERE  isActive=:isActive and entryTime=:entryTime ");
            query.setParameter("isActive","Y");
            query.setParameter("entryTime",entryTime);
			Modules	 moduleslist =  (Modules) query.uniqueResult();
			
			return moduleslist;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	@Override
	public void deleteModule(Modules modules) throws GSmartDatabaseException {
		getconnection();
		Loggers.loggerStart();
		
		try {

			modules.setIsActive("D");
			modules.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(modules);
			transaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		
	}

}
