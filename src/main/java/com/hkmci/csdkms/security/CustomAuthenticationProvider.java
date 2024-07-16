package com.hkmci.csdkms.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.exception.UnknownUserException;
import com.hkmci.csdkms.filter.CustomAuthenticationFilter;
import com.hkmci.csdkms.repository.UserRepository;
import com.hkmci.csdkms.service.UserService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
    private final static Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);

    // This would be a JPA repository to snap your user entities
    private final UserRepository userRepository;
    
    private final UserService userService;
    
    @Autowired
    public CustomAuthenticationProvider(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }    
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
    	logger.info("Starting authenticate...");
        CustomAuthenticationToken auth = (CustomAuthenticationToken) authentication;        
//        User user = userRepository.find(auth.getUid());
        User user = userService.findByStaffNo(auth.getStaffNo());
    	logger.info("Auth user:"+auth.getStaffNo());
        
        if(user == null){
            try {
				throw new UnknownUserException("Could not find user with ID: " + auth.getUid());
			} catch (UnknownUserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            auth.setAuthenticated(false);
            logger.info("Auth - False");
        }else {
            auth.setAuthenticated(true);
            logger.info("Auth - True");
        }
        	
        return auth;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
