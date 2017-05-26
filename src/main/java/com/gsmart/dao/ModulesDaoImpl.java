package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.CompoundModules;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Modules;
import com.gsmart.model.RolePermission;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;
@Repository
@Transactional
public class ModulesDaoImpl implements ModulesDao{
	@Autowired
	private SessionFactory sessionFactory;

	Query query;

	 
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Modules> getModulesList(String role,Hierarchy hierarchy) throws GSmartDatabaseException{
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
			 query=sessionFactory.getCurrentSession().createQuery("from Modules WHERE isActive=:isActive");
			query.setParameter("isActive", "Y");
			 moduleslist = (List<Modules>) query.list();
			
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());

		} 
		Loggers.loggerEnd();
		return moduleslist;	
		}
		
	
	
	@Override
	public CompoundModules addModule(Modules modules) throws GSmartDatabaseException {
		CompoundModules cb = null;
		Session session=this.sessionFactory.getCurrentSession();
		try {
			

		/*	query = session.createQuery(
					"FROM Modules WHERE subModules=:subModules and modules=:modules AND isActive=:isActive");
			query.setParameter("modules", modules.getModules());
			query.setParameter("subModules", modules.getSubModules());
			query.setParameter("isActive", "Y");
			Modules oldmodules = (Modules) query.uniqueResult();
			Loggers.loggerStart(oldmodules);*/
			
			if (modules.getSubModuleName()==null) {
				 Modules module3=fetch1(modules);
				 if (module3 == null) {
						
						modules.setEntryTime(CalendarCalculator.getTimeStamp());
						modules.setIsActive("Y");
						cb = (CompoundModules) session.save(modules);
						addModuleToPermission(modules);
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
		} 
		return cb;
	}
	
	/*private void getconnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();

	}*/



	@SuppressWarnings("unchecked")
	private void addModuleToPermission(Modules modules) {
		try {
			Session session=this.sessionFactory.getCurrentSession();
			 query=session.createQuery("select DISTINCT role from RolePermission");
				 List<String> role= query.list();
				 int i=0;
				 for(String eachRole:role){
					 RolePermission rolePermission=new RolePermission();
					 rolePermission.setRole(eachRole);
					 rolePermission.setModuleName(modules.getModuleName());
					 rolePermission.setSubModuleName(modules.getSubModuleName());
					 rolePermission.setIsActive("Y");
					String entryTime=CalendarCalculator.getTimeStamp().substring(0, CalendarCalculator.getTimeStamp().length()-1);
					 rolePermission.setEntryTime(entryTime+(i++));
					 session.save(rolePermission);
				 }
			
		} catch (Exception e) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			e.printStackTrace();
		}
		
		
		
	}



	@Override
	public Modules editModule(Modules modules) throws GSmartDatabaseException {

		Modules cb=null;
		Session session=this.sessionFactory.getCurrentSession();
		try{
			Loggers.loggerStart();
			
			Modules oldModule = getModule(modules.getEntryTime());
			
			cb=updatemodule(oldModule,modules);
		
		
			if (cb!=null) {
				modules.setEntryTime(CalendarCalculator.getTimeStamp());
				modules.setIsActive("Y");
				session.save(modules);
				
				
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
		Session session=this.sessionFactory.getCurrentSession();
		
		Loggers.loggerStart(modules.getSubModuleName());
		
	      try{
	    	  
	    	  if ( modules.getSubModuleName()==null) {
	    		  Modules module3=fetch1(modules);
	    		  Loggers.loggerStart(module3);
	    		  if (module3==null) {
	    			  oldModule.setIsActive("N");
						oldModule.setUpdateTime(CalendarCalculator.getTimeStamp());
						session.update(oldModule);
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
		query = sessionFactory.getCurrentSession().createQuery(
				"FROM Modules WHERE moduleName=:modules AND isActive=:isActive");
		query.setParameter("modules", modules.getModuleName());
		
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
		query = sessionFactory.getCurrentSession().createQuery(
				"FROM Modules WHERE subModuleName=:subModules and moduleName=:modules AND isActive=:isActive");
		query.setParameter("modules", modules.getModuleName());
		query.setParameter("subModules", modules.getSubModuleName());
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
    	  
			query = sessionFactory.getCurrentSession().createQuery("FROM Modules WHERE  isActive=:isActive and entryTime=:entryTime ");
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
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		
		try {

			modules.setIsActive("D");
			modules.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(modules);

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
		} 
		Loggers.loggerEnd();
		
	}

}
