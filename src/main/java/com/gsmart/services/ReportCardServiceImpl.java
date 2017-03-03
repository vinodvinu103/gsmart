package com.gsmart.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.ChangeListener;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gsmart.dao.ProfileDao;
import com.gsmart.dao.ReportCardDao;
import com.gsmart.model.CompoundReportCard;
import com.gsmart.model.Fee;
import com.gsmart.model.FeeMaster;
import com.gsmart.model.Profile;
import com.gsmart.model.ReportCard;
import com.gsmart.model.Token;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class ReportCardServiceImpl implements ReportCardService {

	@Autowired
	ReportCardDao reportCardDao;

	@Autowired
	ProfileDao profiledao;

	@Autowired
	FeeServices feeServices;

	@Autowired 
	FeeMasterServices feeMasterServices;
	
	@Autowired
	SearchService searchService;
	
	
	
	@Override
	public List<ReportCard> reportCardList() throws GSmartServiceException {
		Loggers.loggerStart();
//		List<ReportCard> list = null;
		try {
			return reportCardDao.reportCardList();
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());
		}
//		return list;
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
	public void excelToDB(String smartId,MultipartFile fileUpload) throws Exception {
		reportCardDao.excelToDB(smartId,fileUpload);
	}

	@Override
	public List<ReportCard> search(Token tokenDetail) throws GSmartServiceException {
		Loggers.loggerStart();
		List<ReportCard> card = null;
		try {
			card = reportCardDao.search(tokenDetail);
			Loggers.loggerEnd();
		} catch (Exception e) {
			throw new GSmartServiceException(e.getMessage());

		}
		return card;

	}


	@Override
	public void calculatPercentage(String smartId, ArrayList<ReportCard>  childReportCards)
			throws GSmartServiceException {
		Double totalMarks=0.0;
		Double obtainedMarks=0.0;
		Double percentage=0.0;
		for (ReportCard reportCard : childReportCards) {
			/*System.out.println("Child reportcard in service met"+reportCard);*/
			totalMarks +=reportCard.getMaxMarks();
			obtainedMarks +=reportCard.getMarksObtained();
		}
		/*System.out.println("Total marks"+totalMarks);
		System.out.println("Total obtained marks"+obtainedMarks);*/
		try {
			percentage=((obtainedMarks/totalMarks)*100);
			System.out.println("percentage..........."+percentage);
		} catch (Exception e) {
			percentage=0.0;
			System.out.println(" in side catch blk percentage..........."+percentage);
		}
		
	}

	

}
