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
import com.hkmci.csdkms.entity.ElearningGallery;
import com.hkmci.csdkms.entity.ElearningGalleryDetail;
import com.hkmci.csdkms.repository.ElearningGalleryDetailRepository;
import com.hkmci.csdkms.repository.ElearningGalleryRepository;
import com.hkmci.csdkms.storage.StorageException;
import com.hkmci.csdkms.storage.StorageService;




@Service
public class ElearningGalleryServiceImpl implements ElearningGalleryService {

	@Autowired
	@Resource
	private ElearningGalleryRepository elearningGalleryRepository;
	
	@Autowired
	@Resource
	private ElearningGalleryDetailRepository elearningGalleryDetailRepository;
	
	@Autowired
	@Resource
	private StorageService storageService;
	
	
	@Override
	public List<ElearningGallery> findAll() {
		return elearningGalleryRepository.findAll();
	}

	@Override
	public ElearningGallery newElearningGallery(ElearningGallery elearningGallery){
		Path userFilePath = storageService.getUserFolderLocation();
		
		//System.out.println(userFilePath);
		
		String userGalleryFolder = userFilePath.toString() + "/" + elearningGallery.getUserId() + "/" + elearningGallery.getGalleryName();
		Path userGalleryFolderPath = Paths.get(userGalleryFolder);
		
		try {
            Files.createDirectories(userGalleryFolderPath);
        }
        catch (IOException e) {
            throw new StorageException("Could not create user folder", e);
        }
		return elearningGalleryRepository.saveAndFlush(elearningGallery);
	}
	
	@Override
	public Optional<ElearningGallery> findById(Long id){
		return elearningGalleryRepository.findById(id);
	}
	
	@Override
	public Optional<ElearningGallery> findActive(Long userId) {
		
		Optional<ElearningGallery> elearningGallery = elearningGalleryRepository.findByUserIdAndIsFinished(userId,0);
		return elearningGallery;
	}
	
	@Override
	public Optional<ElearningGalleryDetail> findElearningGalleryDetail(Long detailId, Long userId) {
		
		Optional<ElearningGalleryDetail> elearningGalleryDetail = elearningGalleryRepository.deleteDetailById(detailId,new Date(),userId);
		return elearningGalleryDetail;
	}
	
	
	@Override
	public Optional<ElearningGallery> findByPostId(Long postId) {
		
		Optional<ElearningGallery> elearningGallery = elearningGalleryRepository.findByIsFinishedAndPostId(1, postId);
		return elearningGallery;
	}
	
	@Override 
	public ElearningGallery updateElearningGallery(ElearningGallery elearningGallery){
		
		return elearningGalleryRepository.saveAndFlush(elearningGallery);
	}
	
	
	@Override
	public List<ElearningGalleryDetail> findByUserId(Long userId){
		
		Optional<ElearningGallery> getElearningGallery = elearningGalleryRepository.findByUserIdAndIsFinished(userId, 0);
		if(getElearningGallery.isPresent()) {
			List<ElearningGalleryDetail> fileList =  elearningGalleryDetailRepository.findByGalleryIdAndDeleted(getElearningGallery.get().getId(),0);
			return fileList;
		}
		else{
			return null;
		}
		 
	}
	
	@Override
	public List<ElearningGalleryDetail> findByGalleryId(ElearningGallery gallery){
		
		//System.out.println(galleryId);
		List<ElearningGalleryDetail> fileList =  elearningGalleryDetailRepository.findByGalleryIdAndDeleted(gallery.getId(),0);
		List<ElearningGalleryDetail> return_data = fileList.stream()
											  .filter((n) ->{ 
												  String fileAddress = "resources/" + gallery.getUserId() + "/" + gallery.getGalleryName();
												  return !n.getOfilename().contains(fileAddress);
											  })
											  .map((n) -> {
												  ElearningGalleryDetail new_temp = new ElearningGalleryDetail();
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
	public ElearningGalleryDetail storeToUserFolder(MultipartFile file, Long galleryId) {
		
		Optional<ElearningGallery> elearningGallery = elearningGalleryRepository.findById(galleryId);
		
		Path userFilePath = storageService.getUserFolderLocation();
	
		String userGalleryFolder = userFilePath.toString() + "/" + elearningGallery.get().getUserId() + "/" + elearningGallery.get().getGalleryName();
//		System.out.println("Gallert Folder = "+ userGalleryFolder);
		
		//elearningGallery.get().getGalleryName(); // no this gallery File
		
		String fileAddress = "resources/" + elearningGallery.get().getUserId() + "/" + elearningGallery.get().getGalleryName();
		
		Path userGalleryFolderPath = Paths.get(userGalleryFolder);
		String file_name = System.currentTimeMillis()+file.getOriginalFilename();
		
		storageService.storeElearningPhotoToLocation(file, userGalleryFolderPath, file_name);
		System.out.println("ElearningGalleryServices 159 : File Original File name = "+ file.getOriginalFilename());
		
		//File Info to Database
	
//		String file_name = StringUtils.cleanPath(file.getOriginalFilename());
        String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
    	Md5Encode md5 = new Md5Encode();
        String filename = md5.getMD5(file_name)+fileType;
        String ofilename = md5.getMD5(file.getOriginalFilename())+fileType;
        
        
        ElearningGalleryDetail new_record = new ElearningGalleryDetail();
        new_record.setCreatedAt(new Date());
        new_record.setCreatedBy(elearningGallery.get().getUserId());
        new_record.setGalleryId(elearningGallery.get().getId());
        new_record.setLabel(fileAddress);
        new_record.setNfilename(filename);
        new_record.setOfilename(ofilename);
        new_record.setOrdering(0);
        
        return elearningGalleryDetailRepository.saveAndFlush(new_record);
	}

	@Override
	public Optional<ElearningGalleryDetail> findDetailById(Long detailId) {
		// TODO Auto-generated method stub
		return elearningGalleryDetailRepository.findById(detailId);
	}

	@Override
	public ElearningGalleryDetail saveDetail(ElearningGalleryDetail detailId) {
		// TODO Auto-generated method stub
		return elearningGalleryDetailRepository.saveAndFlush(detailId);
	}

	
}