package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.GradesDao;
import com.gsmart.model.Band;
import com.gsmart.model.Grades;

import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;
  

@Service
public class GradesServiceImpl implements GradesService {
	
	@Autowired
	GradesDao gradesDao;
	
	   @Override
	   public List<Grades> getGradesList()throws GSmartServiceException{
		Loggers.loggerStart();
		List<Grades> list=null;
		try {
			return gradesDao.getGradesList();
		}catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
	}//end of get method public List<Grades> getGradesList()throws GSmartServiceException;
	   @Override
	   public boolean addGrades(Grades grades) throws GSmartServiceException {
		   Loggers.loggerStart();
		   
			try{
				return gradesDao.addGrades(grades);
			}catch (GSmartDatabaseException exception) {
				throw (GSmartServiceException) exception;
			} catch (Exception e) {
				throw new GSmartServiceException(e.getMessage());
			}
			
			
		}//end of add
	   @Override
	   public boolean updateGrades(Grades grades) throws GSmartServiceException {
			Loggers.loggerStart();
		
			try{
				return gradesDao.updateGrades(grades);
			}catch (GSmartDatabaseException exception) {
				throw (GSmartServiceException) exception;
			} catch (Exception e) {
				throw new GSmartServiceException(e.getMessage());
			}
			
			
		}//end of update
	   @Override
	   public void deleteGrades(Grades grades) throws GSmartServiceException {
		   Loggers.loggerStart();
			try{
				gradesDao.deleteGrades(grades);
			}catch (GSmartDatabaseException exception) {
				throw (GSmartServiceException) exception;
			} catch (Exception e) {
				throw new GSmartServiceException(e.getMessage());
			}
			Loggers.loggerEnd();
			
		}//end of delete
	   

	   

}//end of class
