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

import antlr.Token;

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
			details.setEntryTime(CalendarCalculator.getTimeStamp());
			
			details.setPostedBy(role);
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
	}

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
		} /*finally {
			session.close();
		}*/
	}

	@Override
	public Map<String, ArrayList<MessageDetails>> teacherView(MessageDetails details, Integer min, Integer max) throws Exception {
		Loggers.loggerStart();
		ArrayList<MessageDetails> messages = null;
		Map<String, ArrayList<MessageDetails>> messageMap = new HashMap<>();
		getConnection();
		try {
			
			String reportingManagerId = details.getReportingManagerId();
			System.out.println("before reporting manager id :"+reportingManagerId);

			query = session.createQuery(
					"from MessageDetails where smartId=:rId ORDER BY entryTime DESC");
			query.setParameter("rId", reportingManagerId);
//			messages = (List<MessageDetails>) query.list();
			
			Criteria criteria = session.createCriteria(MessageDetails.class);
			criteria.add(Restrictions.eq("smartId", "reportingManagerId"));
			criteria.addOrder(Order.desc("entryTime"));
			
			System.out.println("reporting manager id :"+reportingManagerId);
			
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			messages = (ArrayList<MessageDetails>) criteria.list();
			criteria.setProjection(Projections.rowCount());
			
			ArrayList<MessageDetails> count = (ArrayList<MessageDetails>) criteria.uniqueResult();
			messageMap.put("messages", count);

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
		messageMap.put("message", messages);
		return messageMap;
	}

	@Override
	public Map<String, ArrayList<MessageDetails>> studentView(MessageDetails details, Integer min, Integer max) throws Exception {
		Loggers.loggerStart();
		ArrayList<MessageDetails> messages = null;
		Map<String, ArrayList<MessageDetails>> msgMap = new HashMap<>();
		getConnection();
		try {
			
			String reportingManagerId = details.getReportingManagerId();
			System.out.println("before reporting manager id :"+reportingManagerId);

			query = session.createQuery(
					"from MessageDetails where smartId=:rId ORDER BY entryTime DESC");
			query.setParameter("rId", reportingManagerId);
//			messages = (List<MessageDetails>) query.list();
			
			Criteria criteria = session.createCriteria(MessageDetails.class);
			criteria.add(Restrictions.eq("smartId", "reportingManagerId"));
			criteria.addOrder(Order.desc("entryTime"));
			
			System.out.println("reporting manager id :"+reportingManagerId);
			
			criteria.setMaxResults(max);
			criteria.setFirstResult(min);
			messages = (ArrayList<MessageDetails>) criteria.list();
			criteria.setProjection(Projections.rowCount());
			
			ArrayList<MessageDetails> count = (ArrayList<MessageDetails>) criteria.uniqueResult();
			msgMap.put("messages", count);

			/*transaction.commit();*/
			Loggers.loggerEnd(msgMap);
			
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
		msgMap.put("message", messages);
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
	public boolean studentToTeacher(MessageDetails details) throws Exception {
		Loggers.loggerStart();
		try {
			
			getConnection();
			details.setEntryTime(CalendarCalculator.getTimeStamp());
			
			String role = null;
			details.setPostedBy(role);
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
	}

	@Override
	public boolean teacherToStudent(MessageDetails details) throws Exception {
		Loggers.loggerStart();
		try {
			getConnection();
			details.setEntryTime(CalendarCalculator.getTimeStamp());
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
	}
	public void getConnection() {
		session = factory.openSession();
		transaction = session.beginTransaction();
	}

}
