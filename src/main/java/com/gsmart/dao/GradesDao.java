package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.Grades;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;

public interface GradesDao {
	public List<Grades> getGradesList()throws GSmartServiceException;

	public void addGrades(Grades grades)throws GSmartDatabaseException;
    

	public void deleteGrades(Grades grades)throws GSmartDatabaseException;
	
	
	public void updateGrades(Grades grades)throws GSmartDatabaseException;

}
