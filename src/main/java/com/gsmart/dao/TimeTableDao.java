package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.CompoundTimeTable;
import com.gsmart.model.TimeTable;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;

public interface TimeTableDao {

	public List<TimeTable> teacherView(String day,String academicYear,Token token)throws GSmartDatabaseException;
	
	public List<TimeTable> studentView(String day,String academicYear,Token token, String standard, String section)throws GSmartDatabaseException;

	public CompoundTimeTable addTTable(TimeTable timeTable ,Token tokenObjt)throws GSmartDatabaseException;

	public List<TimeTable> hodViewForTeacher(String day, String academicYear, Token tokenObj, String smartId) throws GSmartDatabaseException;

	public TimeTable editTimeTable(TimeTable timeTable,Token tokenObjt) throws GSmartDatabaseException;

	public void deleteTimeTable(TimeTable timeTable)throws GSmartDatabaseException;

	public List<TimeTable> hodViewForStudent(String day, String academicYear, Token tokenObj, String standard,
			String section) throws GSmartDatabaseException;
}
