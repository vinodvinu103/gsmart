package com.gsmart.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.CompoundFeeMaster;
import com.gsmart.model.FeeMaster;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

/**
 * provides the implementation for the methods available in {@link FeeMasterDao}
 * interface
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 */

@Repository
public class FeeMasterDaoImpl implements FeeMasterDao {

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;
	Transaction transaction = null;
	Query query;

	/**
	 * to view the list of records available in {@link FeeMaster} table
	 * 
	 * @return list of fee entities available in Fee
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FeeMaster> getFeeList(String role,Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		List<FeeMaster> feeList=null;
		try {
			if(role.equalsIgnoreCase("admin"))
			{
			query = session.createQuery("from FeeMaster where isActive='Y'");
			}else{
				query = session.createQuery("from FeeMaster where isActive='Y' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
			feeList = (List<FeeMaster>)query.list();

		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} finally {

			session.close();
		}
		Loggers.loggerEnd();
		return feeList;
	}

	/**
	 * Adds new fee entity to {@link FeeMaster} save it in database
	 * 
	 * @param fee
	 *            instance of Fee
	 * @return Nothing
	 */
	@Override
	public CompoundFeeMaster addFee(FeeMaster feeMaster) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		CompoundFeeMaster cfm = null;
		try {
			Hierarchy hierarchy=feeMaster.getHierarchy();
			FeeMaster feeMaster2=null;
			query=session.createQuery("FROM FeeMaster WHERE standard=:standard AND isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("standard", feeMaster.getStandard());
			
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameter("isActive", "Y");
			feeMaster2= (FeeMaster) query.uniqueResult();
			if(feeMaster2==null)
			{
				feeMaster.setIsActive("Y");
				feeMaster.setEntryTime(CalendarCalculator.getTimeStamp());
			
				cfm = (CompoundFeeMaster) session.save(feeMaster);
				transaction.commit();
				return cfm;
			}
		} catch (ConstraintViolationException e) {
			transaction.rollback();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		
		return cfm;
	}

	/**
	 * persists the updated band instance
	 * 
	 * @param fee
	 *            instance of {@link FeeMaster}
	 * @return Nothing
	 */
	@Override
	public void editFee(FeeMaster feeMaster) throws GSmartDatabaseException {

		Loggers.loggerStart();
		getConnection();
		try {
			Hierarchy hierarchy=feeMaster.getHierarchy();
			FeeMaster oldFee = getFeeMas(feeMaster.getEntryTime(),hierarchy);
			oldFee.setUpdatedTime(CalendarCalculator.getTimeStamp());
			oldFee.setIsActive("N");
			session.update(oldFee);
			
			/*feeMaster.setEntryTime(CalendarCalculator.getTimeStamp());
			feeMaster.setIsActive("Y");
			session.save(feeMaster);*/

			transaction.commit();
			addFee(feeMaster);
			
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
	
	}
	


	/**
	 * removes the fee entity from the database.
	 * 
	 * @param fee
	 *            instanceOf {@link FeeMaster}
	 * @return Nothing
	 */
	@Override
	public void deleteFee(FeeMaster feeMaster) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		try {
			
			feeMaster.setExitTime(CalendarCalculator.getTimeStamp());
			feeMaster.setIsActive("D");
			session.update(feeMaster);
			transaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		
	}
	
	public FeeMaster getFeeMas(String entryTime,Hierarchy hierarchy)
	{
		Loggers.loggerStart();
		query = session.createQuery("from FeeMaster where isActive=:isActive and entryTime =:entryTime and hierarchy.hid=:hierarchy");
		query.setParameter("isActive", "Y");
		query.setParameter("hierarchy", hierarchy.getHid());
		query.setParameter("entryTime", entryTime);
		FeeMaster fee=(FeeMaster) query.uniqueResult();

		Loggers.loggerValue("feeList", fee);
		return fee;
		
		
	}

	/**
	 * create instance for session and begins transaction
	 */
	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}

	@Override
	public FeeMaster getFeeStructure(String standard,String role,Hierarchy hierarchy) {
		getConnection();
		try {
			if(role.equalsIgnoreCase("admin"))
			{
			query = session.createQuery("from FeeMaster where standard='" + standard + "'  and isActive='Y' ");
			}else{
				query = session.createQuery("from FeeMaster where standard='" + standard + "'  and isActive='Y' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
				
			}
			FeeMaster fee = (FeeMaster) query.list().get(0);
			return fee;
		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
			return null;
		} finally {
			session.close();
		}
	}

	

	/*@Override
	public void fileUpload(FileUpload fileUpload) throws GSmartDatabaseException {

		Loggers.loggerStart();
		try {
			getConnection();
			session.save(fileUpload);
			transaction.commit();
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
	}

	@Override
	public List getFile(String fileName) throws GSmartDatabaseException {
		Loggers.loggerStart();

		List feeList=null;
		try {
			getConnection();
			Loggers.loggerValue(fileName);
			query = session.createQuery("from FileUpload where feeDetails =" + fileName);
			feeList = query.list();
			Loggers.loggerValue(feeList.get(0));

		} catch (Exception e) {
			
			Loggers.loggerException(e.getMessage());
		} finally {

			session.close();
		}
		Loggers.loggerEnd();
		return feeList;
	}*/

}