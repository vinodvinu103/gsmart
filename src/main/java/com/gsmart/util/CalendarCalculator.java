package com.gsmart.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import com.gsmart.services.LoginServices;

@Repository
public class CalendarCalculator implements Job {
	
	@Autowired
	SessionFactory sessionFactroy;

	Session session = null;
	Query query;
	Transaction tranction = null;

	public void getConnection() {
		session = sessionFactroy.openSession();
		tranction = session.beginTransaction();
	}

	public static String getTimeStamp() {
		try {
			Date a = new Date();
			Calendar cc = Calendar.getInstance();
			cc.setTime(a);
			SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
			s.setCalendar(cc);
			return s.format(a);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Hello user u can log in now ..........!");
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/spring-config.xml");
		LoginServices service = applicationContext.getBean(LoginServices.class);
		service.unlockAccounts();

	}

}