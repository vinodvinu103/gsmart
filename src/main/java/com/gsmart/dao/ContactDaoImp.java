package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.MessageDetails;
import com.gsmart.util.CalendarCalculator;
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
	public boolean studentToTeacher(MessageDetails details) throws Exception {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		try {
			details.setEntryTime(CalendarCalculator.getTimeStamp());

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
		try {

			String reportingManagerId = details.getReportingManagerId();
			query = sessionFactory.getCurrentSession().createQuery(
					"from MessageDetails where readByTeacher='Y' and reportingManagerId=:rId ORDER BY entryTime DESC");
			query.setParameter("rId", reportingManagerId);

			List<MessageDetails> messages = (List<MessageDetails>) query.list();
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

	@Override
	public List<MessageDetails> teacherView(MessageDetails details) throws Exception {
		Loggers.loggerStart();
		List<MessageDetails> messages = new ArrayList<>();
		try {
			
			String reportingManagerId = details.getReportingManagerId();
			System.out.println("before reporting manager id :"+reportingManagerId);
			String smartId = details.getSmartId();
			
			query = sessionFactory.getCurrentSession().createQuery(
					"from MessageDetails where reportingManagerId=:smartId ORDER BY entryTime DESC");
			query.setParameter("smartId", smartId);
		/*	query=session.createQuery(
					"from MessageDetails");
			*/
			messages = (List<MessageDetails>) query.list();
			System.out.println("reporting manager id :"+reportingManagerId);
			
		/*	transaction.commit();*/
			Loggers.loggerEnd(messages);
			return messages;
		    } 
		    catch (Exception e) 
		    {
			e.printStackTrace();
			return null;
		    }
		    
	
	}

	@Override
	public List<MessageDetails> studentView(MessageDetails details) throws Exception {
		Loggers.loggerStart();
		List<MessageDetails> messages = new ArrayList<>();
		try {
			
			String reportingManagerId = details.getReportingManagerId();
			System.out.println("before reporting manager id :"+reportingManagerId);

			query = sessionFactory.getCurrentSession().createQuery(
					"from MessageDetails where smartId=:rId ORDER BY entryTime DESC");
			query.setParameter("rId", reportingManagerId);
			messages = (List<MessageDetails>) query.list();
			
		System.out.println("reporting manager id :"+reportingManagerId);

			/*transaction.commit();*/
			Loggers.loggerEnd(messages);
			return messages;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		
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
	public boolean studentToTeacher(MessageDetails details, String role) throws Exception {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		try {
			
			details.setEntryTime(CalendarCalculator.getTimeStamp());
			
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
	public boolean teacherToStudent(MessageDetails details) throws Exception {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		try {
			details.setEntryTime(CalendarCalculator.getTimeStamp());
			details.setReadByTeacher("Y");
			session.save(details);
			Loggers.loggerEnd();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
	/*public void getConnection() {
		session = factory.openSession();
		transaction = session.beginTransaction();
	}*/

}
