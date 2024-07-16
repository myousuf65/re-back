package com.hkmci.csdkms.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.hkmci.csdkms.filter.StaticResourceFilter;
@SuppressWarnings("deprecation")
@Configuration
@EnableWebMvc
public class StaticResourceConfig extends WebMvcConfigurerAdapter {
	/**
	* Filter folder setting 
	* addPathPatterns：Add folder to filter 
	* excludePathPatterns：exclude filter folder
	*/
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	registry.addInterceptor(new StaticResourceFilter()).addPathPatterns("/resources/**");
		//.excludePathPatterns();
		super.addInterceptors(registry);
	}
 
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//addResourceHandler -- Request URL
		//addResourceLocations -- Static Location
		//setCacheControl -- Cache Time
		registry.addResourceHandler("/resources/**")
		.addResourceLocations("file:kms_resource/")
		.addResourceLocations("file:upload_banner/")
		.addResourceLocations("file:cocktail/")
		.addResourceLocations("file:iosdownload/")
		.addResourceLocations("file:user_profile/")
		.addResourceLocations("file:temp_upload/")
		.addResourceLocations("file:pin_icon/")
		.addResourceLocations("file:pop_out/")
		.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
		super.addResourceHandlers(registry);
	}
}
