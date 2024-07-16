package com.hkmci.csdkms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="resource_hit_rate")
public class ResourceHitRate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false)
	private Long id;
	
	@Column(name="resource_id", nullable =false)
	private Long resourceId ;
	
	@Column(name = "hit_rate", nullable =false)
	private Integer hitRate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public Integer getHitRate() {
		return hitRate;
	}

	public void setHitRate(Integer hitRate) {
		this.hitRate = hitRate;
	}
	
	
	public ResourceHitRate (Long id, Long resourceId, Integer hitRate) {
		super();
		this.id = id ;
		this.resourceId = resourceId;
		this.hitRate = hitRate;
	}
	
	public ResourceHitRate() {
		
	}
}
