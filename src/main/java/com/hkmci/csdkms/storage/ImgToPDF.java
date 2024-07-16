package com.hkmci.csdkms.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

public class ImgToPDF {

	/**
	* Img2PDF
	* @param image
	* @return
	* @throws IOException 
	*/

	public boolean imgToPdf(String imgFilePath, String pdfFilePath)throws IOException {
		File file=new File(imgFilePath);
		if(file.exists()){
			Document document = new Document(PageSize.A4, 20, 20, 20, 20);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(pdfFilePath);
				PdfWriter.getInstance(document, fos);

				document.addAuthor("arui");
				document.addSubject("test pdf.");
				document.setPageSize(PageSize.A4);
				document.open();
				//document.add(new Paragraph("JUST TEST ..."));
				Image image = Image.getInstance(imgFilePath);
				float imageHeight=image.getScaledHeight();
				float imageWidth=image.getScaledWidth();
				int i=0;
				while(imageHeight>500||imageWidth>500){
					image.scalePercent(100-i);
					i++;
					imageHeight=image.getScaledHeight();
					imageWidth=image.getScaledWidth();
					//System.out.println("imageHeight->"+imageHeight);
					//System.out.println("imageWidth->"+imageWidth);
				}

				image.setAlignment(Image.ALIGN_CENTER); 
				// image.setAbsolutePosition(0, 0);
				// image.scaleAbsolute(500, 400);
				document.add(image);
			} catch (DocumentException de) {
				//System.out.println(de.getMessage());
			} catch (IOException ioe) {
				//System.out.println(ioe.getMessage());
			}
			document.close();
			fos.flush();
			fos.close();
			return true;
		}else{
			return false;
		}
	}
}
