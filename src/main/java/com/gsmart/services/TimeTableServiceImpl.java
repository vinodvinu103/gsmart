package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.ProfileDao;
import com.gsmart.dao.TimeTableDao;
import com.gsmart.model.Profile;
import com.gsmart.model.TimeTable;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;
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
		Profile profile=profileDao.getProfileDetails(token.getSmartId());
		String standard=profile.getStandard();
		String section=profile.getSection();
		sutdentTTList=timetableDao.studentView(day, academicYear, token,standard,section);
		Loggers.loggerEnd();
		return sutdentTTList;
	}
	
}
