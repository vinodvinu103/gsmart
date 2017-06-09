package com.gsmart.services;

import java.util.List;
import java.util.Map;

import com.gsmart.model.MessageDetails;
import com.gsmart.model.Token;
import com.gsmart.util.GSmartServiceException;

public interface ContactServices   {
	
	public boolean studentToTeacher(MessageDetails details, String role) throws Exception;
	
	public MessageDetails teacherToStudent(MessageDetails details, String role) throws Exception;
	
	public List<MessageDetails> getData()throws GSmartServiceException;
	
	public List<MessageDetails> msgList(MessageDetails details) throws Exception;
	
	public Map<String, Object> studentView(Token details, Integer min, Integer max) throws Exception;
	
	public List<MessageDetails> viewAllMessages() throws Exception;

	public Map<String, Object> teacherView(Token details, Integer min, Integer max) throws Exception;

	public Map<String, Object> studentChat(MessageDetails details) throws Exception;

	public Map<String, Object> teacherChat(MessageDetails details) throws Exception;

	public void updateStatus(Token tk1, Long hid, String smartId) throws Exception;

}
