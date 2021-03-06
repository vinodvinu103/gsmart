package com.gsmart.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.gsmart.model.Banners;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.model.Search;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class ProfileDaoImp implements ProfileDao {

	@Autowired
	private SessionFactory sessionFactory;
	private Query query;
	/* for registration */

	@SuppressWarnings("unchecked")
	public String getMaxSmartId() {
		Loggers.loggerStart();
		/*
		 * This getMaxSmartId() method will get maximum smart id from Emp_Login
		 * table and return to UserProfile Manager.
		 */

		try {

			query = sessionFactory.getCurrentSession()
					.createQuery("select smartId from Profile where entryTime in (select max(entryTime) from Profile)");
			ArrayList<String> maxId = (ArrayList<String>) query.list();

			if (!maxId.isEmpty()) {
				System.out.println(maxId.get(0));
				Loggers.loggerEnd();
				return maxId.get(0);
			} else {
				return "DPS1000";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean userProfileInsert(Profile profile) throws GSmartDatabaseException{
		Loggers.loggerStart();
		Session session = this.sessionFactory.getCurrentSession();

		boolean flag = false;
		try {
			query = sessionFactory.getCurrentSession().createQuery("from Profile where emailId=:emailId and isActive='Y'");
			query.setParameter("emailId", profile.getEmailId());
			Profile profile2 = (Profile) query.uniqueResult();
			if (profile2 != null) {
				flag = false;
			} else {
				profile.setIsActive("Y");
				query = sessionFactory.getCurrentSession()
						.createQuery("from Hierarchy where school='" + profile.getHierarchy().getSchool()
								+ "' and institution='" + profile.getHierarchy().getInstitution() + "'");
				if (query.list().size() > 0) {
					profile.setHierarchy((Hierarchy) query.list().get(0));
					System.out.println(query.list().get(0));
				}

				profile.setEntryTime(CalendarCalculator.getTimeStamp());
				session.save(profile);

				flag = true;
			}
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		}catch (NullPointerException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.NULL_PONITER);
		}catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd();

		return flag;
	}
	/*
	 * public Assign getStandardTeacher(String standard) { Assign assign = null;
	 * Loggers.loggerStart(); try { query = session.createQuery(
	 * "from Assign where standard=:standard"); query.setParameter("standard",
	 * standard); assign = (Assign) query.uniqueResult(); } catch (Exception e)
	 * {
	 * 
	 * e.printStackTrace(); } Criteria criteria = null;Loggers.loggerEnd();
	 * return assign;
	 * 
	 * }
	 */

	@Override
	public String updateProfile(Profile profile) {
		Loggers.loggerStart();
		Session session = this.sessionFactory.getCurrentSession();

		try {

			profile.setIsActive("Y");
			session.update(profile);
			return "update successfully";
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		Loggers.loggerEnd();
		return "update successfully21";
	}

	@Override
	public String changeprofileimage(Profile profile) {
		Loggers.loggerStart();
		try {
			query = sessionFactory.getCurrentSession()
					.createQuery("update Profile set image=:image where smartId=:smartId and isActive=:isActive");
			query.setParameter("image", profile.getImage());
			query.setParameter("smartId", profile.getSmartId());
			query.setParameter("isActive", "Y");
			query.executeUpdate();
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
			return "Profile image not successfully updated";
		}
		return "Profile image updated";
	}

	@Override
	public String deleteprofile(Profile profile) {
		Loggers.loggerStart();
		Session session = this.sessionFactory.getCurrentSession();

		try {
			profile.setIsActive("D");
			profile.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(profile);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return "deleted successfully";
	}

	/* for profile */

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Profile> getAllProfiles(String academicYear) throws GSmartDatabaseException{
		Loggers.loggerStart("getAllProfiles Api started in Profile Dao for year :"+academicYear);
		ArrayList<Profile> getAllProfiles=null;
		try {

			query = sessionFactory.getCurrentSession().createQuery("from Profile where isActive='Y' and academicYear=:academicYear");
			query.setParameter("academicYear", academicYear);
			getAllProfiles=(ArrayList<Profile>) query.list();
			
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		}catch (NullPointerException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.NULL_PONITER);
		}catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerStart("getAllProfiles Api ended in Profile Dao for year :"+academicYear+ " with profiles count of"+getAllProfiles.size());
		return getAllProfiles;
	}

	@Override
	public Map<String, Object> getProfiles(String role, String smartId, Long hid, int min, int max) throws GSmartDatabaseException {
		Loggers.loggerStart(role);
		Criteria criteria = null;
		Loggers.loggerStart("current smartId" + smartId);
		/*
		 * session = this.getSessionFactory().openSession();
		 * session.beginTransaction();
		 */

		// List<Profile> profileList = null;
		
		
		Map<String, Object> profileMap = new HashMap<String, Object>();
		try {

			criteria = sessionFactory.getCurrentSession().createCriteria(Profile.class);
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.setFirstResult(min);
			criteria.setMaxResults(max);
			criteria.addOrder(Order.asc("firstName"));
			// criteria.setProjection(Projections.id());

			Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(Profile.class)
					.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("hierarchy.hid", hid));
			criteriaCount.add(Restrictions.eq("hierarchy.hid", hid));
			if (role.equalsIgnoreCase("student")) {
				criteria.add(Restrictions.eq("role", "student").ignoreCase());
				Profile employeeId=getPreviousEmployeeId(role,hid);

				criteriaCount.add(Restrictions.eq("role", "student").ignoreCase());

				/**
				 * query = session.createQuery( "from Profile where
				 * isActive='Y'and lower(role)='student'" );
				 */
				profileMap.put("profileMap1", criteria.list());
				profileMap.put("employeeId", employeeId.getStudentId());
			} else {
				criteria.add(Restrictions.ne("role", "student").ignoreCase());
				criteriaCount.add(Restrictions.ne("role", "student").ignoreCase());
				Profile employeeId=getPreviousEmployeeId(role,hid);
				/**
				 * query = session.createQuery( "from Profile where
				 * isActive='Y'and lower(role)!='student' " );
				 */
				profileMap.put("profileList", criteria.list());
				profileMap.put("employeeId", employeeId.getTeacherId());
			}
			criteriaCount.setProjection(Projections.rowCount());
			
			profileMap.put("totalProfiles", criteriaCount.uniqueResult());
			Loggers.loggerEnd(criteria.list());
			return profileMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private Profile getPreviousEmployeeId(String role, Long hid) throws GSmartDatabaseException {
		Profile currentProfile =null;
		try {
			Loggers.loggerStart();
			if (role.equalsIgnoreCase("student")) {
				query = sessionFactory.getCurrentSession().createQuery("from Profile where entryTime in (select max(entryTime) from Profile where lower(role)=:role and hid=:hid)");
				query.setParameter("role", role);
			} else {
				query = sessionFactory.getCurrentSession().createQuery("from Profile where entryTime in (select max(entryTime) from Profile where lower(role)!='student' and hid=:hid)");
			}
			query.setParameter("hid", hid);
			
			currentProfile= (Profile) query.uniqueResult();
			
			
		} 	catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd("previous employeeId"+currentProfile.getTeacherId());
		return currentProfile;
		
	}

	@Override
	public Profile getParentInfo(String smartId) throws GSmartDatabaseException{

		Loggers.loggerStart();

		try {
			/*
			 * Profile currentProfile1 = (Profile) session.createQuery(
			 * "from Profile where smartId='" + smartId + "'").list() .get(0);
			 */

			query = sessionFactory.getCurrentSession()
					.createQuery("from Profile where smartId=:smartId and isActive='Y' ");
			query.setParameter("smartId", smartId);
			Profile currentProfile = (Profile) query.uniqueResult();
			Loggers.loggerEnd(currentProfile);
			if (!currentProfile.getReportingManagerId().equals(smartId))
				return getProfileDetails(currentProfile.getReportingManagerId());
			else
				return null;

		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Profile> getReportingProfiles(String smartId) {
		Loggers.loggerStart();

		try {
			ArrayList<Profile> reportingList = null;
			query = sessionFactory.getCurrentSession()
					.createQuery("from Profile where reportingManagerId=:smartId and isActive='Y' ");
			query.setParameter("smartId", smartId);
			reportingList = (ArrayList<Profile>) query.list();
			Loggers.loggerEnd();
			return reportingList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/* for login */
	public Profile getProfileDetails(String smartId) {

		Loggers.loggerStart(smartId);
		Profile profilelist = null;

		try {

			query = sessionFactory.getCurrentSession()
					.createQuery("from Profile where isActive='Y' AND smartId=:smartId");
			query.setParameter("smartId", smartId);
			profilelist = (Profile) query.uniqueResult();
			profilelist.setChildFlag(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(profilelist);
		return profilelist;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> getAllRecord(String academicYear, Long hid) {

		Loggers.loggerStart();

		List<Profile> profile = null;

		try {

			query = sessionFactory.getCurrentSession().createQuery(
					"from Profile where isActive=:isActive and hierarchy.hid=:hierarchy and academicYear=:academicYear");
			query.setParameter("hierarchy", hid);

			query.setParameter("isActive", "Y");

			query.setParameter("academicYear", academicYear);

			profile = (List<Profile>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		Loggers.loggerEnd();
		return profile;
	}

	@SuppressWarnings("unchecked")
	public List<Profile> getsearchRep(Search search, String role, Hierarchy hierarchy) {
		Loggers.loggerStart();

		try {
			if (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director")) {
				query = sessionFactory.getCurrentSession()
						.createQuery("from Profile where isActive like('Y') and band<:band ");
			} else {
				query = sessionFactory.getCurrentSession().createQuery(
						"from Profile where isActive like('Y') and band<:band and school=:school and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
				query.setParameter("school", search.getSchool());
			}
			query.setParameter("band", search.getBand());
			
			List<Profile> profileList = (List<Profile>) query.list();
			query = sessionFactory.getCurrentSession()
					.createQuery("from Profile where isActive like('Y') and role='ADMIN' ");
			profileList.addAll((List<Profile>) query.list());
			Loggers.loggerEnd();
			return profileList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * to search and view the list of records available in {@link Profile} table
	 * 
	 * @return list of profile entities available in Profile
	 */

	@SuppressWarnings("unchecked")
	public List<Profile> search(Profile profile, Hierarchy hierarchy) throws GSmartDatabaseException {
		Loggers.loggerStart();

		List<Profile> profileList;
		try {
			if (hierarchy == null) {

				query = sessionFactory.getCurrentSession()
						.createQuery("from Profile where firstName like '%" + profile.getFirstName() + "%'");
			} else {
				query = sessionFactory.getCurrentSession().createQuery("from Profile where firstName like '%"
						+ profile.getFirstName() + "%' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}

			profileList = query.list();

		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
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

		try {

			query = sessionFactory.getCurrentSession()
					.createQuery("UPDATE Profile set   role=:role WHERE smartId=:smartId and entryTime = :entryTime");
			query.setParameter("entryTime", profile.getEntryTime());
			query.setParameter("role", profile.getRole());
			query.setParameter("smartId", profile.getSmartId());
			query.executeUpdate();

		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> getProfileByHierarchy(Hierarchy hierarchy) throws GSmartDatabaseException {
		List<Profile> profileByHierarchy = null;
		Loggers.loggerStart(hierarchy);
		try {
			query = sessionFactory.getCurrentSession()
					.createQuery("from Profile where hierarchy=" + hierarchy.getHid() + " and role!='STUDENT'");
			profileByHierarchy = (List<Profile>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return profileByHierarchy;

	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getProfilesWithoutRfid(Integer min, Integer max,Long hierarchy) throws GSmartDatabaseException {
		// Loggers.loggerStart(profile);

		 Loggers.loggerStart(hierarchy);

		List<Profile> profileListWithoutRfid;
		Map<String, Object> rfidMap = new HashMap<>();
		// Criteria criteria = session.createCriteria(Profile.class);
		try {
			/*
			 * query = session.createQuery(
			 * "from Profile where rfId is null AND isActive='Y'");
			 * profileListWithoutRfid = query.list();
			 */
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Profile.class);
			/*
			 * criteria.add(Restrictions.isNull("rfId"));
			 * criteria.add(Restrictions.eq("rfId", "''"));
			 */
			criteria.add(Restrictions.disjunction()
					.add(Restrictions.or(Restrictions.isNull("rfId"), Restrictions.like("rfId", ""))));

			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("hierarchy.hid", hierarchy));
			criteria.setFirstResult(min);
			criteria.setMaxResults(max);
			profileListWithoutRfid = criteria.list();
			System.out.println("withoutjhfvdbjdfhvjhdfbvjdh" + profileListWithoutRfid);
			rfidMap.put("profileListWithoutRfid", profileListWithoutRfid);
			Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(Profile.class);
			criteriaCount.setProjection(Projections.rowCount());
			criteriaCount.add(Restrictions.disjunction()
					.add(Restrictions.or(Restrictions.isNull("rfId"), Restrictions.like("rfId", ""))));

			criteriaCount.add(Restrictions.eq("isActive", "Y"));
			criteriaCount.add(Restrictions.eq("hierarchy.hid", hierarchy));
			rfidMap.put("totalrfid", criteriaCount.uniqueResult());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		Loggers.loggerEnd(profileListWithoutRfid);
		return rfidMap;
		// return null;
	}

	public Map<String, Object> addRfid(Profile rfid) throws GSmartDatabaseException {

		try {
			Session session = this.sessionFactory.getCurrentSession();

			session.update(rfid);
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getProfilesWithRfid(Integer min, Integer max,Long hierarchy) throws GSmartDatabaseException {
		//getConnection();
		List<Profile> profileListWithRfid;
		Map<String, Object> rfidWithMap = new HashMap<>();
		try {
			/*
			 * query = session.createQuery(
			 * "from Profile where rfId is not null AND isActive='Y'");
			 * profileListWithRfid = query.list();
			 */


			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Profile.class);

			criteria.add(Restrictions.neOrIsNotNull("rfId", ""));
			/*
			 * criteria.add(Restrictions.disjunction().add(
			 * Restrictions.or(Restrictions.isNotNull("rfId"),
			 * Restrictions.neOrIsNotNull("rfId", ""))));
			 */
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.add(Restrictions.eq("hierarchy.hid", hierarchy));
			criteria.setFirstResult(min);
			criteria.setMaxResults(max);
			profileListWithRfid = criteria.list();
			System.out.println("dfcsdgcysyhfvgyhfgv" + profileListWithRfid);
			rfidWithMap.put("profileListWithRfid", profileListWithRfid);
			Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(Profile.class);
			criteriaCount.setProjection(Projections.rowCount());
			criteriaCount.add(Restrictions.neOrIsNotNull("rfId", ""));
			criteriaCount.add(Restrictions.eq("isActive", "Y"));
			criteriaCount.add(Restrictions.eq("hierarchy.hid", hierarchy));
			rfidWithMap.put("totalwithrfid", criteriaCount.uniqueResult());

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Loggers.loggerEnd(profileListWithRfid);
		return rfidWithMap;
	}

	@Override
	public List<Profile> editRfid(Profile rfid) throws GSmartDatabaseException {
		try {
			Session session = this.sessionFactory.getCurrentSession();

			session.update(rfid);
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}

		Loggers.loggerEnd();
		// return getProfilesWithRfid();
		return null;

	}

	@SuppressWarnings("unchecked")
	public List<Profile> searchProfilesWithoutRfid(String searchData, String role, Hierarchy hierarchy)
			throws GSmartDatabaseException {

		Loggers.loggerStart(searchData);
		List<Profile> profileListWithoutRfid;
		try {
			String key = searchData.toLowerCase();
			query = sessionFactory.getCurrentSession().createQuery(
					"from Profile where smartId in (select smartId from Profile where lower(firstName) like '%" + key
							+ "%' or lower(teacherId) like '%" + key + "%' or lower(studentId) like '%" + key
							+ "%') and rfId is null and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			profileListWithoutRfid = query.list();

		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd(profileListWithoutRfid);
		return profileListWithoutRfid;
	}

	@SuppressWarnings("unchecked")
	public List<Profile> searchProfilesWithRfid(String searchData, String role, Hierarchy hierarchy)
			throws GSmartDatabaseException {

		// Loggers.loggerStart("searching by name : " + profile.getFirstName());
		List<Profile> profileListWithRfid;
		try {
			String key = searchData.toLowerCase();
			query = sessionFactory.getCurrentSession().createQuery(
					"from Profile where smartId in (select smartId from Profile where lower(firstName) like '%" + key
							+ "%' or lower(teacherId) like '%" + key + "%' or lower(studentId) like '%" + key
							+ "%') and rfId is not null and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			profileListWithRfid = query.list();

		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd(profileListWithRfid);

		return profileListWithRfid;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Banners> getBannerList() {
		Loggers.loggerStart("getBanner api started in Profile Dao  " );
		List<Banners> bannerlist = null;
		try {
			Query query = sessionFactory.getCurrentSession()
					.createQuery("FROM Banners WHERE isActive='Y' order by image desc");
			bannerlist = query.list();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Loggers.loggerEnd("getBanner api ended in Profile Dao with bandList size of  "+bannerlist.size() );
		return bannerlist;
	}

	@Override
	public void addBanner(Banners banner) throws GSmartDatabaseException {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			/*
			 * query = session.createQuery(
			 * "FROM BannerImage WHERE title=:title AND bannerimage=:bannerimage "
			 * ); query.setParameter("title", banner.getTitle());
			 * query.setParameter("bannerimage", banner.getBannerimage());
			 */
			banner.setEntryTime(CalendarCalculator.getTimeStamp());
			banner.setIsActive("Y");
			session.save(banner);
			/*
			 * BannerImage oldBannner = (BannerImage) query.uniqueResult();
			 * Loggers.loggerStart(oldBannner); if (oldBannner == null) { cb =
			 * (CompoundBanner) session.save(banner);
			 * Loggers.loggerEnd(oldBannner); }
			 */
		} catch (ConstraintViolationException e) {
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}
	}

	/*
	 * @Override public Banners editBanner(Banners banner) throws
	 * GSmartDatabaseException { try { Loggers.loggerStart(); getConnection();
	 * Banners oldBanner = getBanner(banner.getEntryTime());
	 * oldBanner.setUpdatedTime(CalendarCalculator.getTimeStamp());
	 * oldBanner.setIsActive("N"); session.update(oldBanner);
	 * transaction.commit(); addBanner(banner);
	 * 
	 * session.close(); Loggers.loggerEnd(); } catch
	 * (org.hibernate.exception.ConstraintViolationException e) { } catch
	 * (Throwable e) { throw new GSmartDatabaseException(e.getMessage()); }
	 * 
	 * return banner; }
	 */

	/*
	 * private void updateBanner(Banners oldBanner) { session =
	 * sessionFactory.openSession(); transaction = session.beginTransaction();
	 * 
	 * //Loggers.loggerValue(oldBand.getUpdatedTime());
	 * session.update(oldBanner); transaction.commit(); session.close(); }
	 */

	/*
	 * public Banners getBanner(String entryTime) { Loggers.loggerStart();
	 * Banners banners = null; try {
	 * 
	 * session = sessionFactory.openSession(); transaction =
	 * session.beginTransaction();
	 * 
	 * query = session.createQuery(
	 * "from Banners where isActive='Y'  ORDER BY entryTime desc");
	 * banners=(Banners) query.uniqueResult();
	 * 
	 * Loggers.loggerEnd(banners); return banners; } catch (Exception e) {
	 * e.printStackTrace(); return null; } }
	 */

	/* DELETE DATA FROM THE DATABASE */
	@Override
	public void deleteBanner(Banners banner) throws GSmartDatabaseException {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			Loggers.loggerStart();
			banner.setExitTime(CalendarCalculator.getTimeStamp());
			banner.setIsActive("D");
			session.update(banner);
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> getProfileByHierarchyAndYear(Hierarchy hierarchy, String year) {

		Loggers.loggerStart();
		System.out.println("Year >>>>>>>>>>>> " + year);

		List<Profile> profiles = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Profile where isActive=:isActive and hierarchy.hid=:hierarchy and academicYear=:academicYear");
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameter("isActive", "Y");
			query.setParameter("academicYear", year);

			profiles = (List<Profile>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return profiles;
		}

		return profiles;
	}

	@Override
	public boolean deleteProfileIfMailFailed(String smartId) {
		Loggers.loggerStart();
		try {
			query = sessionFactory.getCurrentSession().createQuery("delete from Profile where smartId=:smartId");
			query.setParameter("smartId", smartId);
			query.executeUpdate();
			deleteLogin(smartId);

		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
		Loggers.loggerEnd();
		return true;
	}

	private void deleteLogin(String smartId) {
		Loggers.loggerStart();
		query = sessionFactory.getCurrentSession().createQuery("delete from Login where smartId=:smartId");
		query.setParameter("smartId", smartId);
		query.executeUpdate();
		Loggers.loggerEnd();

	}
	/*public void getConnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> getProfileByStuentHierarchy(Hierarchy hierarchy,String reportingManagerId) throws GSmartDatabaseException {
		List<Profile> profileByStudent = null;
		Loggers.loggerStart(hierarchy);
		try {

			query = sessionFactory.getCurrentSession().createQuery("from Profile where hierarchy=" + hierarchy.getHid() + " and role='STUDENT' and reportingManagerId=:reportingManagerId and isActive='Y'");
			query.setParameter("reportingManagerId", reportingManagerId);
			profileByStudent = (List<Profile>) query.list(); 

		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd();
		return profileByStudent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> searchemp(Profile profile, Long hid) throws GSmartDatabaseException{
		List<Profile> emplist = null;
		Loggers.loggerStart();
		try {
		if(hid != null){
			query = sessionFactory.getCurrentSession().createQuery("from Profile where firstName like '%"+ profile.getFirstName() +"%' and isActive = 'Y' and role!='STUDENT' and hierarchy.hid = :hierarchy");
			query.setParameter("hierarchy", hid);
		}else{
			query = sessionFactory.getCurrentSession().createQuery("from Profile where firstName like '%"+ profile.getFirstName() +"%' and isActive = 'Y' and role!='STUDENT'");
		}
		emplist = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(emplist);
				return emplist;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> searchstudent(Profile profile, Long hid) throws GSmartDatabaseException {
		List<Profile> studlist = null;
		Loggers.loggerStart();
		try {
		if(hid != null){
			query = sessionFactory.getCurrentSession().createQuery("from Profile where firstName like '%"+ profile.getFirstName() +"%' and isActive = 'Y' and role='STUDENT' and hierarchy.hid = :hierarchy");
			query.setParameter("hierarchy", hid);
		}else{
			query = sessionFactory.getCurrentSession().createQuery("from Profile where firstName like '%"+ profile.getFirstName() +"%' and isActive = 'Y' and role='STUDENT'");
		}
		studlist = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Loggers.loggerEnd(studlist);
		return studlist;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> getProfilesOfNullHierarchy(String academicYear) {
		List<Profile> emplistWithNullHierarchy = null;
		Loggers.loggerStart();
		query = sessionFactory.getCurrentSession().createQuery("from Profile where academicYear=:academicYear and hierarchy.hid is null  and isActive='Y'");
		query.setParameter("academicYear", academicYear);
		emplistWithNullHierarchy = (List<Profile>) query.list(); 
		
		Loggers.loggerEnd(emplistWithNullHierarchy);
		return emplistWithNullHierarchy;

		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> searchwithrfid(Profile profile, Long hid) throws GSmartDatabaseException {
		List<Profile> list = null;
		try{
			query = sessionFactory.getCurrentSession().createQuery("from Profile where isActive='Y' and hierarchy.hid=:hierarchy and rfId!=null and firstName like '%"+profile.getFirstName()+"%'");
			query.setParameter("hierarchy", hid);
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> searchwithoutrfid(Profile profile, Long hid) throws GSmartDatabaseException {
		List<Profile> list = null;
		try{
			query = sessionFactory.getCurrentSession().createQuery("from Profile where isActive='Y' and hierarchy.hid=:hierarchy and rfId=null and firstName like '%"+profile.getFirstName()+"%'");
			query.setParameter("hierarchy", hid);
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

}