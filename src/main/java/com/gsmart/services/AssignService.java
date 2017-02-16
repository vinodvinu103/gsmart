package com.gsmart.services;

import java.util.List;

import com.gsmart.model.Assign;
import com.gsmart.model.CompoundAssign;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartServiceException;

public interface AssignService {
	
	public List<Assign> getAssignReportee(String role, Hierarchy hierarchy) throws GSmartServiceException;
	
	public CompoundAssign addAssigningReportee(Assign assign) throws GSmartServiceException;
	
	public Assign editAssigningReportee(Assign assign) throws GSmartServiceException;
	
	public void deleteAssigningReportee(Assign assign) throws GSmartServiceException;
	
	public Assign getStaffByClassAndSection(String cls, String section, Hierarchy hierarchy);

}
