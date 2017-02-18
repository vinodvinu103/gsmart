package com.gsmart.dao;

import java.util.List;

import javax.validation.ConstraintViolationException;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Assign;
import com.gsmart.model.CompoundAssign;
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
	
	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Assign> getAssignReportee(String role, Hierarchy hierarchy) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		List<Assign> assignList = null;
		try {

			getConnection();
				
			if(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director"))
	 			query = session.createQuery("from Assign where isActive=:isActive");
			else {
				query = session.createQuery("from Assign where isActive=:isActive and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
			query.setParameter("isActive", "Y");
			assignList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}

		Loggers.loggerEnd();
		return assignList;
	}
	
	@Override
	public CompoundAssign addAssigningReportee(Assign assign) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		
		CompoundAssign compoundAssign = null;
		try {
			if(assign.getHierarchy().getHid() == null){
				query = session.createQuery("FROM Assign WHERE standard=:standard AND isActive=:isActive");
			}else{
			query = session.createQuery("FROM Assign WHERE standard=:standard and section=:section AND isActive=:isActive and hierarchy.hid=:hierarchy ");
			}
			query.setParameter("standard", assign.getStandard());
			query.setParameter("isActive", "Y");
			query.setParameter("hierarchy", assign.getHierarchy().getHid());
			query.setParameter("section", assign.getSection());
			Assign assign1 = (Assign) query.uniqueResult();
			if (assign1 == null) {
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
			if(assign.getHierarchy().getHid() == null){
				query = session.createQuery("FROM Assign WHERE standard=:standard AND isActive=:isActive");
			}else{
			query = session.createQuery("FROM Assign WHERE standard=:standard and section=:section AND isActive=:isActive and hierarchy.hid=:hierarchy and teacherSmartId=:teacherSmartId");
			}
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
			
			Assign oldAssign = getAssigns(assign.getEntryTime());
			asgn = updateAssign(oldAssign, assign);
			CompoundAssign ch =	addAssigningReportee(assign);
			if(ch!=null){
				getConnection();
				System.out.println("TechID"+assign.getTeacherSmartId());
				System.out.println("TechName"+assign.getTeacherName());
				System.out.println("class"+assign.getStandard());
				System.out.println("section"+assign.getSection());
				System.out.println("hid"+assign.getHierarchy().getHid());				
			
				query = session.createQuery("UPDATE Profile SET reportingManagerName=:teacherName,reportingManagerId=:reportingManagerId , counterSigningManagerId=:counterSigningManagerId WHERE hierarchy.hid=:hierarchy and standard=:standard and section=:section");
				
				query.setParameter("reportingManagerId", assign.getTeacherSmartId());
				query.setParameter("counterSigningManagerId", assign.getHodSmartId());
				query.setParameter("standard", assign.getStandard());
				query.setParameter("section", assign.getSection());
				query.setParameter("teacherName", assign.getTeacherName());
				query.setParameter("hierarchy", assign.getHierarchy().getHid());
				int a=query.executeUpdate();
				transaction.commit();
				System.out.println("updated"+a);
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
			if(assign.getSection().equals(oldAssign.getSection()) && assign.getStandard().equals(oldAssign.getStandard())){
				Assign assign1 = fetch(assign);
				if (assign1 == null) {
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
	
	

	public Assign getAssigns(String entryTime) {
		Loggers.loggerStart();
		try {
			query = session.createQuery("from Assign where isActive='Y' and entryTime='" + entryTime + "'");
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
}