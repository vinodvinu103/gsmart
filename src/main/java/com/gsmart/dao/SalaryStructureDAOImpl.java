package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.SalaryStructure;
import com.gsmart.util.CalendarCalculator;

public class SalaryStructureDAOImpl implements SalaryStructureDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<SalaryStructure> view() {

		Session session = sessionFactory.openSession();
		String hql = "from SalaryStructure where isActive='Y'";
		Query qry =  session.createQuery(hql);
		return qry.list();
		
	}

	@Override
	public void add(SalaryStructure salaryStructure) {

		Session session = sessionFactory.openSession();
		session.beginTransaction();
		salaryStructure.setIsActive("Y");
		salaryStructure.setEntryTime(CalendarCalculator.getTimeStamp());
		salaryStructure.setSalary(salaryStructure.getSalary());
		salaryStructure.setStatutory(salaryStructure.getStatutory());
		session.save(salaryStructure);
		session.getTransaction().commit();
	}

	@Override
	public void edit(SalaryStructure salaryStructure) {

		Session session = sessionFactory.openSession();
		session.beginTransaction();
		String hql = "from SalaryStructure ss where isActive='Y' and ss.compoundSalaryStructure.entryTime='"+salaryStructure.getEntryTime()+"'";
		Query qry = session.createQuery(hql);
		SalaryStructure oldSalaryStructure = (SalaryStructure) qry.uniqueResult();
		oldSalaryStructure.setIsActive("N");
		oldSalaryStructure.setUpdatedTime(CalendarCalculator.getTimeStamp());
		session.update(oldSalaryStructure);
		add(salaryStructure);
		session.getTransaction().commit();
	}

	@Override
	public void delete(SalaryStructure salaryStructure) {

		Session session = sessionFactory.openSession();
		session.beginTransaction();
		String hql = "from SalaryStructure ss where isActive='Y' and ss.compoundSalaryStructure.entryTime='"+salaryStructure.getEntryTime()+"'";
		Query qry = session.createQuery(hql);
		SalaryStructure oldSalaryStructure = (SalaryStructure) qry.uniqueResult();
		oldSalaryStructure.setIsActive("D");
		oldSalaryStructure.setExitTime(CalendarCalculator.getTimeStamp());
		session.update(oldSalaryStructure);
		session.getTransaction().commit();
	}

}
