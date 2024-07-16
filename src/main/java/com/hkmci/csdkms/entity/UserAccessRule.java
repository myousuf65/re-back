package com.hkmci.csdkms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user_access_rule")
public class UserAccessRule {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;

	@JsonIgnore
	@Column(name = "user_id", nullable = false)  
	private Long userId;
	
	@Column(name = "access_rule_id", nullable = false)  
	private Long accessRuleId;
	
	@JsonIgnore
	@Column(name = "is_deleted")
	private Integer isDeleted;
	
	

	public UserAccessRule(Long id, Long userId, Long accessRuleId, Integer isDeleted) {
		super();
		this.id = id;
		this.userId = userId;
		this.accessRuleId = accessRuleId;
		this.isDeleted = isDeleted;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}

	


	public Long getUserId() {
		return userId;
	}



	public void setUserId(Long userId) {
		this.userId = userId;
	}



	public Long getAccessRuleId() {
		return accessRuleId;
	}



	public void setAccessRuleId(Long accessRuleId) {
		this.accessRuleId = accessRuleId;
	}



	public Integer getIsDeleted() {
		return isDeleted;
	}



	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}



	public UserAccessRule() {
		
	}

}
