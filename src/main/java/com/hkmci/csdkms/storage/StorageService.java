package com.hkmci.csdkms.storage;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;
@Service
public interface StorageService {

    void init();

    void store(MultipartFile file);
    
    void copyToPath(MultipartFile file, String location);

    Stream<Path> loadAll();
    
    Stream<Path> loadMarketAll();

    Path load(String filename);
    
    String getPath(String filename);
    
    Path getMarketPath(String filename);
    
    Path getResourceLocation();
    
    Path getMobileLocation();
    
    Path getTempLocation();

    Resource loadAsResource(String filename);
    
    Path getUserFolderLocation();
    
    Path getCocktailLocation();
    
    String getUserFolderName();
    
    String getResourceFolderName();
    
    String getTempFolderName();

    void deleteAll();

	void storeToLocation(MultipartFile file, Path location);

	String storeBanner(MultipartFile file);

	String storeMobile(MultipartFile file,String app);

	void storeBlogPhotoToLocation(MultipartFile file, Path location,String file_name);
	
	void storeNewsCorner2PhotoToLocation(MultipartFile file, Path location,String file_name);

	String storePin(MultipartFile file);

	String storePopOut(MultipartFile file);

	void storeElearningPhotoToLocation(MultipartFile file, Path userGalleryFolderPath, String file_name);

}
