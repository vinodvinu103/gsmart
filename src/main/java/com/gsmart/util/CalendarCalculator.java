package com.gsmart.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;

import com.gsmart.services.LoginServices;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.springframework.stereotype.Repository;

@Repository
public class CalendarCalculator {
	
	@Autowired
	SessionFactory sessionFactroy;

	Session session = null;
	Query query;
	Transaction tranction = null;

	public void getConnection() {
		session = sessionFactroy.openSession();
		tranction = session.beginTransaction();
	}


	

	public static Long currentEpoch;
	@Autowired
	LoginServices loginservices;

	
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

	/*public void execute(JobExecutionContext context) throws JobExecutionException {
	public void execute(JsonSerializationContext context) throws JRubyExecutionException {
		System.out.println("Hello Quartz!");
	}*/
	
	public static Long getCurrentEpochTime() {
		return new Date().getTime()/1000;
	}


	private Long getUnixtime(String timestamp) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
		Long unixTime = null;
		try {
			unixTime = dateFormat.parse(timestamp).getTime()/1000;
		} catch (ParseException e) {
			Loggers.loggerStart("Parse exception while trying to parse date : " + timestamp);
			e.printStackTrace();
		}
		return unixTime;
	}

}