package com.gsmart.model;

import java.util.List;
import java.util.Map;

public class SyncRequestObject {
	
	private Map<String, List<Attendance>> requestMap;
	
	public Map<String, List<Attendance>> getRequestMap() {
		return requestMap;
	}

	public void setRequestMap(Map<String, List<Attendance>> requestMap) {
		this.requestMap = requestMap;
	}

}
