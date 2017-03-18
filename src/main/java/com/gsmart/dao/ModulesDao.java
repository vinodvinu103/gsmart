package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.CompoundModules;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Modules;
import com.gsmart.util.GSmartDatabaseException;

public interface ModulesDao {
	public List<Modules> getModulesList(String role,Hierarchy hierarchy) throws GSmartDatabaseException;
	public CompoundModules addModule(Modules modules) throws GSmartDatabaseException;
	public Modules editModule(Modules modules)throws GSmartDatabaseException;
	public void deleteModule(Modules modules)throws GSmartDatabaseException;

}
