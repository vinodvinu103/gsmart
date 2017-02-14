package com.gsmart.dao;

import java.util.List;
import java.util.Map;

import com.gsmart.model.Assign;
import com.gsmart.model.CompoundAssign;
import com.gsmart.model.Hierarchy;
import com.gsmart.util.GSmartDatabaseException;

public interface AssignDao {
	
public Map<String, Object> getAssignReportee(String role, Hierarchy hierarchy, Integer min, Integer max) throws GSmartDatabaseException;
	
	public CompoundAssign addAssigningReportee(Assign assign) throws GSmartDatabaseException;
	
	public void editAssigningReportee(Assign assign) throws GSmartDatabaseException;
	
	public void deleteAssigningReportee(Assign assign) throws GSmartDatabaseException;
	
	public Assign getStaffByClassAndSection(String cls, String section, Hierarchy hierarchy);
}
