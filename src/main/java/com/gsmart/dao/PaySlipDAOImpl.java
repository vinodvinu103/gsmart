package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gsmart.model.GenerateSalaryStatement;
import com.gsmart.model.PaySlip;
import com.gsmart.model.Profile;

public class PaySlipDAOImpl implements PaySlipDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public GenerateSalaryStatement download(PaySlip paySlip) {

		Session session = sessionFactory.openSession();
		String hql = "from GenerateSalaryStatement gss where gss.compoundGenerateSalaryStatement.empSmartId="+paySlip.getEmpSmartId()+" and gss.compoundGenerateSalaryStatement.month='"+paySlip.getFromMonth()+"' and gss.compoundGenerateSalaryStatement.year='"+paySlip.getFromYear()+"'";
		Query qry = session.createQuery(hql);
		GenerateSalaryStatement salStmt = (GenerateSalaryStatement) qry.uniqueResult();
		return salStmt;
	}

	@Override
	public List<GenerateSalaryStatement> adminDownload(PaySlip paySlip) {

		Session session = sessionFactory.openSession();
		String hql = "from GenerateSalaryStatement gss where gss.compoundGenerateSalaryStatement.month='"+paySlip.getFromMonth()+"' and gss.compoundGenerateSalaryStatement.year='"+paySlip.getFromYear()+"'";
		Query qry = session.createQuery(hql);
		
		return qry.list();
	}
	
	@Override
	public List<Profile> emailAddress(PaySlip paySlip){
		Session session = sessionFactory.openSession();
		String hql = "from Profile where empSmartId="+paySlip.getEmpSmartId();
		Query qry= session.createQuery(hql);
		return qry.list();
	}

}
