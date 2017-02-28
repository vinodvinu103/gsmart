package com.gsmart.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.LeaveMasterDao;
import com.gsmart.model.CompoundLeaveMaster;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.LeaveMaster;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class LeaveMasterServiceImpl implements LeaveMasterService {
	@Autowired
	private LeaveMasterDao leavemasterdao;

	@Override
	public List<LeaveMaster> getLeaveMasterList(String role,Hierarchy hierarchy) throws GSmartServiceException {

		try {
			return leavemasterdao.getLeaveMasterList(role,hierarchy);

		} catch (GSmartDatabaseException Exception) {
			throw (GSmartServiceException) Exception;

		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
	}

	@Override
	public CompoundLeaveMaster addLeaveMaster(LeaveMaster leaveMaster) throws GSmartServiceException {
		Loggers.loggerStart();
		CompoundLeaveMaster cb;
		try {
			cb = leavemasterdao.addLeaveMaster(leaveMaster);

		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		Loggers.loggerEnd();
		return cb;
	}

	public void deleteLeaveMaster(LeaveMaster leaveMaster) throws GSmartServiceException {

		Loggers.loggerStart();
		try {
			leavemasterdao.deleteLeaveMaster(leaveMaster);

		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}

		Loggers.loggerEnd();

	}

	@Override
	public LeaveMaster editLeaveMaster(LeaveMaster leaveMaster) throws GSmartServiceException {
		Loggers.loggerStart();
		LeaveMaster ch=null;
		try {
			ch=leavemasterdao.editLeaveMaster(leaveMaster);

		} catch (GSmartDatabaseException exception) {
			throw (GSmartServiceException) exception;
		} catch (Exception e) {

			throw new GSmartServiceException(e.getMessage());

		}

		Loggers.loggerEnd();
		return ch;
	}
}
