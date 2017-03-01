package com.gsmart.dao;

import java.util.List;
import java.util.Map;

import com.gsmart.model.MessageDetails;
import com.gsmart.util.GSmartDatabaseException;

public interface ContactDao {

	public boolean studentToTeacher(MessageDetails details, String role) throws Exception;
	
	public List<MessageDetails> getList()throws GSmartDatabaseException;
	
	public List<MessageDetails> msgList(MessageDetails details) throws Exception;
//	public Map<String, Object> teacherView(MessageDetails details, Integer min, Integer max) throws Exception;
//	public Map<String, Object> studentView(MessageDetails details, Integer min, Integer max) throws Exception;
	public List<MessageDetails> viewAllMessages();

	public List<MessageDetails> teacherView(MessageDetails details) throws Exception;

	public boolean studentToTeacher(MessageDetails details) throws Exception;

	public boolean teacherToStudent(MessageDetails details) throws Exception;

	public List<MessageDetails> studentView(MessageDetails details) throws Exception;

	/*List<MessageDetails> teacherView(MessageDetails details) throws Exception;*/
	

}
