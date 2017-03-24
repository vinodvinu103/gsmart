package com.gsmart.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gsmart.model.MessageDetails;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;


@SuppressWarnings("unchecked")
@Repository
public class ContactDaoImp implements ContactDao {
	@Autowired
	SessionFactory factory;
	Session session = null;
	Transaction transaction = null;
	Query query = null;

	@Override
	public boolean studentToTeacher(MessageDetails details, String role) throws Exception {
		Loggers.loggerStart();
		try {
			getConnection();
			details.setEntryTime(CalendarCalculator.getTimeStamp())
			;
			
			details.setPostedBy(role);
			details.setReadByTeacher("Y");
			details.setPostedTo(details.getPostedTo());
			session.save(details);
		//	session.update(details);
			transaction.commit();
			Loggers.loggerEnd();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
	}
	
	@Override
	public boolean teacherToStudent(MessageDetails details, String role) throws Exception {
		Loggers.loggerStart(details);
		try {
			getConnection();
			details.setEntryTime(CalendarCalculator.getTimeStamp());
			details.setPostedTo(details.getPostedTo());
			details.setPostedBy(role);
			details.setReadByTeacher("Y");
			session.save(details);
		//	session.update(details);
			transaction.commit();
			Loggers.loggerEnd();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
	}
	
	/*@Override
	public boolean teacherToStudent(MessageDetails details) {

		Loggers.loggerStart();
		try {
			getConnection();
			details.setEntryTime(CalendarCalculator.getTimeStamp());
			
//			details.setPostedBy(role);
			details.setReadByTeacher("Y");
			session.save(details);
			transaction.commit();
			Loggers.loggerEnd();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
	}*/

	@Override
	public List<MessageDetails> msgList(MessageDetails details) throws Exception {
		try {
			getConnection();

			String reportingManagerId = details.getReportingManagerId();
			query = session.createQuery(
					"from MessageDetails where readByTeacher='Y' and reportingManagerId=:rId ORDER BY entryTime DESC");
			query.setParameter("rId", reportingManagerId);

			List<MessageDetails> messages = (List<MessageDetails>) query.list();
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public Map<String, Object> teacherView(MessageDetails details, Integer min, Integer max) throws Exception {
		Loggers.loggerStart();
		List<MessageDetails> messages = null;
		Map<String, Object> messageMap = new HashMap<>();
		getConnection();
		try {
			
			String reportingManagerId = details.getReportingManagerId();
			System.out.println("before reporting manager id :"+reportingManagerId);

//			query = session.createQuery(
//					"from MessageDetails where smartId=:rId ORDER BY entryTime DESC");
//			query.setParameter("rId", reportingManagerId);
//			messages = (List<MessageDetails>) query.list();
			
			Criteria criteria1 = session.createCriteria(MessageDetails.class);
			criteria1.add(Restrictions.eq("postedBy", "STUDENT"));
			criteria1.addOrder(Order.desc("entryTime"));
			
			System.out.println("reporting manager id :"+reportingManagerId);
			
//			criteria1.setProjection(Projections.projectionList().add(Projections.groupProperty("entryTime"),"entryTime").add(Projections.rowCount(),"max")).addOrder(Order.desc("entryTime"));
//			criteria1.setProjection(Projections.rowCount());
			criteria1.setMaxResults(max);
			criteria1.setFirstResult(min);
			messages = criteria1.list();
			Criteria criteriaCount1 = session.createCriteria(MessageDetails.class);
			criteriaCount1.add(Restrictions.eq("postedBy", "STUDENT"));
			criteriaCount1.setProjection(Projections.rowCount());
//			Long count = criteriaCount.uniqueResult();
			messageMap.put("totalmessage", criteriaCount1.uniqueResult());
			
//			Long count = (Long) criteria1.uniqueResult();
//			messageMap.put("messages", count);

			/*transaction.commit();*/
			Loggers.loggerEnd(messageMap);
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			session.close();
		}
		messageMap.put("messages", messages);
		return messageMap;
	}

	@Override
	public Map<String, Object> studentView(MessageDetails details, Integer min, Integer max) throws Exception {
		Loggers.loggerStart();
		List<MessageDetails> messages = null;
		Map<String, Object> msgMap = new HashMap<>();
		getConnection();
		try {
			
			String reportingManagerId = details.getReportingManagerId();
       		String smartId = details.getSmartId();
			System.out.println("before reporting manager id :"+reportingManagerId);

//			query = session.createQuery(
//					"from MessageDetails where smartId=:rId ORDER BY entryTime DESC");
//			query.setParameter("rId", reportingManagerId);
//			messages = (List<MessageDetails>) query.list();
			
			Criteria criteria = session.createCriteria(MessageDetails.class);
			criteria.add(Restrictions.eq("postedBy", "TEACHER"));
			criteria.add(Restrictions.or(Restrictions.eq("postedTo", smartId), Restrictions.isNull("postedTo")));
			criteria.addOrder(Order.desc("entryTime"));
			
			System.out.println("reporting manager id :"+reportingManagerId);
			
//			criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("entryTime"),"entryTime").add(Projections.rowCount(),"max")).addOrder(Order.desc("entryTime"));
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			messages = criteria.list();
			Criteria criteriaCount = session.createCriteria(MessageDetails.class);
			criteriaCount.add(Restrictions.eq("postedBy", "TEACHER"));
			criteriaCount.setProjection(Projections.rowCount());
			criteriaCount.add(Restrictions.or(Restrictions.eq("postedTo", smartId), Restrictions.isNull("postedTo")));
//			Long count = criteriaCount.uniqueResult();
			msgMap.put("totalmessage", criteriaCount.uniqueResult());

			/*transaction.commit();*/
			Loggers.loggerEnd(criteria.list());
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			session.close();
		}
		msgMap.put("messages", messages);
		return msgMap;
	}

	@Override
	public List<MessageDetails> viewAllMessages() {
		Loggers.loggerStart();
		ArrayList<MessageDetails> messages = new ArrayList<>();
		try {
			getConnection();
			Query query = session.createQuery("from MessageDetails ");
			messages = (ArrayList<MessageDetails>) query.list();
			Loggers.loggerEnd();
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}

	}

	@Override
	public List<MessageDetails> getList() throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<MessageDetails> list = null;
		try {
			getConnection();
			query = session.createQuery("from MessageDetails");
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		return list;

	}
	
	@Override
	public Map<String, Object> teacherChat(MessageDetails details) throws Exception {
		Loggers.loggerStart();
		List<MessageDetails> messages = null;
		Map<String, Object> messageMap = new HashMap<>();
		getConnection();
		try {
			String smartId = details.getSmartId();
			String reportingManagerId = details.getReportingManagerId();
			System.out.println("before reporting manager id :"+reportingManagerId);

			query = session.createQuery(
					"from MessageDetails where smartId=:smartId or postedTo=:smartId ORDER BY entryTime ASC");
			query.setParameter("smartId", smartId);
	//		query.setParameter("rId", reportingManagerId);
			messages = (List<MessageDetails>) query.list();
			
			/*Criteria criteria1 = session.createCriteria(MessageDetails.class);
			criteria1.add(Restrictions.eq("postedBy", "STUDENT"));
			criteria1.addOrder(Order.desc("entryTime"));*/
			
			System.out.println("reporting manager id :"+reportingManagerId);
			
//			criteria1.setProjection(Projections.projectionList().add(Projections.groupProperty("entryTime"),"entryTime").add(Projections.rowCount(),"max")).addOrder(Order.desc("entryTime"));
//			criteria1.setProjection(Projections.rowCount());
			/*criteria1.setMaxResults(max);
			criteria1.setFirstResult(min);
			messages = criteria1.list();
			Criteria criteriaCount1 = session.createCriteria(MessageDetails.class);
			criteriaCount1.add(Restrictions.eq("postedBy", "STUDENT"));
			criteriaCount1.setProjection(Projections.rowCount());*/
//			Long count = criteriaCount.uniqueResult();
//			messageMap.put("totalmessage", criteriaCount1.uniqueResult());
			
//			Long count = (Long) criteria1.uniqueResult();
//			messageMap.put("messages", count);

			/*transaction.commit();*/
			Loggers.loggerEnd(messages);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			session.close();
		}
		messageMap.put("messages", messages);
		return messageMap;
	}

	@Override
	public Map<String, Object> studentChat(MessageDetails details) throws Exception {
		Loggers.loggerStart();
		List<MessageDetails> messages = null;
		Map<String, Object> msgMap = new HashMap<>();
		getConnection();
		try {
			String smartId = details.getSmartId();
			String PostedTo = details.getPostedTo();
			String reportingManagerId = details.getReportingManagerId();
	//		String studentName = details.getStudentName();
			System.out.println("before reporting manager id :"+reportingManagerId);
			System.out.println("posted to :"+PostedTo);
			System.out.println("smartId"+smartId);

			query = session.createQuery("from MessageDetails where (smartId=:smartId and reportingManagerId=:rId) or postedTo=:postedTo ORDER BY entryTime DESC");
			query.setParameter("postedTo", PostedTo);
			query.setParameter("smartId", PostedTo);
			query.setParameter("rId", smartId);
			messages = (List<MessageDetails>) query.list();
	
			/*Criteria criteria = session.createCriteria(MessageDetails.class);
			criteria.add(Restrictions.eq("postedTo", PostedTo));
			criteria.add(Restrictions.and(Restrictions.eq("smartId", smartId), Restrictions.eq("reportingManagerId", reportingManagerId)));
			criteria.addOrder(Order.desc("entryTime"));
	
			System.out.println("reporting manager id :"+reportingManagerId);*/
	
//			criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("entryTime"),"entryTime").add(Projections.rowCount(),"max")).addOrder(Order.desc("entryTime"));
	
//			messages = criteria.list();
			Criteria criteriaCount = session.createCriteria(MessageDetails.class);
			criteriaCount.add(Restrictions.eq("postedTo", PostedTo));
			criteriaCount.setProjection(Projections.rowCount());
			criteriaCount.add(Restrictions.and(Restrictions.eq("smartId", PostedTo), Restrictions.eq("reportingManagerId", smartId)));
//			Long count = criteriaCount.uniqueResult();
			msgMap.put("totalmessage", criteriaCount.uniqueResult());
	
			System.out.println("reporting manager id :"+reportingManagerId);

			/*transaction.commit();*/
			Loggers.loggerEnd(messages);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			session.close();
		}
		msgMap.put("messages", messages);
		return msgMap;
	}
	
	public void getConnection() {
		session = factory.openSession();
		transaction = session.beginTransaction();
	}

}
