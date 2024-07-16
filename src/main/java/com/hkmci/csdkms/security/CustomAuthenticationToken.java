package com.hkmci.csdkms.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.hkmci.csdkms.entity.User;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    
    private static final long serialVersionUID = -1949976839306453197L;
    private User authenticatedUser;
    private Long uid;
    private String staffNo;

    public CustomAuthenticationToken(Long uid){
        super(Arrays.asList());
        this.uid = uid;        
    }

    public CustomAuthenticationToken(String staffNo){
        super(Arrays.asList());
        this.staffNo = staffNo;        
    }
    
    public CustomAuthenticationToken(Collection<? extends GrantedAuthority> authorities, User authenticatedUser, Long uid) {
        super(authorities);
        this.uid = uid;
        this.authenticatedUser = authenticatedUser;
    }

    @Override
    public Object getCredentials() {
//        return authenticatedUser.getPassword();
    	return true;
    }

    @Override
    public Object getPrincipal() {
        return authenticatedUser;
    }

    public Long getUid() {
        return uid;
    }
    
    public String getStaffNo() {
    	return this.staffNo;
    }

}
