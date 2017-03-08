package com.gsmart.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gsmart.model.Banners;
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
	Criteria criteria = null;
	/* for registration */

	@SuppressWarnings("unchecked")
	public String getMaxSmartId() {
		getConnection();
		Loggers.loggerStart();
		/*
		 * This getMaxSmartId() method will get maximum smart id from Emp_Login
		 * table and return to UserProfile Manager.
		 */

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
		getConnection();
		Loggers.loggerStart();

		boolean flag = false;
		try {
			query = session.createQuery("from Profile where emailId=:emailId");
			query.setParameter("emailId", profile.getEmailId());
			Profile profile2 = (Profile) query.uniqueResult();
			if (profile2 != null) {
				flag = false;
			} else {
				profile.setIsActive("Y");
				query = session.createQuery("from Hierarchy where school='" + profile.getHierarchy().getSchool()
						+ "' and institution='" + profile.getHierarchy().getInstitution() + "'");
				if (query.list().size() > 0) {
					profile.setHierarchy((Hierarchy) query.list().get(0));
					System.out.println(query.list().get(0));
				}
				// if (profile.getRole().toUpperCase() == "STUDENT") {
				// Assign assign = getStandardTeacher(profile.getStandard());
				// profile.setReportingManagerId(assign.getTeacherSmartId());
				// session.save(profile);
				//
				// }else{
				// session.save(profile);
				// }
				profile.setEntryTime(CalendarCalculator.getTimeStamp());
				session.save(profile);
				transaction.commit();

				flag = true;
			}
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
	 * public Assign getStandardTeacher(String standard) { Assign assign = null;
	 * Loggers.loggerStart(); try { query =
	 * session.createQuery("from Assign where standard=:standard");
	 * query.setParameter("standard", standard); assign = (Assign)
	 * query.uniqueResult(); } catch (Exception e) {
	 * 
	 * e.printStackTrace(); } Criteria criteria = null;Loggers.loggerEnd();
	 * return assign;
	 * 
	 * }
	 */

	@Override
	public String updateProfile(Profile profile) {
		getConnection();
		Loggers.loggerStart();

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

	@Override
	public String deleteprofile(Profile profile) {
		getConnection();
		Loggers.loggerStart();

		try {
			profile.setIsActive("D");
			profile.setExitTime(CalendarCalculator.getTimeStamp());
			session.update(profile);
			transaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		return "deleted successfully";
	}

	/* for profile */

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Profile> getAllProfiles() {
		getConnection();
		Loggers.loggerStart();

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
	public Map<String, Object> getProfiles(String role, String smartId, String loginUserRole, Hierarchy hierarchy,
			int min, int max) {
		Loggers.loggerStart(role);
		Loggers.loggerStart("current smartId" + smartId);
		/*session = this.getSessionFactory().openSession();
		session.beginTransaction();*/
		getConnection();
		
//		List<Profile> profileList = null; 
		Map<String, Object> profileMap = new HashMap<String, Object>();
		try {
			
			criteria = session.createCriteria(Profile.class);
			criteria.add(Restrictions.eq("isActive", "Y"));
			criteria.setFirstResult(min);
			criteria.setMaxResults(max);
			criteria.addOrder(Order.asc("smartId"));
//			criteria.setProjection(Projections.id());
			Criteria criteriaCount = session.createCriteria(Profile.class).add(Restrictions.eq("isActive", "Y"));

			if (loginUserRole.equalsIgnoreCase("admin") || loginUserRole.equalsIgnoreCase("owner")
					|| loginUserRole.equalsIgnoreCase("director")) {
				if (role.toLowerCase().equals("student")) {
					criteria.add(Restrictions.eq("role", "student").ignoreCase());
					criteriaCount.add(Restrictions.eq("role", "student").ignoreCase());
					
					 /** query = session.createQuery(
					 * "from Profile where isActive='Y'and lower(role)='student'"
					 * );*/
					profileMap.put("profileMap1", criteria.list());
				} else {
					criteria.add(Restrictions.ne("role", "student").ignoreCase());
					criteriaCount.add(Restrictions.ne("role", "student").ignoreCase());
					
					 /** query = session.createQuery(
					 * "from Profile where isActive='Y'and lower(role)!='student' "
					 * );*/
					profileMap.put("profileList", criteria.list());
				}

			} else {
				criteria.add(Restrictions.eq("hierarchy.hid", hierarchy));
				if (role.toLowerCase().equals("student")) {
					criteria.add(Restrictions.eq("role", "student").ignoreCase());
					criteriaCount.add(Restrictions.eq("role", "student").ignoreCase());
					/*
					 * query = session.createQuery(
					 * "from Profile where isActive='Y'and lower(role)='student' and hierarchy.hid=:hierarchy"
					 * );
					 */
					profileMap.put("profileMap1", criteria.list());
				} else {
					criteria.add(Restrictions.ne("role", "student").ignoreCase());
					criteriaCount.add(Restrictions.ne("role", "student").ignoreCase());
					/*
					 * query = session.createQuery(
					 * "from Profile where isActive='Y'and lower(role)!='student' and hierarchy.hid=:hierarchy"
					 * );
					 */
					profileMap.put("profileList", criteria.list());
				}
//				 query.setParameter("hierarchy", hierarchy.getHid());
			}
			/*criteria.setProjection(Projections.projectionList().
					add(Projections.property("smartId")).
					add(Projections.property("firstName"),"firstName").
					add(Projections.property("lastName"),"lastName").
					add(Projections.property("role"),"role").
					add(Projections.property("reportingManagerName"),"reportingManagerName").
					add(Projections.property("standard"),"standard").
					add(Projections.property("section"),"section").
					add(Projections.property("schoolName"),"schoolName"));*/
			criteriaCount.setProjection(Projections.rowCount());
			profileMap.put("totalProfiles", criteriaCount.uniqueResult());
			Loggers.loggerEnd(criteria.list());
			return profileMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}

	}


	@Override
	public Profile getParentInfo(String smartId) {

		getConnection();
		Loggers.loggerStart();

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
		getConnection();
		Loggers.loggerStart();

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

		getConnection();
		Loggers.loggerStart();
		Profile profilelist = null;

		try {

			query = session.createQuery("from Profile where isActive='Y' AND smartId= :smartId");
			query.setParameter("smartId", smartId);
			profilelist = (Profile) query.list().get(0);
			profilelist.setChildFlag(true);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
	public List<Profile> getAllRecord(String academicYear, String role, Hierarchy hierarchy) {

		getConnection();
		Loggers.loggerStart();

		List<Profile> profile = null;

		try {
			if (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director")) {
				query = session.createQuery("from Profile where isActive=:isActive and academicYear=:academicYear");

			} else {
				query = session.createQuery(
						"from Profile where isActive=:isActive and hierarchy.hid=:hierarchy and academicYear=:academicYear");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
			query.setParameter("isActive", "Y");

			query.setParameter("academicYear", academicYear);

			profile = (List<Profile>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}

		return profile;
	}

	@SuppressWarnings("unchecked")
	public List<Profile> getsearchRep(Search search, String role, Hierarchy hierarchy) {
		getConnection();
		Loggers.loggerStart();

		try {
			if (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director")) {
				query = session.createQuery("from Profile where isActive like('Y') and band<:band and school =:school");
			} else {
				query = session.createQuery(
						"from Profile where isActive like('Y') and band<:band and school=:school and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
			query.setParameter("band", search.getBand());
			query.setParameter("school", search.getSchool());
			List<Profile> profileList = (List<Profile>) query.list();
			session.close();
			getConnection();
			query = session.createQuery("from Profile where isActive like('Y') and role='ADMIN'");
			profileList.addAll((List<Profile>) query.list());
			Loggers.loggerEnd();
			return profileList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
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
		getConnection();
		Loggers.loggerStart();

		List<Profile> profileList;
		try {

			query = session.createQuery("from Profile where firstName like '%" + profile.getFirstName() + "%'");
			profileList = query.list();

		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
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
		getConnection();
		Loggers.loggerStart(profile);

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
		} finally {
			session.close();
		}

	}

	@Override
	public Profile profileDetails(String smartId) throws GSmartDatabaseException {
		getConnection();
		Loggers.loggerStart(smartId);
		Profile profilelist = null;

		try {

			query = session.createQuery("from Profile where isActive='Y' AND smartId= :smartId");
			query.setParameter("smartId", smartId);
			profilelist = (Profile) query.list().get(0);
			profilelist.setChildFlag(true);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

		Loggers.loggerEnd(profilelist);
		return profilelist;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> getProfileByHierarchy(Hierarchy hierarchy) throws GSmartDatabaseException {
		getConnection();
		List<Profile> profileByHierarchy = null;
		Loggers.loggerStart(hierarchy);
		try {
			query = session.createQuery("from Profile where hierarchy=" + hierarchy.getHid() + " and role!='STUDENT'");
			profileByHierarchy = (List<Profile>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		Loggers.loggerEnd();
		return profileByHierarchy;

	}

	@SuppressWarnings("unchecked")
	public List<Profile> getProfilesWithoutRfid() throws GSmartDatabaseException {
		getConnection();
		// Loggers.loggerStart(profile);
		List<Profile> profileListWithoutRfid;
		try {
			getConnection();
			query = session.createQuery("from Profile where rfId is null AND isActive='Y'");
			profileListWithoutRfid = query.list();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}

		Loggers.loggerEnd(profileListWithoutRfid);
		return profileListWithoutRfid;
		// return null;
	}

	public List<Profile> addRfid(Profile rfid) throws GSmartDatabaseException {

		getConnection();
		// List<Profile> profileListWithoutRfid = null;

		try {

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
	public List<Profile> getProfilesWithRfid() throws GSmartDatabaseException {
		getConnection();
		// Loggers.loggerStart(profile);
		List<Profile> profileListWithRfid;
		try {
			getConnection();
			query = session.createQuery("from Profile where rfId is not null AND isActive='Y'");
			profileListWithRfid = query.list();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}

		Loggers.loggerEnd(profileListWithRfid);
		return profileListWithRfid;

	}

	@Override
	public List<Profile> editRfid(Profile rfid) throws GSmartDatabaseException {
		// Loggers.loggerStart(profile);

		getConnection();
		try {

			session.update(rfid);

			transaction.commit();

		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}

		Loggers.loggerEnd();
		return getProfilesWithRfid();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Banners> getBannerList() {
		Loggers.loggerStart();
		List<Banners> bannerlist = null;
		try {
			getConnection();
			Query query = session.createQuery("FROM Banners WHERE isActive='Y'");
			bannerlist = query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		Loggers.loggerEnd(bannerlist);
		return bannerlist;
	}

	@Override
	public void addBanner(Banners banner) throws GSmartDatabaseException {
		getConnection();
		try {
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
			transaction.commit();
		} catch (org.hibernate.exception.ConstraintViolationException e) {
			transaction.rollback();
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public Banners editBanner(Banners banner) throws GSmartDatabaseException {
		try {
			Loggers.loggerStart();
			getConnection();
			Banners oldBanner = getBanner(banner.getEntryTime());
			oldBanner.setUpdatedTime(CalendarCalculator.getTimeStamp());
			oldBanner.setIsActive("N");
			session.update(oldBanner);
			transaction.commit();
			addBanner(banner);

			session.close();
			Loggers.loggerEnd();
		} catch (org.hibernate.exception.ConstraintViolationException e) {
		} catch (Throwable e) {
			throw new GSmartDatabaseException(e.getMessage());
		}

		return banner;
	}

	/*
	 * private void updateBanner(Banners oldBanner) { session =
	 * sessionFactory.openSession(); transaction = session.beginTransaction();
	 * 
	 * //Loggers.loggerValue(oldBand.getUpdatedTime());
	 * session.update(oldBanner); transaction.commit(); session.close(); }
	 */

	public Banners getBanner(String entryTime) {
		Loggers.loggerStart();
		Banners banners = null;
		try {

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createQuery("from Banners where isActive='Y' and entryTime='" + entryTime + "'");
			banners = (Banners) query.uniqueResult();
			Loggers.loggerEnd(banners);
			return banners;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/* DELETE DATA FROM THE DATABASE */
	@Override
	public void deleteBanner(Banners banner) throws GSmartDatabaseException {
		try {
			Loggers.loggerStart();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			banner.setExitTime(CalendarCalculator.getTimeStamp());
			banner.setIsActive("D");
			session.update(banner);
			transaction.commit();
			session.close();
			Loggers.loggerEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Profile> getProfileByHierarchyAndYear(Hierarchy hierarchy, String year) {
		// TODO Auto-generated method stub
		getConnection();
		Loggers.loggerStart();

		List<Profile> profiles = null;
		try {

			query = session.createQuery(
					"from Profile where isActive=:isActive and hierarchy.hid=:hierarchy and academicYear=:academicYear");
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameter("isActive", "Y");

			query.setParameter("academicYear", year);

			profiles = (List<Profile>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return profiles;
		} finally {
			session.close();
		}

		return profiles;
	}

}