package com.hkmci.csdkms.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.jodconverter.DocumentConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeUtils;
import org.springframework.beans.factory.annotation.Autowired;
 
/**
 * @author lbh
 */
 
public class OfficeWithLibre {
	@Autowired
	@Resource
	private DocumentConverter documentConverter;
	
	public void Word2Pdf(String srcPath, String desPath) throws IOException {
	    // Source Path
	    File inputFile = new File(srcPath);
	    if (!inputFile.exists()) {
	        System.out.println("Source Path Not Exist！");
	        return;
	    }
	    // Desteantion Path
	    File outputFile = new File(desPath);
	    if (!outputFile.getParentFile().exists()) {
	        outputFile.getParentFile().exists();
	    }
	    // Connect OpenOffice/LibreOffice Service
//	    OfficeManager officeManager = LocalOfficeManager.builder().officeHome("/Applications/LibreOffice.app/Contents").build();
	    try {
	        //officeManager.start();
	        // Convert to PDF
	        long time = System.currentTimeMillis();
	        //JodConverter.convert(inputFile).to(outputFile).execute();
	        documentConverter.convert(inputFile).to(outputFile).execute();
	        System.out.println("File：{}Convert to PDF：{} Done，Time Spend {} mm！" + String.valueOf(System.currentTimeMillis() - time));
	    } catch (OfficeException e) {
	        e.printStackTrace();
	        System.out.println("File：{} Convert to PDF：{} Failed！");
	    } finally {
	        // Close Connection
	        OfficeUtils.stopQuietly(null);
	    }
	}

	public byte[] docxToPDF(InputStream inputStream) {
	    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
	        documentConverter
	                .convert(inputStream)
	                .as(DefaultDocumentFormatRegistry.DOCX)
	                .to(byteArrayOutputStream)
	                .as(DefaultDocumentFormatRegistry.PDF)
	                .execute();
	        return byteArrayOutputStream.toByteArray();
	    } catch (OfficeException | IOException e) {
	        System.out.println("convert pdf error");
	    }
	    return null;
	}
	
}