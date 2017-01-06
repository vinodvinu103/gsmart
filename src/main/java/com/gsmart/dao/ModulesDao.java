package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.CompoundModules;
import com.gsmart.model.Modules;
import com.gsmart.util.GSmartDatabaseException;

public interface ModulesDao {
	public List<Modules> getModulesList() throws GSmartDatabaseException;
	public CompoundModules addModule(Modules modules) throws GSmartDatabaseException;

}
