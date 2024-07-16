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
import com.hkmci.csdkms.entity.BlogGallery;
import com.hkmci.csdkms.entity.BlogGalleryDetail;
import com.hkmci.csdkms.repository.BlogGalleryDetailRepository;
import com.hkmci.csdkms.repository.BlogGalleryRepository;
import com.hkmci.csdkms.storage.StorageException;
import com.hkmci.csdkms.storage.StorageService;




@Service
public class BlogGalleryServiceImpl implements BlogGalleryService {

	@Autowired
	@Resource
	private BlogGalleryRepository blogGalleryRepository;
	
	@Autowired
	@Resource
	private BlogGalleryDetailRepository blogGalleryDetailRepository;
	
	@Autowired
	@Resource
	private StorageService storageService;
	
	
	@Override
	public List<BlogGallery> findAll() {
		return blogGalleryRepository.findAll();
	}

	@Override
	public BlogGallery newBlogGallery(BlogGallery blogGallery){
		Path userFilePath = storageService.getUserFolderLocation();
		
		//System.out.println(userFilePath);
		
		String userGalleryFolder = userFilePath.toString() + "/" + blogGallery.getUserId() + "/" + blogGallery.getGalleryName();
		Path userGalleryFolderPath = Paths.get(userGalleryFolder);
		
		try {
            Files.createDirectories(userGalleryFolderPath);
        }
        catch (IOException e) {
            throw new StorageException("Could not create user folder", e);
        }
		return blogGalleryRepository.saveAndFlush(blogGallery);
	}
	
	@Override
	public Optional<BlogGallery> findById(Long id){
		return blogGalleryRepository.findById(id);
	}
	
	@Override
	public Optional<BlogGallery> findActive(Long userId) {
		
		Optional<BlogGallery> blogGallery = blogGalleryRepository.findByUserIdAndIsFinished(userId,0);
		return blogGallery;
	}
	
	@Override
	public Optional<BlogGalleryDetail> findBlogGalleryDetail(Long detailId, Long userId) {
		
		Optional<BlogGalleryDetail> blogGalleryDetail = blogGalleryRepository.deleteDetailById(detailId,new Date(),userId);
		return blogGalleryDetail;
	}
	
	
	@Override
	public Optional<BlogGallery> findByPostId(Long postId) {
		
		Optional<BlogGallery> blogGallery = blogGalleryRepository.findByIsFinishedAndPostId(1, postId);
		return blogGallery;
	}
	
	@Override 
	public BlogGallery updateBlogGallery(BlogGallery blogGallery){
		
		return blogGalleryRepository.saveAndFlush(blogGallery);
	}
	
	
	@Override
	public List<BlogGalleryDetail> findByUserId(Long userId){
		
		Optional<BlogGallery> getBlogGallery = blogGalleryRepository.findByUserIdAndIsFinished(userId, 0);
		if(getBlogGallery.isPresent()) {
			List<BlogGalleryDetail> fileList =  blogGalleryDetailRepository.findByGalleryIdAndDeleted(getBlogGallery.get().getId(),0);
			return fileList;
		}
		else{
			return null;
		}
		 
	}
	
	@Override
	public List<BlogGalleryDetail> findByGalleryId(BlogGallery gallery){
		
		//System.out.println(galleryId);
		List<BlogGalleryDetail> fileList =  blogGalleryDetailRepository.findByGalleryIdAndDeleted(gallery.getId(),0);
		List<BlogGalleryDetail> return_data = fileList.stream()
											  .filter((n) ->{ 
												  String fileAddress = "resources/" + gallery.getUserId() + "/" + gallery.getGalleryName();
												  return !n.getOfilename().contains(fileAddress);
											  })
											  .map((n) -> {
												  BlogGalleryDetail new_temp = new BlogGalleryDetail();
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
	public BlogGalleryDetail storeToUserFolder(MultipartFile file, Long galleryId) {
		
		Optional<BlogGallery> blogGallery = blogGalleryRepository.findById(galleryId);
		
		Path userFilePath = storageService.getUserFolderLocation();
	
		String userGalleryFolder = userFilePath.toString() + "/" + blogGallery.get().getUserId() + "/" + blogGallery.get().getGalleryName();
//		System.out.println("Gallert Folder = "+ userGalleryFolder);
		
		//blogGallery.get().getGalleryName(); // no this gallery File
		
		String fileAddress = "resources/" + blogGallery.get().getUserId() + "/" + blogGallery.get().getGalleryName();
		
		Path userGalleryFolderPath = Paths.get(userGalleryFolder);
		String file_name = System.currentTimeMillis()+file.getOriginalFilename();
		
		storageService.storeBlogPhotoToLocation(file, userGalleryFolderPath, file_name);
		System.out.println("BlogGalleryServices 159 : File Original File name = "+ file.getOriginalFilename());
		
		//File Info to Database
	
//		String file_name = StringUtils.cleanPath(file.getOriginalFilename());
        String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
    	Md5Encode md5 = new Md5Encode();
        String filename = md5.getMD5(file_name)+fileType;
        String ofilename = md5.getMD5(file.getOriginalFilename())+fileType;
        
        
        BlogGalleryDetail new_record = new BlogGalleryDetail();
        new_record.setCreatedAt(new Date());
        new_record.setCreatedBy(blogGallery.get().getUserId());
        new_record.setGalleryId(blogGallery.get().getId());
        new_record.setLabel(fileAddress);
        new_record.setNfilename(filename);
        new_record.setOfilename(ofilename);
        new_record.setOrdering(0);
        
        return blogGalleryDetailRepository.saveAndFlush(new_record);
	}

	@Override
	public Optional<BlogGalleryDetail> findDetailById(Long detailId) {
		// TODO Auto-generated method stub
		return blogGalleryDetailRepository.findById(detailId);
	}

	@Override
	public BlogGalleryDetail saveDetail(BlogGalleryDetail detailId) {
		// TODO Auto-generated method stub
		return blogGalleryDetailRepository.saveAndFlush(detailId);
	}

	
}