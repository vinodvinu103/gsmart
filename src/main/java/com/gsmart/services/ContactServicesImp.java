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

	@Override
	public List<MessageDetails> msgList(MessageDetails details) throws Exception {
		
		return dao.msgList(details);
	}
	
	@Override
	public Map<String, Object> teacherView(MessageDetails details, Integer min, Integer max) throws Exception {
		return dao.teacherView(details, min, max);
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
	
	@Override
	public Map<String, Object> studentChat(MessageDetails details){
		
		Map<String, Object> chat= null;
		try {
			return dao.studentChat(details);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chat;
	}
	
	@Override
	public Map<String, Object> teacherChat(MessageDetails details){
		
		Map<String, Object> chat1= null;
		try {
			return dao.studentChat(details);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chat1;
	}
	
}