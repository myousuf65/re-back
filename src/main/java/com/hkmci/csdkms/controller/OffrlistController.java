package com.hkmci.csdkms.controller;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.RanksModel;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.service.SeniorOfficeService;
import com.hkmci.csdkms.service.UserService;
import com.hkmci.csdkms.storage.FileCopy;
import com.hkmci.csdkms.storage.StorageException;
import com.hkmci.csdkms.storage.StorageService;
import com.hkmci.csdkms.storage.WaterMark;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Section;
import com.lowagie.text.html.WebColors;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;




@CrossOrigin
@RestController
@RequestMapping("/offrlist")
public class OffrlistController {

	
	private StorageService storageService;
	
	@Autowired
	@Resource
	private LogService logger;
	
	@Autowired
	@Resource
    private Common common;
	
	@Autowired
	@Resource
	private SeniorOfficeService seniorOfficeService;
	
	@Autowired
	@Resource
	private UserService userService;
	
	
	
	
		
	    public  void TRryPDF(Integer des) throws FileNotFoundException, IOException {
	    	Document document = new Document(PageSize.A4.rotate());
	    	Path resourcePath = Paths.get(System.getProperty("user.dir") + "/kms_resource/seniorOfficer/");
	    	PdfWriter writer =null;
	    	
	    	try {
	    		if (des ==1) {
	    			
	    			writer = PdfWriter.getInstance(document, new FileOutputStream(resourcePath+"/seniorOfficerStaff.pdf"));
	    		} else {
	    			writer = PdfWriter.getInstance(document, new FileOutputStream(resourcePath+"/seniorOfficerStaff(Civiliain).pdf"));
	    		}
				
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	 
	    	document.open();
	    	
	    	
	    	
	    
	    	  
	    	  
	    	PdfPTable table = new PdfPTable(6);
	    	table = new PdfPTable(new float[] { 3,3,8,3,6,6 });
	    	table.setWidthPercentage(100);
	    	addAsAtTableHeader(table);
	    	addTableHeader(table);
	    	addRows(table,des);
	    	try {
				addCustomRows(table);
				} catch (BadElementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	 
	    	try {
				document.add(table);
			
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	document.close();	 
	    
	    }
	
	    private void addAsAtTableHeader(PdfPTable table) {
	    	
	    	
	    	 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	    	   LocalDateTime now = LocalDateTime.now();
	    	   System.out.println(dtf.format(now));
	    	
	    	
	        Stream.of("","", "", "","As At : ",dtf.format(now))
	          .forEach(columnTitle -> {
	        	 // Color myColor = WebColors.getRGBColor("#90b8bd");

	            PdfPCell header = new PdfPCell();
	          //  header.setBackgroundColor(myColor);
	            header.setBorderWidth(1);
	            header.setPhrase(new Phrase(columnTitle));
	            table.addCell(header);
	        });
	    }
	    private void addTableHeader(PdfPTable table) {
	        Stream.of("","RANK", "NAME", "SECTION","DUTY","REMARK")
	          .forEach(columnTitle -> {
	        	  Color myColor = WebColors.getRGBColor("#90b8bd");

	            PdfPCell header = new PdfPCell();
	            header.setBackgroundColor(myColor);
	            header.setBorderWidth(1);
	            header.setPhrase(new Phrase(columnTitle));
	            table.addCell(header);
	        });
	    }
	    private void addISNTTableHeader(PdfPTable table,String inst) {
	        Stream.of(inst,"","","","","")
	          .forEach(columnTitle -> {
	        	Color myColor = WebColors.getRGBColor("#e2eef8");
	            PdfPCell header = new PdfPCell();
	            header.setBackgroundColor(myColor);
	            header.setBorderWidth(1);
	            header.setPhrase(new Phrase(columnTitle));
	            table.addCell(header);
	        });
	    }
	    private void addCustomRows(PdfPTable table) 
	    		  throws URISyntaxException, BadElementException, IOException {
	    		   
	    		}
	    @SuppressWarnings("rawtypes")
		private void addRows(PdfPTable table,Integer dif ) {
	    	List<Object > return_data = userService.getSeniorOfficerList(dif);
	    	String upInst ="";
	    	if (dif ==1 ) {
	    	for(Integer i = 0 ; i< return_data.size() ; i++) {
	    		String list = return_data.get(i).toString();
	    		String inst =null;
	    		inst = list.substring(list.indexOf("INST=") + 5);
	    		inst = inst.substring(0, inst.indexOf(","));
	    		
	    		
	    		if (upInst.contains(inst)) {
	    			
	    		} else {
	    			upInst=inst;
	    			addISNTTableHeader(table,inst);
	    		}
	    		
	    		
	    		//table.addCell(inst);
	    		String div = null;
	    		div = list.substring(list.indexOf("{DIV=") + 5);
	    		div = div.substring(0, div.indexOf(","));	    		
	    		table.addCell("");
	    		String rank = null;
			     rank = list.substring(list.indexOf("RANK=") + 5);
			     rank = rank.substring(0, rank.indexOf(","));
			     table.addCell(rank);
	    		String name = null;
	    		name = list.substring(list.indexOf("NAME= ") + 5);
	    		name = name.substring(0, name.indexOf("}"));
		        table.addCell(name);
		        String sect = null;
		        sect = list.substring(list.indexOf("SECT=") + 5);
		        sect = sect.substring(0, sect.indexOf(","));
		        table.addCell(sect);
		        String duty = null;
		        duty = list.substring(list.indexOf("DUTY=") + 5);
		        duty = duty.substring(0, duty.indexOf(","));
		        table.addCell(duty);
		        String remark = null;
		        remark = list.substring(list.indexOf("REMARK=") + 7);
		        remark = remark.substring(0, remark.indexOf(","));
		      
		        table.addCell(remark);
		        
	    	
	    	}
	    	}else {
	    		for(Integer i =0 ;i<return_data.size();i++) {
	    			String list = return_data.get(i).toString();
		    		String inst =null;
		    		inst = list.substring(list.indexOf("{DIV=") + 5);
		    		inst = inst.substring(0, inst.indexOf(","));
		    		
		    		
		    		if (upInst.contains(inst)) {
		    			
		    		} else {
		    			upInst=inst;
		    			addISNTTableHeader(table,inst +" DIVISION");
		    		}
		    		
		    		
		    		//table.addCell(inst);
		    		String div = null;
		    		div = list.substring(list.indexOf("{DIV=") + 5);
		    		div = div.substring(0, div.indexOf(","));	    		
		    		table.addCell("");
		    		String rank = null;
				     rank = list.substring(list.indexOf("RANK=") + 5);
				     rank = rank.substring(0, rank.indexOf(","));
				     table.addCell(rank);
		    		String name = null;
		    		name = list.substring(list.indexOf("NAME= ") + 5);
		    		name = name.substring(0, name.indexOf("}"));
			        table.addCell(name);
			        String sect = null;
			        sect = list.substring(list.indexOf("SECT=") + 5);
			        sect = sect.substring(0, sect.indexOf(","));
			        table.addCell(sect);
			        String duty = null;
			        duty = list.substring(list.indexOf("DUTY=") + 5);
			        duty = duty.substring(0, duty.indexOf(","));
			        table.addCell(duty);
			        String remark = null;
			        remark = list.substring(list.indexOf("REMARK=") + 7);
			        remark = remark.substring(0, remark.indexOf(","));
			      
			        table.addCell(remark);
			        
	    		}
	    		
	    	}
	    }
	    
	

	    
	    @RequestMapping("/{userId}/1")
		private ResponseEntity<JsonResult> getSenior(@PathVariable Long userId,HttpSession session){
			HashMap<String, String> user_check = common.checkUser(userId,session);
			if(user_check.get("msg") != "") {
				return JsonResult.errorMsg(user_check.get("msg").toString());
			}else{
				Object user_session = session.getAttribute("user_session");
				User user = (User) user_session;
				String staff_no = user.getStaffNo();
				Integer channel = (Integer) session.getAttribute("channel");
				System.out.println("See the channel = "+channel);
				
				try {
					TRryPDF(1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				@SuppressWarnings("unchecked")
				List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
				String rankName = rank_session.stream()
						  .filter((n) -> n.getId().equals(user.getRankId()))
						  .map(RanksModel::getRankName)
						  .collect(Collectors.toList())
						  .get(0);
				String PDFPath = null;
				try {
					 PDFPath = waterMark("seniorOfficerStaff.pdf","kms_resource/",staff_no,rankName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("User ="+ user + " channel = "+ channel);
				logger.viewSeniorOfficer(user, 1L, "seniorOfficerStaff.pdf", "success", channel);
				
				List <Object[]> return_list = seniorOfficeService.getSeniorOfficerList(1);
				
				
				
				
				 return JsonResult.ok(PDFPath,session);
				}
			
			}
		

	    
	    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/download/{userId}/1")   
	public ResponseEntity downloadSenior(@PathVariable Long userId,HttpSession session,HttpServletRequest request) throws IOException{
		//Object user_access_rule_session = session.getAttribute("user_access_rule_session");
		
		Object user_session = session.getAttribute("user_session");
		
		User user = (User) user_session;
		String staff_no = user.getStaffNo();
		Integer channel = (Integer) session.getAttribute("channel");
		System.out.println("See the channel = "+channel);
		
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			
			TRryPDF(1);
			Path resourcePath = Paths.get(System.getProperty("user.dir") + "/kms_resource");
			String file_path = null;
			file_path = resourcePath.toString() + "/seniorOfficer/watermark/" + staff_no + "/seniorOfficerStaff.pdf";
			System.out.println("File path: " + file_path);
			File file = new File(file_path);
			System.out.println("File: " + file);
			InputStream in = new FileInputStream(file);
			final HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/pdf");
			headers.add("Content-Disposition", "attachment; filename=seniorOfficerStaff.pdf");
			
			logger.downloadSeniorOfficer(user, 1L, "", "success", 1);
			return new ResponseEntity(IOUtils.toByteArray(in), headers, HttpStatus.OK);
			}
		
		}
	@RequestMapping("/{userId}/2")
	private ResponseEntity<JsonResult> getSeniorCiv(@PathVariable Long userId,HttpSession session){
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			String staff_no = user.getStaffNo();
			Integer channel = (Integer) session.getAttribute("channel");
			System.out.println("See the channel = "+channel);
			
			try {
				TRryPDF(2);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			@SuppressWarnings("unchecked")
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			String rankName = rank_session.stream()
					  .filter((n) -> n.getId().equals(user.getRankId()))
					  .map(RanksModel::getRankName)
					  .collect(Collectors.toList())
					  .get(0);
			String PDFPath = null;
			try {
				 PDFPath = waterMark("seniorOfficerStaff(Civiliain).pdf","kms_resource/",staff_no,rankName);
				 System.out.println("PDF path = "+ PDFPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("PDF path1  = "+ PDFPath);
			logger.viewSeniorOfficer(user, 2L , "", "success", 1);
			 return JsonResult.ok(PDFPath,session);
			}
		
		}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/download/{userId}/2")
	public ResponseEntity downloadSeniorCiv(@PathVariable Long userId,HttpSession session,HttpServletRequest request) throws IOException{
		//Object user_access_rule_session = session.getAttribute("user_access_rule_session");
		
		Object user_session = session.getAttribute("user_session");
		
		User user = (User) user_session;
		String staff_no = user.getStaffNo();
		Integer channel = (Integer) session.getAttribute("channel");
		System.out.println("See the channel = "+channel);
		
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			Path resourcePath = Paths.get(System.getProperty("user.dir") + "/kms_resource");
			String file_path = null;
			file_path = resourcePath.toString() + "/seniorOfficer/watermark/" + staff_no + "/seniorOfficerStaff(Civiliain).pdf";
			System.out.println("File path: " + file_path);
			File file = new File(file_path);
			System.out.println("File: " + file);
			InputStream in = new FileInputStream(file);
			System.out.println("Offrlist Controller : line 263: in = "+ in);
			final HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/pdf");
			headers.add("Content-Disposition", "attachment; filename=seniorOfficerStaff(Civiliain).pdf");
			logger.downloadSeniorOfficer(user, 2L, "", "success", 1);
			return new ResponseEntity(IOUtils.toByteArray(in), headers, HttpStatus.OK);
			}
		
		}
	
	private String waterMark(String Path ,String fileName,String staff_no, String rankName) throws IOException, DocumentException {
		WaterMark convertor = new WaterMark();
		
		//Copy Temp File
		String pureFileName = fileName.substring(fileName.lastIndexOf("/") + 1,fileName.length());
		
		System.out.println("resourcePath: " + Path);
		
		
		String original_fileName = fileName + "seniorOfficer/" + Path;
		System.out.println("original_fileName: " + original_fileName);
		
		String new_filePath = fileName+ "seniorOfficer/watermark/" + staff_no + "/";
		System.out.println("new_filePath: " + new_filePath);
		
		try {
            Files.createDirectories(Paths.get(new_filePath));
        }
        catch (IOException e) {
            throw new StorageException("Could not crate water mark folder", e);
        }
		String new_fileName = new_filePath + Path;
		System.out.println("new file name = "+new_fileName );
		System.out.println("Pure file = "+pureFileName);
		FileCopy.copy(original_fileName, new_fileName, Path);
		
		convertor.doWaterMark(new_fileName, original_fileName, rankName,staff_no);
		
		System.out.println("resources/seniorOfficer/watermark/" + staff_no+"/"+ Path);
		return "resources/seniorOfficer/watermark/" +staff_no+"/"+ Path;
		
		//return "resources/pdf/" + pdfFileName;
	}
}
