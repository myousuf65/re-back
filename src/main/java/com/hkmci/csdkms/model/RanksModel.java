package com.hkmci.csdkms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="ranks")

public class RanksModel {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="rank_name")
	private String rankName;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column (name="rank_desc")
	private String rankDesc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column (name="sort_order")
	private Integer sortOrder;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column (name="z_rank_id")
	private Integer zRankId;
	
	
	@Column (name="rank_type_id")
	private Integer rankTypeId;
	
	public RanksModel(Long id, String rankName,  Integer rankTypeId) {
		super();
		this.id = id;
		this.rankName = rankName;


		this.rankTypeId = rankTypeId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRankName() {
		return rankName;
	}
	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
	
	public Integer getRankTypeId() {
		return rankTypeId;
	}
	public void setRankTypeId(Integer rankTypeId) {
		this.rankTypeId = rankTypeId;
	}
	@Override
	public String toString() {
		return "RanksModel [id=" + id + ", rankName=" + rankName +  ", RankTypeId=" + rankTypeId + "]";
	}
	public RanksModel(Long id, String rankName, String rankDesc, Integer sortOrder, Integer zRankId,
			Integer rankTypeId) {
		super();
		this.id = id;
		this.rankName = rankName;
		this.rankDesc = rankDesc;
		this.sortOrder = sortOrder;
		this.zRankId = zRankId;
		this.rankTypeId = rankTypeId;
	}

	public RanksModel() {
		
	}
	
}
