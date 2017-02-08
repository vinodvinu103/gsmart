package com.gsmart.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Search implements Serializable {

	String name;

	String school;
	
	int band;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public int getBand() {
		return band;
	}

	public void setBand(int band) {
		this.band = band;
	}

}
