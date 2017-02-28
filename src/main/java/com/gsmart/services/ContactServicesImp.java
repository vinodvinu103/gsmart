package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsmart.dao.ContactDao;
import com.gsmart.model.MessageDetails;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
public class ContactServicesImp implements ContactServices {

	
	@Autowired
	ContactDao dao;

	public boolean studentToTeacher(MessageDetails details) throws Exception {
		return dao.studentToTeacher(details);	
	}
	
	@Override
	public List<MessageDetails> msgList(MessageDetails details) throws Exception {
		
		return dao.msgList(details);
	}
	
	@Override
	public Map<String, Object> teacherView(MessageDetails details, Integer min, Integer max) throws Exception {
		return dao.teacherView(details, min, max);
	}

	@Override
	public boolean teacherToStudent(MessageDetails details) throws Exception {
		
		return dao.teacherToStudent(details);
	}

	@Override
	public Map<String, Object> studentView(MessageDetails details, Integer min, Integer max) throws Exception {
		
		return dao.studentView(details, min, max);
	}

	@Override
	public List<MessageDetails> viewAllMessages() throws Exception {
		return dao.viewAllMessages();
	}
	
	@Override
	public List<MessageDetails> getData() throws GSmartServiceException {
		Loggers.loggerStart();
		List<MessageDetails> list=null;
		try {
			list=dao.getList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean studentToTeacher(MessageDetails details, String role) throws Exception {
		
		return dao.studentToTeacher(details,role);
	}
	
}