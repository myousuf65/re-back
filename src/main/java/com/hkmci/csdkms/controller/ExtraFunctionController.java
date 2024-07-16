package com.hkmci.csdkms.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkmci.csdkms.common.OfficeWithOldVersion;
import com.hkmci.csdkms.common.PDF2IMG;
import com.hkmci.csdkms.entity.FileResource;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.service.ResourceService;
import com.hkmci.csdkms.service.UserService;
import com.hkmci.csdkms.storage.FileCopy;
import com.hkmci.csdkms.storage.StorageException;
import com.hkmci.csdkms.storage.StorageService;
import com.lowagie.text.DocumentException;

@CrossOrigin
@RestController
@RequestMapping("/functions")
public class ExtraFunctionController {
	
	@Autowired
	@Resource
	private StorageService storageService;
	
	@Autowired
	@Resource
	private UserService userService;
	
	@Autowired
	@Resource 
	private ResourceService resourceService;
	
	
	@RequestMapping("/genThumb/{folder_name}")
	public ResponseEntity<JsonResult> genThumb(@PathVariable Integer folder_name,HttpSession session) throws Exception{
		String folderName = folder_name.toString();
		Path resourcePath = storageService.getResourceLocation();
		Path targetPath = Paths.get( resourcePath.toString() + "/pdf/" + folderName );
		//System.out.println("targetPath: " + targetPath);
		//List<String> return_data = new ArrayList<String>();
		List<Long> resource_ids = new ArrayList<Long>();
		Integer startNum = (folder_name - 1000) + 1;
		for(Integer id = startNum; id < startNum + 1000; id++) {
			resource_ids.add(Long.parseLong(id.toString()));
		}
		List<FileResource> fileData = resourceService.findByIds(resource_ids);
		List<FileResource> toUpdate = new ArrayList<FileResource>();
		try {
            Stream<Path> files =  Files.walk(targetPath, 1)
                .filter(path -> !path.equals(targetPath))
                .map(targetPath::relativize);
            List<String> files_list = files.filter(
            			(f) -> !f.toString().substring(0,1).equals(".") && 
            					f.toString().substring(f.toString().lastIndexOf(".") + 1,f.toString().length()).toLowerCase().equals("pdf") 
            		).map(
            			(f) -> {return f.toString();}
            		).collect(Collectors.toList());
            System.out.println("files_list: " + files_list);
            for(Integer i = 0; i < files_list.size(); i++) {
            	String thumbnail = "resources/pdf/" + folderName + "/" + pdf2LocalImg(StringUtils.cleanPath(files_list.get(i)),0,"png",folderName);
            	String[] namePatern = files_list.get(i).toString().split("_");
            	String resource_id = namePatern[0];
            	FileResource resource_filtered = fileData.stream().filter(
            				(r) -> r.getId().toString().equals(resource_id)
            			).collect(Collectors.toList()).get(0);
            	resource_filtered.setThumbnail(thumbnail);
            	resourceService.saveSingleThumbs(resource_filtered);
            	toUpdate.add(resource_filtered);
            	//return_data.add(thumbnail);
            }
            //resourceService.saveAllThumbs(toUpdate);
            return JsonResult.ok(toUpdate.stream().map(
            			(r) -> {return r.getThumbnail();}
            		).collect(Collectors.toList()),session);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
	}
	
	@RequestMapping("/convertOthers/{folder_name}")
	public ResponseEntity<JsonResult> convertOthers(@PathVariable Integer folder_name, HttpSession session) throws Exception{
		User user = userService.findByStaffNo("12240");
		session.setAttribute("user_session",user);
		List<FileResource> fileList = getFileList(folder_name);
		
		Path resourcePath = storageService.getResourceLocation();
		Path targetPath = Paths.get( resourcePath.toString() + "/pdf/" + folder_name);
		
		System.out.println("FileList got size: " + fileList.size());
		if( fileList == null || fileList.size() == 0 ) {
			return JsonResult.ok(null,session);
		}else {
			System.out.println("File 1: " + fileList.get(0));
			List<FileResource> resources = new ArrayList<>();
			for(Integer i = 0; i < fileList.size(); i++){
				Path file = Paths.get( resourcePath.toString() + "/pdf/" + folder_name + "\\" + fileList.get(i).getNfilename());
				FileResource afterConvert = new FileResource();
				try {
					afterConvert = convertToPDF(file, fileList.get(i), fileList.get(i).getNfilename(), folder_name.toString());
					resources.add(afterConvert);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return JsonResult.ok(resources,session);
		}
	}
	
	private List<FileResource> getFileList (Integer folder_name) throws Exception{
		String folderName = folder_name.toString();
		Path resourcePath = storageService.getResourceLocation();
		Path targetPath = Paths.get( resourcePath.toString() + "/pdf/" + folderName );
		//System.out.println("targetPath: " + targetPath);
		//List<String> return_data = new ArrayList<String>();
		List<Long> resource_ids = new ArrayList<Long>();
		Integer startNum = (folder_name - 1000);
		Integer endNum = folder_name + 1;
		for(Integer id = startNum; id < startNum + 1000; id++) {
			resource_ids.add(Long.parseLong(id.toString()));
		}
		List<FileResource> fileData = resourceService.findByIdRange(startNum,endNum);
		List<FileResource> toUpdate = new ArrayList<FileResource>();
		try {
            Stream<Path> files =  Files.walk(targetPath, 1)
                .filter(path -> !path.equals(targetPath))
                .map(targetPath::relativize);
            List<String> files_list = files.filter(
            			(f) -> !f.toString().substring(0,1).equals(".") && 
            				   !f.toString().substring(f.toString().lastIndexOf(".") + 1,f.toString().length()).toLowerCase().equals("pdf")  &&
            				   !f.toString().substring(f.toString().lastIndexOf(".") + 1,f.toString().length()).toLowerCase().equals("png")  &&
            				   !f.toString().substring(f.toString().lastIndexOf(".") + 1,f.toString().length()).toLowerCase().equals("jpg")  &&
            				   !f.toString().substring(f.toString().lastIndexOf(".") + 1,f.toString().length()).toLowerCase().equals("exe")  &&
            				   !f.toString().substring(f.toString().lastIndexOf(".") + 1,f.toString().length()).toLowerCase().equals("zip")  &&
            				   !f.toString().substring(f.toString().lastIndexOf(".") + 1,f.toString().length()).toLowerCase().equals("rar")  &&
            				   !f.toString().substring(f.toString().lastIndexOf(".") + 1,f.toString().length()).toLowerCase().equals("gif")  &&
            				   !f.toString().substring(f.toString().lastIndexOf(".") + 1,f.toString().length()).toLowerCase().equals("html")  &&
            				   !f.toString().substring(f.toString().lastIndexOf(".") + 1,f.toString().length()).toLowerCase().equals("htm")  
            		).map(
            			(f) -> {return f.toString();}
            		).collect(Collectors.toList());
            System.out.println("files_list: " + files_list);
            if(files_list == null || files_list.size() == 0) {
            	return toUpdate;
            }else {
            	for(Integer i = 0; i < files_list.size(); i++) {
                	//String thumbnail = "resources/pdf/" + folderName + "/" + pdf2LocalImg(StringUtils.cleanPath(files_list.get(i)),0,"png",folderName);
                	String[] namePatern = files_list.get(i).toString().split("_");
                	String resource_id = namePatern[0];
                	FileResource resource_filtered = fileData.stream().filter(
                				(r) -> r.getId().toString().equals(resource_id)
                			).collect(Collectors.toList()).get(0);
                	//resource_filtered.setThumbnail(thumbnail);
                	//resourceService.saveSingleThumbs(resource_filtered);
                	toUpdate.add(resource_filtered);
                	//return_data.add(thumbnail);
                }
            	return toUpdate;
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
	}
	
	private FileResource convertToPDF(Path files, FileResource resource,String file_name,String folder_name) throws IOException,DocumentException {
		Integer file_id = Integer.parseInt(String.valueOf(resource.getId()));
		String fileType = file_name.substring(file_name.lastIndexOf(".") + 1,file_name.length()).toLowerCase();
		String file_path_in_db = storageService.getResourceFolderName() + "/pdf/" + folder_name;
		System.out.println("--  Microsoft  to PDF -----");
		//pdfUseCMD
//		FileCopy.copy(
//				files.toString(), 
//				files.toString().replace(storageService.getTempFolderName() + "\\",
//						storageService.getResourceFolderName() + "\\" + file_id + "_"
//				),//"upload-dir", "kms-resource/pdf"
//				files.toString());
		String pdfFile = pdfUseCMD(file_name,fileType,folder_name);
		System.out.println("--  After pdfUserCMD ---   ");
		if(pdfFile == "") {
			return new FileResource();//JsonResult.errorMsg("File Convert Error");
		}
		
		String thumbnail = "resources/" + pdf2Img(pdfFile,0,"png",folder_name);
		resource.setFilepath(file_path_in_db);
		resource.setNfilename(pdfFile);
		resource.setThumbnail(thumbnail);
		System.out.println("File path for document :  "+ resource);
		return resource;
		
	}
	
	private String pdf2Img(String filename,Integer indexOfStart,String type, String folderName) {
		Path resourcePath = storageService.getResourceLocation();
		return PDF2IMG.pdf2png(resourcePath.toString(), filename, indexOfStart, type, folderName);
	}
	
	private String pdf2LocalImg(String filename,Integer indexOfStart,String type, String folderName) throws IOException, Exception {
		Path resourcePath = storageService.getResourceLocation();
		return PDF2IMG.pdf2LocalPng(resourcePath.toString(), filename, indexOfStart, type, folderName);
	}
	
	private String pdfUseCMD(String FileName, String fileType, String folderName) throws IOException, DocumentException {
    	String pdfFileName = FileName.replace(fileType, "pdf");
    	Path resourcePath = storageService.getResourceLocation();
		String FilePath = resourcePath.toString() + "/pdf/" + folderName + "/" + FileName;
		String pdfFilePath = resourcePath.toString() + "/pdf/" + folderName + "/" + pdfFileName;
    	
		//Method three -- Use old tools (Only windows version with office)
		OfficeWithOldVersion converter = new OfficeWithOldVersion();
		Integer result = converter.runCMD(FilePath, pdfFilePath);
		if(result == 1) {
			return pdfFileName;
		}else {
			return "";
		}
		
    }
}
