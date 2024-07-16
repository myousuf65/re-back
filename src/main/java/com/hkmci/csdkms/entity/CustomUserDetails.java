package com.hkmci.csdkms.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;

import java.util.Collection;

public class CustomUserDetails implements LdapUserDetails {

//public class CustomUserDetails extends User implements UserDetails{
//public class CustomUserDetails implements LdapUserDetails{

	private static final long serialVersionUID = 1L;
	
	private LdapUserDetailsImpl attributes;


	private String yearJoiningGovernment;	
	private String staffNo;
	private String dateOfBirth;
	
    public CustomUserDetails(LdapUserDetailsImpl attributes, String year_joining_government, String staff_no, String date_of_birth) {
        this.attributes = attributes;
        this.yearJoiningGovernment = year_joining_government;
        this.staffNo = staff_no;
        this.dateOfBirth = date_of_birth;
    }

    public String getYearJoiningGovernment() {
		return yearJoiningGovernment;
	}

	public void setYearJoiningGovernment(String yearJoiningGovernment) {
		this.yearJoiningGovernment = yearJoiningGovernment;
	}

	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
    @Override
    public String getDn() {
        return attributes.getDn();
    }

    @Override
    public void eraseCredentials() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return attributes.getAuthorities();
    }

    @Override
    public String getPassword() {
        return attributes.getPassword();
    }

    @Override
    public String getUsername() {
        return attributes.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return attributes.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return attributes.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return attributes.isEnabled();
    }
}