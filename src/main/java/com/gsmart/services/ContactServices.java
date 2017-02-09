package com.gsmart.services;

import java.util.List;

import com.gsmart.model.MessageDetails;
import com.gsmart.util.GSmartServiceException;

public interface ContactServices 
{

	public boolean studentToTeacher(MessageDetails details) throws Exception;
	
	public List<MessageDetails> getData()throws GSmartServiceException;
	
	public List<MessageDetails> msgList(MessageDetails details) throws Exception;
	public List<MessageDetails> teacherView(MessageDetails details) throws Exception;
	public boolean teacherToStudent(MessageDetails details) throws Exception;
	public List<MessageDetails> studentView(MessageDetails details) throws Exception;
	public List<MessageDetails> viewAllMessages() throws Exception;
}
