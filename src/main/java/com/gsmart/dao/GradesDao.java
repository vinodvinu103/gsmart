package com.gsmart.dao;

import java.util.List;


import com.gsmart.model.Grades;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;

public interface GradesDao {
	public List<Grades> getGradesList(Long hid)throws GSmartServiceException;

	public boolean addGrades(Grades grades)throws GSmartDatabaseException;
    

	public void deleteGrades(Grades grades)throws GSmartDatabaseException;
	
	
	public boolean updateGrades(Grades grades)throws GSmartDatabaseException;

	
}
