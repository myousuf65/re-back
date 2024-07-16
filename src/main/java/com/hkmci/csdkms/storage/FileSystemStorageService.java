package com.hkmci.csdkms.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hkmci.csdkms.controller.Md5Encode;





@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private final Path MarketLocation;
   
//    private final Path AnotherLocation;
    private final Path UserFolderLocation;
    private final Path ResourcePDFLocation;
    private final Path BannerLocation;
    private final Path PopOutLocation;
    private final Path PinLocation;
    private final Path CocktailLocation;
    private final String UserFolderName;
    private final String ResourceFolderName;
    private final String TempFolderName;
    private final String BannerFolderName;
    private final String CocktailFolderName;
    private final Path MobileLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.MarketLocation = Paths.get(properties.getMarketLocation());
        this.PinLocation = Paths.get(properties.getPinLocation());
//        this.AnotherLocation = Paths.get(properties.getAnotherLocation());
        this.UserFolderLocation = Paths.get(properties.getUserLocation());
        this.ResourcePDFLocation = Paths.get(properties.getPDFLocation());
        this.BannerLocation = Paths.get(properties.getBannerLocation());
        this.MobileLocation = Paths.get(properties.getMobileLocation());
        this.CocktailLocation = Paths.get(properties.getCocktailLocation());
        this.UserFolderName = properties.getUserFolderName();
        this.ResourceFolderName = properties.getResourceFolderName();
        this.TempFolderName = properties.getTempFolderName();
        this.BannerFolderName = properties.getUploadBanner();
        this.CocktailFolderName = properties.getCocktailLocation();
        this.PopOutLocation = Paths.get(properties.getPopOut_location());
       
    }
    
    public Path getTempLocation () {
    	return this.rootLocation;
    }
    public Path getCocktailLocation() {
    	return this.CocktailLocation;
    }
    public Path getUserFolderLocation() {
    	return this.UserFolderLocation;
    }
    
    public Path getResourceLocation() {
    	//System.out.println("I am here");
    	return this.MarketLocation;
    }
    
    
    public Path getMobileLocation() {
    	return this.MobileLocation;
    }
    
    
    @Override
    public String storeMobile(MultipartFile file,String app ) {
    	//System.out.println("Update resource !!  "+file.getOriginalFilename()+ " " );
    	System.out.println("Mobile Name = "+ file.getOriginalFilename());
    	String filename = file.getOriginalFilename();
//    	String file_name = StringUtils.cleanPath(file.getOriginalFilename());
//    	String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
//    	Md5Encode md5 = new Md5Encode();
//        String filename = md5.getMD5(file_name)+fileType;
       
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            System.out.println("Mobile file location = "+this.MobileLocation);
            Path store_path = Paths.get(this.MobileLocation+ "\\"+app);
            System.out.println("Mobile Store path = "+ store_path);
            try (InputStream inputStream = file.getInputStream()) {
            	//System.out.println(" 3 STEP to store resoruce  stroe in + "+rootLocation);
//                Files.copy(inputStream, this.MobileLocation.resolve(filename),
//                    StandardCopyOption.REPLACE_EXISTING);
            	 Files.copy(inputStream, store_path.resolve(filename),
                         StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return filename;	
    }
    
    @Override
    public String storePin(MultipartFile file) {
    	//System.out.println("Update resource !!  "+file.getOriginalFilename()+ " " );
    	
    	String file_name = StringUtils.cleanPath(file.getOriginalFilename());
    	String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
    	Md5Encode md5 = new Md5Encode();
        String filename = md5.getMD5(file_name)+fileType;
       
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
            	//System.out.println(" 3 STEP to store resoruce  stroe in + "+rootLocation);
                Files.copy(inputStream, this.PinLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return filename;
    }
    
    @Override 
    public String storePopOut(MultipartFile file) {
    	String file_name = StringUtils.cleanPath(file.getOriginalFilename());
    	String fileType = file_name.substring(file_name.lastIndexOf("."),file_name.length());
    	Md5Encode md5 = new Md5Encode();
    	String filename = md5.getMD5(file_name)+fileType;
    	try {
    		if (file.isEmpty()) {
    			throw new  StorageException("Failed to store empry file "+file_name);
    		}
    		if (filename.contains("..")) {
    			throw new StorageException("Cannot store file with relative path outside current directory "
    					+ filename);
    		}
    		try (InputStream inputStream = file.getInputStream()){
    			Files.copy(inputStream, this.PopOutLocation.resolve(filename),
    					StandardCopyOption.REPLACE_EXISTING);
    		
    		}
    	} 
    	catch (IOException e) {
    		throw new StorageException("Failed to store file "+ filename, e);
    	}
    	
    	return filename;
    }
    
    @Override
    public String storeBanner(MultipartFile file) {
    	//System.out.println("Update resource !!  "+file.getOriginalFilename()+ " " );
    	
    	String file_name = StringUtils.cleanPath(file.getOriginalFilename());
    	String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
    	Md5Encode md5 = new Md5Encode();
        String filename = md5.getMD5(file_name)+fileType;
       
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
            	//System.out.println(" 3 STEP to store resoruce  stroe in + "+rootLocation);
                Files.copy(inputStream, this.BannerLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return filename;
    }
    
    
    
    @Override
    public void store(MultipartFile file) {
    	//System.out.println("Update resource !!  "+file.getOriginalFilename()+ " " );
    	
    	
    	String file_name = StringUtils.cleanPath(file.getOriginalFilename());
    	String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
    	Md5Encode md5 = new Md5Encode();
        String filename = md5.getMD5(file_name)+fileType;
       
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
            	//System.out.println(" 3 STEP to store resoruce  stroe in + "+rootLocation);
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }
    
    @Override
    public void storeBlogPhotoToLocation(MultipartFile file,Path location,String file_name_org) {
    	String file_name = StringUtils.cleanPath(file.getOriginalFilename());
    	 file_name = System.currentTimeMillis()+file.getOriginalFilename();
    	 file_name = file_name_org;
    	String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
    	Md5Encode md5 = new Md5Encode();
        String filename = md5.getMD5(file_name)+fileType;
        System.out.println("File System Storage = ");
        
        
        try {
            if (file.isEmpty()) {
            	 throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
            	
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
//            try (InputStream inputStream = file.getInputStream()) {
//            	System.out.println("Before file copy"+ inputStream + "  file name -- "+ filename+ " location -- "+ location);
//                Files.copy(inputStream, location.resolve(filename),
//                    StandardCopyOption.REPLACE_EXISTING);
//            	System.out.println("After file copy");
//            }
//            
            try (InputStream inputStream = file.getInputStream()) {
            	
            	 Files.copy(inputStream, location.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            	
            }
            
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file  -  " + filename, e);
        }
    }
    
    @Override
    public void storeElearningPhotoToLocation(MultipartFile file,Path location,String file_name_org) {
    	String file_name = StringUtils.cleanPath(file.getOriginalFilename());
    	 file_name = System.currentTimeMillis()+file.getOriginalFilename();
    	 file_name = file_name_org;
    	String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
    	Md5Encode md5 = new Md5Encode();
        String filename = md5.getMD5(file_name)+fileType;
        System.out.println("File System Storage = ");
        
        
        try {
            if (file.isEmpty()) {
            	 throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
            	
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
//            try (InputStream inputStream = file.getInputStream()) {
//            	System.out.println("Before file copy"+ inputStream + "  file name -- "+ filename+ " location -- "+ location);
//                Files.copy(inputStream, location.resolve(filename),
//                    StandardCopyOption.REPLACE_EXISTING);
//            	System.out.println("After file copy");
//            }
//            
            try (InputStream inputStream = file.getInputStream()) {
            	
            	 Files.copy(inputStream, location.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            	
            }
            
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file  -  " + filename, e);
        }
    }
    
    @Override
    public void storeNewsCorner2PhotoToLocation(MultipartFile file,Path location,String file_name_org) {
    	String file_name = StringUtils.cleanPath(file.getOriginalFilename());
    	 file_name = System.currentTimeMillis()+file.getOriginalFilename();
    	 file_name = file_name_org;
    	String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
    	Md5Encode md5 = new Md5Encode();
        String filename = md5.getMD5(file_name)+fileType;
        System.out.println("File System Storage = ");
        
        
        try {
            if (file.isEmpty()) {
            	 throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
            	
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
//            try (InputStream inputStream = file.getInputStream()) {
//            	System.out.println("Before file copy"+ inputStream + "  file name -- "+ filename+ " location -- "+ location);
//                Files.copy(inputStream, location.resolve(filename),
//                    StandardCopyOption.REPLACE_EXISTING);
//            	System.out.println("After file copy");
//            }
//            
            try (InputStream inputStream = file.getInputStream()) {
            	
            	 Files.copy(inputStream, location.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            	
            }
            
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file  -  " + filename, e);
        }
    }
    
    
    @Override
    public void storeToLocation(MultipartFile file,Path location) {
    	String file_name = StringUtils.cleanPath(file.getOriginalFilename());
    	String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
    	Md5Encode md5 = new Md5Encode();
        String filename = md5.getMD5(file_name)+fileType;
        System.out.println("File System Storage = ");
        
        
        try {
            if (file.isEmpty()) {
            	 throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
            	
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
//            try (InputStream inputStream = file.getInputStream()) {
//            	System.out.println("Before file copy"+ inputStream + "  file name -- "+ filename+ " location -- "+ location);
//                Files.copy(inputStream, location.resolve(filename),
//                    StandardCopyOption.REPLACE_EXISTING);
//            	System.out.println("After file copy");
//            }
//            
            try (InputStream inputStream = file.getInputStream()) {
            	
            	 Files.copy(inputStream, location.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            	
            }
            
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file  -  " + filename, e);
        }
    }
    
    @Override
    public void copyToPath(MultipartFile file, String location) {
    	
    	
    	String file_name = StringUtils.cleanPath(file.getOriginalFilename());
    	String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
    	Md5Encode md5 = new Md5Encode();
        String filename = md5.getMD5(file_name)+fileType;
        
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
            	if(location == "market") {
            		Files.copy(inputStream, this.MarketLocation.resolve(filename),
                            StandardCopyOption.REPLACE_EXISTING);
            	}else {
//            		Files.copy(inputStream, this.AnotherLocation.resolve(filename),
//                            StandardCopyOption.REPLACE_EXISTING);
            	}
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file -  " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }
    
    @Override
    public Stream<Path> loadMarketAll() {
        try {
            return Files.walk(this.MarketLocation, 1)
                .filter(path -> !path.equals(this.MarketLocation))
                .map(this.MarketLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }
    
    @Override
    public String getPath(String filename) {
        return rootLocation.resolve(filename).toString();
    }
    
    @Override
    public Path getMarketPath(String filename) {
        return MarketLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
            Files.createDirectories(MarketLocation);
//            Files.createDirectories(AnotherLocation);
            Files.createDirectories(UserFolderLocation);
            Files.createDirectories(ResourcePDFLocation);
            Files.createDirectories(BannerLocation);
            Files.createDirectories(PopOutLocation);
            Files.createDirectories(CocktailLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    
    
	@Override
	public String getUserFolderName() {
		// TODO Auto-generated method stub
		return this.UserFolderName;
	}

	@Override
	public String getResourceFolderName() {
		// TODO Auto-generated method stub
		return this.ResourceFolderName;
	}

	@Override
	public String getTempFolderName() {
		// TODO Auto-generated method stub
		return this.TempFolderName;
	}
}
