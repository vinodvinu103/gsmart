package com.gsmart.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.Attendance;
import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.ReportCard;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class DashboardDaoImpl implements DashboardDao {

	@Autowired
	private SessionFactory sessionFactory;

	Query query;

	/*
	 * public void getConnection() { session = sessionfactory.openSession(); tx
	 * = session.beginTransaction(); }
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<Attendance> getAttendance() {

		Loggers.loggerStart();
		List<Attendance> attendancelist = null;

		query = sessionFactory.getCurrentSession().createQuery("FROM Attendance where inDate=:date and isActive='Y'");
		attendancelist = query.list();

		Loggers.loggerEnd();

		return attendancelist;
	}

	/*
	 * @Override public int getInventory() { getConnection();
	 * 
	 * Loggers.loggerStart(); int inventoryCount; query = session.createQuery(
	 * "From Inventory"); inventoryCount = query.list().size();
	 * 
	 * tx.commit(); session.close(); return inventoryCount; }
	 * 
	 * @Override public int getTotalfee() {
	 * 
	 * getConnection();
	 * 
	 * Loggers.loggerStart(); int totalCOunt; query = session.createQuery(
	 * "From Fee"); totalCOunt = query.list().size();
	 * 
	 * tx.commit(); session.close(); return totalCOunt; }
	 */
	@Override
	public List<Fee> academicYear(Token tokenDetail, String smartId) throws GSmartDatabaseException {
		Loggers.loggerStart();

		List<Fee> academicyear = new ArrayList<>();
		List<String> year = null;
		Hierarchy hierarchy = tokenDetail.getHierarchy();
		String role = tokenDetail.getRole();

		try {
			if (role.equals("ADMIN")) {
				query = sessionFactory.getCurrentSession()
						.createQuery("select distinct academicYear from Fee where isActive='Y' and smartId=:smartId");
				query.setParameter("smartId", smartId);
				year = query.list();}
			/*} else if (role.equals("DIRECTOR")) {
				query = sessionFactory.getCurrentSession()
						.createQuery("select distinct academicYear from Fee where isActive='Y' and smartId=:smartId");
				query.setParameter("smartId", smartId);
				year = query.list();
			} */else {
				query = sessionFactory.getCurrentSession().createQuery(
						"select distinct academicYear from Fee where hid=:hierarchy and isActive='Y' and smartId=:smartId");
				query.setParameter("hierarchy", hierarchy.getHid());
				query.setParameter("smartId", smartId);
				year = query.list();
			}

			for (String yearAndExamName1 : year) {
				Fee yeaAndex = new Fee();
				yeaAndex.setAcademicYear(yearAndExamName1);
				academicyear.add(yeaAndex);
			}

			Loggers.loggerEnd(academicyear);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return academicyear;
	}

	@Override
	public List<ReportCard> examName(Token tokenDetail, String smartId, String academicYear)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<String> exam1 = null;
		List<String> class1 = null;
		ArrayList<ReportCard> examName = new ArrayList<>();
		Hierarchy hierarchy = tokenDetail.getHierarchy();
		String role = tokenDetail.getRole();
		System.out.println(role);
		System.out.println(role.equals("ADMIN"));
		System.out.println(!role.equals("ADMIN"));
		System.out.println(!role.equals("DIRECTOR"));
		try {
			if (!role.equals("ADMIN")) {
				System.out.println(" in side if its for principal>>>>>>>>>>>>>>>>>>>>>>>>>>");
				query = sessionFactory.getCurrentSession().createQuery(
						"select distinct examName from ReportCard where (reportingManagerId=:reportingManagerId or smartId=:smartId) and hid=:hierarchy and academicYear=:academicYear and isActive='Y'");
				query.setParameter("reportingManagerId", smartId);
				query.setParameter("smartId", smartId);
				query.setParameter("hierarchy", hierarchy.getHid());
				query.setParameter("academicYear", academicYear);
				exam1 = query.list();
				query = sessionFactory.getCurrentSession().createQuery(
						"select standard from ReportCard where (reportingManagerId=:reportingManagerId or smartId=:smartId) and hid=:hierarchy and academicYear=:academicYear and isActive='Y'");
				query.setParameter("reportingManagerId", smartId);
				query.setParameter("smartId", smartId);
				query.setParameter("hierarchy", hierarchy.getHid());
				query.setParameter("academicYear", academicYear);
				class1 = query.list();
			} /*else if (!role.equals("DIRECTOR")) {
				System.out.println(" in side ELSE-IF its for DIRECTOR>>>>>>>>>>>>>>>>>>>>>>>>>>");
				query = sessionFactory.getCurrentSession().createQuery(
						"select distinct examName from ReportCard where (reportingManagerId=:reportingManagerId or smartId=:smartId) and hid=:hierarchy and academicYear=:academicYear and isActive='Y'");
				query.setParameter("reportingManagerId", smartId);
				query.setParameter("smartId", smartId);
				query.setParameter("hierarchy", hierarchy.getHid());
				query.setParameter("academicYear", academicYear);
				exam1 = query.list();
				query = sessionFactory.getCurrentSession().createQuery(
						"select standard from ReportCard where (reportingManagerId=:reportingManagerId or smartId=:smartId) and hid=:hierarchy and academicYear=:academicYear and isActive='Y'");
				query.setParameter("reportingManagerId", smartId);
				query.setParameter("smartId", smartId);
				query.setParameter("hierarchy", hierarchy.getHid());
				query.setParameter("academicYear", academicYear);
				class1 = query.list();

			} */else {
				System.out.println(" in side else its for admin >>>>>>>>>>>>>>>>>>>>>>>>>>");
				query = sessionFactory.getCurrentSession().createQuery(
						"select distinct examName from ReportCard where (reportingManagerId=:reportingManagerId or smartId=:smartId) and academicYear=:academicYear and isActive='Y'");
				query.setParameter("reportingManagerId", smartId);
				query.setParameter("smartId", smartId);
				query.setParameter("academicYear", academicYear);
				exam1 = query.list();
				query = sessionFactory.getCurrentSession().createQuery(
						"select standard from ReportCard where (reportingManagerId=:reportingManagerId or smartId=:smartId) and academicYear=:academicYear and isActive='Y'");
				query.setParameter("reportingManagerId", smartId);
				query.setParameter("smartId", smartId);
				query.setParameter("academicYear", academicYear);
				class1 = query.list();
			}

			for (String examName1 : exam1) {
				ReportCard ex = new ReportCard();
				ex.setExamName(examName1);
				for (Iterator iterator = class1.iterator(); iterator.hasNext();) {
					String string = (String) iterator.next();
					ex.setStandard(string);
					ex.setSmartId(tokenDetail.getSmartId());
				}
				examName.add(ex);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return examName;
	}

}
