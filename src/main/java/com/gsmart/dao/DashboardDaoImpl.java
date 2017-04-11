package com.gsmart.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.Attendance;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class DashboardDaoImpl implements DashboardDao {

	@Autowired
	private SessionFactory sessionFactory;

	

	Query query;

	/*public void getConnection() {
		session = sessionfactory.openSession();
		tx = session.beginTransaction();
	}*/

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
