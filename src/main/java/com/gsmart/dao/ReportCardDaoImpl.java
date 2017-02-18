package com.gsmart.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

	public void getConnection() {
		session = sessionFactroy.openSession();
		tranction = session.beginTransaction();
	}

	@Override
	public List<ReportCard> reportCardList() throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		List<ReportCard> cards = null;
		try {

			
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
		getConnection();
		Loggers.loggerStart(card);
		CompoundReportCard card2 = null;
		try {
			
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
		getConnection();
		try {
			Loggers.loggerStart();
			
			card.setExitTime(CalendarCalculator.getTimeStamp());
			card.setIsActive("D");
			session.update(card);
			tranction.commit();
			session.close();
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
	}

	public ReportCard getCard(String entryTime) {
		getConnection();
		Loggers.loggerStart(entryTime);
		ReportCard reportCard = null;
		try {
			
			query = session.createQuery("from ReportCard where entryTime=:entryTime and isActive=:isActive");
			query.setParameter("entryTime", entryTime);
			query.setParameter("isActive", "Y");
			reportCard = (ReportCard) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
		return reportCard;

	}

	@Override
	public List<ReportCard> search(Token tokenDetail) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		List<ReportCard> list = null;
		Hierarchy hierarchy = tokenDetail.getHierarchy();
		try {
			getConnection();
			String role = tokenDetail.getRole();
			String smartId = tokenDetail.getSmartId();
			query = session.createQuery(
					"from ReportCard where reportingManagerId=:reportingManagerId or smartId=:smartId and isActive='Y' and hid=:hierarchy");
			query.setParameter("reportingManagerId", smartId);
			query.setParameter("smartId", smartId);
			query.setParameter("hierarchy", hierarchy.getHid());
			list = query.list();
			System.out.println("Serch based on smartid..." + list);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
		Loggers.loggerEnd();
		return list;
	}

	@Override
	public ArrayList<ReportCard> reportCardBasedOnAcademicYear(String academicYear) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart();
		ArrayList<ReportCard> list = null;
		try {
			

			query = session.createQuery("from ReportCard where academicYear=:academicYear and isActive='Y')");
			query.setParameter("academicYear", academicYear);
			list = (ArrayList<ReportCard>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
		Loggers.loggerEnd();
		return list;
	}

	@Override
	public void excelToDB(String smartId, MultipartFile fileUpload) throws Exception {
		InputStream in = null;
		XSSFWorkbook workBook = null;
		XSSFSheet sheet = null;
		FileInputStream file = null;
		Loggers.loggerStart(smartId);
		try {
			/*
			 * file = new FileInputStream(new
			 * File("/home/gtpl/Desktop/Report_Card.xlsx"));
			 * Loggers.loggerStart(file);
			 * 
			 * XSSFWorkbook workbook = new XSSFWorkbook(file);
			 * System.out.println("11111111111"); XSSFSheet sheet =
			 * workbook.getSheetAt(0); System.out.println("222222222222");
			 * 
			 * // FormulaEvaluator //
			 * fe=workBook.getCreationHelper().createFormulaEvaluator(); XSSFRow
			 * row = null; getConnection(); System.out.println("hi>>>>." +
			 * sheet.getLastRowNum());
			 */
			in = fileUpload.getInputStream();
			workBook = new XSSFWorkbook(in);
			sheet = workBook.getSheetAt(0);
			XSSFRow row = null;
			getConnection();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				System.out.println("in side for loop");
			
				row = sheet.getRow(i);
				// System.out.println("row"+row);
				ReportCard reportCard = new ReportCard();
				// reportCard.setSmartId(smartId);
				System.out.println("row" + reportCard);
				reportCard.setEntryTime(CalendarCalculator.getTimeStamp());
				System.out.println("EntryTime" + reportCard);
				reportCard.setIsActive("Y");
				System.out.println("setIsActive" + reportCard);
				reportCard.setSmartId(row.getCell(0).getStringCellValue());
				System.out.println("setSmartId  >>>" + row.getCell(0).getStringCellValue());

				reportCard.setStudentName(row.getCell(1).getStringCellValue());
				System.out.println("setStudentName  " + row.getCell(1).getStringCellValue());
				try {

					String std = Integer.toString((int) row.getCell(2).getNumericCellValue());
					System.out.println("Stsnsdard.........." + std);
					reportCard.setStandard(std);
					System.out.println("in try block");

				} catch (Exception e) {
					System.out.println("in catch block");
					reportCard.setStandard(row.getCell(2).getStringCellValue());
				}

				reportCard.setSection(row.getCell(3).getStringCellValue());
				System.out.println("setSection  " + row.getCell(3).getStringCellValue());

				reportCard.setSubject(row.getCell(4).getStringCellValue());
				System.out.println("setSubject  " + row.getCell(4).getStringCellValue());

				reportCard.setTeacherName(row.getCell(5).getStringCellValue());
				System.out.println("setTeacherName  " + reportCard);

				reportCard.setReportingManagerId(row.getCell(6).getStringCellValue());
				System.out.println("setReportingManagerId  " + row.getCell(6).getStringCellValue());

				reportCard.setMaxMarks((int) row.getCell(7).getNumericCellValue());
				System.out.println("setMaxMarks  " + row.getCell(7).getNumericCellValue());

				reportCard.setMinMarks((int) row.getCell(8).getNumericCellValue());
				System.out.println("setMinMarks" + row.getCell(8).getNumericCellValue());

				reportCard.setMarksObtained((int) row.getCell(9).getNumericCellValue());
				System.out.println("setMarksObtained" + row.getCell(9).getNumericCellValue());

				reportCard.setSubjectGrade(row.getCell(10).getStringCellValue());
				System.out.println("setSubjectGrade" + row.getCell(10).getStringCellValue());

				reportCard.setTotalGrade(row.getCell(11).getStringCellValue());
				System.out.println("setTotalGrade" + row.getCell(11).getStringCellValue());

				session.merge(reportCard);
				System.out.println("dane");
			}

			/*
			 * for(Row row:sheet){ for(Cell cell:row){
			 * switch(fe.evaluateInCell(cell).getCellType()){ case
			 * Cell.CELL_TYPE_NUMERIC:
			 * System.out.println(cell.getNumericCellValue()+"\t\t"); break;
			 * case Cell.CELL_TYPE_STRING:
			 * System.out.println(cell.getStringCellValue()+"\t\t"); break; } }
			 * }
			 */
			tranction.commit();
			Loggers.loggerEnd();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			/*
			 * workbook.close(); file.close(); session.close();
			 */
		}
	}

}
