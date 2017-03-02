package com.gsmart.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Holiday;
import com.gsmart.model.Profile;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Repository
public class FeeDaoImpl implements FeeDao{
	

	@Autowired
	SessionFactory sessionFactory;
	
	Session session;
	Transaction transaction;
	Query query;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Fee> getFeeList(Fee fee, String role, Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getconnection();
		Loggers.loggerStart(fee.getAcademicYear());
		
		ArrayList<Fee> feeList;
		try{
			if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director"))
			{
				query=session.createQuery("from Fee where smartId =:smartId and academicYear =:academicYear");
			}
			else{
			
			query=session.createQuery("from Fee where smartId =:smartId and academicYear =:academicYear and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			}
			query.setParameter("smartId", fee.getSmartId());
			query.setParameter("academicYear", fee.getAcademicYear());
			feeList=(ArrayList<Fee>) query.list();
		}catch(Exception e){
			throw new GSmartDatabaseException(e.getMessage());
		}/*finally{
			session.close();
		}*/
		Loggers.loggerEnd();
		return feeList;
	}
	
	@Override
	public void addFee(Fee fee) throws GSmartDatabaseException {
		getconnection();
		Loggers.loggerStart();
		
		try{
	
			if(fee.getPaidFee()>0)
			{
			fee.setBalanceFee(fee.getBalanceFee()-fee.getPaidFee());
				if(fee.getBalanceFee()<=0){
					Loggers.loggerStart("inside the paid");
					fee.setFeeStatus("paid");
				}else{
					fee.setFeeStatus("unpaid");
				}
				
			}else
			{
				fee.setBalanceFee(fee.getTotalFee());
			}
     		fee.setEntryTime(CalendarCalculator.getTimeStamp());
			fee.setDate(CalendarCalculator.getTimeStamp());
			Profile profile=getReportingManagerId(fee.getSmartId());
			fee.setReportingManagerId(profile.getReportingManagerId());
			fee.setParentName(profile.getFatherName());
			fee.setIsActive("Y");
			session.save(fee);
			transaction.commit();
		}catch(ConstraintViolationException e){
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		}catch(Exception e){
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}finally{
			session.close();
		}
		Loggers.loggerEnd();
	}
	
	public Profile getReportingManagerId(String smartId)
	{
		Loggers.loggerStart();
		query=session.createQuery("from Profile where smartId=:smartId");
		query.setParameter("smartId", smartId);
		Profile profileList=(Profile) query.uniqueResult();
		Loggers.loggerStart(profileList);
		
		return profileList;
		
	}
	public void getconnection(){
		session=sessionFactory.openSession();
		transaction=session.beginTransaction();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Fee> getFeeLists(String academicYear,String role,Hierarchy hierarchy) throws GSmartDatabaseException {
		getconnection();
		Loggers.loggerStart();
		
		ArrayList<Fee> feeList=null;
		try
		{
			if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director"))
			{
		query=session.createQuery("From Fee where academicYear=:academicYear and isActive='Y'");
			}else
			{
				query=session.createQuery("From Fee where academicYear=:academicYear and isActive='Y' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			}
		query.setParameter("academicYear", academicYear);
		feeList=(ArrayList<Fee>) query.list();
		Loggers.loggerEnd();
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			session.close();
		}
		return feeList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Fee> gettotalfee(String role,Hierarchy hierarchy) throws GSmartServiceException {
		getconnection();
		Loggers.loggerStart();
		List<Fee> list=null;
		try {
		if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director"))
		{
		query = session.createQuery("From Fee where isActive=:isActive");
		}else{
			query = session.createQuery("From Fee where isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
		}
		query.setParameter("isActive", "Y");
		list = query.list();
		}catch (Exception e) {

			e.printStackTrace();
		}
	
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPaidStudentsList(String role,Hierarchy hierarchy, Integer min, Integer max) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getconnection();
		Loggers.loggerStart();
		List<Fee> paidStudentsList=null;
		Map<String, Object> paidfeeMap = new HashMap<String, Object>();
		Criteria criteria = null;
		try
		{
//		System.out.println(academicYear);
		
		Loggers.loggerValue("getting connections", "");
		if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director"))
		{
		query=session.createQuery("From Fee where feeStatus='paid' and isActive='Y'");
		}else{
			query=session.createQuery("From Fee where feeStatus='paid' and isActive='Y' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
		}
		//query.setParameter("academicYear", academicYear);
//		paidStudentsList=(List<Fee>) query.list();
		criteria = session.createCriteria(Fee.class);
		criteria.setMaxResults(max);
		criteria.setFirstResult(min);
		criteria.setProjection(Projections.id());
		paidStudentsList = criteria.list();
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		paidfeeMap.put("totalpaidlist", query.list().size());
		Loggers.loggerEnd();
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
		paidfeeMap.put("paidStudentsList", paidStudentsList);
		return paidfeeMap;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getUnpaidStudentsList(String role,Hierarchy hierarchy, Integer min, Integer max) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Fee> unpaidStudentsList=null;
		Map<String, Object> unpaidfeeMap = new HashMap<String, Object>();
		Criteria criteria = null;
		getconnection();
		try
		{
		//System.out.println(academicYear);
			if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director"))
			{
			query=session.createQuery("From Fee where feeStatus='unpaid' and isActive='Y'");
			}else{
				query=session.createQuery("From Fee where feeStatus='unpaid' and isActive='Y' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
		//query.setParameter("academicYear", academicYear);
		unpaidStudentsList=(List<Fee>) query.list();
		criteria = session.createCriteria(Fee.class);
		criteria.setMaxResults(max);
		criteria.setFirstResult(min);
		criteria.setProjection(Projections.id());
		unpaidStudentsList = criteria.list();
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		unpaidfeeMap.put("totalunpaidlist", query.list().size());
		Loggers.loggerEnd();
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
		unpaidfeeMap.put("unpaidStudentsList", unpaidStudentsList);
		return unpaidfeeMap;
	
	}

	@Override
	public void editFee(Fee fee) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			
			Fee oldFee = getFee(fee.getEntryTime());
			oldFee.setUpdatedTime(CalendarCalculator.getTimeStamp());
			oldFee.setIsActive("N");
			session.update(oldFee);
			
			/*feeMaster.setEntryTime(CalendarCalculator.getTimeStamp());
			feeMaster.setIsActive("Y");
			session.save(feeMaster);*/

			transaction.commit();
			addFee(fee);
			
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			//session.close();
		}
		Loggers.loggerEnd();
		
	}

	
		

		public Fee getFee(String entryTime)
		{
			getconnection();
			Loggers.loggerStart();
			query = session.createQuery("from Fee where isActive=:isActive and entryTime =:entryTime");
			query.setParameter("isActive", "Y");
			query.setParameter("entryTime", entryTime);
			Fee fee=(Fee) query.uniqueResult();

			//session.close();
			Loggers.loggerValue("feeList", fee);
			return fee;
			
			
		}
	
	

	@Override
	public void deleteFee(Fee fee) throws GSmartDatabaseException {
		getconnection();
		Loggers.loggerStart();
		try {
			
			/*query = session.createQuery("update FeeMaster set IsActive=:IsActive, exittime=:exittime where entrytime = :entrytime");
			query.setParameter("entrytime", feeMaster.getEntrytime());
		
			query.setParameter("IsActive", feeMaster.getIsActive());
			query.setParameter("exittime", feeMaster.getExittime());*/
			fee.setExitTime(CalendarCalculator.getTimeStamp());
			fee.setIsActive("D");
			session.update(fee);
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
