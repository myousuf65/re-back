package com.hkmci.csdkms.model;

import java.util.List;

import com.hkmci.csdkms.entity.AccessRule;
import com.hkmci.csdkms.entity.FileResource;

public class ResourceAccessRuleModel {
	
	private FileResource resource;
	
	private List<AccessRule> accessRuleList;

	public FileResource getResource() {
		return resource;
	}

	public void setResource(FileResource resource) {
		this.resource = resource;
	}

	public List<AccessRule> getAccessRuleList() {
		return accessRuleList;
	}

	public void setAccessRuleList(List<AccessRule> accessRuleList) {
		this.accessRuleList = accessRuleList;
	}

	public ResourceAccessRuleModel(FileResource resource, List<AccessRule> accessRuleList) {
		super();
		this.resource = resource;
		this.accessRuleList = accessRuleList;
	}
	
	public ResourceAccessRuleModel() {
		
	}
	
}
