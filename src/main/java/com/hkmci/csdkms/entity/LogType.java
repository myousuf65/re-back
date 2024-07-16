package com.hkmci.csdkms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "logtypes")
public class LogType {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@Column(name = "logtype_name", nullable = false)  
	private Long logtypeName;
	
	@Column(name = "logtype_desc", nullable = true)  
	private Long logtypeDesc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLogtypeName() {
		return logtypeName;
	}

	public void setLogtypeName(Long logtypeName) {
		this.logtypeName = logtypeName;
	}

	public Long getLogtypeDesc() {
		return logtypeDesc;
	}

	public void setLogtypeDesc(Long logtypeDesc) {
		this.logtypeDesc = logtypeDesc;
	}
	
	
}
