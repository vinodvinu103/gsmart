package com.gsmart.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;




@Entity
@Table(name = "banners")
@IdClass(com.gsmart.model.CompoundBanner.class)
public class Banners {
	/**
	 * Image
	 */
	@Lob
	@Column(name = "IMAGE",length = 400000)
//	@Index(name = "image")
	private byte[] image;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int imageId;
	
	@Id
	@Column(name = "ENTRY_TIME")
	private String entryTime;

	@Column(name = "EXIT_TIME")
	private String exitTime;

	@Column(name = "UPDATED_TIME")
	private String updatedTime;

	@Column(name = "IS_ACTIVE")
//	@Index(name = "isActive")
	private String isActive;

	/*public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}*/

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	

	
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


}