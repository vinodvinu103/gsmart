package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gsmart.model.TransportationFee;
import com.gsmart.util.GSmartDatabaseException;

public interface TranspotationFeeDao {

public	TransportationFee addTranspotationFee(TransportationFee taranspotationfee) throws GSmartDatabaseException;

public void editTranspotationFee(TransportationFee transpotationfee, String schoolname) throws GSmartDatabaseException;

public Map<String, Object> getPaidStudentsList(Long hid, Integer min, Integer max) throws GSmartDatabaseException;

public Map<String, Object> getUnpaidStudentsList(Long hid, Integer min, Integer max) throws GSmartDatabaseException;

public ArrayList<TransportationFee> getTransportationFeeList(TransportationFee transportationFee, Long hid)throws GSmartDatabaseException;

public ArrayList<TransportationFee> getStudentUnpaidFeeList(TransportationFee transportationFee, Long hid)throws GSmartDatabaseException;

public List<TransportationFee> searchpaidtrans(TransportationFee trans, Long hid)throws GSmartDatabaseException;

public List<TransportationFee> searchunpaidtrans(TransportationFee trans, Long hid)throws GSmartDatabaseException;

}
