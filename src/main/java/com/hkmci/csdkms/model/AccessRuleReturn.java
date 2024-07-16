package com.hkmci.csdkms.model;

import java.io.Serializable;


public class AccessRuleReturn implements Serializable{
 

	    /**
	 * 
	 */
	private static final long serialVersionUID = -8906734455133971892L;
	
	private Long id;
	
    private String description;
    

    public AccessRuleReturn() {};

    public AccessRuleReturn(Long id, String desc) {
        this.id = id;
        this.description = desc;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}
