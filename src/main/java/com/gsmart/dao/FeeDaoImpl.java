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

import com.gsmart.model.Fee;
import com.gsmart.model.FeeMaster;
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
	public ArrayList<Fee> getFeeList(Fee fee) throws GSmartDatabaseException {
		Loggers.loggerStart();
		ArrayList<Fee> feeList;
		try{
			getconnection();
			query=session.createQuery("from Fee where smartId =:smartId and academicYear =:academicYear");
			query.setParameter("smartId", fee.getSmartId());
			query.setParameter("academicYear", fee.getAcademicYear());
			feeList=(ArrayList<Fee>) query.list();
		}catch(Exception e){
			throw new GSmartDatabaseException(e.getMessage());
		}finally{
			session.close();
		}
		Loggers.loggerEnd();
		return feeList;
	}
	
	@Override
	public void addFee(Fee fee) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try{
			getconnection();
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
		
		return profileList;
		
	}
	public void getconnection(){
		session=sessionFactory.openSession();
		transaction=session.beginTransaction();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Fee> getFeeLists(String academicYear) throws GSmartDatabaseException {
		Loggers.loggerStart();
		ArrayList<Fee> feeList=null;
		try
		{
		System.out.println(academicYear);
		getconnection();
		Loggers.loggerValue("getting connections", "");
		query=session.createQuery("From Fee where academicYear=:academicYear");
		query.setParameter("academicYear", academicYear);
		feeList=(ArrayList<Fee>) query.list();
		Loggers.loggerEnd();
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return feeList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Fee> gettotalfee() throws GSmartServiceException {
		Loggers.loggerStart();
		getconnection();
		query = session.createQuery("From Fee");
		List<Fee> list = query.list();
		session.close();
		Loggers.loggerEnd();
		return list;
	}
	
	@Override
	public List<Fee> getPaidStudentsList() throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Fee> paidStudentsList=null;
		try
		{
//		System.out.println(academicYear);
		getconnection();
		Loggers.loggerValue("getting connections", "");
		query=session.createQuery("From Fee where feeStatus='paid' and isActive='Y'");
		//query.setParameter("academicYear", academicYear);
		paidStudentsList=(List<Fee>) query.list();
		Loggers.loggerEnd();
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return paidStudentsList;
	
	}

	@Override
	public List<Fee> getUnpaidStudentsList() throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Fee> unpaidStudentsList=null;
		try
		{
		//System.out.println(academicYear);
		getconnection();
		Loggers.loggerValue("getting connections", "");
		query=session.createQuery("From Fee where feeStatus='unpaid' and isActive='Y'");
		//query.setParameter("academicYear", academicYear);
		unpaidStudentsList=(List<Fee>) query.list();
		Loggers.loggerEnd();
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return unpaidStudentsList;
	
	}

	@Override
	public void editFee(Fee fee) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			getconnection();
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
			Loggers.loggerStart();
			query = session.createQuery("from Fee where isActive=:isActive and entryTime =:entryTime");
			query.setParameter("isActive", "Y");
			query.setParameter("entryTime", entryTime);
			Fee fee=(Fee) query.uniqueResult();

			Loggers.loggerValue("feeList", fee);
			return fee;
			
			
		}
	
	

	@Override
	public void deleteFee(Fee fee) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			getconnection();
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
