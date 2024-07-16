package com.hkmci.csdkms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "elearning_user_group")
public class ElearningUserGroup {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
    @Column(name = "id", nullable = false)  
    private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "group_name", nullable = false)
    private String groupName;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "group_staff_no", nullable = false)
    private String groupStaffNo;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "created_at", nullable = false)
    private String createdAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "created_by", nullable = false)
    private String createdBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "modifield_at", nullable = true)
    private String modifield_at;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "modifield_by", nullable = true)
    private String modifield_by;	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "is_deleted", nullable = false)
    private String isDeleted;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "deleted_at", nullable = true)
    private String deleted_at;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "deleted_by", nullable = true)
    private String deleted_by;
	
	public Long getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupStaffNo() {
        return groupStaffNo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getModifield_at() {
        return modifield_at;
    }

    public String getModifield_by() {
        return modifield_by;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public String getDeleted_by() {
        return deleted_by;
    }

    // Setter 方法
    public void setId(Long id) {
        this.id = id;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupStaffNo(String groupStaffNo) {
        this.groupStaffNo = groupStaffNo;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setModifield_at(String modifield_at) {
        this.modifield_at = modifield_at;
    }

    public void setModifield_by(String modifield_by) {
        this.modifield_by = modifield_by;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public void setDeleted_by(String deleted_by) {
        this.deleted_by = deleted_by;
    }
}