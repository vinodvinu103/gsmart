package com.gsmart.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.dao.ContactDao;
import com.gsmart.model.Hierarchy;
import com.gsmart.model.MessageDetails;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.Loggers;

@Service
@Transactional
public class ContactServicesImp implements ContactServices {

	
	@Autowired
	private ContactDao dao;

	@Override
	public List<MessageDetails> msgList(MessageDetails details) throws Exception {
		
		return dao.msgList(details);
	}
	
	@Override
	public Map<String, Object> teacherView(Token tk1, Integer min, Integer max) throws Exception {
		return dao.teacherView(tk1, min, max);
	}

	@Override
	public Map<String, Object> studentView(Token tk1, Integer min, Integer max) throws Exception {
		
		return dao.studentView(tk1, min, max);
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
	public boolean teacherToStudent(MessageDetails details, String role) throws Exception {
		return dao.teacherToStudent(details,role);
	}
	
	@Override
	public Map<String, Object> teacherChat(MessageDetails details) throws Exception {
		return dao.teacherChat(details);
	}

	@Override
	public Map<String, Object> studentChat(MessageDetails details) throws Exception {
		return dao.studentChat(details);
	}

	@Override
	public void updateStatus(Token tk1, Long hid, String smartId) throws Exception {
		dao.updateStatus(tk1, hid, smartId);
	}

}