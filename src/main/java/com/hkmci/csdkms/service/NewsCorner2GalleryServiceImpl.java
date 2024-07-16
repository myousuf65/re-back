package com.hkmci.csdkms.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hkmci.csdkms.controller.Md5Encode;
import com.hkmci.csdkms.entity.NewsCorner2Gallery;
import com.hkmci.csdkms.entity.NewsCorner2GalleryDetail;
import com.hkmci.csdkms.repository.NewsCorner2GalleryDetailRepository;
import com.hkmci.csdkms.repository.NewsCorner2GalleryRepository;
import com.hkmci.csdkms.storage.StorageException;
import com.hkmci.csdkms.storage.StorageService;




@Service
public class NewsCorner2GalleryServiceImpl implements NewsCorner2GalleryService {

	@Autowired
	@Resource
	private NewsCorner2GalleryRepository newscorner2GalleryRepository;
	
	@Autowired
	@Resource
	private NewsCorner2GalleryDetailRepository newscorner2GalleryDetailRepository;
	
	@Autowired
	@Resource
	private StorageService storageService;
	
	
	@Override
	public List<NewsCorner2Gallery> findAll() {
		return newscorner2GalleryRepository.findAll();
	}

	@Override
	public NewsCorner2Gallery newNewsCorner2Gallery(NewsCorner2Gallery newscorner2Gallery){
		Path userFilePath = storageService.getUserFolderLocation();
		
		//System.out.println(userFilePath);
		
		String userGalleryFolder = userFilePath.toString() + "/" + newscorner2Gallery.getUserId() + "/" + newscorner2Gallery.getGalleryName();
		Path userGalleryFolderPath = Paths.get(userGalleryFolder);
		
		try {
            Files.createDirectories(userGalleryFolderPath);
        }
        catch (IOException e) {
            throw new StorageException("Could not create user folder", e);
        }
		return newscorner2GalleryRepository.saveAndFlush(newscorner2Gallery);
	}
	
	@Override
	public Optional<NewsCorner2Gallery> findById(Long id){
		return newscorner2GalleryRepository.findById(id);
	}
	
	@Override
	public Optional<NewsCorner2Gallery> findActive(Long userId) {
		
		Optional<NewsCorner2Gallery> newscorner2Gallery = newscorner2GalleryRepository.findByUserIdAndIsFinished(userId,0);
		return newscorner2Gallery;
	}
	
	@Override
	public Optional<NewsCorner2GalleryDetail> findNewsCorner2GalleryDetail(Long detailId, Long userId) {
		
		Optional<NewsCorner2GalleryDetail> newscorner2GalleryDetail = newscorner2GalleryRepository.deleteDetailById(detailId,new Date(),userId);
		return newscorner2GalleryDetail;
	}
	
	
	@Override
	public Optional<NewsCorner2Gallery> findByPostId(Long postId) {
		
		Optional<NewsCorner2Gallery> newscorner2Gallery = newscorner2GalleryRepository.findByIsFinishedAndPostId(1, postId);
		return newscorner2Gallery;
	}
	
	@Override 
	public NewsCorner2Gallery updateNewsCorner2Gallery(NewsCorner2Gallery newscorner2Gallery){
		
		return newscorner2GalleryRepository.saveAndFlush(newscorner2Gallery);
	}
	
	
	@Override
	public List<NewsCorner2GalleryDetail> findByUserId(Long userId){
		
		Optional<NewsCorner2Gallery> getNewsCorner2Gallery = newscorner2GalleryRepository.findByUserIdAndIsFinished(userId, 0);
		if(getNewsCorner2Gallery.isPresent()) {
			List<NewsCorner2GalleryDetail> fileList =  newscorner2GalleryDetailRepository.findByGalleryIdAndDeleted(getNewsCorner2Gallery.get().getId(),0);
			return fileList;
		}
		else{
			return null;
		}
	}
	
	@Override
	public List<NewsCorner2GalleryDetail> findByGalleryId(NewsCorner2Gallery gallery){
		
		//System.out.println(galleryId);
		List<NewsCorner2GalleryDetail> fileList =  newscorner2GalleryDetailRepository.findByGalleryIdAndDeleted(gallery.getId(),0);
		List<NewsCorner2GalleryDetail> return_data = fileList.stream()
											  .filter((n) ->{ 
												  String fileAddress = "resources/" + gallery.getUserId() + "/" + gallery.getGalleryName();
												  return !n.getOfilename().contains(fileAddress);
											  })
											  .map((n) -> {
												  NewsCorner2GalleryDetail new_temp = new NewsCorner2GalleryDetail();
													String fileAddress = "resources/" + gallery.getUserId() + "/" + gallery.getGalleryName();
													String ofilename = n.getNfilename();
													new_temp.setId(n.getId());
													new_temp.setGalleryId(gallery.getId());
													new_temp.setOfilename(fileAddress + "/" + ofilename); 
													return new_temp;
											  })
											  .collect(Collectors.toList());
		return return_data;
		 
	}
	
	@Override
	public NewsCorner2GalleryDetail storeToUserFolder(MultipartFile file, Long galleryId) {
		
		Optional<NewsCorner2Gallery> newscorner2Gallery = newscorner2GalleryRepository.findById(galleryId);
		
		Path userFilePath = storageService.getUserFolderLocation();
	
		String userGalleryFolder = userFilePath.toString() + "/" + newscorner2Gallery.get().getUserId() + "/" + newscorner2Gallery.get().getGalleryName();
//		System.out.println("Gallert Folder = "+ userGalleryFolder);
		
		//newscorner2Gallery.get().getGalleryName(); // no this gallery File
		
		String fileAddress = "resources/" + newscorner2Gallery.get().getUserId() + "/" + newscorner2Gallery.get().getGalleryName();
		
		Path userGalleryFolderPath = Paths.get(userGalleryFolder);
		String file_name = System.currentTimeMillis()+file.getOriginalFilename();
		
		storageService.storeNewsCorner2PhotoToLocation(file, userGalleryFolderPath, file_name);
		System.out.println("NewsCorner2GalleryServices 159 : File Original File name = "+ file.getOriginalFilename());
		
		//File Info to Database
	
//		String file_name = StringUtils.cleanPath(file.getOriginalFilename());
        String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
    	Md5Encode md5 = new Md5Encode();
        String filename = md5.getMD5(file_name)+fileType;
        String ofilename = md5.getMD5(file.getOriginalFilename())+fileType;
        
        
        NewsCorner2GalleryDetail new_record = new NewsCorner2GalleryDetail();
        new_record.setCreatedAt(new Date());
        new_record.setCreatedBy(newscorner2Gallery.get().getUserId());
        new_record.setGalleryId(newscorner2Gallery.get().getId());
        new_record.setLabel(fileAddress);
        new_record.setNfilename(filename);
        new_record.setOfilename(ofilename);
        new_record.setOrdering(0);
        
        return newscorner2GalleryDetailRepository.saveAndFlush(new_record);
	}

	@Override
	public Optional<NewsCorner2GalleryDetail> findDetailById(Long detailId) {
		// TODO Auto-generated method stub
		return newscorner2GalleryDetailRepository.findById(detailId);
	}

	@Override
	public NewsCorner2GalleryDetail saveDetail(NewsCorner2GalleryDetail detailId) {
		// TODO Auto-generated method stub
		return newscorner2GalleryDetailRepository.saveAndFlush(detailId);
	}

	
}