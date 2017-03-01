package com.gsmart.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.gsmart.services.LoginServices;

public class CalendarCalculator implements Job {


	

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
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
				
				System.out.println("Hello Quartz!");	
				
			}

}