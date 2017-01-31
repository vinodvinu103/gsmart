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

import com.gsmart.model.Assign;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.model.Search;
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
		Loggers.loggerStart();
		/*
		 * This getMaxSmartId() method will get maximum smart id from Emp_Login
		 * table and return to UserProfile Manager.
		 */
		getConnection();
		try {

			query = session
					.createQuery("select smartId from Profile where entryTime in (select max(entryTime) from Profile)");
			ArrayList<String> maxId = (ArrayList<String>) query.list();

			if (!maxId.isEmpty()) {
				System.out.println(maxId.get(0));
				Loggers.loggerEnd();
				return maxId.get(0);
			} else {
				return new String("DPS1000");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public boolean userProfileInsert(Profile profile) {
		Loggers.loggerStart();
		getConnection();
		boolean flag = false;
		try {
			Loggers.loggerValue("smart id for the profile with name : " + profile.getFirstName(), profile.getSmartId());
			getConnection();
			profile.setIsActive("Y");
			query = session.createQuery("from Hierarchy where school='" + profile.getHierarchy().getSchool()
					+ "' and institution='" + profile.getHierarchy().getInstitution() + "'");
			if (query.list().size() > 0) {
				profile.setHierarchy((Hierarchy) query.list().get(0));
			}
//			if (profile.getRole().toUpperCase() == "STUDENT") {
//				Assign assign = getStandardTeacher(profile.getStandard());
//				profile.setReportingManagerId(assign.getTeacherSmartId());
//				session.save(profile);
//				
//			}else{
//				session.save(profile);
//			}
			profile.setEntryTime(CalendarCalculator.getTimeStamp());
			session.save(profile);
			transaction.commit();
			
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		
		return flag;
	}
/*
	public Assign getStandardTeacher(String standard) {
		Assign assign = null;
		Loggers.loggerStart();
		try {
			query = session.createQuery("from Assign where standard=:standard");
			query.setParameter("standard", standard);
			assign = (Assign) query.uniqueResult();
		} catch (Exception e) {

			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return assign;

	}*/

	@Override
	public String updateProfile(Profile profile) {
		Loggers.loggerStart();
		getConnection();
		try {

			profile.setIsActive("Y");
			session.update(profile);
			transaction.commit();
			return "update successfully";
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		Loggers.loggerEnd();
		return "update successfully21";
	}

	/* for profile */

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Profile> getAllProfiles() {
		Loggers.loggerStart();
		getConnection();
		try {

			query = session.createQuery("from Profile where isActive='Y'");
			Loggers.loggerEnd();
			return (ArrayList<Profile>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Profile> getProfiles(String role, String smartId,String loginUserRole,Hierarchy hierarchy) {
		getConnection();
		try {
			Loggers.loggerStart(role);

			Loggers.loggerStart("current smartId" + smartId);
			if(loginUserRole.equalsIgnoreCase("admin"))
			{
				if (role.toLowerCase().equals("student")) {
					query = session.createQuery("from Profile where isActive='Y'and role='student' and smartId like '"
							+ smartId.substring(0, 2) + "%' ");
				} else {
					query = session.createQuery("from Profile where isActive='Y'and role!='student' ");
				}
				
			}else{
			if (role.toLowerCase().equals("student")) {
				query = session.createQuery("from Profile where isActive='Y'and role='student' and smartId like '"
						+ smartId.substring(0, 2) + "%' and hierarchy.hid=:hierarchy");
			} else {
				query = session.createQuery("from Profile where isActive='Y'and role!='student' and hierarchy.hid=:hierarchy");
				
			}
			query.setParameter("hierarchy", hierarchy.getHid());
			}
			
			Loggers.loggerEnd(query.list());
			return (ArrayList<Profile>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
		
	}

	@Override
	public Profile getParentInfo(String smartId) {
		Loggers.loggerStart();
		getConnection();
		try {
			/*
			 * Profile currentProfile1 = (Profile)
			 * session.createQuery("from Profile where smartId='" + smartId +
			 * "'").list() .get(0);
			 */

			query = session.createQuery("from Profile where smartId=:smartId and isActive='Y' ");
			query.setParameter("smartId", smartId);
			Profile currentProfile = (Profile) query.uniqueResult();
			Loggers.loggerEnd(currentProfile);
			if (currentProfile.getReportingManagerId() != smartId)
				return getProfileDetails(currentProfile.getReportingManagerId());
			else
				return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Profile> getReportingProfiles(String smartId) {
		Loggers.loggerStart();
		getConnection();
		try {
			ArrayList<Profile> reportingList = null;
			query = session.createQuery("from Profile where reportingManagerId=:smartId and isActive='Y' ");
			query.setParameter("smartId", smartId);
			reportingList = (ArrayList<Profile>) query.list();
			Loggers.loggerEnd();
			return reportingList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	/* for login */
	public Profile getProfileDetails(String smartId) {

		Loggers.loggerStart();
		Profile profilelist = null;
		getConnection();
		try {

			query = session.createQuery("from Profile where isActive='Y' AND smartId= :smartId");
			query.setParameter("smartId", smartId);
			profilelist = (Profile) query.list().get(0);
			profilelist.setChildFlag(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			session.close();
		}
		Loggers.loggerEnd();
		return profilelist;
	}

	public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> getAllRecord(String academicYear,String role,Hierarchy  hierarchy) {

		Loggers.loggerStart();
		getConnection();

		List<Profile> profile = null;

		try {
			if(role.equalsIgnoreCase("admin"))
			{
				query = session.createQuery("from Profile where isActive=:isActive and academicYear=:academicYear");
				
			}else{
				query = session.createQuery("from Profile where isActive=:isActive and hierarchy.hid=:hierarchy and academicYear=:academicYear");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
			query.setParameter("isActive", "Y");
			
			
			query.setParameter("academicYear", academicYear);

			profile = (List<Profile>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		finally {
			session.close();
		}
		
		return profile;
	}

	@SuppressWarnings("unchecked")
	public List<Profile> getsearchRep(Search search,String role,Hierarchy hierarchy) {
		Loggers.loggerStart();
		getConnection();
		try {
			if(role.equalsIgnoreCase("admin"))
			{

			query = session.createQuery("from Profile where isActive like('Y') and band<:band  and school =:school");
			}else{
				query = session.createQuery("from Profile where isActive like('Y') and band<:band  and school =:school and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
			
			query.setParameter("band", search.getBand());
			query.setParameter("school", search.getSchool());
			List<Profile> profileList = (List<Profile>) query.list();
			query = session.createQuery("from Profile where isActive like('Y') and role='ADMIN'");
			profileList.addAll((List<Profile>) query.list());
			Loggers.loggerEnd();
			return profileList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			session.close();
		}
	}

	/**
	 * to search and view the list of records available in {@link Profile} table
	 * 
	 * @return list of profile entities available in Profile
	 */

	@SuppressWarnings("unchecked")
	public List<Profile> search(Profile profile) throws GSmartDatabaseException {

		Loggers.loggerStart();
		getConnection();
		List<Profile> profileList;
		try {

			query = session.createQuery("from Profile where firstName like '%" + profile.getFirstName() + "%'");
			profileList = query.list();

		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} 
		finally {
			session.close();
		}
		
		Loggers.loggerEnd();

		
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
		getConnection();
		try {

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
		finally {
			session.close();
		}
		
	}
	
	@Override
	public Profile profileDetails(String smartId)throws GSmartDatabaseException {

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

	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> getProfileByHierarchy(Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart(hierarchy);
		getConnection();
		query = session.createQuery("from Profile where hierarchy=" + hierarchy.getHid() + " and role!='STUDENT'");
		Loggers.loggerEnd();
		return (List<Profile>) query.list();

		
	}

	@SuppressWarnings("unchecked")
	public List<Profile> getProfilesWithoutRfid() throws GSmartDatabaseException{
		//Loggers.loggerStart(profile);
		List<Profile> profileListWithoutRfid;
		try {
			getConnection();
			query = session.createQuery(" from Profile where rfId is null AND isActive='Y' AND role='STUDENT'");
			profileListWithoutRfid = query.list();


		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		
		Loggers.loggerEnd(profileListWithoutRfid);
		return profileListWithoutRfid;
		//return null;
	}
	
	public List<Profile> addRfid(Profile rfid)throws GSmartDatabaseException{
		
		 //List<Profile> profileListWithoutRfid = null;
		
		try {
			getConnection();

			session.update(rfid);
		
			transaction.commit();
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}
		return getProfilesWithoutRfid();
		
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Profile> getProfilesWithRfid()throws GSmartDatabaseException{
//		Loggers.loggerStart(profile);
		List<Profile> profileListWithRfid;
		try {
			getConnection();
			query = session.createQuery("from Profile where rfId is not null AND isActive='Y' AND role='STUDENT'");
			profileListWithRfid = query.list();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		
		Loggers.loggerEnd(profileListWithRfid);
		return profileListWithRfid;
		
	}	

	@Override
	public List<Profile> editRfid(Profile rfid) throws GSmartDatabaseException {
	//Loggers.loggerStart(profile);
		
		try {
			getConnection();

			session.update(rfid);
		
			transaction.commit();

		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		
		Loggers.loggerEnd();
		return getProfilesWithRfid();

		
	}


}