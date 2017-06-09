package com.gsmart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.PayRoll;
import com.gsmart.util.Loggers;

@Repository
public class PayRollDaoImpl implements PayRollDao {

	@Autowired
	private SessionFactory sessionfactory;
	private Session session;
	private Transaction tx;
	
	public void getConnection(){
		session=sessionfactory.openSession();
		tx=session.beginTransaction();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPayroll() {
		Loggers.loggerStart();
		Query query;
		Map<String, Object> resp=new HashMap<>();
		try{
		getConnection();
		List<PayRoll> payrollList=null;	
		query = session.createQuery("from PayRoll");
		payrollList=query.list();
		resp.put("payrollList", payrollList);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			session.close();
		}
		Loggers.loggerEnd();
		return resp;
	}

	@Override
	public PayRoll addPayroll(PayRoll payroll) {
		Loggers.loggerStart();
		try{
		getConnection();
		session.save(payroll);
		tx.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		Loggers.loggerEnd();
		return payroll;
	}

	@Override
	public PayRoll editPayroll(PayRoll payroll) {
		Loggers.loggerStart();
		try{
		getConnection();
		session.update(payroll);
		//tx.commit();
		}catch (Exception e) {
         e.printStackTrace();
         }finally {
			session.close();
		}
		Loggers.loggerEnd();
		return payroll;
	}

	@Override
	public void deletePayroll(PayRoll payroll) {
		try {
			getConnection();
			Loggers.loggerStart();
			session.update(payroll);
			tx.commit();
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
	}
}
