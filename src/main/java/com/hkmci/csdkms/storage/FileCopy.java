package com.hkmci.csdkms.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.aspose.pdf.ConvertErrorAction;
import com.aspose.pdf.Document;
import com.aspose.pdf.PdfFormat;
import com.aspose.pdf.internal.imaging.system.io.MemoryStream;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

public class FileCopy {
	
	public static void copy(String oldPath, String newPath, String filename) {
		try {
			//int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
//			
//			 PdfDocument document = new PdfDocument();
////	        document.loadFromFile(input);
//	        document.loadFromFile(oldPath);
//	        document.getFileInfo().setVersion(PdfVersion.Version_1_7);
//	        document.saveToFile(oldPath, FileFormat.PDF);
//	        document.close();
//	        System.out.println("FileCopy ==   line 26: Change it to Version 1_7");
//			
		
			
			
//			doc.convert(new MemoryStream(), PdfFormat.v_1_7, ConvertErrorAction.Delete);
//			doc.save(newPath);
			
			if (oldfile.exists()) { //File Esist
				InputStream inStream = new FileInputStream(oldPath); //Read file in Stream
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ( (byteread = inStream.read(buffer)) != -1) {
					//bytesum += byteread; //bytes file size
					//System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		}
		catch (Exception e) {
		
			e.printStackTrace();
		}
	} 
	
}
