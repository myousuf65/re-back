package com.hkmci.csdkms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="usergroup_sitefunction")

public class ACLModel {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name="id")
	private Integer id;
	
	@JsonIgnore
	@Column( name="usergroup_id")
	private Long usergroupId;
	
	@ManyToOne
	@JoinColumn( name="sitefunc_id", nullable = false, insertable = false, updatable = false)
	private SitefuncsModel sitefuncId;
	
	@JsonIgnore
	@Column(name="sitefunc_id")
	private Long siteId;
	
	@Column( name="flag_all")
	private Boolean flagAll;
	
	@Column( name="flag_sv")
	private Boolean flagSv;
	
	@Column( name="flag_add")
	private Boolean flagAdd;
	
	@Column( name="flag_upd")
	private Boolean flagUpd;
	
	@Column( name="flag_del")
	private Boolean flagDel;
	
	@Column( name="flag_download")
	private Boolean flagDownload;
	
	@Column( name="flag_search")
	private Boolean flagSearch;
	


	
	public ACLModel() {
		
	}

	public ACLModel( Integer id, Long usergroupId, SitefuncsModel sitefuncId, Boolean flagAll, Boolean flagSv, Boolean flagAdd,
			Boolean flagUpd, Boolean flagDel, Boolean flagDownload, Boolean flagSearch) {
		super();
		this.id = id;
		this.usergroupId = usergroupId;
		this.sitefuncId = sitefuncId;
		this.flagAll = flagAll;
		this.flagSv = flagSv;
		this.flagAdd = flagAdd;
		this.flagUpd = flagUpd;
		this.flagDel = flagDel;
		this.flagDownload = flagDownload;
		this.flagSearch = flagSearch;
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getUsergroupId() {
		return usergroupId;
	}

	public void setUsergroupId(Long usergroupId) {
		this.usergroupId = usergroupId;
	}

	public SitefuncsModel getSitefuncId() {
		return sitefuncId;
	}

	public void setSitefuncId(SitefuncsModel sitefuncId) {
		this.sitefuncId = sitefuncId;
	}

	public Boolean getFlagAll() {
		return flagAll;
	}

	public void setFlagAll(Boolean flagAll) {
		this.flagAll = flagAll;
	}

	public Boolean getFlagSv() {
		return flagSv;
	}

	public void setFlagSv(Boolean flagSv) {
		this.flagSv = flagSv;
	}

	public Boolean getFlagAdd() {
		return flagAdd;
	}

	public void setFlagAdd(Boolean flagAdd) {
		this.flagAdd = flagAdd;
	}

	public Boolean getFlagUpd() {
		return flagUpd;
	}

	public void setFlagUpd(Boolean flagUpd) {
		this.flagUpd = flagUpd;
	}

	public Boolean getFlagDel() {
		return flagDel;
	}

	public void setFlagDel(Boolean flagDel) {
		this.flagDel = flagDel;
	}
	
	public Long getsiteId() {
		return siteId;
	}
	
	public void setSiteId(Long SiteId) {
		this.siteId = SiteId;
		
	}

	public Boolean getFlagDownload() {
		return flagDownload;
	}

	public void setFlagDownload(Boolean flagDownload) {
		this.flagDownload = flagDownload;
	}
	
	
	public Boolean getFlagSearch() {
		return flagSearch;
	}

	public void setFlagSearch(Boolean flagSearch) {
		this.flagSearch = flagSearch;
	}
	
	@Override
	public String toString() {
		return "ACLModel [id=" + id + ", usergroupId=" + usergroupId +  ", sitefuncId=" + sitefuncId + ", flagAll="
				+ flagAll + ", flagSv=" + flagSv + ", flagAdd=" + flagAdd + ", flagUpd=" + flagUpd + ", flagDel="
				+ flagDel + ", flagDownload=" + flagDownload +", flagSearch "+ flagSearch + "]";
	}

	
	
	
	
	
	
	
	
	
	
	
}
