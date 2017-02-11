package com.gsmart.util;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class CronJob {
	
	static CronJob cronJob = null;
	
	public void cronJob() throws Exception {
		// Quartz 1.6.3
		// JobDetail job = new JobDetail();
		// job.setName("dummyJobName");
		// job.setJobClass(HelloJob.class);
		Loggers.loggerStart("started////////");
		JobDetail job = JobBuilder.newJob(CalendarCalculator.class).withIdentity("dummyJobName", "group1").build();

		// Quartz 1.6.3
		// CronTrigger trigger = new CronTrigger();
		// trigger.setName("dummyTriggerName");
		// trigger.setCronExpression("0/5 * * * * ?");
		Loggers.loggerEnd("//////////");
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("dummyTriggerName", "group1")
				.withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?")).build();

		// schedule it
		Loggers.loggerStart("scheduler");
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		scheduler.scheduleJob(job, trigger);
		Loggers.loggerEnd("end");

	}
	
	public static void startCronJob() {
		if(cronJob == null) {
			Loggers.loggerStart("cron is null for the first time and trying to create and start cron");
			cronJob = new CronJob();
			try {
				Loggers.loggerStart("created a cron object and trying to start");
				cronJob.cronJob();
				Loggers.loggerStart("cron is created and started");
			} catch (Exception e) {
				Loggers.loggerStart("exception while starting cron job");
				e.printStackTrace();
			}
		}
	}
}