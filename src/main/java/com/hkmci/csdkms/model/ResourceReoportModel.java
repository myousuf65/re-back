package com.hkmci.csdkms.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.hkmci.csdkms.entity.FileResource;

public class ResourceReoportModel {
	
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long institutionId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String institution;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String section;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String rank;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer staff_strength;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer no_of_Staff_Logged_in;
	
	

	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer zeroLogin;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer no_of_mobile_staff_login;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String fullname;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String staffNo;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long resourceId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long blogId;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer mobile_login;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer intranet_login;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer internet_login;
	
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer total_login;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer intranet_count;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer internet_count;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer total_hit_rate;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer hit_count;
	 
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer total_count;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String titleEn;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String title;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String titleTc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String pageNameEN;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String pageNameTc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer activated;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date createdAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date publishAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object createdBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object originalCreator;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object blogCategory;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer isDeleted;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long isPublic;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date modifiedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object modifiedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<String> category;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<String> categoryTc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer hits;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer likes;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer comments;
	
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer resource_count;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer shared;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer download;
	

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer userScore;
	
	

		
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer availableUsers;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer level;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String login_percentage;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String login_per_staff_strength;
	
	
	@JsonIgnore
	private FileResource resource;
	
	@JsonIgnore
	private Long categoryId;
	

	public Long getResourceId() {
		return resourceId;
	}

	public String getLogin_percentage() {
		return login_percentage;
	}

	public void setLogin_percentage(String login_percentage) {
		this.login_percentage = login_percentage;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getTitleEn() {
		return titleEn;
	}

	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}

	public String getTitleTc() {
		return titleTc;
	}

	public void setTitleTc(String titleTc) {
		this.titleTc = titleTc;
	}

	public Integer getActivated() {
		return activated;
	}

	public void setActivated(Integer activated) {
		this.activated = activated;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Object getCreatedBy() {
		return createdBy;
	}
	

	public List<String> getCategory() {
		return category;
	}

	public void setCategory(List<String> category) {
		this.category = category;
	}
	
	public List<String> getCategoryTc() {
		return categoryTc;
	}

	public void setCategoryTc(List<String> categoryTc) {
		this.categoryTc = categoryTc;
	}
	

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public Integer getShared() {
		return shared;
	}

	public void setShared(Integer shared) {
		this.shared = shared;
	}

	public Integer getDownload() {
		return download;
	}

	public void setDownload(Integer download) {
		this.download = download;
	}

	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public Integer getHit_count() {
		return hit_count;
	}

	public void setHit_count(Integer hit_count) {
		this.hit_count = hit_count;
	}

	public Integer getIntranet_count() {
		return intranet_count;
	}

	public void setIntranet_count(Integer intranet_count) {
		this.intranet_count = intranet_count;
	}

	public Integer getInternet_count() {
		return internet_count;
	}

	public void setInternet_count(Integer internet_count) {
		this.internet_count = internet_count;
	}

	public Integer getTotal_count() {
		return total_count;
	}

	public void setTotal_count(Integer total_count) {
		this.total_count = total_count;
	}
	
	public String getPageNameEN() {
		return pageNameEN;
	}

	public void setPageNameEN(String pageNameEN) {
		this.pageNameEN = pageNameEN;
	}

	public String getPageNameTc() {
		return pageNameTc;
	}

	public void setPageNameTc(String pageNameTc) {
		this.pageNameTc = pageNameTc;
	}

	public Long getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(Long institutionId) {
		this.institutionId = institutionId;
	}

	public Integer getResource_count() {
		return resource_count;
	}

	public void setResource_count(Integer resource_count) {
		this.resource_count = resource_count;
	}

	public void setCreatedBy(String fullname, String section, String institution, String rank) {
		HashMap<String, String> createdBy = new HashMap<>();
		createdBy.put("fullname", fullname);
		createdBy.put("institution", institution);
		createdBy.put("section", section);
		createdBy.put("rank", rank);
		this.createdBy = createdBy;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Object getModifiedBy() {
		return modifiedBy;
	}
	

	public FileResource getResource() {
		return resource;
	}

	public void setResource(FileResource resource) {
		this.resource = resource;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getBlogId() {
		return blogId;
	}

	public void setBlogId(Long blogId) {
		this.blogId = blogId;
	}

	public Date getPublishAt() {
		return publishAt;
	}

	public void setPublishAt(Date publishAt) {
		this.publishAt = publishAt;
	}

	public Object getOriginalCreator() {
		return originalCreator;
	}

	public void setOriginalCreator(String staffNo, String fullname) {
		HashMap<String, String> temp = new HashMap<>();
		temp.put("staffNo", staffNo);
		temp.put("fullname", fullname);
		this.originalCreator = temp;
	}

	public Object getBlogCategory() {
		return blogCategory;
	}
	
	
	public void setBlogCategory(String nameEn, String nameTc) {
		HashMap<String, String> temp = new HashMap<>();
		temp.put("nameEn", nameEn);
		temp.put("nameTc", nameTc);
		this.blogCategory = temp;
	}


	public Integer getZeroLogin() {
		return zeroLogin;
	}

	public void setZeroLogin(Integer zeroLogin) {
		this.zeroLogin = zeroLogin;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Long isPublic) {
		this.isPublic = isPublic;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getComments() {
		return comments;
	}

	public void setComments(Integer comments) {
		this.comments = comments;
	}
	
	public Integer getUserScore() {
		return userScore;
	}
	
	public void setUserScore(Integer userScore) {
		this.userScore = userScore;
	}
	
	public Integer getLevel() {
		return level;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getAvailableUsers() {
		return availableUsers;
	}

	public void setAvailableUsers(Integer availableUsers) {
		this.availableUsers = availableUsers;
	}

	
	
	
	
//	public Integer getStaffStrength() {
//		return staff_strength;
//	}
//
//	public void setStaffStrength(Integer staff_strength) {
//		this.staff_strength = staff_strength;
//	}

	public Integer getNo_of_Staff_Logged_in() {
		return no_of_Staff_Logged_in;
	}

	public void setNo_of_Staff_Logged_in(Integer no_of_Staff_Logged_in) {
		this.no_of_Staff_Logged_in = no_of_Staff_Logged_in;
	}

	
	
	public Integer getMobile_login() {
		return mobile_login;
	}

	public void setMobile_login(Integer mobile_login) {
		this.mobile_login = mobile_login;
	}
	
	public Integer getIntranet_login() {
		return intranet_login;
	}

	public void setIntranet_login(Integer intranet_login) {
		this.intranet_login = intranet_login;
	}

	public Integer getInternet_login() {
		return internet_login;
	}

	public void setInternet_login(Integer internet_login) {
		this.internet_login = internet_login;
	}

	public Integer getTotal_login() {
		return total_login;
	}

	public void setTotal_login(Integer total_login) {
		this.total_login = total_login;
	}
	
	

//	public Integer getRankTotalStaff() {
//		return strength;
//	}
//
//	public void setRankTotalStaff(Integer strength) {
//		this.strength = strength;
//	}
//
//	public Integer getRankTotalLoginStaff() {
//		return no_of_Staff_Logged_in;
//	}
//
//	public void setRankTotalLoginStaff(Integer no_of_Staff_Logged_in) {
//		this.no_of_Staff_Logged_in = no_of_Staff_Logged_in;
//	}

	
	
	
	
	public Integer getNo_of_mobile_staff_login() {
		return no_of_mobile_staff_login;
	}

	public void setNo_of_mobile_staff_login(Integer no_of_mobile_staff_login) {
		this.no_of_mobile_staff_login = no_of_mobile_staff_login;
	}



	public Integer getStaff_strength() {
		return staff_strength;
	}

	public String getLogin_per_staff_strength() {
		return login_per_staff_strength;
	}

	public void setLogin_per_staff_strength(String login_per_staff_strength) {
		this.login_per_staff_strength = login_per_staff_strength;
	}

	public void setStaff_strength(Integer staff_strength) {
		this.staff_strength = staff_strength;
	}

	public Integer getTotal_hit_rate() {
		return total_hit_rate;
	}

	public void setTotal_hit_rate(Integer total_hit_rate) {
		this.total_hit_rate = total_hit_rate;
	}

	public void setModifiedBy(String fullname, String section, String institution, String rank) {
		HashMap<String, String> modifiedBy = new HashMap<>();
		modifiedBy.put("fullname", fullname);
		modifiedBy.put("institution", institution);
		modifiedBy.put("section", section);
		modifiedBy.put("rank", rank);
		this.modifiedBy = modifiedBy;
	}
	


	public ResourceReoportModel(Long resourceId, String titleEn, String titleTc, Integer activated, Date createdAt,
			Object createdBy, Date modifiedAt, Object modifiedBy) {
		super();
		this.resourceId = resourceId;
		this.titleEn = titleEn;
		this.titleTc = titleTc;
		this.activated = activated;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.modifiedAt = modifiedAt;
		this.modifiedBy = modifiedBy;
	}
	
	public ResourceReoportModel() {
		
	}
	
	public ResourceReoportModel(Long categoryId, FileResource resource) {
		this.categoryId = categoryId;
		this.resource = resource;
	}

	
}
