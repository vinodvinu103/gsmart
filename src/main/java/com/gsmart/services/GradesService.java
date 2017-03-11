package com.gsmart.services;

import java.util.List;

import com.gsmart.model.Grades;
import com.gsmart.util.GSmartServiceException;

public interface GradesService {

	public List<Grades> getGradesList()throws GSmartServiceException;

	public boolean addGrades(Grades grades)throws GSmartServiceException;
	
	
	public  void deleteGrades(Grades grades)throws GSmartServiceException;
 

	public  boolean updateGrades(Grades grades)throws GSmartServiceException;


}
