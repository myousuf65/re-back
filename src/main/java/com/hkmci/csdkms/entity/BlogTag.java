package com.hkmci.csdkms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "blog_post_tag")
public class BlogTag {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@Column(name = "post_id", nullable = false)  
    private Long postId;
	
	@Column(name = "tag", nullable = false)  
    private String tag;
	
	@Column(name = "is_deleted", nullable = false)  
    private Integer isDeleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public BlogTag(Long id, Long postId, String tag, Integer isDeleted) {
		super();
		this.id = id;
		this.postId = postId;
		this.tag = tag;
		this.isDeleted = isDeleted;
	}
	
	public BlogTag(Long postId, String tag, Integer isDeleted) {
		super();
		this.postId = postId;
		this.tag = tag;
		this.isDeleted = isDeleted;
	}
	
	public BlogTag() {
		
	}

}
