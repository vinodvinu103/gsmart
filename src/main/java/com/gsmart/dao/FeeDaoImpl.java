package com.gsmart.dao;

import java.time.Year;
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

import com.gsmart.model.Fee;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.Profile;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Repository
@Transactional
public class FeeDaoImpl implements FeeDao {

	@Autowired
	private SessionFactory sessionFactory;


	
	private Query query;
	private Criteria criteria = null;
	
	private String year = (Year.now().getValue())+"-"+(Year.now().getValue()+1);
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Fee> getDashboardFeeList(Fee fee, Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart(fee.getAcademicYear());

		ArrayList<Fee> feeList;
		try {

			query = sessionFactory.getCurrentSession().createQuery(
					"from Fee where smartId =:smartId and isActive=:isActive and academicYear =:academicYear and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);
			query.setParameter("isActive", "Y");
			query.setParameter("smartId", fee.getSmartId());
			query.setParameter("academicYear", fee.getAcademicYear());
			feeList = (ArrayList<Fee>) query.list();
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		}

		Loggers.loggerEnd();
		return feeList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Fee> getFeeList(Fee fee, Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart(fee.getAcademicYear());
		Loggers.loggerStart(hid);
		ArrayList<Fee> feeList;
		try {
			

				query = sessionFactory.getCurrentSession().createQuery(
						"from Fee where smartId =:smartId and academicYear =:academicYear and paidFee>'0' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hid);
			
			query.setParameter("smartId", fee.getSmartId());
			query.setParameter("academicYear", fee.getAcademicYear());
			feeList = (ArrayList<Fee>) query.list();
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} 
			
			 
		Loggers.loggerEnd(feeList);
		return feeList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Fee> getStudentUnpaidFeeList(Fee fee, Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart(fee.getAcademicYear());
		Loggers.loggerStart(hid);
		ArrayList<Fee> StudentUnpaidfeeList;
		try {
			

				query = sessionFactory.getCurrentSession().createQuery(
						"from Fee where smartId =:smartId and academicYear =:academicYear and isActive='Y' and paidFee='0' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hid);
		
			query.setParameter("smartId", fee.getSmartId());
			query.setParameter("academicYear", fee.getAcademicYear());
			StudentUnpaidfeeList = (ArrayList<Fee>) query.list();
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} 
			
			 
		Loggers.loggerEnd(StudentUnpaidfeeList);
		return StudentUnpaidfeeList;
	}
	

	@Override
	public void addFee(Fee fee) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		
		try{
	
			if(fee.getPaidFee()>0)
			{
			fee.setBalanceFee(fee.getBalanceFee()-fee.getPaidFee());
				if(fee.getBalanceFee()<=0){
					Loggers.loggerStart("inside the paid");
					fee.setFeeStatus("paid");
				}else{
					fee.setFeeStatus("unpaid");
				}
				
			}else
			{
				fee.setBalanceFee(fee.getTotalFee());
			}
			fee.setEntryTime(CalendarCalculator.getTimeStamp());
			fee.setDate(CalendarCalculator.getTimeStamp());
			Profile profile = getReportingManagerId(fee.getSmartId());
			fee.setReportingManagerId(profile.getReportingManagerId());
			fee.setParentName(profile.getFatherName());
			fee.setIsActive("Y");
			session.save(fee);
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd();
	}

	public Profile getReportingManagerId(String smartId) {
		Loggers.loggerStart();
		query = sessionFactory.getCurrentSession().createQuery("from Profile where smartId=:smartId");
		query.setParameter("smartId", smartId);
		Profile profileList = (Profile) query.uniqueResult();

		return profileList;

	}

	/*public void getconnection() {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Fee> getFeeLists(String academicYear,Long hid) throws GSmartDatabaseException {
		Loggers.loggerStart();
		
		ArrayList<Fee> feeList=null;
		try
		{
				query=sessionFactory.getCurrentSession().createQuery("From Fee where academicYear=:academicYear and isActive='Y' and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hid);
			
		query.setParameter("academicYear", academicYear);
		feeList=(ArrayList<Fee>) query.list();
		Loggers.loggerEnd(feeList);
		
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
		return feeList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Fee> gettotalfee(String role,Hierarchy hierarchy) throws GSmartServiceException {
		Loggers.loggerStart();
		List<Fee> list = null;
		try {
			if (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("owner") || role.equalsIgnoreCase("director")) {
				query = sessionFactory.getCurrentSession().createQuery("From Fee where isActive=:isActive");
			} else {
				query = sessionFactory.getCurrentSession().createQuery("From Fee where isActive=:isActive and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hierarchy.getHid());
			}
			query.setParameter("isActive", "Y");
			list = query.list();
		} catch (Exception e) {

			e.printStackTrace();
		} 

		Loggers.loggerEnd();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPaidStudentsList(Long hid, Integer min, Integer max)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		
		List<Fee> paidStudentsList=null;
		Map<String, Object> paidfeeMap = new HashMap<String, Object>();
		try
		{
	   criteria = sessionFactory.getCurrentSession().createCriteria(Fee.class);
		criteria.setMaxResults(max);
		criteria.setFirstResult(min);
		criteria.addOrder(Order.asc("smartId"));
		 criteria.add(Restrictions.eq("isActive", "Y"));
	     criteria.add(Restrictions.eq("hierarchy.hid", hid));
	     criteria.add(Restrictions.eq("feeStatus", "paid"));
	     paidStudentsList=criteria.list();
	 
		 Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(Fee.class);
		 criteriaCount.add(Restrictions.eq("isActive", "Y"));
		 criteriaCount.add(Restrictions.eq("hierarchy.hid", hid));
		 criteriaCount.add(Restrictions.eq("feeStatus", "paid"));
	     criteriaCount.setProjection(Projections.rowCount());
		Long count = (Long) criteriaCount.uniqueResult();
		paidfeeMap.put("totalpaidlist", count);
		Loggers.loggerEnd(paidStudentsList);
		paidfeeMap.put("paidStudentsList", paidStudentsList);
		
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
		
		return paidfeeMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getUnpaidStudentsList(Long hid, Integer min, Integer max)
			throws GSmartDatabaseException {
		Loggers.loggerStart();
		List<Fee> unpaidStudentsList = null;
		Map<String, Object> unpaidfeeMap = new HashMap<String, Object>();
		try {
		criteria = sessionFactory.getCurrentSession().createCriteria(Fee.class);
		criteria.setMaxResults(max);
		criteria.setFirstResult(min);
		criteria.addOrder(Order.asc("smartId"));
		 criteria.add(Restrictions.eq("isActive", "Y"));
	     criteria.add(Restrictions.eq("hierarchy.hid", hid));
	     criteria.add(Restrictions.eq("feeStatus", "unpaid"));
		unpaidStudentsList = criteria.list();
		
	    Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(Fee.class);
	     criteriaCount.setProjection(Projections.rowCount());
	     criteriaCount.add(Restrictions.eq("isActive", "Y"));
	     criteriaCount.add(Restrictions.eq("hierarchy.hid", hid));
	     criteriaCount.add(Restrictions.eq("feeStatus", "unpaid"));
//	     Long count = (Long) criteria.uniqueResult();
		unpaidfeeMap.put("totalunpaidlist", criteriaCount.uniqueResult());
		Loggers.loggerEnd(unpaidStudentsList);
		unpaidfeeMap.put("unpaidStudentsList", unpaidStudentsList);
		
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		
		return unpaidfeeMap;

	}



	@Override
	public void editFee(Fee fee,String schoolname) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		try {
	
			Fee oldFee = getFee(fee.getEntryTime(),fee.getHierarchy());
			oldFee.setUpdatedTime(CalendarCalculator.getTimeStamp());
			//System.out.println(oldFee);
			oldFee.setIsActive("N");
			//System.out.println("--------------------"+oldFee);
			session.update(oldFee);
            String invoicenumber=getinvoice(fee.getHierarchy(),schoolname);
			fee.setInVoice(invoicenumber);

			addFee(fee);

		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd();

	}
	  

@SuppressWarnings("unchecked")
public String getinvoice(Hierarchy hierarchy,String schoolname) {
	Loggers.loggerStart();
	try {
	query = sessionFactory.getCurrentSession()
			.createQuery("select inVoice from Fee where inVoice!='null' and hierarchy.hid=:hierarchy");
	
	query.setParameter("hierarchy", hierarchy.getHid());
	ArrayList<String> number = (ArrayList<String>) query.list();
	if (!number.isEmpty()) {
		Loggers.loggerStart("NEW INVOICE CREATING -----");
		String oldinvoice=getRecentInvioceNumber(hierarchy);
		System.out.println(oldinvoice);
		String[] temp = oldinvoice.split("-");
		String newinvoice = temp[0]+"-"+temp[1]+"-"+temp[2]+"-"+temp[3]+"-"+String.valueOf(Integer.parseInt(temp[4])+1);
		System.out.println(newinvoice);
		return newinvoice;
		
	} else {
		Loggers.loggerStart("FRIST INVIOCE NUMBER------");
		String invoicenumber=schoolname+"-"+year+"-"+1;
		
		return  invoicenumber;
		

	}
	}
	catch (Exception e) {
		e.printStackTrace();
		return null;
	}

	}

public String getRecentInvioceNumber(Hierarchy hierarchy) {
	
	Loggers.loggerStart("OLD INVOICE CHECKING");
	
	
	query = sessionFactory.getCurrentSession()
			.createQuery("select inVoice from Fee where  (entryTime in (select max(entryTime) from Fee where inVoice!='null' and hierarchy.hid=:hierarchy ))");
	query.setParameter("hierarchy", hierarchy.getHid());
	Loggers.loggerEnd(hierarchy.getHid());
	String lastinvoice = (String)query.uniqueResult();
	
	
	return lastinvoice;
}

public Fee getFee(String entryTime,Hierarchy hierarchy) {
		
		Loggers.loggerStart(entryTime);
		query = sessionFactory.getCurrentSession().createQuery("from Fee where isActive=:isActive and entryTime =:entryTime and hierarchy.hid=:hierarchy");
		query.setParameter("hierarchy", hierarchy.getHid());
		query.setParameter("isActive", "Y");
		query.setParameter("entryTime", entryTime);
		Fee fee = (Fee) query.uniqueResult();

		Loggers.loggerValue("feeList", fee);
		return fee;

	}

	@Override
	public void deleteFee(Fee fee) throws GSmartDatabaseException {
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		try {

			/*
			 * query = session.
			 * createQuery("update FeeMaster set IsActive=:IsActive, exittime=:exittime where entrytime = :entrytime"
			 * ); query.setParameter("entrytime", feeMaster.getEntrytime());
			 * 
			 * query.setParameter("IsActive", feeMaster.getIsActive());
			 * query.setParameter("exittime", feeMaster.getExittime());
			 */
			fee.setExitTime(CalendarCalculator.getTimeStamp());
			fee.setIsActive("D");
			session.update(fee);

		} catch (Exception e) {
			e.printStackTrace();
			Loggers.loggerException(e.getMessage());
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Fee> getFeeDashboard(String academicYear, Long hid, List<String> childList)
			throws GSmartServiceException {
		Loggers.loggerStart();
		System.out.println("Academic year : " + academicYear + " hierarchy : " + hid
				+ " childlist size : " + childList.size()+ " childlist  : " + childList.toString());
		List<Fee> feeList = null;
		try {
			query = sessionFactory.getCurrentSession().createQuery(
					"from Fee where academicYear =:academicYear and hierarchy.hid=:hierarchy and smartId in (:smartId) ");
			query.setParameter("hierarchy", hid);
			query.setParameterList("smartId", childList);
			query.setParameter("academicYear", academicYear);
			feeList = query.list();
			Loggers.loggerStart("query.list() : " + feeList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		}
		Loggers.loggerEnd();
		return feeList;
	}



}
