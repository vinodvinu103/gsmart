package com.gsmart.services;

import java.util.List;


import com.gsmart.model.Attenance;
import com.gsmart.model.CompoundAttenance;
import com.gsmart.util.GSmartServiceException;

public interface AttenanceServices {

	public void editAttenance(Attenance attenance)throws GSmartServiceException ;
	
	

	public void deleteAttenance(Attenance attenance)throws GSmartServiceException ;



public	List<Attenance> getAttenanceList() throws GSmartServiceException;



public CompoundAttenance addAttenace(Attenance attenance) throws GSmartServiceException;




	
		
	

}
