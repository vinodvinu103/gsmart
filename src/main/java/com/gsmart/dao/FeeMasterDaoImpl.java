package com.gsmart.dao;

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
@Transactional
public class FeeMasterDaoImpl implements FeeMasterDao {

	@Autowired
	private SessionFactory sessionFactory;

	private Query query;

	/**
	 * to view the list of records available in {@link FeeMaster} table
	 * 
	 * @return list of fee entities available in Fee
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getFeeList(Long hid, int min, int max)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<FeeMaster> feeList = null;
		Criteria criteria = null;
		Map<String, Object> feeMap = new HashMap<>();
		
		try {
			criteria = sessionFactory.getCurrentSession().createCriteria(FeeMaster.class);
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			criteria.addOrder(Order.asc("standard"));
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("hierarchy.hid", hid));
			feeList = criteria.list();
			Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(FeeMaster.class);
			criteriaCount.add(Restrictions.eq("isActive", "Y"));
			criteriaCount.add(Restrictions.eq("hierarchy.hid", hid));
			criteriaCount.setProjection(Projections.rowCount());
			Long count = (Long) criteriaCount.uniqueResult();
			
			feeMap.put("totalfeelist", count);

		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd();
		System.out.println(feeList);
		feeMap.put("feeList", feeList);
	
		return feeMap;
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
		Session session=this.sessionFactory.getCurrentSession();
		
		CompoundFeeMaster cfm = null;
		try {
			FeeMaster feeMaster2=fetch2(feeMaster);
			if(feeMaster2==null)
			{
				feeMaster.setIsActive("Y");
				feeMaster.setEntryTime(CalendarCalculator.getTimeStamp());
			
				cfm = (CompoundFeeMaster) session.save(feeMaster);
			}
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
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
	public FeeMaster editFee(FeeMaster feeMaster) throws GSmartDatabaseException {

		Loggers.loggerStart();
		FeeMaster ch=null;
		try {
			Hierarchy hierarchy=feeMaster.getHierarchy();
			FeeMaster oldFee = getFeeMas(feeMaster.getEntryTime(),hierarchy);
			ch = updateFeeMaster(oldFee, feeMaster);
			addFee(feeMaster);
			
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd();
		return ch;
	
	}
	private FeeMaster updateFeeMaster(FeeMaster oldFeeMaster, FeeMaster feeMaster) throws GSmartDatabaseException {

		Session session=this.sessionFactory.getCurrentSession();
		FeeMaster ch = null;
		try {
			if(oldFeeMaster.getStandard().equals(feeMaster.getStandard())){
				
					oldFeeMaster.setUpdatedTime(CalendarCalculator.getTimeStamp());
					oldFeeMaster.setIsActive("N");
					session.update(oldFeeMaster);
					
					query=sessionFactory.getCurrentSession().createQuery("update Fee set sportsFee=:sportsFee,tuitionFee=:tuitionFee,idCardFee=:idCardFee,miscellaneousFee=:miscellaneousFee,totalFee=:totalFee where standard=:standard");
					query.setParameter("sportsFee", feeMaster.getSportsFee());
					query.setParameter("tuitionFee", feeMaster.getTuitionFee());
					query.setParameter("idCardFee", feeMaster.getIdCardFee());
					query.setParameter("miscellaneousFee", feeMaster.getMiscellaneousFee());
					query.setParameter("totalFee", feeMaster.getTotalFee());
					query.setParameter("standard", feeMaster.getStandard());
					query.executeUpdate();

					return oldFeeMaster;
				
			}else{
				FeeMaster FeeMaster1 = fetch2(feeMaster);
				if (FeeMaster1 == null) {
					oldFeeMaster.setUpdatedTime(CalendarCalculator.getTimeStamp());
					oldFeeMaster.setIsActive("N");
					session.update(oldFeeMaster);

					return oldFeeMaster;
				
			}
			

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
	private FeeMaster fetch2(FeeMaster feeMaster) {
		Hierarchy hierarchy=feeMaster.getHierarchy();
		FeeMaster feeMaster2=null;
		query=sessionFactory.getCurrentSession().createQuery("FROM FeeMaster WHERE standard=:standard AND isActive=:isActive and hierarchy.hid=:hierarchy");
		query.setParameter("hierarchy", hierarchy.getHid());
		
		query.setParameter("standard", feeMaster.getStandard());
		query.setParameter("isActive", "Y");
		feeMaster2= (FeeMaster) query.uniqueResult();
		return feeMaster2;
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
		Session session=this.sessionFactory.getCurrentSession();
		try {
			
			feeMaster.setExitTime(CalendarCalculator.getTimeStamp());
			feeMaster.setIsActive("D");
			session.update(feeMaster);

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd();
		
	}
	
	public FeeMaster getFeeMas(String entryTime,Hierarchy hierarchy)
	{
		Loggers.loggerStart();
			query = sessionFactory.getCurrentSession().createQuery("from FeeMaster where isActive=:isActive and entryTime =:entryTime and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
		
		query.setParameter("isActive", "Y");
		query.setParameter("entryTime", entryTime);
		FeeMaster fee=(FeeMaster) query.uniqueResult();

		Loggers.loggerValue("feeList", fee);
		return fee;
		
		
	}

	/**
	 * create instance for session and begins transaction
	 */
	/*public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}*/

	@Override
	public FeeMaster getFeeStructure(String standard,Long hid) throws GSmartDatabaseException  {
		try {
			
				if(standard!=null){
			query = sessionFactory.getCurrentSession().createQuery("from FeeMaster where standard='" + standard + "'  and isActive='Y' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);
			
		
		FeeMaster fee = (FeeMaster) query.list().get(0);
		return fee;
			
		}else{
			return null;
		}
		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
			return null;
		} 
	}




	@SuppressWarnings("unchecked")
	@Override
	public List<FeeMaster> searchfeemaster(FeeMaster feemaster, Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<FeeMaster> feelist = null;
		try{
			if(hid != null){
				query = sessionFactory.getCurrentSession().createQuery("from FeeMaster where standard like '%"+feemaster.getStandard()+"%' and isActive = 'Y' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hid);
			}else {
				query = sessionFactory.getCurrentSession().createQuery("from FeeMaster where standard like '%"+feemaster.getStandard()+"%' and isActive = 'Y'");
			}
			feelist = query.list();
		}catch (Exception e){
			
			e.printStackTrace();
		}
		return feelist;
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