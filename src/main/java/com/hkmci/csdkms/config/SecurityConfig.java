package com.hkmci.csdkms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.filter.CustomAuthenticationFilter;
import com.hkmci.csdkms.security.CustomAuthenticationProvider;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.security.JwtAuthenticationEntryPoint;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@Order(101)
@Profile({ "prod", "uat" })
@EnableWebSecurity
public class SecurityConfig {

	@Order(2)
	@Configuration
	public static class WebConfiguration extends WebSecurityConfigurerAdapter {
		@Autowired
		private CustomAuthenticationProvider authProvider;

		@Autowired
		private JwtAuthenticationEntryPoint unauthorizedHandler;

		@Autowired
		private Common theCommon;

		@Autowired
		private LogService theReportLogger;

		private Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

		@Value("${app.loginPageURL}")
		private String loginPageURL;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			logger.info("Run Order 2 - DP access - ");

			http
					.authorizeRequests()
					.anyRequest().permitAll()
					.and()
					.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
					.and()
					.csrf().disable()
					.headers()
					.frameOptions()
					.sameOrigin();
//    	   http
//    	    .antMatcher("/**")
//    	    .addFilterBefore(new CustomAuthenticationFilter(theCommon, theReportLogger), BasicAuthenticationFilter.class)
//    	    .authorizeRequests()
//    	    .anyRequest().authenticated()
//    	    .and()
//    	    .authorizeRequests()
//    	    .antMatchers(HttpMethod.GET, "/**").permitAll()
//			.antMatchers(HttpMethod.POST, "/**").permitAll()
//			.and()
//			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
//    	    .and()
//    	    	.formLogin()
//    	    	.loginPage(loginPageURL)
////    	    	.defaultSuccessUrl("/sessions/newheader")
////    	    	.failureUrl("/api/fail")
//    	    	.permitAll()
//    	    .and()
//    	    	.csrf().disable()
//    	    .headers()
//            .frameOptions()
//            .sameOrigin()
//    	    	;
//
//     	    http.exceptionHandling().accessDeniedPage("/403");
//
//			 http.sessionManagement()
//				.maximumSessions(10)
//				.maxSessionsPreventsLogin(false)
//				.expiredUrl("/auth/logout")
//				.sessionRegistry(new SessionRegistryImpl());
//
		}

		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			logger.info("DP and Remote Access auth");
			auth.authenticationProvider(authProvider);

		}
	}



	@Order(1)
	@Configuration
	public static class RestConfiguration extends WebSecurityConfigurerAdapter {
		@Autowired
		private JwtAuthenticationEntryPoint unauthorizedHandler;

		//Getting values from properties file
		@Value("${spring.ldap.urls}")
		private String ldapUrls;
		@Value("${spring.ldap.base-dn}")
		private String ldapBaseDn;

		private Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

		// @Autowired
		// @Qualifier("sessionRegistry")
		// private SessionRegistry sessionRegistry;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			logger.info("Run Order 1 - Remote Access auth - ");
			http
					// .antMatcher("/auth/**")
					// .authorizeRequests()
					// 	.anyRequest().authenticated();

//				.antMatcher("/auth/**")
//                .cors()
//                    .and()
//                .csrf()
//					.disable() // we don't need CSRF because our token is invulnerable
//				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
//					.and()
//                .authorizeRequests()
//	        	    .antMatchers(HttpMethod.GET, "/**").permitAll()
//	        	    .antMatchers(HttpMethod.POST, "/**").permitAll()
//					.anyRequest().authenticated();

//			http.sessionManagement()
//				.maximumSessions(1)
//				.maxSessionsPreventsLogin(false)
//				.expiredUrl("/auth/logout")
//				.sessionRegistry(new SessionRegistryImpl());

					.antMatcher("/auth/**")
					.cors()
					.and()
					.csrf()
					.disable() // we don't need CSRF because our token is invulnerable
					.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
					.and()
					.authorizeRequests()
					.antMatchers(HttpMethod.GET, "/**").permitAll()
					.antMatchers(HttpMethod.POST, "/**").permitAll()
					.anyRequest().authenticated();
		}

		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			//Good to use BcryptEncoder for spring 5.0
			auth
					//Dev with Manager
					.ldapAuthentication()
					.userDnPatterns("uid={0},ou=people,ou=users,o=csd,dc=ccgo,dc=hksarg")
//	                    .userDnPatterns("uid={0},ou=people")
					.userSearchBase("ou=people,ou=users,o=csd") //don't add the base
					.userSearchBase("ou=groups")
					.userSearchFilter("uid={0}")
					.contextSource(getContextSource())
					.userDetailsContextMapper(userDetailsContextMapper());

		}


		@Bean(BeanIds.AUTHENTICATION_MANAGER)
		@Profile({ "prod", "uat" })
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}
/* ---- For Production 		   
	    @Bean
	    public LdapContextSource getContextSource() {
	    	LdapContextSource contextSource = new LdapContextSource();
	        // contextSource.setUrl("ldap://[IP goes here]:[port goes here]");
	        // contextSource.setBase("dc=mycompany,dc=com");
	        // contextSource.setUserDn("cn=aUserUid,dc=mycompany,dc=com");
	        // contextSource.setPassword("hisPassword");
	        // contextSource.afterPropertiesSet(); //needed otherwise you will have a NullPointerException in spring

//	  		contextSource.setUrl(ldapUrls);
//	        contextSource.setBase(ldapBaseDn);
//	        contextSource.setUserDn("uid=ldapro3,ou=people,ou=users,o=csd,dc=ccgo,dc=hksarg");
//	        contextSource.setPassword("Kmsld@p4651");
//	        contextSource.afterPropertiesSet(); //needed otherwise you will have a NullPointerException in spring

	    	contextSource.setUrl(ldapUrls);
	        contextSource.setBase(ldapBaseDn);
	        contextSource.setUserDn("uid=ldapro-kms,ou=people,ou=ServiceAccount,ou=users,o=csd,dc=ccgo,dc=hksarg");
//	        contextSource.setUserDn("uid=ldapro-kms,ou=people,ou=ServiceAccount,ou=users,o=csd,dc=ccgo,dc=hksarg");
	        contextSource.setPassword("KMSpwd01");
//	        contextSource.setPassword("2Nd31Yn#");
	        contextSource.afterPropertiesSet(); //needed otherwise you will have a NullPointerException in spring

	        
	        return contextSource;
	    }			    
	    */


		/* Fro UAT

                @Bean
                public LdapContextSource getContextSource() {
                    LdapContextSource contextSource = new LdapContextSource();

                    contextSource.setUrl(ldapUrls);
                    contextSource.setBase(ldapBaseDn);
                    contextSource.setUserDn("uid=ldapro-kms,ou=people,ou=ServiceAccount,ou=users,o=csd,dc=ccgo,dc=hksarg");
                    contextSource.setPassword("2Nd31Yn#");
                    contextSource.afterPropertiesSet(); //needed otherwise you will have a NullPointerException in spring


                    return contextSource;
                }
         */
		@Bean
		public LdapContextSource getContextSource() {
			String springProfilesActive="";
			try {
				// 加載XML文件
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse("csdkms.xml");

				// 獲取根元素
				Element root = document.getDocumentElement();

				// 獲取arguments元素
				NodeList argumentsList = root.getElementsByTagName("arguments");
				Element argumentsElement = (Element) argumentsList.item(0);

				// 獲取-Dspring.profiles.active的值
				springProfilesActive = argumentsElement.getTextContent().split("-Dspring.profiles.active=")[1].split(" ")[0];
				System.out.println("-Dspring.profiles.active: " + springProfilesActive);


			} catch (Exception e) {
				e.printStackTrace();
			}
			LdapContextSource contextSource = new LdapContextSource();

			System.out.println("ldapUrls: " + ldapUrls);

			if (springProfilesActive.contentEquals("developer") || springProfilesActive.contentEquals("uat")) {
				contextSource.setUrl(ldapUrls);
				contextSource.setBase(ldapBaseDn);
				contextSource.setUserDn("uid=ldapro-kms,ou=people,ou=ServiceAccount,ou=users,o=csd,dc=ccgo,dc=hksarg");
//		    	contextSource.setUserDn("uid=ben,dc=maxcrc,dc=com");
//		        contextSource.setPassword("2Nd31Yn#");
				contextSource.setPassword("secret");
				contextSource.afterPropertiesSet(); //needed otherwise you will have a NullPointerException in spring
			} else if(springProfilesActive.contentEquals("prod") || springProfilesActive.contentEquals("prod_second")) {
				contextSource.setUrl(ldapUrls);
				contextSource.setBase(ldapBaseDn);
				contextSource.setUserDn("uid=ldapro-kms,ou=people,ou=ServiceAccount,ou=users,o=csd,dc=ccgo,dc=hksarg");
				contextSource.setPassword("KMSpwd01");
				contextSource.afterPropertiesSet();
			}

			return contextSource;
		}

		@Bean
		public UserDetailsContextMapper userDetailsContextMapper() {
			return new CustomUserDetailsContextMapper();
		}

		@Bean
		public HttpSessionEventPublisher httpSessionEventPublisher() {
			return new HttpSessionEventPublisher();
		}

		@Bean
		public SessionRegistry sessionRegistry() {
			return new SessionRegistryImpl();
		}
	}

}
