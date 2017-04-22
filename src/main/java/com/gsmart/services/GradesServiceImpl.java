package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.GradesDao;
import com.gsmart.model.Grades;

import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;
  

@Service
@Transactional
public class GradesServiceImpl implements GradesService {
	
	@Autowired
	private GradesDao gradesDao;
	
	   @Override
	   public List<Grades> getGradesList(Long hid)throws GSmartServiceException{
		Loggers.loggerStart();
		try {
			return gradesDao.getGradesList(hid);
		}catch (Exception e) {
			e.printStackTrace();
			throw new GSmartServiceException(e.getMessage());
		}
	}//end of get method public List<Grades> getGradesList()throws GSmartServiceException;
	   @SuppressWarnings("null")
	@Override
	   public boolean addGrades(Grades grades) throws GSmartServiceException {
		   Loggers.loggerStart();
		   
			try{
				List<Grades> list = null;
				
				list= gradesDao.getGradesList(grades.getHierarchy().getHid()); 
				
				for (Grades grades2 : list) {
				 
					if( (grades2.getEndPercentage()>=grades.getEndPercentage() || grades2.getStartPercentage()<=grades.getEndPercentage())
							&&(grades2.getStartPercentage()<=grades.getEndPercentage()||grades2.getStartPercentage()<=grades.getStartPercentage()))
					{
						
						System.out.println(" inn side if condtion <<<<<<<<<>>>>>>>");
						return false;
				    }
					else
				    {
						System.out.println(" in side >M><>>?<>><>>>>");
						return gradesDao.addGrades(grades);
				    }
					
				}//foreach 
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
