package com.hkmci.csdkms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.entity.SpecialCollection;
import com.hkmci.csdkms.entity.NewsCorner2;
import com.hkmci.csdkms.entity.User;


public class PostReturnModel implements Serializable{
 

	    /**
	 * 
	 */
	private static final long serialVersionUID = -8906734455133971892L;
	
	private Blog blog;
	
	private NewsCorner2 newscorner2;
	
	private SpecialCollection specialCollection;
    
    private Long likes;
    
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer is_liked;
    
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private Long comments;
    
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer is_bookmarked;
    
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String fullname;
    
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private String chinese_name;
    
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> tagList;
    
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private Long galleryId;
    
    
    
	public Blog getBlog() {
		return blog;
	}
	
	public void setBlog(Blog blog) {
		this.blog = blog;
	}
	
	public NewsCorner2 getNewsCorner2() {
		return newscorner2;
	}
	
	public void setNewsCorner2(NewsCorner2 newsCorner2) {
		this.newscorner2 = new NewsCorner2(newsCorner2.getId(), newsCorner2.getCategoryId(), newsCorner2.getPostTitle(), newsCorner2.getCreatedAt(), newsCorner2.getCreatedBy(), newsCorner2.getContent(),
			newsCorner2.getIsDeleted(), newsCorner2.getDeletedAt(), newsCorner2.getDeletedBy(), newsCorner2.getModifiedy(), newsCorner2.getModifiedAt(), newsCorner2.getPublishAt(),
			newsCorner2.getIs_public(), newsCorner2.getRankId(), newsCorner2.getInstId(), newsCorner2.getSectionId(), newsCorner2.getOriginalCreator(), newsCorner2.getShowAsAlias(),
			newsCorner2.getAlias(), newsCorner2.getHit() , newsCorner2.getPostVideoLink(), newsCorner2.getLink());
	}
	
	public SpecialCollection getSpecialCollection() {
		return specialCollection;
	}
	
	public void setSpecialCollection(SpecialCollection specialCollection) {
		this.specialCollection = new SpecialCollection(specialCollection.getId(), specialCollection.getPostTitle(), specialCollection.getPostTitleZh(), specialCollection.getCreatedAt(), specialCollection.getCreatedBy(),
			specialCollection.getIsDeleted(), specialCollection.getDeletedAt(), specialCollection.getDeletedBy(), specialCollection.getModifiedy(), specialCollection.getModifiedAt(), specialCollection.getPublishAt(),
			specialCollection.getIs_public(), specialCollection.getOriginalCreator(), specialCollection.getShowAsAlias(),
			specialCollection.getAlias(), specialCollection.getLink(),specialCollection.getAccessRuleId(),specialCollection.getAccessChannel());
	}

	public Long getLikes() {
		return likes;
	}


	public void setLikes(Long likes) {
		this.likes = likes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getChinese_name() {
		return chinese_name;
	}

	public void setChinese_name(String chinese_name) {
		this.chinese_name = chinese_name;
	}

	public Long getComments() {
		return comments;
	}

	public void setComments(Long comments) {
		this.comments = comments;
	}
	
	public Integer getIs_liked() {
		return is_liked;
	}

	public void setIs_liked(Integer is_liked) {
		this.is_liked = is_liked;
	}

	public Integer getIs_bookmarked() {
		return is_bookmarked;
	}

	public void setIs_bookmarked(Integer is_bookmarked) {
		this.is_bookmarked = is_bookmarked;
	}

	public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public Long getGalleryId() {
		return galleryId;
	}

	public void setGalleryId(Long galleryId) {
		this.galleryId = galleryId;
	}

	public PostReturnModel() {}



	public PostReturnModel(Blog blog, Long likes,Long comments, String fullname, String chinese_name) {
		super();
		this.blog = blog;
		this.likes = likes;
		this.comments = comments;
		this.fullname = fullname;
		this.chinese_name = chinese_name;
	}
	
	public PostReturnModel(NewsCorner2 blog, Long likes,Long comments, String fullname, String chinese_name) {
		super();
		this.newscorner2 = blog;
		this.likes = likes;
		this.comments = comments;
		this.fullname = fullname;
		this.chinese_name = chinese_name;
	}

	public PostReturnModel(Blog blog, Long likes,Long comments, String fullname, String chinese_name, Long gallery_id) {
		super();
		this.blog = blog;
		this.likes = likes;
		this.comments = comments;
		this.fullname = fullname;
		this.chinese_name = chinese_name;
		this.galleryId = gallery_id;
	}
	
	public PostReturnModel(NewsCorner2 blog, Long likes,Long comments, String fullname, String chinese_name, Long gallery_id) {
		super();
		this.newscorner2 = blog;
		this.likes = likes;
		this.comments = comments;
		this.fullname = fullname;
		this.chinese_name = chinese_name;
		this.galleryId = gallery_id;
	}
	
	public PostReturnModel(SpecialCollection specialCollection, String fullname, String chinese_name) {
		super();
		this.specialCollection = specialCollection;
		this.fullname = fullname;
		this.chinese_name = chinese_name;
	}




	public PostReturnModel(Blog blog, Long likes, Long comments,
			String fullname, String chinese_name,Integer is_liked, Integer is_bookmarked,List<String> tagList) {
		super();
		this.blog = blog;
		this.likes = likes;
		this.is_liked = is_liked;
		this.comments = comments;
		this.is_bookmarked = is_bookmarked;
		this.fullname = fullname;
		this.chinese_name = chinese_name;
		this.tagList = tagList;
	}
	
	public PostReturnModel(NewsCorner2 blog, Long likes, Long comments,
			String fullname, String chinese_name,Integer is_liked, Integer is_bookmarked,List<String> tagList) {
		super();
		this.newscorner2 = blog;
		this.likes = likes;
		this.is_liked = is_liked;
		this.comments = comments;
		this.is_bookmarked = is_bookmarked;
		this.fullname = fullname;
		this.chinese_name = chinese_name;
		this.tagList = tagList;
	}
	
	public PostReturnModel(Blog blog, Long likes,
			String fullname, String chinese_name) {
		super();
		this.blog = blog;
		this.likes = likes;
		this.fullname = fullname;
		this.chinese_name = chinese_name;
	}
	
	public PostReturnModel(NewsCorner2 blog, Long likes,
			String fullname, String chinese_name) {
		super();
		this.newscorner2 = blog;
		this.likes = likes;
		this.fullname = fullname;
		this.chinese_name = chinese_name;
	}

	@Override
	public String toString() {
		return "PostReturnModel [blog=" + blog + ", likes=" + likes + "]";
	};
	
	
	
}
