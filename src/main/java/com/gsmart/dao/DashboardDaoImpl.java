package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Attendance;
import com.gsmart.util.Loggers;

@Repository
public class DashboardDaoImpl implements DashboardDao {

	@Autowired
	SessionFactory sessionfactory;

	Session session = null;

	Transaction tx = null;

	Query query;

	public void getConnection() {
		session = sessionfactory.openSession();
		tx = session.beginTransaction();
	}

	@Override
	public List<Attendance> getAttendance() {
		getConnection();

		Loggers.loggerStart();
		List<Attendance> attendancelist = null;

		query = session.createQuery("FROM Attendance where inDate=:date and isActive='Y'");
		attendancelist = query.list();
		tx.commit();
		session.close();

		Loggers.loggerEnd();

		return attendancelist;
	}

	/*@Override
	public int getInventory() {
		getConnection();

		Loggers.loggerStart();
		int inventoryCount;
		query = session.createQuery("From Inventory");
		inventoryCount = query.list().size();

		tx.commit();
		session.close();
		return inventoryCount;
	}

	@Override
	public int getTotalfee() {

		getConnection();

		Loggers.loggerStart();
		int totalCOunt;
		query = session.createQuery("From Fee");
		totalCOunt = query.list().size();

		tx.commit();
		session.close();
		return totalCOunt;
	}*/

}
