package com.gsmart.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gsmart.model.MessageDetails;
import com.gsmart.util.GSmartDatabaseException;

public interface ContactDao {

	public boolean studentToTeacher(MessageDetails details, String role) throws Exception;
	
	public List<MessageDetails> getList()throws GSmartDatabaseException;
	
	public List<MessageDetails> msgList(MessageDetails details) throws Exception;
	
	public Map<String, Object> teacherView(MessageDetails details, Integer min, Integer max) throws Exception;
	
	public Map<String, Object> studentView(MessageDetails details, Integer min, Integer max) throws Exception;
	
	public List<MessageDetails> viewAllMessages();

	public Map<String, Object> studentChat(MessageDetails details) throws Exception;	

	public Map<String, Object> teacherChat(MessageDetails details) throws Exception;

//	public boolean teacherToStudent(MessageDetails details);	

}
