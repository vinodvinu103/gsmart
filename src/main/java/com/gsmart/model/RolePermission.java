
package com.gsmart.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * class-name: RolePermission.java Assigning permission for everyone who
 * enrolled in the school from the principal to the students
 * 
 * @author :Nirmal Raj J
 * @version 1.0
 * @since 2016-08-01
 *
 */
@Entity
@Table(name = "PERMISSION_MASTER")
@IdClass(com.gsmart.model.RolePermissionCompound.class)
public class RolePermission implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * Its a time when the new permission instance is add or edit
	 */
	@Id
	@Column(name = "ENTRY_TIME")
	private String entryTime;
	/**
	 * Role of the person to access the permission of each module
	 */
	@Id
	@Column(name = "ROLE")
	private String role;
	/**
	 * Permission allowed module name
	 */
	@Id
	@Column(name = "MODULE_NAME")
	private String moduleName;
	/**
	 * Role of the person to access the add permission
	 */

	@Column(name = "SUB_MODULE_NAME")
	private String subModuleName;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="hid", insertable = false, updatable = false)
	private Hierarchy hierarchy;

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public String getSubModuleName() {
		return subModuleName;
	}

	public void setSubModuleName(String subModuleName) {
		this.subModuleName = subModuleName;
	}

	@Column(name = "P_ADD")
	private boolean add;
	/**
	 * Role of the person to access the view permission
	 */
	@Column(name = "P_VIEW")
	private boolean view;
	/**
	 * Role of the person to access the edit permission
	 */
	@Column(name = "P_EDIT")
	private boolean edit;
	/**
	 * Role of the person to access the delete permission
	 */
	@Column(name = "P_DEL")
	private boolean del;

	@Column(name = "UPDATED_TIME")
	private String updatedTime;

	@Column(name = "EXIT_TIME")
	private String exitTime;

	@Column(name = "IS_ACTIVE")
	private String isActive;

	@Transient
	private String icon;

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public boolean getAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public boolean getView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
	}

	public boolean getEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean getDel() {
		return del;
	}

	public void setDel(boolean del) {
		this.del = del;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	@Override
	public String toString() {
		return "\n RolePermission [\n{\t entryTime=" + entryTime + ", \n\trole=" + role + ", \n\tmoduleName="
				+ moduleName + ", \n\tsubModuleName=" + subModuleName + "\n}, \nadd=" + add + ",\nview=" + view
				+ ",\nedit=" + edit + ",\ndel=" + del + ",\nupdatedTime=" + updatedTime + ",\nexitTime=" + exitTime
				+ ",\nisActive=" + isActive + "]";
	}

}