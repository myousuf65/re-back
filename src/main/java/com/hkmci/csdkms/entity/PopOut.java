package com.hkmci.csdkms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "pop_out")
public class PopOut {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="image_url", nullable = false)
	private String imageUrl;
	
	@Column(name="hypry_link", nullable = false)
	private String hypryLink;
	
	@Column(name="is_deleted", nullable = false)
	private Integer isDeleted;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="access_rule", nullable =false)
	private String accessRule;
	
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="access_channel", nullable = false)
	private String accessChannel;
	
	
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "create_at", nullable = true)  
    private Date createAt;
	
	@Column(name="create_by" , nullable= true)
	private Long createBy;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name="deleted_at" , nullable= false)
	private Date deletedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="modified_by", nullable= false)
	private Long modifiedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern= "yyyy-mm-dd")
	@Column(name="modified_at", nullable = false)
	private Date modifiedAt;
	
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getHypryLink() {
		return hypryLink;
	}

	public void setHypryLink(String hypryLink) {
		this.hypryLink = hypryLink;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getAccessRule() {
		return accessRule;
	}

	public void setAccessRule(String accessRule) {
		this.accessRule = accessRule;
	}

	public String getAccessChannel() {
		return accessChannel;
	}

	public void setAccessChannel(String accessChannel) {
		this.accessChannel = accessChannel;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}


	
}
