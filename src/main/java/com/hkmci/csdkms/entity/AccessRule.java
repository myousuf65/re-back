package com.hkmci.csdkms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "access_rule")
public class AccessRule {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;


	@Column(name = "desc_en", nullable = false)
	private String description;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "area_id", nullable = false)
	private Long areaId;
	
	@JsonIgnore
	@Column(name = "section_id", nullable = false)
	private String sectionId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "inst_id", nullable = false)
	private String instId;
	
	@JsonIgnore
	@Column(name = "rank_id", nullable = false)
	private String rankId;
	
	
	@JsonIgnore
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "deleted_at", nullable = true)
	private Date deletedAt;
	
	
	@JsonIgnore
	@Column(name = "deleted_by", nullable = true)
	private Long deletedBy;
	
//	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "modified_at", nullable = true)
	private Date modifiedAt;
	
	@JsonIgnore
	@Column(name = "modified_by", nullable = true)
	private Long modifiedBy;
	
	@JsonIgnore
	@Column(name = "created_by", nullable = false)
	private Long createdBy;
	
	@Column(name="created_at", nullable= true)
	private Date createdAt;
	
	
	@JsonIgnore
	@Column(name = "is_deleted", nullable = true)
	private int isDeleted;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
//	@JsonIgnore
	List<Integer> instIdList;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
//	@JsonIgnore
	List<Integer> rankIdList;
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
//	@JsonIgnore
	List<Integer> sectionIdList;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}
	

	public String getRankId() {
		return rankId;
	}

	public void setRankId(String rankId) {
		this.rankId = rankId;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = new Date();
	}

	public Long getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = new Date();
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	
	
	@Transient
	public List<Integer> getRankIdList() {
//		List<Integer> rank_list = Arrays.asList(rankId.split(",")).stream().map((n) -> {
//			return  Integer.parseInt(n.toString());
//		}).collect(Collectors.toList());
		
		return rankIdList;
	}
	
	

	public void setRankIdList(List<Integer> rankIdList) {
		this.rankIdList = rankIdList;
	}
	@Transient
	public List<Integer> getSectionIdList(){
//		List<Integer> section_list = Arrays.asList(sectionId.split(",")).stream().map((n) -> {
//			return  Integer.parseInt(n.toString());
//		}).collect(Collectors.toList());
		
		return sectionIdList;
		
	}
	public void setSectionIdList(List<Integer> sectionIdList) {
		this.sectionIdList = sectionIdList;
	}
	
	
	@Transient
	public List<Integer> getInstIdList() {
//		List<Integer> instIdList = Arrays.asList(instId.split(",")).stream().map((n) -> {
//			return  Integer.parseInt(n.toString());
//		}).collect(Collectors.toList());
		
		return instIdList;
	}

	public void setInstIdList(List<Integer> instIdList) {
		this.instIdList = instIdList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String descEn) {
		this.description = descEn;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	
	
	
	
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public AccessRule(Long id, Long accessRuleId, String sectionId, String instId, String rankId,
			Date modifiedAt, Long modifiedBy, Long createdBy, int isDeleted) {
		
		this.id = id;

		this.sectionId = sectionId;
		this.instId = instId;
		this.rankId = rankId;
		this.modifiedAt = modifiedAt;
		this.modifiedBy = modifiedBy;
		this.deletedAt = null;
		this.deletedBy = (long) 0;
		this.createdBy = createdBy;
		this.isDeleted = isDeleted;
	
	}

	public AccessRule() {

	}

	public AccessRule(String sectionId, String instId, String rankId, Long createdBy, int isDeleted) {
		
		this.sectionId = sectionId;
		this.instId = instId;
		this.rankId = rankId;
		this.deletedAt = null;
		this.deletedBy = (long) 0;
		this.createdBy = createdBy;
		this.isDeleted = isDeleted;
		
	}

}
