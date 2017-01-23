package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.AssignDao;
import com.gsmart.model.Assign;
import com.gsmart.model.CompoundAssign;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;


@Service
public class AssignServiceImpl implements AssignService{
	
	
	@Autowired
	AssignDao assignDao;
	
	

	@Override
	public List<Assign> getAssignReportee() throws GSmartServiceException {
		return assignDao.getAssignReportee();
	}

	@Override
	public CompoundAssign addAssigningReportee(Assign assign) throws GSmartServiceException {
		Loggers.loggerStart();
		CompoundAssign compoundAssign=null;
		try{
		compoundAssign=assignDao.addAssigningReportee(assign);
		}
		catch (Exception e) {

		e.printStackTrace();
		}
		Loggers.loggerEnd();
		return compoundAssign;

		
	}

	@Override
	public void editAssigningReportee(Assign assign) throws GSmartServiceException {
		Loggers.loggerStart();
		try{
		assignDao.editAssigningReportee(assign);
		}
		catch (Exception e) {

			e.printStackTrace();
		}
		
	}

	@Override
	public void deleteAssigningReportee(Assign assign) throws GSmartServiceException {

		Loggers.loggerStart();
		try{
		assignDao.deleteAssigningReportee(assign);
	}
	catch (Exception e) {

		e.printStackTrace();
	}
	}

	

}
