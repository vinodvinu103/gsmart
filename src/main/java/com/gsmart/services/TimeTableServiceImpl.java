package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.ProfileDao;
import com.gsmart.dao.TimeTableDao;
import com.gsmart.model.Profile;
import com.gsmart.model.TimeTable;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class TimeTableServiceImpl implements TimeTableService {
	
	@Autowired
	ProfileDao profileDao;
	
	@Autowired
	TimeTableDao timetableDao;

	@Override
	public List<TimeTable> studentView(String academicYear, Token token) throws GSmartDatabaseException {
		Loggers.loggerStart(token);
		List<TimeTable> sutdentTTList=null;
		TimeTable tm=null;
		Profile profile=profileDao.getProfileDetails(token.getSmartId());
		String standard=profile.getStandard();
		String section=profile.getSection();
		List<String> days=new ArrayList<>();
		
		List<Integer> numOfPeriods= new ArrayList<>();
		numOfPeriods.add(1);
		numOfPeriods.add(2);
		numOfPeriods.add(3);
		numOfPeriods.add(4);
		numOfPeriods.add(5);
		numOfPeriods.add(6);
		numOfPeriods.add(7);
		List<TimeTable> studentTable=new ArrayList<>();
		
		days.add("MONDAY");
		days.add("TUESDAY");
		days.add("WEDNESDAY");
		days.add("THURSDAY");
		days.add("FRIDAY");
		days.add("SATURDAY");
//		sutdentTTList=timetableDao.studentView(day, academicYear, token,standard,section);
		try {
			
			for (String dayz : days) {
				List<Integer> periods=new ArrayList<>();
				sutdentTTList=timetableDao.studentView(dayz, academicYear, token,standard,section);
				for(TimeTable tt:sutdentTTList){
					
					periods.add(tt.getPeriod());
					studentTable.add(tt);
					
				}
				for(Integer singlePeriod:numOfPeriods){
					if(!periods.contains(singlePeriod)){
						tm=new TimeTable();
						tm.setDay(dayz);
						tm.setStandard(standard);
						tm.setSection(section);
						tm.setAcademicYear(academicYear);
						tm.setSubject("FREE");
						tm.setPeriod(singlePeriod);
						studentTable.add(tm);
						
					}
				}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Loggers.loggerEnd();
		return studentTable;
	}
	
	@Override
	public List<Profile> getChildTeacher(TimeTable timeTable, Token tokenObj) throws GSmartServiceException {
		Loggers.loggerStart();
		List<Profile> list=null;
		List<TimeTable> listTT=null;
		list=profileDao.getReportingProfiles(tokenObj.getSmartId());
		listTT=timetableDao.getChildTeacher(timeTable,tokenObj);
		try {
			if (listTT==null) {
				return list;
			}
			else {
				for (int i = 0; i < listTT.size(); i++) {
					
					for (Profile profile : list) {
						System.out.println("profile.getSmartId() :"+profile.getSmartId()+"==listTT.get(i).getSmartId() :"+listTT.get(i).getSmartId());
						if(profile.getSmartId().equals(listTT.get(i).getSmartId())){
							list.remove(profile);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(list);
		return list;
	}
	
	@Override
	public List<TimeTable> teacherView(String academicYear, Token token) throws GSmartServiceException {
		Loggers.loggerStart(token);
		List<TimeTable> sutdentTTList=null;
		TimeTable tm=null;
		Profile profile=profileDao.getProfileDetails(token.getSmartId());
		String standard=profile.getStandard();
		String section=profile.getSection();
		List<String> days=new ArrayList<>();
		
		List<Integer> numOfPeriods= new ArrayList<>();
		numOfPeriods.add(1);
		numOfPeriods.add(2);
		numOfPeriods.add(3);
		numOfPeriods.add(4);
		numOfPeriods.add(5);
		numOfPeriods.add(6);
		numOfPeriods.add(7);
		List<TimeTable> studentTable=new ArrayList<>();
		
		days.add("MONDAY");
		days.add("TUESDAY");
		days.add("WEDNESDAY");
		days.add("THURSDAY");
		days.add("FRIDAY");
		days.add("SATURDAY");
//		sutdentTTList=timetableDao.studentView(day, academicYear, token,standard,section);
		try {
			
			for (String dayz : days) {
				List<Integer> periods=new ArrayList<>();
				sutdentTTList=timetableDao.teacherView(dayz, academicYear, token);
				for(TimeTable tt:sutdentTTList){
					
					periods.add(tt.getPeriod());
					studentTable.add(tt);
					
				}
				for(Integer singlePeriod:numOfPeriods){
					if(!periods.contains(singlePeriod)){
						tm=new TimeTable();
						tm.setDay(dayz);
						tm.setStandard(standard);
						tm.setSection(section);
						tm.setAcademicYear(academicYear);
						tm.setSubject("FREE");
						tm.setPeriod(singlePeriod);
						studentTable.add(tm);
						
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Loggers.loggerEnd("size of list "+studentTable.size());
		return studentTable;
	}
	
}
