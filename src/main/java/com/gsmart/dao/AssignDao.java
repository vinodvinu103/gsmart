package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.Assign;
import com.gsmart.model.CompoundAssign;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.util.GSmartDatabaseException;

public interface AssignDao {
	
public List<Assign> getAssignReportee(String role, Hierarchy hierarchy) throws GSmartDatabaseException;
	
	public CompoundAssign addAssigningReportee(Assign assign) throws GSmartDatabaseException;
	
	public void editAssigningReportee(Assign assign) throws GSmartDatabaseException;
	
	public void deleteAssigningReportee(Assign assign) throws GSmartDatabaseException;
	
	public Assign getStaffByClassAndSection(String cls, String section, Hierarchy hierarchy);

	public void editAssigningTeacher(Assign assign ,Hierarchy hierarchy) throws GSmartDatabaseException;

	
}
