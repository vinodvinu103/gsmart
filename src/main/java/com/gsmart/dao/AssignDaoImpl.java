package com.gsmart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import javax.validation.ConstraintViolationException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Assign;
import com.gsmart.model.CompoundAssign;
import com.gsmart.model.FeeMaster;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class AssignDaoImpl implements AssignDao {

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

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getAssignReportee(Long hid, Integer min, Integer max) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		List<Assign> assignList = null;
		
		Map<String, Object> assignMap = new HashMap<>();
		
		try {
			criteria = session.createCriteria(Assign.class);
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("hierarchy.hid", hid));
			assignList = criteria.list();
			criteriaCount= session.createCriteria(Assign.class);
			criteriaCount.setProjection(Projections.rowCount());
			 count= (Long) criteriaCount.uniqueResult();
			assignMap.put("totalassign", count);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
		assignMap.put("assignList", assignList);
		Loggers.loggerEnd();
		return assignMap;
	}
	
	@Override
	public CompoundAssign addAssigningReportee(Assign assign) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		
		CompoundAssign compoundAssign = null;
		Assign assign2=null;
		try {
			
				assign2=fetch2(assign);
			
			if (assign2 == null) {
				assign.setEntryTime(CalendarCalculator.getTimeStamp());
				assign.setIsActive("Y");
				compoundAssign = (CompoundAssign) session.save(assign);
				transaction.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			session.close();
		}
		return compoundAssign;
	}
	
	public Assign fetch(Assign assign) {
		Loggers.loggerStart(assign);
		getConnection();
		Assign assignList = null;
		try {
			
			query = session.createQuery("FROM Assign WHERE standard=:standard and section=:section AND isActive=:isActive and hierarchy.hid=:hierarchy and teacherSmartId=:teacherSmartId");
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
		getConnection();
		Loggers.loggerStart();
		Assign asgn = null;
		try {
			getConnection();			
			
			Assign oldAssign = getAssigns(assign.getEntryTime(),assign.getHierarchy());
			asgn = updateAssign(oldAssign, assign);
			CompoundAssign ch =	addAssigningReportee(assign);
			if(ch!=null){
				getConnection();
				
				query = session.createQuery("UPDATE Profile SET reportingManagerName=:teacherName, reportingManagerId=:reportingManagerId, counterSigningManagerId=:counterSigningManagerId WHERE hierarchy.hid=:hierarchy and standard=:standard and section=:section");
				query.setParameter("reportingManagerId", assign.getTeacherSmartId());
				query.setParameter("counterSigningManagerId", assign.getHodSmartId());
				query.setParameter("standard", assign.getStandard());
				query.setParameter("section", assign.getSection());
				query.setParameter("teacherName", assign.getTeacherName());
				query.setParameter("hierarchy", assign.getHierarchy().getHid());
				query.executeUpdate();
				transaction.commit();
				session.close();
			}			
		} catch (Exception e) {
			e.printStackTrace();
			// throw new GSmartDatabaseException(e.getMessage());
			Loggers.loggerException(e.getMessage());
		}
		return asgn;
	}
	
	private Assign updateAssign(Assign oldAssign, Assign assign) throws GSmartDatabaseException {
		Assign asg = null;
		try {
			if(assign.getStandard().equals(oldAssign.getStandard()) && assign.getSection().equals(oldAssign.getSection())){
				Assign assign1 = fetch(assign);
				if (assign1 == null) {
					oldAssign.setUpdatedTime(CalendarCalculator.getTimeStamp());
					oldAssign.setIsActive("N");
					session.update(oldAssign);
					transaction.commit();
					return oldAssign;
				}
			}else{
				Assign assig2=fetch2(assign);
				if (assig2 == null) {
					oldAssign.setUpdatedTime(CalendarCalculator.getTimeStamp());
					oldAssign.setIsActive("N");
					session.update(oldAssign);
					transaction.commit();
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
		query = session.createQuery("FROM Assign WHERE standard=:standard and section=:section AND isActive=:isActive and hierarchy.hid=:hierarchy");
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
			query = session.createQuery("from Assign where isActive='Y' and entryTime='" + entryTime + "' and hierarchy.hid=:hierarchy");
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
		getConnection();
		Loggers.loggerStart();
		try {
			getConnection();
			
			query = session.createQuery("UPDATE Profile SET reportingManagerId='null', standard='null', section='null' WHERE standard=:standard and section=:section");
			query.setParameter("standard", assign.getStandard());
			query.setParameter("section", assign.getSection());
			query.executeUpdate();			
			
			assign.setIsActive("D");
			assign.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(assign);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
	}

	@Override
	public Assign getStaffByClassAndSection(String standard, String section, Hierarchy hierarchy) {
		getConnection();
		try {
			query = session.createQuery(
					"from Assign where hierarchy.hid=:hierarchy and standard=:standard and section=:section");
			query.setParameter("standard", standard);
			query.setParameter("section", section);
			query.setParameter("hierarchy", hierarchy.getHid());
			return (Assign) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			session.close();
		}
	}
	
	@Override
	public boolean searchStandardFeeDao(String standard,Long hid){
		getConnection();
		Loggers.loggerStart();
		boolean status = false;
		try{
			query = session.createQuery("from FeeMaster where standard =:standard and isActive=:isActive and hierarchy.hid=:hierarchy");
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
		finally{
			session.close();
		}
		Loggers.loggerEnd();
		return status;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Assign> getAssignList(Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart();
		getConnection();
		List<Assign> assignList=null;
		try {
			query = session.createQuery("FROM Assign WHERE isActive=:isActive and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);
			query.setParameter("isActive", "Y");
			assignList=query.list();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
		Loggers.loggerEnd(assignList);
		return assignList;
	}
}
