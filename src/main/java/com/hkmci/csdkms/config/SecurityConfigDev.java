package com.hkmci.csdkms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.util.StringUtils;

//import com.hkmci.csdkms.config.SecurityConfig.WebConfiguration;
import com.hkmci.csdkms.filter.CustomAuthenticationFilter;
import com.hkmci.csdkms.security.JwtAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@Order(102)
@EnableGlobalMethodSecurity(securedEnabled = true)

@Profile({ "dev_ssl", "dev_local","developer" })
public class SecurityConfigDev extends WebSecurityConfigurerAdapter {
    private Logger logger = LoggerFactory.getLogger(SecurityConfigDev.class);

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Value("${app.loginPageURL}")
    private String loginPageURL;

    	static SessionRegistry SR;
    	@Override
	    protected void configure(HttpSecurity http) throws Exception {
  		  	logger.info("Run Order 1 - Remote Access auth - dev_local");

			http
//				.antMatcher("/auth/**")
                .cors()
                    .and()
                .csrf()
					.disable() // we don't need CSRF because our token is invulnerable
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
					.and()
                .authorizeRequests()
	        	    .antMatchers(HttpMethod.GET, "/auth/**").denyAll()
	        	    .antMatchers(HttpMethod.POST, "/**").denyAll()
                    .anyRequest().authenticated();

			http.sessionManagement()
		        .maximumSessions(1)
		        .maxSessionsPreventsLogin(false)
		        .expiredUrl("/")
		        .sessionRegistry(new SessionRegistryImpl());
			
	    }
	
	    @Override
	    public void configure(AuthenticationManagerBuilder auth) throws Exception {
	    	//Good to use BcryptEncoder for spring 5.0
	        auth

					//Dev with Manager
	                .ldapAuthentication()
	                	.userDetailsContextMapper(userDetailsContextMapperDev())
		                .userDnPatterns("uid={0},ou=People")
		                .userSearchBase("ou=People")
						.contextSource()
						.url("ldap://139.162.35.173:389/dc=maxcrc,dc=com");

	    }   


		@Bean(BeanIds.AUTHENTICATION_MANAGER)
		@Profile({ "dev_ssl", "dev_local","developer" })
	    @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }
		
	    @Bean
	    public UserDetailsContextMapper userDetailsContextMapperDev() {
	        return new CustomUserDetailsContextMapper();
		}
		

}
