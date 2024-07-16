package com.hkmci.csdkms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_dog")
public class UserDog {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;

	@Column(name = "user_dog", nullable = false)
	private String userDog;
	
	@Column(name="created_at", nullable= false)
	private Date createdAt;
	
	@Column(name="created_by", nullable= false)
	private Long createdBy;
	
	@Column(name="dog_level1", nullable= false)
	private String dogLevel1;
	
	@Column(name="dog_level2", nullable= false)
	private String dogLevel2;
	
	@Column(name="dog_level3", nullable= false)
	private String dogLevel3;
	
	@Column(name="dog_level4", nullable= false)
	private String dogLevel4;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserDog() {
		return userDog;
	}

	public void setUserDog(String userDog) {
		this.userDog = userDog;
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

	public void setCreatedBy(Long userId) {
		this.createdBy = userId;
	}

	public String getDogLevel1() {
		return dogLevel1;
	}

	public void setDogLevel1(String dogLevel1) {
		this.dogLevel1 = dogLevel1;
	}

	public String getDogLevel2() {
		return dogLevel2;
	}

	public void setDogLevel2(String dogLevel2) {
		this.dogLevel2 = dogLevel2;
	}

	public String getDogLevel3() {
		return dogLevel3;
	}

	public void setDogLevel3(String dogLevel3) {
		this.dogLevel3 = dogLevel3;
	}

	public String getDogLevel4() {
		return dogLevel4;
	}

	public void setDogLevel4(String dogLevel4) {
		this.dogLevel4 = dogLevel4;
	}

	
	
	
}
