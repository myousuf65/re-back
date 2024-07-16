package com.hkmci.csdkms.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Component;

import com.hkmci.csdkms.entity.CustomUserDetails;

//Ori - @Configuration
@Component
public class CustomUserDetailsContextMapper extends LdapUserDetailsMapper implements UserDetailsContextMapper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
  
    @Autowired
    private Environment environment;

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {

        LdapUserDetailsImpl details = (LdapUserDetailsImpl) super.mapUserFromContext(ctx, username, authorities);
//        LdapUserDetails details = (LdapUserDetails) super.mapUserFromContext(ctx, username, authorities);
        logger.info("DN from ctx: " + ctx.getDn()); // return correct DN
        logger.info("Attributes size: " + ctx.getAttributes().size()); // always returns 0
        
        Attributes attributes = ctx.getAttributes();

        NamingEnumeration<? extends Attribute> namingEnumeration = attributes.getAll();

//      Print out the attributes in LDAP
        try {
            while(namingEnumeration.hasMore()){ 
                Attribute attribute = namingEnumeration.next();
                //System.out.println(attribute); 
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
		logger.info("year-joining-government: " + ctx.getStringAttribute("year-joining-government"));
		logger.info("employeeNumber: " + ctx.getStringAttribute("employeeNumber"));
		logger.info("String dob: " + ctx.getStringAttribute("date-of-birth"));

        String year_joining_government = null;
        String employeeNumber = null;
        String date_of_birth = null;
        
        try {
        	year_joining_government = ctx.getStringAttribute("year-joining-government");
        	employeeNumber = ctx.getStringAttribute("employeeNumber");
        	date_of_birth = ctx.getStringAttribute("date-of-birth");
        }
        catch (Exception ex) {
            // TODO: Handle the exception!
        }

        List<String> listProfiles = Arrays.asList(environment.getActiveProfiles());
        if (listProfiles.contains("prod") || listProfiles.contains("uat")){
            logger.info("Env Profile loaded Prod" );
            return new CustomUserDetails(details, year_joining_government, employeeNumber, date_of_birth);
        }else{
            logger.info("Env Profile loaded DEV_LOCAL" );
            return new CustomUserDetails(details, "2012", "12240", "05061988");
        }
    }

    @Override
    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
        // default
    }
}
//public class CustomUserDetailsContextMapper implements UserDetailsContextMapper {
//
//	@Override
//	public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
//			Collection<? extends GrantedAuthority> authorities) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
