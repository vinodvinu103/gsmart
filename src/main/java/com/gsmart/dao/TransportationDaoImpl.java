package com.gsmart.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Transportation;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class TransportationDaoImpl implements TransportationDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction transaction = null;
	Query query;
	
	
	
	//@SuppressWarnings("unchecked")
	@SuppressWarnings("unchecked")
	public  List<Transportation> getTransportationfeeList(Long hid)
			throws GSmartDatabaseException {
		List<Transportation> transportation = null;
	    getConnection();
		Loggers.loggerStart();
		try {
			query = session.createQuery("from Transportation where isactive='Y' and hierarchy.hid=:hierarchy");
			//query = session.createQuery("from Transportation");
			query.setParameter("hierarchy", hid);
	        Loggers.loggerStart(hid);
	        transportation  =  query.list();
	        
	        
			/*Criteria criteria = session.createCriteria(Transportation.class);
			criteria.add(Restrictions.eq("hierarchy.hid", hierarchy.getHid()));
			criteria.add(Restrictions.eq("isactive", "Y"));*/
//			transportation  = (List<Transportation>) query.list();
			/*transportation = criteria.list();*/
			//Loggers.loggerStart(transportation);
			

		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
			//Loggers.loggerEnd();
			//return null;
		} finally {
			session.close();
		}
		
		Loggers.loggerEnd(transportation);
		return transportation;
		
	}



	@Override
	public Transportation addTransportation(Transportation transportation) throws GSmartDatabaseException {
		Loggers.loggerStart("entering into addTransportation");

		getConnection();
		
        try {
		Loggers.loggerStart(transportation);
				transportation.setEntrytime(CalendarCalculator.getTimeStamp());
				
				transportation.setIsactive("Y");
				session.save(transportation);
				Loggers.loggerStart("saving...........");

				transaction.commit();
				Loggers.loggerStart("transaction is commited");
			
				//return transportation;
				

			
			
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}
	//	Loggers.loggerEnd();
        return transportation;
		
	}

	
	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}


	@Override
	public Transportation editTransportationFee(Transportation transportation) throws GSmartDatabaseException {

	getConnection();
	try {
		
		Loggers.loggerStart(transportation);
		Transportation oldTransportationfee = getTFeeList(transportation.getEntrytime());
		if (oldTransportationfee != null) {
			oldTransportationfee.setIsactive("N");
			oldTransportationfee.setUpdatedTime(CalendarCalculator.getTimeStamp());
			session.update(oldTransportationfee);
			transaction.commit();
            
            addTransportation(transportation);
		}
	} catch (Throwable e) {
		e.printStackTrace();
		throw new GSmartDatabaseException(e.getMessage());
  }
	return transportation;
  }
	





private Transportation getTFeeList(String entrytime) throws GSmartDatabaseException {
		
		Loggers.loggerStart(entrytime);
		Loggers.loggerStart();


		try {
             query = session.createQuery("from Transportation where isactive='Y' and entrytime=:entrytime");
             query.setParameter("entrytime",entrytime);
			//query.setParameter("hierarchy", hierarchy.getHid());
			Transportation oldfee = (Transportation) query.uniqueResult();
			
			Loggers.loggerEnd();
			return oldfee;
		} catch (Throwable e) {
		e.printStackTrace();
      	throw new GSmartDatabaseException(e.getMessage());
        } 

	}

		
	}