package com.gsmart.services;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public List<TimeTable> studentView(String day, String academicYear, Token token) throws GSmartDatabaseException {
		Loggers.loggerStart(token);
		List<TimeTable> sutdentTTList=null;
		TimeTable tm=null;
		int per=0;
		Map<Integer, TimeTable> map=new HashMap<>();
		ArrayList<Integer> ped=new ArrayList<>();
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
			/*for (TimeTable timeTable : sutdentTTList) {
				
				for(String dayz:days){
					if(dayz.equalsIgnoreCase(timeTable.getDay())){
						
					}
				}
				System.out.println(" in side foreach condition1111 ");
				if(timeTable.getDay().equalsIgnoreCase("monday"))
				{
					per=timeTable.getPeriod();
				}
				ped.add(per);
				for (int j = 1; j <=7; j++) {
					for (Integer integer : ped) {
						if(integer!=j){
							tm=new TimeTable();
							tm.setDay("MONDAY");
							tm.setSubject("---");
							tm.setPeriod(j);
							System.out.println("addedddd");
						}
					}
				}
				System.out.println(" in side do-while-else condition 555 ");
				sutdentTTList.add(tm);
				System.out.println("112233 "+sutdentTTList);
			}*/
			System.out.println(studentTable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Loggers.loggerEnd("sizwe of list "+studentTable.size());
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
	
}

/*
System.out.println(" in side if condition 2222 "+day1);
int i=1;
do{  
	System.out.println(" in side do-while condition 333 " +i+" period "+timeTable.getPeriod());
	
	if(i==timeTable.getPeriod()){
		System.out.println(" in side do-while-if condition 444 "+i);
		i++;
	}else{
		System.out.println(" in side do-while-else condition 555 "+i);
		tm=new TimeTable();
		tm.setDay("MONDAY");
		tm.setSubject("---");
		tm.setPeriod(i);
		
		i++;
	}
	sutdentTTList.add(tm);
	System.out.println("112233 "+sutdentTTList);
}while(i<=7); */