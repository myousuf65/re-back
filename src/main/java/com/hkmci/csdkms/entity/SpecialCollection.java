package com.hkmci.csdkms.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hkmci.csdkms.common.Common;

@Entity
@Table(name = "special_collection_post")
public class SpecialCollection {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@Column(name = "post_title", nullable = false)  
    private String postTitle;
	
	@Column(name = "post_title_zh", nullable = false)  
    private String postTitleZh;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "created_at", nullable = false)
	private Date createdAt;
	
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "created_by", nullable = false)
	private Long createdBy;
    
    @Column(name = "is_deleted", nullable = false)  
	private Integer isDeleted;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "deleted_at", nullable = true)
	private Date deletedAt;
	
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "deleted_by", nullable = true)
	private Long deletedBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "modified_by", nullable = true)
	private Long modifiedy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "modified_at", nullable = true)
	private Date modifiedAt;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "publish_at", nullable = true)
	private Date publishAt;
    
    @Column(name = "is_public", nullable = false)  
	private Long is_public;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "original_creator")  
	private User originalCreator;
    
    @Column(name = "show_as_alias", nullable = false)  
	private Integer showAsAlias;
    
    @Column(name = "alias", nullable = true)
    private String alias;
    
    @Column(name = "link", nullable = true)  
    private String link;
    
    @Column(name = "access_rule_id", nullable = true)  
    private String accessRuleId;
    
    @Column(name = "access_channel", nullable = true)  
    private String accessChannel;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	
	public String getPostTitleZh() {
		return postTitleZh;
	}

	public void setPostTitleZh(String postTitleZh) {
		this.postTitleZh = postTitleZh;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	
	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Long getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Long getModifiedy() {
		return modifiedy;
	}

	public void setModifiedy(Long modifiedy) {
		this.modifiedy = modifiedy;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Date getPublishAt() {
		return publishAt;
	}

	public void setPublishAt(Date publishAt) {
		this.publishAt = publishAt;
	}

	public Long getIs_public() {
		return is_public;
	}

	public void setIs_public(Long is_public) {
		this.is_public = is_public;
	}
	
	public User getOriginalCreator() {
		return originalCreator;
	}

	public void setOriginalCreator(User originalCreator) {
		this.originalCreator = originalCreator;
	}

	public Integer getShowAsAlias() {
		return showAsAlias;
	}

	public void setShowAsAlias(Integer showAsAlias) {
		this.showAsAlias = showAsAlias;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getAccessRuleId() {
		return accessRuleId;
	}
	
	public void setAccessRuleId(String accessRuleId) {
		this.accessRuleId = accessRuleId;
	}
	
	public String getAccessChannel() {
		return accessChannel;
	}

	public void setAccessChannel(String accessChannel) {
		this.accessChannel = accessChannel;
	}
	
	public SpecialCollection(Long id, String postTitle, String postTitleZh, Date createdAt, Long createdBy,Integer isDeleted, Date deletedAt, Long deletedBy, Long modifiedy, Date modifiedAt, Date publishAt,
			Long is_public, User originalCreator, Integer showAsAlias,
			String alias, String link, String accessRuleId, String accessChannel) {
		super();
		this.id = id;
		this.postTitle = postTitle;
		this.postTitleZh = postTitleZh;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.isDeleted = isDeleted;
		this.deletedAt = deletedAt;
		this.deletedBy = deletedBy;
		this.modifiedy = modifiedy;
		this.modifiedAt = modifiedAt;
		this.publishAt = publishAt;
		this.is_public = is_public;
		this.originalCreator = originalCreator;
		this.showAsAlias = showAsAlias;
		this.alias = alias;
		this.link = link;
		this.accessRuleId = accessRuleId;
		this.accessChannel= accessChannel;
	}
	
	public SpecialCollection(Object[] specialCollection_object) {
    	super();
    	Common common = new Common(null, null, null, null, null);
		this.id = Long.parseLong(specialCollection_object[0].toString());
		this.postTitle = specialCollection_object[2].toString();
		this.postTitleZh = specialCollection_object[3].toString();
		this.createdBy = Long.parseLong(specialCollection_object[4].toString());
		this.createdAt = common.textToDate(specialCollection_object[5].toString());
		this.isDeleted = Integer.parseInt(specialCollection_object[7].toString());
		this.deletedBy = null;
		this.deletedAt = null;
		this.modifiedy = specialCollection_object[11] == null ? null : Long.parseLong(specialCollection_object[11].toString());
		this.modifiedAt = specialCollection_object[12] == null ? null : common.textToDate(specialCollection_object[12].toString());
		this.is_public = Long.parseLong(specialCollection_object[13].toString());
		this.publishAt = specialCollection_object[14] == null ? null : common.textToDate(specialCollection_object[14].toString());
		this.originalCreator = (User)specialCollection_object[18];
		this.showAsAlias = Integer.parseInt(specialCollection_object[19].toString());
		this.alias =  specialCollection_object[20] == null ? "" : specialCollection_object[20].toString();
		this.link = specialCollection_object[21].toString();
		this.accessRuleId = specialCollection_object[22].toString();
		this.accessChannel= specialCollection_object[23].toString();
    };
	
	public SpecialCollection() {
		
	}

}
