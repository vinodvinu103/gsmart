package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Band;
import com.gsmart.model.CompoundBand;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

/**
 * provides the implementation for the methods available in {@link BandDao}
 * interface
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-02-23
 */
@Repository
public class BandDaoImpl implements BandDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction transaction = null;
	Query query;

	/**
	 * to view the list of records available in {@link Band} table
	 * 
	 * @return list of band entities available in Band
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Band> getBandList() throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		List<Band> bandList;
		try {
			

			query = session.createQuery("from Band where isActive='Y'");
			bandList = query.list();

		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {

			session.close();
		}
		Loggers.loggerEnd();
		return bandList;
	}

	/**
	 * Adds new band entity to {@link Band} save it in database
	 * 
	 * @param band
	 *            instance of Band
	 * @return Nothing
	 */
	@Override
	public CompoundBand addBand(Band band) throws GSmartDatabaseException {
		getConnection();
		CompoundBand cb = null;
		try {
			

			query = session.createQuery(
					"FROM Band WHERE bandId=:bandId AND isActive=:isActive AND designation=:designation AND role=:role ");
			query.setParameter("bandId", band.getBandId());
			query.setParameter("isActive", "Y");
			query.setParameter("designation", band.getDesignation());
			query.setParameter("role", band.getRole());
			Band oldBand = (Band) query.uniqueResult();
			Loggers.loggerStart(oldBand);
			if (oldBand == null) {
				band.setEntryTime(CalendarCalculator.getTimeStamp());
				band.setIsActive("Y");
			
				cb = (CompoundBand) session.save(band);
				transaction.commit();
				
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
		Loggers.loggerEnd();
		return cb;
	}

	/* EDITBAND FROM THE DATABASE */

	@Override
	public Band editBand(Band band) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		Band ch = null;
		try {
			
		
		    Band oldBand = getBand(band.getEntryTime());
			ch=	updateBand(oldBand,band);

			
		addBand(band);	
//			if(ch!=null)
//			{
//				band.setEntryTime(CalendarCalculator.getTimeStamp());
//				band.setIsActive("Y");
//				ch=(Band) session.save(band);
//				transaction.commit();
//			}
			Loggers.loggerEnd();
			return ch;
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		
	}
private Band updateBand(Band oldBand, Band band) throws GSmartDatabaseException {
		
		Band ch = null;
		try {
			Band band1 = fetch(band);
			if (band1 == null) {
				oldBand.setUpdatedTime(CalendarCalculator.getTimeStamp());
				oldBand.setIsActive("N");
				session.update(oldBand);

				
				transaction.commit();
				return oldBand;

			}
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		return ch;

	}

//	private void updateBand(Band oldBand) {
//		session = sessionFactory.openSession();
//		transaction = session.beginTransaction();
//
//		//Loggers.loggerValue(oldBand.getUpdatedTime());
//		session.update(oldBand);
//		transaction.commit();
//		session.close();
//	}

	@SuppressWarnings("unchecked")
	public Band getBand(String entryTime) {
		try {
			
			query = session.createQuery("from Band where isActive='Y' and entryTime='" + entryTime + "'");
			ArrayList<Band> bandList = (ArrayList<Band>) query.list();
			return bandList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Band fetch(Band band) {
		getConnection();
		Loggers.loggerStart(band.getBandId());
		Band bandList=null;
		try {
			query = session.createQuery(
					"FROM Band WHERE bandId=:bandId AND designation=:designation and role=:role AND isActive=:isActive");
			query.setParameter("bandId", band.getBandId());
			query.setParameter("isActive", "Y");
			query.setParameter("designation", band.getDesignation());
			query.setParameter("role", band.getRole());
			
			bandList=(Band) query.uniqueResult();
		}catch (Exception e) {

			e.printStackTrace();
		}
			return bandList; 

		}

	/* DELETE DATA FROM THE DATABASE */
	@Override
	public void deleteBand(Band band) throws GSmartDatabaseException {
		getConnection();
		try {
			Loggers.loggerStart();
			
			band.setExitTime(CalendarCalculator.getTimeStamp());
			band.setIsActive("D");
			session.update(band);
			transaction.commit();
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
	}

	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}

	@Override
	public Band getMaxband() throws GSmartDatabaseException {
		getConnection();
		Band band=null;
		try {
			Loggers.loggerStart();
			
			query=session.createQuery("FROM Band WHERE bandId IN (SELECT MIN(bandId) FROM Band where isActive='Y')");
			band=(Band) query.list().get(0);
			transaction.commit();
			session.close();
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
		return band;
	}

}
