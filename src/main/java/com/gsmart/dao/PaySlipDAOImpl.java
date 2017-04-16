package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.GenerateSalaryStatement;
import com.gsmart.model.PaySlip;
import com.gsmart.model.Profile;

@Repository
@Transactional
public class PaySlipDAOImpl implements PaySlipDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public GenerateSalaryStatement download(PaySlip paySlip) {

		String hql = "from GenerateSalaryStatement gss where gss.compoundGenerateSalaryStatement.empSmartId="+paySlip.getSmartId()+" and gss.compoundGenerateSalaryStatement.month='"+paySlip.getFromMonth()+"' and gss.compoundGenerateSalaryStatement.year='"+paySlip.getFromYear()+"'";
		Query qry = sessionFactory.getCurrentSession().createQuery(hql);
		GenerateSalaryStatement salStmt = (GenerateSalaryStatement) qry.uniqueResult();
		return salStmt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GenerateSalaryStatement> adminDownload(PaySlip paySlip) {

		String hql = "from GenerateSalaryStatement gss where gss.compoundGenerateSalaryStatement.month='"+paySlip.getFromMonth()+"' and gss.compoundGenerateSalaryStatement.year='"+paySlip.getFromYear()+"'";
		Query qry = sessionFactory.getCurrentSession().createQuery(hql);
		
		return qry.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> emailAddress(PaySlip paySlip){
		String hql = "from Profile where empSmartId="+paySlip.getSmartId();
		Query qry= sessionFactory.getCurrentSession().createQuery(hql);
		return qry.list();
	}

}
