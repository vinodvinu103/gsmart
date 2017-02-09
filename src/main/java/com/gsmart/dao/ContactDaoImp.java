package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gsmart.model.MessageDetails;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@SuppressWarnings("unchecked")
@Repository
public class ContactDaoImp implements ContactDao
{
	@Autowired
	SessionFactory factory;
	Session session = null;
	Transaction transaction=null;
	Query query=null;
	
	
	@Override
	public boolean studentToTeacher(MessageDetails details) throws Exception 
	{
		Loggers.loggerStart();
		try {
			    getConnection();
				details.setEntryTime(CalendarCalculator.getTimeStamp());
				details.setPostedBy("Student");
				details.setReadByTeacher("N");
				session.save(details);
				transaction.commit();
				Loggers.loggerEnd();
				return true;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally 
		{
			session.close();
		}
	}
	
	
	@Override
	public List<MessageDetails> msgList(MessageDetails details) throws Exception 
	{
		try {
			getConnection();
			
			String reportingManagerId = details.getReportingManagerId();
			query = session.createQuery("from MessageDetails where readByTeacher='N' and reportingManagerId=:rId ORDER BY entryTime DESC" );	
			query.setParameter("rId", reportingManagerId);
			List<MessageDetails> messages = (List<MessageDetails>) query.list();
			return messages;
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
	}
	
	
	@Override
	public List<MessageDetails> teacherView(MessageDetails details) throws Exception
	{
		Loggers.loggerStart();
		List<MessageDetails> messages=null;
		try {
			getConnection();
			
			String reportingManagerId = details.getReportingManagerId();
			System.out.println("before reporting manager id :"+reportingManagerId);
			String smartId = details.getSmartId();
			/*Query query2 = session.createQuery(
					"update MessageDetails set readByTeacher='Y' where reportingManagerId=:rId and "
							+ "smartId=:sId and readByTeacher='N'");
			query2.setParameter("rId", reportingManagerId);
			query2.setParameter("sId", smartId);
			query2.executeUpdate();*/
			query = session.createQuery(
					"from MessageDetails where reportingManagerId=:sId and postedBy='Student' ORDER BY entryTime DESC");
			query.setParameter("sId", smartId);
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
		    finally
		    {
			session.close();
		    }
	}

	@Override
	public boolean teacherToStudent(MessageDetails details) throws Exception 
	{
		Loggers.loggerStart();
		try {
			getConnection();
			details.setEntryTime(CalendarCalculator.getTimeStamp());
			details.setPostedBy("Teacher");
			details.setReadByStudent("N");
			session.save(details);
			transaction.commit();
			Loggers.loggerEnd();
			return true;
		    } 
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally 
		{
			session.close();
		}
	}


	@Override
	public List<MessageDetails> studentView(MessageDetails details) throws Exception 
	{
		Loggers.loggerStart();
		List<MessageDetails> messages=null;
		try {
			getConnection();
			String reportingManagerId = details.getReportingManagerId();
			System.out.println("before reporting manager id :"+reportingManagerId);
//			String smartId = details.getSmartId();
			/*Query query2 = session.createQuery(
					"update MessageDetails set readByStudent='Y' where reportingManagerId=:rId and "
							+ "smartId=:sId and readByStudent='N'");
			query2.setParameter("rId", reportingManagerId);
			query2.setParameter("sId", smartId);
			query2.executeUpdate();*/
			
			query = session.createQuery(
					"from MessageDetails where smartId=:rId and postedBy='Teacher' ORDER BY entryTime DESC");
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
		finally
		{
			session.close();
		}
	}

	@Override
	public List<MessageDetails> viewAllMessages() 
	{
		Loggers.loggerStart();
		ArrayList<MessageDetails> messages=new ArrayList<>();
		try{
			getConnection();
			Query query=session.createQuery("from MessageDetails");
			messages=(ArrayList<MessageDetails>) query.list();
			Loggers.loggerEnd();
			return messages;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally {
			session.close();
		}
		
	}
	@Override
	public List<MessageDetails> getList() throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<MessageDetails> list=null;
		try {
			getConnection();
			query=session.createQuery("from MessageDetails");
			list=query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			session.close();
		}
		Loggers.loggerEnd();
		return list;
		
		
	}
	
	public void getConnection() {
		session = factory.openSession();
		transaction = session.beginTransaction();
	}
	
}
