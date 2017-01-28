package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Attenance;
import com.gsmart.model.Band;
import com.gsmart.model.CompoundAttenance;
import com.gsmart.model.CompoundInventory;
import com.gsmart.model.Inventory;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class AttenaceDaoImpl implements AttenanceDao {
	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction transaction = null;
	Query query;

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Attenance> getAttenanceList() throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Attenance> attenancesList;
		try {
			getConnection();
			query = session.createQuery("from Attenance where isActive='Y'");
			attenancesList = query.list();

		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());

		} finally {
			session.close();
		}
		Loggers.loggerEnd(attenancesList);
		return attenancesList;
	}

	@Override
	public CompoundAttenance addAttenance(Attenance attenance) throws GSmartDatabaseException {

		Loggers.loggerStart();
		CompoundAttenance cb = null;
	
		try {
			getConnection();
			query=session.createQuery("FROM Attenance WHERE gsmartId=:gsmartId AND rfId=:rfId AND isActive=:isActive");
			query.setParameter("gsmartId", attenance.getGsmartId());
			query.setParameter("rfId", attenance.getRfId());
			query.setParameter("isActive", "Y");
			Attenance s=(Attenance) query.uniqueResult();
			if (s ==null) {
				attenance.setEntryTime((CalendarCalculator.getTimeStamp()));
				attenance.setIsActive("Y");
				cb=(CompoundAttenance)session.save(attenance);
			}
		
			transaction.commit();
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		return cb;
	}
	
	
	

	@Override
	public void editAttenance(Attenance attenance) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			getConnection();
			Attenance oldAttenace =  getAttenance( attenance.getEntryTime());
			oldAttenace .setIsActive("N");
			oldAttenace .setUpdateTime(CalendarCalculator.getTimeStamp());
			session.update( oldAttenace );
			attenance.setEntryTime(CalendarCalculator.getTimeStamp());
			attenance.setIsActive("Y");
			session.save(attenance);
			transaction.commit();
			session.close();
	
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());

		}
		
	}
	
	
	
	public Attenance getAttenance(String entryTime) {
		try {
			

			query = session.createQuery("from Attenance where isActive='Y' and entryTime=:entryTime");
			query.setParameter("entryTime", entryTime);
			Attenance attenance = (Attenance) query.uniqueResult();
			return attenance;

	
			
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void deleteAttenance(Attenance attenance) throws GSmartDatabaseException {

		Loggers.loggerStart();
			try {
				Loggers.loggerStart();
				session = sessionFactory.openSession();
				transaction = session.beginTransaction();
				attenance.setExitTime(CalendarCalculator.getTimeStamp());
				attenance.setIsActive("D");
				session.update(attenance);
				transaction.commit();
				session.close();
				Loggers.loggerEnd();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void getConnection() {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
		}

	

}
