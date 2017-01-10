package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gsmart.model.Profile;
import com.gsmart.model.Search;
//import com.gsmart.model.Search;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
public class ProfileDaoImp implements ProfileDao {


	@Autowired
	SessionFactory sessionFactory;
	Session session;
	Query query;
	Transaction transaction;

	/* for registration */

	@SuppressWarnings("unchecked")
	public String getMaxSmartId() {
		/*
		 * This getMaxSmartId() method will get maximum smart id from Emp_Login
		 * table and return to UserProfile Manager.
		 */
		try {
			getConnection();
			query = session
					.createQuery("select smartId from Profile where entryTime in (select max(entryTime) from Profile)");
			ArrayList<String> maxId = (ArrayList<String>) query.list();

			if (!maxId.isEmpty()) {
				System.out.println(maxId.get(0));
				return maxId.get(0);
			} else {
				return new String("DPS1000");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean userProfileInsert(Profile profile) {
		getConnection();
		boolean flag = false;
		try {
			profile.setIsActive("Y");
			profile.setEntryTime(CalendarCalculator.getTimeStamp());
			session.save(profile);
			transaction.commit();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} 
		return flag;
	}

	@Override
	public String updateProfile(Profile profile) {
		try {
			getConnection();

			profile.setIsActive("Y");
			session.update(profile);
			transaction.commit();
			return "update successfully";
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		
		return "update successfully21";
	}

	/* for profile */

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Profile> getAllProfiles() {
		try {
			getConnection();
			query = session.createQuery("from Profile where isActive='Y'");
			return (ArrayList<Profile>) query.list();
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Profile> getProfiles(String role) {
		try {
			Loggers.loggerStart(role);
			getConnection();
			if (role.toLowerCase().equals("student")) {
				query = session.createQuery("from Profile where isActive='Y'and role='student'");
			} else {
				query = session.createQuery("from Profile where isActive='Y'and role!='student'");
			}
			Loggers.loggerEnd(query.list());
			return (ArrayList<Profile>) query.list();
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Profile getParentInfo(String smartId) {
		try {
			getConnection();
			Loggers.loggerStart(smartId);
			/*Profile currentProfile1 = (Profile) session.createQuery("from Profile where smartId='" + smartId + "'").list()
					.get(0);*/
			
			query=session.createQuery("from Profile where smartId=:smartId and isActive='Y' ");
			query.setParameter("smartId", smartId);
			Profile currentProfile=(Profile) query.uniqueResult();
			Loggers.loggerEnd(currentProfile);
			if (currentProfile.getReportingManagerId() != smartId)
				return getProfileDetails(currentProfile.getReportingManagerId());
			else
				return null;
			
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Profile> getReportingProfiles(String smartId) {
		try {
			Loggers.loggerStart(smartId);
			getConnection();
			ArrayList<Profile> reportingList=null;
			query=session.createQuery("from Profile where reportingManagerId=:smartId and isActive='Y' ");
			query.setParameter("smartId", smartId);
			reportingList=(ArrayList<Profile>) query.list();
			Loggers.loggerEnd(reportingList);
			return reportingList;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/* for login */
	public Profile getProfileDetails(String smartId) {

		Loggers.loggerStart(smartId);
		Profile profilelist = null;
		
		try {
			getConnection();
			query = session.createQuery("from Profile where isActive='Y' AND smartId= :smartId");
			query.setParameter("smartId", smartId);
			profilelist = (Profile) query.list().get(0);
			profilelist.setChildFlag(true);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		Loggers.loggerEnd(profilelist);
		return profilelist;
	}

	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> getAllRecord() {
		
		Loggers.loggerStart();
		
		List<Profile> profile = null;
		
		try {
			getConnection();
			query = session.createQuery("from Profile where isActive like('Y')");
			
			profile = (List<Profile>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		
		Loggers.loggerEnd("profile fetched from DB");
		return profile;
	}

	@SuppressWarnings("unchecked")
	public List<Profile> getsearchRep(Search search) {
		Loggers.loggerStart();
		try {
			getConnection();
			query = session.createQuery("from Profile where isActive like('Y') and band<:band  and school =:school");
			query.setParameter("band", search.getBand());
			query.setParameter("school", search.getSchool());
			Loggers.loggerEnd();
			return (List<Profile>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	/**
	 * to search and view the list of records available in {@link Profile} table
	 * 
	 * @return list of profile entities available in Profile
	 */

	@SuppressWarnings("unchecked")
	public List<Profile> search(Profile profile) throws GSmartDatabaseException {
		
		Loggers.loggerStart(profile);
		List<Profile> profileList;
		try {
			getConnection();
			query = session.createQuery("from Profile where firstName like '%" + profile.getFirstName() + "%'");
			profileList = query.list();

		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} 
		
		Loggers.loggerEnd(profileList);
		return profileList;
	}

	/**
	 * persists the updated profile instance
	 * 
	 * @param profile
	 *            instance of {@link Profile}
	 * @return Nothing
	 */
	@Override
	public void editRole(Profile profile) throws GSmartDatabaseException {

		Loggers.loggerStart(profile);
		
		try {
			getConnection();
			query = session.createQuery("UPDATE Profile set   role=:role WHERE entryTime = :entryTime");
			query.setParameter("entryTime", profile.getEntryTime());
			query.setParameter("role", profile.getRole());
			query.executeUpdate();
			transaction.commit();

		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		
		Loggers.loggerEnd();
	}

}