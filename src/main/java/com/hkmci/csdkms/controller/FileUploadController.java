package com.hkmci.csdkms.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hkmci.csdkms.storage.FileCopy;
import com.hkmci.csdkms.storage.StorageFileNotFoundException;
import com.hkmci.csdkms.storage.StorageService;

@CrossOrigin
@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    
    @GetMapping("/market")
    public String listMarketFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadMarketAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "/thymeleaf/uploadForm";
        //return "redirect:http://localhost:3000/#/";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    
    @GetMapping("/upload/move/{location}")
    @ResponseBody
    public ResponseEntity<JsonResult> mobeFile(@PathVariable String location,HttpSession session) {
    	//String old_file_path = storageService.loadAll().toString();
    	
    	//Object[] temp_files = storageService.loadAll().toArray();
    	//Stream temp_file = storageService.load("86dc9ae9265e1eed7fd1258c8b60d0ab.png");
    	
    	List<String> files = storageService.loadAll().map(Path::toString).collect(Collectors.toList());
    	
		//String new_file_path = old_file_path.replace("upload-dir", "upload-market");
		
		for(int i = 0; i < files.size(); i++) {
			FileCopy.copy(
					storageService.load(files.get(i)).toString(), 
					storageService.load(files.get(i)).toString().replace("upload-dir", "kms-resource"),
					files.get(i));
			
		}
	
		
    	return JsonResult.ok(files,session);
    }
    
    @PostMapping("/upload/videoImg/")
    @ResponseBody
    public ResponseEntity<JsonResult> handleVideoImgUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes,HttpSession session) {
    	  
    	System.out.println("Upload single = "+ file);
        storageService.store(file);

        final String nfileType;
        final String nfilename;
        final String originalname;
        final String filesize;
        
        String file_name = StringUtils.cleanPath(file.getOriginalFilename()) ;
        System.out.println("File Name = "+file_name);
        
        String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
        
        Md5Encode md5 = new Md5Encode();
        //nfilename = md5.getMD5(file_name)+fileType;
        nfilename = md5.getMD5(file_name)+fileType;
        
        nfileType = fileType;
        originalname = file_name;
        filesize = String.valueOf(file.getSize());
        
        //Original Checking
        
        HashMap<String, String > return_file_data = new HashMap<String, String>(){
        	/**
			 * 
			 */
			private static final long serialVersionUID = -575551168712619111L;

			{
				put("nfilename",nfilename); 
				put("ofilename",originalname); 
				put("originalname",originalname); 
				put("filetype",nfileType); 
				put("filesize",filesize); 
			}
		};
		
		//Copy to another folder.
		System.out.println("New file name = "+return_file_data.get("nfilename"));
		
        return JsonResult.ok(return_file_data,session);
    }
    
    
    @PostMapping("/upload/single")
    @ResponseBody
    public ResponseEntity<JsonResult> handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes,HttpSession session) {
    	System.out.println("Upload single = "+ file);
        storageService.store(file);

        final String nfileType;
        final String nfilename;
        final String originalname;
        final String filesize;
        
        String time = new Date().toString();
        String file_name = StringUtils.cleanPath(file.getOriginalFilename()) ;
        System.out.println("File Name = "+file_name);
        
        String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
        
        Md5Encode md5 = new Md5Encode();
        //nfilename = md5.getMD5(file_name)+fileType;
        nfilename = md5.getMD5(file_name)+fileType;
        
        nfileType = fileType;
        originalname = file_name;
        filesize = String.valueOf(file.getSize());
        
        //Original Checking
        
        HashMap<String, String > return_file_data = new HashMap<String, String>(){
        	/**
			 * 
			 */
			private static final long serialVersionUID = -575551168712619111L;

			{
				put("nfilename",nfilename); 
				put("ofilename",originalname); 
				put("originalname",originalname); 
				put("filetype",nfileType); 
				put("filesize",filesize); 
			}
		};
		
		//Copy to another folder.
		System.out.println("New file name = "+return_file_data.get("nfilename"));
		
        return JsonResult.ok(return_file_data,session);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
