package com.gsmart.services;

import java.util.ArrayList;
import java.util.Map;

import com.gsmart.model.TransportationFee;
import com.gsmart.util.GSmartServiceException;

public  interface TranspotationFeeservice
{

public	TransportationFee addTranspotationFee(TransportationFee taranspotationfee) throws GSmartServiceException;

public void editTranspotationFee(TransportationFee transpotationfee)throws GSmartServiceException;

public void deleteFee(TransportationFee transpotationfee) throws GSmartServiceException;

public Map<String, Object> getPaidStudentsList(Long hid, Integer min, Integer max)throws GSmartServiceException;

public Map<String, Object> getUnpaidStudentsList(Long hid, Integer min, Integer max) throws GSmartServiceException;

public ArrayList<TransportationFee> getTransportationFeeList(TransportationFee transportationFee, Long hid)throws GSmartServiceException;

public ArrayList<TransportationFee> getStudentUnpaidFeeList(TransportationFee transportationFee, Long hid)throws GSmartServiceException;







}
