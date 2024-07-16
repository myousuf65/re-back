package com.hkmci.csdkms.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hkmci.csdkms.entity.ForumPost;

public class ForumPostDetailReturnModel implements Serializable {
	
	private static final long serialVersionUID = -8906734455133971892L;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ForumPostReturnModel post;
	
	//@JsonInclude(JsonInclude.Include.NON_NULL)
	private ForumPost prePost;
	
	//@JsonInclude(JsonInclude.Include.NON_NULL)
	private ForumPost nextPost;

	public ForumPostReturnModel getPost() {
		return post;
	}

	public void setPost(ForumPostReturnModel post) {
		this.post = post;
	}

	public ForumPost getPrePost() {
		return prePost;
	}

	public void setPrePost(ForumPost prePost) {
		this.prePost = prePost;
	}

	public ForumPost getNextPost() {
		return nextPost;
	}

	public void setNextPost(ForumPost nextPost) {
		this.nextPost = nextPost;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
