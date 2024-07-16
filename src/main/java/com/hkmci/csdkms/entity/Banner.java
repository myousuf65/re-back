package com.hkmci.csdkms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "banner")
public class Banner {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "link_to", nullable = false)  
    private String linkTo;
	
	@Column(name = "name", nullable = false)  
    private String name;
	
	
	@Column(name = "img_url", nullable = true)  
    private String imgUrl;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "level", nullable = true)  
    private String level;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "name_tc", nullable = true)  
    private String name_tc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "orderby", nullable = true)  
    private Integer orderby;
	
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "create_at", nullable = true)  
    private Date createAt;
	
	@Column(name="create_by" , nullable= true)
	private Long createBy;
	
	@Column(name="is_deleted" , nullable= true)
	private Integer isDeleted;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name="deleted_at" , nullable= false)
	private Date deletedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="deleted_by", nullable= false)
	private Long deletedBy;
	
	
	
	public Long getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="modified_by", nullable= false)
	private Long modifiedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern= "yyyy-mm-dd")
	@Column(name="modified_at", nullable = false)
	private Date modifiedAt;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="access_channel", nullable = false)
	private String accessChannel;
	
	
	@Column(name = "access_rule_id", nullable = false)  
    private String accessRuleId;

	
	
	
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLinkTo() {
		return linkTo;
	}

	public void setLinkTo(String linkTo) {
		this.linkTo = linkTo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getName_tc() {
		return name_tc;
	}

	public void setName_tc(String name_tc) {
		this.name_tc = name_tc;
	}

	public Integer getOrderby() {
		return orderby;
	}

	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getAccessRuleId() {
		return accessRuleId;
	}

	public void setAccessRuleId(String accessRuleId) {
		this.accessRuleId = accessRuleId;
	}
	

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
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

	
	public String getAccessChannel() {
		return accessChannel;
	}
	
	public void setAccessChannel(String accessChannel) {
		this.accessChannel = accessChannel;
	}
	
	
	public Banner(Long id, String link_to, String name, String imgUrl, String level, String name_tc,
			Integer orderby, Date createAt, Long createBy, Integer isDeleted, Date deletedAt, Long modifiedBy,
			Date modifiedAt, String accessRuleId,String accessChannel) {
		super();
		this.id = id;
		this.linkTo = link_to;
		this.name = name;
		this.imgUrl = imgUrl;
		this.level = level;
		this.name_tc = name_tc;
		this.orderby = orderby;
		this.createAt = createAt;
		this.createBy = createBy;
		this.isDeleted = isDeleted;
		this.deletedAt = deletedAt;
		this.modifiedBy = modifiedBy;
		this.modifiedAt = modifiedAt;
		this.accessRuleId = accessRuleId;
		this.accessChannel = accessChannel;
	}

	public Banner(Long id, String link_to, String name, String imgUrl, String level, String name_tc,
			Integer orderby, Date createAt, String accessRuleId) {
		super();
		this.id = id;
		this.linkTo = link_to;
		this.name = name;
		this.imgUrl = imgUrl;
		this.level = level;
		this.name_tc = name_tc;
		this.orderby = orderby;
		this.createAt = createAt;
		this.accessRuleId = accessRuleId;
	}

	public Banner() {
		
	}
}
