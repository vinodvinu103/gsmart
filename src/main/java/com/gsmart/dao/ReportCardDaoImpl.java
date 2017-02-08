package com.gsmart.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.gsmart.model.CompoundReportCard;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.ReportCard;
import com.gsmart.model.Token;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class ReportCardDaoImpl implements ReportCardDao {

	@Autowired
	SessionFactory sessionFactroy;

	Session session = null;
	Query query;
	Transaction tranction = null;
	InputStream in = null;

	public void getConnection() {
		session = sessionFactroy.openSession();
		tranction = session.beginTransaction();
	}

	@Override
	public List<ReportCard> reportCardList() throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<ReportCard> cards = null;
		try {

			getConnection();
			query = session.createQuery("from ReportCard where isActive='Y'");
			cards = query.list();
			Loggers.loggerEnd(cards);
			return cards;
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}

	}

	@Override
	public CompoundReportCard addReportCard(ReportCard card) throws GSmartDatabaseException {
		Loggers.loggerStart(card);
		CompoundReportCard card2 = null;
		try {
			getConnection();
			ReportCard card3 = fetch1(card);
			if (card3 == null) {
				card.setEntryTime(CalendarCalculator.getTimeStamp());
				card.setIsActive("Y");
				card2 = (CompoundReportCard) session.save(card);
				tranction.commit();
			} else
				card2 = null;
			Loggers.loggerEnd(card2);
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}
		return card2;
	}

	@Override
	public ReportCard editReportCard(ReportCard card) throws GSmartDatabaseException {
		Loggers.loggerStart(card);
		ReportCard card2 = null;
		try {
			ReportCard oldcard = getCard(card.getEntryTime());
			card2 = updateReportCard(oldcard, card);
			if (card2 != null)
				addReportCard(card);
			Loggers.loggerEnd(card2);
			return card2;
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
	}

	public ReportCard updateReportCard(ReportCard oldCard, ReportCard card) {
		Loggers.loggerStart();
		ReportCard card2 = null;
		getConnection();
		try {
			ReportCard card3 = fetch(card);
			if (card3 == null) {
				oldCard.setUpdateTime(CalendarCalculator.getTimeStamp());
				oldCard.setIsActive("N");
				session.update(oldCard);

				tranction.commit();
				return oldCard;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return card2;
	}

	public ReportCard fetch1(ReportCard card) throws GSmartDatabaseException {
		Loggers.loggerStart("smartid");
		try {
			query = session
					.createQuery("from ReportCard where smartId=:smartId and isActive=:isActive and subject=:subject");
			query.setParameter("smartId", card.getSmartId());
			query.setParameter("isActive", "Y");
			query.setParameter("subject", card.getSubject());
			ReportCard list = (ReportCard) query.uniqueResult();
			return list;
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
	}

	public ReportCard fetch(ReportCard card) throws GSmartDatabaseException {
		Loggers.loggerStart("smartid");

		try {
			query = session.createQuery(
					"from ReportCard where smartId=:smartId and isActive=:isActive and subject=:subject and "
							+ "maxMarks=:maxMarks and minMarks=:minMarks and marksObtained=:marksObtained and "
							+ "subjectGrade=:subjectGrade and totalGrade=:totalGrade "
							+ "and academicYear=:academicYear and reportingManagerId=:reportingManagerId");
			query.setParameter("smartId", card.getSmartId());
			query.setParameter("isActive", "Y");
			query.setParameter("subject", card.getSubject());
			query.setParameter("minMarks", card.getMinMarks());
			query.setParameter("maxMarks", card.getMaxMarks());
			query.setParameter("marksObtained", card.getMarksObtained());
			query.setParameter("subjectGrade", card.getSubjectGrade());
			query.setParameter("totalGrade", card.getTotalGrade());
			query.setParameter("academicYear", card.getAcademicYear());
			query.setParameter("reportingManagerId", card.getReportingManagerId());
			ReportCard list = (ReportCard) query.uniqueResult();

			return list;
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
	}

	@Override
	public void deleteReportCard(ReportCard card) throws GSmartDatabaseException {
		try {
			Loggers.loggerStart();
			getConnection();
			card.setExitTime(CalendarCalculator.getTimeStamp());
			card.setIsActive("D");
			session.update(card);
			tranction.commit();
			session.close();
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ReportCard getCard(String entryTime) {
		Loggers.loggerStart(entryTime);
		ReportCard reportCard = null;
		try {
			getConnection();
			query = session.createQuery("from ReportCard where entryTime=:entryTime and isActive=:isActive");
			query.setParameter("entryTime", entryTime);
			query.setParameter("isActive", "Y");
			reportCard = (ReportCard) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportCard;

	}

	@Override
	public List<ReportCard> search(Token tokenDetail) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<ReportCard> list = null;
		Hierarchy hierarchy= tokenDetail.getHierarchy();
		try {
			getConnection();

			/*query = session.createQuery(
					"from ReportCard where reportingManagerId=:reportingManagerId or smartId=:smartId and isActive='Y'");
			query.setParameter("reportingManagerId", smartId);
			query.setParameter("smartId", smartId);*/
			String role=tokenDetail.getRole();
			String smartId=tokenDetail.getSmartId();
			if(role.equalsIgnoreCase("Principal")|role.equalsIgnoreCase("admin")|role.equalsIgnoreCase("owner")){
			query=session.createQuery("select sum(maxMarks),sum(marksObtained) from ReportCard where isActive='Y' and hierarchy:hierarchy ");
			query.setParameter("hierarchy", hierarchy.getHid());
			list = query.list();
			}
			else if (role.equalsIgnoreCase("HOD")) {
				query=session.createQuery("select sum(maxMarks),sum(marksObtained) from ReportCard where isActive='Y' and hierarchy:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
				list = query.list();
			}
			System.out.println("Serch based on smartid..." + list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return list;
	}

	@Override
	public List<ReportCard> search(String subject, int standard) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<ReportCard> list = null;
		try {
			getConnection();

			query = session.createQuery("from ReportCard where marksObtained = (Select max(marksObtained) "
					+ "from ReportCard where subject=:subject and isActive='Y' and standard=:standard)");
			query.setParameter("subject", subject);
			query.setParameter("standard", standard);
			list = query.list();
			System.out.println("list on max marks" + list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return list;
	}

	@Override
	public void excelToDB(String smartId, MultipartFile fileUpload) throws Exception {
		XSSFWorkbook workBook = null;
		XSSFSheet sheet = null;
		try {
			in = fileUpload.getInputStream();
			workBook = new XSSFWorkbook(in);
			sheet = workBook.getSheetAt(0);
			XSSFRow row = null;
			getConnection();
			for (int i = 1; i < sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				ReportCard reportCard = new ReportCard();
				reportCard.setSmartId(smartId);
				reportCard.setEntryTime(CalendarCalculator.getTimeStamp());
				reportCard.setIsActive("Y");
				reportCard.setSmartId(row.getCell(0).getStringCellValue());
				reportCard.setStudentName(row.getCell(1).getStringCellValue());
				reportCard.setStandard(row.getCell(2).getStringCellValue());
				reportCard.setSection(row.getCell(3).getStringCellValue());
				reportCard.setSubject(row.getCell(4).getStringCellValue());
				reportCard.setTeacherName(row.getCell(5).getStringCellValue());
				reportCard.setReportingManagerId(row.getCell(6).getStringCellValue());
				reportCard.setMaxMarks((int) row.getCell(7).getNumericCellValue());
				reportCard.setMinMarks((int) row.getCell(8).getNumericCellValue());
				reportCard.setMarksObtained((int) row.getCell(9).getNumericCellValue());
				reportCard.setSubjectGrade(row.getCell(10).getStringCellValue());
				reportCard.setTotalGrade(row.getCell(11).getStringCellValue());
				
				
				
			
				
				
				

				session.merge(reportCard);
			}
			tranction.commit();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			workBook.close();
			in.close();
			session.close();
		}
	}

}
