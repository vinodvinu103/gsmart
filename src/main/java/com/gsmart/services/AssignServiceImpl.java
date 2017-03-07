package com.gsmart.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.AssignDao;
import com.gsmart.model.Assign;
import com.gsmart.model.CompoundAssign;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;


@Service
public class AssignServiceImpl implements AssignService{
	
	
	@Autowired
	AssignDao assignDao;
	
	

	@Override
	public Map<String, Object> getAssignReportee(Long hid, Integer min, Integer max) throws GSmartServiceException {
		return assignDao.getAssignReportee(hid, min, max);
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
	public Assign editAssigningReportee(Assign assign) throws GSmartServiceException {
		Loggers.loggerStart();
		Assign asn = null;
		try{
		asn = assignDao.editAssigningReportee(assign);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return asn;		
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

	@Override
	public Assign getStaffByClassAndSection(String standard, String section, Hierarchy hierarchy) {
		return assignDao.getStaffByClassAndSection(standard, section, hierarchy);
	}
	
	@Override
	public boolean searchStandardFeeService(String standard,Long hid){
		Loggers.loggerStart();
		boolean status=false;
		try{
		status= assignDao.searchStandardFeeDao(standard,hid);
		}catch(Exception e){
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return status;
		}

	@Override
	public List<Assign> getAssignList(Long hid) throws GSmartServiceException {
		Loggers.loggerStart();
		List<Assign> assignList=null;
		try {
			assignList=assignDao.getAssignList(hid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return assignList;
	}
	
	
	
}
