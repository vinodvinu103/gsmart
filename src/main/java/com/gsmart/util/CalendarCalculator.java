package com.gsmart.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CalendarCalculator implements Job {

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
		System.out.println("Hello Quartz!");
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