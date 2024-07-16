package com.hkmci.csdkms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Holfer ZHANG
 * @Description
 * @project springboot_jpa
 * @package com.hkmci.entity
 * @email holfer.zhang@hkmci.com
 * @date 2019/03/04
 */

@Entity
@Table(name = "user")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;

	//@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "username", nullable = true, length = 50)
	private String username;
	
	@JsonIgnore
	@Column(name = "password", nullable = true, length = 50)
	private String password;
	
	@Column(name = "fullname", nullable = true, length = 50)
	private String fullname;
	
	@JsonIgnore
	@Column(name = "is_deleted", nullable = true, length = 3)
	private Integer isDeleted;
	
	//@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss a",locale="zh",timezone="GMT+8")
	@JsonIgnore
//	@JsonInclude(Include.NON_NULL)
	@Column(name = "email", nullable = true, length = 50)
	private String email;
	
//	@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "usergroup", nullable = false)
	private Long usergroup;

	
	@JsonIgnore
	@Column(name = "last_ip", nullable = true)
	private String lastIp;
	
	@JsonIgnore
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "password_last_change", nullable = true)
	private Date passwordLastChange;
	
	@JsonInclude(Include.NON_NULL)
	@Column(name = "login_tries", nullable = true)
	private Long loginTries;
	
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@JsonInclude(Include.NON_NULL)
	@Column(name = "login_last_try", nullable = true)
	private Date loginLastTry;
	
	@JsonIgnore
	@Column(name = "approved", nullable = true)
	private Long approved;
	
	//@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "lang", nullable = true)
	private String lang;
	
	@JsonIgnore
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "created_at", nullable = false)
	private Date createdAt;
	
	@JsonInclude(Include.NON_NULL)
	@Column(name = "staff_no", nullable = false)
	private String staffNo;
	
	//@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "substantive_rank", nullable = false)
	private String substantiveRank;
	
	//@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "active_rank", nullable = false)
	private String activeRank;
	
	@JsonInclude(Include.NON_NULL)
	@Column(name = "chinese_name", nullable = true)
	private String chineseName;
	
	//@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "inst_t", nullable = false)
	private String instT;
	
	//@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "section_t", nullable = false)
	private String sectionT;
	
	//@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "duties_t", nullable = false)
	private String dutiesT;
	
	//@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "inst_x", nullable = false)
	private String instX;
	
	//@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "section_x", nullable = false)
	private String sectionX;
	
	//@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "duties_x", nullable = false)
	private String dutiesX;
	
	//@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "sex", nullable = false)
	private String sex;
	
	//@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "notes_account", nullable = false)
	private String notesAccount;
	
	@JsonIgnore
	@Column(name = "rank_id", nullable = false)
	private Long rankId;
	
	@Transient 
	private String rank;
	
	@Transient 
	private Integer ifAccess;
	
	
	@JsonIgnore
	@Column(name = "institution_id", nullable = false)
	private Long institutionId;
	
	@Transient 
	private String institution;
	
	
	@JsonIgnore
	@Column(name = "section_id", nullable = false)
	private Long sectionId;
	
	@Transient 
	private String section;
	
	
	
	
	
	@JsonInclude(Include.NON_NULL)
	private Integer allowUpload;
	

	
	
	@JsonIgnore
	@OneToMany(mappedBy = "userId",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@Where(clause = "is_deleted = 0")
	@OrderBy("id asc")
	//@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<UserAccessRule> accessRule;//AccessRuleList
	
	@JsonInclude(Include.NON_NULL)
	@Transient 
	private List<Long> accessRuleList;
	
	@Transient
	public List<Long> getAccessRuleList() {
		return accessRuleList;
	}
	public void setAccessRuleList(List<Long> accessRuleList) {
		this.accessRuleList = accessRuleList;
	}
	
	public List<UserAccessRule> getAccessRule() {
		return accessRule;
	}
	public void setAccessRule(List<UserAccessRule> accessRule) {
		this.accessRule = accessRule;
	}

	@JsonIgnore
	@Column(name = "inst_r", nullable = false)
	private String instR;
	
	@JsonIgnore
	@Column(name = "section_r", nullable = false)
	private String sectionR;
	
	@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "last_sync", nullable = false)
	private Date lastSync;
	
	

	@JsonInclude(Include.NON_NULL)
	@Column(name = "alias", nullable = false)
	private String alias;
	

//	@JsonInclude(Include.NON_NULL)
	@Column(name = "profile_photo", nullable = false)
	private String profilePhoto;
	

//	@JsonInclude(Include.NON_NULL)
	@Column(name = "alias_photo", nullable = false, length = 200)
	private String aliasPhoto;
	
	
	@JsonInclude(Include.NON_NULL)
	@Column(name="alias_photo_is_pending", nullable = false)
	private String aliasPhotoIsPending;
	
	
	
	@JsonInclude(Include.NON_NULL)
	@Column(name="profile_photo_is_pending", nullable =false)
	private String profilePhotoIsPending;
	

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonIgnore
	@Column(name = "alias_photo_pending_date", nullable = true)
	private Date aliasPhotoPendingDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonIgnore
	@Column(name = "profile_photo_pending_date", nullable =true)
	private Date profilePhotoPendingDate;
	
	
	
	
	
	@Column(name="user_score_level")
	private Integer userScoreLevel;
	
	@JsonIgnore
	@Column(name = "blog_create", nullable = false, length = 1)
	private String blogCreate;
	
	@Transient 
	@JsonInclude(Include.NON_NULL)
	private Integer isBlogger;
	
	@JsonIgnore
	@JsonInclude(Include.NON_NULL)
	@Column(name = "post_create", nullable = false, length = 1)
	private String post_create;
	


	@JsonInclude(Include.NON_NULL)
	@Column(name = "score", nullable = false)
	private Integer score;
	
	@JsonInclude(Include.NON_NULL)
	@Column(name = "today_score", nullable = false)
	private Integer todayScore;
	
	@JsonIgnore
	@Column(name = "login_fail_attempt", nullable = true)
	private Long loginFailAttempt;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonIgnore
	@Column(name = "login_fail_at", nullable = true)
	private Date loginFailAt;
	
	
//	@JsonInclude(Include.NON_NULL)
	@Transient 
	private List<User> assistant;
	
	@Transient
	public List<User> getAssistant() {
		return assistant;
	}
	public void setAssistant(List<User> assistant) {
		this.assistant = assistant;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getifAccess() {
		return ifAccess;
	}
	public void setifAccess(Integer ifAccess) {
		this.ifAccess = ifAccess;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getUsergroup() {
		return usergroup;
	}
	public void setUsergroup(Long usergroup) {
		this.usergroup = usergroup;
	}
	public String getLastIp() {
		return lastIp;
	}
	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}
	public Date getPasswordLastChange() {
		return passwordLastChange;
	}
	public void setPasswordLastChange(Date passwordLastChange) {
		this.passwordLastChange = passwordLastChange;
	}
	public Long getLoginTries() {
		return loginTries;
	}
	public void setLoginTries(Long loginTries) {
		this.loginTries = loginTries;
	}
	public Date getLoginLastTry() {
		return loginLastTry;
	}
	public void setLoginLastTry(Date loginLastTry) {
		this.loginLastTry = loginLastTry;
	}
	public Long getApproved() {
		return approved;
	}
	public void setApproved(Long approved) {
		this.approved = approved;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getStaffNo() {
		return staffNo;
	}
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	public String getSubstantiveRank() {
		return substantiveRank;
	}
	public void setSubstantiveRank(String substantiveRank) {
		this.substantiveRank = substantiveRank;
	}
	public String getActiveRank() {
		return activeRank;
	}
	public void setActiveRank(String activeRank) {
		this.activeRank = activeRank;
	}
	public String getChineseName() {
		if (chineseName == null) {
			return "";
		}else {
			return chineseName;
		}
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	public String getInstT() {
		return instT;
	}
	public void setInstT(String instT) {
		this.instT = instT;
	}
	public String getSectionT() {
		return sectionT;
	}
	public void setSectionT(String sectionT) {
		this.sectionT = sectionT;
	}
	public String getDutiesT() {
		return dutiesT;
	}
	public void setDutiesT(String dutiesT) {
		this.dutiesT = dutiesT;
	}
	public String getInstX() {
		return instX;
	}
	public void setInstX(String instX) {
		this.instX = instX;
	}
	public String getSectionX() {
		return sectionX;
	}
	public void setSectionX(String sectionX) {
		this.sectionX = sectionX;
	}
	public String getDutiesX() {
		return dutiesX;
	}
	public void setDutiesX(String dutiesX) {
		this.dutiesX = dutiesX;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNotesAccount() {
		return notesAccount;
	}
	public void setNotesAccount(String notesAccount) {
		this.notesAccount = notesAccount;
	}
	public Long getRankId() {
		return rankId;
	}
	public void setRankId(Long rankId) {
		this.rankId = rankId;
	}
	public Long getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(Long institutionId) {
		this.institutionId = institutionId;
	}
	public Long getSectionId() {
		return sectionId;
	}
	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}
	public String getInstR() {
		return instR;
	}
	public void setInstR(String instR) {
		this.instR = instR;
	}
	public String getSectionR() {
		return sectionR;
	}
	public void setSectionR(String sectionR) {
		this.sectionR = sectionR;
	}
	public Date getLastSync() {
		return lastSync;
	}
	public void setLastSync(Date lastSync) {
		this.lastSync = lastSync;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getProfilePhoto() {
		return profilePhoto;
	}
	public void setProfilePhoto(String profilePhoto) {
		
		
//		 if(profilePhoto.length() >5 ) {
//			
//			 profilePhoto = profilePhoto.replace("user_profile/", "resources/");
//		 }
		this.profilePhoto = profilePhoto;
	}
	

	
	public String getBlogCreate() {
		return blogCreate;
	}
	public void setBlogCreate(String blogCreate) {
		this.blogCreate = blogCreate;
	}
	public String getPost_create() {
		return post_create;
	}
	public void setPost_create(String post_create) {
		this.post_create = post_create;
	}
	public String getAliasPhoto() {
		return aliasPhoto;
	}
	public void setAliasPhoto(String aliasPhoto) {
		this.aliasPhoto = aliasPhoto;
	}
	
	
	
	@Transient 
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	@Transient 
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	@Transient 
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	
	public Integer getIsBlogger() {
		return isBlogger;
	}
	public void setIsBlogger(Integer isBlogger) {
		this.isBlogger = isBlogger;
	}
	
	public Integer getScore() {
	
		return score;
	}
	public void setScore(Integer score) {
		setUserScoreLevel(score);
		this.score = score;
	
	}
	
	public Integer getTodayScore() {
		return todayScore;
	}
	public void setTodayScore(Integer todayScore) {
		this.todayScore = todayScore;
	}
	
	public Long getLoginFailAttempt() {
		return loginFailAttempt;
	}
	public void setLoginFailAttempt(Long loginFailAttempt) {
		this.loginFailAttempt = loginFailAttempt;
	}
	public Date getLoginFailAt() {
		return loginFailAt;
	}
	public void setLoginFailAt(Date loginFailAt) {
		this.loginFailAt = loginFailAt;
	}

	public Integer getAllowUpload() {
		return allowUpload;
	}
	public void setAllowUpload(Integer allowUpload) {
		this.allowUpload = allowUpload;
	}
	
	
	
	
	
	public Integer getUserScoreLevel() {
		return userScoreLevel;
	}
	public void setUserScoreLevel(Integer score) {
	
		if (score < 500) {
//			System.out.println("User : Line 637 : Score = "+ score);
			this.userScoreLevel = 1;
		} else if (score>499 && score<999) {
//			System.out.println("User : Line 640 : Score = "+ score);
			this.userScoreLevel = 2;
		} else if (score>1000 && score<1800) {
//			System.out.println("User : Line 643 : Score = "+ score);
			this.userScoreLevel = 3;
		} else if (score>1799 && score<2700) {
//			System.out.println("User : Line 646 : Score = "+ score);
			this.userScoreLevel = 4; 
		} else if (score > 2699) {
//			System.out.println("User : Line 649 : Score = "+ score);
			this.userScoreLevel = 5;
		}
//		System.out.println("User : Line 652 : Score = "+ score);
	}
	
	
	

//	public Integer getUserScoreLevel() {
//		return userScoreLevel;
//	}
//	public void setUserScoreLevel(Integer userScoreLevel) {
//		this.userScoreLevel = userScoreLevel;
//	}
//	
	
	
	
	
	public String getAliasPhotoIsPending() {
		return aliasPhotoIsPending;
	}
	public void setAliasPhotoIsPending(String aliasPhotoIsPending) {
		this.aliasPhotoIsPending = aliasPhotoIsPending;
	}
	public String getProfilePhotoIsPending() {
		return profilePhotoIsPending;
	}
	public void setProfilePhotoIsPending(String profilePhotoIsPending) {
		this.profilePhotoIsPending = profilePhotoIsPending;
	}
	public Date getAliasPhotoPendingDate() {
		return aliasPhotoPendingDate;
	}
	public void setAliasPhotoPendingDate(Date aliasPhotoPendingDate) {
		this.aliasPhotoPendingDate = aliasPhotoPendingDate;
	}
	public Date getProfilePhotoPendingDate() {
		return profilePhotoPendingDate;
	}
	public void setProfilePhotoPendingDate(Date profilePhotoPendingDate) {
		this.profilePhotoPendingDate = profilePhotoPendingDate;
	}
	public User() {
		
    }
//	public User(Long id, String username, String password, String fullname, Integer deleted, String email,
//			int usergroup, String lastIp, Date passwordLastChange, int loginTries, Date loginLastTry, int approved,
//			String lang, Date createdAt, String staffNo, String substantiveRank, String activeRank, String chineseName,
//			String instT, String sectionT, String dutiesT, String instX, String sectionX, String dutiesX, int sex,
//			String notesAccount, Long rankId, Long institutionId, Long sectionId, String instR, String sectionR,
//			Date lastSync, String alias, String profilePhoto, String blogCreate, String post_create,
//			String aliasPhoto) {
//		super();
//		this.id = id;
//		this.username = username;
//		this.password = password;
//		this.fullname = fullname;
//		this.deleted = deleted;
//		this.email = email;
//		this.usergroup = usergroup;
//		this.lastIp = lastIp;
//		this.passwordLastChange = passwordLastChange;
//		this.loginTries = loginTries;
//		this.loginLastTry = loginLastTry;
//		this.approved = approved;
//		this.lang = lang;
//		this.createdAt = createdAt;
//		this.staffNo = staffNo;
//		this.substantiveRank = substantiveRank;
//		this.activeRank = activeRank;
//		this.chineseName = chineseName;
//		this.instT = instT;
//		this.sectionT = sectionT;
//		this.dutiesT = dutiesT;
//		this.instX = instX;
//		this.sectionX = sectionX;
//		this.dutiesX = dutiesX;
//		this.sex = sex;
//		this.notesAccount = notesAccount;
//		this.rankId = rankId;
//		this.institutionId = institutionId;
//		this.sectionId = sectionId;
//		this.instR = instR;
//		this.sectionR = sectionR;
//		this.lastSync = lastSync;
//		this.alias = alias;
//		this.profilePhoto = profilePhoto;
//		this.blogCreate = blogCreate;
//		this.post_create = post_create;
//		this.aliasPhoto = aliasPhoto;
//	}
	
	
	
	
	
	public User(Long id, String fullname, String staff_no, String username, String email, String notes_account
			,Long institutionId, Long sectionId, Long rankId,String profilePhoto,String aliasPhoto, String aliasPhotoIsPending, String profilePhotoIsPending, Integer isDeleted) {
		super();
		this.id= id;
		this.fullname = fullname;
		this.staffNo = staff_no;
		this.username = username;
		this.email= email;
		this.notesAccount = notes_account;
		this.institutionId = institutionId;
		this.sectionId = sectionId;
		this.rankId = rankId;
		this.profilePhoto = profilePhoto;
		this.aliasPhoto = aliasPhoto;
		this.aliasPhotoIsPending = aliasPhotoIsPending;
		this.profilePhotoIsPending = profilePhotoIsPending;
		this.isDeleted = isDeleted;
	}
	
	public User(Long id, String fullname,String staff_no,Long institutionId, Long sectionId, Long rankId) {
		super();
		this.id = id;
		this.fullname = fullname;
		this.staffNo = staff_no;
		this.email = email;
		this.institutionId = institutionId;
		this.sectionId = sectionId;
		this.rankId = rankId;
	}
	
	public User(String staff_no, String name) {
		this.staffNo = staff_no;
		this.fullname = name;
	}
	
	public User(Long id, String name) {
		this.id = id;
		this.fullname = name;
	}

	@Transient 
	private CustomUserDetails customUserDetails;

	public CustomUserDetails getCustomUserDetails() {
		return customUserDetails;
	}
	public void setCustomUserDetails(CustomUserDetails customUserDetails) {
		this.customUserDetails = customUserDetails;
	}
	
	public User(CustomUserDetails customUserDetails) {
		this.customUserDetails = customUserDetails;
	}
	
}
