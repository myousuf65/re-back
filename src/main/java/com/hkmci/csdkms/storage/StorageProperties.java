package com.hkmci.csdkms.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
	//String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
	//String test_path = "./";//getClass().getClassLoader().toString();
	String test_path = System.getProperty("user.dir") + "/";
	//String test_path = System.getProperty("user.home") + "/";
	
	//Resource resource = new ClassPathResource("upload-market");
	
	//User Folder Name
	private String UserFolderName = "user_profile";
	private String resourceFolderName = "kms_resource";
	private String tempFolderName = "temp_upload";
	private String uploadBanner = "upload_banner";
	private String cocktail = "cocktail";
	private String pinIcon = "pin_icon";
	private String popOut = "pop_out";
	private String mobileUpload ="mobile_download";
	
	
    private String location = test_path + tempFolderName;
    private String location_market = test_path + resourceFolderName;
    private String user_location = test_path + UserFolderName;
    private String PDF_location = test_path + resourceFolderName + "/" + "pdf";
    private String banner_location = test_path + uploadBanner;
    private String cocktail_location = test_path + cocktail;
    private String mobile_location = test_path + mobileUpload;
    private String pin_location = test_path + pinIcon;
    private String popOut_location = test_path + popOut;
    
    //private String location_another = test_path + "upload_another";
    
    
    
    
    
    public String getResourceFolderName() {
		return resourceFolderName;
	}

	public void setResourceFolderName(String resourceFolderName) {
		this.resourceFolderName = resourceFolderName;
	}

	public String getTempFolderName() {
		return tempFolderName;
	}

	public void setTempFolderName(String tempFolderName) {
		this.tempFolderName = tempFolderName;
	}

	public String getUserFolderName() {
        return UserFolderName;
    }

    public void setUserFolderName(String UserFolderName) {
        this.UserFolderName = UserFolderName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getMarketLocation() {
        return location_market;
    }

    public void setMarketLocation(String location_market) {
        this.location_market = location_market;
    }
//    public String getAnotherLocation() {
//        return location_another;
//    }
//
//    public void setAnotherLocation(String location_another) {
//        this.location_another = location_another;
//    }

	public String getUserLocation() {
		return user_location;
	}

	public void setUserLocation(String PDF_location) {
		this.user_location = PDF_location;
	}
	
	public String getPDFLocation() {
		return PDF_location;
	}

	public void setPDFLocation(String PDF_location) {
		this.PDF_location = PDF_location;
	}

	public String getTest_path() {
		return test_path;
	}

	public void setTest_path(String test_path) {
		this.test_path = test_path;
	}

	public String getUploadBanner() {
		return uploadBanner;
	}

	public void setUploadBanner(String uploadBanner) {
		this.uploadBanner = uploadBanner;
	}

	
	
	public String getMobileUpload() {
		return mobileUpload;
	}

	public void setMobileUpload(String mobileUpload) {
		this.mobileUpload = mobileUpload;
	}

	public String getCocktail() {
		return cocktail;
	}

	public void setCocktail(String cocktail) {
		this.cocktail = cocktail;
	}

	public String getLocation_market() {
		return location_market;
	}

	public void setLocation_market(String location_market) {
		this.location_market = location_market;
	}

	public String getUser_location() {
		return user_location;
	}

	public void setUser_location(String user_location) {
		this.user_location = user_location;
	}

	public String getPDF_location() {
		return PDF_location;
	}

	public void setPDF_location(String pDF_location) {
		PDF_location = pDF_location;
	}

	public String getBannerLocation() {
		return banner_location;
	}

	public void setBannerLocation(String banner_location) {
		this.banner_location = banner_location;
	}
	
	public String getPopOut_location() {
		return popOut_location;
	}

	public void setPopOut_location(String popOut_location) {
		this.popOut_location = popOut_location;
	}

	public String getPinLocation() {
		return pin_location;
	}
	
	public void setPinLocation(String pin_location) {
		this.pin_location = pin_location;
	}
	
	public String getMobileLocation() {
		return mobile_location;
	}
	
	public void setMobileLocation(String mobile_location) {
		this.mobile_location = mobile_location;
	}
	
	
	public String getCocktailLocation() {
		return cocktail_location;
	}

	public void setCocktailLocation(String cocktail_location) {
		this.cocktail_location = cocktail_location;
	}
	
	
	
    

}
