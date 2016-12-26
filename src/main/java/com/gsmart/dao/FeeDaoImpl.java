package com.gsmart.dao;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Fee;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;

@Repository
public class FeeDaoImpl implements FeeDao{
	

	@Autowired
	SessionFactory sessionFactory;
	
	Session session;
	Transaction transaction;
	Query query;
	
	Logger logger=Logger.getLogger(FeeDaoImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Fee> getFeeList(Fee fee) throws GSmartDatabaseException {
		logger.debug("Start :: FeeDaoImpl. getFeeList()");
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
		logger.debug("End :: FeeDaoImpl. getFeeList()");
		return feeList;
	}
	
	@Override
	public void addFee(Fee fee) throws GSmartDatabaseException {
		logger.debug("Start :: FeeDaoImpl. addFee()");
		try{
			getconnection();
			fee.setBalanceFee(fee.getTotalFee()-fee.getPaidFee());
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
		logger.debug("End :: FeeDaoImpl. addFee()");
	}
	
	public void getconnection(){
		session=sessionFactory.openSession();
		transaction=session.beginTransaction();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Fee> getFeeLists(String academicYear) throws GSmartDatabaseException {
		getconnection();
		query=session.createQuery("from Fee where academicYear= :academicYear");
		query.setParameter("academicYear", academicYear);
		
		ArrayList<Fee> feeList=(ArrayList<Fee>) query.list();
		return feeList;
	}

}
