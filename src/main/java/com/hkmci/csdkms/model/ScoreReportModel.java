package com.hkmci.csdkms.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ScoreReportModel {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long instId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String institution;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer total;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer lv1;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer lv2;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer lv3;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer lv4;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer score;
	
	public Long getInstId() {
		return instId;
	}
	public void setInstId(Long instId) {
		this.instId = instId;
	}
	
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	public Integer getLv1() {
		return lv1;
	}
	
	public void setLv1(Integer lv1) {
		this.lv1 = lv1;
	}
	
	public Integer getLv2() {
		return lv2;
	}
	public void setLv2 (Integer lv2) {
		this.lv2=lv2;
	}
	
	public Integer getLv3() {
		return lv3;
	}
	public void setLv3(Integer lv3) {
		this.lv3=lv3;
	}
	
	public Integer getLv4() {
		return lv4;
	}
	public void setLv4(Integer lv4) {
		this.lv4 = lv4;
	}
	
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	
	
	public ScoreReportModel() {
		
	}
	
	public ScoreReportModel(Long instId, String institution,Integer total,
			Integer lv1, Integer lv2, Integer lv3, Integer lv4) {
		this.instId = instId;
		this.institution = institution;
		this.total = total;
		this.lv1 = lv1;
		this.lv2 = lv2;
		this.lv3 = lv3;
		this.lv4 = lv4;
	}
}
