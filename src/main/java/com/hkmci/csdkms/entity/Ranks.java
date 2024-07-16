package com.hkmci.csdkms.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "ranks")
public class Ranks {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "rank_name", nullable = false)
	private String rankName;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "rank_desc", nullable = false)
	private String rankDesc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "sort_order", nullable = false)
	private Integer sortOrder;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "z_rank_id", nullable = false)
	private Integer zRankId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "rank_type_id", nullable = false)
	private Integer rankTypeId;
	
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

    public String getRankDesc() {
        return rankDesc;
    }

    public void setRankDesc(String rankDesc) {
        this.rankDesc = rankDesc;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getZRankId() {
        return zRankId;
    }

    public void setZRankId(Integer zRankId) {
        this.zRankId = zRankId;
    }

    public Integer getRankTypeId() {
        return rankTypeId;
    }

    public void setRankTypeId(Integer rankTypeId) {
        this.rankTypeId = rankTypeId;
    }
}
