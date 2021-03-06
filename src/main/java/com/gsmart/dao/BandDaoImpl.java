package com.gsmart.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.Band;
import com.gsmart.model.CompoundBand;
import com.gsmart.model.Hierarchy;
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
@Transactional
 public class BandDaoImpl implements BandDao {

	@Autowired
	private SessionFactory sessionFactory;

	private Query query;
	private Criteria criteria = null;

	/**
	 * to view the list of records available in {@link Band} table
	 * 
	 * @return list of band entities available in Band
	 */
	@SuppressWarnings("unchecked")
	public List<Band> search(Band band, Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart();

		List<Band> bandList=null;
		try {
			if (hierarchy == null) {

				query = sessionFactory.getCurrentSession()
						.createQuery("from Band where role like '%" + band.getRole() + "%' AND isActive='Y'");
			} else {
				query = sessionFactory.getCurrentSession().createQuery("from Band where role like '%"
						+ band.getRole() + "%' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}

			bandList = query.list();

		} catch (ConstraintViolationException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getLocalizedMessage());
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		}catch (NullPointerException e) {
			throw new GSmartDatabaseException(Constants.NULL_PONITER);
		}catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}

		Loggers.loggerEnd();

		return bandList;
	}
	
	@Override
	public Map<String, Object> getBandList(int min, int max) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Map<String, Object> bandMap = new HashMap<String, Object>();
		try {
			criteria = sessionFactory.getCurrentSession().createCriteria(Band.class);
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.setFirstResult(min);
			criteria.setMaxResults(max);
			criteria.addOrder(Order.asc("bandId"));
			bandMap.put("bandList", criteria.list());
			criteria = sessionFactory.getCurrentSession().createCriteria(Band.class).add(Restrictions.eq("isActive", "Y"))
					.setProjection(Projections.rowCount());
			Long count = (Long) criteria.uniqueResult();
			bandMap.put("totalBands", count);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd();
		return bandMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Band> getBandList1() throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Band> bandList=null;
		try {
			criteria = sessionFactory.getCurrentSession().createCriteria(Band.class);
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.setProjection(Projections.id());
			bandList= criteria.list();
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
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
		CompoundBand cb = null;
		try {
			
			

			query = sessionFactory.getCurrentSession().createQuery(
					"FROM Band WHERE bandId=:bandId AND isActive=:isActive AND designation=:designation AND role=:role ");
			query.setParameter("bandId", band.getBandId());
			query.setParameter("isActive", "Y");
			query.setParameter("designation", band.getDesignation());
			query.setParameter("role", band.getRole());
			Band oldBand = (Band) query.uniqueResult();
			Loggers.loggerStart(oldBand);
			if (oldBand == null) {
			Session session=this.sessionFactory.getCurrentSession();
				band.setEntryTime(CalendarCalculator.getTimeStamp());
				band.setIsActive("Y");
			
				cb = (CompoundBand) session.save(band);
				
			}
			
		} catch (ConstraintViolationException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		}catch (NullPointerException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.NULL_PONITER);
		}catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cb;
	}

	/* EDITBAND FROM THE DATABASE */

	@Override
	public Band editBand(Band band) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Band ch = null;
		try {

			Band oldBand = getBand(band.getEntryTime());
			ch=updateBand(oldBand,band);

			Loggers.loggerValue("Band", band);
			addBand(band);
			

		
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd(ch);
		return ch;
		
	}
private Band updateBand(Band oldBand,Band band) throws GSmartDatabaseException {
	Session session=this.sessionFactory.getCurrentSession();
		Band ch = null;
		try {
			Band band1 = fetch(band);
			if (band1 == null) {
				oldBand.setUpdatedTime(CalendarCalculator.getTimeStamp());
				oldBand.setIsActive("N");
				session.update(oldBand);
				
				query=sessionFactory.getCurrentSession().createQuery("update Profile set role=:role,designation=:designation,band=:bandId where band=:band");
				query.setParameter("role", band.getRole());
				query.setParameter("designation", band.getDesignation());
				query.setParameter("bandId", band.getBandId());
				query.setParameter("band",oldBand.getBandId());
				query.executeUpdate();

				
				return oldBand;

			}
		} catch (ConstraintViolationException e) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw new GSmartDatabaseException(e.getMessage());
		}
		return ch;
	}



	@SuppressWarnings("unchecked")
	public Band getBand(String entryTime) {
		try {
			
			query = sessionFactory.getCurrentSession().createQuery("from Band where isActive='Y' and entryTime='" + entryTime + "'");
			ArrayList<Band> bandList = (ArrayList<Band>) query.list();
			return bandList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Band fetch(Band band) {
		Loggers.loggerStart(band.getBandId());
		Band bandList=null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
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
		try {
			Session session=this.sessionFactory.getCurrentSession();
			Loggers.loggerStart();
			
			band.setExitTime(CalendarCalculator.getTimeStamp());
			band.setIsActive("D");
			session.update(band);
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
	}

	/*public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}*/

	@Override
	public Band getMaxband() throws GSmartDatabaseException {
		Band band = null;
		try {
			Loggers.loggerStart();
			
			query = sessionFactory.getCurrentSession().createQuery("FROM Band WHERE bandId IN (SELECT MIN(bandId) FROM Band where isActive='Y')");
			band = (Band) query.list().get(0);
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		return band;
	}

}
