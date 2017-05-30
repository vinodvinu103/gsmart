package com.gsmart.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.LeaveMasterDao;
import com.gsmart.model.CompoundLeaveMaster;
import com.gsmart.model.LeaveMaster;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
@Transactional
public class LeaveMasterServiceImpl implements LeaveMasterService {
	@Autowired
	private LeaveMasterDao leavemasterdao;

	@Override
	public Map<String, Object> getLeaveMasterList(Long hid, Integer min, Integer max) throws GSmartServiceException {

		try {
			return leavemasterdao.getLeaveMasterList(hid, min, max);

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

	@Override
	public List<LeaveMaster> getLeaveMasterInfo(Long hid) throws GSmartServiceException {
		try {
			return leavemasterdao.getLeaveMasterListForApplyLeave(hid);

		} catch (GSmartDatabaseException Exception) {
			throw (GSmartServiceException) Exception;

		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
	}

	@Override
	public List<LeaveMaster> searchLeaveMaster(LeaveMaster leavemaster, Long hid) throws GSmartServiceException {

		return leavemasterdao.searchLeaveMaster(leavemaster, hid);
	}
}
