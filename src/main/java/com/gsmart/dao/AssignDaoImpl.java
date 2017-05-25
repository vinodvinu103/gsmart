package com.gsmart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;

import javax.validation.ConstraintViolationException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.Assign;
import com.gsmart.model.CompoundAssign;
import com.gsmart.model.FeeMaster;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class AssignDaoImpl implements AssignDao {

	@Autowired
	private SessionFactory sessionFactory;


	Query query;
	Criteria criteria = null;
	Criteria criteriaCount=null;
	Long count=null;
	
	/*public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getAssignReportee(Long hid, Integer min, Integer max) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Assign> assignList = null;
		
		Map<String, Object> assignMap = new HashMap<>();
		
		try {

			criteria = sessionFactory.getCurrentSession().createCriteria(Assign.class);
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			criteria.addOrder(Order.asc("standard"));
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("hierarchy.hid", hid));
			assignList = criteria.list();
			criteriaCount= sessionFactory.getCurrentSession().createCriteria(Assign.class);
			criteriaCount.add(Restrictions.eq("isActive", "Y"));
			criteriaCount.add(Restrictions.eq("hierarchy.hid", hid));
			criteriaCount.setProjection(Projections.rowCount());
			 count= (Long) criteriaCount.uniqueResult();
			assignMap.put("totalassign", count);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		assignMap.put("assignList", assignList);
		Loggers.loggerEnd();
		return assignMap;
	}
	
	@Override
	public CompoundAssign addAssigningReportee(Assign assign) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		
		CompoundAssign compoundAssign = null;
		Assign assign2=null;
		try {
			
				assign2=fetch2(assign);
			
			if (assign2 == null) {
				assign.setEntryTime(CalendarCalculator.getTimeStamp());
				assign.setIsActive("Y");
				compoundAssign = (CompoundAssign) session.save(assign);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return compoundAssign;
	}
	
	public Assign fetch(Assign assign) {
		Loggers.loggerStart(assign);
		Assign assignList = null;
		try {
			
			query = sessionFactory.getCurrentSession().createQuery("FROM Assign WHERE standard=:standard and section=:section AND isActive=:isActive and hierarchy.hid=:hierarchy and teacherSmartId=:teacherSmartId");
			query.setParameter("teacherSmartId", assign.getTeacherSmartId());
			query.setParameter("standard", assign.getStandard());
			query.setParameter("isActive", "Y");
			query.setParameter("hierarchy", assign.getHierarchy().getHid());
			query.setParameter("section", assign.getSection());
			assignList = (Assign) query.uniqueResult();
			Loggers.loggerEnd(assignList);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return assignList;
	}

	@Override
	public Assign editAssigningReportee(Assign assign) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Assign asgn = null;
		try {
			
			Assign oldAssign = getAssigns(assign.getEntryTime(),assign.getHierarchy());
			asgn = updateAssign(oldAssign, assign);
			CompoundAssign ch =	addAssigningReportee(assign);
			if(ch!=null){
				
				query = sessionFactory.getCurrentSession().createQuery("UPDATE Profile SET reportingManagerName=:teacherName, reportingManagerId=:reportingManagerId, counterSigningManagerId=:counterSigningManagerId WHERE hierarchy.hid=:hierarchy and standard=:standard and section=:section");
				query.setParameter("reportingManagerId", assign.getTeacherSmartId());
				query.setParameter("counterSigningManagerId", assign.getHodSmartId());
				query.setParameter("standard", assign.getStandard());
				query.setParameter("section", assign.getSection());
				query.setParameter("teacherName", assign.getTeacherName());
				query.setParameter("hierarchy", assign.getHierarchy().getHid());
				query.executeUpdate();
			}			
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		return asgn;
	}
	
	private Assign updateAssign(Assign oldAssign, Assign assign) throws GSmartDatabaseException {
		Assign asg = null;
		try {
			Session session=this.sessionFactory.getCurrentSession();
			if(assign.getStandard().equals(oldAssign.getStandard()) && assign.getSection().equals(oldAssign.getSection())){
				Assign assign1 = fetch(assign);
				if (assign1 == null) {
					oldAssign.setUpdatedTime(CalendarCalculator.getTimeStamp());
					oldAssign.setIsActive("N");
					session.update(oldAssign);
					return oldAssign;
				}
			}else{
				Assign assig2=fetch2(assign);
				if (assig2 == null) {
					oldAssign.setUpdatedTime(CalendarCalculator.getTimeStamp());
					oldAssign.setIsActive("N");
					session.update(oldAssign);
					return oldAssign;
				}
			}
				
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		return asg;
	}
	
	

	private Assign fetch2(Assign assign) {
		Assign assignList=null;
		query = sessionFactory.getCurrentSession().createQuery("FROM Assign WHERE standard=:standard and section=:section AND isActive=:isActive and hierarchy.hid=:hierarchy");
		query.setParameter("standard", assign.getStandard());
		query.setParameter("isActive", "Y");
		query.setParameter("hierarchy", assign.getHierarchy().getHid());
		query.setParameter("section", assign.getSection());
		assignList = (Assign) query.uniqueResult();
		Loggers.loggerEnd(assignList);
		return assignList;
	}

	public Assign getAssigns(String entryTime,Hierarchy hierarchy) {
		Loggers.loggerStart();
		try {
			query = sessionFactory.getCurrentSession().createQuery("from Assign where isActive='Y' and entryTime='" + entryTime + "' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			Assign assign = (Assign) query.uniqueResult();
			return assign;

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerEnd();
			return null;
		}
	}

	@Override
	public void deleteAssigningReportee(Assign assign) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			Session session=this.sessionFactory.getCurrentSession();
			
			query = sessionFactory.getCurrentSession().createQuery("UPDATE Profile SET reportingManagerId='null', standard='null', section='null' WHERE standard=:standard and section=:section");
			query.setParameter("standard", assign.getStandard());
			query.setParameter("section", assign.getSection());
			query.executeUpdate();			
			
			assign.setIsActive("D");
			assign.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(assign);
		} catch (Exception e) {
			e.printStackTrace();
			
			Loggers.loggerException(e.getMessage());
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd();
	}

	@Override
	public Assign getStaffByClassAndSection(String standard, String section, Hierarchy hierarchy) {
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Assign where hierarchy.hid=:hierarchy and standard=:standard and section=:section");
			query.setParameter("standard", standard);
			query.setParameter("section", section);
			query.setParameter("hierarchy", hierarchy.getHid());
			return (Assign) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean searchStandardFeeDao(String standard,Long hid){
		Loggers.loggerStart();
		boolean status = false;
		try{
			query = sessionFactory.getCurrentSession().createQuery("from FeeMaster where standard =:standard and isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("standard", standard);
			query.setParameter("isActive", "Y");
			query.setParameter("hierarchy", hid);
			FeeMaster fem =(FeeMaster) query.uniqueResult();
			if(fem == null){
				status=true;
			}
			else{
				status= false;
			}	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Loggers.loggerEnd();
		return status;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Assign> getAssignList(Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Assign> assignList=null;
		try {
			query = sessionFactory.getCurrentSession().createQuery("FROM Assign WHERE isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);
			query.setParameter("isActive", "Y");
			assignList=query.list();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd(assignList);
		return assignList;
	}

	@Override
	public List<Assign> searchassign(Assign assign, Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Assign> assignList = null;
		try{
			if(hid !=null){
				query= sessionFactory.getCurrentSession().createQuery("from Assign where standard like '%"+assign.getStandard()+"%' and isActive='Y' and hierarchy.hid = :hierarchy");
				query.setParameter("hierarchy", hid);
			}else{
				query = sessionFactory.getCurrentSession().createQuery("from Assign where standard like '%"+assign.getStandard()+"%' and isActive='Y'");
			}
			assignList = query.list();
			Loggers.loggerEnd(assignList);
		}catch (Exception e){
			e.printStackTrace();
		}
		return assignList;
	}
}
