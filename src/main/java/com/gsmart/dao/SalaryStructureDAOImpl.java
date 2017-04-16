package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.SalaryStructure;
import com.gsmart.util.CalendarCalculator;

@Repository
@Transactional
public class SalaryStructureDAOImpl implements SalaryStructureDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SalaryStructure> view() {

		String hql = "from SalaryStructure where isActive='Y'";
		Query qry =  sessionFactory.getCurrentSession().createQuery(hql);
		return qry.list();
		
	}

	@Override
	public void add(SalaryStructure salaryStructure) {
		Session session=this.sessionFactory.getCurrentSession();

		salaryStructure.setIsActive("Y");
		salaryStructure.setEntryTime(CalendarCalculator.getTimeStamp());
		salaryStructure.setSalary(salaryStructure.getSalary());
		salaryStructure.setStatutory(salaryStructure.getStatutory());
		session.save(salaryStructure);
	}

	@Override
	public void edit(SalaryStructure salaryStructure) {
		Session session=this.sessionFactory.getCurrentSession();

		String hql = "from SalaryStructure ss where isActive='Y' and ss.compoundSalaryStructure.entryTime='"+salaryStructure.getEntryTime()+"'";
		Query qry = sessionFactory.getCurrentSession().createQuery(hql);
		SalaryStructure oldSalaryStructure = (SalaryStructure) qry.uniqueResult();
		oldSalaryStructure.setIsActive("N");
		oldSalaryStructure.setUpdatedTime(CalendarCalculator.getTimeStamp());
		session.update(oldSalaryStructure);
		add(salaryStructure);
	}

	@Override
	public void delete(SalaryStructure salaryStructure) {
		Session session=this.sessionFactory.getCurrentSession();

		String hql = "from SalaryStructure ss where isActive='Y' and ss.compoundSalaryStructure.entryTime='"+salaryStructure.getEntryTime()+"'";
		Query qry = sessionFactory.getCurrentSession().createQuery(hql);
		SalaryStructure oldSalaryStructure = (SalaryStructure) qry.uniqueResult();
		oldSalaryStructure.setIsActive("D");
		oldSalaryStructure.setExitTime(CalendarCalculator.getTimeStamp());
		session.update(oldSalaryStructure);
	}

}
