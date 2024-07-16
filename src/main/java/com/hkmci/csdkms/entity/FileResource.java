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
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hkmci.csdkms.model.FileCatPath;

/**
 * @author Holfer ZHANG
 * @Description
 * @project springboot_jpa
 * @package com.hkmci.entity
 * @email holfer.zhang@hkmci.com
 * @date 2019/03/04
 */

@Entity
@Table(name = "resource")
public class FileResource {
	
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@Column(name = "title_tc", nullable = true, length = 200)
	private String titleTc;
	
	@Column(name = "title_en", nullable = true, length = 200)
	private String titleEn;
	
	@Column(name = "resource_type_id", nullable = false)
	private int resourceTypeId;
	
	@Column(name = "file_extension", nullable = false)
	private String Type;
	
	@Column(name = "desc_tc", nullable = false, length = 300)
	private String descTc;
	
	@Column(name = "desc_en", nullable = false, length = 300)
	private String descEn;
	
	@Column(name = "access_channel", nullable = false)
	private String accessChannel;
	
	@Column(name = "latest_news", nullable = false)
	private Integer latestNews;
	
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "latest_news_expiry", nullable = false)
	private Date latestNewsExpiry;
	
	@Column(name = "activated", nullable = false)
	private Integer activated;
	
	@DateTimeFormat(pattern="yyyy-mm-dd")  
	@Column(name = "created_at", nullable = false)
	private Date createdAt;
	
	@Column(name = "created_by", nullable = false)
	private Long createdBy;
	
	@DateTimeFormat(pattern="yyyy-mm-dd")  
	@Column(name = "modified_at", nullable = false)
	private Date modifiedAt;
	
	@Column(name = "modified_by", nullable = false)
	private Long modifiedBy;
	
	@DateTimeFormat(pattern="yyyy-mm-dd")  
	@Column(name = "deleted_at", nullable = true)
	private Date deletedAt;
	
	@Column(name = "deleted_by", nullable = false)
	private Long deletedBy;
	
	@Column(name = "wfilename" , nullable = false)
	private String wfilename;
	
	@Column(name = "nfilename", nullable = false)
	private String nfilename;
	
	@Column(name = "ofilename", nullable = false)
	private String ofilename;
	
	@Column(name = "filesize", nullable = false)
	private String filesize;
	
	@Column(name = "filepath", nullable = false)
	private String filepath;
	
	@Column(name = "video_link", nullable = false)
	private String videoLink;
	
	@Column(name = "thumbnail", nullable = false)
	private String thumbnail;
	
	@JsonIgnore
	@Column(name = "deleted", nullable = false)
	private Integer deleted;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private float averageRating;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer userRating;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer hitRate;
	
	
	@Column(name="as_word",nullable = false )
	private Integer asWord;
	
	
	
	

	@Column(name="as_watermark",nullable = false)
	private Integer asWatermark;
	
	
	@Transient 
	@JsonInclude(Include.NON_NULL)
	private List<Object> catpath;
	
	@Transient 
	@JsonInclude(Include.NON_NULL)
	private List<Long> catId;

//	@OneToMany(mappedBy = "resource",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
//	@Where(clause = "deleted_by is null")
//	@OrderBy("id asc")
	
	@Transient
	private List<ResourceAccessRule> resourceAccessRuleList;//AccessRuleList



	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Transient
	private List<FileCatPath> fileCatPath;
	
	
	
	public Integer getAsWatermark() {
		return asWatermark;
	}

	public void setAsWatermark(Integer asWatermark) {
		this.asWatermark = asWatermark;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitleTc() {
		return titleTc;
	}

	public void setTitleTc(String titleTc) {
		this.titleTc = titleTc;
	}

	public String getTitleEn() {
		return titleEn;
	}

	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}

	public int getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(int resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public String getType() {
		return Type;
	}

	public void setType(String Type) {
		this.Type = Type;
	}

	public String getDescTc() {
		return descTc;
	}

	public void setDescTc(String descTc) {
		this.descTc = descTc;
	}

	public String getDescEn() {
		return descEn;
	}

	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}

	public String getAccessChannel() {
		return accessChannel;
	}

	public void setAccessChannel(String accessChannel) {
		this.accessChannel = accessChannel;
	}

	public Integer getLatestNews() {
		return latestNews;
	}

	public void setLatestNews(Integer latestNews) {
		this.latestNews = latestNews;
	}

	public Date getLatestNewsExpiry() {
		return latestNewsExpiry;
	}

	public void setLatestNewsExpiry(Date latestNewsExpiry) {
		this.latestNewsExpiry = latestNewsExpiry;
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

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
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
	
	

	public String getWfilename() {
		return wfilename;
	}

	public void setWfilename(String wfilename) {
		this.wfilename = wfilename;
	}

	public String getNfilename() {
		return nfilename;
	}

	public void setNfilename(String nfilename) {
		this.nfilename = nfilename;
	}

	public String getOfilename() {
		return ofilename;
	}

	public void setOfilename(String ofilename) {
		this.ofilename = ofilename;
	}

	public String getFilesize() {
		return filesize;
	}
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	
	@Transient
	public List<Long> getCatId() {
		return catId;
	}
	
	public void setCatId( List<Long> catId) {
	
		this.catId = catId;
	}
	
	@Transient
	public List<Object> getCatpath() {
		return catpath;
	}
	public void setCatpath(List<Object> catpath) {
		this.catpath = catpath;
	
	}
	
	public List<ResourceAccessRule> getResourceAccessRuleList() {
		return resourceAccessRuleList;
	}
	
	public void setResourceAccessRuleList(List<ResourceAccessRule> resourceAccessRuleList) {
	
		this.resourceAccessRuleList = resourceAccessRuleList;
	}


	public Integer getAsWord() {
		return asWord;
	}

	public void setAsWord(Integer asWord) {
		this.asWord = asWord;
	}
	
	

	public float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}

	
	
	public Integer getUserRating() {
		return userRating;
	}

	public void setUserRating(Integer userRating) {
		this.userRating = userRating;
	}
	
	
	public Integer getHitRate() {
		return hitRate;
	}
	public void setHitRate(Integer hitRate) {
		this.hitRate = hitRate;
	}
	

	public FileResource(Long id, String titleTc, String titleEn, int resourceTypeId, String Type,
			String descTc, String descEn, String accessChannel, Integer latestNews, Date latestNewsExpiry,
			Integer activated, Date createdAt, Long createdBy, Date modifiedAt, Long modifiedBy, Date deletedAt,
			Long deletedBy, String nfilename, String ofilename, String filesize, String filepath, String videoLink,
			String thumbnail, Integer deleted, Integer asWord,Integer asWatermark,Float averageRating, Integer userRating, Integer hitRate) {
		super();
		this.id = id;
		this.titleTc = titleTc;
		this.titleEn = titleEn;
		this.resourceTypeId = resourceTypeId;
		this.Type = Type;
		this.descTc = descTc;
		this.descEn = descEn;
		this.accessChannel = accessChannel;
		this.latestNews = latestNews;
		this.latestNewsExpiry = latestNewsExpiry;
		this.activated = activated;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.modifiedAt = modifiedAt;
		this.modifiedBy = modifiedBy;
		this.deletedAt = deletedAt;
		this.deletedBy = deletedBy;
		this.nfilename = nfilename;
		this.ofilename = ofilename;
		this.filesize = filesize;
		this.filepath = filepath;
		this.videoLink = videoLink;
		this.thumbnail = thumbnail;
		this.deleted = deleted;
		this.asWord = asWord;
		this.asWatermark= asWatermark;
		this.averageRating  = averageRating;
		this.userRating = userRating;
		this.hitRate = hitRate;
	}

	@Override
	public String toString() {
		return "FileResource [id=" + id + ", titleTc=" + titleTc + ", titleEn=" + titleEn + ", resourceTypeId="
				+ resourceTypeId + ", Type=" + Type + ", descTc=" + descTc + ", descEn=" + descEn
				+ ", accessChannel=" + accessChannel + ", latestNews=" + latestNews + ", latestNewsExpiry="
				+ latestNewsExpiry + ", activated=" + activated + ", createdAt=" + createdAt + ", createdBy="
				+ createdBy + ", modifiedAt=" + modifiedAt + ", modifiedBy=" + modifiedBy + ", deletedAt=" + deletedAt
				+ ", deletedBy=" + deletedBy + ", nfilename=" + nfilename + ", ofilename=" + ofilename + ", filesize="
				+ filesize + ", filepath=" + filepath + ", videoLink=" + videoLink + ", thumbnail=" + thumbnail
				+ ", deleted=" + deleted + "]";
	}
	
	public FileResource() {
		
	};
	
	
}
