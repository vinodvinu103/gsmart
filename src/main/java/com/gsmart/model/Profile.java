package com.gsmart.model;

import java.util.Arrays;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PROFILE_MASTER")
public class Profile {

	@Id
	@Column(name = "SMART_ID")
	private String smartId;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "MIDDLE_NAME")
	private String middleName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "DOB")
	private Date dob;

	@Column(name = "GENDER")
	private String gender;
	
	@Column(name = "MARTIAL_STATUS")
	private String martialStatus;

	@Column(name = "BLOOD_GROUP")
	private String bloodGroup;

	@Column(name = "FATHER_NAME")
	private String fatherName;

	@Column(name = "MOTHER_NAME")
	private String motherName;

	@Column(name = "FATHER_OCCUPATION")
	private String fatherOccupation;

	@Column(name = "MOTHER_OCCUPATION")
	private String motherOccupation;

	@Column(name = "CASTE")
	private String caste;

	@Column(name = "RELIGION")
	private String religion;

	@Column(name = "NATIONALITY")
	private String nationality;

	@Column(name = "LANGUAGE_KNOWN")
	private String languageKnown;

	@Column(name = "INSTITUTION")
	private String institution;

	@Column(name = "SCHOOL")
	private String school;

	@Column(name = "BAND")
	private int band;

	@Column(name = "DESIGNATION")
	private String designation;

	@Column(name = "ROLE")
	private String role;

	@Column(name = "DEPT_NAME")
	private String deptName;

	@Column(name = "SUB_DEPT_NAME")
	private String subDeptName;

	@Column(name = "FATHER_INCOME")
	private int fatherIncome;

	@Column(name = "MOTHER_INCOME")
	private int motherIncome;

	@Column(name = "GUARDIAN_NAME")
	private String guardianName;

	@Column(name = "RELATION_WITH_GUARDIAN")
	private String relationWithGuardian;

	@Column(name = "FATHER_MOB_NUMBER")
	private String fatherMobNumber;

	@Column(name = "MOTHER_MOB_NUMBER")
	private String motherMobNumber;

	@Column(name = "TEACHER_ID")
	private String teacherId;
	
	@Column(name = "UPD_SMARTID")
	private String updSmartId;
	
	@Column(name = "IMAGE",columnDefinition="mediumblob")
	private byte[] image;
	
	// CONTACT DETAILS

	@Column(name = "EMAIL_ID")
	private String emailId;

	@Column(name = "MOBILE_NUMBER")
	private String mobileNumber;

	@Column(name = "EMERGENCY_CONTACT_PERSON")
	private String emergencyContactPerson;

	@Column(name = "EMERGENCY_CONTACT_NUMBER")
	private String emergencyContactNumber;

	@Column(name = "ADD_LINE1")
	private String addLine1;

	@Column(name = "ADD_LINE2")
	private String addLine2;

	@Column(name = "ADD_LINE3")
	private String addLine3;

	@Column(name = "STATE")
	private String state;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "CITY")
	private String city;

	@Column(name = "PINCODE")
	private Integer pinCode;

	// PASSPORT Details

	@Column(name = "PASSPORT_NUMBER")
	private String passportNumber;

	@Column(name = "PASSPORT_ISSUE_DATE")
	private Date passportIssueDate;

	@Column(name = "PASSPORT_EXPIRY_DATE")
	private Date passportExpiryDate;

	@Column(name = "ECNR_STATUS")
	private String ecnrStatus;

	@Column(name = "VISA_DETAILS")
	private String visaDetails;

	@Column(name = "PAN_NUMBER")
	private String panNumber;

	@Column(name = "AADHAR_NUMBER")
	private String aadharNumber;

	// REPORTING DETAILS
	@Column(name = "REPORTING_MANAGER_NAME")
	private String reportingManagerName;

	@Column(name = "REPORTING_MANAGER_ID")
	private String reportingManagerId;

	@Column(name = "COUNTER_SIGNING_MANAGER_NAME")
	private String counterSigningManagerName;

	@Column(name = "COUNTER_SIGNING_MANAGER_ID")
	private String counterSigningManagerId;

	@Column(name = "FUNCTIONAL_MANAGER")
	private String functionalManager;

	// EDUCATIONAL QUALIFICATION

	@Column(name = "QUALIFICATION")
	private String qualification;

	@Column(name = "CERTIFICATION_DETAILS")
	private String certificationDetails;

	@Column(name = "JOINING_DATE")
	private Date joiningDate;

	@Column(name = "DOMAIN")
	private String domain;

	@Column(name = "BILLING_DETAILS")
	private String billingDetails;

	@Column(name = "TEN_SCHOOL_NAME")
	private String tenSchoolName;

	@Column(name = "TEN_PERCENTAGE")
	private String tenPercentage;

	@Column(name = "TEN_PASS_YEAR")
	private String tenPassYear;

	@Column(name = "TWELVE_SCHOOL_NAME")
	private String twelveSchoolName;

	@Column(name = "TWELVE_PERCENTAGE")
	private String twelvePercentage;

	@Column(name = "TWELVE_PASS_YEAR")
	private String twelvePassYear;

	@Column(name = "UG_SCHOOL_NAME")
	private String ugSchoolName;

	@Column(name = "UG_PERCENTAGE")
	private String ugPercentage;

	@Column(name = "UG_PASS_YEAR")
	private String ugPassYear;

	@Column(name = "PG_SCHOOL_NAME")
	private String pgSchoolName;

	@Column(name = "PG_PERCENTAGE")
	private String pgPercentage;

	@Column(name = "PG_PASS_YEAR")
	private String pgPassYear;

	@Column(name = "EXPERIANCE")
	private String experiance;

	// Admission Details


	@Column(name = "SCHOOL_NAME")
	private String schoolName;

	@Column(name = "ACADEMIC_YEAR")
	private String academicYear;

	@Column(name = "STUDENT_ID")
	private String studentId;

	@Column(name = "CLASS")
	private String standard;

	@Column(name = "SECTION")
	private String section;

	@Column(name = "SYLLABUS")
	private String syllabus;

	@Column(name = "PREVIOUS_CLASS")
	private String previousClass;

	@Column(name = "PREVIOUS_SCHOOL")
	private String previousSchool;

	@Column(name = "PREVIOUS_YEAR")
	private String previousYear;
	
	@Column(name = "ENTRY_TIME")
	private String entryTime;
	
	@Column(name = "UPDATED_TIME")
	private String updatedTime;
	
	@Column(name = "EXIT_TIME")
	private String exitTime;
	
	@Column(name = "IS_ACTIVE")
	private String isActive;
	
	@Transient
	private boolean childFlag;
	
	@Transient
	private boolean parentFlag;
	

	@Transient
	private double totalAmount;
	
	
	@Transient
	private double paidAmount;
	
	
	@Transient
	private double balanceAmount;

	
	
	// -----------------------------------------------------------/

	
	
	public String getUpdSmartId() {
		return updSmartId;
	}


	public boolean isChildFlag() {
		return childFlag;
	}


	public void setChildFlag(boolean childFlag) {
		this.childFlag = childFlag;
	}


	public boolean isParentFlag() {
		return parentFlag;
	}


	public void setParentFlag(boolean parentFlag) {
		this.parentFlag = parentFlag;
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

	public void setUpdSmartId(String updSmartId) {
		this.updSmartId = updSmartId;
	}

	public String getSmartId() {
		return smartId;
	}

	public void setSmartId(String smartId) {
		this.smartId = smartId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMartialStatus() {
		return martialStatus;
	}


	public void setMartialStatus(String martialStatus) {
		this.martialStatus = martialStatus;
	}


	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getFatherOccupation() {
		return fatherOccupation;
	}

	public void setFatherOccupation(String fatherOccupation) {
		this.fatherOccupation = fatherOccupation;
	}

	public String getMotherOccupation() {
		return motherOccupation;
	}

	public void setMotherOccupation(String motherOccupation) {
		this.motherOccupation = motherOccupation;
	}

	public String getCaste() {
		return caste;
	}

	public void setCaste(String caste) {
		this.caste = caste;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getLanguageKnown() {
		return languageKnown;
	}

	public void setLanguageKnown(String languageKnown) {
		this.languageKnown = languageKnown;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
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

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getRole() {
		return role;
	}

	@Override
	public String toString() {
		return "Profile [smartId=" + smartId + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", dob=" + dob + ", gender=" + gender + ", martialStatus=" + martialStatus
				+ ", bloodGroup=" + bloodGroup + ", fatherName=" + fatherName + ", motherName=" + motherName
				+ ", fatherOccupation=" + fatherOccupation + ", motherOccupation=" + motherOccupation + ", caste="
				+ caste + ", religion=" + religion + ", nationality=" + nationality + ", languageKnown=" + languageKnown
				+ ", institution=" + institution + ", school=" + school + ", band=" + band + ", designation="
				+ designation + ", role=" + role + ", deptName=" + deptName + ", subDeptName=" + subDeptName
				+ ", fatherIncome=" + fatherIncome + ", motherIncome=" + motherIncome + ", guardianName=" + guardianName
				+ ", relationWithGuardian=" + relationWithGuardian + ", fatherMobNumber=" + fatherMobNumber
				+ ", motherMobNumber=" + motherMobNumber + ", teacherId=" + teacherId + ", updSmartId=" + updSmartId
				+ ", image=" + Arrays.toString(image) + ", emailId=" + emailId + ", mobileNumber=" + mobileNumber
				+ ", emergencyContactPerson=" + emergencyContactPerson + ", emergencyContactNumber="
				+ emergencyContactNumber + ", addLine1=" + addLine1 + ", addLine2=" + addLine2 + ", addLine3="
				+ addLine3 + ", state=" + state + ", country=" + country + ", city=" + city + ", pinCode=" + pinCode
				+ ", passportNumber=" + passportNumber + ", passportIssueDate=" + passportIssueDate
				+ ", passportExpiryDate=" + passportExpiryDate + ", ecnrStatus=" + ecnrStatus + ", visaDetails="
				+ visaDetails + ", panNumber=" + panNumber + ", aadharNumber=" + aadharNumber
				+ ", reportingManagerName=" + reportingManagerName + ", reportingManagerId=" + reportingManagerId
				+ ", counterSigningManagerName=" + counterSigningManagerName + ", counterSigningManagerId="
				+ counterSigningManagerId + ", functionalManager=" + functionalManager + ", qualification="
				+ qualification + ", certificationDetails=" + certificationDetails + ", joiningDate=" + joiningDate
				+ ", domain=" + domain + ", billingDetails=" + billingDetails + ", tenSchoolName=" + tenSchoolName
				+ ", tenPercentage=" + tenPercentage + ", tenPassYear=" + tenPassYear + ", twelveSchoolName="
				+ twelveSchoolName + ", twelvePercentage=" + twelvePercentage + ", twelvePassYear=" + twelvePassYear
				+ ", ugSchoolName=" + ugSchoolName + ", ugPercentage=" + ugPercentage + ", ugPassYear=" + ugPassYear
				+ ", pgSchoolName=" + pgSchoolName + ", pgPercentage=" + pgPercentage + ", pgPassYear=" + pgPassYear
				+ ", experiance=" + experiance + ", schoolName=" + schoolName + ", academicYear=" + academicYear
				+ ", studentId=" + studentId + ", standard=" + standard + ", section=" + section + ", syllabus="
				+ syllabus + ", previousClass=" + previousClass + ", previousSchool=" + previousSchool
				+ ", previousYear=" + previousYear + ", entryTime=" + entryTime + ", updatedTime=" + updatedTime
				+ ", exitTime=" + exitTime + ", isActive=" + isActive + ", childFlag=" + childFlag + ", parentFlag="
				+ parentFlag + ", totalAmount=" + totalAmount + ", paidAmount=" + paidAmount + ", balanceAmount="
				+ balanceAmount + "]";
	}


	public void setRole(String role) {
		this.role = role;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getSubDeptName() {
		return subDeptName;
	}

	public void setSubDeptName(String subDeptName) {
		this.subDeptName = subDeptName;
	}

	public int getFatherIncome() {
		return fatherIncome;
	}

	public void setFatherIncome(int fatherIncome) {
		this.fatherIncome = fatherIncome;
	}

	public int getMotherIncome() {
		return motherIncome;
	}

	public void setMotherIncome(int motherIncome) {
		this.motherIncome = motherIncome;
	}

	public String getGuardianName() {
		return guardianName;
	}

	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}

	public String getRelationWithGuardian() {
		return relationWithGuardian;
	}

	public void setRelationWithGuardian(String relationWithGuardian) {
		this.relationWithGuardian = relationWithGuardian;
	}

	public String getFatherMobNumber() {
		return fatherMobNumber;
	}

	public void setFatherMobNumber(String fatherMobNumber) {
		this.fatherMobNumber = fatherMobNumber;
	}

	public String getMotherMobNumber() {
		return motherMobNumber;
	}

	public void setMotherMobNumber(String motherMobNumber) {
		this.motherMobNumber = motherMobNumber;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmergencyContactPerson() {
		return emergencyContactPerson;
	}

	public void setEmergencyContactPerson(String emergencyContactPerson) {
		this.emergencyContactPerson = emergencyContactPerson;
	}

	public String getEmergencyContactNumber() {
		return emergencyContactNumber;
	}

	public void setEmergencyContactNumber(String emergencyContactNumber) {
		this.emergencyContactNumber = emergencyContactNumber;
	}

	public String getAddLine1() {
		return addLine1;
	}

	public void setAddLine1(String addLine1) {
		this.addLine1 = addLine1;
	}

	public String getAddLine2() {
		return addLine2;
	}

	public void setAddLine2(String addLine2) {
		this.addLine2 = addLine2;
	}

	public String getAddLine3() {
		return addLine3;
	}

	public void setAddLine3(String addLine3) {
		this.addLine3 = addLine3;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getPinCode() {
		return pinCode;
	}

	public void setPinCode(Integer pinCode) {
		this.pinCode = pinCode;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}



	
	public String getEcnrStatus() {
		return ecnrStatus;
	}


	public void setEcnrStatus(String ecnrStatus) {
		this.ecnrStatus = ecnrStatus;
	}


	public String getVisaDetails() {
		return visaDetails;
	}

	public void setVisaDetails(String visaDetails) {
		this.visaDetails = visaDetails;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getReportingManagerName() {
		return reportingManagerName;
	}

	public void setReportingManagerName(String reportingManagerName) {
		this.reportingManagerName = reportingManagerName;
	}

	public String getReportingManagerId() {
		return reportingManagerId;
	}

	public void setReportingManagerId(String reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}

	public String getCounterSigningManagerName() {
		return counterSigningManagerName;
	}

	public void setCounterSigningManagerName(String counterSigningManagerName) {
		this.counterSigningManagerName = counterSigningManagerName;
	}

	public String getCounterSigningManagerId() {
		return counterSigningManagerId;
	}

	public void setCounterSigningManagerId(String counterSigningManagerId) {
		this.counterSigningManagerId = counterSigningManagerId;
	}

	public String getFunctionalManager() {
		return functionalManager;
	}

	public void setFunctionalManager(String functionalManager) {
		this.functionalManager = functionalManager;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getCertificationDetails() {
		return certificationDetails;
	}

	public void setCertificationDetails(String certificationDetails) {
		this.certificationDetails = certificationDetails;
	}

	public Date getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getBillingDetails() {
		return billingDetails;
	}

	public void setBillingDetails(String billingDetails) {
		this.billingDetails = billingDetails;
	}

	public String getTenSchoolName() {
		return tenSchoolName;
	}

	public void setTenSchoolName(String tenSchoolName) {
		this.tenSchoolName = tenSchoolName;
	}

	public String getTenPercentage() {
		return tenPercentage;
	}

	public void setTenPercentage(String tenPercentage) {
		this.tenPercentage = tenPercentage;
	}

	public String getTenPassYear() {
		return tenPassYear;
	}

	public void setTenPassYear(String tenPassYear) {
		this.tenPassYear = tenPassYear;
	}

	public String getTwelveSchoolName() {
		return twelveSchoolName;
	}

	public void setTwelveSchoolName(String twelveSchoolName) {
		this.twelveSchoolName = twelveSchoolName;
	}

	public String getTwelvePercentage() {
		return twelvePercentage;
	}

	public void setTwelvePercentage(String twelvePercentage) {
		this.twelvePercentage = twelvePercentage;
	}

	public String getTwelvePassYear() {
		return twelvePassYear;
	}

	public void setTwelvePassYear(String twelvePassYear) {
		this.twelvePassYear = twelvePassYear;
	}



	public String getUgSchoolName() {
		return ugSchoolName;
	}


	public void setUgSchoolName(String ugSchoolName) {
		this.ugSchoolName = ugSchoolName;
	}


	public String getUgPercentage() {
		return ugPercentage;
	}


	public void setUgPercentage(String ugPercentage) {
		this.ugPercentage = ugPercentage;
	}


	public String getUgPassYear() {
		return ugPassYear;
	}


	public void setUgPassYear(String ugPassYear) {
		this.ugPassYear = ugPassYear;
	}


	public String getPgSchoolName() {
		return pgSchoolName;
	}


	public void setPgSchoolName(String pgSchoolName) {
		this.pgSchoolName = pgSchoolName;
	}


	public String getPgPercentage() {
		return pgPercentage;
	}


	public void setPgPercentage(String pgPercentage) {
		this.pgPercentage = pgPercentage;
	}


	public String getPgPassYear() {
		return pgPassYear;
	}


	public void setPgPassYear(String pgPassYear) {
		this.pgPassYear = pgPassYear;
	}


	public String getExperiance() {
		return experiance;
	}

	public void setExperiance(String experiance) {
		this.experiance = experiance;
	}


	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStandard() {
		return standard;
	}


	public void setStandard(String standard) {
		this.standard = standard;
	}


	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getSyllabus() {
		return syllabus;
	}

	public void setSyllabus(String syllabus) {
		this.syllabus = syllabus;
	}

	public String getPreviousClass() {
		return previousClass;
	}

	public void setPreviousClass(String previousClass) {
		this.previousClass = previousClass;
	}

	public String getPreviousSchool() {
		return previousSchool;
	}

	public void setPreviousSchool(String previousSchool) {
		this.previousSchool = previousSchool;
	}

	public String getPreviousYear() {
		return previousYear;
	}

	public void setPreviousYear(String previousYear) {
		this.previousYear = previousYear;
	}


	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}


	public Date getPassportIssueDate() {
		return passportIssueDate;
	}


	public void setPassportIssueDate(Date passportIssueDate) {
		this.passportIssueDate = passportIssueDate;
	}


	public Date getPassportExpiryDate() {
		return passportExpiryDate;
	}


	public void setPassportExpiryDate(Date passportExpiryDate) {
		this.passportExpiryDate = passportExpiryDate;
	}


	public double getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}


	public double getPaidAmount() {
		return paidAmount;
	}


	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}


	public double getBalanceAmount() {
		return balanceAmount;
	}


	public void setBalanceAmount(double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

}