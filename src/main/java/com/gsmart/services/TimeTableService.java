package com.gsmart.services;

import java.util.List;

import com.gsmart.model.Profile;
import com.gsmart.model.TimeTable;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;

public interface TimeTableService {
	
	public List<TimeTable> studentView(String day,String academicYear,Token token) throws GSmartDatabaseException;

	public List<Profile> getChildTeacher(TimeTable timeTable, Token tokenObj)throws GSmartServiceException;
}
