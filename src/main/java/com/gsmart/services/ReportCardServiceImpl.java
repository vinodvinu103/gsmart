package com.gsmart.services;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gsmart.dao.ReportCardDao;
import com.gsmart.model.CompoundReportCard;
import com.gsmart.model.ReportCard;
import com.gsmart.model.Token;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class ReportCardServiceImpl implements ReportCardService {

	@Autowired
	ReportCardDao reportCardDao;

	@Override
	public List<ReportCard> reportCardList() throws GSmartServiceException {
		Loggers.loggerStart();
		List<ReportCard> list = null;
		try {
			list = reportCardDao.reportCardList();
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
		return list;
	}

	@Override
	public CompoundReportCard addReportCard(ReportCard card) throws GSmartServiceException {
		Loggers.loggerStart(card);
		CompoundReportCard card2 = null;
		try {
			card2 = reportCardDao.addReportCard(card);

		} catch (ConstraintViolationException e) {
			throw new GSmartServiceException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());

		}
		Loggers.loggerEnd(card2);
		return card2;
	}

	@Override
	public ReportCard editReportCard(ReportCard card) throws GSmartServiceException {
		Loggers.loggerStart(card);
		ReportCard card2 = null;
		try {
			card2 = reportCardDao.editReportCard(card);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(card2);
		return card2;
	}

	@Override
	public void deleteReportCard(ReportCard card) throws GSmartServiceException {
		Loggers.loggerStart(card);
		try {
			reportCardDao.deleteReportCard(card);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
	}

	@Override
	public void excelToDB(String smartId, MultipartFile fileUpload) throws Exception {
		reportCardDao.excelToDB(smartId, fileUpload);
	}

	@Override
	public List<ReportCard> search(Token tokenDetail) throws GSmartServiceException {
		Loggers.loggerStart();
		List<ReportCard> card = null;
		try {
			card = reportCardDao.search(tokenDetail);
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());

		}
		return card;

	}

	@Override
	public List<ReportCard> search(String subject, int acadamicYear) throws GSmartServiceException {
		Loggers.loggerStart();
		List<ReportCard> card = null;
		try {
			card = reportCardDao.search(subject, acadamicYear);
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());

		}
		return card;
	}


}
