package com.gsmart.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



import org.springframework.stereotype.Repository;

@Repository
public class CalendarCalculator {
	
public static Long currentEpoch;


	
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



}