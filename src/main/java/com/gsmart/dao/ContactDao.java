package com.gsmart.dao;

import java.util.List;

import com.gsmart.model.MessageDetails;
import com.gsmart.util.GSmartDatabaseException;

public interface ContactDao {

	public boolean studentToTeacher(MessageDetails details) throws Exception;
	
	public List<MessageDetails> getList()throws GSmartDatabaseException;
	
	public List<MessageDetails> msgList(MessageDetails details) throws Exception;
	public List<MessageDetails> teacherView(MessageDetails details) throws Exception;
	public boolean teacherToStudent(MessageDetails details) throws Exception;
	public List<MessageDetails> studentView(MessageDetails details) throws Exception;
	public List<MessageDetails> viewAllMessages();
}
