package com.gsmart.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

public class CalendarCalculator {
	
	public static Long currentEpoch = new Date().getTime()/1000;

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

}