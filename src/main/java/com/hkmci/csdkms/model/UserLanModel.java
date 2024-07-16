package com.hkmci.csdkms.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="usr_lang")
public class UserLanModel {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int Id;
	@Column(name="userid")
	private int UserId;
	@Column(name="language")
	private String language;
	@Column(name="created_at")
	private LocalDateTime CreateAt;
	
	public UserLanModel() {
		
	}
	public UserLanModel(int id, int userId, String language, LocalDateTime createAt) {
		
		
		UserId = userId;
		this.language = language;
		CreateAt = createAt;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public LocalDateTime getCreateAt() {
		return CreateAt;
	}
	public void setCreateAt(String createAt) {
		CreateAt = LocalDateTime.now();
	}
	@Override
	public String toString() {
		return "UserLanModel [Id=" + Id + ", UserId=" + UserId + ", language=" + language + ", CreateAt=" + CreateAt
				+ "]";
	}
	
	
	
	
}
