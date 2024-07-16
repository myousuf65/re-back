package com.hkmci.csdkms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "elearning_course")
public class ElearningCourse {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "course_name", nullable = false)
	private String courseName;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "access_channel", nullable = true)  
    private String accessChannel;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "access_rule_id", nullable = true)  
    private String accessRuleId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "staff_no_list", nullable = true)  
    private String staffNoList;
    
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "start_date", nullable = true)  
    private Date startDate;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "end_date", nullable = false)  
    private Date endDate;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "is_publish", nullable = false)  
    private Long isPublish;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "is_deleted", nullable = false)  
    private Long isDeleted;

   	@DateTimeFormat(pattern="yyyy-mm-dd")
   	@Column(name = "deleted_at", nullable = true)
   	private Date deletedAt;
       
   	@Column(name = "deleted_by", nullable = true)
   	private Long deletedBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "created_by", nullable = false)
	private Long createdBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "created_at", nullable = false)
	private Date createdAt;   

	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "modified_at", nullable = true)
	private Date modifiedAt;
    
  
	@Column(name = "modified_by", nullable = true)
	private Long modifiedBy;
	
	// id
	public Long getId() {
	    return id;
	}

	public void setId(Long id) {
	    this.id = id;
	}

	// courseName
	public String getCourseName() {
	    return courseName;
	}

	public void setCourseName(String courseName) {
	    this.courseName = courseName;
	}
	
    public String getAccessChannel() {
        return accessChannel;
    }

    public void setAccessChannel(String accessChannel) {
        this.accessChannel = accessChannel;
    }
    
    public String getAccessRuleId() {
        return accessRuleId;
    }

    public void setAccessRuleId(String accessRuleId) {
        this.accessRuleId = accessRuleId;
    }

	// staffNoList
	public String getStaffNoList() {
	    return staffNoList;
	}

	public void setStaffNoList(String staffNoList) {
	    this.staffNoList = staffNoList;
	}

	// startDate
	public Date getStartDate() {
	    return startDate;
	}

	public void setStartDate(Date startDate) {
	    this.startDate = startDate;
	}

	// end_date
	public Date getEndDate() {
	    return endDate;
	}

	public void setEndDate(Date endDate) {
	    this.endDate = endDate;
	}

	// isPublish
	public Long getIsPublish() {
	    return isPublish;
	}

	public void setIsPublish(long l) {
	    this.isPublish = l;
	}

	// isDeleted
	public Long getIsDeleted() {
	    return isDeleted;
	}

	public void setIsDeleted(long l) {
	    this.isDeleted = l;
	}

	// deletedAt
	public Date getDeletedAt() {
	    return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
	    this.deletedAt = deletedAt;
	}

	// deletedBy
	public Long getDeletedBy() {
	    return deletedBy;
	}

	public void setDeletedBy(Long deletedBy) {
	    this.deletedBy = deletedBy;
	}

	// createdBy
	public Long getCreatedBy() {
	    return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
	    this.createdBy = createdBy;
	}

	// createdAt
	public Date getCreatedAt() {
	    return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
	    this.createdAt = createdAt;
	}

	// modifiedAt
	public Date getModifiedAt() {
	    return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
	    this.modifiedAt = modifiedAt;
	}

	// modifiedBy
	public Long getModifiedBy() {
	    return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
	    this.modifiedBy = modifiedBy;
	}
}
