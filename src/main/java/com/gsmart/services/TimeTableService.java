package com.gsmart.services;

import java.util.List;

import com.gsmart.model.TimeTable;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;

public interface TimeTableService {
	
	public List<TimeTable> studentView(String day,String academicYear,Token token) throws GSmartDatabaseException;
}
