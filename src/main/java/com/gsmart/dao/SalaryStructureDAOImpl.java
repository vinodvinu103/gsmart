package com.gsmart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.CompoundSalaryStructure;
import com.gsmart.model.SalaryStructure;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class SalaryStructureDAOImpl implements SalaryStructureDAO{

	@Autowired
	SessionFactory sessionFactory;

	Session session = null;;
	Query query;
	Transaction transaction = null;
	Criteria criteria = null;
	Criteria criteriaCount=null;
	Long count=null;
	
	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}


	@Override
	public Map<String, Object> getSalaryStructure(Long hid, Integer min, Integer max) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		List<SalaryStructure> salaryStructureList = null;
		
		Map<String, Object> salaryStructureMap = new HashMap<>();
		
		try {
            criteria = session.createCriteria(SalaryStructure.class);
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			criteria.addOrder(Order.asc("standard"));
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("hierarchy.hid", hid));
			salaryStructureList = criteria.list();
			criteriaCount= session.createCriteria(SalaryStructure.class);
			criteriaCount.setProjection(Projections.rowCount());
			 count= (Long) criteriaCount.uniqueResult();
			 salaryStructureMap.put("totalSalaryStructure", count);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
		salaryStructureMap.put("assignList", salaryStructureMap);
		Loggers.loggerEnd();
		return salaryStructureMap;
	}
	
	
	@Override
	public CompoundSalaryStructure addSalaryStructure(SalaryStructure salarystructure) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		CompoundSalaryStructure css=null;
		try{
			query=session.createQuery("from SalaryStructure where isActive=:isActive and smartId=:smartId and year=:year hierarchy.hid=:hierarchy");
			query.setParameter("smartId",salarystructure.getSmartId());
			query.setParameter("hierarchy", salarystructure.getHierarchy().getHid());
			query.setParameter("year", salarystructure.getYear());
			css = (CompoundSalaryStructure) query.list();
		}catch (Exception e) {
          e.printStackTrace();
		}
		return css;
	}

	@Override
	public SalaryStructure editSalaryStructure(SalaryStructure salarystructure) throws GSmartDatabaseException {
		return null;
	}
	
	
	
	
	/*@Override
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
*/
}
