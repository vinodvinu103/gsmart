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
import com.gsmart.model.TransportationFee;
import com.gsmart.util.CalendarCalculator;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartDatabaseException;
import com.gsmart.util.Loggers;
@Repository
@Transactional
public class TranspotationFeeDaoImpl implements TranspotationFeeDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	Query query;
	Criteria criteria = null;
	
	@Override
	public TransportationFee addTranspotationFee(TransportationFee fee) throws GSmartDatabaseException {
	
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		TransportationFee cb=null;
		try{
	         query=sessionFactory.getCurrentSession().createQuery("from TransportationFee where smartId=:smartId    and isActive=:isActive");
			 query.setParameter("isActive", "Y");
			 query.setParameter("smartId", fee.getSmartId());
			TransportationFee	feedata=(TransportationFee) query.uniqueResult();
			if (feedata==null) {
				fee.setFeeStatus("unpaid");
				fee.setPaidFee(0);
				fee.setFeeType("transpotationFee");
			
				fee.setBalanceFee(fee.getTotalTransportationFee());
				fee.setEntryTime(CalendarCalculator.getTimeStamp());
				fee.setDate(CalendarCalculator.getTimeStamp());
				Profile profile = getReportingManagerId(fee.getSmartId());
				fee.setReportingManagerId(profile.getReportingManagerId());
				fee.setParentName(profile.getFatherName());
				fee.setName(profile.getFirstName());
				fee.setStandard(profile.getStandard());
				fee.setSection(profile.getSection());
				fee.setIsActive("Y");
				session.save(fee);
			
			}
			else{
				return cb;
			}
			
		} catch (ConstraintViolationException e) {
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} 
		Loggers.loggerEnd();
		return 	fee;
	}

	public Profile getReportingManagerId(String smartId) {
		Loggers.loggerStart();
		query = sessionFactory.getCurrentSession().createQuery("from Profile where smartId=:smartId");
		query.setParameter("smartId", smartId);
		Profile profileList = (Profile) query.uniqueResult();

		return profileList;

	}

	@Override
	public void editTranspotationFee(TransportationFee transpotationfee, String schoolname) throws GSmartDatabaseException {
		
		Loggers.loggerStart();
		Session session=this.sessionFactory.getCurrentSession();
		try {
	
			TransportationFee oldTranspotationFee = getTranspotationFee(transpotationfee.getEntryTime(),transpotationfee.getHierarchy());
			oldTranspotationFee.setUpdatedTime(CalendarCalculator.getTimeStamp());
			//System.out.println(oldFee);
			oldTranspotationFee.setIsActive("N");
			//System.out.println("--------------------"+oldFee);
			session.update(oldTranspotationFee);
            String invoicenumber=getinvoice(transpotationfee.getHierarchy(),schoolname);
            transpotationfee.setInVoice(invoicenumber);

            addTranspotationFeelist(transpotationfee);

		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GSmartDatabaseException(e.getMessage());
		} finally {
			// session.close();
		}
		Loggers.loggerEnd();
	}
	
	
	public void addTranspotationFeelist(TransportationFee transpotationfee) throws GSmartDatabaseException {
		Session session=this.sessionFactory.getCurrentSession();
		try{
		  	if(transpotationfee.getPaidFee()>0)
			{
				transpotationfee.setBalanceFee(transpotationfee.getBalanceFee()-transpotationfee.getPaidFee());
				if(transpotationfee.getBalanceFee()<=0){
					Loggers.loggerStart("inside the paid");
					transpotationfee.setFeeStatus("paid");
				}else{
					transpotationfee.setFeeStatus("unpaid");
					
				}
				
			}else
			{  
				transpotationfee.setFeeStatus("unpaid");
				transpotationfee.setFeeType("transpotationFee");
				transpotationfee.setBalanceFee(transpotationfee.getTotalTransportationFee());
			}
		  	transpotationfee.setEntryTime(CalendarCalculator.getTimeStamp());
		  	transpotationfee.setDate(CalendarCalculator.getTimeStamp());
			Profile profile = getReportingManagerId(transpotationfee.getSmartId());
			transpotationfee.setReportingManagerId(profile.getReportingManagerId());
			transpotationfee.setParentName(profile.getFatherName());
			transpotationfee.setIsActive("Y");
			session.save(transpotationfee);
		}
		 catch (ConstraintViolationException e) {
				throw new GSmartDatabaseException(Constants.CONSTRAINT_VIOLATION);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GSmartDatabaseException(e.getMessage());
			} 
			Loggers.loggerEnd();
		
	}


	String year = (Year.now().getValue())+"-"+(Year.now().getValue()+1);

	  public String getinvoice(Hierarchy hierarchy,String schoolname) {
	  	Loggers.loggerStart();
	  	try {
	  	query = sessionFactory.getCurrentSession()
	  			.createQuery("select inVoice from TransportationFee where inVoice!='null' and hierarchy.hid=:hierarchy");
	  	
	  	query.setParameter("hierarchy", hierarchy.getHid());
	  	ArrayList<String> number = (ArrayList<String>) query.list();
	  	if (!number.isEmpty()) {
	  		Loggers.loggerStart("NEW INVOICE CREATING -----");
	  		String oldinvoice=getRecentInvioceNumber(hierarchy);
	  		System.out.println(oldinvoice);
	  		String[] temp = oldinvoice.split("-");
	  		
	  		String newinvoice = temp[0]+"-"+temp[1]+"-"+temp[2]+"-"+temp[3]+"-"+temp[4]+"-"+String.valueOf(Integer.parseInt(temp[5])+1);
	  		System.out.println(newinvoice);
	  		return newinvoice;
	  		
	  	} else {
	  		Loggers.loggerStart("FRIST INVIOCE NUMBER------");
	  		String invoicenumber=schoolname+"-"+"TRP"+"-"+year+"-"+1;
	  		
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
	  			.createQuery("select inVoice from TransportationFee where  (entryTime in (select max(entryTime) from TransportationFee where inVoice!='null' and hierarchy.hid=:hierarchy ))");
	  	query.setParameter("hierarchy", hierarchy.getHid());
	  	Loggers.loggerEnd(hierarchy.getHid());
	  	String lastinvoice = (String)query.uniqueResult();
	  	
	  	
	  	return lastinvoice;
	  }
	  public TransportationFee getTranspotationFee(String entryTime,Hierarchy hierarchy) {
			
			Loggers.loggerStart(entryTime);
			query = sessionFactory.getCurrentSession().createQuery("from TransportationFee where isActive=:isActive and entryTime =:entryTime and hierarchy.hid=:hierarchy");
			query.setParameter("hierarchy", hierarchy.getHid());
			query.setParameter("isActive", "Y");
			query.setParameter("entryTime", entryTime);
			TransportationFee transportationfee = (TransportationFee) query.uniqueResult();

			Loggers.loggerValue("feeList", transportationfee);
			return transportationfee;

		}

	@Override
	public Map<String, Object> getPaidStudentsList(Long hid, Integer min, Integer max) throws GSmartDatabaseException {
		
       Loggers.loggerStart();
		
		List<Fee> paidStudentsList=null;
		Map<String, Object> paidfeeMap = new HashMap<String, Object>();
		try
		{
	       criteria = sessionFactory.getCurrentSession().createCriteria(TransportationFee.class);
		   criteria.setMaxResults(max);
		   criteria.setFirstResult(min);
		   criteria.addOrder(Order.asc("smartId"));
		   criteria.add(Restrictions.eq("isActive", "Y"));
	       criteria.add(Restrictions.eq("hierarchy.hid", hid));
	       criteria.add(Restrictions.eq("feeStatus", "paid"));
	     paidStudentsList=criteria.list();
	 
		 Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(TransportationFee.class);
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

	@Override
	public Map<String, Object> getUnpaidStudentsList(Long hid, Integer min, Integer max)
			throws GSmartDatabaseException {
		
		Loggers.loggerStart();
		List<Fee> unpaidStudentsList = null;
		Map<String, Object> unpaidfeeMap = new HashMap<String, Object>();
		try {
		criteria = sessionFactory.getCurrentSession().createCriteria(TransportationFee.class);
		criteria.setMaxResults(max);
		criteria.setFirstResult(min);
		criteria.addOrder(Order.asc("smartId"));
		 criteria.add(Restrictions.eq("isActive", "Y"));
	     criteria.add(Restrictions.eq("hierarchy.hid", hid));
	     criteria.add(Restrictions.eq("feeStatus", "unpaid"));
		unpaidStudentsList = criteria.list();
		
	    Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(TransportationFee.class);
	     criteriaCount.setProjection(Projections.rowCount());
	     criteriaCount.add(Restrictions.eq("isActive", "Y"));
	     criteriaCount.add(Restrictions.eq("hierarchy.hid", hid));
	     criteriaCount.add(Restrictions.eq("feeStatus", "unpaid"));

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
	public ArrayList<TransportationFee> getTransportationFeeList(TransportationFee transportationFee, Long hid)
			throws GSmartDatabaseException {
		
		Loggers.loggerStart(transportationFee.getAcademicYear());
		Loggers.loggerStart(hid);
		ArrayList<TransportationFee> feeList;
		try {
			

				query = sessionFactory.getCurrentSession().createQuery(
						"from TransportationFee where smartId =:smartId and academicYear =:academicYear and paidFee>'0' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hid);
			
			query.setParameter("smartId", transportationFee.getSmartId());
			query.setParameter("academicYear", transportationFee.getAcademicYear());
			feeList = (ArrayList<TransportationFee>) query.list();
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} 
			
			 
		Loggers.loggerEnd(feeList);
		return feeList;
	}

	@Override
	public ArrayList<TransportationFee> getStudentUnpaidFeeList(TransportationFee transportationFee, Long hid)
			throws GSmartDatabaseException {
		
		Loggers.loggerStart(transportationFee.getAcademicYear());
		Loggers.loggerStart(hid);
		ArrayList<TransportationFee> StudentUnpaidfeeList;
		try {
			

				query = sessionFactory.getCurrentSession().createQuery(
						"from TransportationFee where smartId =:smartId and academicYear =:academicYear and isActive='Y' and paidFee='0' and hierarchy.hid=:hierarchy");
				query.setParameter("hierarchy", hid);
		
			    query.setParameter("smartId", transportationFee.getSmartId());
			    query.setParameter("academicYear", transportationFee.getAcademicYear());
			   StudentUnpaidfeeList = (ArrayList<TransportationFee>) query.list();
		} catch (Exception e) {
			throw new GSmartDatabaseException(e.getMessage());
		} 
			
			 
		Loggers.loggerEnd(StudentUnpaidfeeList);
		return StudentUnpaidfeeList;
	}

	@Override
	public List<TransportationFee> searchpaidtrans(TransportationFee trans, Long hid) throws GSmartDatabaseException {
		List<TransportationFee> list = null;
		try{
			query = sessionFactory.getCurrentSession().createQuery("from TransportationFee where isActive = 'Y' and hierarchy.hid=:hierarchy and name like '%"+trans.getName()+"%' and feeStatus='paid'");
			query.setParameter("hierarchy", hid);
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<TransportationFee> searchunpaidtrans(TransportationFee trans, Long hid) throws GSmartDatabaseException {
		List<TransportationFee> list = null;
		try{
			query = sessionFactory.getCurrentSession().createQuery("from TransportationFee where isActive = 'Y' and hierarchy.hid=:hierarchy and name like '%"+trans.getName()+"%' and feeStatus='unpaid'");
			query.setParameter("hierarchy", hid);
			list = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

}
