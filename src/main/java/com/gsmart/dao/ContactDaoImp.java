package com.gsmart.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.Details;
import com.gsmart.model.Fee;
import com.gsmart.model.MessageDetails;
import com.gsmart.model.Profile;
import com.gsmart.model.Token;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;


@SuppressWarnings("unchecked")
@Repository
@Transactional
public class ContactDaoImp implements ContactDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	Query query = null;
	

	@Override
	public boolean studentToTeacher(MessageDetails details, String role) throws Exception {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		try {
			details.setEntryTime(CalendarCalculator.getTimeStamp());
			
			details.setPostedBy(role);
			details.setClasssection(details.getClasssection());
			details.setAcademicYear(details.getAcademicYear());
			details.setHierarchy(details.getHierarchy());
			details.setReadByTeacher("Y");
			details.setReadByStudent("Unread");
			session.save(details);
			Loggers.loggerEnd();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean teacherToStudent(MessageDetails details, String role) throws Exception {
		Loggers.loggerStart(details);
		Session session=this.sessionFactory.getCurrentSession();
		try {
			details.setEntryTime(CalendarCalculator.getTimeStamp());
			
			details.setPostedTo(details.getPostedTo());
			details.setClasssection(details.getClasssection());
			details.setAcademicYear(details.getAcademicYear());
			details.setHierarchy(details.getHierarchy());
			details.setReadByStudent("Unread");
			details.setPostedBy(role);
			details.setReadByTeacher("Y");
			session.save(details);
			Loggers.loggerEnd();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
	
	@Override
	public List<MessageDetails> msgList(MessageDetails details) throws Exception {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			String reportingManagerId = details.getReportingManagerId();
			query = session.createQuery(
					"from MessageDetails where readByStudent='Unread' and reportingManagerId=:rId ORDER BY entryTime DESC");
			query.setParameter("rId", reportingManagerId);
			List<MessageDetails> messages = (List<MessageDetails>) query.list();
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<String, Object> teacherView(Token token, Integer min, Integer max) throws Exception {
		Loggers.loggerStart();

		List<MessageDetails> messages = null;
		Map<String, Object> messageMap = new HashMap<>();
		
		int count = 0;
		try {
			Session session=this.sessionFactory.getCurrentSession();
			String reportingManagerId = token.getSmartId();
			
			System.out.println("reporting manager id :"+reportingManagerId);
				
			Criteria criteria1 = session.createCriteria(MessageDetails.class);
			criteria1.add(Restrictions.eq("postedBy", "STUDENT"));
			criteria1.add(Restrictions.eq("readByStudent", "Unread"));
			criteria1.add(Restrictions.or(Restrictions.eq("postedTo", reportingManagerId), Restrictions.isNull("postedTo")));
			criteria1.addOrder(Order.desc("entryTime"));
			
			System.out.println("before reporting manager id :"+reportingManagerId);
			
			criteria1.setMaxResults(max);
			criteria1.setFirstResult(min);
			messages = criteria1.list();
			
			Criteria criteriaCount1 = session.createCriteria(MessageDetails.class);
			criteriaCount1.add(Restrictions.eq("postedBy", "STUDENT"));
			criteriaCount1.add(Restrictions.eq("readByStudent", "Unread"));
			criteriaCount1.add(Restrictions.or(Restrictions.eq("postedTo", reportingManagerId), Restrictions.isNull("postedTo")));
			criteriaCount1.setProjection(Projections.rowCount());
			messageMap.put("totalmessage", criteriaCount1.uniqueResult());
			
			System.out.println("after reporting manager id :" +reportingManagerId);
			
			Loggers.loggerEnd(messageMap);
			
			Loggers.loggerEnd(messages);
			count = messages.size();
			System.out.println(" count ??????????????????? "+count);
			messageMap.put("unreadCount", count);
			messageMap.put("messages", messages);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		
		return messageMap;
	}

	@Override
	public Map<String, Object> studentView(Token token, Integer min, Integer max) throws Exception {
		Loggers.loggerStart();
		List<MessageDetails> msgs=null;
		List<MessageDetails> messages = null;
		Map<String, Object> msgMap = new HashMap<>();
		int count=0;
		
		try {
			Session session=this.sessionFactory.getCurrentSession();
       		String smartId = token.getSmartId();
			
			Criteria criteria = session.createCriteria(MessageDetails.class);
			criteria.add(Restrictions.eq("postedBy", "TEACHER"));
			criteria.add(Restrictions.eq("readByStudent", "Unread"));
			criteria.add(Restrictions.or(Restrictions.eq("postedTo", smartId), Restrictions.isNull("postedTo")));
			criteria.addOrder(Order.desc("entryTime"));
			
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			messages = criteria.list();
			Criteria criteriaCount = session.createCriteria(MessageDetails.class);
			criteriaCount.add(Restrictions.eq("postedBy", "TEACHER"));
			criteriaCount.add(Restrictions.eq("readByStudent", "Unread"));
			criteriaCount.setProjection(Projections.rowCount());
			criteriaCount.add(Restrictions.or(Restrictions.eq("postedTo", smartId), Restrictions.isNull("postedTo")));
			msgMap.put("totalmessage", criteriaCount.uniqueResult());

			Loggers.loggerEnd(criteria.list());
			
			Loggers.loggerEnd(messages);
			count=messages.size();
			System.out.println(" count ??????????????????? "+count);
			msgMap.put("unreadCount", count);
			msgMap.put("messages", messages);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		
		return msgMap;
	}
	
	@Override
	public List<MessageDetails> viewAllMessages() {
		Loggers.loggerStart();
		ArrayList<MessageDetails> messages = new ArrayList<>();
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("from MessageDetails ");
			messages = (ArrayList<MessageDetails>) query.list();
			Loggers.loggerEnd();
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

	@Override
	public List<MessageDetails> getList() throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<MessageDetails> list = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery("from MessageDetails");
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		Loggers.loggerEnd();
		return list;
	}
	
	@Override
	public Map<String, Object> teacherChat(MessageDetails details) throws Exception {
		Loggers.loggerStart();

		List<MessageDetails> messages = null;
		Map<String, Object> messageMap = new HashMap<>();
		Session session=this.sessionFactory.getCurrentSession();
		try {
			String smartId = details.getSmartId();
			String reportingManagerId = details.getReportingManagerId();
			System.out.println("before reporting manager id :"+reportingManagerId);

			query = session.createQuery(
					"from MessageDetails where smartId=:smartId or postedTo=:smartId ORDER BY entryTime ASC");
			query.setParameter("smartId", smartId);
			messages = (List<MessageDetails>) query.list();
			
			
			System.out.println("reporting manager id :"+reportingManagerId);
			
			Loggers.loggerEnd(messages);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		messageMap.put("messages", messages);
		return messageMap;
	}

	@Override
	public Map<String, Object> studentChat(MessageDetails details) throws Exception {
		Loggers.loggerStart();

		List<MessageDetails> messages = null;
		Map<String, Object> msgMap = new HashMap<>();
		
		Session session=this.sessionFactory.getCurrentSession();
		try {
			String smartId = details.getSmartId();
			String PostedTo = details.getPostedTo();
			String reportingManagerId = details.getReportingManagerId();
			System.out.println("before reporting manager id :"+reportingManagerId);
			System.out.println("posted to :"+PostedTo);
			System.out.println("smartId"+smartId);

			query = session.createQuery("from MessageDetails where (smartId=:smartId and reportingManagerId=:rId) or postedTo=:postedTo ORDER BY entryTime ASC");
			query.setParameter("postedTo", PostedTo);
			query.setParameter("smartId", PostedTo);
			query.setParameter("rId", smartId);
			messages = (List<MessageDetails>) query.list();
	
			Criteria criteriaCount = session.createCriteria(MessageDetails.class);
			criteriaCount.add(Restrictions.eq("postedTo", PostedTo));
			criteriaCount.setProjection(Projections.rowCount());
			criteriaCount.add(Restrictions.and(Restrictions.eq("smartId", PostedTo), Restrictions.eq("reportingManagerId", smartId)));
			msgMap.put("totalmessage", criteriaCount.uniqueResult());
	
			System.out.println("reporting manager id :"+reportingManagerId);

			Loggers.loggerEnd(messages);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		msgMap.put("messages", messages);
		return msgMap;
	}

	@Override
	public void updateStatus(Long hid, String smartId) throws Exception {
		try{
			query=sessionFactory.getCurrentSession().createQuery("update MessageDetails set readByStudent='Read' where hierarchy.hid=:hierarchy and postedTo=:smartId");
			query.setParameter("hierarchy", hid);
			query.setParameter("smartId", smartId);
			query.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}