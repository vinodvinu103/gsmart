package com.gsmart.services;

import java.util.List;

import com.gsmart.model.CompoundModules;
import com.gsmart.model.Modules;
import com.gsmart.util.GSmartServiceException;

public interface ModulesServices {
	public List<Modules> getModulesList() throws GSmartServiceException;
	public CompoundModules addModules(Modules modules) throws GSmartServiceException;
}
