package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CompoundBanner implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private int imageId;
	private String entryTime;
	
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	
	
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	

}
