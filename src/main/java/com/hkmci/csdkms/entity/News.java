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
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hkmci.csdkms.common.Common;

@Entity
@Table(name = "news")
public class News {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@Column(name = "category_id", nullable = false)  
    private Long categoryId;
	
	
	@Column(name = "news_title", nullable = false)  
    private String newsTitle;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "created_at", nullable = false)
	private Date createdAt;
	
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "created_by", nullable = false)
	private Long createdBy;
    
    @Column(name = "content", nullable = false)  
	private String content;
    
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
    
    @Column(name = "rank_id", nullable = true)  
	private Long rankId;
    
    @Column(name = "inst_id", nullable = true)  
	private Long instId;
    
    @Column(name = "section_id", nullable = true)  
	private Long sectionId;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "original_creator")  
	private User originalCreator;
    
    @Column(name = "show_as_alias", nullable = false)  
	private Integer showAsAlias;
    
    @Column(name = "alias", nullable = true)
    private String alias;
    
    @Column(name = "hit", nullable = true)
    private Long hit;
    
    @Transient
    private String thumbnail;
    
 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Long getRankId() {
		return rankId;
	}

	public void setRankId(Long rankId) {
		this.rankId = rankId;
	}

	public Long getInstId() {
		return instId;
	}

	public void setInstId(Long instId) {
		this.instId = instId;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
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

	public Long getHit() {
		return hit;
	}

	public void setHit(Long hit) {
		this.hit = hit;
	}

	@Transient
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	
	
	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + ((categoryId == null) ? 0 : categoryId.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((deletedAt == null) ? 0 : deletedAt.hashCode());
		result = prime * result + ((deletedBy == null) ? 0 : deletedBy.hashCode());
		result = prime * result + ((hit == null) ? 0 : hit.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((instId == null) ? 0 : instId.hashCode());
		result = prime * result + ((isDeleted == null) ? 0 : isDeleted.hashCode());
		result = prime * result + ((is_public == null) ? 0 : is_public.hashCode());
		result = prime * result + ((modifiedAt == null) ? 0 : modifiedAt.hashCode());
		result = prime * result + ((modifiedy == null) ? 0 : modifiedy.hashCode());
		result = prime * result + ((originalCreator == null) ? 0 : originalCreator.hashCode());
		result = prime * result + ((newsTitle == null) ? 0 : newsTitle.hashCode());
		result = prime * result + ((publishAt == null) ? 0 : publishAt.hashCode());
		result = prime * result + ((rankId == null) ? 0 : rankId.hashCode());
		result = prime * result + ((sectionId == null) ? 0 : sectionId.hashCode());
		result = prime * result + ((showAsAlias == null) ? 0 : showAsAlias.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		News other = (News) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		if (categoryId == null) {
			if (other.categoryId != null)
				return false;
		} else if (!categoryId.equals(other.categoryId))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (deletedAt == null) {
			if (other.deletedAt != null)
				return false;
		} else if (!deletedAt.equals(other.deletedAt))
			return false;
		if (deletedBy == null) {
			if (other.deletedBy != null)
				return false;
		} else if (!deletedBy.equals(other.deletedBy))
			return false;
		if (hit == null) {
			if (other.hit != null)
				return false;
		} else if (!hit.equals(other.hit))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (instId == null) {
			if (other.instId != null)
				return false;
		} else if (!instId.equals(other.instId))
			return false;
		if (isDeleted == null) {
			if (other.isDeleted != null)
				return false;
		} else if (!isDeleted.equals(other.isDeleted))
			return false;
		if (is_public == null) {
			if (other.is_public != null)
				return false;
		} else if (!is_public.equals(other.is_public))
			return false;
		if (modifiedAt == null) {
			if (other.modifiedAt != null)
				return false;
		} else if (!modifiedAt.equals(other.modifiedAt))
			return false;
		if (modifiedy == null) {
			if (other.modifiedy != null)
				return false;
		} else if (!modifiedy.equals(other.modifiedy))
			return false;
		if (originalCreator == null) {
			if (other.originalCreator != null)
				return false;
		} else if (!originalCreator.equals(other.originalCreator))
			return false;
		if (newsTitle == null) {
			if (other.newsTitle != null)
				return false;
		} else if (!newsTitle.equals(other.newsTitle))
			return false;
		if (publishAt == null) {
			if (other.publishAt != null)
				return false;
		} else if (!publishAt.equals(other.publishAt))
			return false;
		if (rankId == null) {
			if (other.rankId != null)
				return false;
		} else if (!rankId.equals(other.rankId))
			return false;
		if (sectionId == null) {
			if (other.sectionId != null)
				return false;
		} else if (!sectionId.equals(other.sectionId))
			return false;
		if (showAsAlias == null) {
			if (other.showAsAlias != null)
				return false;
		} else if (!showAsAlias.equals(other.showAsAlias))
			return false;
		return true;
	}

	public News(Long id, Long categoryId, String newsTitle, Date createdAt, Long createdBy, String content,
			Integer isDeleted, Date deletedAt, Long deletedBy, Long modifiedy, Date modifiedAt, Date publishAt,
			Long is_public, Long rankId, Long instId, Long sectionId, User originalCreator, Integer showAsAlias,
			String alias, Long hit) {
		super();
		this.id = id;
		this.categoryId = categoryId;
		this.newsTitle = newsTitle;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.content = content;
		this.isDeleted = isDeleted;
		this.deletedAt = deletedAt;
		this.deletedBy = deletedBy;
		this.modifiedy = modifiedy;
		this.modifiedAt = modifiedAt;
		this.publishAt = publishAt;
		this.is_public = is_public;
		this.rankId = rankId;
		this.instId = instId;
		this.sectionId = sectionId;
		this.originalCreator = originalCreator;
		this.showAsAlias = showAsAlias;
		this.alias = alias;
		this.hit = hit;
	}
	
    public News(Object[] object) {
    	super();
    	Common common = new Common(null, null, null, null, null);
		this.id = Long.parseLong(object[0].toString());
		this.categoryId = Long.parseLong(object[1].toString());
		this.newsTitle = object[2].toString();
		this.createdBy = Long.parseLong(object[3].toString());
		this.createdAt = common.textToDate(object[4].toString());
		this.content = object[5].toString();
		this.isDeleted = Integer.parseInt(object[6].toString());
		this.deletedBy = null;
		this.deletedAt = null;
		this.modifiedy = object[10] == null ? null : Long.parseLong(object[10].toString());
		this.modifiedAt = object[11] == null ? null : common.textToDate(object[11].toString());
		this.is_public = Long.parseLong(object[12].toString());
		this.publishAt = object[13] == null ? null : common.textToDate(object[13].toString());
		this.rankId = Long.parseLong(object[14].toString());
		this.instId = Long.parseLong(object[15].toString());
		this.sectionId = Long.parseLong(object[16].toString());
		this.originalCreator = (User)object[17];
		this.showAsAlias = Integer.parseInt(object[18].toString());
		this.alias =  object[19] == null ? "" : object[19].toString();
		this.hit = Long.parseLong(object[20].toString());
    };
    
    public News() {
    	
    }
    

}
