package com.gsmart.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;


import org.hibernate.criterion.Restrictions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gsmart.model.CompoundReportCard;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.model.ReportCard;
import com.gsmart.model.Token;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class ReportCardDaoImpl implements ReportCardDao {

	@Autowired
	private SessionFactory sessionFactory;

	Query query;
	Criteria criteria=null;
	/*public void getConnection() {
		session = sessionFactroy.openSession();
		tranction = session.beginTransaction();
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public List<ReportCard> reportCardList(Token tokenDetail) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<ReportCard> list = null;
		Hierarchy hierarchy = tokenDetail.getHierarchy();
		try {
			String smartId = tokenDetail.getSmartId();
			query = sessionFactory.getCurrentSession().createQuery(
					"from ReportCard where (reportingManagerId=:reportingManagerId or smartId=:smartId) "
					+ "and hid=:hierarchy and isActive='Y'");
			query.setParameter("reportingManagerId", smartId);
			query.setParameter("smartId", smartId);
			query.setParameter("hierarchy", hierarchy.getHid());
			list = query.list();

/*
		List<ReportCard> cards = null;
//		Map<String, Object> reportcardMap = new HashMap<String, Object>();
		Criteria criteria = null;
		try {

			
			query = session.createQuery("from ReportCard where isActive='Y'");
			cards = query.list();
			criteria=session.createCriteria(ReportCard.class);
			criteria.setFirstResult(0);
		    criteria.setMaxResults(5);
		    criteria.addOrder(Order.asc("standard"));
		    criteria.setProjection(Projections.id());
//		     cards = criteria.list();
		     Criteria criteriaCount = session.createCriteria(ReportCard.class);
		     criteriaCount.setProjection(Projections.rowCount());
		     Long count = (Long) criteriaCount.uniqueResult();
//		     reportcardMap.put("totalcards", query.list().size());
			Loggers.loggerEnd(cards);
			*/


			System.out.println("Serch based on smartid..." + list);


		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return list;
	}

	@Override
	public CompoundReportCard addReportCard(ReportCard card) throws GSmartDatabaseException {
		Loggers.loggerStart(card);
		Session session=this.sessionFactory.getCurrentSession();
		CompoundReportCard card2 = null;
		try {

			ReportCard card3 = fetch1(card);
			if (card3 == null) {
				card.setEntryTime(CalendarCalculator.getTimeStamp());
				card.setIsActive("Y");
				card2 = (CompoundReportCard) session.save(card);
			} else
				card2 = null;
			Loggers.loggerEnd(card2);
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
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
		Session session=this.sessionFactory.getCurrentSession();
		ReportCard card2 = null;
		try {
			ReportCard card3 = fetch(card);
			if (card3 == null) {
				oldCard.setUpdateTime(CalendarCalculator.getTimeStamp());
				oldCard.setIsActive("N");
				session.update(oldCard);

				return oldCard;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return card2;
	}

	public ReportCard fetch1(ReportCard card) throws GSmartDatabaseException {
		Loggers.loggerStart();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from ReportCard where smartId=:smartId and isActive=:isActive and subject=:subject and examName=:examName and standard=:standard");
			query.setParameter("smartId", card.getSmartId());
			query.setParameter("isActive", "Y");
			query.setParameter("subject", card.getSubject());
			query.setParameter("examName", card.getExamName());
			query.setParameter("standard", card.getStandard());
			ReportCard list = (ReportCard) query.uniqueResult();
			return list;
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
	}

	public ReportCard fetch(ReportCard card) throws GSmartDatabaseException {
		Loggers.loggerStart();

		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from ReportCard where smartId=:smartId and isActive=:isActive and subject=:subject and "
							+ "maxMarks=:maxMarks and minMarks=:minMarks and marksObtained=:marksObtained and "
							+ "subjectGrade=:subjectGrade and academicYear=:academicYear "
							+ "and reportingManagerId=:reportingManagerId and examName=:examName");
			query.setParameter("smartId", card.getSmartId());
			query.setParameter("isActive", "Y");
			query.setParameter("subject", card.getSubject());
			query.setParameter("minMarks", card.getMinMarks());
			query.setParameter("maxMarks", card.getMaxMarks());
			query.setParameter("marksObtained", card.getMarksObtained());
			query.setParameter("subjectGrade", card.getSubjectGrade());
			query.setParameter("academicYear", card.getAcademicYear());
			query.setParameter("reportingManagerId", card.getReportingManagerId());
			query.setParameter("examName", card.getExamName());
			ReportCard list = (ReportCard) query.uniqueResult();

			return list;
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
	}

	@Override
	public void deleteReportCard(ReportCard card) throws GSmartDatabaseException {
		try {
			Session session=this.sessionFactory.getCurrentSession();
			Loggers.loggerStart();

			card.setExitTime(CalendarCalculator.getTimeStamp());
			card.setIsActive("D");
			session.update(card);
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public ReportCard getCard(String entryTime) {
		Loggers.loggerStart();
		ReportCard reportCard = null;
		try {

			query = sessionFactory.getCurrentSession().createQuery("from ReportCard where entryTime=:entryTime and isActive=:isActive");
			query.setParameter("entryTime", entryTime);
			query.setParameter("isActive", "Y");
			reportCard = (ReportCard) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return reportCard;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReportCard> search(Token tokenDetail,String academicYear,String examName) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<ReportCard> list = null;
		Hierarchy hierarchy = tokenDetail.getHierarchy();
		try {
			String smartId = tokenDetail.getSmartId();
			query = sessionFactory.getCurrentSession().createQuery(
					"from ReportCard where (reportingManagerId=:reportingManagerId or smartId=:smartId) "
					+ "and hid=:hierarchy and isActive='Y' and academicYear=:academicYear and "
					+ "examName=:examName");
			query.setParameter("reportingManagerId", smartId);
			query.setParameter("smartId", smartId);
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameter("academicYear", academicYear);
			query.setParameter("examName", examName);
			list = query.list();
			System.out.println("Serch based on smartid..." + list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<ReportCard> reportCardforHOD(Token token, String examName, String academicYear,String smartId) throws GSmartDatabaseException {
		Loggers.loggerStart();
		ArrayList<ReportCard> list = null;
		Hierarchy hierarchy = token.getHierarchy();
		try {

			query = sessionFactory.getCurrentSession().createQuery("from ReportCard where academicYear=:academicYear and examName=:examName and reportingManagerId=:smartId and isActive='Y' and hid=:hierarchy)");
			query.setParameter("academicYear", academicYear);
			query.setParameter("examName", examName);
			query.setParameter("smartId", smartId);
			query.setParameter("hierarchy", hierarchy.getHid());	
			list = (ArrayList<ReportCard>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		Loggers.loggerEnd(list);
		return list;
	}

	@Override
	public void excelToDB(String smartId, MultipartFile fileUpload,Hierarchy hid) throws Exception {
		InputStream in = null;
		Session session=this.sessionFactory.getCurrentSession();
		XSSFWorkbook workBook = null;
		XSSFSheet sheet = null; // org.apache.poi.xssf.usermodel.XSSFSheet
		Loggers.loggerStart();
		Hierarchy hierarchy=hid;
		try {
			 
			in = fileUpload.getInputStream();
			workBook = new XSSFWorkbook(in);
			sheet = workBook.getSheetAt(0);
			XSSFRow row = null;
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
				
				reportCard.setExamName(row.getCell(11).getStringCellValue());
				reportCard.setHierarchy(hierarchy);
				session.merge(reportCard);
				System.out.println("done");
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
			Loggers.loggerEnd();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			/*
			 * workbook.close(); file.close(); session.close();
			 */
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReportCard> acdemicYearAndExamName(Token tokenDetail) throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<ReportCard> yearAndExamName = new ArrayList<>();
		Hierarchy hierarchy = tokenDetail.getHierarchy();
		String role = tokenDetail.getRole();
		String smartId = tokenDetail.getSmartId();
		try {
			if(!role.equals("HOD")){
			query = sessionFactory.getCurrentSession().createQuery(
					"select distinct academicYear from ReportCard where (reportingManagerId=:reportingManagerId or smartId=:smartId ) and hid=:hierarchy and isActive='Y'");
			query.setParameter("reportingManagerId", smartId);
			query.setParameter("smartId", smartId);
			query.setParameter("hierarchy", hierarchy.getHid());
			}
			else{
				query=sessionFactory.getCurrentSession().createQuery("select distinct academicYear from ReportCard where hid=:hierarchy and isActive='Y' ");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
			List<String> year = query.list();

			for (String yearAndExamName1 : year) {
				ReportCard yeaAndex = new ReportCard();
				yeaAndex.setAcademicYear(yearAndExamName1);
				yearAndExamName.add(yeaAndex);
			}
			
			Loggers.loggerEnd(yearAndExamName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return yearAndExamName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<ReportCard> examName(Token tokenDetail, String acdemicYear,String smartId) throws GSmartDatabaseException {
		Loggers.loggerStart();
		ArrayList<ReportCard> examName = new ArrayList<>();
		Hierarchy hierarchy = tokenDetail.getHierarchy();
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"select distinct examName from ReportCard where (reportingManagerId=:reportingManagerId or smartId=:smartId) and hid=:hierarchy and academicYear=:academicYear and isActive='Y'");
			query.setParameter("reportingManagerId", smartId);
			query.setParameter("smartId", smartId);
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameter("academicYear", acdemicYear);
			List<String> exam1 = query.list();
			query = sessionFactory.getCurrentSession().createQuery(
					"select standard from ReportCard where (reportingManagerId=:reportingManagerId or smartId=:smartId) and hid=:hierarchy and academicYear=:academicYear and isActive='Y'");
			query.setParameter("reportingManagerId", smartId);
			query.setParameter("smartId", smartId);
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameter("academicYear", acdemicYear);
			List<String> class1 = query.list();
			
			
			for (String examName1 : exam1) {
				ReportCard ex = new ReportCard();
				ex.setExamName(examName1);
				for (@SuppressWarnings("rawtypes")
				Iterator iterator = class1.iterator(); iterator.hasNext();) {
					String string = (String) iterator.next();
					ex.setStandard(string);
					ex.setSmartId(smartId);
				}
				examName.add(ex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return examName;
	}
	
	@Override
	public Map<String, Object> reportCardListForTeacher(Token tokenDetail, Integer min, Integer max,String academicYear,String examName)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		Map<String, Object> reportCard=new HashMap();
		try {
			criteria = sessionFactory.getCurrentSession().createCriteria(ReportCard.class);
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("reportingManagerId", tokenDetail.getSmartId()));
			criteria.add(Restrictions.eq("hierarchy.hid", tokenDetail.getHierarchy().getHid()));
			criteria.add(Restrictions.eq("academicYear",academicYear));
			criteria.add(Restrictions.eq("examName",examName));
			criteria.addOrder(Order.asc("smartId"));
			criteria.setFirstResult(min);
			criteria.setMaxResults(max);
			reportCard.put("reportCardList", criteria.list());
			
			criteria = sessionFactory.getCurrentSession().createCriteria(ReportCard.class).add(Restrictions.eq("isActive", "Y"))
					.add(Restrictions.eq("reportingManagerId", tokenDetail.getSmartId()))
					.add(Restrictions.eq("academicYear",academicYear))
					.add(Restrictions.eq("examName",examName))
					.add(Restrictions.eq("hierarchy.hid", tokenDetail.getHierarchy().getHid()))
					.setProjection(Projections.rowCount());
			Long count = (Long) criteria.uniqueResult();
			reportCard.put("count", count);
			Loggers.loggerEnd(reportCard);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportCard;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Profile> findChildTeacher(Token tokenObj,String acdemicYear) throws GSmartDatabaseException {
		Loggers.loggerStart(acdemicYear);
		Loggers.loggerStart(tokenObj);
			ArrayList<Profile> childTeacher = null;
			Hierarchy hierarchy = tokenObj.getHierarchy();
			String smartId = tokenObj.getSmartId();
			try {
				query = sessionFactory.getCurrentSession().createQuery(
						"from Profile where reportingManagerId=:smartId and hierarchy.hid=:hierarchy and academicYear=:academicYear and isActive='Y' and role='TEACHER'");
				query.setParameter("smartId", smartId);
				query.setParameter("hierarchy", hierarchy.getHid());
				query.setParameter("academicYear", acdemicYear);
				childTeacher = (ArrayList<Profile>) query.list();
				sessionFactory.getCurrentSession().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Loggers.loggerEnd(childTeacher);
			return childTeacher;
		}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> studentList(Token tokenDetail) throws GSmartDatabaseException {
		List<Profile> list=new ArrayList<>();
		Loggers.loggerStart();
		try {
			query=sessionFactory.getCurrentSession().createQuery("from Profile where isActive='Y' and hid=:hierarchy and reportingManagerId=:smartId");
			query.setParameter("hierarchy", tokenDetail.getHierarchy().getHid());
			query.setParameter("smartId", tokenDetail.getSmartId());
			list=query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return list;
	}
}
