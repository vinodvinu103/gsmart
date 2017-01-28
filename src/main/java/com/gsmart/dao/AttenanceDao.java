package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.Attenance;
import com.gsmart.model.CompoundAttenance;
import com.gsmart.util.GSmartDatabaseException;

public  interface AttenanceDao {

public	void editAttenance(Attenance attenance) throws GSmartDatabaseException;

public void deleteAttenance(Attenance attenance) throws GSmartDatabaseException;

public List<Attenance> getAttenanceList() throws GSmartDatabaseException;

public CompoundAttenance addAttenance(Attenance attenance) throws GSmartDatabaseException;

	
	

}
