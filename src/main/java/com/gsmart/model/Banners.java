package com.gsmart.model;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.sql.rowset.serial.SerialBlob;

import org.hibernate.annotations.Type;


@Entity
@Table(name = "banners")
@IdClass(com.gsmart.model.CompoundBanner.class)
public class Banners {

	/**
	 * Title of Image
	 */
	
	@Column(name = "TITLE")
	private String title;

	/**
	 * Image
	 */
	@Lob
	@Column(name = "IMAGE",length = 400000)
	private byte[] image;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	@Id
	@Column(name = "ENTRY_TIME")
	private String entryTime;

	@Column(name = "EXIT_TIME")
	private String exitTime;

	@Column(name = "UPDATED_TIME")
	private String updatedTime;

	@Column(name = "IS_ACTIVE")
	private String isActive;

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "Banners [title=" + title + ", image=" + image + ", entryTime=" + entryTime + ", exitTime=" + exitTime
				+ ", updatedTime=" + updatedTime + ", isActive=" + isActive + "]";
	}

	

}